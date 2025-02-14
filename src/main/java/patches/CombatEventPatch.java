package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.events.exordium.Mushrooms;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.Instanceof;
import modcore.MizukiModCore;

@SpirePatch(clz = ProceedButton.class, method = "update")
public class CombatEventPatch
{
    public static ExprEditor Instrument()
    {

        return new ExprEditor()
        {
            public void edit(Instanceof i) throws CannotCompileException
            {
                try
                {
                    if (i.getType().getName().equals(Mushrooms.class.getName()))
                        i.replace("$_ = $proceed($$) || currentRoom.event instanceof events.BootyBay" +
                                " || currentRoom.event instanceof events.LingeringGlimmer" +
                                " || currentRoom.event instanceof events.PuppyDogEyes" +
                                " || currentRoom.event instanceof events.APassionforWork" +
                                " || currentRoom.event instanceof events.SomeTimetoKill" +
                                " || currentRoom.event instanceof events.DuckforceTest" +
                                " || currentRoom.event instanceof events.DancingChests" +
                                " || currentRoom.event instanceof events.BusinessEmpire;");
                }
                catch (NotFoundException e)
                {
                    MizukiModCore.logger.error("Combat proceed button patch broken.", (Throwable)e);
                }
            }
        };
    }
}
