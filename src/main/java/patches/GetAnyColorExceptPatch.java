package patches;


import cards.AbstractFoodCard;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.events.exordium.Mushrooms;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.Instanceof;
import javassist.expr.MethodCall;
import modcore.MizukiModCore;

@SpirePatch(clz = CardLibrary.class, method = "getAnyColorCard", paramtypez={AbstractCard.CardRarity.class})
public class GetAnyColorExceptPatch
{
    public static ExprEditor Instrument()
    {

        return new ExprEditor()
        {
            public void edit(MethodCall m) throws CannotCompileException
            {
                if (m.getClassName().equals(CardGroup.class.getName()) && m.getMethodName().equals("addToBottom"))
                    m.replace("if (" + GetAnyColorExceptPatch.class.getName() + ".Do($1)) {$_ = $proceed($$);}");
            }
        };
    }

    public static boolean Do(AbstractCard card)
    {
        if (card instanceof AbstractFoodCard)
        {
            return false;
        }
        return true;
    }
}
