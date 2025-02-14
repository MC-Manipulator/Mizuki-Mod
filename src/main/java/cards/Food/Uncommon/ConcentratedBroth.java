package cards.Food.Uncommon;

import cards.AbstractFoodCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import modcore.MizukiModCore;

public class ConcentratedBroth extends AbstractFoodCard
{
    public static final String ID = MizukiModCore.MakePath(ConcentratedBroth.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public ConcentratedBroth()
    {
        super(ID, cardStrings);
        setUncommonFood();
        //M为剩余可消耗次数 M2为可抽牌
        setupMagicNumber(2, 3, 0, 0);
        this.eatableTimes = magicNumber;
        setupBlock(20);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster abstractMonster)
    {
        //此处应该添加下回合抽牌
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature) p, (AbstractCreature)p, (AbstractPower)
                new DrawCardNextTurnPower((AbstractCreature)p, magicNumber2), magicNumber2));
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
