package powers;

import actions.EnvironmentAnalyzingAction;
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
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import modcore.MizukiModCore;

import java.util.ArrayList;

public class EchoOfOvergrowthPower extends AbstractMizukiPower
{
    public static final String id = MizukiModCore.MakePath(EchoOfOvergrowthPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(id);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;


    public EchoOfOvergrowthPower(AbstractCreature owner, int amt)
    {
        super(owner, amt, id, NAME);
        updateDescription();
        this.type = PowerType.BUFF;
    }

    public String getDescription()
    {
        return DESCRIPTIONS[0];
    }
    public void atStartOfTurnPostDraw()
    {
        ArrayList<AbstractCard> stanceChoices = new ArrayList<>();
        stanceChoices.add(new StrengthOption());
        stanceChoices.add(new DexterityOption());
        stanceChoices.add(new HealthOption());
        stanceChoices.add(new CardOption());
        addToBot((AbstractGameAction)new ChooseOneAction(stanceChoices));
    }
}
