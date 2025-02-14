package powers;

import Impairment.NervousImpairment;
import actions.ApplyImpairmentAction;
import actions.TentacleAttackAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import modcore.MizukiModCore;

public class ArousalPower extends AbstractMizukiPower
{
    public static final String id = MizukiModCore.MakePath(ArousalPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(id);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public ArousalPower(AbstractCreature owner, int amt)
    {
        super(owner, amt, id, NAME);
        updateDescription();
        this.type = PowerType.BUFF;
        this.isTurnBased = false;
    }

    public String getDescription()
    {
        return DESCRIPTIONS[0];
    }

    public void onUseCard(AbstractCard card, UseCardAction action)
    {
        if (card.type == AbstractCard.CardType.ATTACK)
        {
            flash();
            AbstractMonster target = null;
            for (AbstractMonster m2 : (AbstractDungeon.getCurrRoom()).monsters.monsters)
            {
                if (target == null && !m2.isDead)
                {
                    target = m2;
                }

                if (!m2.isDeadOrEscaped() && m2.currentHealth < target.currentHealth)
                {
                    target = m2;
                }
            }
            addToBot((AbstractGameAction) new TentacleAttackAction(target));
            addToBot((AbstractGameAction) new ApplyImpairmentAction(new NervousImpairment(), target, this.amount));
            addToBot((AbstractGameAction) new RemoveSpecificPowerAction(this.owner, this.owner, id));
        }
    }
}
