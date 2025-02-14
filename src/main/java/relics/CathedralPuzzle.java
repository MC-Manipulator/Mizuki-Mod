package relics;

import com.megacrit.cardcrawl.relics.AbstractRelic;
import modcore.MizukiModCore;

public class CathedralPuzzle extends AbstractMizukiRelic
{
    //大教堂拼图

    public static final String ID = MizukiModCore.MakePath(CathedralPuzzle.class.getSimpleName());
    public CathedralPuzzle()
    {
        super(ID, AbstractRelic.RelicTier.SPECIAL, AbstractRelic.LandingSound.FLAT);
    }
}
