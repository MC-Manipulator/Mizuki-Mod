package relics;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import modcore.MizukiModCore;

public class ViviparousLily extends AbstractMizukiRelic
{
    public static final String ID = MizukiModCore.MakePath(ViviparousLily.class.getSimpleName());
    public ViviparousLily()
    {
        super(ID, AbstractRelic.RelicTier.SPECIAL, AbstractRelic.LandingSound.MAGICAL);
    }

    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.SKILL) {
            this.counter++;
            if (this.counter % 3 == 0) {

                this.counter = 0;

            }
        }
        int roll = MathUtils.random(2);
        if (roll == 3)
        {
            flash();
            addToBot((AbstractGameAction) new RelicAboveCreatureAction((AbstractCreature) AbstractDungeon.player, this));
            addToBot((AbstractGameAction)new GainEnergyAction(1));
        }
        else if (roll == 2)
        {
            flash();
            addToBot((AbstractGameAction) new RelicAboveCreatureAction((AbstractCreature) AbstractDungeon.player, this));
            AbstractDungeon.player.loseEnergy(1);
        }
    }
}