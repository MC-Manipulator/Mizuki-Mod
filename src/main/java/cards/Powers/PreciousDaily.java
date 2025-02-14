package cards.Powers;

import cards.AbstractMizukiCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import modcore.MizukiModCore;
import powers.NerveCorrosionPower;
import powers.PreciousDailyPower;

public class PreciousDaily extends AbstractMizukiCard
{
    public static final String ID = MizukiModCore.MakePath(PreciousDaily.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final int drawCardsAmount = 1;

    public PreciousDaily()
    {
        super(ID, false, cardStrings, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        setupMagicNumber(drawCardsAmount, 1, 0, 0);
    }

    public void use(AbstractPlayer p, AbstractMonster m)
    {
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p, (AbstractPower)
                new PreciousDailyPower((AbstractCreature)p, magicNumber), magicNumber));
    }

    public void upgrade()
    {
        if (!this.upgraded)
        {
            upgradeName();
            this.isInnate = true;
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
