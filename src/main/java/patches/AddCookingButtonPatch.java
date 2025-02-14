package patches;

import characters.Mizuki;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import helper.CookingHelper;
import javassist.CtBehavior;
import modcore.MizukiModCore;
import ui.CookingOption;

import java.util.ArrayList;

public class AddCookingButtonPatch
{
    @SpirePatch(clz = CampfireUI.class, method = "initializeButtons")
    public static class AddKeys
    {
        @SpireInsertPatch(locator = AddCookingButtonPatch.Locator.class)
        public static void patch(CampfireUI __instance, ArrayList<AbstractCampfireOption> ___buttons)
        {
            Boolean active = Boolean.valueOf(true);
            if (CookingHelper.getCookableCards().size() < 3)
            {
                active = Boolean.valueOf(false);
            }
            if (AbstractDungeon.player instanceof Mizuki || active.booleanValue())
            {
                CookingHelper.CookingCampfireOption = new CookingOption(active.booleanValue());
                ___buttons.add(CookingHelper.CookingCampfireOption);
            }
        }
    }

    public static class Locator extends SpireInsertLocator
    {
        public int[] Locate(CtBehavior ctBehavior) throws Exception
        {
            Matcher.FieldAccessMatcher fieldAccessMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "relics");
            return LineFinder.findInOrder(ctBehavior, (Matcher)fieldAccessMatcher);
        }
    }
}