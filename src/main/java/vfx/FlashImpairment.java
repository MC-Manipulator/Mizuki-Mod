package vfx;

import Impairment.AbstractImpairment;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class FlashImpairment extends AbstractGameEffect
{
    private float x;

    private float y;

    private Texture img;

    private static final int W = 32;

    private float scale = Settings.scale;

    public FlashImpairment(AbstractImpairment imp)
    {
        if (!imp.creature.isDeadOrEscaped())
        {
            this.x = imp.creature.hb.cX;
            this.y = imp.creature.hb.cY;
        }
        this.img = imp.img;
        this.duration = 0.7F;
        this.startingDuration = 0.7F;
        this.color = Color.WHITE.cpy();
        this.renderBehind = false;
    }

    public void update()
    {
        super.update();
        this.scale = Interpolation.exp5In.apply(Settings.scale, Settings.scale * 0.3F, this.duration / this.startingDuration);
    }

    public void render(SpriteBatch sb)
    {
        sb.setBlendFunction(770, 1);
        sb.setColor(this.color);
        if (this.img != null)
        {
            sb.draw(this.img, this.x - 16.0F, this.y - 16.0F, 16.0F, 16.0F, 32.0F, 32.0F, this.scale * 12.0F, this.scale * 12.0F, 0.0F, 0, 0, 32, 32, false, false);
            sb.draw(this.img, this.x - 16.0F, this.y - 16.0F, 16.0F, 16.0F, 32.0F, 32.0F, this.scale * 10.0F, this.scale * 10.0F, 0.0F, 0, 0, 32, 32, false, false);
            sb.draw(this.img, this.x - 16.0F, this.y - 16.0F, 16.0F, 16.0F, 32.0F, 32.0F, this.scale * 8.0F, this.scale * 8.0F, 0.0F, 0, 0, 32, 32, false, false);
            sb.draw(this.img, this.x - 16.0F, this.y - 16.0F, 16.0F, 16.0F, 32.0F, 32.0F, this.scale * 7.0F, this.scale * 7.0F, 0.0F, 0, 0, 32, 32, false, false);
        }
        sb.setBlendFunction(770, 771);
    }

    public void dispose()
    {

    }
}
