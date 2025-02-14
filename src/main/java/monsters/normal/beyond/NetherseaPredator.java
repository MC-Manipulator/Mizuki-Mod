package monsters.normal.beyond;

import Impairment.NervousImpairment;
import actions.ApplyImpairmentAction;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import monsters.AbstractMizukiMonster;

import powers.PhantomOfNetherseaPower;

public class NetherseaPredator extends AbstractMizukiMonster
{
    //深溟猎食者
    public static final String ID = "Mizuki:NetherseaPredator";

    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Mizuki:NetherseaPredator");

    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    public NetherseaPredator(float x, float y)
    {

        super(NAME, "Mizuki:NetherseaPredator", 90, 0.0F, 0.0F, 150.0F, 320.0F, null, x, y);
        int dmg1, dmg2;
        this.type = AbstractMonster.EnemyType.NORMAL;



        if (AbstractDungeon.ascensionLevel >= 7)
        {
            setHp(62, 68);
        }
        else
        {
            setHp(54, 60);
        }

        if (AbstractDungeon.ascensionLevel >= 17)
        {
            dmg2 = 16;
            dmg1 = 12;
        }
        else if (AbstractDungeon.ascensionLevel >= 2)
        {
            dmg2 = 16;
            dmg1 = 12;
        }
        else
        {
            dmg2 = 16;
            dmg1 = 10;
        }

        this.damage.add(new DamageInfo((AbstractCreature) this, MathUtils.floor(dmg1)));
        this.damage.add(new DamageInfo((AbstractCreature) this, MathUtils.floor(dmg2)));

        loadAnimation("resources/img/monster/NetherseaPredator/enemy_1231_dsrunr.atlas", "resources/img/monster/NetherseaPredator/enemy_1231_dsrunr.json", 1.8F);
        this.stateData.setMix("Idle", "Die", 0.1F);
        this.flipHorizontal = true;
        this.state.addAnimation(0, "Idle", true, 0.0F);
        this.nextMove = 0;
    }

    @Override
    public void usePreBattleAction()
    {
        //深溟幻形PhantomOfNethersea
        AbstractDungeon.actionManager.addToBottom(
                (AbstractGameAction)new ApplyPowerAction((AbstractCreature)this, (AbstractCreature)this,
                        (AbstractPower)new PhantomOfNetherseaPower((AbstractCreature)this, -1)));
    }

    public void takeTurn()
    {
        AbstractPlayer p = AbstractDungeon.player;

        switch (this.nextMove)
        {
            case 0:
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "ATTACK"));
                addToBot((AbstractGameAction) new DamageAction((AbstractCreature)p, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                addToBot(new ApplyImpairmentAction(new NervousImpairment(), p, 1));
                break;
            case 1:
                AbstractMonster self = this;
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "ATTACK"));
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
                            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AttackEffect.SLASH_HORIZONTAL));
                        tickDuration();
                        if (this.isDone)
                        {
                            this.target.damage(damage.get(1));
                            if (this.target.lastDamageTaken > 0)
                            {
                                addToBot(new GainBlockAction(self, this.target.lastDamageTaken));
                                addToTop((AbstractGameAction)new WaitAction(0.1F));
                            }
                            if ((AbstractDungeon.getCurrRoom()).monsters.areMonstersBasicallyDead())
                                AbstractDungeon.actionManager.clearPostCombatActions();
                        }
                    }
                });
                break;
            case 2:
                AbstractDungeon.actionManager.addToBottom(
                        (AbstractGameAction)new ApplyPowerAction((AbstractCreature)this, (AbstractCreature)this,
                                (AbstractPower)new ArtifactPower((AbstractCreature)this, 2), 2));
                break;
        }
        getMove(0);
    }

    protected void getMove(int i)
    {
        int roll = AbstractDungeon.aiRng.random(99);
        if (roll < 33)
        {
            setMove(MOVES[0], (byte)0, Intent.ATTACK_DEBUFF, ((DamageInfo)this.damage.get(0)).base);
        }
        else if (roll < 66)
        {
            setMove(MOVES[1], (byte)1, Intent.ATTACK_DEFEND, ((DamageInfo)this.damage.get(1)).base);
        }
        else
        {
            setMove(MOVES[2], (byte)2, Intent.DEBUFF);
        }
    }


    public void changeState(String stateName)
    {
        if (stateName.equals("ATTACK"))
        {
            this.state.setAnimation(0, "Attack", false);
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
