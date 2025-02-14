package powers;

import Impairment.NervousImpairment;
import actions.ApplyImpairmentAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import helper.EventHelper;
import modcore.MizukiModCore;

import java.util.ArrayList;

public class EchoOfDeathAndRebirthPower  extends AbstractMizukiPower implements EventHelper.OnEnemyDeathSubscriber
{
    public static final String id = MizukiModCore.MakePath(EchoOfDeathAndRebirthPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(id);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;


    public EchoOfDeathAndRebirthPower(AbstractCreature owner, int amt)
    {
        super(owner, amt, id, NAME);
        updateDescription();
        this.type = PowerType.BUFF;
    }

    public void OnEnemyDeath(AbstractMonster monster)
    {
        if ((((AbstractMonster)monster).isDying || monster.currentHealth <= 0) && !monster.halfDead &&
                !monster.hasPower("Minion"))
        {
            AbstractDungeon.player.increaseMaxHp(amount * 2, false);
            addToBot((AbstractGameAction)new HealAction((AbstractCreature)owner, (AbstractCreature)owner, amount * 7));
        }
    }

    public void atStartOfTurnPostDraw()
    {
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead())
        {
            flash();
            addToBot((AbstractGameAction)new LoseHPAction(this.owner, this.owner, amount, AbstractGameAction.AttackEffect.FIRE));
            for (AbstractMonster m2 : (AbstractDungeon.getCurrRoom()).monsters.monsters)
            {
                if (!m2.isDeadOrEscaped())
                {
                    addToBot((AbstractGameAction) new ApplyImpairmentAction(new NervousImpairment(), m2, amount));
                }
            }
        }
    }

    public String getDescription()
    {
        return DESCRIPTIONS[0] + amount * 2 + DESCRIPTIONS[1] + amount * 7 + DESCRIPTIONS[2];
    }
}
