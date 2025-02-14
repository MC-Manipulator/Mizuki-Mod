package cards.Options;

import cards.AbstractMizukiCard;
import cards.Attacks.AdaptingStrike;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import modcore.MizukiModCore;
import patches.FoodCardColorEnumPatch;

public class CardOption extends AbstractMizukiCard
{
    public static final String ID = MizukiModCore.MakePath(CardOption.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private final static int increaseAmount = 4;
    private final static int decreaseAmount = 1;

    public CardOption()
    {
        super(ID, false, cardStrings, -2, CardType.POWER, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.NONE);
        setupMagicNumber(increaseAmount, decreaseAmount, 0, 0);
    }

    public void use(AbstractPlayer p, AbstractMonster m)
    {
        onChoseThisOption();
    }

    public void onChoseThisOption()
    {
        AbstractPlayer p = AbstractDungeon.player;
        //addToBot((AbstractGameAction)new ReducePowerAction((AbstractCreature) p, (AbstractCreature) p, "Strength", 5));
        //addToBot((AbstractGameAction)new ReducePowerAction((AbstractCreature) p, (AbstractCreature) p, "Dexterity", 6));
        //addToBot((AbstractGameAction)new ReducePowerAction((AbstractCreature) p, (AbstractCreature)p, (AbstractPower) new StrengthPower((AbstractCreature)p, 5), 5));
        //addToBot((AbstractGameAction)new ReducePowerAction((AbstractCreature) p, (AbstractCreature)p, (AbstractPower) new DexterityPower((AbstractCreature)p, 6), 6));

        //addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature) p, (AbstractCreature)p, (AbstractPower) new StrengthPower((AbstractCreature)p, -1), -1));
        //addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature) p, (AbstractCreature)p, (AbstractPower) new DexterityPower((AbstractCreature)p, -2), -2));
        //addToBot((AbstractGameAction)new LoseHPAction(p , p, 3, AbstractGameAction.AttackEffect.NONE));
        addToBot((AbstractGameAction)new DrawCardAction(p, magicNumber));
        StrengthOption.onNotChooseThis();
        DexterityOption.onNotChooseThis();
        HealthOption.onNotChooseThis();
    }

    public static void onNotChooseThis()
    {
        AbstractPlayer p = AbstractDungeon.player;
        AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new DiscardAction(p, p, decreaseAmount, false));
        //addToBot();
    }

    public void upgrade()
    {
        if (!this.upgraded)
        {
            upgradeName();
        }
    }

    public AbstractCard makeCopy()
    {
        return new CardOption();
    }
}
