package powers;

import Impairment.AbstractImpairment;
import Impairment.NervousImpairment;
import actions.ApplyImpairmentAction;
import cards.AbstractMizukiCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import modcore.MizukiModCore;

public class EchoOfPredationPower extends AbstractMizukiPower
{
    public static final String id = MizukiModCore.MakePath(EchoOfPredationPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(id);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;


    public EchoOfPredationPower(AbstractCreature owner, int amt)
    {
        super(owner, amt, id, NAME);
        updateDescription();
        this.type = PowerType.BUFF;
    }

    public String getDescription()
    {
        return DESCRIPTIONS[0];
    }

    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target)
    {
        if (damageAmount > 0 && target != this.owner && info.type == DamageInfo.DamageType.NORMAL)
        {
            flash();
            addToBot((AbstractGameAction) new ApplyImpairmentAction(new NervousImpairment(), (AbstractMonster) target, amount, AbstractImpairment.ImpairmentType.Nervous));
        }
    }
    public void atEndOfTurn(boolean isPlayer)
    {
        addToBot((AbstractGameAction)new RemoveSpecificPowerAction(owner, owner, id));
    }
}
