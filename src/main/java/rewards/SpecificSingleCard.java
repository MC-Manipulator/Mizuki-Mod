package rewards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class SpecificSingleCard extends AbstractMizukiReward
{
    private static final String path = "resources/img/reward/card.png";
    private static final String description = rewardString.TEXT[0];
    private AbstractCard rewardCard = null;

    public SpecificSingleCard(AbstractCard rewardCard)
    {
        super(path, description, Enums.Mizuki_SpecificSingleCard);
        this.rewardCard = rewardCard;
        this.text = rewardString.TEXT[7];
        this.text = this.text  + rewardCard.name;
    }

    @Override
    public boolean claimReward()
    {
        if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.COMBAT_REWARD)
        {
            cards.add(rewardCard);
            AbstractDungeon.cardRewardScreen.open(this.cards, this, rewardString.TEXT[1]);
            AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.COMBAT_REWARD;
        }
        return false;
    }
}
