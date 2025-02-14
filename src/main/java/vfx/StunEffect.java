package vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class StunEffect extends AbstractGameEffect
{
    private float x;

    private float y;

    private static Texture img = ImageMaster.INTENT_STUN;

    public StunEffect()
    {
        this.startingDuration = 2.0F;
        this.duration = this.startingDuration;
        this.scale = Settings.scale * 3.0F;
        this.x = Settings.WIDTH * 0.5F;
        this.y = Settings.HEIGHT * 0.5F;
        this.color = Color.WHITE.cpy();
    }

    public void update()
    {
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F)
            this.isDone = true;
        if (this.duration < 1.0F)
        {
            this.color.a = Interpolation.fade.apply(0.0F, 1.0F, this.duration);
        }
        else
        {
            this.y = Interpolation.swingIn.apply(Settings.HEIGHT * 0.7F - Settings.HEIGHT * 0.5F, -Settings.HEIGHT * 0.5F, this.duration - 1.0F);
        }
    }

    public void render(SpriteBatch sb)
    {
        sb.setColor(this.color);
        sb.draw(img, this.x, this.y, img.getWidth(), img.getHeight());
    }

    public void dispose() {}
}
