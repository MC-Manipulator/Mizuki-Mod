package powers;

import Impairment.NervousImpairment;
import actions.ApplyImpairmentAction;
import actions.TentacleAttackAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import modcore.MizukiModCore;

public class SymbiosisPower extends AbstractMizukiPower
{
    public static final String id = MizukiModCore.MakePath(SymbiosisPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(id);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public SymbiosisPower(AbstractCreature owner, int amt)
    {
        super(owner, amt, id, NAME);
        updateDescription();
        this.type = PowerType.BUFF;
        this.isTurnBased = false;
    }

    @Override
    public String getDescription()
    {
        if (owner != null)
        {
            return DESCRIPTIONS[0] + owner.name + DESCRIPTIONS[1];
        }
        else
        {
            return DESCRIPTIONS[0] + "null" + DESCRIPTIONS[1];
        }
    }
}
