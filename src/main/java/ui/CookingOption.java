package ui;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import helper.CookingHelper;
import modcore.MizukiModCore;
import vfx.CookingEffect;

public class CookingOption extends AbstractCampfireOption
{
    public static final String[] DESCRIPTIONS;

    private static final UIStrings UI_STRINGS = CardCrawlGame.languagePack.getUIString("Mizuki_CookingCampfireOption");

    static
    {
        DESCRIPTIONS = UI_STRINGS.TEXT;
    }

    public CookingOption(boolean active)
    {
        this.label = DESCRIPTIONS[0];
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group)
            c.update();
        this.usable = active;
        if (active)
        {
            this.description = DESCRIPTIONS[1];
            this.img = ImageMaster.loadImage("resources/img/UI/campfire/CookingCampfireEnable.png");
        }
        else
        {
            this.description = DESCRIPTIONS[2];
            this.img = ImageMaster.loadImage("resources/img/UI/campfire/CookingCampfireDisable.png");
        }
    }

    public void useOption()
    {
        if (this.usable)
        {
            CookingHelper.currCookingEffect = new CookingEffect();
            AbstractDungeon.effectList.add(CookingHelper.currCookingEffect);
            MizukiModCore.logger.info("COOKING OPTION");
        }
    }

    public void reCheck()
    {
        if (CookingHelper.getCookableCards().size() < 3)
        {
            this.usable = false;
        }
        if (this.usable)
        {
            this.description = DESCRIPTIONS[1];
            this.img = ImageMaster.loadImage("resources/img/UI/campfire/CookingCampfireEnable.png");
        }
        else
        {
            this.description = DESCRIPTIONS[2];
            this.img = ImageMaster.loadImage("resources/img/UI/campfire/CookingCampfireDisable.png");
        }
    }

    public void update()
    {
        float hackScale = ((Float) ReflectionHacks.getPrivate(this, AbstractCampfireOption.class, "scale")).floatValue();
        if (this.hb.hovered)
        {
            if (!this.hb.clickStarted)
            {
                ReflectionHacks.setPrivate(this, AbstractCampfireOption.class, "scale", Float.valueOf(MathHelper.scaleLerpSnap(hackScale, Settings.scale)));
                ReflectionHacks.setPrivate(this, AbstractCampfireOption.class, "scale", Float.valueOf(MathHelper.scaleLerpSnap(hackScale, Settings.scale)));
            }
            else
            {
                ReflectionHacks.setPrivate(this, AbstractCampfireOption.class, "scale", Float.valueOf(MathHelper.scaleLerpSnap(hackScale, 0.9F * Settings.scale)));
            }
        }
        else
        {
            ReflectionHacks.setPrivate(this, AbstractCampfireOption.class, "scale", Float.valueOf(MathHelper.scaleLerpSnap(hackScale, 0.9F * Settings.scale)));
        }
        super.update();
    }
}
