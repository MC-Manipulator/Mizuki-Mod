package powers;

import cards.AbstractMizukiCard;
import cards.Skills.AbsurdFate;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.unique.AddCardToDeckAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import helper.EventHelper;
import modcore.MizukiModCore;
import rewards.SpecificSingleCard;

public class BreathOfTheTidePower extends AbstractMizukiPower
{
    public static final String id = MizukiModCore.MakePath(BreathOfTheTidePower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(id);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private AbstractCard sourceCard;

    public BreathOfTheTidePower(AbstractCard source, AbstractCreature owner, int amt)
    {
        super(owner, amt, id, NAME);
        sourceCard = source;
        this.type = PowerType.BUFF;
        updateDescription();
        EventHelper.Subscribe(this);
    }

    public void onVictory()
    {
        AbstractPlayer p = AbstractDungeon.player;
        MizukiModCore.logger.info("IF HAS POWER PLATED ARMOR" + AbstractDungeon.player.hasPower("Plated Armor"));
        if (AbstractDungeon.player.hasPower(PlatedArmorPower.POWER_ID))
        {
            for (int i = 0;i < this.amount;i++)
            {
                MizukiModCore.logger.info("ADD CARD ABSURDFATE");
                (AbstractDungeon.getCurrRoom()).rewards.add(new SpecificSingleCard(new AbsurdFate()));
                //AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new AbsurdFate(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                //addToBot((AbstractGameAction)new AddCardToDeckAction(new AbsurdFate()));
                AbstractDungeon.player.masterDeck.removeCard(sourceCard);
            }
            addToBot((AbstractGameAction)new RemoveSpecificPowerAction(owner, owner, this.ID));
        }
    }

    public String getDescription()
    {
        return DESCRIPTIONS[0];
    }
}
