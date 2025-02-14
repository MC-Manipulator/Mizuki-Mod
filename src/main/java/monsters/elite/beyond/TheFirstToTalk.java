package monsters.elite.beyond;

import Impairment.NervousImpairment;
import actions.ApplyImpairmentAction;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import helper.RejectionHelper;
import modcore.MizukiModCore;
import monsters.AbstractMizukiMonster;
import powers.AbstractMizukiPower;
import powers.PocketSeaStingPower;

public class TheFirstToTalk extends AbstractMizukiMonster
{
    //首言者
    public static final String ID = "Mizuki:TheFirstToTalk";

    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Mizuki:TheFirstToTalk");

    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    private boolean firstTurn;

    private boolean hasStrengthened = false;
    private boolean hasGrown = false;
    public int multiplier;

    public int strengthCount = 2;

    public int platedArmourAmount;

    public int artifactAmount = 2;

    public int curseAmount = 3;

    private int possibility = 0;

    public TheFirstToTalk(float x, float y)
    {

        super(NAME, "Mizuki:TheFirstToTalk", 90, 0.0F, 0.0F, 250.0F, 350.0F, null, x, y);
        int dmg1, dmg2;
        this.type = AbstractMonster.EnemyType.NORMAL;



        if (AbstractDungeon.ascensionLevel >= 8)
        {
            setHp(220, 228);
        }
        else
        {
            setHp(202, 210);
        }

        if (AbstractDungeon.ascensionLevel >= 18)
        {
            curseAmount = 4;
            dmg1 = 7;
        }
        else if (AbstractDungeon.ascensionLevel >= 3)
        {
            curseAmount = 3;
            dmg1 = 7;
        }
        else
        {
            curseAmount = 3;
            dmg1 = 6;
        }

        this.damage.add(new DamageInfo((AbstractCreature) this, MathUtils.floor(dmg1)));

        this.multiplier = 3;

        loadAnimation("resources/img/monster/TheFirstToTalk/enemy_1154_dsmant.atlas", "resources/img/monster/TheFirstToTalk/enemy_1154_dsmant.json", 1.2F);
        this.stateData.setMix("Idle", "Die", 0.1F);
        this.flipHorizontal = true;
        this.state.addAnimation(0, "Idle", true, 0.0F);
        this.nextMove = 0;
        this.firstTurn = true;
        this.hasStrengthened = false;
        this.strengthCount = 2;
        this.platedArmourAmount = 6;
        this.possibility = 0;
    }

    public void takeTurn()
    {
        AbstractPlayer p = AbstractDungeon.player;

        switch (this.nextMove)
        {
            case 0:
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "DEBUFF"));
                for (int i = 0;i < curseAmount;i++)
                {
                    AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new MakeTempCardInDiscardAction((AbstractCard) RejectionHelper.getRandomRejection(), 1));
                }
                break;
            case 1:
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "STRENGTHEN"));
                addToBot((AbstractGameAction)new ApplyPowerAction(this, this,
                        (AbstractPower)new ArtifactPower(this, this.artifactAmount), this.artifactAmount));
                hasStrengthened = true;
                break;
            case 2:
                for (int i = 0;i < multiplier;i++)
                {
                    //AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "ATTACK"));
                    addToBot(new AbstractGameAction()
                    {
                        boolean justIn = true;

                        @Override
                        public void update()
                        {
                            this.target = p;
                            if (justIn)
                            {
                                this.duration = 0.5F;
                                justIn = false;
                            }
                            if (this.duration == 0.5F)
                                AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AttackEffect.BLUNT_HEAVY));
                            tickDuration();
                            if (this.isDone)
                            {
                                this.target.damage(damage.get(0));
                                addToBot(new ApplyImpairmentAction(new NervousImpairment(), p, 2));
                                addToTop((AbstractGameAction)new WaitAction(0.1F));
                                if ((AbstractDungeon.getCurrRoom()).monsters.areMonstersBasicallyDead())
                                    AbstractDungeon.actionManager.clearPostCombatActions();
                            }
                        }
                    });
                    //addToBot((AbstractGameAction) new DamageAction((AbstractCreature)p, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                }

                AbstractMizukiMonster m = this;

                addToBot(new AbstractGameAction()
                {
                    @Override
                    public void update()
                    {
                        AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(m, "END"));
                        isDone = true;
                    }
                });
                hasStrengthened = false;
                break;
            case 3:
                addToBot((AbstractGameAction)new ApplyPowerAction(this, this,
                        (AbstractPower)new PlatedArmorPower(this, this.platedArmourAmount), this.platedArmourAmount));
                addToBot(new ApplyPowerAction(this, this,
                        new StrengthPower(this, strengthCount), strengthCount));
                hasGrown = true;
                break;
        }
        getMove(0);
    }



    protected void getMove(int i)
    {
        if (firstTurn)
        {
            int roll = MathUtils.random(99);
            if (roll < 50)
            {
                setMove(MOVES[0], (byte)0, Intent.STRONG_DEBUFF);
            }
            else if (roll < 65)
            {
                setMove(MOVES[1], (byte)1, Intent.UNKNOWN);
            }
            else
            {
                setMove(MOVES[3], (byte)3, Intent.BUFF);
            }
            firstTurn = false;
            return;
        }
        if (hasStrengthened)
        {
            setMove(MOVES[2], (byte)2, Intent.ATTACK_DEBUFF, ((DamageInfo)this.damage.get(0)).base, multiplier, true);
            return;
        }
        if (hasGrown || (this.hasPower(PlatedArmorPower.POWER_ID) && this.getPower(PlatedArmorPower.POWER_ID).amount > 3))
        {
            hasGrown = false;
            int roll = MathUtils.random(99);
            if (roll < 40)
            {
                setMove(MOVES[0], (byte)0, Intent.STRONG_DEBUFF);
            }
            else
            {
                setMove(MOVES[1], (byte)1, Intent.UNKNOWN);
            }
            return;
        }
        int roll = MathUtils.random(99);
        if (roll < 15 - possibility * 5)
        {
            setMove(MOVES[0], (byte)0, Intent.STRONG_DEBUFF);
        }
        else if (roll < 45 + possibility * 6)
        {
            setMove(MOVES[1], (byte)1, Intent.UNKNOWN);
        }
        else
        {
            setMove(MOVES[3], (byte)3, Intent.BUFF);
        }
    }


    public void changeState(String stateName)
    {
        if (stateName.equals("DEBUFF"))
        {
            this.state.setAnimation(0, "Attack_1", false);
            this.state.addAnimation(0, "Idle", true, 0.0F);
        }
        if (stateName.equals("ATTACK"))
        {
            this.state.setAnimation(0, "Attack_2", false);
            this.state.addAnimation(0, "Idle", true, 0.0F);
        }
        if (stateName.equals("STRENGTHEN"))
        {
            this.state.setAnimation(0, "Skill_Begin", false);
            this.state.addAnimation(0, "Skill_Loop", true, 0.0F);
        }
        if (stateName.equals("END"))
        {
            this.state.setAnimation(0, "Skill_End", false);
            this.state.addAnimation(0, "Idle", true, 0.0F);
        }
    }

    public void die()
    {
        this.state.setAnimation(0, "Die", false);
        addToBot(new AbstractGameAction()
        {
            @Override
            public void update()
            {
                trueDie();
                this.isDone = true;
            }
        });
    }

    public void trueDie()
    {
        super.die();
    }
}
