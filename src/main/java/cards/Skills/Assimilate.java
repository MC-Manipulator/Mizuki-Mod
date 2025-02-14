package cards.Skills;

import basemod.abstracts.AbstractCardModifier;
import basemod.cardmods.ExhaustMod;
import basemod.helpers.CardModifierManager;
import cards.AbstractMizukiCard;
import cards.Attacks.MindTentacle;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import modcore.MizukiModCore;

public class Assimilate extends AbstractMizukiCard
{
    public static final String ID = MizukiModCore.MakePath(Assimilate.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public Assimilate()
    {
        super(ID, false, cardStrings, 1, AbstractCard.CardType.SKILL, AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.SELF);
        setupMagicNumber(2, 2, 0, 0);
        AbstractCard c = new MindTentacle();

        CardModifierManager.addModifier(c, (AbstractCardModifier)new ExhaustMod());
        //c.cost = 0;
        //c.costForTurn = 0;
        //c.isCostModified = true;
        this.cardsToPreview = (AbstractCard)c;
    }

    public void use(AbstractPlayer p, AbstractMonster m)
    {
        addToBot((AbstractGameAction) new ExhaustAction(this.magicNumber, false, false, false));
        for (int i = 0;i< magicNumber2;i++)
        {
            AbstractCard c = new MindTentacle();
            if (this.upgraded)
                c.upgrade();

            CardModifierManager.addModifier(c, (AbstractCardModifier)new ExhaustMod());
            //c.cost = 0;
            //c.costForTurn = 0;
            //c.isCostModified = true;
            addToBot((AbstractGameAction) new MakeTempCardInHandAction(c));
        }
    }

    public void upgrade()
    {
        if (!this.upgraded)
        {
            //upgradeMagicNumber(1);
            //upgradeMagicNumber2(1);
            upgradeName();
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            AbstractCard c = new MindTentacle();
            c.upgrade();
            this.cardsToPreview = c;
            initializeDescription();
        }
    }

    public AbstractCard makeCopy()
    {
        return new Assimilate();
    }
}
