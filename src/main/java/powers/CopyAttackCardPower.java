package powers;

import basemod.abstracts.AbstractCardModifier;
import basemod.cardmods.ExhaustMod;
import basemod.helpers.CardModifierManager;
import cards.Skills.AbsurdFate;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import helper.EventHelper;
import modcore.MizukiModCore;

public class CopyAttackCardPower extends AbstractMizukiPower
{
    public static final String id = MizukiModCore.MakePath(CopyAttackCardPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(id);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private AbstractCard sourceCard;

    public CopyAttackCardPower(AbstractCard source, AbstractCreature owner, int amt)
    {
        super(owner, amt, id, NAME);
        sourceCard = source;
        updateDescription();
        this.type = PowerType.BUFF;
        EventHelper.Subscribe(this);
    }

    @Override
    public void onAfterCardPlayed(AbstractCard usedCard)
    {
        if (usedCard.type == AbstractCard.CardType.ATTACK)
        {
            flash();
            AbstractCard card = usedCard.makeCopy();

            CardModifierManager.addModifier(card, (AbstractCardModifier)new ExhaustMod());
            AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(card));
            addToBot((AbstractGameAction) new ReducePowerAction(owner, owner, this.ID, 1));
            //addToBot((AbstractGameAction) new MakeTempCardInHandAction(card));
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        addToBot((AbstractGameAction) new RemoveSpecificPowerAction(owner, owner, this));
    }

    public String getDescription()
    {
        return DESCRIPTIONS[0];
    }
}
