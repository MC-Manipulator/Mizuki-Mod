package actions;

import Impairment.AbstractImpairment;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.vfx.combat.PowerDebuffEffect;
import helper.EventHelper;
import helper.ImpairementManager;
import modcore.MizukiModCore;
import relics.NightsunGrass;
import relics.Unleashings;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.returnTrulyRandomCardInCombat;

public class ApplyImpairmentAction extends AbstractGameAction
{
    public AbstractImpairment ipm;
    public AbstractImpairment.ImpairmentType type;
    public AbstractCreature c;
    public int count = 0;

    public ApplyImpairmentAction(AbstractImpairment ipm, AbstractCreature c, int count)
    {
        this(ipm, c, count, ipm.type);
    }

    public ApplyImpairmentAction(AbstractImpairment ipm, AbstractCreature c, int count, AbstractImpairment.ImpairmentType type)
    {
        this.ipm = ipm;
        this.c = c;
        this.count = count;
        this.type = type;
    }

    public void update()
    {
        if(!c.isDeadOrEscaped())
        {
            if (count > 0)
            {
                if (type == AbstractImpairment.ImpairmentType.Nervous)
                {
                    if (c instanceof AbstractPlayer)
                    {
                        if (((AbstractPlayer)c).hasRelic(NightsunGrass.ID))
                        {
                            NightsunGrass NG = (NightsunGrass)((AbstractPlayer)c).getRelic(NightsunGrass.ID);
                            if (!(NG).hasTrigger)
                            {
                                (NG).flash();
                                (NG).hasTrigger = true;
                                addToTop((AbstractGameAction)new RelicAboveCreatureAction((AbstractCreature)AbstractDungeon.player,
                                        (NG)));
                                NG.stopPulse();
                                isDone = true;
                                return;
                            }
                        }

                        //触发绽放的获得能量效果
                        if (((AbstractPlayer)c).hasRelic(Unleashings.ID))
                        {
                            Unleashings UnL = (Unleashings)((AbstractPlayer)c).getRelic(Unleashings.ID);
                            if (!(UnL).hasImpairment)
                            {
                                (UnL).flash();
                                (UnL).hasImpairment = true;
                            }
                            UnL.beginLongPulse();
                        }
                    }
                    CardCrawlGame.sound.play("POWER_POISON", 0.05F);
                    this.c.useFastShakeAnimation(0.5F);
                    ImpairementManager.increaseNervousImpairment(c, count);
                }
                else if (type == AbstractImpairment.ImpairmentType.Corrosion)
                {
                    CardCrawlGame.sound.play("POWER_POISON", 0.05F);
                    this.c.useFastShakeAnimation(0.5F);
                    ImpairementManager.increaseCorrosionImpairment(c, count);
                }
            }
            else if (count < 0)
            {
                CardCrawlGame.sound.play("NULLIFY_SFX");
                if (type == AbstractImpairment.ImpairmentType.Nervous)
                {
                    ImpairementManager.decreaseNervousImpairment(c, -count);
                }
                else if (type == AbstractImpairment.ImpairmentType.Corrosion)
                {
                    ImpairementManager.decreaseCorrosionImpairment(c, -count);
                }
            }
        }
        isDone = true;
    }
}
