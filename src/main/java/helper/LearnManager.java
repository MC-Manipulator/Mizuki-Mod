package helper;

import cards.AbstractMizukiCard;
import cards.Skills.AdaptiveEvolution;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.UUID;

public class LearnManager
{
    public static void CounterIncreaseAndCheck(AbstractMizukiCard c)
    {
        CounterIncreaseAndCheck(c, 0);
    }
    //M4为学习上限，M3为计数器需求数，M2为计数器，M为学习数
    public static void CounterIncreaseAndCheck(AbstractMizukiCard c, int count)
    {
        if (c.magicNumber >= c.magicNumber4 && c.magicNumber4 != -1)
            return;

        c.magicNumber2 += count;
        c.baseMagicNumber2 += count;
        triggerLearnCheck(c);
    }
    public static void triggerLearnCheck(AbstractMizukiCard c)
    {

        if (c.magicNumber == c.magicNumber4 && c.magicNumber4 != -1)
            return;
        if (c.magicNumber > c.magicNumber4 && c.magicNumber4 != -1)
        {
            c.magicNumber = c.magicNumber4;
            c.baseMagicNumber = c.baseMagicNumber4;
        }
        if (c.magicNumber2 >= c.magicNumber3)
        {
            c.magicNumber++;
            c.baseMagicNumber++;
            c.superFlash();
            c.magicNumber2 = 0;
            c.baseMagicNumber2 = 0;
            EventHelper.ON_LEARN_SUBSCRIBERS.forEach(sub -> sub.OnLearn(c.uuid));
        }
    }
    public static boolean synchronise(AbstractMizukiCard card)
    {
        //学习牌，战斗中的牌与牌组中的牌同步
        if (!ifInMasterDeck(card))
        {
            AbstractMizukiCard cardindeck = (AbstractMizukiCard)findInMasterDeck(card);
            if (cardindeck != null)
            {
                card.magicNumber = cardindeck.magicNumber;
                card.magicNumber2 = cardindeck.magicNumber2;
                card.magicNumber3 = cardindeck.magicNumber3;
                card.magicNumber4 = cardindeck.magicNumber4;

                card.baseMagicNumber = cardindeck.baseMagicNumber;
                card.baseMagicNumber2 = cardindeck.baseMagicNumber2;
                card.baseMagicNumber3 = cardindeck.baseMagicNumber3;
                card.baseMagicNumber4 = cardindeck.baseMagicNumber4;
            }
            return false;
        }
        return true;
    }
    public static boolean ifInMasterDeck(AbstractCard card)
    {
        return AbstractDungeon.player.masterDeck.group.contains(card);
    }
    public static AbstractCard findInMasterDeck(AbstractCard card)
    {
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group)
        {
            if (c.uuid.equals(card.uuid))
                return c;
        }
        return null;
    }
    public static AbstractCard findInMasterDeck(UUID uuid)
    {
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group)
        {
            if (c.uuid.equals(uuid))
                return c;
        }
        return null;
    }
}
