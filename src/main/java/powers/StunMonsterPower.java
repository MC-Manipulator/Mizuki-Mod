package powers;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import helper.EventHelper;
import modcore.MizukiModCore;
import vfx.StunEffect;

import java.lang.reflect.Field;

public class StunMonsterPower extends AbstractMizukiPower
{
    public static final String id = MizukiModCore.MakePath(StunMonsterPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(id);

    public static final String NAME = powerStrings.NAME;

    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private byte moveByte;

    private AbstractMonster.Intent moveIntent;

    private boolean playerStunnedTrigger = false;

    private String moveName = "";

    private boolean end = false;

    private EnemyMoveInfo move;

    public StunMonsterPower(AbstractMonster owner)
    {
        this(owner, 1);
    }

    public StunMonsterPower(AbstractCreature owner, int amount)
    {
        super(owner, amount, id, NAME);
        this.type = PowerType.DEBUFF;
        this.isTurnBased = true;
        updateDescription();
    }

    @Override
    public void atStartOfTurnPostDraw()
    {
        if (playerStunnedTrigger && !end)
        {
            AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.GOLD, true));
            //AbstractDungeon.topLevelEffectsQueue.add(new StunEffect());
            addToBot((AbstractGameAction)new PressEndTurnButtonAction());

            if (this.amount <= 0)
            {
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new RemoveSpecificPowerAction(this.owner, this.owner, this));
            }
            else
            {
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ReducePowerAction(this.owner, this.owner, this, 1));
            }
        }
    }



    public String getDescription()
    {
        return DESCRIPTIONS[0];
    }

    public void updateDescription()
    {
        this.description = DESCRIPTIONS[0] + this.amount;

        if (this.amount == 1)
        {
            this.description += DESCRIPTIONS[1];
        }
        else
        {
            this.description += DESCRIPTIONS[2];
        }
    }

    public void atEndOfRound()
    {
        if (!owner.isPlayer)
        {
            if (this.amount <= 0)
            {
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new RemoveSpecificPowerAction(this.owner, this.owner, this));
            }
            else
            {
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ReducePowerAction(this.owner, this.owner, this, 1));
            }
        }
        else
        {
            if (playerStunnedTrigger)
            {
                if (this.amount <= 0)
                {
                    AbstractDungeon.actionManager.addToTop((AbstractGameAction)new RemoveSpecificPowerAction(this.owner, this.owner, this));
                }
                else
                {
                    AbstractDungeon.actionManager.addToTop((AbstractGameAction)new ReducePowerAction(this.owner, this.owner, this, 1));
                }
                end = true;
            }
            else
            {
                playerStunnedTrigger = true;
            }
        }
    }

    public void onInitialApplication()
    {
        if (owner.isPlayer)
        {
            playerStunnedTrigger = true;
            AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.GOLD, true));
            //AbstractDungeon.topLevelEffectsQueue.add(new StunEffect());
            addToBot((AbstractGameAction)new PressEndTurnButtonAction());
            return;
        }
        else
        {
            AbstractDungeon.actionManager.addToBottom(new AbstractGameAction()
            {
                public void update()
                {
                    if (StunMonsterPower.this.owner instanceof AbstractMonster)
                    {
                        StunMonsterPower.this.moveName = ((AbstractMonster)StunMonsterPower.this.owner).moveName;
                        StunMonsterPower.this.moveByte = ((AbstractMonster)StunMonsterPower.this.owner).nextMove;
                        StunMonsterPower.this.moveIntent = ((AbstractMonster)StunMonsterPower.this.owner).intent;
                        try
                        {
                            Field f = AbstractMonster.class.getDeclaredField("move");
                            f.setAccessible(true);
                            StunMonsterPower.this.move = (EnemyMoveInfo)f.get(StunMonsterPower.this.owner);
                            EnemyMoveInfo stunMove = new EnemyMoveInfo(StunMonsterPower.this.moveByte, AbstractMonster.Intent.STUN, -1, 0, false);
                            f.set(StunMonsterPower.this.owner, stunMove);
                            ((AbstractMonster) StunMonsterPower.this.owner).moveName = DESCRIPTIONS[3];
                            ((AbstractMonster)StunMonsterPower.this.owner).createIntent();
                        }
                        catch (IllegalAccessException|NoSuchFieldException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    this.isDone = true;
                }
            });
        }
    }

    public void onRemove()
    {
        if (this.owner instanceof AbstractMonster)
        {
            AbstractMonster m = (AbstractMonster)this.owner;
            if (this.move != null)
            {
                m.setMove(this.moveByte, this.moveIntent, this.move.baseDamage, this.move.multiplier, this.move.isMultiDamage);
            }
            else
            {
                m.setMove(this.moveByte, this.moveIntent);
            }
            m.moveName = this.moveName;
            m.createIntent();
            m.applyPowers();
        }
    }
}
