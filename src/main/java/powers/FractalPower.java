package powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import modcore.MizukiModCore;

public class FractalPower extends AbstractMizukiPower
{
    public static final String id = MizukiModCore.MakePath(FractalPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(id);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public int limit = 0;
    public FractalPower(AbstractCreature owner, int amt, int limit)
    {
        super(owner, amt, id, NAME);
        updateDescription();
        this.type = PowerType.BUFF;
        this.limit = limit;
        updateDescription();
    }

    public String getDescription()
    {
        return DESCRIPTIONS[0] + this.limit + DESCRIPTIONS[1];
    }
}
