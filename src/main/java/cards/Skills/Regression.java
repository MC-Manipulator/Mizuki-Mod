package cards.Skills;

import cards.AbstractMizukiCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import modcore.MizukiModCore;
import powers.GainDexterityPower;

public class Regression extends AbstractMizukiCard
{
    public static final String ID = MizukiModCore.MakePath(Regression.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public Regression()
    {
        super(ID, false, cardStrings, 0, AbstractCard.CardType.SKILL, AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.SELF);
        setupMagicNumber(2, 2, 0, 0);
    }


    public void use(AbstractPlayer p, AbstractMonster m)
    {
        addToBot((AbstractGameAction)new ApplyPowerAction(p, p, new StrengthPower((AbstractCreature)p, -this.magicNumber), -this.magicNumber));
        if (!AbstractDungeon.player.hasPower("Artifact"))
        {
            addToBot((AbstractGameAction)
                    new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p, (AbstractPower)new GainStrengthPower((AbstractCreature)p, this.magicNumber), this.magicNumber));
        }

        AbstractMizukiCard c = this;
        addToBot(new AbstractGameAction()
        {
            @Override
            public void update()
            {
                addToBot((AbstractGameAction)new ApplyPowerAction(p, p, new DexterityPower((AbstractCreature)p, -c.magicNumber), -c.magicNumber));
                if (!AbstractDungeon.player.hasPower("Artifact"))
                {
                    addToBot((AbstractGameAction)
                            new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p, (AbstractPower)new GainDexterityPower((AbstractCreature)p, c.magicNumber), c.magicNumber));
                }
                isDone = true;
            }
        });


        addToBot((AbstractGameAction)new GainEnergyAction(magicNumber2));
    }

    public void upgrade()
    {
        if (!this.upgraded)
        {
            upgradeName();
            this.exhaust = false;
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            upgradeMagicNumber2(1);
            initializeDescription();
        }
    }
    public AbstractCard makeCopy()
    {
        return new Regression();
    }
}
