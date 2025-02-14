package monsters.boss.exordium;

import Impairment.CorrosionImpairment;
import Impairment.NervousImpairment;
import actions.ApplyImpairmentAction;
import basemod.abstracts.AbstractCardModifier;
import basemod.cardmods.EtherealMod;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.ClearCardQueueAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import helper.RejectionHelper;
import modcore.MizukiModCore;
import monsters.AbstractMizukiMonster;
import powers.AmmoPower;
import powers.SymbiosisPower;

public class TidelinkedImmortal extends AbstractMizukiMonster
{
    //接潮斥亡体

    public static final String ID = "Mizuki:TidelinkedImmortal";

    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Mizuki:TidelinkedImmortal");

    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    private int turn = 0;

    private int multiplier = 3;

    private float percent = 0.34f;

    public TidelinkedImmortal(float x, float y)
    {

        super(NAME, "Mizuki:TidelinkedImmortal", 90, 0.0F, 0.0F, 200.0F, 320.0F, null, x, y);
        this.type = EnemyType.BOSS;

        int dmg1, dmg2;

        if (AbstractDungeon.ascensionLevel >= 9)
        {
            setHp(40);
        }
        else
        {
            setHp(30);
        }

        if (AbstractDungeon.ascensionLevel >= 19)
        {
            dmg1 = 10;
            dmg2 = 5;
            percent = 0.5f;
        }
        else if (AbstractDungeon.ascensionLevel >= 4)
        {
            dmg1 = 10;
            dmg2 = 5;
            percent = 0.34f;
        }
        else
        {
            dmg1 = 8;
            dmg2 = 5;
            percent = 0.34f;
        }

        this.damage.add(new DamageInfo((AbstractCreature) this, MathUtils.floor(dmg1)));
        this.damage.add(new DamageInfo((AbstractCreature) this, MathUtils.floor(dmg2)));

        //填入spine文件名
        loadAnimation("resources/img/monster/TidelinkedImmortal/enemy_2030_symon2.atlas", "resources/img/monster/TidelinkedImmortal/enemy_2030_symon2.json", 1.5F);
        this.flipHorizontal = true;
        this.stateData.setMix("Idle", "Die", 0.1F);
        this.state.setTimeScale(1.25f);
        this.state.addAnimation(0, "Idle", true, 0.0F);
        this.nextMove = 0;
    }

