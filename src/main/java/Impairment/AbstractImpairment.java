package Impairment;

import basemod.interfaces.PreMonsterTurnSubscriber;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.BobEffect;
import vfx.FlashImpairment;

public abstract class AbstractImpairment
{
    public String name;
    public String description;
    public String ID;
    public float cX = 0.0F;
    public float cY = 0.0F;
    public float tX;
    public float tY;
    protected float angle;
    protected float scale;
    protected static final float NUM_X_OFFSET = 20.0F * Settings.scale;
    protected static final float NUM_Y_OFFSET = -12.0F * Settings.scale;
    public float fontScale = Settings.scale * 0.7F;
    protected Color c = Settings.CREAM_COLOR.cpy();
    public Texture img = null;
    protected BobEffect bobEffect = new BobEffect(3.0F * Settings.scale, 3.0F);
    public int maxcount = 1;
    public int currentcount = 0;
    public int maxDryout = 2;
    public int currentDryout = 0;
    public ImpairmentType type;
    public Hitbox hb = new Hitbox(50, 50);
    public boolean isDryout = false;

    public AbstractCreature creature;

    public abstract void render(SpriteBatch paramSpriteBatch);
    protected void renderText(SpriteBatch sb)
    {
        if (!isDryout)
        {
            FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L,
                    Integer.toString(currentcount) + "/" + Integer.toString(maxcount), tX, tY, Color.CYAN, this.fontScale);
        }
        else
        {
            FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L,
                    Integer.toString(currentDryout) + "/" + Integer.toString(maxDryout), tX, tY, Color.GRAY, this.fontScale);
        }
    }

    public void flash()
    {
        AbstractDungeon.effectList.add(new FlashImpairment(this));
    }

    public enum  ImpairmentType
    {
        Nervous,
        Corrosion
    }
}
