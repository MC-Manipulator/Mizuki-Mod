package cards.Curses;

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
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import helper.EventHelper;
import helper.LearnManager;
import modcore.MizukiModCore;

public class HemopoieticInhibition extends AbstractMizukiCard implements EventHelper.OnHealthChangedSubscriber
{
    public static final String ID = MizukiModCore.MakePath(HemopoieticInhibition.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    protected boolean hasPlayedCard = false;

    protected boolean hasChangedHealth = false;

    public HemopoieticInhibition()
    {
        super(ID, false, cardStrings, -2, CardType.CURSE, CardRarity.CURSE, CardTarget.NONE);
        this.color = AbstractCard.CardColor.CURSE;
        setupMagicNumber(5, 1, 0, 0);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {}

    @Override
    public void OnHealthChanged(int delta)
    {
        if (AbstractDungeon.player.hand.contains(this) && !hasChangedHealth)
        {
            addToBot((AbstractGameAction)new LoseHPAction((AbstractCreature)AbstractDungeon.player, (AbstractCreature)AbstractDungeon.player, magicNumber));
            hasChangedHealth = true;
        }
    }

    @Override
    public void triggerOnEndOfPlayerTurn()
    {
        hasChangedHealth = false;
        super.triggerOnEndOfPlayerTurn();
    }

    public void upgrade() {}

    public AbstractCard makeCopy()
    {
        return new HemopoieticInhibition();
    }
}