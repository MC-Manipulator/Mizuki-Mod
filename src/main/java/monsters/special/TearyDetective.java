package monsters.special;

import basemod.helpers.VfxBuilder;
import characters.Mizuki;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.EscapeAction;
import com.megacrit.cardcrawl.actions.common.SetMoveAction;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import helper.ImpairementManager;
import modcore.MizukiModCore;
import monsters.AbstractMizukiMonster;
import powers.StealthPower;
import powers.StunMonsterPower;

import java.util.ArrayList;

public class TearyDetective extends AbstractMizukiMonster
{
    //流泪小子
    public static final String ID = "Mizuki:TearyDetective";

    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Mizuki:TearyDetective");

    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    private int mtype = 0;
    private int turns = 0;

    private float timer = 0;

    private boolean chat = false;

    public TearyDetective(float x, float y, int type)
    {
        super(NAME, "Mizuki:TearyDetective", 60, 0.0F, 0.0F, 150.0F, 320.0F, null, x, y);
        int dmg1, hp;
        this.mtype = type;
        this.type = EnemyType.NORMAL;

        if (mtype == 0)
        {
            if (AbstractDungeon.ascensionLevel >= 17)
            {
                dmg1 = 16;
            }
            else if (AbstractDungeon.ascensionLevel >= 2)
            {
                dmg1 = 16;
            }
            else
            {
                dmg1 = 12;
            }

            this.damage.add(new DamageInfo((AbstractCreature) this, MathUtils.floor(dmg1)));

            if (AbstractDungeon.ascensionLevel >= 7)
            {
                hp = 64;
            }
            else
            {
                hp = 60;
            }

            setHp(MathUtils.floor(hp));
        }
        if (mtype == 1)
        {
            if (AbstractDungeon.ascensionLevel >= 17)
            {
                dmg1 = 20;
            }
            else if (AbstractDungeon.ascensionLevel >= 2)
            {
                dmg1 = 20;
            }
            else
            {
                dmg1 = 16;
            }

            this.damage.add(new DamageInfo((AbstractCreature) this, MathUtils.floor(dmg1)));

            if (AbstractDungeon.ascensionLevel >= 7)
            {
                hp = 140;
            }
            else
            {
                hp = 132;
            }

            setHp(MathUtils.floor(hp));
        }
        if (mtype == 2)
        {
            if (AbstractDungeon.ascensionLevel >= 17)
            {
                dmg1 = 23;
            }
            else if (AbstractDungeon.ascensionLevel >= 2)
            {
                dmg1 = 23;
            }
            else
            {
                dmg1 = 20;
            }

            this.damage.add(new DamageInfo((AbstractCreature) this, MathUtils.floor(dmg1)));

            if (AbstractDungeon.ascensionLevel >= 7)
            {
                hp = 170;
            }
            else
            {
                hp = 162;
            }

            setHp(MathUtils.floor(hp));
        }

        loadAnimation("resources/img/monster/TearyDetective/enemy_2034_sythef.atlas", "resources/img/monster/TearyDetective/enemy_2034_sythef.json", 1.5F);
        this.flipHorizontal = true;
        this.state.addAnimation(0, "Idle", true, 0.0F);
        this.nextMove = -1;
        this.turns = 0;
        this.stateData.setMix("Idle", "Die", 0.1F);
        chat = true;
    }
    public TearyDetective(float x, float y, int type, boolean chat)
    {
        this(x, y, type);
        this.chat = chat;
    }


