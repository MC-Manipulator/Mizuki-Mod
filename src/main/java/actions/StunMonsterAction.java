package actions;

import powers.StunMonsterPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class StunMonsterAction extends AbstractGameAction
{
    public StunMonsterAction(AbstractMonster target, AbstractCreature source)
    {
        this(target, source, 1);
    }

    public StunMonsterAction(AbstractMonster target, AbstractCreature source, int amount)
    {
        this.target = (AbstractCreature)target;
        this.source = source;
        this.amount = amount;
        this.actionType = ActionType.DEBUFF;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    public void update()
    {
        if (this.duration == Settings.ACTION_DUR_FAST)
            AbstractDungeon.actionManager.addToTop((AbstractGameAction)new ApplyPowerAction(this.target, this.source, (AbstractPower)new StunMonsterPower((AbstractMonster)this.target, this.amount), this.amount));
        tickDuration();
    }
}
