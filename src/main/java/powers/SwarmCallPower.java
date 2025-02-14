package powers;

import Impairment.NervousImpairment;
import actions.ApplyImpairmentAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import modcore.MizukiModCore;
import monsters.AbstractMizukiMonster;

public class SwarmCallPower extends AbstractMizukiPower
{
    public static final String id = MizukiModCore.MakePath(SwarmCallPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(id);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public SwarmCallPower(AbstractCreature owner, int amt)
    {
        super(owner, amt, id, NAME);
        updateDescription();
        this.type = PowerType.BUFF;
    }


    public String getDescription()
    {
        if (AbstractDungeon.player != null)
        {
            return DESCRIPTIONS[0] + AbstractDungeon.player.getCharacterString().NAMES[0] + DESCRIPTIONS[1] + amount * 10 + DESCRIPTIONS[2] + amount + DESCRIPTIONS[3];
        }
        else
        {
            return DESCRIPTIONS[0] + "player" + DESCRIPTIONS[1] + amount * 10 + DESCRIPTIONS[2] + amount + DESCRIPTIONS[3];
        }
    }




    @Override
    public void atStartOfTurn()
    {
        AbstractPlayer p = AbstractDungeon.player;
        addToBot((AbstractGameAction)new DamageAction(p, new DamageInfo(this.owner, this.amount * 10, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.POISON));
        addToBot(new ApplyImpairmentAction(new NervousImpairment(), p, this.amount));
    }
}
