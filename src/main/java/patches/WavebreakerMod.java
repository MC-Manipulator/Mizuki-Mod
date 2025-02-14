package patches;

import basemod.ReflectionHacks;
import basemod.abstracts.AbstractCardModifier;
import cards.Powers.EchoOfAssimilation;
import cards.Skills.TowerOfTheWitchKing;
import characters.Mizuki;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import modcore.MizukiModCore;
import relics.Wavebreaker;

public class WavebreakerMod extends AbstractCardModifier
{
    public static String ID = "Mizuki:WavebreakerMod";
    AbstractRelic r = new Wavebreaker();

    Texture t = ImageMaster.loadImage("resources/img/relics/" + "Wavebreaker" + ".png");
    public WavebreakerMod()
    {

    }
    @Override
    public void onRender(AbstractCard card, SpriteBatch sb)
    {
        boolean hovered = ((Boolean) ReflectionHacks.getPrivate(card, AbstractCard.class, "renderTip")).booleanValue();

        //float targetScale = hovered ? (3F * card.drawScale) : (0.2F * card.drawScale);
        float targetScale = Settings.scale * card.drawScale;
        r.scale = MathHelper.cardScaleLerpSnap(r.scale, targetScale * 0.9F);
        //card.current_x += AbstractCard.IMG_WIDTH * c.drawScale;
        //card.current_y += AbstractCard.IMG_HEIGHT * 0.5F * (card.drawScale + c.drawScale);

        r.targetX = card.current_x;
        r.targetY = card.current_y + AbstractCard.IMG_HEIGHT * 0.3F * (card.drawScale + r.scale);
        r.currentX = MathHelper.cardLerpSnap(r.currentX, r.targetX);
        r.currentY = MathHelper.cardLerpSnap(r.currentY, r.targetY);
        sb.draw(this.t, r.currentX - 64.0F, r.currentY - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, r.scale, r.scale, 0, 0, 0, 128, 128, false, false);
    }

    public AbstractCardModifier makeCopy()
    {
        return new WavebreakerMod();
    }

    public String identifier(AbstractCard card)
    {
        return ID;
    }
}
