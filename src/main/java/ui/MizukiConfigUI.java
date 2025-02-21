package ui;

import basemod.BaseMod;
import basemod.ModPanel;
import characters.Mizuki;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import modcore.MizukiModCore;
import powers.AcidPower;

import java.io.IOException;
import java.util.HashSet;

public class MizukiConfigUI
{
    private SpireConfig config;

    public ConfigOption_Boolean FightWithOthers;
    public ConfigOption_Boolean RelicAppear;

    public static final String[] DESCRIPTIONS;

    private static final UIStrings UI_STRINGS = CardCrawlGame.languagePack.getUIString("Mizuki_ConfigOptions");

    static
    {
        DESCRIPTIONS = UI_STRINGS.TEXT;
    }

    public void Initialize()
    {
        try
        {
            this.config = new SpireConfig("Mizuki", "MizukiConfig");
            this.FightWithOthers = new ConfigOption_Boolean(this.config, "Mizuki-MonstersFightWithOtherChars", false);
            this.RelicAppear = new ConfigOption_Boolean(this.config, "Mizuki-RelicAppearWhenPlayingOtherChars", false);
            LoadOriginMod();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void InitializeOptions()
    {
        ModPanel panel = new ModPanel();
        //AnimatorStrings.Misc misc = GR.Animator.Strings.Misc;
        this.FightWithOthers.AddToPanel(panel, DESCRIPTIONS[0], 400.0F, 700.0F);
        this.RelicAppear.AddToPanel(panel, DESCRIPTIONS[1], 400.0F, 650.0F);

        BaseMod.registerModBadge(ImageMaster.loadImage("resources/img/powers/HysterotraumaticPower32.png"), "Mizuki", "Manipulator", "", panel);
    }


    public void LoadOriginMod()
    {
        try
        {
            MizukiModCore.originalMod = this.config.getBool("Mizuki-OriginMod");
            MizukiModCore.logger.info("读取原版模式状态:" + MizukiModCore.originalMod);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void SaveOriginalMod()
    {
        try
        {
            this.config.setBool("Mizuki-OriginMod", MizukiModCore.originalMod);
            MizukiModCore.logger.info("保存原版模式状态:" + MizukiModCore.originalMod);
            config.save();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public boolean Save()
    {
        try
        {
            this.config.save();
            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }
}
