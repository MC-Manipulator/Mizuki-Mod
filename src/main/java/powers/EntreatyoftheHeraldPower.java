package powers;

import basemod.abstracts.AbstractCardModifier;
import basemod.cardmods.EtherealMod;
import basemod.cardmods.ExhaustMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.AddCardToDeckAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import helper.RejectionHelper;
import modcore.MizukiModCore;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import powers.CrazyProliferationPower;
import com.megacrit.cardcrawl.actions.unique.LimitBreakAction;

public class EntreatyoftheHeraldPower extends AbstractMizukiPower
{
    public static final String id = MizukiModCore.MakePath(EntreatyoftheHeraldPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(id);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final float HEAL_MULTIPLIER = 1.5F;
    public int drawcount;
    public int recovercount;
    public EntreatyoftheHeraldPower(AbstractCreature owner, int amt, int drawcount, int recovercount)
    {
        super(owner, amt, id, NAME);
        updateDescription();
        this.type = PowerType.BUFF;
        this.drawcount = drawcount;
        this.recovercount = recovercount;
    }

    public String getDescription()
    {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atStartOfTurnPostDraw()
    {
        addToBot(new AbstractGameAction()
        {
            @Override
            public void update()
            {
                for (AbstractCard c : AbstractDungeon.player.hand.group)
                {
                    CardModifierManager.addModifier(c, (AbstractCardModifier)new EtherealMod());
                }
                isDone = true;
            }
        });
    }

    @Override
    public void onExhaust(AbstractCard card)
    {
        if (card.type == AbstractCard.CardType.CURSE)
        {
            addToBot((AbstractGameAction) new DrawCardAction(drawcount * amount));
            addToBot((AbstractGameAction) new HealAction(AbstractDungeon.player, AbstractDungeon.player, recovercount * amount));
        }
    }

    @Override
    public void atEndOfTurnPreEndTurnCards(boolean isPlayer)
    {
        if (isPlayer)
        {
            AbstractCard c = RejectionHelper.getRandomRejection();
            //addToBot((AbstractGameAction) new AddCardToDeckAction(c));
            addToBot((AbstractGameAction)new MakeTempCardInDrawPileAction(c, amount, true, true));
        }
    }




    @Override
    public void atEndOfTurn(boolean isPlayer)
    {

    }

    public void updateDescription()
    {
        this.description = powerStrings.DESCRIPTIONS[0] + recovercount * amount + powerStrings.DESCRIPTIONS[1] + drawcount * amount + powerStrings.DESCRIPTIONS[2];
    }

    /*
    public void atEndOfRound()
    {

        if (this.amount != 0 && owner.currentHealth >= owner.maxHealth / 2 )
        {
            turns++;
            addToBot((AbstractGameAction)new ReducePowerAction(this.owner, this.owner, id, 1));
        }
        if (this.amount != 0 && owner.currentHealth < owner.maxHealth / 2 )
        {
            flash();
            if ((this.amount - (3 - turns)) == 0)
            {
                addToBot((AbstractGameAction)new RemoveSpecificPowerAction(this.owner, this.owner, id));
            }
            else
            {
                addToBot((AbstractGameAction)new ReducePowerAction(this.owner, this.owner, id, 3 - turns));
            }
            addToBot((AbstractGameAction)new ApplyPowerAction(owner, owner, (AbstractPower)new CrazyProliferationPower(owner, 1), 1));
            turns = 0;
        }
        if (this.amount == 0 && turns == 3)
        {
            flash();
            turns = 0;
            addToBot((AbstractGameAction)new RemoveSpecificPowerAction(this.owner, this.owner, id));
            addToBot((AbstractGameAction)new ApplyPowerAction(owner, owner, (AbstractPower)new StrengthPower(owner, 7), 7));
            addToBot((AbstractGameAction)new LimitBreakAction());
        }
        if (this.amount != 0 && turns == 3)
        {
            flash();
            turns = 0;
            addToBot((AbstractGameAction)new ApplyPowerAction(owner, owner, (AbstractPower)new StrengthPower(owner, 7), 7));
            addToBot((AbstractGameAction)new LimitBreakAction());
        }
    }*/
}
