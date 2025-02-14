package cards.Food.Uncommon;

import cards.AbstractFoodCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import modcore.MizukiModCore;

public class CarbonWaterFattyAggregate extends AbstractFoodCard
{
    public static final String ID = MizukiModCore.MakePath(CarbonWaterFattyAggregate.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public CarbonWaterFattyAggregate()
    {
        super(ID, cardStrings);
        setUncommonFood();
        //M为剩余可消耗次数 M2为可获得生命 M3为可获得最大生命
        setupMagicNumber(2, 15, 10, 0);
        this.eatableTimes = magicNumber;
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster)
    {
        addToBot((AbstractGameAction)new HealAction(AbstractDungeon.player, AbstractDungeon.player, magicNumber2));
        AbstractDungeon.player.increaseMaxHp(magicNumber3, true);
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
