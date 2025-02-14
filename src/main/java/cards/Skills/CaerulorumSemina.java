package cards.Skills;

 import cards.AbstractMizukiCard;
 import com.megacrit.cardcrawl.actions.common.LoseHPAction;
 import modcore.MizukiModCore;
 import com.megacrit.cardcrawl.cards.AbstractCard;
 import com.megacrit.cardcrawl.characters.AbstractPlayer;
 import com.megacrit.cardcrawl.core.CardCrawlGame;
 import com.megacrit.cardcrawl.localization.CardStrings;
 import com.megacrit.cardcrawl.monsters.AbstractMonster;
 import com.megacrit.cardcrawl.actions.AbstractGameAction;
 import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
 import com.megacrit.cardcrawl.core.AbstractCreature;
 import com.megacrit.cardcrawl.powers.AbstractPower;
 import powers.CaerulorumSeminaPower;

 public class CaerulorumSemina extends AbstractMizukiCard
{
    public static final String ID = MizukiModCore.MakePath(CaerulorumSemina.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final int costHealth = 6;
    private static final int increaseStrength = 2;

    public CaerulorumSemina()
    {
        super(ID, false, cardStrings, 2, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        this.exhaust = true;
        setupMagicNumber(costHealth, increaseStrength, 0, 0);
        //this.tags.add(AbstractCard.CardTags.HEALING);
    }


    public void use(AbstractPlayer p, AbstractMonster m)
    {
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p, (AbstractPower)new CaerulorumSeminaPower((AbstractCreature)p, magicNumber2), magicNumber2));
        addToBot((AbstractGameAction)new LoseHPAction(p , p, magicNumber, AbstractGameAction.AttackEffect.POISON));
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
        return new CaerulorumSemina();
    }
}