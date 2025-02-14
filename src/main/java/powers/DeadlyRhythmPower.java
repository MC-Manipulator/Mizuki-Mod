package powers;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import modcore.MizukiModCore;

public class DeadlyRhythmPower extends AbstractMizukiPower
{
    public static final String id = MizukiModCore.MakePath(DeadlyRhythmPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(id);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private boolean hasGainedStrength;
    private boolean firstTurn;

    public DeadlyRhythmPower(AbstractCreature owner, int amt)
    {
        super(owner, amt, id, NAME);
        hasGainedStrength = false;
        updateDescription();
        this.type = PowerType.BUFF;
    }


    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        AbstractPower p = this;
        if (!isPlayer)
        {
            if (!hasGainedStrength && owner.hasPower(StrengthPower.POWER_ID))
            {
                addToBot(new AbstractGameAction()
                {
                    @Override
                    public void update()
                    {
                        if (p.owner.getPower(StrengthPower.POWER_ID).amount > 0)
                        {
                            addToBot((AbstractGameAction)new ReducePowerAction(p.owner, p.owner, StrengthPower.POWER_ID,
                                    MathUtils.ceil(owner.getPower(StrengthPower.POWER_ID).amount / 2.0F)));
                        }
                        if (owner.getPower(StrengthPower.POWER_ID).amount == 0)
                        {
                            addToBot((AbstractGameAction)new RemoveSpecificPowerAction(p.owner, p.owner, StrengthPower.POWER_ID));
                        }
                        isDone = true;
                    }
                });
            }
            hasGainedStrength = false;
        }
    }

    public void onInflictDamage(DamageInfo info, int damageAmount, AbstractCreature target)
    {
        if (damageAmount > 0 && info.type != DamageInfo.DamageType.THORNS)
        {
            AbstractDungeon.actionManager.addToBottom(
                    (AbstractGameAction)new ApplyPowerAction((AbstractCreature)owner, (AbstractCreature)owner,
                            (AbstractPower)new StrengthPower((AbstractCreature)owner, amount), amount));
        }
    }

    public String getDescription()
    {
        return DESCRIPTIONS[0];
    }


    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
    {
        if (power.ID.equals(StrengthPower.POWER_ID) && source == owner)
        {
            hasGainedStrength = true;
        }
    }

}
