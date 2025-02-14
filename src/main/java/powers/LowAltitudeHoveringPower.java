package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import modcore.MizukiModCore;
import monsters.boss.beyond.ParanoiaIllusion;

import java.util.ArrayList;

public class LowAltitudeHoveringPower extends AbstractMizukiPower
{
    public static final String id = MizukiModCore.MakePath(LowAltitudeHoveringPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(id);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    ArrayList<AbstractPower> currPower;

    private boolean shouldTriggerAni = true;

    public LowAltitudeHoveringPower(AbstractCreature owner, int amt)
    {
        super(owner, amt, id, NAME);
        currPower = new ArrayList<>();
        if (owner.powers != null && !owner.powers.isEmpty())
            currPower.addAll(owner.powers);
        updateDescription();
        this.type = PowerType.BUFF;
    }

    public LowAltitudeHoveringPower(AbstractCreature owner, int amt, boolean shouldTriggerAni)
    {
        this(owner, amt);
        this.shouldTriggerAni = shouldTriggerAni;
    }

    public String getDescription()
    {
        return DESCRIPTIONS[0];
    }

    @Override
    public void update(int slot)
    {
        super.update(slot);
        for (AbstractPower p : owner.powers)
        {
            if (p.type == PowerType.DEBUFF)
            {
                addToBot((AbstractGameAction) new RemoveSpecificPowerAction(this.owner, this.owner, id));
            }
        }
        /*
        if (currPower.size() > owner.powers.size())
        {
            if (!owner.powers.isEmpty())
            {
                currPower.clear();
                currPower.addAll(owner.powers);
            }
            else
            {
                currPower.clear();
            }
        }
        if (currPower.size() <= owner.powers.size())
        {
            for (AbstractPower p : owner.powers)
            {
                boolean hasExist = false;
                for (AbstractPower p2 : currPower)
                {
                    if (p.ID.equals(p2.ID))
                    {
                        if (p.amount > p2.amount)
                        {
                            break;
                        }
                        hasExist = true;
                        break;
                    }
                }
                if (!hasExist && p.type == PowerType.DEBUFF)
                {
                    addToBot((AbstractGameAction) new RemoveSpecificPowerAction(this.owner, this.owner, id));
                }
            }
            if (!owner.powers.isEmpty())
            {
                currPower.clear();
                currPower.addAll(owner.powers);
            }
        }*/
    }

    public float atDamageFinalReceive(float damage, DamageInfo.DamageType type)
    {
        return calculateDamageTakenAmount(damage, type);
    }

    private float calculateDamageTakenAmount(float damage, DamageInfo.DamageType type)
    {
        if (type != DamageInfo.DamageType.HP_LOSS && type != DamageInfo.DamageType.THORNS)
            return damage / 2.0F;
        return damage;
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
    {
        super.onApplyPower(power, target, source);

    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();
        if (owner instanceof ParanoiaIllusion && shouldTriggerAni)
        {
            addToBot((AbstractGameAction)new ChangeStateAction((AbstractMonster) this.owner, "REVIVE"));
        }
    }

    public void onRemove()
    {
        if (!this.owner.isDeadOrEscaped())
        {
            addToBot((AbstractGameAction)new ChangeStateAction((AbstractMonster) this.owner, "GROUNDED"));
        }
    }
}
