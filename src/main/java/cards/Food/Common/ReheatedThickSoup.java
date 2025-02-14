package cards.Food.Common;

import cards.AbstractFoodCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.BufferPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import modcore.MizukiModCore;

public class ReheatedThickSoup extends AbstractFoodCard
{
    public static final String ID = MizukiModCore.MakePath(ReheatedThickSoup.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public ReheatedThickSoup()
    {
        super(ID, cardStrings);
        setCommonFood();
        //M为剩余可消耗次数 M2为可获得缓冲
        setupMagicNumber(3, 2, 0, 0);
        this.eatableTimes = magicNumber;
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster)
    {
        addToBot((AbstractGameAction) new ApplyPowerAction(
                        (AbstractCreature) abstractPlayer, (AbstractCreature)abstractPlayer, (AbstractPower)
                new BufferPower((AbstractCreature)abstractPlayer, this.magicNumber), this.magicNumber));
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
