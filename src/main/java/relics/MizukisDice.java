package relics;

import actions.GainDiceAction;
import cards.Skills.Dice;
import cards.Skills.TowerOfTheWitchKing;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import helper.DiceHelper;
import modcore.MizukiModCore;
import patches.DicePatch;
import powers.TowerOfTheWitchKingPower;

import javax.smartcardio.Card;

public class MizukisDice extends AbstractMizukiRelic implements DicePatch
{

    //水月的骰子

    public static final String ID = MizukiModCore.MakePath(MizukisDice.class.getSimpleName());

    private static final int diceCount = 2;

    public boolean ifStarter;

    public MizukisDice()
    {
        super(ID, AbstractRelic.RelicTier.STARTER, AbstractRelic.LandingSound.SOLID);
        if (this.counter < 0)
        {
            this.counter = 0;
        }
        this.counter += diceCount;
        this.ifStarter = true;
        this.description = getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        PowerTip p = new PowerTip();
        p.header = DESCRIPTIONS[4];
        p.body = DESCRIPTIONS[5];
        tips.add(p);
        this.initializeTips();
    }

    public MizukisDice(boolean ifStarter)
    {
        this();
        this.ifStarter = ifStarter;
        if (!ifStarter)
        {
            this.counter -= diceCount;
        }
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();
        initializeTips();
    }

    public String getUpdatedDescription()
    {
        if (ifStarter)
        {
            return this.DESCRIPTIONS[0] + diceCount + this.DESCRIPTIONS[1] + this.DESCRIPTIONS[2] + diceCount + this.DESCRIPTIONS[3];
        }
        return this.DESCRIPTIONS[2] + diceCount + this.DESCRIPTIONS[3];
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

    public int getCounter()
    {
        return this.counter;
    }

    public void addCounter(int diceCount)
    {
        this.counter += diceCount;
    }
}
