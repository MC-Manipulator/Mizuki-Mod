package cards.Skills;

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
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import modcore.MizukiModCore;
import powers.GiveImpToATKerPower;
import powers.StressResponsePower;

public class StressResponse extends AbstractMizukiCard
{
    public static final String ID = MizukiModCore.MakePath(StressResponse.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public StressResponse()
    {
        super(ID, false, cardStrings, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        setupMagicNumber(10, 1, 0, 0);
    }


    public void use(AbstractPlayer p, AbstractMonster m)
    {
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p, (AbstractPower)new StressResponsePower((AbstractCreature)p, this.magicNumber), this.magicNumber));
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p, (AbstractPower)new GiveImpToATKerPower((AbstractCreature)p, this.magicNumber2), this.magicNumber2));

    }

    public void upgrade()
    {
        if (!this.upgraded)
        {
            upgradeName();
            upgradeMagicNumber(2);
            upgradeMagicNumber2(1);
        }
    }

    public AbstractCard makeCopy()
    {
        return new StressResponse();
    }
}
