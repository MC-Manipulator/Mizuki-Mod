package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.megacrit.cardcrawl.ui.buttons.CancelButton;
import com.megacrit.cardcrawl.ui.buttons.GridSelectConfirmButton;
import helper.CookingHelper;
import modcore.MizukiModCore;
import stances.UntargetableStance;

import java.lang.reflect.Field;
import java.util.ArrayList;


@SpirePatch(clz = GridCardSelectScreen.class, method = "update")
public class CookingGridPatchComfirmAction
{
    @SpireInsertPatch(loc = 132)
    public static void Prefix(GridCardSelectScreen obj)
    {
        if (CookingHelper.gridScreenForCooking)
        {
            if (obj.selectedCards.size() < 3)
            {
                obj.confirmButton.isDisabled = true;
            }
            if (obj.selectedCards.size() >= 3)
            {
                obj.confirmButton.isDisabled = false;
            }
            if (obj.confirmButton.hb.clicked && !obj.confirmButton.isDisabled && !obj.confirmScreenUp && !CookingHelper.confirmScreenForCooking)
            {
                try
                {
                    MizukiModCore.logger.info("一级界面点击确定");
                    CookingHelper.display = new ArrayList<>();
                    for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards)
                    {
                        CookingHelper.display.add(c.makeStatEquivalentCopy());
                    }
                    CookingHelper.confirmScreenForCooking = true;
                    obj.confirmScreenUp = true;
                    obj.confirmButton.hb.clicked = false;
                    Class<?> C = GridCardSelectScreen.class;
                    Field nameField = C.getDeclaredField("hoveredCard");
                    Field nameField2 = C.getDeclaredField("upgradePreviewCard");
                    nameField.setAccessible(true);
                    nameField2.setAccessible(true);
                    AbstractCard cardtoput = CookingHelper.getFood(AbstractDungeon.gridSelectScreen.selectedCards).makeStatEquivalentCopy();

                    nameField.set(obj, cardtoput);
                    nameField2.set(obj, cardtoput);

                }
                catch (Exception e)
                {
                    MizukiModCore.logger.info(e);
                }
                AbstractDungeon.overlayMenu.cancelButton.show(GridCardSelectScreen.TEXT[1]);
                /*
                if (obj.selectedCards.size() == 3)
                {
                    CookingHelper.ableToCook = true;
                }
                AbstractDungeon.overlayMenu.cancelButton.hide();
                obj.confirmButton.hb.clicked = false;
                for (AbstractCard c : obj.selectedCards)
                    c.stopGlowing();
                AbstractDungeon.closeCurrentScreen();
                obj.confirmButton.isDisabled = false;*/
            }
            if (obj.confirmButton.hb.clicked && !obj.confirmButton.isDisabled && obj.confirmScreenUp)
            {
                CookingHelper.confirmScreenForCooking = false;
                if (obj.selectedCards.size() == 3)
                {
                    CookingHelper.ableToCook = true;
                }
                AbstractDungeon.overlayMenu.cancelButton.hide();
                obj.confirmButton.hb.clicked = false;
                for (AbstractCard c : obj.selectedCards)
                    c.stopGlowing();
                AbstractDungeon.closeCurrentScreen();
                obj.confirmButton.isDisabled = false;
            }
        }
    }

    @SpireInsertPatch(loc = 162)
    public static void Prefix2(GridCardSelectScreen obj)
    {
        if (CookingHelper.gridScreenForCooking)
        {
            if (obj.selectedCards.size() == 4)
            {
                try
                {
                    Class<?> C = GridCardSelectScreen.class;
                    Field nameField = C.getDeclaredField("cardSelectAmount");
                    nameField.setAccessible(true);
                    int cardSelectAmount = (int) nameField.get(obj);
                    nameField.set(obj, cardSelectAmount - 1);
                    obj.selectedCards.get(0).stopGlowing();
                    obj.selectedCards.remove(0);
                }
                catch (Exception e)
                {
                    MizukiModCore.logger.info(e);
                }
            }
        }
    }
}