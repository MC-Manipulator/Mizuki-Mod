package cards.Skills;

 import cards.AbstractMizukiCard;
 import modcore.MizukiModCore;
 import com.megacrit.cardcrawl.cards.AbstractCard;
 import com.megacrit.cardcrawl.characters.AbstractPlayer;
 import com.megacrit.cardcrawl.core.CardCrawlGame;
 import com.megacrit.cardcrawl.localization.CardStrings;
 import com.megacrit.cardcrawl.monsters.AbstractMonster;
 import com.megacrit.cardcrawl.actions.AbstractGameAction;
 import com.megacrit.cardcrawl.actions.common.DrawCardAction;
 import com.megacrit.cardcrawl.actions.common.HealAction;
 import com.megacrit.cardcrawl.core.AbstractCreature;

 public class Kettle extends AbstractMizukiCard
{
    public static final String ID = MizukiModCore.MakePath(Kettle.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public Kettle()
    {
        super(ID, false, cardStrings, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);

        this.tags.add(AbstractCard.CardTags.HEALING);
        this.exhaust = true;
        this.baseMagicNumber = 1;
        this.magicNumber = this.baseMagicNumber;
    }


    public void use(AbstractPlayer p, AbstractMonster m)
    {
        addToBot((AbstractGameAction)new DrawCardAction((AbstractCreature)p, this.magicNumber));
        addToBot((AbstractGameAction)new HealAction((AbstractCreature)p, (AbstractCreature)p, 4));
    }

    public void upgrade()
    {
        if (!this.upgraded)
        {
            upgradeName();
            upgradeMagicNumber(1);
        }
    }
    public AbstractCard makeCopy()
    {
        return new Kettle();
    }
}