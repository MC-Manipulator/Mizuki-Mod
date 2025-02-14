package cards.Curses;

import cards.AbstractMizukiCard;
import cards.Skills.AbsurdFate;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import modcore.MizukiModCore;

public class MetastaticAberration extends AbstractMizukiCard
{
    public static final String ID = MizukiModCore.MakePath(MetastaticAberration.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public MetastaticAberration()
    {
        super(ID, false, cardStrings, -2, CardType.CURSE, CardRarity.CURSE, CardTarget.NONE);
        this.color = AbstractCard.CardColor.CURSE;
        setupMagicNumber(2, 2, 0, 0);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {}

    public void triggerWhenDrawn()
    {
        //addToBot((AbstractGameAction)new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower((AbstractCreature)AbstractDungeon.player, -this.magicNumber), -this.magicNumber));
        //addToBot((AbstractGameAction)new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower((AbstractCreature)AbstractDungeon.player, -this.magicNumber), -this.magicNumber));
        addToBot((AbstractGameAction)new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower((AbstractCreature)AbstractDungeon.player, -this.magicNumber), -this.magicNumber));
        //addToBot((AbstractGameAction)new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new LoseStrengthPower((AbstractCreature)AbstractDungeon.player, -this.magicNumber), -this.magicNumber));
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)AbstractDungeon.player, (AbstractCreature)AbstractDungeon.player, (AbstractPower)new GainStrengthPower((AbstractCreature)AbstractDungeon.player, this.magicNumber), this.magicNumber));

        drawCards(magicNumber2);
    }

    public void upgrade() {}

    public AbstractCard makeCopy()
    {
        return new MetastaticAberration();
    }
}
