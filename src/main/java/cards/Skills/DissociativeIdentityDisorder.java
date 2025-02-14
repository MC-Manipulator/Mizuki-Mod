package cards.Skills;

import actions.LearnAction;
import actions.PowerStoreAction;
import cards.AbstractMizukiCard;
import characters.Mizuki;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import helper.LearnManager;
import modcore.MizukiModCore;
import patches.FoodCardColorEnumPatch;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.UUID;

public class DissociativeIdentityDisorder extends AbstractMizukiCard
{
    public static final String ID = MizukiModCore.MakePath(DissociativeIdentityDisorder.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public DissociativeIdentityDisorder()
    {
        super(ID, false, cardStrings, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        //m2为当前卡牌置入数量，m为学习需求数，m3为当前剩余数量
        //M4为学习上限，M3为计数器需求数，M2为计数器，M为学习数
        setupMagicNumber(1, 0, 5, 5);
        storeCardUUID = new ArrayList<>();
        this.tags.add(Mizuki.Enums.LEARNING_CARD);

        tags.add(CardTags.HEALING);
        /*
        this.misc = 0;
        this.baseMagicNumber = 5;
        this.magicNumber = this.baseMagicNumber;
        this.baseMagicNumber2 = 1;
        this.magicNumber2 = this.baseMagicNumber2;
        this.baseMagicNumber3 = this.misc;
        this.magicNumber3 = this.baseMagicNumber3;
        this.baseMagicNumber4 = 5;
        this.magicNumber4 = this.baseMagicNumber4;*/

        //this.hasLearnLimit = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m)
    {

        for (int i = 0;i < this.magicNumber;i++)
        {
            CardRarity cardRarity;
            int roll = AbstractDungeon.cardRandomRng.random(99);
            if (roll < 55)
            {
                cardRarity = CardRarity.COMMON;
            }
            else if (roll < 85)
            {
                cardRarity = CardRarity.UNCOMMON;
            }
            else
            {
                cardRarity = CardRarity.RARE;
            }
            AbstractCard c;
            while (true)
            {
                c = CardLibrary.getAnyColorCard(cardRarity);
                if (c.color != Mizuki.Enums.MIZUKI_CARD && c.color != FoodCardColorEnumPatch.CardColorPatch.FOOD && !c.tags.contains(CardTags.HEALING))
                {
                    break;
                }
            }

            storeCardUUID.add(c.uuid);
            addToBot((AbstractGameAction)new MakeTempCardInHandAction(c, true, true));
        }
    }

    public void triggerOnCardPlayed(AbstractCard cardPlayed)
    {
        if (magicNumber < magicNumber4)
        {
            for (UUID u : storeCardUUID)
            {
                if (u == cardPlayed.uuid)
                {
                    /*
                    addToTop((AbstractGameAction) new LearnAction(this.uuid, 1));
                    for (AbstractCard c : AbstractDungeon.player.masterDeck.group)
                    {
                        if (!(c instanceof DissociativeIdentityDisorder))
                        {
                            continue;
                        }
                        AbstractMizukiCard mc = (AbstractMizukiCard)c;
                        if (!mc.uuid.equals(this.uuid))
                            continue;
                        mc.applyPowers();
                    }*/
                    LearnManager.CounterIncreaseAndCheck(this, 1);
                    AbstractMizukiCard c = (AbstractMizukiCard)LearnManager.findInMasterDeck(this);
                    if (c != null)
                    {
                        LearnManager.CounterIncreaseAndCheck(c, 1);
                    }
                    applyPowers();
                    break;
                }
            }
        }
    }

    public void applyPowers()
    {
        if (!LearnManager.ifInMasterDeck(this))
        {
            LearnManager.synchronise(this);
        }

        LearnManager.CounterIncreaseAndCheck(this);

        /*
        if (magicNumber2 > magicNumber4)
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
                    if (!(c instanceof DissociativeIdentityDisorder))
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
                    this.baseMagicNumber2++;
                    this.misc = 0;
                }
                this.baseMagicNumber3 = this.misc;
                this.magicNumber3 = this.baseMagicNumber3;
                this.magicNumber2 = this.baseMagicNumber2;
            }
        }*/

        super.applyPowers();
        initializeDescription();
    }

    public void upgrade()
    {
        if (!this.upgraded)
        {
            upgradeName();
            upgradeBaseCost(0);

            if (AbstractDungeon.isPlayerInDungeon())
            {
                if (AbstractDungeon.player != null)
                {
                    applyPowers();
                }
            }
        }
    }

    public AbstractCard makeCopy()
    {
        return new DissociativeIdentityDisorder();
    }
}
