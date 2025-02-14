package relics;

import Impairment.NervousImpairment;
import actions.ApplyImpairmentAction;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import modcore.MizukiModCore;

public class PathfinderFins extends AbstractMizukiRelic
{
    //指路鳞
    public static final String ID = MizukiModCore.MakePath(PathfinderFins.class.getSimpleName());
    private static final int impairmentAmount = 1;

    public PathfinderFins()
    {
        super(ID, RelicTier.COMMON, LandingSound.FLAT);
    }

    public String getUpdatedDescription()
    {
        return this.DESCRIPTIONS[0] + impairmentAmount + this.DESCRIPTIONS[1];
    }

    public void atBattleStart()
    {
        flash();
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
        {
            addToBot((AbstractGameAction)new RelicAboveCreatureAction((AbstractCreature)m, this));
            addToTop(new ApplyImpairmentAction(new NervousImpairment(), m, impairmentAmount));
        }
    }
}