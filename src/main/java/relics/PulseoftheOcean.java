package relics;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import modcore.MizukiModCore;

public class PulseoftheOcean extends AbstractMizukiRelic
{
    //海洋的脉搏
    public static final String ID = MizukiModCore.MakePath(PulseoftheOcean.class.getSimpleName());

    public final float increasePercent = 0.05f;

    public final float maxPercent = 0.5f;

    public PulseoftheOcean()
    {
        super(ID, AbstractRelic.RelicTier.SPECIAL, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0] + increasePercent * 100 + DESCRIPTIONS[1] + maxPercent * 100 + DESCRIPTIONS[2];
    }

    @Override
    public void atTurnStart()
    {
        this.counter = 0;
    }

    @Override
    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction)
    {
        if (this.counter * increasePercent < maxPercent)
        {
            this.counter++;
            flash();
        }
    }

    @Override
    public int onAttackToChangeDamage(DamageInfo info, int damageAmount)
    {
        return MathUtils.ceil(damageAmount * (1 + this.counter * increasePercent));
    }
}
