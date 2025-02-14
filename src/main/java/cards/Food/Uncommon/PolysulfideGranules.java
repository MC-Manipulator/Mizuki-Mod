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
import modcore.MizukiModCore;
import powers.MultipleAttackCardsPlayPower;
import powers.MultipleSkillCardsPlayPower;

public class PolysulfideGranules extends AbstractFoodCard
{
    public static final String ID = MizukiModCore.MakePath(PolysulfideGranules.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public PolysulfideGranules()
    {
        super(ID, cardStrings);
        setUncommonFood();
        //M为剩余可消耗次数 M2为可获得最大生命
        setupMagicNumber(2, 8, 0, 0);
        this.eatableTimes = magicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster abstractMonster)
    {
        //此处应该添加下一张攻击牌释放三次
        addToTop((AbstractGameAction) new ApplyPowerAction(p, p, new MultipleAttackCardsPlayPower(p, 3), 3));
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
