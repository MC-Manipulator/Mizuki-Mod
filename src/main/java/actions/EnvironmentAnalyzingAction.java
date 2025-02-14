package actions;

import characters.Mizuki;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

import com.megacrit.cardcrawl.core.Settings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.returnTrulyRandomCardInCombat;

public class EnvironmentAnalyzingAction extends AbstractGameAction
{
    private static final Logger logger = LogManager.getLogger(Mizuki.class);

    private AbstractPlayer p;

    private boolean isRandom;

    private boolean anyNumber;

    private boolean canPickZero;

    public static int numExhausted;

    private static final float DURATION = 0.1F;

    public EnvironmentAnalyzingAction(int amount, boolean isRandom, boolean anyNumber, boolean canPickZero)
    {
        this.p = AbstractDungeon.player;
        this.anyNumber = anyNumber;
        this.canPickZero = canPickZero;
        this.isRandom = isRandom;
        this.amount = amount;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.EXHAUST;
    }

    public void update()
    {
        if (this.duration == this.startDuration)
        {
            if (this.p.hand.size() == 0)
            {
                this.isDone = true;
                return;
            }
            if (!this.anyNumber &&
                    this.p.hand.size() <= this.amount)
            {
                this.amount = this.p.hand.size();
                numExhausted = this.amount;
                int tmp = this.p.hand.size();
                for (int i = 0; i < tmp; i++) {
                    AbstractCard c = this.p.hand.getTopCard();
                    this.p.hand.moveToExhaustPile(c);
                }
                CardCrawlGame.dungeon.checkForPactAchievement();
                return;
            }
            if (this.isRandom)
            {
                for (int i = 0; i < this.amount; i++)
                    this.p.hand.moveToExhaustPile(this.p.hand.getRandomCard(AbstractDungeon.cardRandomRng));
                CardCrawlGame.dungeon.checkForPactAchievement();
            }
            else
            {
                numExhausted = this.amount;
                AbstractDungeon.handCardSelectScreen.open("消耗", this.amount, this.anyNumber, this.canPickZero);
                tickDuration();
                return;
            }
        }
        int number = 0;
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved)
        {
            number = AbstractDungeon.handCardSelectScreen.selectedCards.group.size();
            for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group)
                this.p.hand.moveToExhaustPile(c);
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
        }
        tickDuration();
        addToBot((AbstractGameAction)new AnalyzingAction());
        isDone = true;
        tickDuration();
    }
}

