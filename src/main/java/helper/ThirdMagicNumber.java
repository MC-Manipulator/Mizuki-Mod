package helper;

import basemod.abstracts.DynamicVariable;
import cards.AbstractMizukiCard;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class ThirdMagicNumber extends DynamicVariable
{
    public String key()
    {
        return "MKM3";
    }

    public boolean isModified(AbstractCard card)
    {
        if (card instanceof AbstractMizukiCard)
        {
            AbstractMizukiCard mc = (AbstractMizukiCard)card;
            return (mc.magicNumber3 != mc.baseMagicNumber3);
        }
        return false;
    }

    public int value(AbstractCard card)
    {
        if (card instanceof AbstractMizukiCard)
        {
            AbstractMizukiCard mc = (AbstractMizukiCard)card;
            return mc.magicNumber3;
        }
        return 0;
    }

    public int baseValue(AbstractCard card)
    {
        if (card instanceof AbstractMizukiCard)
        {
            AbstractMizukiCard mc = (AbstractMizukiCard)card;
            return mc.baseMagicNumber3;
        }
        return 0;
    }

    public boolean upgraded(AbstractCard card)
    {
        if (card instanceof AbstractMizukiCard)
        {
            AbstractMizukiCard mc = (AbstractMizukiCard)card;
            return mc.upgradedMagicNumber3;
        }
        return false;
    }
}
