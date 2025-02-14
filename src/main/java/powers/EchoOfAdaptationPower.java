package powers;

import cards.Options.CardOption;
import cards.Options.DexterityOption;
import cards.Options.HealthOption;
import cards.Options.StrengthOption;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import modcore.MizukiModCore;

import java.util.ArrayList;

public class EchoOfAdaptationPower extends AbstractMizukiPower
{
    public static final String id = MizukiModCore.MakePath(EchoOfAdaptationPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(id);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public EchoOfAdaptationPower(AbstractCreature owner, int amt)
    {
        super(owner, amt, id, NAME);
        updateDescription();
        this.type = PowerType.BUFF;
    }
    public String getDescription()
    {
        return DESCRIPTIONS[0];
    }
    public void onEnergyRecharge()
    {
        flash();
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)owner, (AbstractCreature)owner, (AbstractPower)new FreeToUsePower((AbstractCreature)owner, 1), 1));
        for (int i = 0; EnergyPanel.getCurrentEnergy() > 0;i++)
        {
            AbstractDungeon.player.loseEnergy(1);
            for (int j = 0;j < amount;j++)
            {
                addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)owner, (AbstractCreature)owner, (AbstractPower)new MultipleCardsPlayPower((AbstractCreature)owner, 1), 1));
            }
        }
    }
}
