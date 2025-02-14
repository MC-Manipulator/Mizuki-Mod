package cards.Attacks;

import cards.AbstractMizukiCard;
import characters.Mizuki;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import helper.LearnManager;
import modcore.MizukiModCore;

public class DirectPain extends AbstractMizukiCard
{
    public static final String ID = MizukiModCore.MakePath(DirectPain.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public DirectPain()
    {
        super(ID, false, cardStrings, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        setupDamage(5);
        //M4为学习上限，M3为计数器需求数，M2为计数器，M为学习数
        setupMagicNumber(1, 0, 3, -1);
        this.tags.add(Mizuki.Enums.LEARNING_CARD);
        tags.add(CardTags.HEALING);

        /*
        this.misc = 0;
        this.baseMagicNumber = 3;
        this.magicNumber = this.baseMagicNumber;
        this.baseMagicNumber2 = 1;
        this.magicNumber2 = this.baseMagicNumber2;
        this.baseMagicNumber3 = this.misc;
        this.magicNumber3 = this.baseMagicNumber3;*/
    }

    public void use(AbstractPlayer p, AbstractMonster m)
    {
        addToBot((AbstractGameAction)new DamageAction((AbstractCreature)m, new DamageInfo((AbstractCreature)p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SMASH));

        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)m, (AbstractCreature)p, (AbstractPower)new VulnerablePower((AbstractCreature)m, this.magicNumber, false), this.magicNumber));
    }
    public void tookDamage()
    {
        LearnManager.CounterIncreaseAndCheck(this, 1);
        AbstractMizukiCard c = (AbstractMizukiCard)LearnManager.findInMasterDeck(this);
        if (c != null)
        {
            LearnManager.CounterIncreaseAndCheck(c, 1);
        }
        /*addToTop((AbstractGameAction) new LearnAction(this.uuid, 1));
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group)
        {
            if (!(c instanceof DirectPain))
            {
                continue;
            }
            AbstractMizukiCard mc = (AbstractMizukiCard)c;
            if (!mc.uuid.equals(this.uuid))
                continue;
            mc.applyPowers();
        }*/
        applyPowers();

    }
    public void applyPowers()
    {
        if (!LearnManager.ifInMasterDeck(this))
        {
            LearnManager.synchronise(this);
        }

        LearnManager.CounterIncreaseAndCheck(this);
        /*
        boolean find = false;
        if (!AbstractDungeon.player.masterDeck.group.contains(this))
        {
            for (AbstractCard c : AbstractDungeon.player.masterDeck.group)
            {
                if (!(c instanceof DirectPain))
                {
                    continue;
                }
                AbstractMizukiCard mc = (AbstractMizukiCard)c;
                if (!mc.uuid.equals(this.uuid))
                    continue;
                mc.applyPowers();
                this.baseMagicNumber2 = mc.baseMagicNumber2;
                this.magicNumber2 = mc.magicNumber2;
                this.baseMagicNumber3 = mc.baseMagicNumber3;
                this.magicNumber3 = mc.baseMagicNumber3;
                find = true;
            }
        }
        if (AbstractDungeon.player.masterDeck.group.contains(this) || !find)
        {
            if (this.misc >= this.magicNumber)
            {
                this.misc = 0;
                this.baseMagicNumber2++;
            }
            this.baseMagicNumber3 = this.misc;
            this.magicNumber3 = this.baseMagicNumber3;
            this.magicNumber2 = this.baseMagicNumber2;
        }*/

        super.applyPowers();
        initializeDescription();
    }

    public void upgrade()
    {
        if (!this.upgraded)
        {
            upgradeName();
            upgradeDamage(3);
            //upgradeMagicNumber(-1);
        }
    }

    public AbstractCard makeCopy()
    {
        return new DirectPain();
    }
}
