package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.CardLibrary;

public class FoodCardColorEnumPatch
{
    public static class CardColorPatch
    {
        @SpireEnum
        public static AbstractCard.CardColor FOOD;
    }

    public static class LibColorPatch
    {
        @SpireEnum
        public static CardLibrary.LibraryType FOOD;
    }
}
