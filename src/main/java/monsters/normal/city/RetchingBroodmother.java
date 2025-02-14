package monsters.normal.city;

import Impairment.CorrosionImpairment;
import actions.ApplyImpairmentAction;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import monsters.AbstractMizukiMonster;
import monsters.special.BalefulBroodling;
import powers.DyingPower;

public class RetchingBroodmother extends AbstractMizukiMonster
{
    //投嗣育母
    public static final String ID = "Mizuki:RetchingBroodmother";

    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Mizuki:RetchingBroodmother");

    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    private boolean hasMultiplyBreeded;

    private boolean firstTurn;

    public float[] POSX = new float[4];

    public float[] POSY = new float[4];

    private int ballsPerSpawn;

    private AbstractMonster[] balls = new AbstractMonster[4];

    public RetchingBroodmother(float x, float y)
    {
        super(NAME, "Mizuki:RetchingBroodmother", 90, 0.0F, 0.0F, 250.0F, 280.0F, null, x, y);
        this.type = EnemyType.NORMAL;


        int dmg1, dmg2;

        if (AbstractDungeon.ascensionLevel >= 7)
        {
            setHp(78, 82);
        }
        else
        {
            setHp(75, 79);
        }

        if (AbstractDungeon.ascensionLevel >= 17)
        {
            dmg1 = 7;
        }
        else if (AbstractDungeon.ascensionLevel >= 2)
        {
            dmg1 = 7;
        }
        else
        {
            dmg1 = 6;
        }

        if (AbstractDungeon.ascensionLevel >= 17)
        {
            dmg2 = 21;
        }
        else if (AbstractDungeon.ascensionLevel >= 2)
        {
            dmg2 = 21;
        }
        else
        {
            dmg2 = 18;
        }
        this.damage.add(new DamageInfo((AbstractCreature) this, MathUtils.floor(dmg1)));
        this.damage.add(new DamageInfo((AbstractCreature) this, MathUtils.floor(dmg2)));


        loadAnimation("resources/img/monster/RetchingBroodmother/enemy_2023_sypult.atlas", "resources/img/monster/RetchingBroodmother/enemy_2023_sypult.json", 1.5F);
        this.flipHorizontal = true;
        this.state.addAnimation(0, "Idle", true, 0.0F);
        this.nextMove = 0;
        this.stateData.setMix("Idle", "Die", 0.1F);
        this.hasMultiplyBreeded = false;
        this.firstTurn = true;
    }

    public void usePreBattleAction()
    {
        POSX[0] = 100;
        POSX[1] = -100;
        POSX[2] = 200;
        POSX[3] = -200;

        POSY[0] = MathUtils.random(-20F,20F);
        POSY[1] = MathUtils.random(-20F,20F);
        POSY[2] = MathUtils.random(-20F,20F);
        POSY[3] = MathUtils.random(-20F,20F);
    }

