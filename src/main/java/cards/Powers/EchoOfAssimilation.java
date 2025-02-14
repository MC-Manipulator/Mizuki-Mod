package cards.Powers;

import basemod.abstracts.AbstractCardModifier;
import basemod.cardmods.ExhaustMod;
import basemod.helpers.CardModifierManager;
import cards.AbstractMizukiCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import modcore.MizukiModCore;
import patches.AssimilationMod;

public class EchoOfAssimilation  extends AbstractMizukiCard
{
    public static final String ID = MizukiModCore.MakePath(EchoOfAssimilation.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public CardStrings strings;

    public int targetamount = 0;
    public int requireamount = 0;
    public int processamount = 0;
    public AssimilationMod mod = null;

    public EchoOfAssimilation()
    {
        super(ID, false, cardStrings, 1, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        this.selfRetain = true;
        strings = cardStrings;
        this.tags.add(AbstractCard.CardTags.HEALING);
    }

    @Override
    public boolean canUpgrade()
    {
        return false;
    }

    public void use(AbstractPlayer p, AbstractMonster m)
    {

        MizukiModCore.logger.info("target" + targetamount);

        if (targetamount != 0)
        {
            processamount++;
            baseMagicNumber++;
            magicNumber = baseMagicNumber;
            mod.targetamount = targetamount;
            mod.requireamount = requireamount;
            mod.processamount = processamount;
        }

        if (targetamount == 1)
        {
            MizukiModCore.logger.info("TARGET1");

            if (magicNumber < magicNumber2)
            {
                addToBot((AbstractGameAction) new MakeTempCardInHandAction(this, false, true));
            }
            if (magicNumber == magicNumber2)
            {
                for(int i = 0;i < AbstractDungeon.player.potionSlots;i++)
                {
                    addToBot((AbstractGameAction)new ObtainPotionAction(AbstractDungeon.returnRandomPotion(true)));
                }
            }
        }

        if (targetamount == 2)
        {
            MizukiModCore.logger.info("TARGET2");

            if (magicNumber < magicNumber2)
            {
                addToBot((AbstractGameAction) new MakeTempCardInHandAction(this, false, true));
            }
            if (magicNumber == magicNumber2)
            {
                AbstractDungeon.getCurrRoom().addRelicToRewards(AbstractRelic.RelicTier.COMMON);
            }
        }

        if (targetamount == 3)
        {
            MizukiModCore.logger.info("TARGET3");

            if (magicNumber < magicNumber2)
            {
                addToBot((AbstractGameAction) new MakeTempCardInHandAction(this, false, true));
            }
            if (magicNumber == magicNumber2)
            {
                for (int i = 0;i < 3;i++)
                {
                    AbstractDungeon.getCurrRoom().addCardReward(new RewardItem());
                }
            }
        }

        if (targetamount == 4)
        {
            MizukiModCore.logger.info("TARGET4");

            if (magicNumber < magicNumber2)
            {
                addToBot((AbstractGameAction) new MakeTempCardInHandAction(this, false, true));
            }
            if (magicNumber == magicNumber2)
            {
                AbstractDungeon.player.increaseMaxHp(10, true);
            }
        }

        if (targetamount == 0)
        {
            MizukiModCore.logger.info("INITIALIZE");
            //此处应有生成随机数的功能
            int roll = AbstractDungeon.cardRandomRng.random(99);
            if (roll < 50)
            {
                targetamount = 1;
            }
            else if (roll < 70)
            {
                targetamount = 4;
            }
            else if (roll < 90)
            {
                targetamount = 3;
            }
            else
            {
                targetamount = 2;
            }
            //

            mod = new AssimilationMod(this, targetamount);

            if (targetamount == 1)
            {
                requireamount = 2;
                setupMagicNumber(0, requireamount, 0, 0);
            }

            if (targetamount == 2)
            {
                requireamount = 7;
                setupMagicNumber(0, requireamount, 0, 0);
            }

            if (targetamount == 3)
            {
                requireamount = 5;
                setupMagicNumber(0, requireamount, 0, 0);
            }

            if (targetamount == 4)
            {
                requireamount = 4;
                setupMagicNumber(0, requireamount, 0, 0);
            }

            mod.targetamount = targetamount;
            mod.requireamount = requireamount;
            mod.processamount = processamount;
            CardModifierManager.addModifier(this, (AbstractCardModifier)mod);
            addToBot((AbstractGameAction) new MakeTempCardInHandAction(this, false, true));



        }
        applyPowers();
    }

    public void applyPowers()
    {
        setupMagicNumber(processamount, requireamount, 0, 0);
        super.applyPowers();
        initializeDescription();
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
        return new EchoOfAssimilation();
    }
}
