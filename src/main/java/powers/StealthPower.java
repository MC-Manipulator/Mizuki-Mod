package powers;

import Impairment.NervousImpairment;
import actions.ApplyImpairmentAction;
import actions.TentacleAttackAction;
import basemod.patches.com.megacrit.cardcrawl.screens.mainMenu.ColorTabBar.ColorTabBarFix;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
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
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import modcore.MizukiModCore;
import monsters.AbstractMizukiMonster;
import vfx.UntargetableAuraEffect;

public class StealthPower extends AbstractMizukiPower
{
    public static final String id = MizukiModCore.MakePath(StealthPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(id);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private boolean justApplied;
    private float timer;
    public StealthPower(AbstractCreature owner, int amt)
    {
        super(owner, amt, id, NAME);
        updateDescription();
        this.type = PowerType.BUFF;
        this.isTurnBased = true;
        this.justApplied = true;
    }

    @Override
    public void update(int slot)
    {
        super.update(slot);
        this.timer -= Gdx.graphics.getDeltaTime();
        if (this.timer < 0.0F)
        {
            this.timer = MathUtils.random(0.01F, 0.05F);
            AbstractDungeon.effectsQueue.add(new UntargetableAuraEffect(this.owner));
        }
    }

    /*
    @Override
    public float atDamageFinalReceive(float damage, DamageInfo.DamageType type)
    {
        return 0;
    }*/

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        if (!isPlayer)
        {
            if (this.justApplied)
            {
                this.justApplied = false;
                return;
            }

            if (this.amount == 1)
            {
                addToTop((AbstractGameAction)new RemoveSpecificPowerAction(this.owner, this.owner, id));
            }
            else
            {
                addToTop((AbstractGameAction)new ReducePowerAction(this.owner, this.owner, id, 1));
            }
        }
    }

    public String getDescription()
    {
        return DESCRIPTIONS[0];
    }
}
