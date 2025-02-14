package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.audio.MusicMaster;
import com.megacrit.cardcrawl.screens.options.Slider;
import helper.EventHelper;

public class MusicStopPatch
{

    @SpirePatch(clz = MusicMaster.class, method = "fadeOutTempBGM")
    public static class MusicMasterFadeTempPatch
    {
        public static void Postfix(MusicMaster _inst)
        {
            EventHelper.musicHelper.fadeOut();
        }
    }

    @SpirePatch(clz = MusicMaster.class, method = "justFadeOutTempBGM")
    public static class MusicMasterJustFadeTempPatch
    {
        public static void Postfix(MusicMaster _inst)
        {
            EventHelper.musicHelper.fadeOut();
        }
    }

    @SpirePatch(clz = MusicMaster.class, method = "silenceTempBgmInstantly")
    public static class MusicMasterSilencePatch
    {
        public static void Postfix(MusicMaster _inst)
        {
            EventHelper.musicHelper.silence();
        }
    }

    @SpirePatch(clz = MusicMaster.class, method = "dispose")
    public static class MusicMasterDisposePatch
    {
        public static void Postfix(MusicMaster _inst)
        {
            EventHelper.musicHelper.kill();
        }
    }

    @SpirePatch(clz = MusicMaster.class, method = "fadeAll")
    public static class MusicMasterFadeAllPatch
    {
        public static void Postfix(MusicMaster _inst)
        {
            EventHelper.musicHelper.fadeOut();
        }
    }

    @SpirePatch(clz = MusicMaster.class, method = "updateVolume")
    public static class MusicMasterModifyPatch
    {
        public static void Postfix(MusicMaster _inst)
        {
            EventHelper.musicHelper.updateVolume();
        }
    }
}
