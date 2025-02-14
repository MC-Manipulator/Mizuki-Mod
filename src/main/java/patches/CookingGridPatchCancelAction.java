package patches;



import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import com.megacrit.cardcrawl.ui.buttons.CancelButton;
import helper.CookingHelper;
import modcore.MizukiModCore;

import java.lang.reflect.Field;


@SpirePatch(clz = CancelButton.class, method = "update")
public class CookingGridPatchCancelAction
{
    @SpirePrefixPatch
    public static void Prefix(CancelButton obj)
    {
        if (!obj.isHidden)
        {
            obj.hb.update();
            if (obj.hb.clicked || ((InputHelper.pressedEscape || CInputActionSet.cancel.isJustPressed()) && obj.current_x != CancelButton.HIDE_X))
            {
                if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.GRID && CookingHelper.gridScreenForCooking &&
                        !AbstractDungeon.gridSelectScreen.confirmScreenUp)
                {
                    MizukiModCore.logger.info("一级界面点击取消");
                    CookingHelper.gridScreenForCooking = false;
                    CookingHelper.ableToCook = false;
                    AbstractDungeon.closeCurrentScreen();
                    for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards)
                        c.stopGlowing();
                    if (AbstractDungeon.getCurrRoom() instanceof RestRoom)
                    {
                        RestRoom r = (RestRoom)AbstractDungeon.getCurrRoom();
                        r.campfireUI.reopen();
                    }
                }

            }
        }
    }
}
