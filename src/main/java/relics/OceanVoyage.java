package relics;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import modcore.MizukiModCore;

public class OceanVoyage extends AbstractMizukiRelic
{
    //海程
    public static final String ID = MizukiModCore.MakePath(OceanVoyage.class.getSimpleName());
    private AbstractPlayer p;
    private boolean hasIncrease = false;
    private boolean hasDecrease = false;
    private static final int strengthCount = 1;
    private static final int dexterityCount = 2;

    public OceanVoyage()
    {
        super(ID, AbstractRelic.RelicTier.SPECIAL, LandingSound.SOLID);
        p = AbstractDungeon.player;
    }

    @Override
    public void atPreBattle()
    {
        super.atPreBattle();
        hasIncrease = false;
        hasDecrease = false;
    }

    @Override
    public String getUpdatedDescription()
    {
        return this.DESCRIPTIONS[0] + dexterityCount + this.DESCRIPTIONS[1] + strengthCount + this.DESCRIPTIONS[2];
    }
    public void update()
    {
        super.update();
        if (p == null)
        {
            return;
        }

        if (!hasIncrease)
        {
            for (AbstractPower p : p.powers)
            {
                if (p.ID.equals(DexterityPower.POWER_ID))
                {
                    addToBot(new ApplyPowerAction(this.p, this.p, new DexterityPower(this.p, dexterityCount), dexterityCount));
                    addToTop((AbstractGameAction)new RelicAboveCreatureAction((AbstractCreature)AbstractDungeon.player, this));
                    hasIncrease = true;
                    break;
                }
            }
        }

        if (!hasDecrease)
        {
            for (AbstractPower p : p.powers)
            {
                if (p.ID.equals(StrengthPower.POWER_ID))
                {
                    addToBot(new ApplyPowerAction(this.p, this.p, new StrengthPower(this.p, -strengthCount), -strengthCount));
                    addToTop((AbstractGameAction)new RelicAboveCreatureAction((AbstractCreature)AbstractDungeon.player, this));
                    hasDecrease = true;
                    break;
                }
            }
        }
    }
}
