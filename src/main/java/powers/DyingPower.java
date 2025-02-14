package powers;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import modcore.MizukiModCore;

public class DyingPower extends AbstractMizukiPower
{
    public static final String id = MizukiModCore.MakePath(DyingPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(id);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private boolean isOriginExist;
    private boolean firstTurn;

    public DyingPower(AbstractCreature owner, int amt)
    {
        super(owner, amt, id, NAME);
        isOriginExist = false;
        firstTurn = true;
        this.type = PowerType.DEBUFF;
        updateDescription();
    }
    public DyingPower(AbstractCreature owner, int amt, boolean isOriginExist)
    {
        this(owner, amt);
        this.isOriginExist = isOriginExist;
    }

    public String getDescription()
    {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        if (!isPlayer && !isOriginExist && !firstTurn)
        {
            addToBot(new LoseHPAction(owner, owner, this.amount, AbstractGameAction.AttackEffect.POISON));
            return;
        }
        if (!isPlayer && isOriginExist)
        {
            addToBot(new LoseHPAction(owner, owner, this.amount, AbstractGameAction.AttackEffect.POISON));
            return;
        }
        firstTurn = false;
    }
}
