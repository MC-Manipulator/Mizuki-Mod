package powers;

import Impairment.NervousImpairment;
import actions.ApplyImpairmentAction;
import actions.TentacleAttackAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import modcore.MizukiModCore;

public class ArmourPenetrationPower extends AbstractMizukiPower
{
    public static final String id = MizukiModCore.MakePath(ArmourPenetrationPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(id);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public ArmourPenetrationPower(AbstractCreature owner, int amt)
    {
        super(owner, amt, id, NAME);
        updateDescription();
        updateDescription();
        this.type = PowerType.DEBUFF;
        this.isTurnBased = false;
    }

    public String getDescription()
    {
        return DESCRIPTIONS[0];
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType damageType)
    {
        if (damageType == DamageInfo.DamageType.NORMAL)
        {
            return damage + this.amount;
        }
        return damage;
    }
}
