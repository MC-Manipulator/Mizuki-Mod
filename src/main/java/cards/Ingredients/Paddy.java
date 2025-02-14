package cards.Ingredients;


import cards.AbstractIngredientCard;
import cards.Attacks.StrengtheningStab;
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
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import modcore.MizukiModCore;

public class Paddy extends AbstractIngredientCard
{
    public static final String ID = MizukiModCore.MakePath(Paddy.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public Paddy()
    {
        super(ID, cardStrings, 0, CardType.SKILL, CardTarget.SELF);
        setGeneratable(CardRarity.COMMON);
        setupMagicNumber(2, 0, 0, 0);
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster)
    {
        addToBot((AbstractGameAction)new HealAction(AbstractDungeon.player, AbstractDungeon.player, magicNumber));
    }

    @Override
    public void upgrade()
    {
    }
}