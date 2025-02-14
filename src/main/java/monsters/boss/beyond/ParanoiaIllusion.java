package monsters.boss.beyond;

import Impairment.CorrosionImpairment;
import Impairment.NervousImpairment;
import actions.ApplyImpairmentAction;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.ClearCardQueueAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.VampireDamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import com.megacrit.cardcrawl.vfx.combat.IntimidateEffect;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import helper.EventHelper;
import helper.RejectionHelper;
import monsters.AbstractMizukiMonster;
import monsters.boss.city.SalVientoBishopQuintus;
import monsters.normal.exordium.BoneSeaDrifter;
import monsters.special.*;
import powers.*;

public class ParanoiaIllusion extends AbstractMizukiMonster
{
    //偏执泡影
    public static final String ID = "Mizuki:ParanoiaIllusion";

    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Mizuki:ParanoiaIllusion");

    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    private int stage = 0;

    private int turn = 0;

    private int reviveTurn = 0;

    public int multiplier = 2;

    private boolean down = false;

    private boolean firstTurn = true;

    RegressedBasinSeaReaper mons1;

    RegressedPocketSeaCrawler mons2;

    RegressedNetherseaPredator mons3;

    RegressedNetherseaReefbreaker mons4;

    public float[] POSX = new float[2];

    public float[] POSY = new float[2];

    private AbstractMonster[] fishs = new AbstractMonster[2];

    public ParanoiaIllusion(float x, float y)
    {

        super(NAME, "Mizuki:ParanoiaIllusion", 90, 0.0F, 0.0F, 350.0F, 320.0F, null, x, y);
        this.type = EnemyType.BOSS;

        int dmg1, dmg2, dmg3, dmg4, dmg5;

        if (AbstractDungeon.ascensionLevel >= 9)
        {
            setHp(220);
        }
        else
        {
            setHp(200);
        }

        if (AbstractDungeon.ascensionLevel >= 19)
        {
            dmg1 = 12;
            dmg2 = 17;
        }
        else if (AbstractDungeon.ascensionLevel >= 4)
        {
            dmg1 = 12;
            dmg2 = 17;
        }
        else
        {
            dmg1 = 10;
            dmg2 = 15;
        }

        this.damage.add(new DamageInfo((AbstractCreature) this, MathUtils.floor(dmg1)));
        this.damage.add(new DamageInfo((AbstractCreature) this, MathUtils.floor(dmg2)));

        //填入spine文件名
        loadAnimation("resources/img/monster/ParanoiaIllusion/enemy_2037_sygirl.atlas", "resources/img/monster/ParanoiaIllusion/enemy_2037_sygirl.json", 1F);
        this.flipHorizontal = true;
        this.state.setTimeScale(1.25f);
        this.state.addAnimation(0, "C1_Idle", true, 0.0F);
        this.nextMove = 0;
        this.down = false;
        this.firstTurn = true;
        this.multiplier = 2;
    }

    @Override
    public void update()
    {
        super.update();
        if (!down && !this.hasPower(LowAltitudeHoveringPower.id))
        {
            down = true;
        }
        if (down && this.hasPower(LowAltitudeHoveringPower.id))
        {
            down = false;
        }
    }

    @Override
    public void damage(DamageInfo info)
    {
        super.damage(info);
        if (this.currentHealth <= 0 && !this.halfDead)
        {
            if (stage == 0)
            {
                if (!down)
                {
                    AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "GROUNDED"));
                }
            }

            this.halfDead = true;
            for (AbstractPower p : this.powers)
                p.onDeath();
            for (AbstractRelic r : AbstractDungeon.player.relics)
                r.onMonsterDeath(this);
            addToTop((AbstractGameAction)new ClearCardQueueAction());
            this.powers.removeIf(p -> p.type == AbstractPower.PowerType.DEBUFF || p.ID.equals("Shackled"));
            applyPowers();

            stage++;

