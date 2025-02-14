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
import modcore.MizukiModCore;
import powers.MultipleCardsPlayPower;
import powers.MultipleSkillCardsPlayPower;

public class KnightSupplement extends AbstractFoodCard
{
    public static final String ID = MizukiModCore.MakePath(KnightSupplement.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public KnightSupplement()
    {
        super(ID, cardStrings);
        setRareFood();
        //M为剩余可消耗次数
        setupMagicNumber(1, 4, 0, 0);
        this.eatableTimes = magicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster abstractMonster)
    {
        //此处应该添加下一次技能牌释放多次
        addToTop((AbstractGameAction)
                new ApplyPowerAction(p, p,
                        new MultipleSkillCardsPlayPower(p, magicNumber2), magicNumber2));
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
