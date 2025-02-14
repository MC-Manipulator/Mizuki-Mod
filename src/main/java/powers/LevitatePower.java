package powers;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import modcore.MizukiModCore;

public class LevitatePower extends AbstractMizukiPower
{
    public static final String id = MizukiModCore.MakePath(LevitatePower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(id);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private float originalX;
    private float originalY;
    private boolean justApplied;
    private float time;
    private float time2;
    private boolean hasTips = false;

    public LevitatePower(AbstractCreature owner, int amt)
    {
        super(owner, amt, id, NAME);
        updateDescription();
        this.type = PowerType.DEBUFF;
        this.isTurnBased = true;
        this.originalX = owner.drawX;
        this.originalY = owner.drawY;
        this.justApplied = true;
        this.hasTips = false;
    }

    @Override
    public void update(int slot)
    {
        super.update(slot);

        time2 += Gdx.graphics.getDeltaTime();
        if (time2 > 1F)
        {
            time2 = 0F;
            this.owner.useShakeAnimation(1);
        }
        //起飞
        if (this.justApplied)
        {
            this.time += Gdx.graphics.getDeltaTime();
            this.owner.drawY += Gdx.graphics.getDeltaTime() * 500.0F * Settings.scale;
            if (this.time >= 0.2F)
            {
                this.justApplied = false;
            }
        }
    }

    @Override
    public void onRemove()
    {
        AbstractCreature o = this.owner;
        //还原位置
        addToBot(new AbstractGameAction()
        {
            public boolean justStart = true;
            @Override
            public void update()
            {
                if (justStart)
                {
                    this.duration = 0.2F;
                    this.justStart = false;
                }
                this.duration -= Gdx.graphics.getDeltaTime();
                o.drawY -= Gdx.graphics.getDeltaTime() * 500.0F * Settings.scale;

                if (this.duration <= 0F)
                {
                    o.drawX = originalX;
                    o.drawY = originalY;
                    isDone = true;
                }
            }
        });
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action)
    {
        super.onUseCard(card, action);
        if (card.baseBlock > 0)
        {
            if (!hasTips)
            {
                hasTips = true;
                addToBot((AbstractGameAction)new TalkAction(true, DESCRIPTIONS[1], 2.0F, 2.0F));
            }
        }
    }

    @Override
    public float modifyBlockLast(float blockAmount)
    {
        return 0F;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        flash();
        if (this.amount == 0)
        {
            addToBot((AbstractGameAction)new RemoveSpecificPowerAction(this.owner, this.owner, id));
        }
        else
        {
            addToBot((AbstractGameAction)new ReducePowerAction(this.owner, this.owner, id, 1));
        }
    }

    public String getDescription()
    {
        return DESCRIPTIONS[0];
    }
}
