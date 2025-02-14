package cards.Curses;

import cards.AbstractMizukiCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import modcore.MizukiModCore;

public class Neurodegeneration extends AbstractMizukiCard
{
    public static final String ID = MizukiModCore.MakePath(Neurodegeneration.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public Neurodegeneration()
    {
        super(ID, false, cardStrings, -2, CardType.CURSE, CardRarity.CURSE, CardTarget.NONE);
        this.color = AbstractCard.CardColor.CURSE;
        //M为消耗能量数量
        setupMagicNumber(1, 0, 0, 0);
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m)
    {
        if (AbstractDungeon.player.hand.contains(this))
        {
            AbstractCard card = this;
            if (c.type == CardType.ATTACK)
            {
                this.superFlash();
                addToBot(new AbstractGameAction()
                {
                    @Override
                    public void update()
                    {
                        AbstractDungeon.player.loseEnergy(magicNumber);
                        AbstractDungeon.player.hand.moveToDiscardPile(card);
                        card.triggerOnManualDiscard();
                        GameActionManager.incrementDiscard(false);
                        isDone = true;
                    }
                });
            }
        }
    }

    public void use(AbstractPlayer p, AbstractMonster m) {}

    public void upgrade() {}

    public AbstractCard makeCopy()
    {
        return new Neurodegeneration();
    }
}
