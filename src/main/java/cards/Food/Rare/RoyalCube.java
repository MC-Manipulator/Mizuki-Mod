package cards.Food.Rare;

import cards.AbstractFoodCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import modcore.MizukiModCore;

public class RoyalCube extends AbstractFoodCard
{
    public static final String ID = MizukiModCore.MakePath(RoyalCube.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public RoyalCube()
    {
        super(ID, cardStrings);
        setRareFood();
        //M为剩余可消耗次数
        setupMagicNumber(1, 0, 0, 0);
        this.eatableTimes = magicNumber;
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster)
    {
        //此处应该添加能量翻3倍
        addToBot(new AbstractGameAction()
        {
            @Override
            public void update()
            {
                AbstractDungeon.player.gainEnergy(EnergyPanel.totalCount);
                AbstractDungeon.player.gainEnergy(EnergyPanel.totalCount);
                isDone = true;
            }
        });
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
