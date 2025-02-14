package cards.Skills;

import actions.LearnAction;
import cards.AbstractMizukiCard;
import characters.Mizuki;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import helper.LearnManager;
import modcore.MizukiModCore;
import patches.FoodCardColorEnumPatch;

import java.util.ArrayList;
import java.util.UUID;

public class CramUp extends AbstractMizukiCard
{
    public static final String ID = MizukiModCore.MakePath(CramUp.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public CramUp()
    {
        super(ID, false, cardStrings, 2, CardType.SKILL, CardRarity.COMMON, CardTarget.NONE);
        //M4为学习上限，M3为计数器需求数，M2为计数器，M为学习数


        setupMagicNumber(0, 0, 1, 5);
        storeCard = new ArrayList<AbstractCard>();
        this.tags.add(Mizuki.Enums.LEARNING_CARD);
        tags.add(CardTags.HEALING);

        /*
        this.misc = 0;
        this.baseMagicNumber = this.misc;
        this.magicNumber = this.baseMagicNumber;
        this.baseMagicNumber4 = 5;
        this.magicNumber4 = this.baseMagicNumber4;
        this.hasLearnLimit = true;*/
    }

    public void use(AbstractPlayer p, AbstractMonster m)
    {
        //MizukiModCore.logger.info(misc);
        //MizukiModCore.logger.info(storeCard.size());
        for (int i = 0;i < storeCard.size();i++)
        {
            int temp = i;
            float x = this.current_x;
            float y = this.current_y;
            AbstractMizukiCard card = this;
            AbstractCard tmp = card.storeCard.get(temp).makeSameInstanceOf();

            addToBot(new AbstractGameAction()
            {
                @Override
                public void update()
                {
                    //MizukiModCore.logger.info("size" + card.storeCard.size());
                    AbstractMonster selectm = null;
                    selectm = (AbstractMonster) (AbstractDungeon.getCurrRoom()).monsters.getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
                    AbstractDungeon.player.limbo.addToBottom(tmp);
                    tmp.current_x = x + temp * 10F;
                    tmp.current_y = y;
                    tmp.target_x = Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
                    tmp.target_y = Settings.HEIGHT / 2.0F;
                    if (selectm != null)
                        tmp.calculateCardDamage(selectm);
                    tmp.purgeOnUse = true;
                    AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(tmp, selectm, tmp.energyOnUse, true, true), true);
                    isDone = true;
                }
            });

        }

        if (!upgraded)
        {
            this.storeCard.clear();
            this.baseMagicNumber = 0;
            this.magicNumber = this.baseMagicNumber;
            AbstractMizukiCard c = (AbstractMizukiCard) LearnManager.findInMasterDeck(this);
            if (c != null)
            {
                c.storeCard.clear();
                c.baseMagicNumber = 0;
                c.magicNumber = c.baseMagicNumber;
                c.applyPowers();
            }
            applyPowers();
            //MizukiModCore.logger.info("result");
            //MizukiModCore.logger.info("result" + storeCard.size());
            /*
            this.misc = 0;
            for (AbstractCard card : AbstractDungeon.player.masterDeck.group)
            {
                if (!(card instanceof CramUp))
                {
                    continue;
                }
                AbstractMizukiCard mc = (AbstractMizukiCard)card;
                if (!mc.uuid.equals(this.uuid))
                    continue;
                if (mc.storeCard.size() == 0)
                {
                    break;
                }
                mc.storeCard.clear();
                MizukiModCore.logger.info("result" + mc.storeCard.size());
                mc.misc = 0;
                mc.applyPowers();
            }*/

        }

        //MizukiModCore.logger.info(misc);
        //MizukiModCore.logger.info(storeCard.size());
    }

