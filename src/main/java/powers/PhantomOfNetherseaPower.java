package powers;

import Impairment.NervousImpairment;
import actions.ApplyImpairmentAction;
import actions.TentacleAttackAction;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
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
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.BlockedWordEffect;
import com.megacrit.cardcrawl.vfx.stance.CalmParticleEffect;
import com.megacrit.cardcrawl.vfx.stance.DivinityParticleEffect;
import com.megacrit.cardcrawl.vfx.stance.StanceAuraEffect;
import modcore.MizukiModCore;
import vfx.MissedWordEffect;
import vfx.PhantomOfNetherseaEffect;
import vfx.UntargetableAuraEffect;

import java.util.ArrayList;

public class PhantomOfNetherseaPower extends AbstractMizukiPower
{
    public static final String id = MizukiModCore.MakePath(PhantomOfNetherseaPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(id);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private float timer = 0;
    private float timer2 = 0;
    private boolean activated;
    private boolean trigger;
    ArrayList<AbstractPower> currPower;

    public PhantomOfNetherseaPower(AbstractCreature owner, int amt)
    {
        super(owner, amt, id, NAME);
        updateDescription();
        currPower = new ArrayList<>();
        this.type = PowerType.BUFF;
        this.isTurnBased = false;
        activated = true;
        trigger = false;
    }

    public String getDescription()
    {
        return DESCRIPTIONS[0] + DESCRIPTIONS[1];
    }

    public void update(int slot)
    {
        super.update(slot);
        /*
        this.timer -= Gdx.graphics.getDeltaTime();
        if (this.timer < 0.0F)
        {
            this.timer = MathUtils.random(0.01F, 0.05F);
            AbstractDungeon.effectsQueue.add(new UntargetableAuraEffect(this.owner));
        }*/

        if (!hasDebuff())
        {
            if (!Settings.DISABLE_EFFECTS)
            {
                this.timer -= Gdx.graphics.getDeltaTime();
                if (this.timer < 0.0F) {
                    this.timer = 0.04F;
                    AbstractDungeon.effectsQueue.add(new PhantomOfNetherseaEffect(this.owner.hb.cX, this.owner.hb.cY));
                }
            }
            /*
            this.timer2 -= Gdx.graphics.getDeltaTime();
            if (this.timer2 < 0.0F)
            {
                this.timer2 = MathUtils.random(0.45F, 0.55F);
                AbstractDungeon.effectsQueue.add(new StanceAuraEffect("Calm"));
            }*/
        }
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount)
    {
        if (activated)
        {
            int roll = AbstractDungeon.cardRandomRng.random(99);
            if (roll < 50)
            {
                AbstractDungeon.effectList.add(new MissedWordEffect(this.owner, this.owner.hb.cX, this.owner.hb.cY, "MISS"));
                trigger = false;
                return 0;
            }
        }
        return damageAmount;
    }

    private boolean hasDebuff()
    {
        for (AbstractPower p : owner.powers)
        {
            if (p.type == PowerType.DEBUFF)
            {
                activated = false;
                return true;
            }
        }
        activated = true;
        return false;
    }
}
