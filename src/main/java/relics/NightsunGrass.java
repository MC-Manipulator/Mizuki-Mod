package relics;

import modcore.MizukiModCore;

public class NightsunGrass extends AbstractMizukiRelic
{
    //夜阳草

    public boolean hasTrigger = false;

    public static final String ID = MizukiModCore.MakePath(NightsunGrass.class.getSimpleName());
    public NightsunGrass()
    {
        super(ID, RelicTier.SPECIAL, LandingSound.FLAT);
        this.hasTrigger = false;
    }

    @Override
    public void atBattleStart()
    {
        this.hasTrigger = false;
        beginLongPulse();
    }
}