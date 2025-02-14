package cards.Food.Basic;

import cards.AbstractFoodCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.EnergizedPower;
import modcore.MizukiModCore;
public class PineBranchSmokedPork extends AbstractFoodCard
{
    public static final String ID = MizukiModCore.MakePath(PineBranchSmokedPork.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public PineBranchSmokedPork()
    {
        super(ID, cardStrings);
        setBasicFood();
        //M为剩余可消耗次数 M2为可获得能量
        setupMagicNumber(4, 2, 0, 0);
        this.eatableTimes = magicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster abstractMonster)
    {
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature) p, (AbstractCreature)p,
                (AbstractPower)new EnergizedPower((AbstractCreature)p, magicNumber2), magicNumber2));

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
