package cards.Skills;

import actions.CrisisConsciousnessAction;
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

public class CrisisConsciousness extends AbstractMizukiCard
{
    public static final String ID = MizukiModCore.MakePath(CrisisConsciousness.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public CrisisConsciousness()
    {
        super(ID, false, cardStrings, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.ENEMY);
        setupBlock(0);
    }


    public void use(AbstractPlayer p, AbstractMonster m)
    {
        addToBot((AbstractGameAction)new CrisisConsciousnessAction(m));
        if (this.upgraded)
        {
            gainBlock();
        }
    }

    public void upgrade()
    {
        if (!this.upgraded)
        {
            upgradeName();
            upgradeBlock(3);
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    public AbstractCard makeCopy()
    {
        return new CrisisConsciousness();
    }
}
