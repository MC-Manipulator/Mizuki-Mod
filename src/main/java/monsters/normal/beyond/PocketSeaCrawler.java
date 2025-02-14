package monsters.normal.beyond;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import monsters.AbstractMizukiMonster;
import powers.PocketSeaStingPower;

public class PocketSeaCrawler extends AbstractMizukiMonster
{
    //囊海爬行者
    public static final String ID = "Mizuki:PocketSeaCrawler";

    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Mizuki:PocketSeaCrawler");

    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    private int turns;

    public int strengthCount = 3;

    public int stingCount = 25;

    public int loseHealthCount = 20;

    public int recoverCount = 20;

    public PocketSeaCrawler(float x, float y)
    {
        super(NAME, "Mizuki:PocketSeaCrawler", 90, 0.0F, 0.0F, 180.0F, 270.0F, null, x, y);
        int dmg1, dmg2;
        this.type = AbstractMonster.EnemyType.NORMAL;

        if (AbstractDungeon.ascensionLevel >= 7)
        {
            setHp(90, 90);
        }
        else
        {
            setHp(80, 80);
        }

        if (AbstractDungeon.ascensionLevel >= 17)
        {
            loseHealthCount = 25;
            recoverCount = 30;
            stingCount = 15;
        }
        else if (AbstractDungeon.ascensionLevel >= 2)
        {
            loseHealthCount = 25;
            recoverCount = 20;
            stingCount = 25;
        }
        else
        {
            loseHealthCount = 20;
            recoverCount = 20;
            stingCount = 25;
        }

        loadAnimation("resources/img/monster/PocketSeaCrawler/enemy_1152_dsurch.atlas", "resources/img/monster/PocketSeaCrawler/enemy_1152_dsurch.json", 1.8F);
        this.stateData.setMix("Idle", "Die", 0.1F);
        this.flipHorizontal = true;
        this.state.addAnimation(0, "Idle", true, 0.0F);
        this.nextMove = 0;
        this.turns = 0;
        this.stingCount = 25;
        this.strengthCount = 3;
    }

    @Override
    public void usePreBattleAction()
    {
        //囊海刺伤
        addToBot(new ApplyPowerAction(this, this, new PocketSeaStingPower(this, stingCount)));
        addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, strengthCount * 2)));
        //在一回合中每失去一定生命，对敌人造成等同于自身力量的伤害并给予2点元素损伤
    }

    public void takeTurn()
    {
        AbstractPlayer p = AbstractDungeon.player;

        switch (this.nextMove)
        {
            case 0:
                //强化能力并恢复生命
                addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, strengthCount)));
                addToBot(new HealAction(this, this, recoverCount));
                break;
            case 1:
                //增加护甲并失去生命
                addToBot(new GainBlockAction(this, 10));
                addToBot(new LoseHPAction(this, this, loseHealthCount));
                break;
            case 2:
                //失去所有生命
                addToBot(new LoseHPAction(this, this, this.currentHealth));
                break;
        }
        getMove(0);
    }

    protected void getMove(int i)
    {
        int roll = AbstractDungeon.aiRng.random(99);
        if (roll < 40)
        {
            setMove(MOVES[0], (byte)0, Intent.BUFF);
        }
        else
        {
            setMove(MOVES[1], (byte)1, Intent.DEFEND_DEBUFF);
        }

        if (turns > 5)
        {
            setMove(MOVES[2], (byte)2, Intent.UNKNOWN);
        }
        turns++;
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