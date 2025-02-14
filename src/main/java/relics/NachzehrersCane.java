package relics;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import modcore.MizukiModCore;

public class NachzehrersCane extends AbstractMizukiRelic
{
    //食腐者手杖
    public static final String ID = MizukiModCore.MakePath(NachzehrersCane.class.getSimpleName());
    private boolean inBattle = false;

    public NachzehrersCane()
    {
        super(ID, RelicTier.RARE, LandingSound.MAGICAL);
        inBattle = false;
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();
        inBattle = true;
    }

    @Override
    public int onPlayerHeal(int healAmount)
    {
        if (inBattle)
        {
            addToBot((AbstractGameAction) new RelicAboveCreatureAction((AbstractCreature) AbstractDungeon.player, this));
            addToBot((AbstractGameAction)
                    new DamageAllEnemiesAction(null, DamageInfo.createDamageMatrix(healAmount, true),
                            DamageInfo.DamageType.THORNS,
                            AbstractGameAction.AttackEffect.POISON));
        }
        return super.onPlayerHeal(healAmount);
    }

    @Override
    public void onVictory()
    {
        super.onVictory();
        inBattle = false;
    }
}
