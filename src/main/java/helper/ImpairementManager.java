package helper;

import Impairment.AbstractImpairment;
import Impairment.CorrosionImpairment;
import Impairment.NervousImpairment;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import modcore.MizukiModCore;
import patches.ImpairmentPatch;
import powers.ArmourPenetrationPower;
import powers.StunMonsterPower;

import java.util.ArrayList;

public class ImpairementManager
{
    public static int DazedAmount = 3;
    public static int playerNervousImpairmentDamage = 10;
    public static int corrosionImpairementDamage = 20;
    public static int nervousImpairementDamage = 20;
    public static int nervousImpairementTimes = 1;
    public static ArrayList<Integer> tempOncenNrvousImpairementDamage = new ArrayList<>();
    public static ArrayList<Integer> tempOnceNervousImpairementTimes = new ArrayList<>();
    public static void increaseNervousImpairment(AbstractCreature c, int count)
    {
        if (getCreatureImpairments(c).size() == 0)
        {
            MizukiModCore.logger.info("create nervous impairment");
            //MizukiModCore.logger.info("arraysize:" + getMonsterImpairments(c).size());
            setNervousImpairmentOnCreature(c);
        }
        MizukiModCore.logger.info(c.name  + " NervousImpairment increase" + count);
        if (getCreatureImpairments(c).size() != 0)
        {
            AbstractImpairment ipm = getCreatureNervousImpairments(c);
            if (ipm == null)
            {
                setNervousImpairmentOnCreature(c);
            }
            ipm = getCreatureNervousImpairments(c);
            if (ipm.isDryout)
                return;
            ipm.fontScale = 3.6F;
            for (int i = 0;i < count;i++)
            {
                MizukiModCore.logger.info("increasing nervous impairment count");
                int roll = MathUtils.random(0, 2);
                if (roll == 0)
                {
                    CardCrawlGame.sound.play("DEBUFF_1");
                }
                else if (roll == 1)
                {
                    CardCrawlGame.sound.play("DEBUFF_2");
                }
                else
                {
                    CardCrawlGame.sound.play("DEBUFF_3");
                }
                ipm.currentcount++;
            }

            //ipm.flash();
        }
        if (getCreatureNervousImpairments(c) != null)
        {
            if (getCreatureNervousImpairments(c).currentcount >= getCreatureNervousImpairments(c).maxcount)
            {
                evokeCreatureNervousImpairments(c);
                resetCreatureNervousImpairments(c);
            }
        }
    }

    public static void decreaseNervousImpairment(AbstractCreature c, int count)
    {
        if (getCreatureImpairments(c).size() == 0)
        {
            MizukiModCore.logger.info("create nervous impairment");
            //MizukiModCore.logger.info("arraysize:" + getMonsterImpairments(c).size());
            setNervousImpairmentOnCreature(c);
        }
        MizukiModCore.logger.info(c.name  + " NervousImpairment decrease" + count);
        if (getCreatureImpairments(c).size() != 0)
        {
            AbstractImpairment ipm = getCreatureNervousImpairments(c);
            if (ipm == null)
            {
                setNervousImpairmentOnCreature(c);
            }
            ipm = getCreatureNervousImpairments(c);
            if (ipm.isDryout)
                return;
            if (ipm.currentcount != 0)
                ipm.fontScale = 3.6F;
            for (int i = 0;i < count;i++)
            {
                MizukiModCore.logger.info("decreasing nervous impairment count");
                /*
                int roll = MathUtils.random(0, 2);
                if (roll == 0)
                {
                    CardCrawlGame.sound.play("DEBUFF_1");
                }
                else if (roll == 1)
                {
                    CardCrawlGame.sound.play("DEBUFF_2");
                }
                else
                {
                    CardCrawlGame.sound.play("DEBUFF_3");
                }*/
                if (ipm.currentcount == 0)
                {
                    break;
                }
                ipm.currentcount--;
            }

            //ipm.flash();
        }
    }

    public static void increaseCorrosionImpairment(AbstractCreature c, int count)
    {
        if (getCreatureImpairments(c).size() == 0)
        {
            MizukiModCore.logger.info("create corrosion impairment");
            //MizukiModCore.logger.info("arraysize:" + getMonsterImpairments(c).size());
            setCorrosionImpairmentOnCreature(c);
        }
        MizukiModCore.logger.info(c.name  + " CorrosionImpairment increase" + count);
        if (getCreatureImpairments(c).size() != 0)
        {
            AbstractImpairment ipm = getCreatureCorrosionImpairments(c);
            if (ipm == null)
            {
                MizukiModCore.logger.info("set corrosion impairment on" + c.name);
                setCorrosionImpairmentOnCreature(c);
            }
            ipm = getCreatureCorrosionImpairments(c);
            if (ipm.isDryout)
                return;
            ipm.fontScale = 3.6F;
            for (int i = 0;i < count;i++)
            {
                MizukiModCore.logger.info("increasing corrosion impairment count");

                int roll = MathUtils.random(0, 2);
                if (roll == 0)
                {
                    CardCrawlGame.sound.play("DEBUFF_1");
                }
                else if (roll == 1)
                {
                    CardCrawlGame.sound.play("DEBUFF_2");
                }
                else
                {
                    CardCrawlGame.sound.play("DEBUFF_3");
                }
                ipm.currentcount++;
            }

            //ipm.flash();
        }
        if (getCreatureCorrosionImpairments(c) != null)
        {
            if (getCreatureCorrosionImpairments(c).currentcount >= getCreatureCorrosionImpairments(c).maxcount)
            {
                evokeCreatureCorrosionImpairments(c);
                resetCreatureCorrosionImpairments(c);
            }
        }
    }

