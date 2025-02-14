package cards.Skills;

import Impairment.CorrosionImpairment;
import Impairment.NervousImpairment;
import actions.ApplyImpairmentAction;
import actions.GainDiceAction;
import basemod.cardmods.EtherealMod;
import basemod.cardmods.ExhaustMod;
import basemod.helpers.CardModifierManager;
import cards.AbstractMizukiCard;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import helper.DiceHelper;
import modcore.MizukiModCore;
import patches.DicePatch;
import powers.ArousalPower;
import relics.MizukisD8;
import relics.MizukisDice;
import vfx.DiceEffect;

import java.util.ArrayList;

public class Dice extends AbstractMizukiCard
{
    public static final String ID = MizukiModCore.MakePath(Dice.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private DicePatch dice;

    public Dice()
    {
        super(ID, false, cardStrings, 0, CardType.SKILL, CardRarity.BASIC, CardTarget.SELF);
        setupMagicNumber(0, 0, 0, 0);
        this.selfRetain = true;

    }


    public void use(AbstractPlayer p, AbstractMonster m)
    {
        int roll = 0;
        if (DiceHelper.isD6())
        {
            roll = AbstractDungeon.cardRandomRng.random(1, 6);
        }
        else
        {
            roll = AbstractDungeon.cardRandomRng.random(1, 8);
        }
        dice = DiceHelper.getDice();
        if (dice != null)
        {
            if (dice.getCounter() > 0)
            {
                addToBot(new GainDiceAction(-1));
                DiceHelper.rollDice(roll, p, m);
            }
        }
        else
        {
            AbstractRelic relic = new MizukisDice(false);
            relic.obtain();
            relic.isObtained = true;
            relic.isAnimating = false;
            relic.isDone = false;
            relic.flash();
            addToBot(new GainDiceAction(-1));
            DiceHelper.rollDice(roll, p, m);
        }
    }

    public void upgrade()
    {
        if (!this.upgraded)
        {
            upgradeName();
        }
    }

    @Override
    public boolean canUpgrade()
    {
        return false;
    }

    public AbstractCard makeCopy()
    {
        return new Dice();
    }

    @Override
    public void update()
    {
        super.update();
        if (AbstractDungeon.isPlayerInDungeon() && AbstractDungeon.player != null)
        {
            if (dice != null)
            {
                if (dice.getCounter() <= 1)
                {
                    CardModifierManager.addModifier(this, new ExhaustMod());
                }
                if (dice.getCounter() <= 0)
                {
                    CardModifierManager.addModifier(this, new EtherealMod());
                }
                if (dice.getCounter() > 1)
                {
                    CardModifierManager.removeModifiersById(this, ExhaustMod.ID, true);
                    CardModifierManager.removeModifiersById(this, EtherealMod.ID, true);
                }
            }
            else
            {
                dice = DiceHelper.getDice();
                if (dice == null)
                {
                    MizukisDice relic = new MizukisDice(false);
                    relic.obtain();
                    relic.isObtained = true;
                    relic.isAnimating = false;
                    relic.isDone = false;
                    relic.flash();
                    dice = DiceHelper.getDice();
                }
            }
        }
    }

    /*
    public static boolean hasDice()
    {
        AbstractPlayer p = AbstractDungeon.player;
        return p.hasRelic(MizukisDice.ID) || p.hasRelic(MizukisD8.ID);
    }

    public static DicePatch getDice()
    {
        AbstractPlayer p = AbstractDungeon.player;
        DicePatch dice = null;
        if (p.hasRelic(MizukisDice.ID))
        {
            dice = (DicePatch) (MizukisDice)AbstractDungeon.player.getRelic(MizukisDice.ID);
        }
        else if (p.hasRelic(MizukisD8.ID))
        {
            dice = (DicePatch) (MizukisD8)AbstractDungeon.player.getRelic(MizukisD8.ID);
        }
        return dice;
    }

    public static boolean isD6()
    {
        AbstractPlayer p = AbstractDungeon.player;
        if (hasDice())
        {
            if (p.hasRelic(MizukisDice.ID))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        return false;
    }

    private void rollDice(int roll, AbstractPlayer p, AbstractMonster m)
    {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0;i < MathUtils.ceil(roll / 3F);i++)
        {
            while (true)
            {
                int roll2 = MathUtils.random(3);
                if (!list.contains(roll2))
                {
                    list.add(roll2);
                    break;
                }
            }

            addToBot(new AbstractGameAction()
            {
                @Override
                public void update()
                {
                    CardCrawlGame.sound.play("POWER_TIME_WARP", 0.05F);
                    AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.GOLD, true));
                    if (isD6())
                    {
                        MizukiModCore.logger.info("D6Roll" + roll);
                        AbstractDungeon.topLevelEffectsQueue.add(new DiceEffect(roll, true));
                    }
                    else
                    {
                        MizukiModCore.logger.info("D8Roll" + roll);
                        AbstractDungeon.topLevelEffectsQueue.add(new DiceEffect(roll, false));
                    }
                    isDone = true;
                }
            });

            switch (list.get(list.size() - 1))
            {
                case 0:
                    addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, MathUtils.ceil(roll / 2f))));
                    addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, MathUtils.ceil(roll / 2f))));
                    break;
                case 1:
                    addToBot(new DamageRandomEnemyAction(new DamageInfo(p, roll * 3, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                    break;
                case 2:
                    addToBot(new ApplyPowerToRandomEnemyAction(p, new WeakPower(null, roll, false), roll));
                    addToBot(new ApplyPowerToRandomEnemyAction(p, new VulnerablePower(null, roll, false), roll));
                    break;
                case 3:
                    addToBot(new GainBlockAction(p, roll * 2));
                    addToBot(new ApplyImpairmentAction(new NervousImpairment(), p, -roll));
                    addToBot(new ApplyImpairmentAction(new CorrosionImpairment(), p, -roll));
                    break;
            }
        }
    }*/
}