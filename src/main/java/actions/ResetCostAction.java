package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class ResetCostAction extends AbstractGameAction
{
    private AbstractPlayer p;
    private boolean forCombat = false;
    public ResetCostAction(boolean forRestOfCombat)
    {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.p = AbstractDungeon.player;
        this.duration = Settings.ACTION_DUR_FAST;
        this.forCombat = forRestOfCombat;
    }
    public void update()
    {
        if (this.duration == Settings.ACTION_DUR_FAST)
            for (AbstractCard c : this.p.hand.group)
            {
                c.costForTurn = c.makeCopy().cost;
                c.isCostModifiedForTurn = true;
                if (this.forCombat)
                {
                    c.cost = c.makeCopy().cost;
                    c.isCostModified = true;
                }
            }
        isDone = true;
        tickDuration();
    }
}
