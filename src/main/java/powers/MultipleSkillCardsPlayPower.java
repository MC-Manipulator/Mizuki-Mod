package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import modcore.MizukiModCore;

public class MultipleSkillCardsPlayPower extends AbstractMizukiPower
{
    public static final String id = MizukiModCore.MakePath(MultipleSkillCardsPlayPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(id);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public MultipleSkillCardsPlayPower(AbstractCreature owner, int amt)
    {
        super(owner, amt - 1, id, NAME);
        updateDescription();
        this.type = PowerType.BUFF;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action)
    {
        if (!card.purgeOnUse && card.type == AbstractCard.CardType.SKILL && this.amount > 0)
        {
            flash();
            AbstractMonster m = null;
            if (action.target != null)
                m = (AbstractMonster)action.target;
            for (int i = 0;i < this.amount;i++)
            {
                AbstractCard tmp = card.makeSameInstanceOf();
                AbstractDungeon.player.limbo.addToBottom(tmp);
                tmp.current_x = card.current_x;
                tmp.current_y = card.current_y;
                tmp.target_x = Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
                tmp.target_y = Settings.HEIGHT / 2.0F;
                if (m != null)
                    tmp.calculateCardDamage(m);
                tmp.purgeOnUse = true;
                AbstractDungeon.actionManager.addCardQueueItem(
                        new CardQueueItem(tmp, m, card.energyOnUse, true, true), true);
            }
            addToTop((AbstractGameAction)new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
    }

    public String getDescription()
    {
        return DESCRIPTIONS[0];
    }
}
