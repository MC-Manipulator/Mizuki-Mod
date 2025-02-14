package cards.Skills;

import cards.AbstractMizukiCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import modcore.MizukiModCore;

public class AbsurdFate extends AbstractMizukiCard
{
    public static final String ID = MizukiModCore.MakePath(AbsurdFate.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public AbsurdFate()
    {
        super(ID, false, cardStrings, 0, AbstractCard.CardType.SKILL, CardRarity.SPECIAL, AbstractCard.CardTarget.SELF);
        this.exhaust = true;
        setupMagicNumber(3, 1, 3, 0);
    }


    public void use(AbstractPlayer p, AbstractMonster m)
    {
        addToBot((AbstractGameAction)new LoseHPAction(p, p, magicNumber));
        addToBot((AbstractGameAction)new GainEnergyAction(magicNumber2));
        addToBot((AbstractGameAction)new DrawCardAction(magicNumber3));
    }

    public void upgrade()
    {
        if (!this.upgraded)
        {
            upgradeName();
            upgradeMagicNumber2(1);
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
    public AbstractCard makeCopy()
    {
        return new AbsurdFate();
    }
}
