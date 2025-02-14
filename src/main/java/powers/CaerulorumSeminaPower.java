package powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import modcore.MizukiModCore;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;

public class CaerulorumSeminaPower extends AbstractMizukiPower
{
    public static final String id = MizukiModCore.MakePath(CaerulorumSeminaPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(id);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;


    public CaerulorumSeminaPower(AbstractCreature owner, int amt)
    {
        super(owner, amt, id, NAME);
        updateDescription();
        this.type = PowerType.BUFF;
    }

    public String getDescription()
    {
        return DESCRIPTIONS[0];
    }

    public void onAfterCardPlayed(AbstractCard card)
    {
        flash();
        addToTop((AbstractGameAction)new ApplyPowerAction(owner, owner, (AbstractPower)new StrengthPower(owner, this.amount), this.amount));
        //addToTop((AbstractGameAction)new HealAction(owner, owner, 4 * this.amount));
    }

    public void atEndOfTurn(boolean isPlayer)
    {
        addToBot((AbstractGameAction)new RemoveSpecificPowerAction(owner, owner, id));
    }
}
