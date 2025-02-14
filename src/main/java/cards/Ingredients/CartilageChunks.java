package cards.Ingredients;

import cards.AbstractIngredientCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import modcore.MizukiModCore;


public class CartilageChunks extends AbstractIngredientCard
{
    public static final String ID = MizukiModCore.MakePath(CartilageChunks.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public CartilageChunks()
    {
        super(ID, cardStrings, 0, CardType.SKILL, CardTarget.SELF);
        setGeneratable(CardRarity.RARE);
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