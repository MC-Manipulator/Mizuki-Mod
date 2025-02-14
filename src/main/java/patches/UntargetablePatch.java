package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.exordium.Mushrooms;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import com.megacrit.cardcrawl.ui.buttons.CancelButton;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;
import helper.CookingHelper;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.Instanceof;
import modcore.MizukiModCore;
import powers.StealthPower;

@SpirePatch(clz = AbstractCard.class, method = "cardPlayable")
public class UntargetablePatch
{
    @SpirePrefixPatch
    public static SpireReturn<Boolean> Prefix2(AbstractCard obj, AbstractMonster m)
    {
        if (((obj.target == AbstractCard.CardTarget.ENEMY || obj.target == AbstractCard.CardTarget.SELF_AND_ENEMY) && m != null && m.halfDead))
        {
            obj.cantUseMessage = null;
            AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new TalkAction(true, "...最好先不对那个敌人出手。", 2.0F, 2.0F));
            return SpireReturn.Return(false);
        }
        if (((obj.target == AbstractCard.CardTarget.ENEMY || obj.target == AbstractCard.CardTarget.SELF_AND_ENEMY) && m != null && m.hasPower(StealthPower.id)))
        {
            obj.cantUseMessage = null;
            AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new TalkAction(true, "...没办法对潜行中的敌人下手呢。", 2.0F, 2.0F));
            return SpireReturn.Return(false);
        }
        return SpireReturn.Continue();
    }
}
