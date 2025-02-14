package relics;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import modcore.MizukiModCore;
import patches.PureWhiteDanceShoesMod;
import patches.WavebreakerMod;

public class PureWhiteDanceShoes extends AbstractMizukiRelic
{
    //洁白的舞鞋
    public static final String ID = MizukiModCore.MakePath(PureWhiteDanceShoes.class.getSimpleName());
    public PureWhiteDanceShoes()
    {
        super(ID, AbstractRelic.RelicTier.SPECIAL, AbstractRelic.LandingSound.MAGICAL);
    }

    @Override
    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction)
    {
        super.onUseCard(targetCard, useCardAction);
        if (targetCard.type == AbstractCard.CardType.ATTACK && !CardModifierManager.hasModifier(targetCard, "Mizuki:JetBlackDanceShoesMod"))
        {
            CardModifierManager.addModifier(targetCard, (AbstractCardModifier)new PureWhiteDanceShoesMod());
            targetCard.type = AbstractCard.CardType.SKILL;
        }
    }
}
