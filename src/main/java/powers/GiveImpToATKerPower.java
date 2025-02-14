package powers;

import Impairment.AbstractImpairment;
import Impairment.NervousImpairment;
import actions.ApplyImpairmentAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import modcore.MizukiModCore;

public class GiveImpToATKerPower extends AbstractMizukiPower
{
    public static final String id = MizukiModCore.MakePath(GiveImpToATKerPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(id);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public GiveImpToATKerPower(AbstractCreature owner, int amt)
    {
        super(owner, amt, id, NAME);
        updateDescription();
        this.type = PowerType.BUFF;
    }

    public String getDescription()
    {
        return DESCRIPTIONS[0];
    }

    public int onAttacked(DamageInfo info, int damageAmount)
    {
        if (info.type != DamageInfo.DamageType.THORNS && info.type != DamageInfo.DamageType.HP_LOSS && info.owner != null && info.owner != this.owner)
        {
            flash();
            addToBot((AbstractGameAction) new ApplyImpairmentAction(new NervousImpairment(), (AbstractMonster) info.owner, amount, AbstractImpairment.ImpairmentType.Nervous));
            addToTop((AbstractGameAction)new ReducePowerAction(this.owner, this.owner, this.ID, this.amount));
        }
        return damageAmount;
    }
}
