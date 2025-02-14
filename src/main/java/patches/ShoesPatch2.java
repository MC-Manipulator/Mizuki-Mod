package patches;

import basemod.helpers.CardModifierManager;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import modcore.MizukiModCore;

import java.lang.reflect.Field;

@SpirePatch(clz = UseCardAction.class, method = "update")

public class ShoesPatch2
{
    @SpireInsertPatch(loc = 85)
    public static void Prefix1(UseCardAction obj)
    {
        try
        {
            Class<?> C = UseCardAction.class;
            Field nameField = C.getDeclaredField("targetCard");
            nameField.setAccessible(true);
            AbstractCard c = (AbstractCard) nameField.get(obj);
            if (CardModifierManager.hasModifier(c, "Mizuki:PureWhiteDanceShoesMod"))
            {
                c.type = AbstractCard.CardType.SKILL;
                nameField.set(obj, c);
            }
            if (CardModifierManager.hasModifier(c, "Mizuki:JetBlackDanceShoesMod"))
            {
                c.type = AbstractCard.CardType.ATTACK;
                nameField.set(obj, c);
            }
        }
        catch (Exception e)
        {
            MizukiModCore.logger.info(e);
        }
    }

    @SpireInsertPatch(loc = 155)
    public static void Prefix2(UseCardAction obj)
    {
        try
        {
            Class<?> C = UseCardAction.class;
            Field nameField = C.getDeclaredField("targetCard");
            nameField.setAccessible(true);
            AbstractCard c = (AbstractCard) nameField.get(obj);
            if (CardModifierManager.hasModifier(c, "Mizuki:PureWhiteDanceShoesMod"))
            {
                c.type = AbstractCard.CardType.ATTACK;
                nameField.set(obj, c);
            }
            if (CardModifierManager.hasModifier(c, "Mizuki:JetBlackDanceShoesMod"))
            {
                c.type = AbstractCard.CardType.SKILL;
                nameField.set(obj, c);
            }
        }
        catch (Exception e)
        {
            MizukiModCore.logger.info(e);
        }
    }
}
