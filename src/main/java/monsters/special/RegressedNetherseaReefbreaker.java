package monsters.special;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import monsters.AbstractMizukiMonster;
import powers.DeadlyRhythmPower;
import powers.StealthPower;

public class RegressedNetherseaReefbreaker extends AbstractMizukiMonster
{
    //退行的深溟裂礁者

    public static final String ID = "Mizuki:RegressedNetherseaReefbreaker";

    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Mizuki:RegressedNetherseaReefbreaker");

    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    public RegressedNetherseaReefbreaker(float x, float y)
    {

        super(NAME, "Mizuki:RegressedNetherseaReefbreaker", 90, 0.0F, 0.0F, 150.0F, 320.0F, null, x, y);
        this.type = AbstractMonster.EnemyType.NORMAL;


        if (AbstractDungeon.ascensionLevel >= 19)
        {
            setHp(40);
        }
        else
        {
            setHp(30);
        }


        loadAnimation("resources/img/monster/NetherseaReefbreaker/enemy_1235_dsbskr.atlas", "resources/img/monster/NetherseaReefbreaker/enemy_1235_dsbskr.json", 1.2F);
        this.stateData.setMix("Idle", "Die", 0.1F);
        this.flipHorizontal = true;
        this.state.addAnimation(0, "Idle", true, 0.0F);
        this.nextMove = 0;
    }

    public void takeTurn()
    {
        AbstractPlayer p = AbstractDungeon.player;
        getMove(0);
    }

    protected void getMove(int i)
    {
        setMove(MOVES[0], (byte)0, Intent.UNKNOWN);
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
