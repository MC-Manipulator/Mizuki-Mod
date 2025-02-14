package actions;

import basemod.abstracts.AbstractCardModifier;
import basemod.cardmods.ExhaustMod;
import basemod.cardmods.RetainMod;
import basemod.helpers.CardModifierManager;
import basemod.interfaces.OnStartBattleSubscriber;
import cards.Skills.TowerOfTheWitchKing;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import patches.TowerOfTheWitchKingMod;

import java.util.ArrayList;

public class TowerOfTheWitchKingAction extends AbstractGameAction
{
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("ArmamentsAction");

    public static final String[] TEXT = uiStrings.TEXT;

    private AbstractPlayer p;

    private ArrayList<AbstractCard> cannotUpgrade = new ArrayList<>();

    public TowerOfTheWitchKing sourceCard;

    public boolean isupgraded;

    public TowerOfTheWitchKingAction(boolean isupgraded, TowerOfTheWitchKing sourceCard)
    {
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.p = AbstractDungeon.player;
        this.duration = Settings.ACTION_DUR_FAST;
        this.isupgraded = isupgraded;
        this.sourceCard = sourceCard;
    }

    public void update()
    {
        if (this.duration == Settings.ACTION_DUR_FAST)
        {
            for (AbstractCard c : this.p.hand.group)
            {
                if (!c.canUpgrade() || c.type != AbstractCard.CardType.ATTACK)
                    this.cannotUpgrade.add(c);
            }
            if (this.cannotUpgrade.size() == this.p.hand.group.size())
            {
                this.isDone = true;
                return;
            }
            if (this.p.hand.group.size() - this.cannotUpgrade.size() == 1)
                for (AbstractCard c : this.p.hand.group)
                {
                    if (c.canUpgrade() && c.type == AbstractCard.CardType.ATTACK)
                    {
                        c.upgrade();
                        c.superFlash();
                        c.applyPowers();
                        setUpCard(c);
                        CardModifierManager.addModifier(c, (AbstractCardModifier)new TowerOfTheWitchKingMod(c, sourceCard));
                        CardModifierManager.addModifier(c, (AbstractCardModifier)new RetainMod());
                        this.isDone = true;
                        return;
                    }
                }
            this.p.hand.group.removeAll(this.cannotUpgrade);
            if (this.p.hand.group.size() > 1)
            {
                AbstractDungeon.handCardSelectScreen.open(TEXT[0], 1, false, false, false, true);
                tickDuration();
                return;
            }
            if (this.p.hand.group.size() == 1)
            {
                this.p.hand.getTopCard().upgrade();
                this.p.hand.getTopCard().superFlash();
                returnCards();
                this.isDone = true;
            }
        }
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved)
        {
            for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group)
            {
                c.upgrade();
                c.superFlash();
                c.applyPowers();

                CardModifierManager.addModifier(c, (AbstractCardModifier)new TowerOfTheWitchKingMod(c, sourceCard));
                CardModifierManager.addModifier(c, (AbstractCardModifier)new RetainMod());
                setUpCard(c);
                this.p.hand.addToTop(c);
            }
            returnCards();
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            this.isDone = true;
        }
        tickDuration();
    }

    private void returnCards()
    {
        for (AbstractCard c : this.cannotUpgrade)
            this.p.hand.addToTop(c);
        this.p.hand.refreshHandLayout();
    }

    private void setUpCard(AbstractCard card)
    {
        if (sourceCard.Storecard1 == null)
        {
            sourceCard.Storecard1 = card;
        }
        if (sourceCard.Storecard1 != null && isupgraded)
        {
            sourceCard.Storecard2 = card;
        }
    }
}