            if (stage < 2)
            {
                if (this.nextMove != 5)
                {
                    setMove((byte)5, Intent.BUFF);
                    createIntent();
                    AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new SetMoveAction(this, (byte)5, AbstractMonster.Intent.BUFF));
                }
            }
            else
            {
                (AbstractDungeon.getCurrRoom()).cannotLose = false;
                this.die();
            }
        }
    }

    public void usePreBattleAction()
    {
        POSX[0] = -250;
        POSX[1] = -500;

        POSY[0] = MathUtils.random(-20F,20F);
        POSY[1] = MathUtils.random(-20F,20F);

        (AbstractDungeon.getCurrRoom()).cannotLose = true;
        AbstractDungeon.actionManager.addToBottom(
                (AbstractGameAction)new ApplyPowerAction((AbstractCreature)this, (AbstractCreature)this,
                        (AbstractPower)new LowAltitudeHoveringPower(this, 1, false)));
        AbstractDungeon.actionManager.addToBottom(
                (AbstractGameAction)new ApplyPowerAction((AbstractCreature)this, (AbstractCreature)this,
                        (AbstractPower)new SilencePower(this, 0)));
        if (AbstractDungeon.getCurrRoom() instanceof com.megacrit.cardcrawl.rooms.MonsterRoomBoss)
        {
            CardCrawlGame.music.unsilenceBGM();
            AbstractDungeon.scene.fadeOutAmbiance();
            EventHelper.musicHelper.playBGM("MIZUKI_BOSS3_1_INTRO", "MIZUKI_BOSS3_1_LOOP");
        }
    }

    public void takeTurn()
    {
        AbstractPlayer p = AbstractDungeon.player;
        switch (this.nextMove)
        {
            case 0:
                //苦潮
                for (int i = 0;i < multiplier;i++)
                {
                    AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "ATTACK"));

                    if ((1.9F - multiplier * 0.3F) > 0.4F)
                    {
                        AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new WaitAction((1.9F - multiplier * 0.2F)));
                    }
                    else
                    {
                        AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new WaitAction(0.4F));
                    }

                    if (stage == 0)
                    {
                        AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new DamageAction((AbstractCreature)p, this.damage
                                .get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                    }
                    else
                    {
                        AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new DamageAction((AbstractCreature)p, this.damage
                                .get(1), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                    }
                }

                if (stage == 0)
                {
                    multiplier = 1;
                }
                else
                {
                    multiplier = 1;
                }

                addToBot(new ApplyImpairmentAction(new CorrosionImpairment(), p, 2));
                break;
            case 1:
                //迷茫
                break;
            case 2:
                //起身
                this.powers.removeIf(pr -> pr.type == AbstractPower.PowerType.DEBUFF || pr.ID.equals("Shackled"));
                if (stage == 0)
                {
                    AbstractDungeon.actionManager.addToBottom(
                            (AbstractGameAction)new ApplyPowerAction((AbstractCreature)this, (AbstractCreature)this,
                                    (AbstractPower)new LowAltitudeHoveringPower(this, 1)));
                }
                else
                {
                    AbstractDungeon.actionManager.addToBottom(
                            (AbstractGameAction)new ApplyPowerAction((AbstractCreature)this, (AbstractCreature)this,
                                    (AbstractPower)new LowAltitudeHoveringPower(this, 1)));
                    AbstractDungeon.actionManager.addToBottom(
                            (AbstractGameAction)new ApplyPowerAction((AbstractCreature)this, (AbstractCreature)this,
                                    (AbstractPower)new StrengthPower(this, 2)));
                }
                break;
            case 3:
                //大群的祈望
                spawn(1);
                break;
            case 4:
                //悲歌
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "ATTACK"));
                addToBot(new ApplyImpairmentAction(new CorrosionImpairment(), p, 6));
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new WaitAction(2F));
                break;
            case 5:
                //复活
                addToBot(new WaitAction(1.5f));
                if (down)
                {
                    AbstractDungeon.actionManager.addToBottom(
                            (AbstractGameAction)new ApplyPowerAction((AbstractCreature)this, (AbstractCreature)this,
                                    (AbstractPower)new LowAltitudeHoveringPower(this, 1, false)));
                }
                turn = 0;
                reviveTurn = 0;
                down = false;
                halfDead = false;
                this.firstTurn = true;
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "REVIVE"));
                AbstractDungeon.actionManager.addToBottom(
                        (AbstractGameAction)new HealAction((AbstractCreature)this, (AbstractCreature)this, (int)this.maxHealth));
                break;
        }
        if (down)
            reviveTurn++;
        getMove(0);
        if (!halfDead)
            turn++;
    }

    protected void getMove(int i)
    {
        if (reviveTurn >= 3)
        {
            setMove(MOVES[2], (byte)2, Intent.BUFF);
            reviveTurn = 0;
            return;
        }

        int roll = AbstractDungeon.aiRng.random(99);

        if (stage == 0)
        {
            if (multiplier > 4)
            {
                setMove(MOVES[0], (byte)0, Intent.ATTACK, damage.get(0).base, multiplier, true);
                return;
            }
            if (firstTurn)
            {
                setMove(MOVES[3], (byte)3, Intent.UNKNOWN);
                firstTurn = false;
                return;
            }

            if (roll < 60)
            {
                setMove(MOVES[0], (byte)0, Intent.ATTACK, damage.get(0).base, multiplier, true);
            }
            else if (roll < 70)
            {
                setMove(MOVES[1], (byte)1, Intent.UNKNOWN);
            }
            else
            {
                if (isFull())
                {
                    getMove(0);
                    return;
                }
                setMove(MOVES[3], (byte)3, Intent.UNKNOWN);
            }

            return;
        }
        if (stage == 1)
        {
            if (firstTurn)
            {
                setMove(MOVES[4], (byte)4, Intent.DEBUFF);
                firstTurn = false;
                return;
            }
            if (turn >= 3)
            {
                setMove(MOVES[4], (byte)4, Intent.DEBUFF);
                turn = 0;
                return;
            }

            setMove(MOVES[0], (byte)0, Intent.ATTACK, damage.get(1).base, multiplier, true);
            return;
        }
    }

    public void changeState(String stateName)
    {
        if (stateName.equals("ATTACK"))
        {
            if (!down)
            {
                this.state.setAnimation(0, "C1_Attack", false);
                this.state.addAnimation(0, "C1_Idle", true, 0.0F);
            }
            else
            {
                this.state.setAnimation(0, "C2_Attack", false);
                this.state.addAnimation(0, "C2_Idle", true, 0.0F);
            }
        }

        if (stateName.equals("GROUNDED"))
        {
            this.state.setAnimation(0, "C1_Die", false);
            this.state.addAnimation(0, "C1_Revive1", true, 0.0F);
        }
        if (stateName.equals("REVIVE"))
        {
            this.state.setAnimation(0, "C1_Revive2", false);
            this.state.addAnimation(0, "C1_Idle", true, 0.0F);
        }

        if (stateName.equals("DIE"))
        {
            if (!down)
            {
                this.state.setAnimation(0, "C1_Die", false);
            }
            else
            {
                this.state.setAnimation(0, "C2_Die", false);
            }
        }
    }

    public void addMultiplier()
    {
        this.multiplier++;
        if (this.nextMove == 0)
        {
            if (stage == 0)
            {
                setMove(MOVES[0], (byte)0, Intent.ATTACK, damage.get(0).base, multiplier, true);
            }
            else
            {
                setMove(MOVES[0], (byte)0, Intent.ATTACK, damage.get(1).base, multiplier, true);
            }
            createIntent();
        }
    }

    private boolean isFull()
    {
        int amount = 0;
        for (int i = 0;i < this.fishs.length;i++)
        {
            if (this.fishs[i] != null && !this.fishs[i].isDeadOrEscaped())
            {
                amount++;
            }
        }
        return amount == this.fishs.length;
    }

    public boolean isDown()
    {
        return down;
    }

    protected void spawn(int amount)
    {
        int fishsSpawned = 0;
        for (int i = 0; fishsSpawned < amount && i < this.fishs.length; i++)
        {
            if (this.fishs[i] == null || this.fishs[i].isDeadOrEscaped())
            {
                BoneSeaDrifter fishToSpawn;
                if (i == 1 || i == 3)
                {
                    fishToSpawn = new BoneSeaDrifter(POSX[i], POSY[i]);
                }
                else
                {
                    fishToSpawn = new BoneSeaDrifter(POSX[i], POSY[i]);
                }

                fishToSpawn.drawX = this.drawX + POSX[i] * Settings.xScale;
                fishToSpawn.drawY = AbstractDungeon.floorY + POSY[i] * Settings.yScale;

                this.fishs[i] = fishToSpawn;
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new SpawnMonsterAction(fishToSpawn, true));
                AbstractDungeon.actionManager.addToBottom(
                        (AbstractGameAction)new ApplyPowerAction((AbstractCreature)fishToSpawn, (AbstractCreature)fishToSpawn,
                                (AbstractPower)new StrengthPower((AbstractCreature)fishToSpawn, 2)));
                addToBot(new GainBlockAction(fishToSpawn, 10));
                fishsSpawned++;
            }
        }
    }

    public void die()
    {
        if (!(AbstractDungeon.getCurrRoom()).cannotLose)
        {
            for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters)
            {
                if (!m.isDeadOrEscaped())
                {
                    addToBot(new SuicideAction(m));
                }
            }

            isDying = true;
            useFastShakeAnimation(5.0F);
            CardCrawlGame.screenShake.rumble(4.0F);
            changeState("DIE");
            super.die();
            onBossVictoryLogic();
            onFinalBossVictoryLogic();
        }
    }
}