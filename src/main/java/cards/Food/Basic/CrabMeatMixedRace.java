package cards.Food.Basic;

import cards.AbstractFoodCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import modcore.MizukiModCore;

public class CrabMeatMixedRace extends AbstractFoodCard
{
    public static final String ID = MizukiModCore.MakePath(CrabMeatMixedRace.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public CrabMeatMixedRace()
    {
        super(ID, cardStrings);
        setBasicFood();
        //M为剩余可消耗次数 M2为提升的生命上限
        setupMagicNumber(4, 3, 0, 0);
        this.eatableTimes = magicNumber;
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster)
    {
        AbstractDungeon.player.increaseMaxHp(magicNumber2, true);
        //addToBot((AbstractGameAction)new HealAction((AbstractCreature)abstractPlayer, (AbstractCreature)abstractPlayer, magicNumber2));
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
