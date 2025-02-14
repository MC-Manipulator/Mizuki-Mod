package cards.Food.Common;

import cards.AbstractFoodCard;
import cards.Food.Basic.ExThickPie;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import modcore.MizukiModCore;

public class VacuumCooledMeatChop extends AbstractFoodCard
{
    public static final String ID = MizukiModCore.MakePath(VacuumCooledMeatChop.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public VacuumCooledMeatChop()
    {
        super(ID, cardStrings);
        setCommonFood();
        //M为剩余可消耗次数 M2为可获得力量 M3为可抽牌
        setupMagicNumber(3, 3, 2, 0);
        this.eatableTimes = magicNumber;
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster)
    {
        addToTop((AbstractGameAction)new ApplyPowerAction(abstractPlayer, abstractPlayer, (AbstractPower)
                new StrengthPower(abstractPlayer, magicNumber2), magicNumber2));
        addToBot((AbstractGameAction) new DrawCardAction(magicNumber3));
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
