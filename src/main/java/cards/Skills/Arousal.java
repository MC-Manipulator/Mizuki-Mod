package cards.Skills;

 import cards.AbstractMizukiCard;
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
 import com.megacrit.cardcrawl.powers.watcher.VigorPower;
 import powers.ArousalPower;

public class Arousal extends AbstractMizukiCard
{
    public static final String ID = MizukiModCore.MakePath(Arousal.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public Arousal()
    {
        super(ID, false, cardStrings, 0, CardType.SKILL, CardRarity.BASIC, CardTarget.SELF);
        setupMagicNumber(3, 1, 0, 0);
    }


    public void use(AbstractPlayer p, AbstractMonster m)
    {
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p, (AbstractPower)new VigorPower((AbstractCreature)p, this.magicNumber), this.magicNumber));
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p, (AbstractPower)new ArousalPower((AbstractCreature)p, this.magicNumber2), this.magicNumber2));
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
        return new Arousal();
    }
}