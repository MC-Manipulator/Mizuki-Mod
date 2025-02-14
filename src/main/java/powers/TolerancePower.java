package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import modcore.MizukiModCore;

public class TolerancePower extends AbstractMizukiPower
{
    public static final String id = MizukiModCore.MakePath(TolerancePower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(id);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;


    public TolerancePower(AbstractCreature owner, int amt)
    {
        super(owner, amt, id, NAME);
        updateDescription();
        this.type = PowerType.BUFF;
    }
    public String getDescription()
    {
        return DESCRIPTIONS[0];
    }

    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount)
    {
        if (damageAmount > 0)
            addToTop((AbstractGameAction)new ReducePowerAction(this.owner, this.owner, this.ID, 1));
        return damageAmount / 2;
    }
}
