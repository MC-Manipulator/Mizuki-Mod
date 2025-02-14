package patches;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.screens.mainMenu.ColorTabBar;
import javassist.CtBehavior;

public class FoodCardTabNamePatch
{
    @SpirePatch(cls = "basemod.patches.com.megacrit.cardcrawl.screens.mainMenu.ColorTabBar.ColorTabBarFix$Render", method = "Insert")
    public static class TabNamePatch
    {
        private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("Mizuki_TapName");

        @SpireInsertPatch(locator = TabNameLocator.class, localvars = {"tabName"})
        public static void InsertFix(ColorTabBar _instance, SpriteBatch sb, float y, ColorTabBar.CurrentTab curTab, @ByRef String[] tabName)
        {
            if (tabName[0].equals("Food"))
                tabName[0] = uiStrings.TEXT[0];
        }

        private static class TabNameLocator extends SpireInsertLocator
        {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(FontHelper.class, "renderFontCentered");
                return LineFinder.findInOrder(ctMethodToPatch, (Matcher)methodCallMatcher);
            }
        }
    }
}