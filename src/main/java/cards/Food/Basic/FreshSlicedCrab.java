package cards.Food.Basic;

import cards.AbstractFoodCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import modcore.MizukiModCore;

public class FreshSlicedCrab extends AbstractFoodCard
{
    public static final String ID = MizukiModCore.MakePath(FreshSlicedCrab.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public FreshSlicedCrab()
    {
        super(ID, cardStrings);
        setBasicFood();
        //M为剩余可消耗次数
        setupMagicNumber(3, 0, 0, 0);
        this.eatableTimes = magicNumber;
        setupBlock(10);
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster)
    {
        gainBlock();
        reduceEatableTimes();
        applyPowers();
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
