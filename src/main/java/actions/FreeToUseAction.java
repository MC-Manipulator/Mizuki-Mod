package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class FreeToUseAction extends AbstractGameAction
{
    private AbstractPlayer p;

    private boolean forCombat = false;

    public FreeToUseAction(boolean forRestOfCombat)
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
                if (c.costForTurn > 0)
                {
                    c.costForTurn = 0;
                    c.isCostModifiedForTurn = true;
                }
                if (this.forCombat && c.cost > 0)
                {
                    c.cost = 0;
                    c.isCostModified = true;
                }
            }
        isDone = true;
        tickDuration();
    }
}
