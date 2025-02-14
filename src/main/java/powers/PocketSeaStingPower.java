package powers;

import Impairment.NervousImpairment;
import actions.ApplyImpairmentAction;
import actions.TentacleAttackAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import modcore.MizukiModCore;

public class PocketSeaStingPower extends AbstractMizukiPower
{
    public static final String id = MizukiModCore.MakePath(PocketSeaStingPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(id);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final int maxAmount = 25;
    private static final int impairmentAmount = 2;
    //private int strengthAmount = 0;
    public PocketSeaStingPower(AbstractCreature owner, int amt)
    {
        super(owner, amt, id, NAME);
        updateDescription();
        this.type = PowerType.BUFF;
        this.isTurnBased = false;
        //this.strengthAmount = 0;
    }

    public String getDescription()
    {
        return DESCRIPTIONS[0] + impairmentAmount + DESCRIPTIONS[1];
    }

    @Override
    public void update(int slot)
    {
        super.update(slot);
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount)
    {
        int originalDamage = damageAmount;
        if (damageAmount != 0)
        {
            int strengthAmount = getStrengthAmount();

            while (damageAmount >= this.amount)
            {
                AbstractPower reduceMe = this;
                damageAmount -= this.amount;
                //reduceMe.reducePower(this.amount);
                this.amount = maxAmount;
                if (strengthAmount != 0)
                {
                    addToTop(new DamageAction(AbstractDungeon.player, new DamageInfo(this.owner, strengthAmount), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                }
                addToBot(new ApplyImpairmentAction(new NervousImpairment(), AbstractDungeon.player, impairmentAmount));

                reduceMe.updateDescription();
                AbstractDungeon.onModifyPower();
            }
            if (damageAmount != 0)
            {
                addToTop((AbstractGameAction)new ReducePowerAction(this.owner, this.owner, id, damageAmount));
            }
        }

        return super.onAttacked(info, originalDamage);
    }

    @Override
    public int onLoseHp(int damageAmount)
    {
        return super.onLoseHp(damageAmount);
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();
        this.amount = maxAmount;
    }

    private int getStrengthAmount()
    {
        if (this.owner.hasPower(StrengthPower.POWER_ID))
        {
            return this.owner.getPower(StrengthPower.POWER_ID).amount;
        }
        return 0;
    }
}
