package monsters.special;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import monsters.AbstractMizukiMonster;
import powers.PocketSeaStingPower;

public class RegressedPocketSeaCrawler extends AbstractMizukiMonster
{
    //退行的囊海爬行者
    public static final String ID = "Mizuki:RegressedPocketSeaCrawler";

    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Mizuki:RegressedPocketSeaCrawler");

    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    public RegressedPocketSeaCrawler(float x, float y)
    {
        super(NAME, "Mizuki:RegressedPocketSeaCrawler", 90, 0.0F, 0.0F, 150.0F, 320.0F, null, x, y);
        this.type = AbstractMonster.EnemyType.NORMAL;

        if (AbstractDungeon.ascensionLevel >= 19)
        {
            setHp(30);
        }
        else
        {
            setHp(20);
        }

        loadAnimation("resources/img/monster/PocketSeaCrawler/enemy_1152_dsurch.atlas", "resources/img/monster/PocketSeaCrawler/enemy_1152_dsurch.json", 1.8F);
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
