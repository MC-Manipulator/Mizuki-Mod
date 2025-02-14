package cards.Food.Common;

import cards.AbstractFoodCard;
import cards.Food.Basic.PineBranchSmokedPork;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.EnergizedPower;
import modcore.MizukiModCore;

public class MealSubstitutePowder extends AbstractFoodCard
{
    public static final String ID = MizukiModCore.MakePath(MealSubstitutePowder.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public MealSubstitutePowder()
    {
        super(ID, cardStrings);
        setCommonFood();
        //M为剩余可消耗次数 M2为可获得最大生命
        setupMagicNumber(4, 5, 0, 0);
        this.eatableTimes = magicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster abstractMonster)
    {
        AbstractDungeon.player.increaseMaxHp(magicNumber2, true);
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
