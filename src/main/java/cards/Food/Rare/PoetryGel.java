package cards.Food.Rare;

import cards.AbstractFoodCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import helper.EventHelper;
import modcore.MizukiModCore;
import powers.MultipleCardsPlayPower;

public class PoetryGel extends AbstractFoodCard
{
    public static final String ID = MizukiModCore.MakePath(PoetryGel.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public PoetryGel()
    {
        super(ID, cardStrings);
        setRareFood();
        //M为剩余可消耗次数
        setupMagicNumber(1, EventHelper.poetryGelUsedTimes, 0, 0);
        this.eatableTimes = magicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster abstractMonster)
    {
        //此处应当添加特殊效果
        addToTop((AbstractGameAction)
                new ApplyPowerAction(p, p,
                        new MultipleCardsPlayPower(p, magicNumber2), magicNumber2));
        EventHelper.poetryGelUsedTimes++;
        EventHelper.poetryGelUsedTimes++;
        reduceEatableTimes();
    }

    @Override
    public void applyPowers()
    {
        synchronize();
        baseMagicNumber2 = EventHelper.poetryGelUsedTimes;
        magicNumber2 = baseMagicNumber2;
        initializeDescription();
        super.applyPowers();
    }

    @Override
    public void upgrade()
    {

    }
}
