package cards.Skills;

import Impairment.NervousImpairment;
import actions.ApplyImpairmentAction;
import cards.AbstractMizukiCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import modcore.MizukiModCore;
import powers.CaerulorumSeminaPower;
import powers.EchoOfPredationPower;

public class EchoOfPredation extends AbstractMizukiCard
{
    public static final String ID = MizukiModCore.MakePath(EchoOfPredation.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public EchoOfPredation()
    {
        super(ID, false, cardStrings, 1, CardType.SKILL, CardRarity.RARE, CardTarget.ALL);
        setupMagicNumber(1, 1, 0, 0);
        this.exhaust = true;
        this.isEthereal = true;
    }


    public void use(AbstractPlayer p, AbstractMonster m)
    {
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                (AbstractPower)new EchoOfPredationPower((AbstractCreature)p, 1), magicNumber2));


        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                (AbstractPower)new VulnerablePower((AbstractCreature)p, 1, false), magicNumber));

        for (AbstractMonster m2 : (AbstractDungeon.getCurrRoom()).monsters.monsters)
        {
            if (!m2.isDeadOrEscaped())
            {
                addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)m2, (AbstractCreature)p,
                        (AbstractPower)new VulnerablePower((AbstractCreature)m2, 1, false), magicNumber));
            }
        }
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
        return new EchoOfPredation();
    }
}
