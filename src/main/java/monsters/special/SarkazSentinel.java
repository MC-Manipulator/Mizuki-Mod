package monsters.special;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.BlockedWordEffect;
import com.megacrit.cardcrawl.vfx.combat.DeckPoofEffect;
import com.megacrit.cardcrawl.vfx.combat.HbBlockBrokenEffect;
import com.megacrit.cardcrawl.vfx.combat.StrikeEffect;
import helper.EventHelper;
import monsters.AbstractMizukiMonster;
import powers.AlertPower;

import java.util.Iterator;

public class SarkazSentinel extends AbstractMizukiMonster
{
    //萨卡兹哨兵
    //在玩家回合，每过1s，积攒一层警戒值
    //当警戒值达到100时，结束玩家回合，移除宝箱，并召唤萨卡兹
    //当受到伤害时，直接结束玩家回合，移除宝箱，并召唤萨卡兹
    //将一张 逃离 放入玩家手中，在警戒值未达到100之前，玩家可以打出逃离并离开战斗
    //萨卡兹哨兵每回合会为萨卡兹增加1点力量
    //当萨卡兹全部死亡时，逃离战斗


    public static final String ID = "Mizuki:SarkazSentinel";

    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Mizuki:SarkazSentinel");

    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    private float timer = 0;

    private float timer2 = 1;

    public boolean hasAlert = false;

    public boolean delayDeath = false;

    public float[] POSX = new float[2];

    public float[] POSY = new float[2];

    private AbstractMonster[] minions = new AbstractMonster[2];

    public SarkazSentinel(float x, float y)
    {

        super(NAME, "Mizuki:SarkazSentinel", 90, 0.0F, 0.0F, 150.0F, 320.0F, null, x, y);
        this.type = AbstractMonster.EnemyType.NORMAL;


        //生命值应当较高
        if (AbstractDungeon.ascensionLevel >= 7)
        {
            setHp(80, 83);
        }
        else
        {
            setHp(78, 79);
        }

        if (AbstractDungeon.ascensionLevel >= 17)
        {

        }
        else if (AbstractDungeon.ascensionLevel >= 2)
        {

        }
        else
        {

        }

        //填入spine
        loadAnimation("resources/img/monster/SarkazSentinel/enemy_1073_dscout.atlas", "resources/img/monster/SarkazSentinel/enemy_1073_dscout.json", 1.5F);
        this.flipHorizontal = true;
        this.stateData.setMix("Idle", "Die", 0.1F);
        this.state.setAnimation(0, "Idle", true);
        this.nextMove = 0;
    }

    public void usePreBattleAction()
    {
        POSX[0] = -250;
        POSX[1] = 250;

        POSY[0] = MathUtils.random(-20F,20F);
        POSY[1] = MathUtils.random(-20F,20F);
        AbstractDungeon.actionManager.addToBottom(
                        (AbstractGameAction)new ApplyPowerAction((AbstractCreature)this, (AbstractCreature)this,
                                (AbstractPower)new AlertPower((AbstractCreature)this, 1), 1));
        spawn(2);
    }

