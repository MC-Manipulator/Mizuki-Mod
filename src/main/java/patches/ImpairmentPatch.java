package patches;

import Impairment.AbstractImpairment;
import basemod.BaseMod;
import basemod.interfaces.PreMonsterTurnSubscriber;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.characters.Ironclad;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import helper.EventHelper;
import helper.ImpairementManager;
import modcore.MizukiModCore;

import java.util.ArrayList;

public class ImpairmentPatch
{
    /*
    @SpirePatch(clz = AbstractMonster.class, method = "init")
    public static class DroneInitPatch {
        public static void Postfix(AbstractMonster _inst) {
            ((ArrayList)ImpairmentPatch.ImpairmentFieldsPatch.impairments.get(_inst)).clear();
            ImparementManager.setNervousImpairmentOnMonster(_inst);
        }
    }*/
    @SpirePatch(clz = AbstractCreature.class, method = "<class>")
    public static class ImpairmentFieldsPatch
    {
        public static SpireField<ArrayList<AbstractImpairment>> impairments
                = new SpireField(() -> new ArrayList<>());
    }
    @SpirePatch(clz = AbstractRoom.class, method = "render", paramtypez = {SpriteBatch.class})
    public static class MonsterRenderOrbPatch
    {
        @SpireInsertPatch(rloc = 19)
        public static void Insert(AbstractRoom _inst, SpriteBatch sb)
        {
            if (_inst.monsters != null)
            {
                sb.setColor(Color.WHITE);
                for (AbstractCreature mo : (AbstractDungeon.getMonsters()).monsters)
                {
                    if (((AbstractCreature)mo != null
                            && !((AbstractCreature)mo).isDeadOrEscaped()
                            && !((AbstractCreature)mo).isDead))
                    {
                        for (AbstractImpairment ipm : ImpairmentFieldsPatch.impairments.get(mo))
                        {
                            ipm.render(sb);
                        }
                    }

                    ImpairementManager.setUIOnCreature(mo, null);
                }
            }

            for (AbstractImpairment ipm : ImpairmentFieldsPatch.impairments.get(AbstractDungeon.player))
            {
                ipm.render(sb);
            }
            ImpairementManager.setUIOnCreature(AbstractDungeon.player, null);
        }
    }
}