    @Override
    public void update()
    {
        super.update();
        if (this.currentHealth == this.maxHealth && this.halfDead)
        {
            AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "RECOVER"));
            this.halfDead = false;
            (AbstractDungeon.getCurrRoom()).cannotLose = true;
            getMove(0);
            createIntent();
        }
    }

    @Override
    public void damage(DamageInfo info)
    {
        super.damage(info);
        if (this.currentHealth <= 0 && !this.halfDead)
        {
            AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "DIE_AND_RECOVERING"));
            this.halfDead = true;
            for (AbstractPower p : this.powers)
                p.onDeath();
            for (AbstractRelic r : AbstractDungeon.player.relics)
                r.onMonsterDeath(this);
            addToTop((AbstractGameAction)new ClearCardQueueAction());
            this.powers.removeIf(p -> p.type == AbstractPower.PowerType.DEBUFF || p.ID.equals("Shackled"));
            applyPowers();
            boolean allDead = true;
            for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters)
            {
                if (!m.halfDead)
                {
                    allDead = false;
                    break;
                }
            }
            if (!allDead)
            {
                if (this.nextMove != 3)
                {
                    setMove((byte)3, AbstractMonster.Intent.UNKNOWN);
                    createIntent();
                    AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new SetMoveAction(this, (byte)3, AbstractMonster.Intent.UNKNOWN));
                }
            }
            else
            {
                (AbstractDungeon.getCurrRoom()).cannotLose = false;
                this.halfDead = false;
                for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters)
                    m.die();
            }
        }
    }

    public void usePreBattleAction()
    {
        AbstractDungeon.actionManager.addToBottom(
                (AbstractGameAction)new ApplyPowerAction((AbstractCreature)this, (AbstractCreature)this,
                        new SymbiosisPower(this, 1)));
    }

    public void takeTurn()
    {
        AbstractPlayer p = AbstractDungeon.player;
        switch (this.nextMove)
        {
            case 0:
                //刺击
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "ATTACK"));
                addToBot((AbstractGameAction) new DamageAction((AbstractCreature)p, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                addToTop(new ApplyImpairmentAction(new CorrosionImpairment(), p, 2));
                break;
            case 1:
                //酸腐
                addToTop(new ApplyImpairmentAction(new CorrosionImpairment(), p, 2));
                AbstractDungeon.actionManager.addToBottom(
                        (AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)this,
                                (AbstractPower)new FrailPower((AbstractCreature)p, 2, true), 2));
                break;
            case 2:
                //连斩
                for (int i = 0;i < this.multiplier;i++)
                {
                    slash();
                }
                break;
            case 3:
                //复活
                AbstractDungeon.actionManager.addToBottom(
                        (AbstractGameAction)new HealAction((AbstractCreature)this, (AbstractCreature)this, (int)(this.maxHealth * percent)));
                break;
        }
        turn++;
        getMove(0);
    }

    protected void getMove(int i)
    {
        if (halfDead)
        {
            setMove((byte)3, AbstractMonster.Intent.UNKNOWN);
            return;
        }
        if (turn == 5)
        {
            turn = 2;
        }
        switch (turn)
        {
            case 0:
                //刺击与酸腐
                int roll = AbstractDungeon.aiRng.random(99);
                if (roll < 50)
                {
                    setMove(MOVES[0], (byte)0, Intent.ATTACK, ((DamageInfo)this.damage.get(0)).base);
                }
                else
                {
                    setMove(MOVES[1], (byte)1, Intent.DEBUFF);
                }
                break;
            case 2:
                //刺击
                setMove(MOVES[0], (byte)0, Intent.ATTACK, ((DamageInfo)this.damage.get(0)).base);
                break;
            case 3:
                //酸腐
                setMove(MOVES[1], (byte)1, Intent.DEBUFF);
                break;
            case 1:
            case 4:
                //连斩
                setMove(MOVES[2], (byte)2, Intent.ATTACK, ((DamageInfo)this.damage.get(1)).base, this.multiplier, true);
                break;
        }
    }

    public void changeState(String stateName)
    {
        if (stateName.equals("ATTACK"))
        {
            this.state.setAnimation(0, "Attack", false);
            this.state.addAnimation(0, "Idle", true, 0.0F);
        }
        if (stateName.equals("DIE_AND_RECOVERING"))
        {
            this.state.setAnimation(0, "Die", false);
            this.state.addAnimation(0, "Die_Loop", true, 0.0F);
        }
        if (stateName.equals("RECOVER"))
        {
            this.state.setAnimation(0, "Die_Idle", false);
            this.state.addAnimation(0, "Idle", true, 0.0F);
        }
    }

    public void die()
    {
        if (!(AbstractDungeon.getCurrRoom()).cannotLose)
        {
            isDying = true;
            useFastShakeAnimation(5.0F);
            super.die();
        }
    }

    private void slash()
    {
        AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "ATTACK"));
        AbstractPlayer p = AbstractDungeon.player;
        addToBot(new AbstractGameAction()
        {
            boolean justIn = true;

            @Override
            public void update()
            {
                this.target = AbstractDungeon.player;

                if (justIn)
                {
                    this.duration = 0.5F;
                    justIn = false;
                }

                tickDuration();
                if (this.isDone)
                {
                    AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AttackEffect.SLASH_HEAVY));
                    this.target.damage(damage.get(1));

                    if (this.target.lastDamageTaken > 0)
                    {
                        addToTop(new ApplyImpairmentAction(new CorrosionImpairment(), p, 1));
                        addToTop((AbstractGameAction)new WaitAction(0.1F));
                    }

                    if ((AbstractDungeon.getCurrRoom()).monsters.areMonstersBasicallyDead())
                        AbstractDungeon.actionManager.clearPostCombatActions();
                }
            }
        });
    }
}
