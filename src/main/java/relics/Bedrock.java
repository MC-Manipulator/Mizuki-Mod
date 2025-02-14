package relics;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import modcore.MizukiModCore;

public class Bedrock extends AbstractMizukiRelic
{
    //基岩
    public static final String ID = MizukiModCore.MakePath(Bedrock.class.getSimpleName());

    private static final float health1Percent = 0.25f;

    private static final int powerAmount = 2;

    public Bedrock()
    {
        super(ID, AbstractRelic.RelicTier.SPECIAL, LandingSound.HEAVY);
    }

    public String getUpdatedDescription()
    {
        return this.DESCRIPTIONS[0] + (int)(health1Percent * 100) + this.DESCRIPTIONS[1] + powerAmount + this.DESCRIPTIONS[2];
    }

    public void onEquip()
    {
        AbstractDungeon.player.increaseMaxHp(MathUtils.ceil(AbstractDungeon.player.maxHealth * health1Percent), true);
        AbstractDungeon.player.masterHandSize--;
        AbstractDungeon.bossList.clear();
    }





    public void atBattleStart()
    {
        flash();
        addToTop(
                (AbstractGameAction) new ApplyPowerAction((AbstractCreature) AbstractDungeon.player, (AbstractCreature)AbstractDungeon.player,
                        (AbstractPower) new StrengthPower((AbstractCreature)AbstractDungeon.player, powerAmount), powerAmount));
        addToTop(
                (AbstractGameAction) new ApplyPowerAction((AbstractCreature) AbstractDungeon.player, (AbstractCreature)AbstractDungeon.player,
                        (AbstractPower) new DexterityPower((AbstractCreature)AbstractDungeon.player, powerAmount), powerAmount));
        addToTop(
                (AbstractGameAction)new RelicAboveCreatureAction((AbstractCreature)AbstractDungeon.player, this));
    }

    @Override
    public void onUnequip()
    {
        AbstractDungeon.player.masterHandSize++;
    }

    @Override
    public void onEnterRoom(AbstractRoom room)
    {
        super.onEnterRoom(room);
    }
}
