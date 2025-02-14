package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import helper.CookingHelper;
import modcore.MizukiModCore;


@SpirePatch(clz = GridCardSelectScreen.class, method = "open", paramtypez = {CardGroup.class, int.class, String.class, boolean.class, boolean.class, boolean.class, boolean.class})
public class CookingGridPatchCancelButton
{
    @SpirePrefixPatch
    public static void Prefix(GridCardSelectScreen obj, CardGroup group, int numCards, String tipMsg, boolean forUpgrade, boolean forTransform, boolean canCancel, boolean forPurge)
    {
        if (CookingHelper.gridScreenForCooking && canCancel)
        {
            AbstractDungeon.overlayMenu.cancelButton.show(GridCardSelectScreen.TEXT[1]);
        }
    }

    @SpirePostfixPatch
    public static void Postfix(GridCardSelectScreen obj, CardGroup group, int numCards, String tipMsg, boolean forUpgrade, boolean forTransform, boolean canCancel, boolean forPurge)
    {
        if (CookingHelper.gridScreenForCooking && canCancel)
        {

            obj.confirmButton.hideInstantly();
            obj.confirmButton.show();
            obj.confirmButton.updateText(obj.TEXT[0]);
            obj.confirmButton.isDisabled = true;
            //MizukiModCore.logger.info("confirmButtonState" + obj.confirmButton.isDisabled);
        }
    }
}