package powers;

import actions.EnvironmentAnalyzingAction;
import actions.FreeToUseAction;
import actions.ResetCostAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import helper.EventHelper;
import modcore.MizukiModCore;

public class FreeToUsePower extends AbstractMizukiPower implements EventHelper.OnGetCardInHandSubscriber
{
    public static final String id = MizukiModCore.MakePath(FreeToUsePower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(id);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;


    public FreeToUsePower(AbstractCreature owner, int amt)
    {
        super(owner, amt, id, NAME);
        updateDescription();
        this.type = PowerType.BUFF;
    }

    public String getDescription()
    {
        return DESCRIPTIONS[0];
    }
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
    {
        flash();
        addToBot((AbstractGameAction) new FreeToUseAction(false));
    }

    public void onCardDraw(AbstractCard card)
    {
        //addToBot((AbstractGameAction) new FreeToUseAction(false));
    }

    @Override
    public void OnGetCardInHand(AbstractCard card)
    {
        addToBot((AbstractGameAction) new FreeToUseAction(false));
        MizukiModCore.logger.info(card.cardID);
    }

    public void onUseCard(AbstractCard card, UseCardAction action)
    {
        if (!card.purgeOnUse && this.amount > 0)
        {
            flash();
            this.amount--;
            if (this.amount == 0)
            {
                addToTop((AbstractGameAction) new RemoveSpecificPowerAction(this.owner, this.owner, id));
                addToBot((AbstractGameAction) new ResetCostAction(false));
            }
        }
    }
}
