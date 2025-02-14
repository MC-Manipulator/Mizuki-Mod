package cards;

import characters.Mizuki;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import helper.LearnManager;
import patches.FoodCardColorEnumPatch;

public abstract class AbstractFoodCard extends AbstractMizukiCard
{
    public boolean hasEatenUp;
    public int eatableTimes;

    public AbstractFoodCard(String ID, CardStrings strings)
    {
        super(ID, false, strings, 0, CardType.POWER, FoodCardColorEnumPatch.CardColorPatch.FOOD, CardRarity.SPECIAL, CardTarget.SELF);
        tags.add(CardTags.HEALING);
        tags.add(Mizuki.Enums.FOOD_CARD);
        this.hasEatenUp = false;
    }

    protected void setBasicFood()
    {
        this.cost = 0;
        this.costForTurn = cost;
        this.rarity = CardRarity.BASIC;
    }

    protected void setCommonFood()
    {
        this.cost = 0;
        this.costForTurn = cost;
        this.rarity = CardRarity.COMMON;
    }

    protected void setUncommonFood()
    {
        this.cost = 0;
        this.costForTurn = cost;
        this.rarity = CardRarity.UNCOMMON;
    }

    protected void setRareFood()
    {
        this.cost = 0;
        this.costForTurn = cost;
        this.rarity = CardRarity.RARE;
    }

    protected void synchronize()
    {
        if (!LearnManager.ifInMasterDeck(this))
        {
            AbstractFoodCard c = ((AbstractFoodCard)LearnManager.findInMasterDeck(this));
            if (c != null)
            {
                this.baseMagicNumber = c.baseMagicNumber;
                this.magicNumber = c.magicNumber;
            }
        }
        this.eatableTimes = this.baseMagicNumber;
        this.magicNumber = this.baseMagicNumber;
        if (LearnManager.ifInMasterDeck(this) && this.eatableTimes <= 0)
        {
            AbstractDungeon.player.masterDeck.removeCard(this);
        }
    }

    protected void playEatingSound()
    {
        CardCrawlGame.sound.play("MIZUKI_EAT", 0.05F);
    }

    protected void reduceEatableTimes()
    {
        playEatingSound();
        this.baseMagicNumber--;
        AbstractFoodCard c = ((AbstractFoodCard) LearnManager.findInMasterDeck(this));
        if (c != null)
        {
            c.baseMagicNumber--;
            c.applyPowers();
        }
    }

    protected void updateEatableTimesDescription()
    {/*
        if (this.eatableTimes > 1)
        {
            this.rawDescription += CardCrawlGame.languagePack.getUIString("Mizuki_FoodDescription").TEXT[0];
        }
        if (this.eatableTimes == 1)
        {
            this.rawDescription += CardCrawlGame.languagePack.getUIString("Mizuki_FoodDescription").TEXT[1];
        }*/
    }

    @Override
    public boolean canUpgrade()
    {
        return false;
    }
}
