package cards.Food.Rare;

import cards.AbstractFoodCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import modcore.MizukiModCore;

public class SoupExtract extends AbstractFoodCard
{
    public static final String ID = MizukiModCore.MakePath(SoupExtract.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public SoupExtract()
    {
        super(ID, cardStrings);
        setRareFood();
        //M为剩余可消耗次数
        setupMagicNumber(1, 20, 0, 0);
        this.eatableTimes = magicNumber;
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster)
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
