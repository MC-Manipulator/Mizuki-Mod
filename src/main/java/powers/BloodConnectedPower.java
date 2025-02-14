package powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import modcore.MizukiModCore;
import monsters.boss.city.SalVientoBishopQuintus;

public class BloodConnectedPower extends AbstractMizukiPower
{
    public static final String id = MizukiModCore.MakePath(BloodConnectedPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(id);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private SalVientoBishopQuintus mainBody = null;
    public BloodConnectedPower(AbstractCreature owner, int amt)
    {
        super(owner, amt, id, NAME);
        updateDescription();
        this.type = PowerType.BUFF;
        this.isTurnBased = false;
    }

    @Override
    public String getDescription()
    {
        if (mainBody == null)
        {
            for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters)
            {
                if (!m.isDeadOrEscaped() && m.id.equals("Mizuki:SalVientoBishopQuintus"))
                {
                    mainBody = (SalVientoBishopQuintus)m;
                }
            }
        }
        if (owner != null && mainBody != null)
        {
            return DESCRIPTIONS[0] + owner.name + DESCRIPTIONS[1] + mainBody.name + DESCRIPTIONS[2];
        }
        else
        {
            return DESCRIPTIONS[0] + "owner" + DESCRIPTIONS[1] + "mainBody" + DESCRIPTIONS[2];
        }
    }
}
