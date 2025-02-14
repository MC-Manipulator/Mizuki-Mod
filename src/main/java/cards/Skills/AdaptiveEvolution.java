package cards.Skills;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import basemod.interfaces.PostDrawSubscriber;
import cards.AbstractMizukiCard;
import characters.Mizuki;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import helper.EventHelper;
import helper.LearnManager;
import modcore.MizukiModCore;

public class AdaptiveEvolution extends AbstractMizukiCard implements EventHelper.OnHealthChangedSubscriber
{
    public static final String ID = MizukiModCore.MakePath(AdaptiveEvolution.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public AdaptiveEvolution()
    {
        super(ID, false, cardStrings, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        //M4为学习上限，M3为计数器需求数，M2为计数器，M为学习数
        setupMagicNumber(1, 0, 6, 4);
        this.tags.add(Mizuki.Enums.LEARNING_CARD);

        tags.add(CardTags.HEALING);
        //MizukiModCore.logger.info("onCreat");
    }

    public void use(AbstractPlayer p, AbstractMonster m)
    {
        addToBot((AbstractGameAction)new DrawCardAction(this.magicNumber));
        addToBot(new AbstractGameAction()
        {
            @Override
            public void update()
            {
                DrawCardAction.drawnCards.forEach(
                        (card) ->
                        {
                            if (card.canUpgrade())
                            {
                                card.upgrade();
                                card.superFlash();
                                card.applyPowers();
                            }
                        }
                );
                this.isDone = true;
            }
        });
    }

    @Override
    public void OnHealthChanged(int delta)
    {
        LearnManager.CounterIncreaseAndCheck(this, 1);
        applyPowers();
    }

    public void applyPowers()
    {
        if (AbstractDungeon.player != null)
        {
            //一般情况
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
            super.applyPowers();
            initializeDescription();
            /*
            AbstractMizukiCard cardindeck = (AbstractMizukiCard)LearnManager.findInMasterDeck(this);
            if (this.magicNumber4 != this.magicNumber)
            {
                if (cardindeck != null)
                {
                    if (cardindeck.magicNumber4 == cardindeck.magicNumber)
                    {
                        LearnManager.CounterIncreaseAndCheck(this);
                        MizukiModCore.logger.info("1");
                        super.applyPowers();
                        initializeDescription();
                        return;
                    }
                }
            }
            if (cardindeck != null)
                LearnManager.CounterIncreaseAndCheck(cardindeck);
            else
            {
                LearnManager.CounterIncreaseAndCheck(this);
                LearnManager.synchronise(this);
            }*/
        }

        /*
        if (magicNumber > magicNumber4)
        {
            baseMagicNumber2 = magicNumber4;
        }
        if (AbstractDungeon.player != null)
        {
            boolean find = false;
            if (!AbstractDungeon.player.masterDeck.group.contains(this))
            {
                for (AbstractCard c : AbstractDungeon.player.masterDeck.group)
                {
                    if (!(c instanceof AdaptiveEvolution))
                    {
                        continue;
                    }
                    AbstractMizukiCard mc = (AbstractMizukiCard)c;
                    if (!mc.uuid.equals(this.uuid))
                        continue;
                    mc.applyPowers();

                    this.baseMagicNumber = mc.baseMagicNumber;
                    this.magicNumber = mc.magicNumber;
                    this.baseMagicNumber3 = mc.baseMagicNumber3;
                    this.magicNumber3 = mc.magicNumber3;
                    this.baseMagicNumber4 = mc.baseMagicNumber4;
                    this.magicNumber4 = mc.baseMagicNumber4;
                    find = true;
                }
            }
            if (AbstractDungeon.player.masterDeck.group.contains(this) || !find)
            {
                if (this.misc >= this.magicNumber)
                {
                    this.baseMagicNumber2++;
                    this.misc = 0;
                }
                this.baseMagicNumber3 = this.misc;
                this.magicNumber3 = this.baseMagicNumber3;
                this.magicNumber = this.baseMagicNumber;
            }
        }*/

    }

    public void upgrade()
    {
        if (!this.upgraded)
        {
            upgradeName();
            upgradeMagicNumber4(1);
        }
    }
    public AbstractCard makeCopy()
    {
        return new AdaptiveEvolution();
    }
}
