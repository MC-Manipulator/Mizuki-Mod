package rewards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.ArrayList;

public class SingleCard extends AbstractMizukiReward
{
    private static final String path = "resources/img/reward/card.png";
    private static final String description = rewardString.TEXT[0];
    private AbstractCard rewardCard = null;
    public AbstractCard.CardType rewardCardType = null;

    public SingleCard(AbstractCard.CardType type)
    {
        super(path, description, Enums.Mizuki_SingleCard);
        this.rewardCardType = type;
        this.text = rewardString.TEXT[7];
        if (type == AbstractCard.CardType.ATTACK)
        {
            this.text = this.text + rewardString.TEXT[8];
        }
        else if (type == AbstractCard.CardType.SKILL)
        {
            this.text = this.text + rewardString.TEXT[9];
        }
        else if (type == AbstractCard.CardType.POWER)
        {
            this.text = this.text + rewardString.TEXT[10];
        }
        this.text = this.text  + rewardString.TEXT[11];
    }

    @Override
    public boolean claimReward()
    {
        if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.COMBAT_REWARD)
        {
            if (cards.size() < 1)
            {
                do
                {
                    rewardCard = AbstractDungeon.returnRandomCard();
                }
                while (rewardCard.type != rewardCardType);
                cards.add(rewardCard);
            }

            AbstractDungeon.cardRewardScreen.open(this.cards, this, rewardString.TEXT[1]);
            AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.COMBAT_REWARD;
        }
        return false;
    }
}
