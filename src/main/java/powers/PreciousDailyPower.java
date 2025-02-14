package powers;

import cards.Skills.AbsurdFate;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import com.megacrit.cardcrawl.powers.EnergizedPower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import helper.EventHelper;
import modcore.MizukiModCore;

public class PreciousDailyPower extends AbstractMizukiPower
{
    public static final String id = MizukiModCore.MakePath(PreciousDailyPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(id);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private boolean hasTrigger = false;

    public PreciousDailyPower(AbstractCreature owner, int amt)
    {
        super(owner, amt, id, NAME);
        updateDescription();
        this.type = PowerType.BUFF;
    }

    @Override
    public void atStartOfTurn()
    {
        hasTrigger = false;
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m)
    {
        if (card.costForTurn == 0 && !hasTrigger)
        {
            flash();
            addToBot(new DrawCardAction(amount));
            hasTrigger = true;
            /*
            addToBot((AbstractGameAction) new ApplyPowerAction(owner, owner,
                    new EnergizedPower(owner, amount), amount));
            addToBot((AbstractGameAction)new ApplyPowerAction(owner, owner, (AbstractPower)
                    new DrawCardNextTurnPower(owner, amount), amount));*/
        }
    }

    public String getDescription()
    {
        return !hasTrigger ? DESCRIPTIONS[0] : DESCRIPTIONS[0] + DESCRIPTIONS[1];
    }
}
