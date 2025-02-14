package relics;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import helper.DiceHelper;
import modcore.MizukiModCore;

public class CastlesOffspring extends AbstractMizukiRelic
{
    //古堡的子嗣
    public static final String ID = MizukiModCore.MakePath(CastlesOffspring.class.getSimpleName());
    private static final int dexterityAmount = 6;
    private static final int turns = 4;

    public CastlesOffspring()
    {
        super(ID, RelicTier.UNCOMMON, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription()
    {
        //new DexterityPower()
        return DESCRIPTIONS[0] + turns + DESCRIPTIONS[1] + dexterityAmount + DESCRIPTIONS[2];
    }

    public void atBattleStart()
    {
        this.counter = 0;
    }

    public void atTurnStart()
    {
        if (!this.grayscale)
            this.counter++;
        if (this.counter > 4)
        {
            flash();
            addToBot((AbstractGameAction) new RelicAboveCreatureAction((AbstractCreature) AbstractDungeon.player, this));
            addToBot((AbstractGameAction) new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                    new DexterityPower(AbstractDungeon.player, dexterityAmount),
                    dexterityAmount));
            this.counter = -1;
            this.grayscale = true;
        }
    }

    public void onVictory()
    {
        this.counter = -1;
        this.grayscale = false;
    }
}
