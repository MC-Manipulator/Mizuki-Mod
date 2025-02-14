package cards.Powers;

import cards.AbstractMizukiCard;
import characters.Mizuki;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import powers.PainAdaptationPower;

public class PainAdaptation extends AbstractMizukiCard
{
    public static final String ID = MizukiModCore.MakePath(PainAdaptation.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public PainAdaptation()
    {
        super(ID, false, cardStrings, 2, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
    }

    public void use(AbstractPlayer p, AbstractMonster m)
    {
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p, (AbstractPower)new PainAdaptationPower((AbstractCreature)p, 1), 1));
    }

    public void upgrade()
    {
        if (!this.upgraded)
        {
            upgradeName();
            upgradeBaseCost(1);
        }
    }
    public AbstractCard makeCopy()
    {
        return new PainAdaptation();
    }
}