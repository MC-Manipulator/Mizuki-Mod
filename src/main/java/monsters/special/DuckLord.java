package monsters.special;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.SetMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import monsters.AbstractMizukiMonster;
import powers.StunMonsterPower;

public class DuckLord extends AbstractMizukiMonster
{
    //鸭爵
    public static final String ID = "Mizuki:DuckLord";

    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Mizuki:DuckLord");

    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    private int turns = 0;

    private int strengthAmount = 0;
    private float timer = 0;
    private float time2 = 0;
    private boolean hasRun = false;
    private boolean startRun = false;
    private boolean leftOrRight = false;
    private boolean stunned = false;

    public DuckLord(float x, float y)
    {
        super(NAME, "Mizuki:DuckLord", 90, 0.0F, 0.0F, 150.0F, 320.0F, null, x, y);
        int hp;
        this.type = EnemyType.NORMAL;

        if (AbstractDungeon.ascensionLevel >= 8)
        {
            hp = 110;
        }
        else
        {
            hp = 102;
        }
        setHp(MathUtils.floor(hp));


        loadAnimation("resources/img/monster/DuckLord/enemy_2001_duckmi.atlas", "resources/img/monster/DuckLord/enemy_2001_duckmi.json", 1.5F);
        this.flipHorizontal = true;
        this.state.addAnimation(0, "Idle", true, 0.0F);
        this.nextMove = 0;
        this.stateData.setMix("Idle", "Die", 0.1F);
        hasRun = false;
        turns = 0;
        time2 = 0;
        timer = 0;
        startRun = false;
        leftOrRight = false;
        this.stunned = false;
        this.strengthAmount = 1;
    }

    public DuckLord(float x, float y, int type)
    {
        this(x, y);
        if (type == 0)
        {
            return;
        }
        else
        {
            int hp;
            if (AbstractDungeon.ascensionLevel >= 8)
            {
                hp = 150;
            }
            else
            {
                hp = 142;
            }
            this.strengthAmount = 2;
            setHp(MathUtils.floor(hp));
        }
    }

    public void takeTurn()
    {
        switch (this.nextMove)
        {
            case 0:
                for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters)
                {
                    if (!m.isDying && !m.isEscaping)
                        AbstractDungeon.actionManager.addToBottom(
                                (AbstractGameAction)new ApplyPowerAction((AbstractCreature)m, (AbstractCreature)this,
                                        (AbstractPower) new StrengthPower((AbstractCreature)m, strengthAmount), strengthAmount));
                }
                break;
            case 1:
                AbstractDungeon.actionManager.addToBottom(new AbstractGameAction()
                {
                    @Override
                    public void update()
                    {
                        state.setAnimation(0, "Run", true);
                        CardCrawlGame.sound.play("MIZUKI_DuckLord_Run", 0.05F);
                        startRun = true;
                        isDone = true;
                    }
                });
                break;
            case 2:
                //AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new EscapeAction(this));

                AbstractDungeon.actionManager.addToBottom(new AbstractGameAction()
                {
                    @Override
                    public void update()
                    {
                        state.setAnimation(0, "Run", true);
                        CardCrawlGame.sound.play("MIZUKI_DuckLord_Run", 0.05F);
                        hideHealthBar();
                        isEscaping = true;
                        timer = 8F;
                        isDone = true;
                    }
                });
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new SetMoveAction(this, (byte)5, AbstractMonster.Intent.ESCAPE));
                break;

        }
        getMove(0);
    }

    protected void getMove(int i)
    {
        turns++;
        if (this.maxHealth == this.currentHealth)
        {
            setMove(MOVES[0], (byte)0, Intent.BUFF);
        }
        else
        {
            if (!this.hasRun)
            {
                setMove(MOVES[1], (byte)1, Intent.UNKNOWN);
                hasRun = true;
            }
            if (this.turns > 5)
            {
                setMove(MOVES[2], (byte)2, Intent.ESCAPE);
            }
        }
    }

    @Override
    public void update()
    {
        super.update();

        if (this.isEscaping)
        {
            if (this.timer != 0.0F)
            {
                this.flipHorizontal = false;
                this.timer -= Gdx.graphics.getDeltaTime();
                this.drawX += Gdx.graphics.getDeltaTime() * 800.0F * Settings.scale;
            }
            if (this.timer < 0.0F)
            {
                this.escaped = true;
                if (AbstractDungeon.getMonsters().areMonstersDead() && !(AbstractDungeon.getCurrRoom()).isBattleOver &&
                        !(AbstractDungeon.getCurrRoom()).cannotLose)
                    AbstractDungeon.getCurrRoom().endBattle();
            }
        }
        else
        {
            if (startRun && !isDying & !hasPower(StunMonsterPower.id))
            {
                if (this.stunned)
                {
                    this.state.setAnimation(0, "Run", true);
                    this.stunned = false;
                }
                if (this.time2 < -0.5)
                {
                    leftOrRight = false;
                }
                if (this.time2 > 0.5)
                {
                    leftOrRight = true;
                }
                if (!leftOrRight)
                {
                    this.flipHorizontal = false;
                    this.time2 += Gdx.graphics.getDeltaTime();
                    this.drawX += Gdx.graphics.getDeltaTime() * 800.0F * Settings.scale;
                }
                if (leftOrRight)
                {
                    this.flipHorizontal = true;
                    this.time2 -= Gdx.graphics.getDeltaTime();
                    this.drawX -= Gdx.graphics.getDeltaTime() * 800.0F * Settings.scale;
                }
            }
            if (!this.stunned && startRun && !isDying & hasPower(StunMonsterPower.id))
            {
                this.state.setAnimation(0, "Idle", true);
                this.stunned = true;
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
                CardCrawlGame.sound.play("MIZUKI_DuckLord_Die", 0.05F);
                isDone = true;
            }
        });
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
            AbstractDungeon.getCurrRoom().addGoldToRewards(150);
        }
        AbstractDungeon.getCurrRoom().addRelicToRewards(AbstractRelic.RelicTier.UNCOMMON);
        super.die();
    }
}