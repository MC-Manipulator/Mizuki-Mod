package cards.Curses;

import cards.AbstractMizukiCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.PlayTopCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import modcore.MizukiModCore;

public class ConcentrationDisorder extends AbstractMizukiCard
{
    public static final String ID = MizukiModCore.MakePath(ConcentrationDisorder.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public ConcentrationDisorder()
    {
        super(ID, false, cardStrings, -2, CardType.CURSE, CardRarity.CURSE, CardTarget.NONE);
        this.color = AbstractCard.CardColor.CURSE;
    }

    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (this.dontTriggerOnUseCard)
        {
            addToBot(new AbstractGameAction()
            {
                public void update()
                {
                    addToBot((AbstractGameAction)new PlayTopCardAction(
                            (AbstractCreature)(AbstractDungeon.getCurrRoom()).monsters.getRandomMonster(null, true, AbstractDungeon.cardRandomRng), false));
                    this.isDone = true;
                }
            });
        }
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard()
    {
        if (this.isEthereal)
        {
            return;
        }
        this.dontTriggerOnUseCard = true;
        AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(this, true));
    }

    public void upgrade() {}

    public AbstractCard makeCopy()
    {
        return new ConcentrationDisorder();
    }
}
