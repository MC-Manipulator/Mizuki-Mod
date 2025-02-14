package helper;

import basemod.abstracts.DynamicVariable;
import cards.AbstractMizukiCard;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class FourthMagicNumber extends DynamicVariable
{
    public String key()
    {
        return "MKM4";
    }

    public boolean isModified(AbstractCard card)
    {
        if (card instanceof AbstractMizukiCard)
        {
            AbstractMizukiCard mc = (AbstractMizukiCard)card;
            return (mc.magicNumber4 != mc.baseMagicNumber4);
        }
        return false;
    }

    public int value(AbstractCard card)
    {
        if (card instanceof AbstractMizukiCard)
        {
            AbstractMizukiCard mc = (AbstractMizukiCard)card;
            return mc.magicNumber4;
        }
        return 0;
    }

    public int baseValue(AbstractCard card)
    {
        if (card instanceof AbstractMizukiCard)
        {
            AbstractMizukiCard mc = (AbstractMizukiCard)card;
            return mc.baseMagicNumber4;
        }
        return 0;
    }

    public boolean upgraded(AbstractCard card)
    {
        if (card instanceof AbstractMizukiCard)
        {
            AbstractMizukiCard mc = (AbstractMizukiCard)card;
            return mc.upgradedMagicNumber4;
        }
        return false;
    }
}
