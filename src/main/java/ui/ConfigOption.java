package ui;

import basemod.ModPanel;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import java.io.IOException;

public abstract class ConfigOption<T>
{
    public String Key;

    public SpireConfig Config;

    protected T Value;

    public ConfigOption(SpireConfig config, String key)
    {
        this.Config = config;
        this.Key = key;
    }

    public abstract T Get();

    public abstract T Set(T paramT, boolean paramBoolean);

    public boolean Save()
    {
        try
        {
            this.Config.save();
            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public abstract void AddToPanel(ModPanel paramModPanel, String paramString, float paramFloat1, float paramFloat2);

}
