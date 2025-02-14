package patches;

import cards.Skills.CramUp;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import helper.EventHelper;
import javassist.CtBehavior;
import modcore.MizukiModCore;

public class CramUpDisplayCards
{

    public static CramUp card = null;
    public static boolean isChoosing = false;

    public static boolean isShowingCards = false;
    public static void showStoreCards(AbstractCard card)
    {
        if (card instanceof CramUp)
        {
            CramUpDisplayCards.card = (CramUp) card;
            CramUp c = (CramUp) card;
            if (c.storeCard.size() > 0 && !isShowingCards)
            {
                for (int i = 0; i < c.storeCard.size(); i++)
                {
                    AbstractCard target = c.storeCard.get(i).makeStatEquivalentCopy();
                    target.drawScale = 0.6F;
                    target.targetDrawScale = 0.6F;
                    target.current_x = c.current_x - AbstractCard.IMG_WIDTH * c.storeCard.size() + AbstractCard.IMG_WIDTH * i;
                            //Settings.WIDTH + AbstractCard.IMG_WIDTH;
                    //target.target_x = Settings.WIDTH - 200.0F * Settings.scale;

                    target.target_x = c.current_x - AbstractCard.IMG_WIDTH * c.storeCard.size() + AbstractCard.IMG_WIDTH * i;
                    target.current_y = c.current_y + AbstractCard.IMG_HEIGHT + (50.0F) * Settings.scale;
                    target.target_y = c.current_y + AbstractCard.IMG_HEIGHT;
                            //(150.0F + 30.0F * i) * Settings.scale;
                    //target.target_y = (150.0F + 30.0F * i) * Settings.scale;
                    target.transparency = 0.3f;
                    target.targetTransparency = 1f;
                    EventHelper.showCards.add(0, target);
                }
                isShowingCards = true;
            }
        }
    }

    public static void releaseCard()
    {
        /*
        if (isChoosing)
        {
            if ((AbstractDungeon.getMonsters()).monsters.contains(Chosen))
                (AbstractDungeon.getMonsters()).monsters.remove(Chosen);
            isChoosing = false;
            Hpr.info("have removed monster");
        }*/
        if (isShowingCards)
        {
            EventHelper.showCards.clear();
            isShowingCards = false;
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "updateInput")
    public static class ShowAttractNumPatch1
    {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(AbstractPlayer _inst)
        {
            CramUpDisplayCards.showStoreCards(_inst.hoveredCard);
        }

        public static class Locator extends SpireInsertLocator
        {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher.MethodCallMatcher matcher = new Matcher.MethodCallMatcher(AbstractCard.class, "flash");
                return LineFinder.findInOrder(ctMethodToPatch, (Matcher)matcher);
            }
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "manuallySelectCard")
    public static class ShowAttractNumPatch2
    {
        public static void Postfix(AbstractPlayer _inst, AbstractCard card)
        {
            CramUpDisplayCards.showStoreCards(card);
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "playCard")
    public static class RemoveTargetPatch1
    {
        public static void Postfix(AbstractPlayer _inst)
        {
            CramUpDisplayCards.releaseCard();
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "releaseCard")
    public static class RemoveTargetPatch2
    {
        public static void Postfix(AbstractPlayer _inst)
        {
            CramUpDisplayCards.releaseCard();
        }
    }
}
