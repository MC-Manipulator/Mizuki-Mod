package cards;

import characters.Mizuki;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.localization.CardStrings;

public abstract class AbstractIngredientCard extends AbstractMizukiCard
{
    public AbstractIngredientCard(String ID, CardStrings strings, int COST, CardType TYPE, CardTarget TARGET)
    {
        super(ID, false, strings, COST, TYPE, CardRarity.SPECIAL, TARGET);
        tags.add(CardTags.HEALING);
        tags.add(Mizuki.Enums.INGREDIENT_CARD);
        this.exhaust = true;
    }
    protected void setUnGeneratable()
    {
        this.rarity = CardRarity.SPECIAL;
    }

    protected void setGeneratable(CardRarity rarity)
    {
        this.rarity = rarity;
    }
    @Override
    public boolean canUpgrade()
    {
        return false;
    }
}
