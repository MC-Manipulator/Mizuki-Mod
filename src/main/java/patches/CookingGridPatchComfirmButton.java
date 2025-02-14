package patches;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import helper.CookingHelper;
import modcore.MizukiModCore;

import java.lang.reflect.Field;

//烹饪界面，按下按钮后就会完成烹饪

@SpirePatch(clz = GridCardSelectScreen.class, method = "render", paramtypez = {SpriteBatch.class})
public class CookingGridPatchComfirmButton
{
    private static float arrowScale1 = 1.0F, arrowScale2 = 1.0F, arrowScale3 = 1.0F, arrowTimer = 0.0F;

    @SpirePrefixPatch
    public static void Prefix(GridCardSelectScreen obj, SpriteBatch sb)
    {
        if (CookingHelper.gridScreenForCooking)
        {
            obj.confirmButton.render(sb);
        }
    }

    @SpireInsertPatch(loc = 811)
    public static SpireReturn<Void> Prefix3(GridCardSelectScreen obj, SpriteBatch sb)
    {
        if (CookingHelper.gridScreenForCooking)
        {
            float x = Settings.WIDTH / 2.0F - 73.0F * Settings.scale - 32.0F;
            sb.setColor(Color.WHITE);
            sb.draw(ImageMaster.UPGRADE_ARROW, x, Settings.HEIGHT / 2.0F - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, arrowScale1 * Settings.scale, arrowScale1 * Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
            x += 64.0F * Settings.scale;
            sb.setColor(Color.WHITE);
            sb.draw(ImageMaster.UPGRADE_ARROW, x, Settings.HEIGHT / 2.0F - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, arrowScale2 * Settings.scale, arrowScale2 * Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
            x += 64.0F * Settings.scale;
            sb.draw(ImageMaster.UPGRADE_ARROW, x, Settings.HEIGHT / 2.0F - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, arrowScale3 * Settings.scale, arrowScale3 * Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
            arrowTimer += Gdx.graphics.getDeltaTime() * 2.0F;
            arrowScale1 = 0.8F + (MathUtils.cos(arrowTimer) + 1.0F) / 8.0F;
            arrowScale2 = 0.8F + (MathUtils.cos(arrowTimer - 0.8F) + 1.0F) / 8.0F;
            arrowScale3 = 0.8F + (MathUtils.cos(arrowTimer - 1.6F) + 1.0F) / 8.0F;
            obj.confirmButton.render(sb);

            try
            {
                Class<?> C = GridCardSelectScreen.class;
                Field nameField = C.getDeclaredField("hoveredCard");
                Field nameField2 = C.getDeclaredField("upgradePreviewCard");
                nameField.setAccessible(true);
                nameField2.setAccessible(true);
                AbstractCard hoveredCard = (AbstractCard) nameField.get(obj);
                AbstractCard upgradePreviewCard = (AbstractCard) nameField2.get(obj);
                /*hoveredCard.current_x = Settings.WIDTH * 0.36F;
                hoveredCard.current_y = Settings.HEIGHT / 2.0F;
                hoveredCard.target_x = Settings.WIDTH * 0.36F;
                hoveredCard.target_y = Settings.HEIGHT / 2.0F;*/
                float i = 0;

                for (AbstractCard c : CookingHelper.display)
                {
                    c.current_x = Settings.WIDTH * (0.36F - i++ * 0.03F);
                    c.current_y = Settings.HEIGHT / 2.0F;
                    c.target_x = Settings.WIDTH * (0.36F - i++ * 0.03F);
                    c.target_y = Settings.HEIGHT / 2.0F;
                    c.render(sb);
                    c.updateHoverLogic();
                    c.renderCardTip(sb);
                }
                hoveredCard.current_x = Settings.WIDTH * 0.73F;
                hoveredCard.current_y = Settings.HEIGHT / 2.0F;
                hoveredCard.target_x = Settings.WIDTH * 0.73F;
                hoveredCard.target_y = Settings.HEIGHT / 2.0F;
                hoveredCard.render(sb);
                hoveredCard.updateHoverLogic();
                hoveredCard.renderCardTip(sb);

                upgradePreviewCard.current_x = Settings.WIDTH * 0.73F;
                upgradePreviewCard.current_y = Settings.HEIGHT / 2.0F;
                upgradePreviewCard.target_x = Settings.WIDTH * 0.73F;
                upgradePreviewCard.target_y = Settings.HEIGHT / 2.0F;
                upgradePreviewCard.render(sb);
                upgradePreviewCard.updateHoverLogic();
                upgradePreviewCard.renderCardTip(sb);
            }
            catch (Exception e)
            {
                MizukiModCore.logger.info(e);
            }


            return SpireReturn.Return();
        }
        return SpireReturn.Continue();
    }
}