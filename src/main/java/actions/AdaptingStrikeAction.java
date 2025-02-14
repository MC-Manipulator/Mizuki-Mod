package actions;

import javax.swing.*;;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import helper.CookingHelper;
import rewards.IngredientReward;
import rewards.SingleCard;

import java.util.ArrayList;
import java.util.Iterator;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.returnTrulyRandomCardInCombat;

public class AdaptingStrikeAction extends AbstractGameAction
{
    private DamageInfo info;
    private int cardAmount;

    private static final float DURATION = 0.1F;

    public AdaptingStrikeAction(AbstractCreature target, DamageInfo info, int cardAmount)
    {
        this.info = info;
        setValues(target, info);
        this.cardAmount = cardAmount;
        this.actionType = ActionType.DAMAGE;
        this.duration = 0.1F;
    }

    public void update()
    {
        if (this.duration == 0.1F &&
                this.target != null) {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AttackEffect.NONE));
            this.target.damage(this.info);
            if ((((AbstractMonster)this.target).isDying || this.target.currentHealth <= 0) && !this.target.halfDead &&
                    !this.target.hasPower("Minion"))
            {
                CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

                for (int i = 0;i < this.cardAmount;i++)
                {

                    AbstractDungeon.getCurrRoom().rewards.add(new SingleCard(AbstractCard.CardType.ATTACK));
                    /*
                    AbstractCard card = returnTrulyRandomCardInCombat(AbstractCard.CardType.ATTACK);
                    UnlockTracker.markCardAsSeen(card.cardID);
                    card.isSeen = true;
                    for (AbstractRelic r : AbstractDungeon.player.relics)
                        r.onPreviewObtainCard(card);
                    group.addToBottom(card);*/
                }
                //AbstractDungeon.gridSelectScreen.openConfirmationGrid(group, "大群的选择...");
            }
            if ((AbstractDungeon.getCurrRoom()).monsters.areMonstersBasicallyDead())
                AbstractDungeon.actionManager.clearPostCombatActions();
        }
        isDone = true;
        tickDuration();
    }
}
