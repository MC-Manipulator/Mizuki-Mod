package actions;

import cards.AbstractMizukiCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import modcore.MizukiModCore;

import java.util.UUID;

;

public class PowerStoreAction extends AbstractGameAction
{
    private static final float DURATION = 0.1F;

    private UUID uuid;
    private Class<?> powerClass;


    public PowerStoreAction(UUID uuid, Class<?> powerClass)
    {
        this.uuid = uuid;
        this.powerClass = powerClass;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
    }

    public void update()
    {
        if (this.duration == this.startDuration)
        {
            for (AbstractCard card : AbstractDungeon.player.masterDeck.group)
            {
                AbstractMizukiCard mc = (AbstractMizukiCard)card;
                if (!mc.uuid.equals(this.uuid))
                    continue;
                try
                {
                    mc.storePower.add(Class.forName(powerClass.getName()));

                }
                catch (Exception e)
                {
                    MizukiModCore.logger.info(e);
                }
                mc.applyPowers();
            }
        }
        tickDuration();
    }
}
