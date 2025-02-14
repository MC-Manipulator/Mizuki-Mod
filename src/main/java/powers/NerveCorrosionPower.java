package powers;

import cards.Skills.AbsurdFate;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import helper.EventHelper;
import helper.ImpairementManager;
import modcore.MizukiModCore;

public class NerveCorrosionPower extends AbstractMizukiPower
{

    public static final String id = MizukiModCore.MakePath(NerveCorrosionPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(id);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private AbstractCard sourceCard;

    public NerveCorrosionPower(AbstractCreature owner, int amt)
    {
        super(owner, amt, id, NAME);
        updateDescription();
        EventHelper.Subscribe(this);
        this.type = PowerType.BUFF;
    }

    public String getDescription()
    {
        return DESCRIPTIONS[0];
    }
}
