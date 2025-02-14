package relics;

import helper.DiceHelper;
import modcore.MizukiModCore;

public class DeepSeaSireSculpture extends AbstractMizukiRelic
{
    //深洋主宰刻像
    public static final String ID = MizukiModCore.MakePath(DeepSeaSireSculpture.class.getSimpleName());
    private static final int diceAmount = 6;

    public DeepSeaSireSculpture()
    {
        super(ID, RelicTier.COMMON, LandingSound.HEAVY);
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