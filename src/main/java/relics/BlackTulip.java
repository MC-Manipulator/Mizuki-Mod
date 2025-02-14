package relics;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import helper.EventHelper;
import modcore.MizukiModCore;
import powers.ArousalPower;

public class BlackTulip extends AbstractMizukiRelic
{
    //黑色郁金香
    public static final String ID = MizukiModCore.MakePath(BlackTulip.class.getSimpleName());
    private static final int cardsAmount = 5;
    private static final int impairmentAmount = 2;

    public BlackTulip()
    {
        super(ID, RelicTier.UNCOMMON, LandingSound.FLAT);
    }


    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0] + cardsAmount + DESCRIPTIONS[1] + impairmentAmount + DESCRIPTIONS[2];
    }

    @Override
    public void atBattleStart()
    {
        if (this.counter == cardsAmount - 1)
        {
            beginPulse();
            this.pulse = true;
        }
    }

    @Override
    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction)
    {
        super.onUseCard(targetCard, useCardAction);
        if (targetCard.type == AbstractCard.CardType.SKILL)
        {
            counter++;
        }
        if (this.counter == cardsAmount)
        {
            this.counter = 0;
            flash();
            this.pulse = false;
            addToBot((AbstractGameAction)new RelicAboveCreatureAction((AbstractCreature)AbstractDungeon.player, this));
            addToBot((AbstractGameAction)new ApplyPowerAction(
                    AbstractDungeon.player,
                    AbstractDungeon.player,
                    new ArousalPower(AbstractDungeon.player, impairmentAmount),
                    impairmentAmount));
        }
        else if (this.counter == cardsAmount - 1)
        {
            beginPulse();
            this.pulse = true;
        }
    }
}
