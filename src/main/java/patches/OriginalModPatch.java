package patches;

import characters.Mizuki;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.MonsterInfo;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;
import com.megacrit.cardcrawl.ui.buttons.GridSelectConfirmButton;
import helper.CookingHelper;
import modcore.MizukiModCore;

import java.util.ArrayList;

@SpirePatch(clz = MonsterInfo.class, method = "normalizeWeights")
public class OriginalModPatch
{
    @SpireInsertPatch(loc = 22)
    public static void RemoveMonsters(@ByRef ArrayList<MonsterInfo>[] monsters)
    {
        if (((AbstractDungeon.player instanceof Mizuki) && MizukiModCore.originalMod) ||
                (!(AbstractDungeon.player instanceof Mizuki) && !MizukiModCore.Config.FightWithOthers.Get()))
        {

            MizukiModCore.logger.info("进入原版模式");
            ArrayList<String> removeList = new ArrayList<>();
            removeList.add("Mizuki:" + "DeepSeaSlider");
            removeList.add("Mizuki:" + "DeepSeaSlider_And_FungiBeast");
            removeList.add("Mizuki:" + "DeepSeaSlider_And_AcidSlime_M");
            removeList.add("Mizuki:" + "BoneSeaDrifter");
            removeList.add("Mizuki:" + "BoneSeaDrifter_And_FungiBeast");
            removeList.add("Mizuki:" + "BoneSeaDrifter_And_AcidSlime_M");
            removeList.add("Mizuki:" + "DeepSeaSlider_And_BoneSeaDrifter");
            removeList.add("Mizuki:" + "BalefulBroodling");
            removeList.add("Mizuki:" + "BasinSeaReaper");
            removeList.add("Mizuki:" + "2_BasinSeaReaper");
            removeList.add("Mizuki:" + "2_SkimmingSeaDrifter");
            removeList.add("Mizuki:" + "3_SkimmingSeaDrifter");
            removeList.add("Mizuki:" + "JawWorms_And_NetherseaSwarmcaller");
            removeList.add("Mizuki:" + "NetherseaReefbreaker");
            removeList.add("Mizuki:" + "3_NetherseaPredator");
            removeList.add("Mizuki:" + "2_PocketSeaCrawler");
            removeList.add("Mizuki:" + "TheFirstToTalk");

            for (int i = 0;i < monsters[0].size();i++)
            {
                MonsterInfo m = monsters[0].get(i);
                for (String n : removeList)
                {
                    if (m.name.equals(n))
                    {
                        monsters[0].remove(m);
                        i = 0;
                    }
                }
            }
        }
    }
}
