package powers;

import Impairment.NervousImpairment;
import actions.ApplyImpairmentAction;
import actions.TentacleAttackAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
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
import modcore.MizukiModCore;
import monsters.AbstractMizukiMonster;

public class ArtsEnhancingPower extends AbstractMizukiPower
{
    public static final String id = MizukiModCore.MakePath(ArtsEnhancingPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(id);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public ArtsEnhancingPower(AbstractCreature owner, int amt)
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
    public int onAttackToChangeDamage(DamageInfo info, int damageAmount) {
        return super.onAttackToChangeDamage(info, damageAmount);
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target)
    {
        super.onAttack(info, damageAmount, target);
        info.type = DamageInfo.DamageType.HP_LOSS;
        if (this.amount == 1)
        {
            addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, ArtsEnhancingPower.id));
        }
        else
        {
            addToBot(new ReducePowerAction(this.owner, this.owner, ArtsEnhancingPower.id, 1));
        }
    }
}
