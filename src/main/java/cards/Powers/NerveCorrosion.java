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
import helper.ImpairementManager;
import modcore.MizukiModCore;
import powers.HysterotraumaticPower;
import powers.NerveCorrosionPower;

public class NerveCorrosion extends AbstractMizukiCard
{
    public static final String ID = MizukiModCore.MakePath(NerveCorrosion.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public NerveCorrosion()
    {
        super(ID, false, cardStrings, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        //setupMagicNumber(15, 0, 0, 0);
        setupMagicNumber(1, 0, 0, 0);
    }

    public void use(AbstractPlayer p, AbstractMonster m)
    {
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p, (AbstractPower)new NerveCorrosionPower((AbstractCreature)p, magicNumber), magicNumber));
        addToBot(new AbstractGameAction()
        {
            @Override
            public void update()
            {
                ImpairementManager.nervousImpairementTimes++;
                isDone = true;
            }
        });
    }

    public void upgrade()
    {
        if (!this.upgraded)
        {
            upgradeName();

            isInnate = true;
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
            //upgradeBaseCost(0);
        }
    }
}
