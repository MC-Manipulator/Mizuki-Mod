package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import com.megacrit.cardcrawl.ui.buttons.CancelButton;
import helper.CookingHelper;
import modcore.MizukiModCore;

@SpirePatch(clz = CancelButton.class, method = "update")
public class CancelFromConfirm
{
    @SpireInsertPatch(loc = 96)
    public static SpireReturn<Void> Prefix2(CancelButton obj)
    {
        if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.GRID && CookingHelper.gridScreenForCooking &&
                AbstractDungeon.gridSelectScreen.confirmScreenUp)
        {
            MizukiModCore.logger.info("二级界面点击取消");
            AbstractDungeon.gridSelectScreen.confirmScreenUp = false;
            CookingHelper.confirmScreenForCooking = false;
            AbstractDungeon.overlayMenu.cancelButton.show(GridCardSelectScreen.TEXT[1]);

            return SpireReturn.Return();
        }
        return SpireReturn.Continue();
    }
}
