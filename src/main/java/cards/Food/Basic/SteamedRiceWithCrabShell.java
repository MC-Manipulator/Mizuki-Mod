package cards.Food.Basic;

import cards.AbstractFoodCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import modcore.MizukiModCore;

public class SteamedRiceWithCrabShell extends AbstractFoodCard
{
    public static final String ID = MizukiModCore.MakePath(SteamedRiceWithCrabShell.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public SteamedRiceWithCrabShell()
    {
        super(ID, cardStrings);
        setBasicFood();
        //M为剩余可消耗次数 M2为可获得敏捷
        setupMagicNumber(3, 4, 0, 0);
        this.eatableTimes = magicNumber;
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster)
    {
        addToBot((AbstractGameAction)new ApplyPowerAction(abstractPlayer, abstractPlayer,
                (AbstractPower)new DexterityPower(abstractPlayer, this.magicNumber2), this.magicNumber2));
        reduceEatableTimes();
        applyPowers();
    }

    @Override
    public void applyPowers()
    {
        /*
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
        }*/
        synchronize();
        initializeDescription();
        super.applyPowers();
    }

    @Override
    public void upgrade()
    {

    }
}
