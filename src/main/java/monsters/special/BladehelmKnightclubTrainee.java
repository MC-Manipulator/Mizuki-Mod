package monsters.special;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import monsters.AbstractMizukiMonster;

public class BladehelmKnightclubTrainee extends AbstractMizukiMonster
{
    //锋盔骑士团学徒，第二层
    public static final String ID = "Mizuki:BladehelmKnightclubTrainee";

    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Mizuki:BladehelmKnightclubTrainee");

    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    private static final int debuffCount = 5;

    private int turn = 0;

    public int leaderIntent = 0;

    public float[] POSX = new float[2];

    public float[] POSY = new float[2];

    private AbstractMonster[] minions = new AbstractMonster[2];

    private boolean firstTurn = true;

    public BladehelmKnightclubTrainee(float x, float y)
    {

        super(NAME, "Mizuki:BladehelmKnightclubTrainee", 90, 0.0F, 0.0F, 150.0F, 320.0F, null, x, y);
        int dmg1, dmg2, acid, block;
        this.type = EnemyType.ELITE;


        if (AbstractDungeon.ascensionLevel >= 8)
        {
            setHp(130, 135);
        }
        else
        {
            setHp(120, 127);
        }


        if (AbstractDungeon.ascensionLevel >= 18)
        {
            dmg1 = 25;
        }
        else if (AbstractDungeon.ascensionLevel >= 3)
        {
            dmg1 = 25;
        }
        else
        {
            dmg1 = 20;
        }

        this.damage.add(new DamageInfo((AbstractCreature) this, MathUtils.floor(dmg1)));

        int turn = 0;
        int leaderIntent = 0;
        int debuffCount = 5;
        loadAnimation("resources/img/monster/BladehelmKnightclubTrainee/enemy_1104_lfkght.atlas", "resources/img/monster/BladehelmKnightclubTrainee/enemy_1104_lfkght.json", 1.2F);
        this.stateData.setMix("Idle", "Die", 0.1F);
        this.flipHorizontal = true;
        this.state.addAnimation(0, "Idle", true, 0.0F);
        this.nextMove = 0;
    }

    public void usePreBattleAction()
    {
        POSX[0] = AbstractDungeon.player.drawX - this.drawX + 400;
        POSX[1] = AbstractDungeon.player.drawX - this.drawX + 850;

        POSY[0] = MathUtils.random(-20F,20F);
        POSY[1] = MathUtils.random(-20F,20F);
    }

    public void takeTurn()
    {
        AbstractPlayer p = AbstractDungeon.player;

        switch (this.nextMove)
        {
            case 0:
                spawn(2);
            case 1:
                //空置
            case 2:
                //空置
            case 3:
                //三种号令和召唤都对应一个动作
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "Order"));
                break;
            case 4:
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "Order"));
                addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)this, (AbstractPower)new StrengthPower((AbstractCreature)p, -debuffCount), -debuffCount));
                if (p != null && !p.hasPower("Artifact"))
                    addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)this, (AbstractPower)new GainStrengthPower((AbstractCreature)p, debuffCount), debuffCount));
                break;
            case 5:
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "ATTACK"));
                addToBot((AbstractGameAction) new DamageAction((AbstractCreature)p, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                break;
        }

        addToBot(new AbstractGameAction()
        {
            @Override
            public void update()
            {
                if (!ifMinionExist())
                {
                    hasSpawned = false;
                }
                this.isDone = true;
            }
        });
        getMove(0);
    }

    protected boolean ifMinionExist()
    {
        for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters)
        {
            if (!m.isDeadOrEscaped() && m.id.equals("Mizuki:RoarKnightclubTrainee"))
            {
                return true;
            }
        }
        for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters)
        {
            if (!m.isDeadOrEscaped() && m.id.equals("Mizuki:NamelessIndependentKnight"))
            {
                return true;
            }
        }
        return false;
    }

    boolean hasSpawned = false;

    protected void getMove(int i)
    {
        boolean minionExist = ifMinionExist();
        if (hasSpawned)
        {
            //一开始随机指示
            //给出指示后两回合保持指示
            //防御过后会强化
            //强化之后会进攻
            //进攻之后随机指示
            if (turn == 0)
            {
                int roll = AbstractDungeon.aiRng.random(99);
                if (leaderIntent == 0 || leaderIntent == 3)
                {
                    if (roll < 33)
                    {
                        leaderIntent = 1;
                        setMove(MOVES[1], (byte)1, AbstractMonster.Intent.BUFF);
                    }
                    else if (roll < 66)
                    {
                        leaderIntent = 2;
                        setMove(MOVES[2], (byte)2, AbstractMonster.Intent.BUFF);
                    }
                    else
                    {
                        leaderIntent = 3;
                        setMove(MOVES[3], (byte)3, AbstractMonster.Intent.BUFF);
                    }
                }
                else if (leaderIntent == 1)
                {
                    leaderIntent = 2;
                    setMove(MOVES[2], (byte)2, Intent.BUFF);
                }
                else if (leaderIntent == 2)
                {
                    leaderIntent = 3;
                    setMove(MOVES[3], (byte)3, AbstractMonster.Intent.BUFF);
                }
                turn = 1;
            }
            else
            {
                int roll = AbstractDungeon.aiRng.random(99);
                if (roll < 50)
                {
                    setMove(MOVES[4], (byte)4, Intent.DEBUFF);
                }
                else
                {
                    setMove(MOVES[5], (byte)5, Intent.ATTACK, ((DamageInfo)this.damage.get(0)).base);
                }
                turn--;
            }
        }
        else
        {
            //号令
            if (firstTurn)
            {
                setMove(MOVES[0], (byte)0, Intent.UNKNOWN);
                hasSpawned = true;
            }
            else
            {
                int roll = MathUtils.random(99);
                if (roll < 50)
                {
                    setMove(MOVES[4], (byte)4, Intent.DEBUFF);
                }
                else
                {
                    setMove(MOVES[5], (byte)5, Intent.ATTACK, ((DamageInfo)this.damage.get(0)).base);
                }
            }

        }
        if (firstTurn)
        {
            firstTurn = false;
        }
    }


    public void changeState(String stateName)
    {
        if (stateName.equals("ATTACK"))
        {
            this.state.setAnimation(0, "Attack", false);
            this.state.addAnimation(0, "Idle", true, 0.0F);
        }
        if (stateName.equals("Order"))
        {
            this.state.setAnimation(0, "Skill_1", false);
            this.state.addAnimation(0, "Idle", true, 0.0F);
        }
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
        if (AbstractDungeon.ascensionLevel >= 13)
        {
            AbstractDungeon.getCurrRoom().addGoldToRewards(100);
        }
        else
        {
            AbstractDungeon.getCurrRoom().addGoldToRewards(100);
        }
        AbstractDungeon.getCurrRoom().addRelicToRewards(AbstractRelic.RelicTier.UNCOMMON);
        super.die();
    }


    protected void spawn(int amount)
    {
        int minionsSpawned = 0;
        for (int i = 0; minionsSpawned < amount && i < this.minions.length; i++)
        {
            if (this.minions[i] == null || this.minions[i].isDeadOrEscaped())
            {
                AbstractMizukiMonster minionToSpawn;
                if (i == 1)
                {
                    minionToSpawn = new NamelessIndependentKnight(POSX[i], POSY[i]);
                }
                else
                {
                    minionToSpawn = new RoarKnightclubTrainee(POSX[i], POSY[i]);
                }

                this.minions[i] = minionToSpawn;
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new SpawnMonsterAction(minionToSpawn, true));

                minionsSpawned++;
            }
        }
    }
}
