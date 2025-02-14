package patches;

import basemod.ReflectionHacks;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.ui.buttons.GridSelectConfirmButton;
import helper.CookingHelper;
import modcore.MizukiModCore;

import javax.smartcardio.Card;
import java.lang.reflect.Field;

@SpirePatch(clz = UseCardAction.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractCard.class, AbstractCreature.class})
public class ShoesPatch
{

    @SpireInsertPatch(loc = 25)
    public static void Prefix1(UseCardAction obj, AbstractCard card, AbstractCreature target)
    {
        try
        {
            Class<?> C = UseCardAction.class;
            Field nameField = C.getDeclaredField("targetCard");
            nameField.setAccessible(true);
            AbstractCard c =  (AbstractCard) nameField.get(obj);
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

    @SpireInsertPatch(loc = 71)
    public static void Prefix2(UseCardAction obj, AbstractCard card, AbstractCreature target)
    {
        try
        {
            Class<?> C = UseCardAction.class;
            Field nameField = C.getDeclaredField("targetCard");
            nameField.setAccessible(true);
            AbstractCard c =  (AbstractCard) nameField.get(obj);
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
