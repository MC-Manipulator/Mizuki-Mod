package relics;

import Impairment.NervousImpairment;
import actions.ApplyImpairmentAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import modcore.MizukiModCore;

public class BlankCoral extends AbstractMizukiRelic
{
    //无字珊瑚
    public static final String ID = MizukiModCore.MakePath(BlankCoral.class.getSimpleName());

    public BlankCoral()
    {
        super(ID, RelicTier.UNCOMMON, LandingSound.FLAT);
    }

    public String getUpdatedDescription()
    {
        return this.DESCRIPTIONS[0];
    }
}
