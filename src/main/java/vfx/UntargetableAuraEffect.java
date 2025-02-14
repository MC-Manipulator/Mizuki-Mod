package vfx;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import modcore.MizukiModCore;

public class UntargetableAuraEffect extends AbstractGameEffect
{
    public static boolean switcher = true;
    private TextureAtlas.AtlasRegion img = ImageMaster.EXHAUST_L;
    private float vY;
    private float y;
    private float x;

    public UntargetableAuraEffect()
    {
        this.color = new Color(MathUtils.random(0.5F, 0.6F), MathUtils.random(0.5F, 0.6F), MathUtils.random(0.5F, 0.6F), 0.0F);
        this.x = AbstractDungeon.player.hb.cX + MathUtils.random(-AbstractDungeon.player.hb.width / 1.5F, AbstractDungeon.player.hb.width / 1.5F);
        this.y = AbstractDungeon.player.hb.cY + MathUtils.random(-AbstractDungeon.player.hb.height / 2F, AbstractDungeon.player.hb.height / 1.25F);
        this.x -= this.img.packedWidth / 2.0F;
        this.y -= this.img.packedHeight / 2.0F;
        switcher = !switcher;
        this.renderBehind = true;
        this.rotation = MathUtils.random(360.0F);
        if (switcher) {
            this.renderBehind = true;
            this.vY = MathUtils.random(0.0F, 40.0F);
        } else {
            this.renderBehind = false;
            this.vY = MathUtils.random(0.0F, -40.0F);
        }
        this.duration = 2.0F;
    }

    public UntargetableAuraEffect(AbstractCreature c)
    {
        this();
        x = c.hb.cX + MathUtils.random(-c.hb.width / 1.5F, c.hb.width / 1.5F);
        y = c.hb.cY + MathUtils.random(-c.hb.height / 2F, c.hb.height / 1.25F);
        this.x -= this.img.packedWidth / 2.0F;
        this.y -= this.img.packedHeight / 2.0F;
    }

    public void update()
    {
        if (this.duration > 1.0F)
        {
            this.color.a = Interpolation.fade.apply(0.3F, 0.0F, this.duration - 1.0F);
        }
        else
        {
            this.color.a = Interpolation.fade.apply(0.0F, 0.3F, this.duration);
        }
        this.rotation += Gdx.graphics.getDeltaTime() * this.vY;
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F)
            this.isDone = true;
    }

    public void render(SpriteBatch sb)
    {
        sb.setColor(this.color);
        sb.setBlendFunction(770, 1);
        sb.draw((TextureRegion) this.img, this.x, this.y, this.img.packedWidth / 2.0F, this.img.packedHeight / 2.0F, this.img.packedWidth, this.img.packedHeight, this.scale, this.scale, this.rotation);
        sb.setBlendFunction(770, 771);
    }

    public void dispose() {}
}