    public void takeTurn()
    {
        AbstractCreature c = this;
        AbstractPlayer p = AbstractDungeon.player;
        switch (this.nextMove)
        {
            case 0:
                this.state.setAnimation(0, "Attack", false);
                this.state.addAnimation(0, "Attack", false, 0.0F);
                this.state.addAnimation(0, "Idle", true, 0.0F);
                addToBot((AbstractGameAction) new DamageAction((AbstractCreature)p, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                addToBot((AbstractGameAction) new DamageAction((AbstractCreature)p, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                addToBot(new AbstractGameAction()
                {
                    @Override
                    public void update()
                    {
                        if (!c.isDeadOrEscaped())
                        {
                            spawn(2);
                        }
                        isDone = true;
                    }
                });
                hasMultiplyBreeded = true;
                break;
            case 1:
                int count = 0;
                for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters)
                {
                    if (!m.isDeadOrEscaped() && m.id.equals("Mizuki:BalefulBroodling"))
                    {
                        count++;
                    }
                }
                if (count != 0)
                {
                    for (int i = 0;i < count;i++)
                    {
                        AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new MakeTempCardInDiscardAction((AbstractCard) new Slimed(), 1));
                    }
                }
                else
                {
                    AbstractDungeon.actionManager.addToBottom(
                            (AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)this,
                                    (AbstractPower)new WeakPower((AbstractCreature)p, 1, true), 1));
                    addToBot(new ApplyImpairmentAction(new CorrosionImpairment(), p, 2));
                }
                hasMultiplyBreeded = false;
                break;
            case 2:
                this.state.setAnimation(0, "Attack", false);
                this.state.addAnimation(0, "Idle", true, 0.0F);
                addToBot((AbstractGameAction) new DamageAction((AbstractCreature)p, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                addToBot(new AbstractGameAction()
                {
                    @Override
                    public void update()
                    {
                        if (!c.isDeadOrEscaped())
                        {
                            spawn(1);
                        }
                        isDone = true;
                    }
                });
                hasMultiplyBreeded = false;
                break;
        }
        getMove(0);
    }

    protected void spawn(int amount)
    {
        int ballsSpawned = 0;
        for (int i = 0; ballsSpawned < amount && i < this.balls.length; i++)
        {
            if (this.balls[i] == null || this.balls[i].isDeadOrEscaped())
            {
                BalefulBroodling ballToSpawn;
                if (i == 1 || i == 3)
                {
                    ballToSpawn = new BalefulBroodling(POSX[i], POSY[i], false);
                }
                else
                {
                    ballToSpawn = new BalefulBroodling(POSX[i], POSY[i]);
                }

                ballToSpawn.drawX = (float) Settings.WIDTH * 0.25F + POSX[i] * Settings.xScale;
                ballToSpawn.drawY = AbstractDungeon.floorY + POSY[i] * Settings.yScale;

                this.balls[i] = ballToSpawn;
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new SpawnMonsterAction(ballToSpawn, true));
                AbstractDungeon.actionManager.addToBottom(
                        (AbstractGameAction)new ApplyPowerAction((AbstractCreature)ballToSpawn, (AbstractCreature)ballToSpawn,
                                (AbstractPower)new DyingPower((AbstractCreature)ballToSpawn, 1)));
                ballsSpawned++;
            }
        }
    }

    protected void getMove(int i)
    {
        if (!firstTurn)
        {
            setMove(MOVES[0], (byte)0, AbstractMonster.Intent.ATTACK_BUFF, ((DamageInfo)this.damage.get(0)).base,2, true);
            firstTurn = false;
        }
        else
        {
            if (hasMultiplyBreeded)
            {
                int roll = AbstractDungeon.aiRng.random(99);
                if (roll < 50)
                {
                    setMove(MOVES[1], (byte)1, Intent.DEBUFF);
                }
                else
                {
                    setMove(MOVES[2], (byte)2, AbstractMonster.Intent.ATTACK_BUFF, ((DamageInfo)this.damage.get(1)).base);
                }
            }
            else
            {
                int count = 0;
                for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters)
                {
                    if (!m.isDeadOrEscaped() && m.id.equals("Mizuki:BalefulBroodling"))
                    {
                        count++;
                    }
                }
                if (count > 0)
                {
                    int roll = AbstractDungeon.aiRng.random(99);
                    if (roll < 40)
                    {
                        setMove(MOVES[0], (byte)0, Intent.ATTACK_BUFF, ((DamageInfo)this.damage.get(0)).base,2, true);
                    }
                    else if (roll < 75)
                    {
                        setMove(MOVES[1], (byte)1, Intent.DEBUFF);
                    }
                    else
                    {
                        setMove(MOVES[2], (byte)2, AbstractMonster.Intent.ATTACK_BUFF, ((DamageInfo)this.damage.get(1)).base);
                    }
                }
                else
                {
                    int roll = AbstractDungeon.aiRng.random(99);
                    if (roll < 60)
                    {
                        setMove(MOVES[0], (byte)0, Intent.ATTACK_BUFF, ((DamageInfo)this.damage.get(0)).base,2, true);
                    }
                    else
                    {
                        setMove(MOVES[2], (byte)2, AbstractMonster.Intent.ATTACK_BUFF, ((DamageInfo)this.damage.get(1)).base);
                    }
                }
            }
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
        for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters)
        {
            if (!m.isDeadOrEscaped() && m.id.equals("Mizuki:BalefulBroodling"))
            {
                addToBot(new SuicideAction(m));
            }
        }
        super.die();
    }
}