    public static void decreaseCorrosionImpairment(AbstractCreature c, int count)
    {
        if (getCreatureImpairments(c).size() == 0)
        {
            MizukiModCore.logger.info("create corrosion impairment");
            //MizukiModCore.logger.info("arraysize:" + getMonsterImpairments(c).size());
            setNervousImpairmentOnCreature(c);
        }
        MizukiModCore.logger.info(c.name  + " CorrosionImpairment decrease" + count);
        if (getCreatureImpairments(c).size() != 0)
        {
            AbstractImpairment ipm = getCreatureCorrosionImpairments(c);
            if (ipm == null)
            {
                setCorrosionImpairmentOnCreature(c);
            }
            ipm = getCreatureCorrosionImpairments(c);
            if (ipm.isDryout)
                return;
            if (ipm.currentcount != 0)
                ipm.fontScale = 3.6F;
            for (int i = 0;i < count;i++)
            {
                MizukiModCore.logger.info("decreasing corrosion impairment count");
                /*
                int roll = MathUtils.random(0, 2);
                if (roll == 0)
                {
                    CardCrawlGame.sound.play("DEBUFF_1");
                }
                else if (roll == 1)
                {
                    CardCrawlGame.sound.play("DEBUFF_2");
                }
                else
                {
                    CardCrawlGame.sound.play("DEBUFF_3");
                }*/
                if (ipm.currentcount == 0)
                {
                    break;
                }
                ipm.currentcount--;
            }

            //ipm.flash();
        }
    }

    public static void restoreCreatureImpairments(AbstractCreature c)
    {
        if (getCreatureImpairments(c) != null)
        {
            for (AbstractImpairment ipm : getCreatureImpairments(c))
            {
                if (ipm.isDryout)
                {
                    ipm.fontScale = 3.6F;
                    //ipm.flash();
                    int roll = MathUtils.random(0, 2);
                    if (roll == 0)
                    {
                        CardCrawlGame.sound.play("BUFF_1");
                    }
                    else if (roll == 1)
                    {
                        CardCrawlGame.sound.play("BUFF_2");
                    }
                    else
                    {
                        CardCrawlGame.sound.play("BUFF_3");
                    }
                    ipm.currentDryout++;
                    if (ipm.currentDryout >= ipm.maxDryout)
                    {
                        ipm.currentDryout = 0;
                        ipm.isDryout = false;
                    }
                }
            }
        }
    }
    public static void evokeCreatureNervousImpairments(AbstractCreature c)
    {
        if (getCreatureNervousImpairments(c) != null)
        {

            for (int i = 0;i < nervousImpairementTimes;i++)
            {
                if (c instanceof AbstractMonster)
                {
                    AbstractDungeon.actionManager.addToBottom(
                        (AbstractGameAction) new LoseHPAction(c , null, (nervousImpairementDamage), AbstractGameAction.AttackEffect.POISON));
                    for (int j = 0;j < tempOnceNervousImpairementTimes.size();j++)
                    {
                        //MizukiModCore.logger.info("SIZE" + tempOnceImpairementTimes.size());
                        //MizukiModCore.logger.info("Times" + tempOnceImpairementTimes.get(j));
                        for (int k = 0;k < tempOnceNervousImpairementTimes.get(j);k++)
                        {
                            AbstractDungeon.actionManager.addToBottom(
                                    (AbstractGameAction) new LoseHPAction(c , null, (tempOncenNrvousImpairementDamage.get(j)), AbstractGameAction.AttackEffect.POISON));
                        }
                    }
                }

            }
            if (c instanceof AbstractPlayer)
            {
                AbstractDungeon.actionManager.addToBottom(
                        (AbstractGameAction) new LoseHPAction(c , null, playerNervousImpairmentDamage, AbstractGameAction.AttackEffect.POISON));
            }
            if (EventHelper.isPlayerTurn)
            {
                if (c instanceof AbstractPlayer)
                {
                    AbstractDungeon.actionManager.addToBottom(
                            (AbstractGameAction)new MakeTempCardInDrawPileAction((AbstractCard)new Dazed(), DazedAmount, true, true));
                }
                else
                {
                    AbstractDungeon.actionManager.addToBottom(
                            (AbstractGameAction) new ApplyPowerAction((AbstractCreature) c, null,
                                    (AbstractPower) new StunMonsterPower(c, 1), 1, true, AbstractGameAction.AttackEffect.SMASH));
                }

            }
            else
            {
                if (c instanceof AbstractPlayer)
                {
                    AbstractDungeon.actionManager.addToBottom(
                            (AbstractGameAction)new MakeTempCardInDrawPileAction((AbstractCard)new Dazed(), DazedAmount, true, true));
                }
                else
                {
                    for (int i = 0;i < EventHelper.applyStunnedWhenTurnStartTargets.size();i++)
                    {
                        if (EventHelper.applyStunnedWhenTurnStartTargets.get(i) == c)
                        {
                            EventHelper.applyStunnedWhenTurnStart.set(i, EventHelper.applyStunnedWhenTurnStart.get(i) + 1);
                            break;
                        }
                    }
                    EventHelper.applyStunnedWhenTurnStartTargets.add(c);
                    EventHelper.applyStunnedWhenTurnStart.add(1);
                }
            }

            tempOncenNrvousImpairementDamage.clear();
            tempOnceNervousImpairementTimes.clear();
            getCreatureNervousImpairments(c).isDryout = true;
        }
    }

