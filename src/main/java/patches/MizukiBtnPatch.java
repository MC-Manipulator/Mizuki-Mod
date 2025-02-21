package patches;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomSavable;
import characters.Mizuki;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;
import modcore.MizukiModCore;

import java.util.ArrayList;

public class MizukiBtnPatch
{
    public static final Hitbox challengeDownHitbox = new Hitbox(40.0F * Settings.scale * 0.991F, 40.0F * Settings.scale);

    public static final ArrayList<PowerTip> challengeTips = new ArrayList<>();

    public static final UIStrings uiStrings = (CardCrawlGame.languagePack.getUIString(MizukiModCore.MakePath(MizukiBtnPatch.class.getSimpleName())));

    @SpirePatch(clz = CharacterSelectScreen.class, method = "render")
    public static class RenderBtn
    {
        public static void Postfix(CharacterSelectScreen obj, SpriteBatch sb)
        {
            if (SkinSelectPatch.isMizukiSelected())
            {
                MizukiBtnPatch.challengeDownHitbox.move(190.0F * Settings.scale, Settings.HEIGHT / 2.0F - 190.0F * Settings.scale);
                MizukiBtnPatch.challengeDownHitbox.render(sb);
                sb.setColor(Color.WHITE);
                sb.draw(ImageMaster.CHECKBOX, MizukiBtnPatch.challengeDownHitbox.cX - 32.0F, MizukiBtnPatch.challengeDownHitbox.cY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale * 0.991F, Settings.scale * 0.991F, 0.0F, 0, 0, 64, 64, false, false);
                if (MizukiModCore.originalMod)
                    sb.draw(ImageMaster.TICK, MizukiBtnPatch.challengeDownHitbox.cX - 32.0F, MizukiBtnPatch.challengeDownHitbox.cY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale * 0.991F, Settings.scale * 0.991F, 0.0F, 0, 0, 64, 64, false, false);
                FontHelper.renderSmartText(sb, FontHelper.tipHeaderFont, MizukiBtnPatch.uiStrings.TEXT[0], MizukiBtnPatch.challengeDownHitbox.cX + 25.0F * Settings.scale, MizukiBtnPatch.challengeDownHitbox.cY, Settings.BLUE_TEXT_COLOR);
            }
        }
    }


    @SpirePatch(clz = CharacterSelectScreen.class, method = "update")
    public static class UpdateHitbox
    {
        public static void Prefix(CharacterSelectScreen obj)
        {
            if (SkinSelectPatch.isMizukiSelected())
            {
                MizukiBtnPatch.challengeDownHitbox.update();
                if (MizukiBtnPatch.challengeDownHitbox.hovered)
                {
                    if (MizukiBtnPatch.challengeTips.isEmpty())
                        MizukiBtnPatch.challengeTips.add(new PowerTip(MizukiBtnPatch.uiStrings.TEXT[0], MizukiBtnPatch.uiStrings.TEXT[1]));
                    if (InputHelper.mX < 1400.0F * Settings.scale)
                    {
                        TipHelper.queuePowerTips(InputHelper.mX + 60.0F * Settings.scale, InputHelper.mY - 50.0F * Settings.scale, MizukiBtnPatch.challengeTips);
                    }
                    else
                    {
                        TipHelper.queuePowerTips(InputHelper.mX - 350.0F * Settings.scale, InputHelper.mY - 50.0F * Settings.scale, MizukiBtnPatch.challengeTips);
                    }
                    if (InputHelper.justClickedLeft)
                    {
                        CardCrawlGame.sound.playA("UI_CLICK_1", -0.4F);
                        MizukiBtnPatch.challengeDownHitbox.clickStarted = true;
                    }
                    if (MizukiBtnPatch.challengeDownHitbox.clicked)
                    {
                        MizukiModCore.originalMod = !MizukiModCore.originalMod;
                        MizukiModCore.Config.SaveOriginalMod();
                        MizukiBtnPatch.challengeDownHitbox.clicked = false;
                    }
                }
            }
        }
    }
}