package relics;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import helper.EventHelper;
import modcore.MizukiModCore;

public class Unleashings extends AbstractMizukiRelic
{
    //绽放
    public static final String ID = MizukiModCore.MakePath(Unleashings.class.getSimpleName());
    private boolean hasDamaged = false;
    public boolean hasImpairment = false;


    public Unleashings()
    {
        super(ID, AbstractRelic.RelicTier.BOSS, LandingSound.MAGICAL);
        hasDamaged = false;
        hasImpairment = false;
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onLoseHp(int damageAmount)
    {
        super.onLoseHp(damageAmount);
        if (!hasDamaged)
        {
            flash();
            hasDamaged = true;
            if (!this.pulse)
            {
                beginLongPulse();
            }
        }
    }

    @Override
    public void atTurnStart()
    {
        super.atTurnStart();
        if (hasDamaged || hasImpairment)
        {
            addToTop((AbstractGameAction)new RelicAboveCreatureAction((AbstractCreature)AbstractDungeon.player, this));
        }
        if (hasDamaged)
        {
            flash();
            addToTop(new GainEnergyAction(1));
            hasDamaged = false;
        }
        if (hasImpairment)
        {
            addToTop(new GainEnergyAction(1));
            hasImpairment = false;
        }

        if (this.pulse)
        {
            stopPulse();
        }
    }
}
