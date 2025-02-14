package patches;

import characters.Mizuki;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.map.DungeonMap;
import modcore.MizukiModCore;
import monsters.boss.beyond.ParanoiaIllusion;
import monsters.boss.city.SalVientoBishopQuintus;
import monsters.boss.city.TheEndspeaker;
import monsters.boss.exordium.Pathshaper;
import monsters.boss.exordium.SaintCarmen;
import monsters.boss.exordium.TidelinkedBishop;

import java.util.Collections;
import java.util.Random;

@SpirePatch(clz = AbstractDungeon.class, method = "setBoss")
public class BossSetPatch
{
    @SpirePrefixPatch
    public static void Prefix(AbstractDungeon __instance, String key)
    {
        /*
        if (__instance instanceof com.megacrit.cardcrawl.dungeons.Exordium && AbstractDungeon.player instanceof Mizuki)
        {
            AbstractDungeon.bossList.clear();
            AbstractDungeon.bossList.add(SaintCarmen.ID);
            AbstractDungeon.bossList.add(TidelinkedBishop.ID);
            //MizukiModCore.logger.info("setboss");
            Collections.shuffle(AbstractDungeon.bossList, new Random(AbstractDungeon.monsterRng.randomLong()));
        }*/
    }

    @SpirePostfixPatch
    public static void Postfix(AbstractDungeon __instance, String key)
    {
        if (key.equals(SaintCarmen.ID))
        {
            DungeonMap.boss = ImageMaster.loadImage("resources/img/UI/bossIcon/SaintCarmenIcon.png");
            DungeonMap.bossOutline = ImageMaster.loadImage("resources/img/UI/bossIcon/SaintCarmenIconOutline.png");
        }
        if (key.equals(TidelinkedBishop.ID))
        {
            DungeonMap.boss = ImageMaster.loadImage("resources/img/UI/bossIcon/TidelinkedBishopIcon.png");
            DungeonMap.bossOutline = ImageMaster.loadImage("resources/img/UI/bossIcon/TidelinkedBishopIconOutline.png");
        }
        if (key.equals(Pathshaper.ID))
        {
            DungeonMap.boss = ImageMaster.loadImage("resources/img/UI/bossIcon/PathshaperIcon.png");
            DungeonMap.bossOutline = ImageMaster.loadImage("resources/img/UI/bossIcon/PathshaperIconOutline.png");
        }
        if (key.equals(TheEndspeaker.ID))
        {
            DungeonMap.boss = ImageMaster.loadImage("resources/img/UI/bossIcon/TheEndspeakerIcon.png");
            DungeonMap.bossOutline = ImageMaster.loadImage("resources/img/UI/bossIcon/TheEndspeakerIconOutline.png");
        }
        if (key.equals(SalVientoBishopQuintus.ID))
        {
            DungeonMap.boss = ImageMaster.loadImage("resources/img/UI/bossIcon/SalVientoBishopQuintusIcon.png");
            DungeonMap.bossOutline = ImageMaster.loadImage("resources/img/UI/bossIcon/SalVientoBishopQuintusIconOutline.png");
        }
        if (key.equals(ParanoiaIllusion.ID))
        {
            DungeonMap.boss = ImageMaster.loadImage("resources/img/UI/bossIcon/ParanoiaIllusionIcon.png");
            DungeonMap.bossOutline = ImageMaster.loadImage("resources/img/UI/bossIcon/ParanoiaIllusionIconOutline.png");
        }
    }
}
