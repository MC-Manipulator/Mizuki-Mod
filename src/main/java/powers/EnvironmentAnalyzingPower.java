package powers;

import actions.AnalyzingAction;
import actions.EnvironmentAnalyzingAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import modcore.MizukiModCore;

public class EnvironmentAnalyzingPower extends AbstractMizukiPower
{
    public static final String id = MizukiModCore.MakePath(EnvironmentAnalyzingPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(id);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private int storedDrawAmount = 0;

    public EnvironmentAnalyzingPower(AbstractPlayer owner, int amt)
    {
        super(owner, amt, id, NAME);
        updateDescription();
        this.type = PowerType.BUFF;
    }

    public String getDescription()
    {
        return DESCRIPTIONS[0];
    }

    public void onInitialApplication()
    {
        AbstractDungeon.player.gameHandSize -= this.amount;
    }

    @Override
    public void stackPower(int stackAmount)
    {
        super.stackPower(stackAmount);
        AbstractDungeon.player.gameHandSize -= stackAmount;
    }

    @Override
    public void reducePower(int reduceAmount)
    {
        super.reducePower(reduceAmount);
        AbstractDungeon.player.gameHandSize += reduceAmount;
    }

    public void onRemove()
    {
        AbstractDungeon.player.gameHandSize += this.amount;
    }


    public void atStartOfTurnPostDraw()
    {
        flash();
        for (int i = 0;i < this.amount;i++)
        {
            addToBot((AbstractGameAction)new AnalyzingAction());
        }
        //addToBot((AbstractGameAction)new EnvironmentAnalyzingAction(this.amount, false, true, true));
    }
}
