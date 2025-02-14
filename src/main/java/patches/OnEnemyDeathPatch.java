package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import helper.EventHelper;
import modcore.MizukiModCore;
import powers.StealthPower;
import powers.StunMonsterPower;

public class OnEnemyDeathPatch
{

    @SpirePatch(clz = AbstractMonster.class, method = "die", paramtypez = {boolean.class})
    public static class Execute
    {
        public static SpireReturn<Void> Prefix(AbstractMonster __instance, boolean triggerRelics)
        {
            EventHelper.ON_ENEMYDEATH_SUBSCRIBERS.forEach(sub -> sub.OnEnemyDeath(__instance));
            return SpireReturn.Continue();
        }
    }
}
