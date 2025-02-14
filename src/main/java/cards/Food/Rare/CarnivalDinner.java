package cards.Food.Rare;

import cards.AbstractFoodCard;
import cards.Food.Common.CompoundRefinedSauce;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import modcore.MizukiModCore;
import powers.HidingPower;

public class CarnivalDinner extends AbstractFoodCard
{
    public static final String ID = MizukiModCore.MakePath(CarnivalDinner.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public CarnivalDinner()
    {
        super(ID, cardStrings);
        setRareFood();
        //M为剩余可消耗次数 M2为可获得力量
        setupMagicNumber(1, 15, 0, 0);
        this.eatableTimes = magicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster abstractMonster)
    {
        addToTop((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p, (AbstractPower)
                new HidingPower((AbstractCreature)p, 1), 1));
        addToTop((AbstractGameAction)new ApplyPowerAction(p, p, (AbstractPower)
                new StrengthPower(p, magicNumber2), magicNumber2));
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
