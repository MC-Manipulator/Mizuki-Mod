package actions;

import characters.Mizuki;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;
import helper.EventHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import patches.FoodCardColorEnumPatch;

import java.util.ArrayList;

public class EchoOfExplorationAction extends AbstractGameAction
{
    private static final Logger logger = LogManager.getLogger(Mizuki.class);
    private boolean retrieveCard = false;
    private AbstractPlayer p;
    private int loseHP;
    private int distimes;

    private boolean isRandom;

    private boolean anyNumber;

    private boolean canPickZero;

    public static int numExhausted;

    private static final float DURATION = 0.1F;

    public EchoOfExplorationAction(int amount, boolean anyNumber, int loseHP, int distimes)
    {
        this.p = AbstractDungeon.player;
        this.anyNumber = anyNumber;
        this.distimes = distimes;
        this.amount = amount;
        this.loseHP = loseHP;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
    }

    public void update()
    {
        if (this.duration == this.startDuration)
        {
            AbstractDungeon.cardRewardScreen.customCombatOpen(generateCardChoices(), CardRewardScreen.TEXT[1], true);
            tickDuration();
            return;
        }
        if (AbstractDungeon.cardRewardScreen.discoveryCard != null)
        {
            AbstractCard disCard = AbstractDungeon.cardRewardScreen.discoveryCard.makeStatEquivalentCopy();
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
        else
        {
            if (loseHP != 0)
            {
                addToTop((AbstractGameAction)new EchoOfExplorationAction(amount, true, loseHP, distimes + 1));
                addToTop((AbstractGameAction)new LoseHPAction((AbstractCreature)p, (AbstractCreature)p, loseHP));
            }
        }
        isDone = true;
        tickDuration();
    }

    private ArrayList<AbstractCard> generateCardChoices()
    {
        ArrayList<AbstractCard> derp = new ArrayList<>();
        while (derp.size() != amount)
        {
            AbstractCard.CardRarity cardRarity;
            boolean dupe = false;

            int roll = AbstractDungeon.cardRandomRng.random(99);
            if (roll < 55)
            {
                cardRarity = AbstractCard.CardRarity.COMMON;
            }
            else if (roll < 85)
            {
                cardRarity = AbstractCard.CardRarity.UNCOMMON;
            }
            else
            {
                cardRarity = AbstractCard.CardRarity.RARE;
            }
            AbstractCard tmp;
            while (true)
            {
                tmp = CardLibrary.getAnyColorCard(cardRarity);
                if (tmp.color != Mizuki.Enums.MIZUKI_CARD && tmp.color != FoodCardColorEnumPatch.CardColorPatch.FOOD && !tmp.tags.contains(AbstractCard.CardTags.HEALING))
                {
                    break;
                }
            }

            for (AbstractCard c : derp)
            {
                if (c.cardID.equals(tmp.cardID))
                {
                    dupe = true;
                    break;
                }
            }
            if (!dupe)
            {
                AbstractCard tmp2 = tmp.makeCopy();
                if (tmp2.cost > distimes)
                {
                    tmp2.setCostForTurn(tmp2.cost - distimes);
                }
                else
                {
                    tmp2.setCostForTurn(0);
                }
                derp.add(tmp2);
            }
        }

        return derp;
    }
}

