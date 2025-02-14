package Impairment;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import modcore.MizukiModCore;

public class CorrosionImpairment extends AbstractImpairment
{
    int previouscount = 0;
    private static final Texture IMG = ImageMaster.loadImage("resources/img/impairment/corrosion.png");

    public CorrosionImpairment()
    {
        this.img = IMG;
        maxcount = 5;
        currentcount = 0;
        maxDryout = 2;
        currentDryout = 0;
        type = ImpairmentType.Corrosion;
        MizukiModCore.logger.info("generate Corrosion Impairment");
    }
    public void render(SpriteBatch sb)
    {
        if (this.fontScale != 0.7)
        {
            this.fontScale = MathUtils.lerp(this.fontScale, 0.7F, Gdx.graphics.getDeltaTime() * 10.0F);
            if (this.fontScale - 0.7 < 0.05F)
                this.fontScale = 0.7F;
        }
        if (previouscount != currentcount)
        {
            //MizukiModCore.logger.info("countchange" + previouscount + "=>" + currentcount);
            previouscount = currentcount;
        }
        sb.draw(this.img, tX -50 / 2F, tY + 50 / 2F, 0, 0, 50, 50, 1F, 1F, 0, 0, 0, 50, 50, false, false);
        sb.setBlendFunction(770, 771);

        //MizukiModCore.logger.info(scale);
        renderText(sb);
        hb.render(sb);
    }
}
