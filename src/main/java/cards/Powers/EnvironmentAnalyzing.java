package cards.Powers;

import cards.AbstractMizukiCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import modcore.MizukiModCore;
import powers.EntreatyoftheHeraldPower;
import powers.EnvironmentAnalyzingPower;

public class EnvironmentAnalyzing extends AbstractMizukiCard
{
    public static final String ID = MizukiModCore.MakePath(EnvironmentAnalyzing.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public EnvironmentAnalyzing()
    {
        super(ID, false, cardStrings, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        setupMagicNumber(1, 0, 0, 0);
    }

    public void use(AbstractPlayer p, AbstractMonster m)
    {
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p, (AbstractPower)new EnvironmentAnalyzingPower((AbstractPlayer) p, this.magicNumber), this.magicNumber));
    }

    public void upgrade()
    {
        if (!this.upgraded)
        {
            upgradeName();
            upgradeMagicNumber(1);
        }
    }
}
