package monsters.normal.exordium;

import Impairment.NervousImpairment;
import actions.ApplyImpairmentAction;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import monsters.AbstractMizukiMonster;

public class DeepSeaSlider extends AbstractMizukiMonster
{
    //底海滑动者
    public static final String ID = "Mizuki:DeepSeaSlider";

    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Mizuki:DeepSeaSlider");

    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    private int turns;
    private boolean hasDebuffed;
    public int multiplier;

    public DeepSeaSlider(float x, float y)
    {

        super(NAME, "Mizuki:DeepSeaSlider", 90, 0.0F, 0.0F, 150.0F, 320.0F, null, x, y);
        int dmg1, dmg2;
        this.type = AbstractMonster.EnemyType.NORMAL;



        if (AbstractDungeon.ascensionLevel >= 7)
        {
            setHp(48, 52);
        }
        else
        {
            setHp(40, 44);
        }

        if (AbstractDungeon.ascensionLevel >= 17)
        {
            this.multiplier = 6;
            dmg2 = 1;
            dmg1 = 8;
        }
        else if (AbstractDungeon.ascensionLevel >= 2)
        {
            this.multiplier = 6;
            dmg2 = 1;
            dmg1 = 7;
        }
        else
        {
            this.multiplier = 5;
            dmg2 = 1;
            dmg1 = 7;
        }

        this.damage.add(new DamageInfo((AbstractCreature) this, MathUtils.floor(dmg1)));
        this.damage.add(new DamageInfo((AbstractCreature) this, MathUtils.floor(dmg2)));

        loadAnimation("resources/img/monster/DeepSeaSlider/enemy_1148_dssbr.atlas", "resources/img/monster/DeepSeaSlider/enemy_1148_dssbr.json", 1.8F);
        this.stateData.setMix("Idle", "Die", 0.1F);
        this.flipHorizontal = true;
        this.state.addAnimation(0, "Idle", true, 0.0F);
        this.nextMove = 0;
        this.turns = 0;
        this.hasDebuffed = false;
    }

    public void takeTurn()
    {
        AbstractPlayer p = AbstractDungeon.player;

        switch (this.nextMove)
        {
            case 0:
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "ATTACK"));
                addToBot((AbstractGameAction) new DamageAction((AbstractCreature)p, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                addToBot(new ApplyImpairmentAction(new NervousImpairment(), p, 2));
                break;
            case 1:
                for (int i = 0;i < multiplier;i++)
                {
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
                            tickDuration();
                            if (this.isDone)
                            {
                                AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AttackEffect.BLUNT_LIGHT));
                                this.target.damage(damage.get(1));
                                if (this.target.lastDamageTaken > 0)
                                {
                                    addToTop(new ApplyImpairmentAction(new NervousImpairment(), p, 1));
                                    addToTop((AbstractGameAction)new WaitAction(0.1F));
                                }
                                if ((AbstractDungeon.getCurrRoom()).monsters.areMonstersBasicallyDead())
                                    AbstractDungeon.actionManager.clearPostCombatActions();
                            }
                        }
                    });
                    //addToBot((AbstractGameAction) new DamageAction((AbstractCreature)p, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                }
                break;
            case 2:
                hasDebuffed = true;
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "ATTACK"));
                AbstractDungeon.actionManager.addToBottom(
                        (AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)this,
                                (AbstractPower)new WeakPower((AbstractCreature)p, 2, true), 2));
                break;
        }
        getMove(0);
    }

    protected void getMove(int i)
    {
        if (AbstractDungeon.ascensionLevel >= 17)
        {
            if (turns == 0)
            {
                turns++;
            }
            if (turns == 1)
            {
                setMove(MOVES[2], (byte)2, Intent.DEBUFF);
                turns++;
                return;
            }
            if (hasDebuffed)
            {
                setMove(MOVES[1], (byte)1, Intent.ATTACK_DEBUFF, ((DamageInfo)this.damage.get(1)).base, multiplier, true);
                hasDebuffed = false;
                return;
            }

            int roll = AbstractDungeon.aiRng.random(99);
            if (roll < 40)
            {
                setMove(MOVES[0], (byte)0, AbstractMonster.Intent.ATTACK_DEBUFF, ((DamageInfo)this.damage.get(0)).base);
            }
            else if (roll < 70)
            {
                setMove(MOVES[1], (byte)1, Intent.ATTACK_DEBUFF, ((DamageInfo)this.damage.get(1)).base, multiplier, true);
            }
            else
            {
                setMove(MOVES[2], (byte)2, Intent.DEBUFF);
            }
            return;
        }

        int roll = AbstractDungeon.aiRng.random(99);
        if (roll < 40)
        {
            setMove(MOVES[0], (byte)0, AbstractMonster.Intent.ATTACK_DEBUFF, ((DamageInfo)this.damage.get(0)).base);
        }
        else if (roll < 70)
        {
            setMove(MOVES[1], (byte)1, Intent.ATTACK_DEBUFF, ((DamageInfo)this.damage.get(1)).base, multiplier, true);
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
