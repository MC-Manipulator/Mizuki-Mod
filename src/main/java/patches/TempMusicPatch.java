package patches;

import com.badlogic.gdx.audio.Music;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.audio.MainMusic;
import com.megacrit.cardcrawl.audio.TempMusic;
import modcore.MizukiModCore;

@SpirePatch(clz = TempMusic.class, method = "getSong")
public class TempMusicPatch
{
    @SpirePostfixPatch
    public static SpireReturn<Music> Prefix(TempMusic __instance, String key)
    {
        if (key.equals("MIZUKI_BOSS1_INTRO"))
            return SpireReturn.Return(MainMusic.newMusic("resources/music/m_bat_rglk2normal2_intro.ogg"));
        if (key.equals("MIZUKI_BOSS1_LOOP"))
            return SpireReturn.Return(MainMusic.newMusic("resources/music/m_bat_rglk2normal2_loop.ogg"));

        if (key.equals("MIZUKI_BOSS2_1_INTRO"))
            return SpireReturn.Return(MainMusic.newMusic("resources/music/m_bat_dsdevr_intro.ogg"));
        if (key.equals("MIZUKI_BOSS2_1_LOOP"))
            return SpireReturn.Return(MainMusic.newMusic("resources/music/m_bat_dsdevr_loop.ogg"));

        if (key.equals("MIZUKI_BOSS2_2_INTRO"))
            return SpireReturn.Return(MainMusic.newMusic("resources/music/m_bat_abyssalhunters_intro.ogg"));
        if (key.equals("MIZUKI_BOSS2_2_LOOP"))
            return SpireReturn.Return(MainMusic.newMusic("resources/music/m_bat_abyssalhunters_loop.ogg"));

        if (key.equals("MIZUKI_BOSS2_3_LOOP"))
            return SpireReturn.Return(MainMusic.newMusic("resources/music/m_bat_dsbish.ogg"));

        if (key.equals("MIZUKI_BOSS3_1_INTRO"))
            return SpireReturn.Return(MainMusic.newMusic("resources/music/m_bat_rglk2boss1_intro.ogg"));
        if (key.equals("MIZUKI_BOSS3_1_LOOP"))
            return SpireReturn.Return(MainMusic.newMusic("resources/music/m_bat_rglk2boss1_loop.ogg"));

        if (key.equals("MIZUKI_BOSS3_2_INTRO"))
            return SpireReturn.Return(MainMusic.newMusic("resources/music/m_bat_rglk2boss2_intro.ogg"));
        if (key.equals("MIZUKI_BOSS3_2_LOOP"))
            return SpireReturn.Return(MainMusic.newMusic("resources/music/m_bat_rglk2boss2_loop.ogg"));

        if (key.equals("MIZUKI_BOSS4_INTRO"))
            return SpireReturn.Return(MainMusic.newMusic("resources/music/m_bat_rglk2boss3_intro.ogg"));
        if (key.equals("MIZUKI_BOSS4_LOOP"))
            return SpireReturn.Return(MainMusic.newMusic("resources/music/m_bat_rglk2boss3_loop.ogg"));

        return SpireReturn.Continue();
    }
}