    public static void evokeCreatureCorrosionImpairments(AbstractCreature c)
    {
        if (getCreatureCorrosionImpairments(c) != null)
        {
            AbstractDungeon.actionManager.addToBottom(
                    (AbstractGameAction) new ApplyPowerAction((AbstractCreature) c, null,
                            (AbstractPower) new ArmourPenetrationPower(c, 2), 2));
            AbstractDungeon.actionManager.addToBottom(
                    (AbstractGameAction) new LoseHPAction(c , null, corrosionImpairementDamage, AbstractGameAction.AttackEffect.POISON));
                    /*(AbstractGameAction) new LoseHPAction(c , null, (corrosionImpairementDamage), AbstractGameAction.AttackEffect.POISON));*/
            getCreatureCorrosionImpairments(c).isDryout = true;
        }
    }

    public static void resetCreatureNervousImpairments(AbstractCreature c)
    {
        if (getCreatureNervousImpairments(c) != null)
            getCreatureNervousImpairments(c).currentcount = 0;
    }

    public static void resetCreatureCorrosionImpairments(AbstractCreature c)
    {
        if (getCreatureCorrosionImpairments(c) != null)
            getCreatureCorrosionImpairments(c).currentcount = 0;
    }

    public static AbstractImpairment getCreatureNervousImpairments(AbstractCreature c)
    {
        for (AbstractImpairment ipm : getCreatureImpairments(c))
        {
            if (ipm.type == AbstractImpairment.ImpairmentType.Nervous)
            {
                return ipm;
            }
        }
        return null;
    }
    public static AbstractImpairment getCreatureCorrosionImpairments(AbstractCreature c)
    {
        for (AbstractImpairment ipm : getCreatureImpairments(c))
        {
            if (ipm.type == AbstractImpairment.ImpairmentType.Corrosion)
            {
                return ipm;
            }
        }
        return null;
    }
    public static ArrayList<AbstractImpairment> getCreatureImpairments(AbstractCreature c)
    {
        return ((ArrayList<AbstractImpairment>) ImpairmentPatch.ImpairmentFieldsPatch.impairments.get(c));
    }
    public static void setNervousImpairmentOnCreature(AbstractCreature c)
    {
        AbstractImpairment ipm = new NervousImpairment();
        ipm.creature = c;
        getCreatureImpairments(c).add(ipm);
        setUIOnCreature(c, ipm);
    }

    public static void setCorrosionImpairmentOnCreature(AbstractCreature c)
    {
        AbstractImpairment ipm = new CorrosionImpairment();
        ipm.creature = c;
        getCreatureImpairments(c).add(ipm);
        setUIOnCreature(c, ipm);
    }
    public static void setUIOnCreature(AbstractCreature c, AbstractImpairment ipm)
    {
        /*
        MizukiModCore.logger.info(mo.drawX);
        MizukiModCore.logger.info(mo.healthHb.x);
        MizukiModCore.logger.info(mo.drawY);
        MizukiModCore.logger.info(mo.healthHb.y);

         */
        /*
        ipm.tX = c.healthHb.x + c.healthHb.width / 2F - 30F;
        ipm.tY = c.healthHb.y - 25F;*/

        if (getCreatureImpairments(c).size() == 1)
        {
            getCreatureImpairments(c).get(0).tX = c.hb.cX;
            getCreatureImpairments(c).get(0).tY = c.hb.cY - c.hb.height / 2F;
        }

        if (getCreatureImpairments(c).size() == 2)
        {
            getCreatureImpairments(c).get(0).tX = c.hb.cX - 100 / 2F;
            getCreatureImpairments(c).get(0).tY = c.hb.cY - c.hb.height / 2F;

            getCreatureImpairments(c).get(1).tX = c.hb.cX + 100 / 2F;
            getCreatureImpairments(c).get(1).tY = c.hb.cY - c.hb.height / 2F;
        }
    }
}
