package actions;

import cards.AbstractMizukiCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import helper.EventHelper;

import java.util.UUID;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.returnTrulyRandomCardInCombat;

;

public class LearnAction extends AbstractGameAction
{
    private static final float DURATION = 0.1F;

    private UUID uuid;

    private int increaseCount;

    public LearnAction(UUID uuid, int increaseCount)
    {
        this.uuid = uuid;
        this.increaseCount = increaseCount;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
    }

    public void update()
    {
        if (this.duration == this.startDuration)
        {
            for (AbstractCard c : AbstractDungeon.player.masterDeck.group)
            {
                if (!c.uuid.equals(this.uuid))
                    continue;
                c.misc += increaseCount;
                c.applyPowers();
            }
            for (AbstractCard c : GetAllInBattleInstances.get(this.uuid))
            {
                c.misc += increaseCount;
                c.applyPowers();
            }
            EventHelper.ON_LEARN_SUBSCRIBERS.forEach(sub -> sub.OnLearn(uuid));
        }
        tickDuration();
    }
}
