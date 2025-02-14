package actions;

import characters.Mizuki;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.DrawPileToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import patches.FoodCardColorEnumPatch;

import java.util.ArrayList;

public class AnalyzingAction extends AbstractGameAction
{
    private static final Logger logger = LogManager.getLogger(Mizuki.class);
    private AbstractPlayer player;
    private static final float DURATION = 0.1F;

    public AnalyzingAction()
    {
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.player = AbstractDungeon.player;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    public void update()
    {
        if (this.duration == this.startDuration)
        {
            if (this.player.drawPile.size() == 0)
            {
                isDone = true;
                return;
            }
            AbstractDungeon.cardRewardScreen.customCombatOpen(generateCardChoices(), CardRewardScreen.TEXT[1], true);
            tickDuration();
            return;
        }
        if (AbstractDungeon.cardRewardScreen.discoveryCard != null)
        {
            AbstractCard disCard = AbstractDungeon.cardRewardScreen.discoveryCard;
            this.player.drawPile.removeCard(disCard);
            disCard.current_x = -1000.0F * Settings.xScale;
            if (AbstractDungeon.player.hand.size() < 10)
            {
                AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(disCard, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
            }
            else
            {
                AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(disCard, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
            }
            AbstractDungeon.cardRewardScreen.discoveryCard = null;
        }
        isDone = true;
        tickDuration();
        /*
        for (AbstractMonster monster : (AbstractDungeon.getCurrRoom()).monsters.monsters)
        {
            if (!monster.isDeadOrEscaped() && monster.getIntentBaseDmg() >= 0)
            {
                for (int i = 0;i < amount;i++)
                {
                    addToBot((AbstractGameAction)new DrawPileToHandAction(this.amount, AbstractCard.CardType.SKILL));
                }
                isDone = true;
                tickDuration();
                return;
            }
        }
        for (int i = 0;i < amount;i++)
        {
            addToBot((AbstractGameAction)new DrawPileToHandAction(this.amount, AbstractCard.CardType.ATTACK));
        }
        isDone = true;
         */
        /*
        if (this.duration == this.startDuration)
        {
            for (AbstractMonster monster : (AbstractDungeon.getCurrRoom()).monsters.monsters)
            {
                if (!monster.isDeadOrEscaped() && monster.getIntentBaseDmg() >= 0)
                {
                    for (int i = 0;i < amount;i++)
                    {
                        AbstractCard c = AbstractDungeon.returnTrulyRandomCardInCombat(AbstractCard.CardType.SKILL).makeCopy();
                        for (;c.hasTag(AbstractCard.CardTags.HEALING) || c.hasTag(Mizuki.Enums.INGREDIENT_CARD) || c.hasTag(Mizuki.Enums.LEARNING_CARD);)
                        {
                            c = AbstractDungeon.returnTrulyRandomCardInCombat(AbstractCard.CardType.SKILL).makeCopy();
                        }
                        addToBot((AbstractGameAction) new MakeTempCardInHandAction(c));
                    }
                    isDone = true;
                    tickDuration();
                    return;
                }
            }
            for (int i = 0;i < amount;i++)
            {
                AbstractCard c = AbstractDungeon.returnTrulyRandomCardInCombat(AbstractCard.CardType.ATTACK).makeCopy();
                for (;c.hasTag(AbstractCard.CardTags.HEALING) || c.hasTag(Mizuki.Enums.INGREDIENT_CARD) || c.hasTag(Mizuki.Enums.LEARNING_CARD);)
                {
                    c = AbstractDungeon.returnTrulyRandomCardInCombat(AbstractCard.CardType.ATTACK).makeCopy();
                }
                addToBot((AbstractGameAction)new MakeTempCardInHandAction(c));
            }
            isDone = true;
            tickDuration();
        }
        tickDuration();*/
    }

    private ArrayList<AbstractCard> generateCardChoices()
    {
        ArrayList<AbstractCard> derp = new ArrayList<>();
        for (int i = 0;;i++)
        {
            if (i >= 3)
            {
                break;
            }
            if (derp.size() == this.player.drawPile.size())
            {
                break;
            }
            AbstractCard tmpc;
            do
            {
                tmpc = this.player.drawPile.getRandomCard(true);
            }
            while (derp.contains(tmpc));
            derp.add(tmpc);
        }
        return derp;
    }
}
