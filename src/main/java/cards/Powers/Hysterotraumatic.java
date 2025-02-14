package cards.Powers;

import cards.AbstractMizukiCard;
import modcore.MizukiModCore;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.powers.AbstractPower;
import powers.HysterotraumaticPower;

public class Hysterotraumatic extends AbstractMizukiCard
{
    public static final String ID = MizukiModCore.MakePath(Hysterotraumatic.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public Hysterotraumatic()
    {
        super(ID, false, cardStrings, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        setupMagicNumber(3, 0, 0, 0);
    }

    public void use(AbstractPlayer p, AbstractMonster m)
    {
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p, (AbstractPower)new HysterotraumaticPower((AbstractCreature)p, magicNumber), magicNumber));
    }

    public void upgrade()
    {
        if (!this.upgraded)
        {
            upgradeName();
            upgradeMagicNumber(2);
        }
    }

}
