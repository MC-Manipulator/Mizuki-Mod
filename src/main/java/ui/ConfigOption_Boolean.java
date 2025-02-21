package ui;

import basemod.IUIElement;
import basemod.ModLabel;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.ModToggleButton;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;

public class ConfigOption_Boolean extends ConfigOption<Boolean>
{
    public boolean DefaultValue;

    public ConfigOption_Boolean(SpireConfig config, String key, boolean defaultValue)
    {
        super(config, key);
        this.DefaultValue = defaultValue;
    }

    public Boolean Get()
    {
        if (this.Value == null)
        {
            if (this.Config.has(this.Key))
            {
                this.Value = this.Config.getBool(this.Key);
            }
            else
            {
                this.Value = this.DefaultValue;
            }
        }

        return this.Value;
    }

    public Boolean Set(Boolean value, boolean save)
    {
        this.Value = value;
        this.Config.setBool(this.Key, value);
        if (save)
            Save();
        return this.Value;
    }

    public void AddToPanel(ModPanel panel, String label, float x, float y)
    {
        panel.addUIElement((IUIElement)new ModLabeledToggleButton(label, x, y, Settings.CREAM_COLOR.cpy(), FontHelper.charDescFont,
                Get(), panel, __ -> {}, c -> Set(c.enabled, true)));
    }
}
