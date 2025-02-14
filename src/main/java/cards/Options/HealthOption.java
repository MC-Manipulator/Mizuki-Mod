package cards.Options;

import cards.AbstractMizukiCard;
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

public class HealthOption extends AbstractMizukiCard
{
    public static final String ID = MizukiModCore.MakePath(HealthOption.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private final static int increaseAmount = 8;
    private final static int decreaseAmount = 2;

    public HealthOption()
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
        //addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature) p, (AbstractCreature)p, (AbstractPower) new StrengthPower((AbstractCreature)p, -1), -1));
        //addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature) p, (AbstractCreature)p, (AbstractPower) new DexterityPower((AbstractCreature)p, -2), -2));
        addToBot((AbstractGameAction)new HealAction(p , p, magicNumber));
        StrengthOption.onNotChooseThis();
        DexterityOption.onNotChooseThis();
        CardOption.onNotChooseThis();
    }

    public static void onNotChooseThis()
    {
        AbstractPlayer p = AbstractDungeon.player;
        AbstractDungeon.actionManager.addToBottom((AbstractGameAction)
                new LoseHPAction(p , p, decreaseAmount, AbstractGameAction.AttackEffect.POISON));
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
        return new HealthOption();
    }
}
