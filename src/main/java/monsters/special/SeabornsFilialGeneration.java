package monsters.special;

import Impairment.CorrosionImpairment;
import Impairment.NervousImpairment;
import actions.ApplyImpairmentAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.IntangiblePower;
import modcore.MizukiModCore;
import monsters.AbstractMizukiMonster;
import powers.DyingPower;
import powers.HungerSensePower;

public class SeabornsFilialGeneration extends AbstractMizukiMonster
{
    public static final String ID = "Mizuki:SeabornsFilialGeneration";

    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Mizuki:SeabornsFilialGeneration");

    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    public SeabornsFilialGeneration(float x, float y)
    {

        super(NAME, "Mizuki:SeabornsFilialGeneration", 90, 0.0F, 0.0F, 150.0F, 320.0F, null, x, y);

        setHp(2, 2);

        loadAnimation("resources/img/monster/SeabornsFilialGeneration/trap_039_dstnta.atlas", "resources/img/monster/SeabornsFilialGeneration/trap_039_dstnta.json", 1.5F);
        this.flipHorizontal = true;
        this.state.setAnimation(0, "Idle_02", true);
        this.nextMove = 0;
        getMove(0);
    }

    public SeabornsFilialGeneration(float x, float y, boolean isflip)
    {
        this(x, y);
        this.flipHorizontal = isflip;
    }

    public void usePreBattleAction()
    {
        AbstractDungeon.actionManager.addToBottom(
                (AbstractGameAction)new ApplyPowerAction((AbstractCreature)this, (AbstractCreature)this,
                        (AbstractPower)new HungerSensePower((AbstractCreature)this, -1)));
        //饥饿本能
        //若玩家本回合没有打出至少2种类型的卡牌，则给予敌人2点神经损伤
    }

    public void takeTurn()
    {
        AbstractPlayer p = AbstractDungeon.player;

        switch (this.nextMove)
        {
            case 0:
                //摄食
                this.state.setAnimation(0, "Attack_Begin", false);
                this.state.addAnimation(0, "Attack_Loop", true, 0.0F);
                addToBot(new ApplyImpairmentAction(new NervousImpairment(), p, 2));
                //MizukiModCore.logger.info("Move0");
                getMove(0);
                break;
            case 1:
                //退行
                addToBot((AbstractGameAction)new SuicideAction(this));
                //MizukiModCore.logger.info("Move1");
                getMove(1);
                break;
        }
    }

    protected void getMove(int i)
    {
        //MizukiModCore.logger.info("I" + i);
        if (i == 1)
        {
            //MizukiModCore.logger.info("Move1");
            setMove(MOVES[1], (byte)1, AbstractMonster.Intent.STUN);
        }
        else
        {
            setMove(MOVES[0], (byte)0, Intent.DEBUFF);
        }
    }

    public void die()
    {
        this.state.setAnimation(0, "Die_02", false);
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
