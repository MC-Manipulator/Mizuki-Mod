package relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class AbstractMizukiRelic extends CustomRelic
{
    /*
    public AbstractMizukiRelic(String id, RelicTier tier, LandingSound sfx)
    {
        super(id, ImageMaster.loadImage(String.format("resources/img/relics/%s.png", new Object[] { id.replace("Mizuki_", "") })), tier, sfx);
    }*/

    public AbstractMizukiRelic(String id, RelicTier tier, LandingSound sfx)
    {
        super(id, ImageMaster.loadImage(getPicPath(id)), ImageMaster.loadImage(getOutLinePath(id)), tier, sfx);
    }

    public static final String getPicPath(String id) {
        return "resources/img/relics/" + id.replace("Mizuki_", "") + ".png";
    }

    public static final String getOutLinePath(String id) {
        return "resources/img/relics/" + id.replace("Mizuki_", "") + "Outline" + ".png";
    }
    public String getUpdatedDescription()
    {
        return this.DESCRIPTIONS[0];
    }

    public void onEquip()
    {
        super.onEquip();
    }

    public void onUnequip()
    {
        super.onUnequip();
    }
}
