package relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.BarricadePower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import modcore.MizukiModCore;

public class KingsFellowship extends AbstractMizukiRelic
{
    //国王的护戒
    public static final String ID = MizukiModCore.MakePath(KingsFellowship.class.getSimpleName());

    private final int healthAmount = 10;

    public KingsFellowship()
    {
        super(ID, AbstractRelic.RelicTier.SPECIAL, LandingSound.SOLID);
    }

    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0] + healthAmount + DESCRIPTIONS[1];
    }

    @Override
    public void onEquip()
    {
        this.counter = AbstractDungeon.player.maxHealth - healthAmount;
        AbstractDungeon.player.decreaseMaxHealth(AbstractDungeon.player.maxHealth - healthAmount);
    }

    @Override
    public void atBattleStart()
    {
        AbstractPlayer p = AbstractDungeon.player;
        addToBot(new GainBlockAction(p, this.counter));
        addToBot(new ApplyPowerAction(p, p, new BarricadePower(p)));
    }
}
