package cards.Food.Uncommon;

import cards.AbstractFoodCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import modcore.MizukiModCore;

public class FreshCondensedCube extends AbstractFoodCard
{
    public static final String ID = MizukiModCore.MakePath(FreshCondensedCube.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public FreshCondensedCube()
    {
        super(ID, cardStrings);
        setUncommonFood();
        //M为剩余可消耗次数 M2为可获得能量 M3为可获得力量
        setupMagicNumber(2, 4, 2, 0);
        this.eatableTimes = magicNumber;
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster)
    {
        addToTop((AbstractGameAction)new GainEnergyAction(magicNumber2));
        addToTop((AbstractGameAction)new ApplyPowerAction(abstractPlayer, abstractPlayer, (AbstractPower)
                new StrengthPower(abstractPlayer, magicNumber3), magicNumber3));
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
