package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import com.megacrit.cardcrawl.ui.buttons.GridSelectConfirmButton;
import helper.CookingHelper;
import modcore.MizukiModCore;

import java.lang.reflect.Field;

@SpirePatch(clz = GridSelectConfirmButton.class, method = "update")
public class CookingGridPatch
{
    @SpireInsertPatch(loc = 55)
    public static void Prefix3(GridSelectConfirmButton obj)
    {
        if (obj.hb.clicked && CookingHelper.confirmScreenForCooking)
        {
            MizukiModCore.logger.info("二级界面点击确定");
            CookingHelper.confirmScreenForCooking = false;
            if (AbstractDungeon.gridSelectScreen.selectedCards.size() == 3)
            {
                CookingHelper.ableToCook = true;
            }
            AbstractDungeon.overlayMenu.cancelButton.hide();
            obj.hb.clicked = false;
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards)
                c.stopGlowing();
            AbstractDungeon.closeCurrentScreen();
            obj.isDisabled = true;
        }
        /*
        try
        {
            Class<?> C = GridCardSelectScreen.class;
            Field nameField = C.getDeclaredField("canCancel");
            nameField.setAccessible(true);
            boolean b = (boolean) nameField.get(obj);
            if (CookingHelper.gridScreenForCooking && b)
            {
                MizukiModCore.logger.info("回退到一级界面");
                AbstractDungeon.overlayMenu.cancelButton.show(GridCardSelectScreen.TEXT[1]);
            }
        }
        catch (Exception e)
        {
            MizukiModCore.logger.info(e);
        }*/

    }
}
