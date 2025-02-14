package relics;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import modcore.MizukiModCore;

public class JetBlackDanceShoes extends AbstractMizukiRelic
{
    //漆黑的舞鞋
    public static final String ID = MizukiModCore.MakePath(JetBlackDanceShoes.class.getSimpleName());
    public JetBlackDanceShoes()
    {
        super(ID, AbstractRelic.RelicTier.SPECIAL, AbstractRelic.LandingSound.MAGICAL);
    }

    @Override
    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction)
    {
        super.onUseCard(targetCard, useCardAction);
        if (targetCard.type == AbstractCard.CardType.SKILL && !CardModifierManager.hasModifier(targetCard, "Mizuki:PureWhiteDanceShoesMod"))
        {
            AbstractCard c = targetCard.makeCopy();
            c.type = AbstractCard.CardType.ATTACK;
            targetCard.type = AbstractCard.CardType.ATTACK;
        }
    }
}
