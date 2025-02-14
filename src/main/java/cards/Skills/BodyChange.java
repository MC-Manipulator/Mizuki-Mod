package cards.Skills;

import cards.AbstractMizukiCard;
import cards.Attacks.EnhancedTentacle;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import modcore.MizukiModCore;

public class BodyChange extends AbstractMizukiCard
{
    public static final String ID = MizukiModCore.MakePath(BodyChange.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public BodyChange()
    {
        super(ID, false, cardStrings, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        this.baseMagicNumber = 10;
        this.magicNumber = this.baseMagicNumber;
        setupBlock(10);
        this.cardsToPreview = (AbstractCard)new EnhancedTentacle();
    }


    public void use(AbstractPlayer p, AbstractMonster m)
    {
        gainBlock(this.block);
        AbstractDungeon.player.decreaseMaxHealth(this.magicNumber);
        addToBot((AbstractGameAction)new MakeTempCardInDrawPileAction(this.cardsToPreview.makeStatEquivalentCopy(), 1, true, true, false));
        ;
        //addToBot((AbstractGameAction)new MakeTempCardInHandAction(this.cardsToPreview.makeStatEquivalentCopy(), 1));
        //addToBot((AbstractGameAction)new MakeTempCardInDrawPileAction(this.cardsToPreview.makeStatEquivalentCopy(), 1, true, true, false));
    }

    public void upgrade()
    {
        if (!this.upgraded)
        {
            upgradeName();
            upgradeBlock(5);
            upgradeMagicNumber(5);
            this.cardsToPreview.upgrade();
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
    public AbstractCard makeCopy()
    {
        return new BodyChange();
    }
}
