package helper;

import Impairment.CorrosionImpairment;
import Impairment.NervousImpairment;
import actions.ApplyImpairmentAction;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import modcore.MizukiModCore;
import patches.DicePatch;
import relics.BlankCoral;
import relics.MizukisD8;
import relics.MizukisDice;
import vfx.DiceEffect;

import java.util.ArrayList;

public class DiceHelper
{
    public static void gainDice(int count)
    {
        AbstractRelic dice = (AbstractRelic) DiceHelper.getDice();

        if (DiceHelper.hasDice())
        {
            MizukiModCore.logger.info("Gain Dice" + count);
            dice.counter += count;
        }
        else
        {
            AbstractRelic relic = new MizukisDice(false);
            dice = relic;
            EventHelper.relicList.add(relic);
            relic.flash();
            relic.counter += count;
        }
        if (count != 0)
        {
            dice.flash();
        }
    }

    public static void rollDice(int roll, AbstractPlayer p, AbstractMonster m)
    {
        ArrayList<Integer> list = new ArrayList<>();



        addToBot(new AbstractGameAction()
        {
            boolean justIn = true;

            @Override
            public void update()
            {
                if (justIn)
                {
                    this.duration = 0.75F;
                    this.justIn = false;
                    CardCrawlGame.sound.play("MIZUKI_ThrowDice", 0.05F);
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
                }
                tickDuration();
            }
        });

        for (int i = 0; i < MathUtils.ceil(roll / 3F); i++)
        {
            while (true)
            {
                int roll2 = MathUtils.random(2);
                if (!list.contains(roll2))
                {
                    list.add(roll2);
                    break;
                }
            }

            switch (list.get(list.size() - 1))
            {
                case 0:
                    addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, MathUtils.ceil(roll / 2f))));
                    addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, MathUtils.ceil(roll / 2f))));
                    break;
                case 1:
                    addToBot(new ApplyPowerToRandomEnemyAction(p, new WeakPower(null, roll, false), roll));
                    addToBot(new ApplyPowerToRandomEnemyAction(p, new VulnerablePower(null, roll, false), roll));
                    break;
                case 2:
                    addToBot(new GainBlockAction(p, roll * 2));
                    addToBot(new ApplyImpairmentAction(new NervousImpairment(), p, -roll));
                    addToBot(new ApplyImpairmentAction(new CorrosionImpairment(), p, -roll));
                    break;
            }
        }

        //触发无字珊瑚遗物
        if (p.hasRelic(BlankCoral.ID))
        {
            (p.getRelic(BlankCoral.ID)).flash();
            addToBot(new HealAction(p, p, roll));
        }
    }

    public static boolean isD6()
    {
        AbstractPlayer p = AbstractDungeon.player;
        if (hasDice())
        {
            return p.hasRelic(MizukisDice.ID);
        }
        return false;
    }

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

    private static void addToBot(AbstractGameAction action)
    {
        AbstractDungeon.actionManager.addToBottom(action);
    }
}