    public void takeTurn()
    {
        AbstractPlayer p = AbstractDungeon.player;
        switch (this.nextMove)
        {
            case 0:
                cry();
                break;
            case 1:
                AbstractDungeon.actionManager.addToBottom(
                        (AbstractGameAction)new ApplyPowerAction((AbstractCreature)this, (AbstractCreature)this,
                                (AbstractPower)new StealthPower((AbstractCreature)this, 1), 1));
                if (chat)
                    addToBot((AbstractGameAction)new TalkAction(true, DIALOG[6], 2.0F, 2.0F));
                break;
            case 2:
                cry();
                MizukiModCore.logger.info("Cry");
                break;
            case 3:
                AbstractDungeon.actionManager.addToBottom(
                        (AbstractGameAction)new ApplyPowerAction((AbstractCreature)this, (AbstractCreature)this,
                                (AbstractPower)new StealthPower((AbstractCreature)this, 1), 1));
                break;
            case 4:
                this.state.setAnimation(0, "Attack", false);
                this.state.addAnimation(0, "Idle", true, 0.0F);
                addToBot((AbstractGameAction) new DamageAction((AbstractCreature)p, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                AbstractDungeon.actionManager.addToBottom(
                        (AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)this,
                                (AbstractPower)new StunMonsterPower((AbstractCreature)p, 1), 1));
                getMove(1);
                return;
            case 5:

                this.state.setAnimation(0, "Move", true);
                //AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new EscapeAction(this));

                AbstractDungeon.actionManager.addToBottom(new AbstractGameAction()
                {
                    @Override
                    public void update()
                    {

                        hideHealthBar();
                        isEscaping = true;
                        timer = 3F;
                        isDone = true;
                    }
                });
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new SetMoveAction(this, (byte)5, AbstractMonster.Intent.ESCAPE));
                break;
        }
        getMove(0);
    }

    @Override
    public void update()
    {
        super.update();
        if (this.timer != 0.0F)
        {
            this.flipHorizontal = false;
            this.timer -= Gdx.graphics.getDeltaTime();
            this.drawX += Gdx.graphics.getDeltaTime() * 400.0F * Settings.scale;
        }
        if (this.timer < 0.0F)
        {
            this.escaped = true;
            if (AbstractDungeon.getMonsters().areMonstersDead() && !(AbstractDungeon.getCurrRoom()).isBattleOver &&
                    !(AbstractDungeon.getCurrRoom()).cannotLose)
                AbstractDungeon.getCurrRoom().endBattle();
        }
    }

    private void cry()
    {
        int ran = MathUtils.random(5);
        addToBot((AbstractGameAction)new TalkAction((AbstractCreature)this, DIALOG[ran], 0.3F, 3.0F));
    }

    protected void getMove(int i)
    {
        turns++;
        if (turns >= 10)
        {
            //逃离
            setMove(MOVES[3], (byte)5, Intent.ESCAPE);
            return;
        }
        //0为固定行动1，呜呜呜
        //1为固定行动2，侦探工作
        if (this.nextMove == -1)
        {
            setMove(MOVES[0], (byte)++this.nextMove, Intent.UNKNOWN);
            return;
        }
        if (this.nextMove == 0)
        {
            setMove(MOVES[1], (byte)++this.nextMove, Intent.BUFF);
            return;
        }

        int ran = AbstractDungeon.aiRng.random(99);
        if (i == 0)
        {
            if (ran <= 20)
            {
                //呜呜呜
                setMove(MOVES[0], (byte)2, Intent.UNKNOWN);
            }
            else if (ran <= 55)
            {
                //侦探工作
                setMove(MOVES[1], (byte)3, Intent.BUFF);
            }
            else
            {
                //踢一脚
                setMove(MOVES[2], (byte)4, Intent.ATTACK_DEBUFF, ((DamageInfo)this.damage.get(0)).base);
            }
        }
        else
        {
            if (ran <= 50)
            {
                //呜呜呜
                setMove(MOVES[0], (byte)2, Intent.UNKNOWN);
            }
            else
            {
                //侦探工作
                setMove(MOVES[1], (byte)3, Intent.BUFF);
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
        if (this.mtype == 0)
        {
            if (AbstractDungeon.ascensionLevel >= 13)
            {
                AbstractDungeon.getCurrRoom().addGoldToRewards(50);
            }
            else
            {
                AbstractDungeon.getCurrRoom().addGoldToRewards(100);
            }
            AbstractDungeon.getCurrRoom().addRelicToRewards(AbstractRelic.RelicTier.UNCOMMON);
        }
        else
        {
            if (AbstractDungeon.ascensionLevel >= 13)
            {
                AbstractDungeon.getCurrRoom().addGoldToRewards(100);
            }
            else
            {
                AbstractDungeon.getCurrRoom().addGoldToRewards(150);
            }
            AbstractDungeon.getCurrRoom().addRelicToRewards(AbstractRelic.RelicTier.RARE);
        }
        super.die();
    }
}