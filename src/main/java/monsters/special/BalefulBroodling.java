package monsters.special;

import Impairment.CorrosionImpairment;
import actions.ApplyImpairmentAction;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import helper.ImpairementManager;
import monsters.AbstractMizukiMonster;
import powers.DyingPower;


public class BalefulBroodling extends AbstractMizukiMonster
{
    //注亡拟嗣
    public static final String ID = "Mizuki:BalefulBroodling";

    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Mizuki:BalefulBroodling");

    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    public BalefulBroodling(float x, float y)
    {

        super(NAME, "Mizuki:BalefulBroodling", 90, 0.0F, 0.0F, 150.0F, 320.0F, null, x, y);

        if (AbstractDungeon.ascensionLevel >= 7)
        {
            setHp(3, 3);
        }
        else
        {
            setHp(2, 2);
        }

        loadAnimation("resources/img/monster/BalefulBroodling/enemy_2024_synut.atlas", "resources/img/monster/BalefulBroodling/enemy_2024_synut.json", 1.5F);
        this.flipHorizontal = true;
        this.state.addAnimation(0, "Idle", true, 0.0F);
        this.nextMove = 0;
        this.stateData.setMix("Idle", "Die", 0.1F);
    }

    public BalefulBroodling(float x, float y, boolean isflip)
    {
        this(x, y);
        this.flipHorizontal = isflip;
    }

    public void usePreBattleAction()
    {
        AbstractDungeon.actionManager.addToBottom(
                (AbstractGameAction)new ApplyPowerAction((AbstractCreature)this, (AbstractCreature)this,
                        (AbstractPower)new DyingPower((AbstractCreature)this, 1, true)));
    }

    public void takeTurn()
    {
        AbstractPlayer p = AbstractDungeon.player;

        switch (this.nextMove)
        {
            case 0:
                this.state.setAnimation(0, "Attack", false);
                this.state.addAnimation(0, "Idle", true, 0.0F);
                addToBot(new ApplyImpairmentAction(new CorrosionImpairment(), p, 1));
                break;
        }
        getMove(0);
    }

    protected void getMove(int i)
    {
        setMove(MOVES[0], (byte)0, Intent.DEBUFF);
    }

    public void die()
    {

        this.state.setAnimation(0, "Die", false);
        addToBot(new AbstractGameAction()
        {
            @Override
            public void update()
            {
                trueDie();
                this.isDone = true;
            }
        });

    }

    public void trueDie()
    {
        super.die();
    }
}


