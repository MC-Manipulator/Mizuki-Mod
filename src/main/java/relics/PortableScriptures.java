package relics;

import actions.GainDiceAction;
import helper.DiceHelper;
import modcore.MizukiModCore;

public class PortableScriptures extends AbstractMizukiRelic
{
    //便携经书

    private static final int diceAmount = 2;

    public static final String ID = MizukiModCore.MakePath(PortableScriptures.class.getSimpleName());

    public PortableScriptures()
    {
        super(ID, RelicTier.SPECIAL, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0] + diceAmount + DESCRIPTIONS[1];
    }

    @Override
    public void onEquip()
    {
        DiceHelper.gainDice(diceAmount);
    }
}