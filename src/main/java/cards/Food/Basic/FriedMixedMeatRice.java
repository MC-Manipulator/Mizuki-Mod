package cards.Food.Basic;

import cards.AbstractFoodCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import modcore.MizukiModCore;

public class FriedMixedMeatRice extends AbstractFoodCard
{
    public static final String ID = MizukiModCore.MakePath(FriedMixedMeatRice.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public FriedMixedMeatRice()
    {
        super(ID, cardStrings);
        setBasicFood();
        //M为剩余可消耗次数 M2为可获得力量
        setupMagicNumber(4, 3, 0, 0);
        this.eatableTimes = magicNumber;
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster)
    {
        addToTop((AbstractGameAction)new ApplyPowerAction(abstractPlayer, abstractPlayer, (AbstractPower)
                new StrengthPower(abstractPlayer, magicNumber2), magicNumber2));
        reduceEatableTimes();
    }

    @Override
    public void applyPowers()
    {
        synchronize();
        initializeDescription();
        super.applyPowers();
    }

    @Override
    public void upgrade()
    {

    }
}