    public void triggerOnCardPlayed(AbstractCard cardPlayed)
    {
        MizukiModCore.logger.info(cardPlayed.getClass().getName());
        if (this.magicNumber < magicNumber4)
        {
            if (cardPlayed.color != Mizuki.Enums.MIZUKI_CARD && cardPlayed.color != CardColor.COLORLESS && !cardPlayed.purgeOnUse && cardPlayed.color != FoodCardColorEnumPatch.CardColorPatch.FOOD)
            {
                storeCard.add(cardPlayed.makeCopy());
                MizukiModCore.logger.info("1StoreCardSize" + storeCard.size());
                LearnManager.CounterIncreaseAndCheck(this, 1);
                MizukiModCore.logger.info("2StoreCardSize" + storeCard.size());
                AbstractMizukiCard c = (AbstractMizukiCard)LearnManager.findInMasterDeck(this);
                MizukiModCore.logger.info("3StoreCardSize" + storeCard.size());
                if (c != null)
                {
                    LearnManager.CounterIncreaseAndCheck(c, 1);
                }
                /*
                if (c != null)
                {
                    MizukiModCore.logger.info("4StoreCardSize" + storeCard.size());
                    c.storeCard.add(cardPlayed.makeCopy());
                    MizukiModCore.logger.info("5StoreCardSize" + storeCard.size());
                    c.applyPowers();
                    MizukiModCore.logger.info("6StoreCardSize" + storeCard.size());
                }*/
                applyPowers();
                MizukiModCore.logger.info("7StoreCardSize" + storeCard.size());
            }
        }
        /*
        if(this.misc < magicNumber4)
        {
            if (cardPlayed.color != Mizuki.Enums.MIZUKI_CARD && !cardPlayed.purgeOnUse)
            {
                storeCard.add(cardPlayed.makeCopy());
                addToTop((AbstractGameAction) new LearnAction(this.uuid, 1));
                applyPowers();
                for (AbstractCard card : AbstractDungeon.player.masterDeck.group)
                {
                    if (!(card instanceof CramUp))
                    {
                        continue;
                    }
                    AbstractMizukiCard mc = (AbstractMizukiCard)card;
                    if (!mc.uuid.equals(this.uuid))
                        continue;
                    mc.storeCard.add(cardPlayed.makeCopy());
                    //MizukiModCore.logger.info("storeCardSize ****:" + mc.storeCard.size());
                    mc.applyPowers();
                }
            }
        }*/
    }

    public void applyPowers()
    {
        if (!LearnManager.ifInMasterDeck(this))
        {
            LearnManager.synchronise(this);
        }
        LearnManager.CounterIncreaseAndCheck(this);
        if (this.storeCard.size() == 0)
        {
            LearnManager.CounterIncreaseAndCheck(this);
            AbstractMizukiCard c = (AbstractMizukiCard)LearnManager.findInMasterDeck(this);
            if (c != null)
            {
                for (AbstractCard tc : c.storeCard)
                {
                    storeCard.add(tc);
                }
            }
        }
        /*
        if (this.misc > magicNumber4)
        {
            this.misc = magicNumber4;
        }
        if (this.storeCard.size() == 0)
        {
            for (AbstractCard card : AbstractDungeon.player.masterDeck.group)
            {
                if (!(card instanceof CramUp))
                {
                    continue;
                }
                AbstractMizukiCard mc = (AbstractMizukiCard)card;
                if (!mc.uuid.equals(this.uuid))
                    continue;

                if (mc.storeCard.size() == 0)
                {
                    break;
                }

                for (AbstractCard tc : mc.storeCard)
                {
                    storeCard.add(tc);
                }
                mc.applyPowers();
            }
        }

        this.baseMagicNumber = this.misc;
        this.magicNumber = this.baseMagicNumber;*/
        super.applyPowers();
        initializeDescription();
    }

    public void upgrade()
    {
        if (!this.upgraded)
        {
            upgradeBaseCost(1);
            upgradeName();
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    public AbstractCard makeCopy()
    {
        return new CramUp();
    }
}
