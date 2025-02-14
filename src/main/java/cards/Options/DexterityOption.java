package cards.Options;

import cards.AbstractMizukiCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
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

public class DexterityOption extends AbstractMizukiCard
{
    public static final String ID = MizukiModCore.MakePath(DexterityOption.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private final static int increaseAmount = 4;
    private final static int decreaseAmount = 1;

    public DexterityOption()
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
        //addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature) p, (AbstractCreature)p, (AbstractPower) new StrengthPower((AbstractCreature)p, -1), -1));
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature) p, (AbstractCreature)p, (AbstractPower) new DexterityPower((AbstractCreature)p, magicNumber), magicNumber));
        StrengthOption.onNotChooseThis();
        HealthOption.onNotChooseThis();
        CardOption.onNotChooseThis();
        //addToBot((AbstractGameAction)new DiscardAction(p, p, 1, false));
    }

    public static void onNotChooseThis()
    {
        AbstractPlayer p = AbstractDungeon.player;
        AbstractDungeon.actionManager.addToBottom((AbstractGameAction)
                new ApplyPowerAction((AbstractCreature) p, (AbstractCreature)p, (AbstractPower)
                        new DexterityPower((AbstractCreature)p, -decreaseAmount), -decreaseAmount));
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
        return new DexterityOption();
    }
}
