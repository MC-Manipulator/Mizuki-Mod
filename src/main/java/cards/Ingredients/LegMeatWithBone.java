package cards.Ingredients;

import cards.AbstractIngredientCard;
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

public class LegMeatWithBone extends AbstractIngredientCard
{
    public static final String ID = MizukiModCore.MakePath(LegMeatWithBone.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public LegMeatWithBone()
    {
        super(ID, cardStrings, 0, CardType.SKILL, CardTarget.SELF);
        setGeneratable(CardRarity.UNCOMMON);
        setupMagicNumber(1, 0, 0, 0);
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster)
    {
        addToTop((AbstractGameAction)new ApplyPowerAction(abstractPlayer, abstractPlayer, (AbstractPower)
                new StrengthPower(abstractPlayer, magicNumber), magicNumber));
    }

    @Override
    public void upgrade()
    {
    }
}