    @Override
    public void update()
    {
        if (EventHelper.isPlayerTurn && !hasAlert)
        {
            if (this.timer2 != 0.0F)
            {
                timer2 -= Gdx.graphics.getDeltaTime();
            }
            if (this.timer2 < 0.0F)
            {
                if (this.hasPower(AlertPower.id))
                {
                    this.getPower(AlertPower.id).amount++;
                    this.getPower(AlertPower.id).flashWithoutSound();
                    //AbstractDungeon.actionManager.addToBottom(
                    //        (AbstractGameAction)new ApplyPowerAction((AbstractCreature)this, (AbstractCreature)this,
                    //                (AbstractPower)new AlertPower((AbstractCreature)this, 1), 1));
                }
                timer2 = 1;
            }
        }
        else
        {
            timer2 = 1;
        }
        if (this.hasPower(AlertPower.id))
        {
            AlertPower ap = (AlertPower) this.getPower(AlertPower.id);
            if (ap.amount >= 50)
            {
                addToBot(new AbstractGameAction()
                {
                    @Override
                    public void update()
                    {
                        Alert();
                        isDone = true;
                    }
                });
            }
        }
        super.update();
        if (this.isEscaping)
        {
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
    }

    public void Alert()
    {
        if (hasAlert)
        {
            return;
        }
        CardCrawlGame.sound.play("STANCE_ENTER_WRATH");
        AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this, this, AlertPower.id));
        hasAlert = true;
        ((Chest) minions[0]).alert = true;
        ((Chest) minions[1]).alert = true;
        minions[0].die();
        minions[0].hideHealthBar();
        minions[1].die();
        minions[1].hideHealthBar();
        minions = new AbstractMonster[2];
        AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.RED, true));
        AbstractDungeon.actionManager.addToTop((AbstractGameAction)new ChangeStateAction(this, "ALERT"));
        spawn(2);
        ((SarkazBladeweaver)minions[0]).getMove(0);
        ((SarkazGrudgebearer)minions[1]).getMove(0);
        minions[0].createIntent();
        minions[1].createIntent();
        setMove(MOVES[1], (byte)1, Intent.BUFF);
        createIntent();
    }

    @Override
    public void damage(DamageInfo info)
    {
        if (info.output > 0 && this.hasPower("IntangiblePlayer"))
        {
            info.output = 1;
        }

        int damageAmount = info.output;
        if (!this.isDying && !this.isEscaping)
        {
            if (damageAmount < 0)
            {
                damageAmount = 0;
            }

            boolean hadBlock = true;
            if (this.currentBlock == 0)
            {
                hadBlock = false;
            }

            boolean weakenedToZero = damageAmount == 0;
            damageAmount = this.decrementBlock(info, damageAmount);
            Iterator var5;
            AbstractRelic r;
            if (info.owner == AbstractDungeon.player)
            {
                for(var5 = AbstractDungeon.player.relics.iterator(); var5.hasNext(); damageAmount = r.onAttackToChangeDamage(info, damageAmount))
                {
                    r = (AbstractRelic)var5.next();
                }
            }

            AbstractPower p;
            if (info.owner != null)
            {
                for(var5 = info.owner.powers.iterator(); var5.hasNext(); damageAmount = p.onAttackToChangeDamage(info, damageAmount))
                {
                    p = (AbstractPower)var5.next();
                }
            }

            for(var5 = this.powers.iterator(); var5.hasNext(); damageAmount = p.onAttackedToChangeDamage(info, damageAmount))
            {
                p = (AbstractPower)var5.next();
            }

            if (info.owner == AbstractDungeon.player)
            {
                var5 = AbstractDungeon.player.relics.iterator();

                while(var5.hasNext())
                {
                    r = (AbstractRelic)var5.next();
                    r.onAttack(info, damageAmount, this);
                }
            }

            var5 = this.powers.iterator();

            while(var5.hasNext())
            {
                p = (AbstractPower)var5.next();
                p.wasHPLost(info, damageAmount);
            }

            if (info.owner != null)
            {
                var5 = info.owner.powers.iterator();

                while(var5.hasNext())
                {
                    p = (AbstractPower)var5.next();
                    p.onAttack(info, damageAmount, this);
                }
            }

            for(var5 = this.powers.iterator(); var5.hasNext(); damageAmount = p.onAttacked(info, damageAmount))
            {
                p = (AbstractPower)var5.next();
            }

            this.lastDamageTaken = Math.min(damageAmount, this.currentHealth);
            boolean probablyInstantKill = this.currentHealth == 0;
            if (damageAmount > 0)
            {
                if (info.owner != this)
                {
                    this.useStaggerAnimation();
                }

                if (damageAmount >= 99 && !CardCrawlGame.overkill)
                {
                    CardCrawlGame.overkill = true;
                }

                this.currentHealth -= damageAmount;
                if (!probablyInstantKill)
                {
                    AbstractDungeon.effectList.add(new StrikeEffect(this, this.hb.cX, this.hb.cY, damageAmount));
                }

                if (this.currentHealth <= 0)
                {
                    if (!hasAlert)
                    {
                        delayDeath = true;
                        this.currentHealth = 1;
                    }
                    else
                    {
                        this.currentHealth = 0;
                    }
                }

                if (!hasAlert)
                {
                    addToBot(new AbstractGameAction()
                    {
                        @Override
                        public void update()
                        {
                            Alert();
                            isDone = true;
                        }
                    });
                }


                this.healthBarUpdatedEvent();
            }
            else if (!probablyInstantKill)
            {
                if (weakenedToZero && this.currentBlock == 0)
                {
                    if (hadBlock)
                    {
                        AbstractDungeon.effectList.add(new BlockedWordEffect(this, this.hb.cX, this.hb.cY, TEXT[30]));
                    }
                    else
                    {
                        AbstractDungeon.effectList.add(new StrikeEffect(this, this.hb.cX, this.hb.cY, 0));
                    }
                }
                else if (Settings.SHOW_DMG_BLOCK)
                {
                    AbstractDungeon.effectList.add(new BlockedWordEffect(this, this.hb.cX, this.hb.cY, TEXT[30]));
                }
            }

            if (this.currentHealth <= 0)
            {
                this.die();
                if (AbstractDungeon.getMonsters().areMonstersBasicallyDead())
                {
                    AbstractDungeon.actionManager.cleanCardQueue();
                    AbstractDungeon.effectList.add(new DeckPoofEffect(64.0F * Settings.scale, 64.0F * Settings.scale, true));
                    AbstractDungeon.effectList.add(new DeckPoofEffect((float)Settings.WIDTH - 64.0F * Settings.scale, 64.0F * Settings.scale, false));
                    AbstractDungeon.overlayMenu.hideCombatPanels();
                }

                if (this.currentBlock > 0)
                {
                    this.loseBlock();
                    AbstractDungeon.effectList.add(new HbBlockBrokenEffect(this.hb.cX - this.hb.width / 2.0F + BLOCK_ICON_X, this.hb.cY - this.hb.height / 2.0F + BLOCK_ICON_Y));
                }
            }
        }
    }

    public void takeTurn()
    {
        AbstractPlayer p = AbstractDungeon.player;

        switch (this.nextMove)
        {
            case 0:
                //巡逻
                break;
            case 1:
                //警戒
                for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters)
                {
                    if (!m.isDeadOrEscaped() && m != this)
                    {
                        AbstractDungeon.actionManager.addToBottom(
                                (AbstractGameAction)new ApplyPowerAction((AbstractCreature)m, (AbstractCreature)this,
                                        (AbstractPower)new StrengthPower((AbstractCreature)m, 1), 1));
                    }
                }
                break;
            case 2:
                //逃离
                AbstractDungeon.actionManager.addToBottom(new AbstractGameAction()
                {
                    @Override
                    public void update()
                    {
                        changeState("MOVE");
                        hideHealthBar();
                        isEscaping = true;
                        timer = 4F;
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

        if (this.minions[0] != null && this.minions[1] != null)
        {
            if (this.minions[0].isDead && this.minions[1].isDead)
            {
                //2号技能：逃离
                setMove(MOVES[2], (byte)2, Intent.ESCAPE);
                return;
            }
        }
        if (hasAlert)
        {
            //1号技能：警戒
            setMove(MOVES[1], (byte)1, Intent.BUFF);
            return;
        }
        //0号技能：巡逻
        setMove(MOVES[0], (byte)0, Intent.UNKNOWN);
    }

    public void changeState(String stateName)
    {
        if (stateName.equals("ALERT"))
        {
            //警戒动作
            this.state.setAnimation(0, "Skill_Begin", false);
            this.state.addAnimation(0, "Skill_Idle", true, 0.0F);
        }

        if (stateName.equals("MOVE"))
        {
            if (hasAlert)
            {
                //移动动作
                this.state.setAnimation(0, "Skill_Move", true);
            }
            else
            {
                this.state.setAnimation(0, "Move", true);
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
                if (hasAlert)
                {
                    if (i == 1)
                    {
                        minionToSpawn = new SarkazGrudgebearer(POSX[i], POSY[i]);
                    }
                    else
                    {
                        minionToSpawn = new SarkazBladeweaver(POSX[i], POSY[i]);
                    }
                }
                else
                {
                    if (i == 1)
                    {
                        minionToSpawn = new Chest(POSX[i], POSY[i]);
                    }
                    else
                    {
                        minionToSpawn = new Chest(POSX[i], POSY[i]);
                    }
                }

                minionToSpawn.drawX = this.drawX + POSX[i] * Settings.xScale;
                minionToSpawn.drawY = AbstractDungeon.floorY + POSY[i] * Settings.yScale;

                this.minions[i] = minionToSpawn;
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new SpawnMonsterAction(minionToSpawn, false));

                minionsSpawned++;
            }
        }
        if (delayDeath)
        {
            this.currentHealth = 0;
            die();
            if (AbstractDungeon.getMonsters().areMonstersBasicallyDead())
            {
                AbstractDungeon.actionManager.cleanCardQueue();
                AbstractDungeon.effectList.add(new DeckPoofEffect(64.0F * Settings.scale, 64.0F * Settings.scale, true));
                AbstractDungeon.effectList.add(new DeckPoofEffect((float)Settings.WIDTH - 64.0F * Settings.scale, 64.0F * Settings.scale, false));
                AbstractDungeon.overlayMenu.hideCombatPanels();
            }

            if (this.currentBlock > 0)
            {
                this.loseBlock();
                AbstractDungeon.effectList.add(new HbBlockBrokenEffect(this.hb.cX - this.hb.width / 2.0F + BLOCK_ICON_X, this.hb.cY - this.hb.height / 2.0F + BLOCK_ICON_Y));
            }
        }
    }
}
