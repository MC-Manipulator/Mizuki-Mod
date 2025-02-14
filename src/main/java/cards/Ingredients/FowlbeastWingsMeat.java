package cards.Ingredients;

import cards.AbstractIngredientCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import modcore.MizukiModCore;


public class FowlbeastWingsMeat extends AbstractIngredientCard
{
    public static final String ID = MizukiModCore.MakePath(FowlbeastWingsMeat.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public FowlbeastWingsMeat()
    {
        super(ID, cardStrings, 0, CardType.SKILL, CardTarget.SELF);
        setGeneratable(CardRarity.UNCOMMON);
        setupMagicNumber(0, 0, 0, 0);
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster)
    {

    }

    @Override
    public void upgrade()
    {

    }
}