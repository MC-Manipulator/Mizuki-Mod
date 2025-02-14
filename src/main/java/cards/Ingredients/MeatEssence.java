package cards.Ingredients;

import cards.AbstractIngredientCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.BufferPower;
import modcore.MizukiModCore;

public class MeatEssence extends AbstractIngredientCard
{
    public static final String ID = MizukiModCore.MakePath(MeatEssence.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public MeatEssence()
    {
        super(ID, cardStrings, 0, AbstractCard.CardType.SKILL, AbstractCard.CardTarget.SELF);
        setGeneratable(CardRarity.RARE);
        setupMagicNumber(1, 0, 0, 0);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p, (AbstractPower)new ArtifactPower((AbstractCreature)p, this.magicNumber), this.magicNumber));
    }

    @Override
    public void upgrade()
    {
    }
}
