package patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;
import modcore.MizukiModCore;
import monsters.boss.beyond.ParanoiaIllusion;
import monsters.boss.city.SalVientoBishopQuintus;
import monsters.boss.city.TheEndspeaker;
import monsters.boss.exordium.Pathshaper;
import monsters.boss.exordium.SaintCarmen;
import monsters.boss.exordium.TidelinkedBishop;

public class OriginalModBossPatch
{
    @SpirePatch(clz = Exordium.class, method = "initializeBoss")
    public static class SetBoss1
    {
        public static void Postfix(AbstractDungeon obj)
        {
            if (MizukiModCore.originalMod)
            {
                AbstractDungeon.bossList.remove(SaintCarmen.ID);
                AbstractDungeon.bossList.remove(TidelinkedBishop.ID);
                AbstractDungeon.bossList.remove(Pathshaper.ID);
            }
        }
    }
    @SpirePatch(clz = TheCity.class, method = "initializeBoss")
    public static class SetBoss2
    {
        public static void Postfix(AbstractDungeon obj)
        {
            if (MizukiModCore.originalMod)
            {
                AbstractDungeon.bossList.remove(TheEndspeaker.ID);
                AbstractDungeon.bossList.remove(SalVientoBishopQuintus.ID);
            }
        }
    }
    @SpirePatch(clz = TheBeyond.class, method = "initializeBoss")
    public static class SetBoss3
    {
        public static void Postfix(AbstractDungeon obj)
        {
            if (MizukiModCore.originalMod)
            {
                AbstractDungeon.bossList.remove(ParanoiaIllusion.ID);
            }
        }
    }
}
