package patches;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import org.apache.commons.lang3.StringUtils;

public class FoodMod extends AbstractCardModifier
{
    public String modifyDescription(String rawDescription, AbstractCard card)
    {
        if (card.magicNumber > 1)
        {
            return rawDescription + CardCrawlGame.languagePack.getUIString("Mizuki_FoodDescription").TEXT[0];
        }
        if (card.magicNumber == 1)
        {
            return rawDescription + CardCrawlGame.languagePack.getUIString("Mizuki_FoodDescription").TEXT[1];
        }
        return rawDescription;
    }

    public AbstractCardModifier makeCopy()
    {
        return new FoodMod();
    }
}