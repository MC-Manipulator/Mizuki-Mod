package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import modcore.MizukiModCore;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;

public class CountertransferencePower extends AbstractMizukiPower
{
    public static final String id = MizukiModCore.MakePath(CountertransferencePower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(id);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public CountertransferencePower(AbstractCreature owner, int amt)
    {
        super(owner, amt, id, NAME);
        updateDescription();
        this.type = PowerType.BUFF;
    }

    public String getDescription()
    {
        return DESCRIPTIONS[0];
    }

    public void atStartOfTurn()
    {
        //int temp = 0;
        if (owner.currentHealth <= owner.maxHealth / 2)
        {
            flash();
            addToBot((AbstractGameAction)new DrawCardAction(amount));
        }
        for (AbstractMonster m2 : (AbstractDungeon.getCurrRoom()).monsters.monsters)
        {
            if (!m2.isDeadOrEscaped() && m2.currentHealth <= m2.maxHealth / 2)
            {
                flash();
                //temp++;
                addToBot((AbstractGameAction)new DrawCardAction(amount));
            }
        }
        /*
        if (temp > 0)
        {
            addToTop((AbstractGameAction)new ApplyPowerAction(owner, owner, (AbstractPower)new StrengthPower(owner, this.amount), this.amount));
            //addToBot((AbstractGameAction)new ApplyPowerAction(owner, owner, (AbstractPower)new LoseStrengthPower(owner, this.amount), this.amount));
        }*/
    }
}
