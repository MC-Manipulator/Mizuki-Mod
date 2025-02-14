package powers;

import Impairment.CorrosionImpairment;
import actions.ApplyImpairmentAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import modcore.MizukiModCore;
import monsters.boss.beyond.ParanoiaIllusion;
import vfx.MissedWordEffect;

public class SilencePower extends AbstractMizukiPower
{
    public static final String id = MizukiModCore.MakePath(SilencePower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(id);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public SilencePower(AbstractCreature owner, int amt)
    {
        super(owner, amt, id, NAME);
        updateDescription();
        this.type = PowerType.BUFF;
    }

    public String getDescription()
    {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action)
    {
        flashWithoutSound();
        this.amount++;
        if (this.amount >= 4)
        {
            this.amount = 0;
            playApplyPowerSfx();
            if (owner instanceof ParanoiaIllusion)
            {
                AbstractDungeon.effectList.add(new MissedWordEffect(this.owner, this.owner.hb.cX, this.owner.hb.cY, DESCRIPTIONS[1]));
                ((ParanoiaIllusion)owner).addMultiplier();
            }
            flash();
        }
        updateDescription();
    }
}
