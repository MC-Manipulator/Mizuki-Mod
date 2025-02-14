package powers;

import characters.Mizuki;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.BufferPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import modcore.MizukiModCore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PainAdaptationPower extends AbstractMizukiPower
{
    public static final String id = MizukiModCore.MakePath(PainAdaptationPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(id);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public PainAdaptationPower(AbstractCreature owner, int amt)
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
    public int onLoseHp(int damageAmount)
    {
        if (damageAmount > 0)
        {
            flash();
            addToTop((AbstractGameAction)new ApplyPowerAction(owner, owner, (AbstractPower)new PainPower(owner, 1), 1));
        }
        return damageAmount;
    }
/*
    public int onAttacked(DamageInfo info, int damageAmount)
    {
        if (info.type != DamageInfo.DamageType.THORNS && info.type != DamageInfo.DamageType.HP_LOSS && info.owner != null && info.owner != this.owner && damageAmount > 0)
        {
            flash();
            addToTop((AbstractGameAction)new ApplyPowerAction(owner, owner, (AbstractPower)new PainPower(owner, 1), 1));
        }
        return damageAmount;
    }*/

}
