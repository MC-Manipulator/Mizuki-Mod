package powers;

import cards.Skills.AbsurdFate;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import helper.EventHelper;
import modcore.MizukiModCore;

public class MultipleCardsPlayPower extends AbstractMizukiPower
{
    public static final String id = MizukiModCore.MakePath(MultipleCardsPlayPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(id);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    int cardsamount = 0;

    public MultipleCardsPlayPower(AbstractCreature owner, int amt)
    {
        //在调用的时候，请按照将会释放几次使用
        super(owner, amt - 1, id, NAME);
        this.cardsamount = cardsamount;
        updateDescription();
        this.type = PowerType.BUFF;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action)
    {
        if (!card.purgeOnUse && this.amount > 0)
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
