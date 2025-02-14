package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.BufferPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import modcore.MizukiModCore;
import org.w3c.dom.views.AbstractView;

public class PainPower extends AbstractMizukiPower
{

    public static final String id = MizukiModCore.MakePath(PainPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(id);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;


    public PainPower(AbstractCreature owner, int amt)
    {
        super(owner, amt, id, NAME);
        updateDescription();
        this.type = PowerType.BUFF;
    }

    public String getDescription()
    {
        return DESCRIPTIONS[0] + AbstractDungeon.player.getPower("Mizuki_PainAdaptationPower").amount + DESCRIPTIONS[1];
    }

    public void stackPower(int stackAmount)
    {
        super.stackPower(stackAmount);
        if (this.amount >= 4)
        {
            flash();
            this.amount -= 4;
            for (int i = 0;i < AbstractDungeon.player.getPower("Mizuki_PainAdaptationPower").amount;i++)
            {
                addToTop((AbstractGameAction)new ApplyPowerAction(owner, owner, (AbstractPower)new BufferPower(owner, 1), 1));
                //addToTop((AbstractGameAction)new ApplyPowerAction(owner, owner, (AbstractPower)new TolerancePower(owner, 1), 1));
            }
            if (this.amount <= 0)
                addToTop((AbstractGameAction)new RemoveSpecificPowerAction(this.owner, this.owner, id));

        }
    }

}
