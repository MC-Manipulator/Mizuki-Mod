package relics;

import actions.GainDiceAction;
import cards.Skills.Dice;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import helper.DiceHelper;
import modcore.MizukiModCore;
import patches.DicePatch;

public class MizukisD8 extends AbstractMizukiRelic implements DicePatch
{

    //水月的骰子，D8

    public static final String ID = MizukiModCore.MakePath(MizukisD8.class.getSimpleName());

    private static final int diceCount = 4;


    public MizukisD8()
    {
        super(ID, RelicTier.STARTER, AbstractRelic.LandingSound.SOLID);
        if (this.counter < 0)
        {
            this.counter = 0;
        }
        if (AbstractDungeon.isPlayerInDungeon())
        {
            if (AbstractDungeon.player.hasRelic(MizukisDice.ID))
            {
                MizukisDice relic = (MizukisDice) AbstractDungeon.player.getRelic(MizukisDice.ID);
                this.counter = relic.counter + diceCount;
            }
            else
            {
                this.counter = diceCount;
            }
        }
        else
        {
            this.counter = diceCount;
        }
        PowerTip p = new PowerTip();
        p.header = DESCRIPTIONS[2];
        p.body = DESCRIPTIONS[3];
        tips.add(p);
    }

    public String getUpdatedDescription()
    {
        return this.DESCRIPTIONS[0] + diceCount + this.DESCRIPTIONS[1];
    }

    public void atBattleStartPreDraw()
    {
        if (this.counter > 0)
        {
            Dice card = new Dice();
            addToBot((AbstractGameAction)new RelicAboveCreatureAction((AbstractCreature) AbstractDungeon.player, this));
            addToBot((AbstractGameAction)new MakeTempCardInHandAction((AbstractCard) card, 1, false));
        }
    }

    public void onEnterRoom(AbstractRoom room)
    {
        if (room instanceof com.megacrit.cardcrawl.rooms.RestRoom)
        {
            flash();
            DiceHelper.gainDice(diceCount);
        }
    }

    public void obtain()
    {
        if (AbstractDungeon.player.hasRelic(MizukisDice.ID))
        {
            for (int i = 0; i < AbstractDungeon.player.relics.size(); i++)
            {
                if (((AbstractRelic)AbstractDungeon.player.relics.get(i)).relicId.equals(MizukisDice.ID))
                {
                    instantObtain(AbstractDungeon.player, i, true);
                    break;
                }
            }
        }
        else
        {
            super.obtain();
        }
    }

    public boolean canSpawn()
    {
        return AbstractDungeon.player.hasRelic(MizukisDice.ID);
    }

    public int getCounter()
    {
        return this.counter;
    }

    public void addCounter(int count)
    {
        this.counter += count;
    }
}
