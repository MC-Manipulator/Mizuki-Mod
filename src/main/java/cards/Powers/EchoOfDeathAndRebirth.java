package cards.Powers;

import cards.AbstractMizukiCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import modcore.MizukiModCore;
import powers.EchoOfDeathAndRebirthPower;

public class EchoOfDeathAndRebirth extends AbstractMizukiCard
{
    public static final String ID = MizukiModCore.MakePath(EchoOfDeathAndRebirth.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public EchoOfDeathAndRebirth()
    {
        super(ID, false, cardStrings, 1, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        setupMagicNumber(2, 7, 1 ,1);
        this.tags.add(CardTags.HEALING);
    }

    public void use(AbstractPlayer p, AbstractMonster m)
    {
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p, (AbstractPower)new EchoOfDeathAndRebirthPower((AbstractCreature)p, magicNumber4), magicNumber4));
    }

    public void upgrade()
    {
        if (!this.upgraded)
        {
            upgradeName();
            upgradeBaseCost(0);
        }
    }

    public AbstractCard makeCopy()
    {
        return new EchoOfDeathAndRebirth();
    }
}
