package actions;

import basemod.abstracts.AbstractCardModifier;
import basemod.cardmods.ExhaustMod;
import basemod.helpers.CardModifierManager;
import cards.AbstractFoodCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import helper.CookingHelper;
import patches.HyperplasticTentacleMod;

import java.util.ArrayList;
/*
public class SelectFoodAction extends AbstractGameAction
{
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("ReprogramAction");

    public static final String[] TEXT = uiStrings.TEXT;

    private float startingDuration;

    public SelectFoodAction()
    {
        this.startingDuration = Settings.ACTION_DUR_FAST;
        this.duration = this.startingDuration;
    }

    public void update()
    {
        if (this.duration == this.startingDuration)
        {
            CardGroup originalGroup = CookingHelper.getFoodInDeck();
            CardGroup tmpGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            CardGroup removeGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

            for (AbstractCard c : originalGroup.group)
            {
                tmpGroup.addToTop(c.makeSameInstanceOf());
            }

            for (AbstractCard c : AbstractDungeon.player.drawPile.group)
            {
                if (c instanceof AbstractFoodCard)
                {
                    removeGroup.addToTop(c);
                }
            }

            AbstractDungeon.player.drawPile.group.removeAll(removeGroup.group);

            AbstractDungeon.gridSelectScreen.open(tmpGroup, this.amount, true, TEXT[0]);
        }
        else if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty())
        {
            float x = Settings.WIDTH / 2.0F;
            float y = Settings.HEIGHT / 2.0F;
            int counter = 0;
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards)
            {
                int innercounter = counter++;
                addToTop(new AbstractGameAction()
                {
                    @Override
                    public void update()
                    {
                        //MizukiModCore.logger.info("size" + card.storeCard.size());
                        AbstractMonster selectm = null;
                        selectm = (AbstractMonster) (AbstractDungeon.getCurrRoom()).monsters.getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
                        AbstractDungeon.player.limbo.addToBottom(c);
                        c.current_x = x + innercounter * 10F;
                        c.current_y = y;
                        c.target_x = Settings.WIDTH / 2.0F + 300.0F * Settings.scale;
                        c.target_y = Settings.HEIGHT / 2.0F;
                        c.purgeOnUse = true;
                        AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(c, selectm, c.energyOnUse, true, true), true);
                        isDone = true;
                    }
                });
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
        tickDuration();
    }
}
*/