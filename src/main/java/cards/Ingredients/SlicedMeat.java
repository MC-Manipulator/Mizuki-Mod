package cards.Ingredients;

import cards.AbstractIngredientCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import modcore.MizukiModCore;

public class SlicedMeat extends AbstractIngredientCard
{
    public static final String ID = MizukiModCore.MakePath(SlicedMeat.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public SlicedMeat()
    {
        super(ID, cardStrings, 0, AbstractCard.CardType.SKILL, AbstractCard.CardTarget.SELF);
        setGeneratable(AbstractCard.CardRarity.UNCOMMON);
        setupMagicNumber(1, 0, 0, 0);
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster)
    {
        addToBot((AbstractGameAction) new GainEnergyAction(magicNumber));
    }

    @Override
    public void upgrade()
    {
    }
}
