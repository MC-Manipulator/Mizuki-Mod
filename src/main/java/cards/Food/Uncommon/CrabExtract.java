package cards.Food.Uncommon;

import cards.AbstractFoodCard;
import cards.Food.Common.CompoundRefinedSauce;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import modcore.MizukiModCore;

public class CrabExtract extends AbstractFoodCard
{
    public static final String ID = MizukiModCore.MakePath(CrabExtract.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public CrabExtract()
    {
        super(ID, cardStrings);
        setUncommonFood();
        //M为剩余可消耗次数 M2为可获得敏捷
        setupMagicNumber(2, 8, 0, 0);
        this.eatableTimes = magicNumber;
        setupBlock(11);
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster)
    {
        gainBlock();
        addToBot((AbstractGameAction)new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                (AbstractPower)new DexterityPower(AbstractDungeon.player, this.magicNumber2), this.magicNumber2));
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
