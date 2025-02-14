package helper;

import basemod.abstracts.DynamicVariable;
import cards.AbstractMizukiCard;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class SecondMagicNumber extends DynamicVariable
{
    public String key()
    {
        return "MKM2";
    }

    public boolean isModified(AbstractCard card)
    {
        if (card instanceof AbstractMizukiCard)
        {
            AbstractMizukiCard mc = (AbstractMizukiCard)card;
            return (mc.magicNumber2 != mc.baseMagicNumber2);
        }
        return false;
    }

    public int value(AbstractCard card)
    {
        if (card instanceof AbstractMizukiCard)
        {
            AbstractMizukiCard mc = (AbstractMizukiCard)card;
            return mc.magicNumber2;
        }
        return 0;
    }

    public int baseValue(AbstractCard card)
    {
        if (card instanceof AbstractMizukiCard)
        {
            AbstractMizukiCard mc = (AbstractMizukiCard)card;
            return mc.baseMagicNumber2;
        }
        return 0;
    }

    public boolean upgraded(AbstractCard card)
    {
        if (card instanceof AbstractMizukiCard)
        {
            AbstractMizukiCard mc = (AbstractMizukiCard)card;
            return mc.upgradedMagicNumber2;
        }
        return false;
    }
}
