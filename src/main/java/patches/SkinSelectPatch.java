package patches;


import basemod.ReflectionHacks;
import characters.Mizuki;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;
import ui.MizukiSkinSelectScreen;

public class SkinSelectPatch
{
    public static boolean isMizukiSelected()
    {
        return (CardCrawlGame.chosenCharacter == Mizuki.Enums.Mizuki && (
                (Boolean) ReflectionHacks.getPrivate(CardCrawlGame.mainMenuScreen.charSelectScreen, CharacterSelectScreen.class, "anySelected")).booleanValue());
    }

    @SpirePatch(clz = CharacterSelectScreen.class, method = "update")
    public static class UpdateButtonPatch
    {
        public static void Prefix(CharacterSelectScreen _inst)
        {
            if (SkinSelectPatch.isMizukiSelected())
            {
                MizukiSkinSelectScreen.Inst.update();
            }
        }
    }

    @SpirePatch(clz = CharacterSelectScreen.class, method = "render")
    public static class RenderButtonPatch
    {
        public static void Postfix(CharacterSelectScreen _inst, SpriteBatch sb)
        {
            if (SkinSelectPatch.isMizukiSelected())
            {
                MizukiSkinSelectScreen.Inst.render(sb);
            }
        }
    }
}
