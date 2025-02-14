package cards.Food.Rare;

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
import com.megacrit.cardcrawl.powers.DexterityPower;
import modcore.MizukiModCore;

public class NanoDish extends AbstractFoodCard
{
    public static final String ID = MizukiModCore.MakePath(NanoDish.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public NanoDish()
    {
        super(ID, cardStrings);
        setRareFood();
        //M为剩余可消耗次数 M2为可恢复生命 M3为可获得敏捷
        setupMagicNumber(1, 20, 10, 0);
        this.eatableTimes = magicNumber;
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster)
    {
        addToBot((AbstractGameAction)new HealAction(AbstractDungeon.player, AbstractDungeon.player, magicNumber2));
        addToBot((AbstractGameAction)new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                (AbstractPower)new DexterityPower(AbstractDungeon.player, this.magicNumber3), this.magicNumber3));
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
