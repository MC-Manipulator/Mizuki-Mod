package cards.Attacks;

import actions.AdaptingStrikeAction;
import actions.ExtendAttackRandomEnemyAction;
import actions.LearnAction;
import basemod.devcommands.debug.Debug;
import cards.AbstractMizukiCard;
import characters.Mizuki;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import helper.LearnManager;
import modcore.MizukiModCore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class StrengtheningStab extends AbstractMizukiCard
{
    public static final String ID = MizukiModCore.MakePath(StrengtheningStab.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public StrengtheningStab()
    {
        super(ID, false, cardStrings, 2, CardType.ATTACK, CardRarity.COMMON, CardTarget.ALL_ENEMY);

        //M4为学习上限，M3为计数器需求数，M2为计数器，M为学习数
        setupMagicNumber(8, 0, 10, 30);
        this.baseDamage = magicNumber;
        this.tags.add(Mizuki.Enums.LEARNING_CARD);
        tags.add(CardTags.HEALING);

        /*this.misc = 6;
        this.baseMagicNumber = 10;
        this.magicNumber = this.baseMagicNumber;
        this.baseDamage = this.misc;
        this.baseMagicNumber2 = this.misc;
        this.magicNumber2 = this.baseMagicNumber2;
        this.baseMagicNumber4 = 20;
        this.magicNumber4 = this.baseMagicNumber4;
        this.hasLearnLimit = true;*/
    }

    public void use(AbstractPlayer p, AbstractMonster m)
    {
        int totaldamage = 0;
        if (this.magicNumber2 < this.baseMagicNumber4)
        {
            for (AbstractMonster m2 : (AbstractDungeon.getCurrRoom()).monsters.monsters)
            {
                if (m2.isDead || m2.isDying)
                {
                    continue;
                }
                if (m2.intent == AbstractMonster.Intent.ATTACK || m2.intent == AbstractMonster.Intent.ATTACK_BUFF
                        || m2.intent == AbstractMonster.Intent.ATTACK_DEBUFF || m2.intent == AbstractMonster.Intent.ATTACK_DEFEND)
                {
                    try
                    {
                        Class<?> C = AbstractMonster.class;
                        Field nameField = C.getDeclaredField("move");
                        nameField.setAccessible(true);
                        EnemyMoveInfo eMI = (EnemyMoveInfo) nameField.get(m2);
                        int multiplier = 1;
                        if (eMI.isMultiDamage)
                        {
                            multiplier = eMI.multiplier;
                        }
                        totaldamage += m2.getIntentDmg() * multiplier;
                    }
                    catch (Exception e)
                    {
                        MizukiModCore.logger.info(e);
                    }
                }
            }

            int increaseCount = (totaldamage / magicNumber3) * 2;
            if (increaseCount > 0)
            {
                //addToBot((AbstractGameAction) new LearnAction(this.uuid, increaseCount));
                for (int i = 0;i < increaseCount;i++)
                {
                    LearnManager.CounterIncreaseAndCheck(this, magicNumber3);
                    AbstractMizukiCard c = (AbstractMizukiCard)LearnManager.findInMasterDeck(this);
                    if (c != null)
                    {
                        LearnManager.CounterIncreaseAndCheck(c, magicNumber3);
                        c.applyPowers();
                    }
                }
            }
        }

        applyPowers();

        int enemyCount = 0;
        for (AbstractMonster m2 : (AbstractDungeon.getCurrRoom()).monsters.monsters)
        {
            if (m2.isDead || m2.isDying)
            {
                continue;
            }
            enemyCount++;
        }



        int[] damageCount = new int[enemyCount];

        for (int i = 0;i < this.damage;i++)
        {
            int ran = MathUtils.random(0, enemyCount - 1);
            damageCount[ran]++;
        }

        int i = 0;
        for (AbstractMonster m2 : (AbstractDungeon.getCurrRoom()).monsters.monsters)
        {
            if (m2.isDead || m2.isDying)
            {
                continue;
            }
            addToBot((AbstractGameAction)new DamageAction((AbstractCreature)m2, new DamageInfo((AbstractCreature)AbstractDungeon.player, damageCount[i++]), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        }
    }

    public void upgrade()
    {
        if (!this.upgraded)
        {
            upgradeName();
            upgradeMagicNumber4(10);
        }
    }
    public void applyPowers()
    {
        if (this.magicNumber > this.magicNumber4)
        {
            this.magicNumber = this.magicNumber4;
        }

        if (AbstractDungeon.player != null)
        {
            if (!this.upgraded)
            {
                if (LearnManager.ifInMasterDeck(this))
                {
                    LearnManager.CounterIncreaseAndCheck(this);
                }
                else
                {
                    LearnManager.synchronise(this);
                }
            }

            //针对该牌单独在战斗中升级的情况做出特殊处理
            if (this.upgraded)
            {
                AbstractCard c = LearnManager.findInMasterDeck(this);
                if ( c != null && !c.upgraded)
                {
                    LearnManager.CounterIncreaseAndCheck(this);
                }
                if ( c != null && c.upgraded)
                {
                    if (!LearnManager.synchronise(this))
                    {
                        LearnManager.CounterIncreaseAndCheck(this);
                    }
                }
            }
            this.baseDamage = this.magicNumber;
            super.applyPowers();
            initializeDescription();
        }

        //super.applyPowers();
        /*
        if (this.misc > this.baseMagicNumber4)
        {
            this.misc = this.baseMagicNumber4;
        }
        this.baseMagicNumber2 = this.misc;
        this.magicNumber2 = this.baseMagicNumber2;*/
/*
        if (!LearnManager.ifInMasterDeck(this) && !this.upgraded)
        {
            LearnManager.synchronise(this);
        }
        LearnManager.CounterIncreaseAndCheck(this);


        //this.calculateCardDamage(null);
        initializeDescription();*/

    }
    public void triggerOnGlowCheck()
    {
        if (this.magicNumber >= this.magicNumber4)
            return;
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        int totaldamage = 0;
        for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters)
        {
            if (m.isDead || m.isDying)
            {
                continue;
            }
            if (m.intent == AbstractMonster.Intent.ATTACK || m.intent == AbstractMonster.Intent.ATTACK_BUFF
                    || m.intent == AbstractMonster.Intent.ATTACK_DEBUFF || m.intent == AbstractMonster.Intent.ATTACK_DEFEND)
            {
                try
                {
                    Class<?> C = AbstractMonster.class;
                    Field nameField = C.getDeclaredField("move");
                    nameField.setAccessible(true);
                    EnemyMoveInfo eMI = (EnemyMoveInfo) nameField.get(m);
                    int multiplier = 1;
                    if (eMI.isMultiDamage)
                    {
                        multiplier = eMI.multiplier;
                    }
                    totaldamage += m.getIntentDmg() * multiplier;
                }
                catch (Exception e)
                {
                    MizukiModCore.logger.info(e);
                }
            }
        }
        if (totaldamage >= magicNumber3)
        {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        }
        else
        {
            this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }

    public AbstractCard makeCopy()
    {
        return new StrengtheningStab();
    }
}
