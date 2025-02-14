package powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import modcore.MizukiModCore;

public class WeakPointPower extends AbstractMizukiPower
{
    public static final String id = MizukiModCore.MakePath(WeakPointPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(id);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public WeakPointPower(AbstractCreature owner, int amt)
    {
        super(owner, amt, id, NAME);
        updateDescription();
        this.type = PowerType.DEBUFF;
    }

    public String getDescription()
    {
        return DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}
