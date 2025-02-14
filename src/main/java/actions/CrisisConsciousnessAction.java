package actions;

import characters.Mizuki;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import modcore.MizukiModCore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;

public class CrisisConsciousnessAction extends AbstractGameAction
{
    private static final Logger logger = LogManager.getLogger(Mizuki.class);
    AbstractMonster monster;
    private static final float DURATION = 0.1F;

    public CrisisConsciousnessAction(AbstractMonster monster)
    {
        this.monster = monster;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.BLOCK;
    }

    public void update()
    {
        if (this.duration == this.startDuration)
        {
            try
            {

                Class<?> C = AbstractMonster.class;
                Field nameField = C.getDeclaredField("move");
                nameField.setAccessible(true);
                EnemyMoveInfo eMI = (EnemyMoveInfo) nameField.get(monster);
                //MizukiModCore.logger.info(monster.getIntentBaseDmg());
                int multiplier = 1;
                if (eMI.isMultiDamage)
                {
                    multiplier = eMI.multiplier;
                }
                if (Settings.FAST_MODE)
                {
                    //MizukiModCore.logger.info((monster.getIntentBaseDmg() * multiplier) / 2);
                    //MizukiModCore.logger.info((monster.getIntentDmg() * multiplier) / 2);
                    addToBot((AbstractGameAction)new GainBlockAction((AbstractCreature) AbstractDungeon.player, (AbstractCreature)AbstractDungeon.player, (monster.getIntentDmg() * multiplier) / 2, true));
                }
                else
                {
                    addToBot((AbstractGameAction)new GainBlockAction((AbstractCreature)AbstractDungeon.player, (AbstractCreature)AbstractDungeon.player, (monster.getIntentDmg() * multiplier) / 2));
                }
            }
            catch (Exception e)
            {
                MizukiModCore.logger.info(e);
            }

        }
        isDone = true;
        tickDuration();
    }
}
