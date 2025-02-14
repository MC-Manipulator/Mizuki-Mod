package cards.Skills;

import cards.AbstractMizukiCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
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
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import modcore.MizukiModCore;

public class TargetedEvolution extends AbstractMizukiCard
{
    public static final String ID = MizukiModCore.MakePath(TargetedEvolution.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public TargetedEvolution()
    {
        super(ID, false, cardStrings, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        setupBlock(6);
    }


    public void use(AbstractPlayer p, AbstractMonster m)
    {
        gainBlock();
        int Strength = 0;
        int Dexterity = 0;
        if (AbstractDungeon.player.hasPower("Strength"))
        {
            Strength = AbstractDungeon.player.getPower("Strength").amount;
            addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p, (AbstractPower)new StrengthPower((AbstractCreature)p, -Strength), -Strength));

        }

        if (AbstractDungeon.player.hasPower("Dexterity"))
        {
            Dexterity = AbstractDungeon.player.getPower("Dexterity").amount;
            addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p, (AbstractPower)new DexterityPower((AbstractCreature)p, -Dexterity), -Dexterity));
        }
        if (Dexterity != 0)
        {
            addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p, (AbstractPower)new StrengthPower((AbstractCreature)p, Dexterity), Dexterity));
        }
        if (Strength != 0)
        {
            addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p, (AbstractPower)new DexterityPower((AbstractCreature)p, Strength), Strength));
        }
    }

    public void upgrade()
    {
        if (!this.upgraded)
        {
            upgradeName();
            upgradeBaseCost(0);
        }
    }

    public AbstractCard makeCopy()
    {
        return new TargetedEvolution();
    }
}
