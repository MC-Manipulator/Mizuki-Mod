package cards.Attacks;

import cards.AbstractMizukiCard;
import characters.Mizuki;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import helper.LearnManager;
import modcore.MizukiModCore;

public class MultipleGrowth extends AbstractMizukiCard
{
    public static final String ID = MizukiModCore.MakePath(MultipleGrowth.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public MultipleGrowth()
    {
        super(ID, false, cardStrings, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        setupDamage(5);
        //M4为学习上限，M3为计数器需求数，M2为计数器，M为学习数
        setupMagicNumber(1, 0, 3, 4);
        this.tags.add(Mizuki.Enums.LEARNING_CARD);
        tags.add(CardTags.HEALING);
        /*
        this.misc = 0;
        this.baseMagicNumber = 4;
        this.magicNumber = this.baseMagicNumber;
        this.baseMagicNumber2 = 1;
        this.magicNumber2 = this.baseMagicNumber2;
        this.baseMagicNumber3 = this.misc;
        this.magicNumber3 = this.baseMagicNumber3;
        this.baseMagicNumber4 = 5;
        this.magicNumber4 = this.baseMagicNumber4;
        this.hasLearnLimit = true;*/
    }

    public void use(AbstractPlayer p, AbstractMonster m)
    {
        for (int i = 0;i < this.magicNumber;i++)
        {
            addToBot((AbstractGameAction)new DamageAction((AbstractCreature)m, new DamageInfo((AbstractCreature)p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SMASH));
        }

        //当攻击段数不为1的时候，使用该牌会让攻击段数归1

        /*
        if (this.magicNumber != 1)
        {
            this.baseMagicNumber = 1;
            this.magicNumber = this.baseMagicNumber;
            AbstractMizukiCard c = (AbstractMizukiCard)LearnManager.findInMasterDeck(this);
            if (c != null)
            {
                c.baseMagicNumber = 1;
                c.magicNumber = c.baseMagicNumber;
            }
            /*

            for (AbstractCard c : AbstractDungeon.player.masterDeck.group)
            {
                if (!(c instanceof MultipleGrowth))
                {
                    continue;
                }
                AbstractMizukiCard mc = (AbstractMizukiCard)c;
                if (!mc.uuid.equals(this.uuid))
                    continue;

                mc.baseMagicNumber2 = 1;
                mc.magicNumber2 = mc.baseMagicNumber2;

                mc.applyPowers();
            }*/
/*
            applyPowers();
        }*/
    }

    public void triggerOnCardPlayed(AbstractCard card)
    {
        /*
        if (magicNumber2 < magicNumber4)
        {

        }*/
        //若使用的牌为攻击牌，且该牌在弃牌堆，则计数器加1
        if (AbstractDungeon.player.discardPile.contains(this) && card.type == CardType.ATTACK)
        {
            LearnManager.CounterIncreaseAndCheck(this, 1);
            AbstractMizukiCard c = (AbstractMizukiCard)LearnManager.findInMasterDeck(this);
            if (c != null)
            {
                LearnManager.CounterIncreaseAndCheck(c, 1);

            }
                /*
                addToTop((AbstractGameAction) new LearnAction(this.uuid, 1));
                for (AbstractCard c : AbstractDungeon.player.masterDeck.group)
                {
                    if (!(c instanceof MultipleGrowth))
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
    }

    public void applyPowers()
    {
        LearnManager.synchronise(this);
        LearnManager.CounterIncreaseAndCheck(this);
        /*
        if (magicNumber2 > magicNumber4)
        {
            magicNumber2 = magicNumber4;
        }
        MizukiModCore.logger.info("ApplyPower");

        //当计数器满足要求后，提升攻击段数1点，计数器归0
        boolean find = false;
        if (!AbstractDungeon.player.masterDeck.group.contains(this))
        {
            for (AbstractCard c : AbstractDungeon.player.masterDeck.group)
            {
                if (!(c instanceof MultipleGrowth))
                {
                    continue;
                }
                AbstractMizukiCard mc = (AbstractMizukiCard)c;
                if (!mc.uuid.equals(this.uuid))
                    continue;
                mc.applyPowers();
                this.magicNumber2 = mc.magicNumber2;
                this.baseMagicNumber2 = mc.baseMagicNumber2;
                this.baseMagicNumber3 = mc.baseMagicNumber3;
                this.magicNumber3 = mc.baseMagicNumber3;
                find = true;
                //MizukiModCore.logger.info(this.baseMagicNumber2);
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

        //MizukiModCore.logger.info("FinishApply");
        super.applyPowers();
        initializeDescription();
    }

    public void upgrade()
    {
        if (!this.upgraded)
        {
            upgradeName();
            //upgradeDamage(3);
            upgradeMagicNumber4(1);
        }
    }

    public AbstractCard makeCopy()
    {
        return new MultipleGrowth();
    }
}
