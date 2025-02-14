package powers;

import Impairment.CorrosionImpairment;
import Impairment.NervousImpairment;
import actions.ApplyImpairmentAction;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import modcore.MizukiModCore;
import monsters.AbstractMizukiMonster;

public class DreamInterruptedPower extends AbstractMizukiPower
{
    public static final String id = MizukiModCore.MakePath(DreamInterruptedPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(id);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private boolean isOriginExist;
    private boolean firstTurn;

    public DreamInterruptedPower(AbstractCreature owner, int amt)
    {
        super(owner, amt, id, NAME);
        isOriginExist = false;
        firstTurn = true;
        updateDescription();
        this.type = PowerType.BUFF;
    }
    public DreamInterruptedPower(AbstractCreature owner, int amt, boolean isOriginExist)
    {
        this(owner, amt);
        this.isOriginExist = isOriginExist;
    }

    public String getDescription()
    {
        return DESCRIPTIONS[0] + DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        if (!isPlayer && !isOriginExist && !firstTurn)
        {
            addToBot(new LoseHPAction(owner, owner, MathUtils.floor(owner.maxHealth * 0.15F), AbstractGameAction.AttackEffect.POISON));
            addToBot(new ApplyImpairmentAction(new NervousImpairment(), AbstractDungeon.player, this.amount));
            return;
        }
        if (!isPlayer && isOriginExist)
        {
            addToBot(new LoseHPAction(owner, owner, MathUtils.floor(owner.maxHealth * 0.15F), AbstractGameAction.AttackEffect.POISON));
            addToBot(new ApplyImpairmentAction(new NervousImpairment(), AbstractDungeon.player, this.amount));
            return;
        }
        firstTurn = false;
    }
}
