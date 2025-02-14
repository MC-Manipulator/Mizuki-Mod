package stances;

import basemod.helpers.VfxBuilder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.actions.watcher.NotStanceCheckAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.EmptyStanceEffect;
import vfx.UntargetableAuraEffect;
import vfx.UntargetableParticalEffect;

public class UntargetableStance extends AbstractStance
{
    public static final String STANCE_ID = "Untargetable";

    private static long sfxId = -1L;

    public UntargetableStance()
    {
        this.ID = "Untargetable";
        this.name = CardCrawlGame.languagePack.getCharacterString("Mizuki_Mizuki").TEXT[3];
        updateDescription();
    }

    public float atDamageReceive(float damage, DamageInfo.DamageType type)
    {
        if (type == DamageInfo.DamageType.NORMAL)
            return 0;
        return damage;
    }

    public void updateAnimation()
    {
        if (!Settings.DISABLE_EFFECTS)
        {
            this.particleTimer -= Gdx.graphics.getDeltaTime();
        }
        this.particleTimer2 -= Gdx.graphics.getDeltaTime();
        if (this.particleTimer2 < 0.0F)
        {
            this.particleTimer2 = MathUtils.random(0.01F, 0.05F);
            AbstractDungeon.effectsQueue.add(new UntargetableAuraEffect());
        }
    }

    @Override
    public void onPlayCard(AbstractCard card)
    {
        if (card.type == AbstractCard.CardType.ATTACK)
        {
            AbstractDungeon.actionManager.addToBottom(
                    (AbstractGameAction) new NotStanceCheckAction("Neutral", (AbstractGameAction)new VFXAction(
                            (AbstractGameEffect)new EmptyStanceEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY), 0.1F)));
            AbstractDungeon.actionManager.addToBottom(
                    (AbstractGameAction)new ChangeStanceAction("Neutral"));
        }
        /*
        AbstractDungeon.actionManager.addToBottom(
                (AbstractGameAction) new NotStanceCheckAction("Neutral", (AbstractGameAction)new VFXAction(
                        (AbstractGameEffect)new EmptyStanceEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY), 0.1F)));
        AbstractDungeon.actionManager.addToBottom(
                (AbstractGameAction)new ChangeStanceAction("Neutral"));*/
    }

    @Override
    public void atStartOfTurn()
    {
        AbstractDungeon.actionManager.addToBottom(
                (AbstractGameAction) new NotStanceCheckAction("Neutral", (AbstractGameAction)new VFXAction(
                        (AbstractGameEffect)new EmptyStanceEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY), 0.1F)));
        AbstractDungeon.actionManager.addToBottom(
                (AbstractGameAction)new ChangeStanceAction("Neutral"));
    }

    public void updateDescription()
    {
        this.description = CardCrawlGame.languagePack.getCharacterString("Mizuki_Mizuki").TEXT[4];
    }

    public void onEnterStance()
    {
        if (sfxId != -1L)
            stopIdleSfx();
        CardCrawlGame.sound.play("STANCE_ENTER_CALM");
        sfxId = CardCrawlGame.sound.playAndLoop("STANCE_LOOP_CALM");
        AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.GRAY, true));
        //AbstractDungeon.effectsQueue.add(new UntargetableStanceParticalGenerator(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY));
    }

    public void onExitStance()
    {
        stopIdleSfx();
    }

    public void stopIdleSfx()
    {
        if (sfxId != -1L)
        {
            CardCrawlGame.sound.stop("STANCE_LOOP_CALM", sfxId);
            sfxId = -1L;
        }
    }
}
