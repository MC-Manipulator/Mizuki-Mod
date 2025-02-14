package patches;

import basemod.ReflectionHacks;
import basemod.abstracts.AbstractCardModifier;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import relics.PureWhiteDanceShoes;

public class JetBlackDanceShoesMod extends AbstractCardModifier
{
    public static String ID = "Mizuki:JetBlackDanceShoesMod";
    AbstractRelic r = new PureWhiteDanceShoes();

    Texture t = ImageMaster.loadImage("resources/img/relics/" + "JetBlackDanceShoes" + ".png");
    public JetBlackDanceShoesMod()
    {

    }

    @Override
    public void onRender(AbstractCard card, SpriteBatch sb)
    {
        /*
        float targetScale = Settings.scale * card.drawScale;
        r.scale = MathHelper.cardScaleLerpSnap(r.scale, targetScale * 0.9F);
        r.targetX = card.current_x;
        r.targetY = card.current_y + AbstractCard.IMG_HEIGHT * 0.3F * (card.drawScale + r.scale);
        r.currentX = MathHelper.cardLerpSnap(r.currentX, r.targetX);
        r.currentY = MathHelper.cardLerpSnap(r.currentY, r.targetY);
        sb.draw(this.t, r.currentX - 64.0F, r.currentY - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, r.scale, r.scale, 0, 0, 0, 128, 128, false, false);

         */
    }

    public AbstractCardModifier makeCopy()
    {
        return new JetBlackDanceShoesMod();
    }

    public String identifier(AbstractCard card)
    {
        return ID;
    }
}
