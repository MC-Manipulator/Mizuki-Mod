package relics;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import modcore.MizukiModCore;

public class DuckLordsGoldenBrick extends AbstractMizukiRelic
{
    //鸭爵金砖
    public static final String ID = MizukiModCore.MakePath(DuckLordsGoldenBrick.class.getSimpleName());

    public DuckLordsGoldenBrick()
    {
        super(ID, AbstractRelic.RelicTier.BOSS, AbstractRelic.LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onEquip()
    {
        super.onEquip();
        addToBot(new AbstractGameAction()
        {
            @Override
            public void update()
            {
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, new MizukisD8());
                isDone = true;
            }
        });
    }

    @Override
    public boolean canSpawn()
    {
        return AbstractDungeon.player.hasRelic(MizukisDice.ID);
    }
}
