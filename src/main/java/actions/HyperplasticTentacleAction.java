package actions;

import basemod.abstracts.AbstractCardModifier;
import basemod.cardmods.ExhaustMod;
import basemod.cardmods.RetainMod;
import basemod.helpers.CardModifierManager;
import cards.Attacks.HyperplasticTentacle;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.ReduceCostAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import patches.HyperplasticTentacleMod;
import patches.TowerOfTheWitchKingMod;

import javax.xml.soap.Text;
import java.util.ArrayList;

public class HyperplasticTentacleAction extends AbstractGameAction
{
    public static final String[] TEXT = (CardCrawlGame.languagePack.getUIString("BetterToHandAction")).TEXT;
    private AbstractPlayer player;
    private int numberOfCards;
    private boolean optional = false;
    private boolean upgraded = false;

    public HyperplasticTentacleAction(int numberOfCards, boolean upgraded)
    {
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.player = AbstractDungeon.player;
        this.numberOfCards = numberOfCards;
        this.upgraded = upgraded;
    }

    public void update()
    {
        if (this.duration == this.startDuration)
        {

            CardGroup cardsToMoveGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            ArrayList<AbstractCard> cardsToMove = new ArrayList<>();
            for (AbstractCard c : this.player.discardPile.group)
            {
                if (c.type == AbstractCard.CardType.ATTACK)
                {
                    cardsToMove.add(c);
                }
            }
            cardsToMoveGroup.group.addAll(cardsToMove);
            if (cardsToMoveGroup.group.isEmpty() || this.player.discardPile.isEmpty() || this.numberOfCards <= 0)
            {
                this.isDone = true;
                return;
            }

            if (this.player.discardPile.size() <= this.numberOfCards && !this.optional)
            {

                for (AbstractCard c : cardsToMove)
                {
                    if (this.player.hand.size() < 10)
                    {
                        AbstractCard tempcard = c.makeStatEquivalentCopy();
                        CardModifierManager.addModifier(tempcard, (AbstractCardModifier)new HyperplasticTentacleMod(upgraded));
                        CardModifierManager.addModifier(tempcard, (AbstractCardModifier)new ExhaustMod());
                        //addToBot((AbstractGameAction)new ReduceCostAction(tempcard));
                        addToBot((AbstractGameAction)new MakeTempCardInHandAction(tempcard));
                    }
                    c.lighten(false);
                }
                this.isDone = true;
                return;
            }
            if (this.numberOfCards == 1)
            {
                if (this.optional)
                {
                    AbstractDungeon.gridSelectScreen.open(cardsToMoveGroup, this.numberOfCards, true, TEXT[0]);
                }
                else
                {
                    AbstractDungeon.gridSelectScreen.open(cardsToMoveGroup, this.numberOfCards, TEXT[0], false);
                }
            }
            else if (this.optional)
            {
                AbstractDungeon.gridSelectScreen.open(cardsToMoveGroup, this.numberOfCards, true, TEXT[1] + this.numberOfCards + TEXT[2]);
            }
            else
            {
                AbstractDungeon.gridSelectScreen.open(cardsToMoveGroup, this.numberOfCards, TEXT[1] + this.numberOfCards + TEXT[2], false);
            }
            tickDuration();
            return;
        }
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty())
        {
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards)
            {
                if (this.player.hand.size() < 10)
                {
                    AbstractCard tempcard = c.makeCopy();
                    CardModifierManager.addModifier(tempcard, (AbstractCardModifier)new HyperplasticTentacleMod(upgraded));
                    CardModifierManager.addModifier(tempcard, (AbstractCardModifier)new ExhaustMod());
                    //addToBot((AbstractGameAction)new ReduceCostAction(tempcard));
                    addToBot((AbstractGameAction)new MakeTempCardInHandAction(tempcard));
                }
                c.lighten(false);
                c.unhover();
            }
            for (AbstractCard c : this.player.discardPile.group)
            {
                c.unhover();
                c.target_x = CardGroup.DISCARD_PILE_X;
                c.target_y = 0.0F;
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            AbstractDungeon.player.hand.refreshHandLayout();
        }
        tickDuration();
    }
}
