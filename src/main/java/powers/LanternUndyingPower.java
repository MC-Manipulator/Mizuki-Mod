package powers;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import modcore.MizukiModCore;
import monsters.friendlys.Lumen;
import vfx.PhantomOfNetherseaEffect;

public class LanternUndyingPower extends AbstractMizukiPower
{
    public static final String id = MizukiModCore.MakePath(LanternUndyingPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(id);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final int strengthCount = 2;

    private float timer = 0;

    public LanternUndyingPower(AbstractCreature owner, int amt)
    {
        super(owner, amt, id, NAME);
        updateDescription();
        this.type = ArousalPower.PowerType.BUFF;
        this.isTurnBased = true;
    }

    public void update(int slot)
    {
        super.update(slot);
        if (!Settings.DISABLE_EFFECTS)
        {
            this.timer -= Gdx.graphics.getDeltaTime();
            if (this.timer < 0.0F) {
                this.timer = 0.04F;
                AbstractDungeon.effectsQueue.add(new PhantomOfNetherseaEffect(this.owner.hb.cX, this.owner.hb.cY));
            }
        }
    }

    @Override
    public void onRemove()
    {
        super.onRemove();


        if (!this.owner.isDeadOrEscaped())
        {
            ((Lumen)(this.owner)).lanternUndying = false;
            addToBot((AbstractGameAction)new ChangeStateAction((AbstractMonster) this.owner, "SKILLEND"));
        }
    }

    public String getDescription()
    {
        return DESCRIPTIONS[0];
    }
}
