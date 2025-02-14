package monsters.boss.exordium;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FadingPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import helper.EventHelper;
import modcore.MizukiModCore;
import monsters.AbstractMizukiMonster;
import monsters.special.PathshaperFractal;
import powers.*;

public class Pathshaper extends AbstractMizukiMonster
{

    public static final String ID = "Mizuki:Pathshaper";

    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Mizuki:Pathshaper");

    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    public int turn = 0;

    private int summon = 0;

    private int multiplier = 0;

    private int fractalpowerAmount = 0;

    private int damageTime = 0;

    private boolean stunned = false;

    private boolean isFirstTurn = true;

    public int position = 0;

    public Pathshaper(float x, float y)
    {

        super(NAME, "Mizuki:Pathshaper", 90, 0.0F, 0.0F, 250.0F, 320.0F, null, x, y);
        this.type = AbstractMonster.EnemyType.BOSS;

        int dmg1, dmg2, dmg3, multiplier, fractalpowerAmount;

        if (AbstractDungeon.ascensionLevel >= 9)
        {
            setHp(150, 150);
        }
        else
        {
            setHp(140, 140);
        }

        if (AbstractDungeon.ascensionLevel >= 19)
        {
            dmg1 = 10;
            dmg2 = 6;
            dmg3 = 34;
            multiplier = 2;
            fractalpowerAmount = 3;
        }
        else if (AbstractDungeon.ascensionLevel >= 4)
        {
            dmg1 = 10;
            dmg2 = 6;
            dmg3 = 34;
            multiplier = 2;
            fractalpowerAmount = 4;
        }
        else
        {
            dmg1 = 10;
            dmg2 = 6;
            dmg3 = 30;
            multiplier = 2;
            fractalpowerAmount = 4;
        }

        this.damage.add(new DamageInfo((AbstractCreature) this, MathUtils.floor(dmg1)));
        this.damage.add(new DamageInfo((AbstractCreature) this, MathUtils.floor(dmg2)));
        this.damage.add(new DamageInfo((AbstractCreature) this, MathUtils.floor(dmg3)));
        this.multiplier = multiplier;
        this.fractalpowerAmount = fractalpowerAmount;

        //填入spine文件名
        loadAnimation("resources/img/monster/Pathshaper/enemy_2031_syboy.atlas", "resources/img/monster/Pathshaper/enemy_2031_syboy.json", 1.5F);
        this.flipHorizontal = true;
        this.stateData.setMix("Idle", "Die", 0.1F);
        this.state.setTimeScale(1.25f);
        this.state.addAnimation(0, "Idle", true, 0.0F);
        this.nextMove = 0;
    }

    @Override
    public void damage(DamageInfo info)
    {
        super.damage(info);
        AbstractMonster m = this;
        addToBot(new AbstractGameAction()
        {
            @Override
            public void update()
            {
                if (m.currentHealth > 0)
                {
                    if (m.hasPower(FractalPower.id))
                    {
                        damageTime++;
                        m.getPower(FractalPower.id).amount++;
                        if (!(damageTime >= ((FractalPower)(m.getPower(FractalPower.id))).limit))
                        {
                            m.getPower(FractalPower.id).flashWithoutSound();
                        }
                        for (;damageTime >= ((FractalPower)(m.getPower(FractalPower.id))).limit;damageTime -= ((FractalPower)(m.getPower(FractalPower.id))).limit)
                        {
                            m.getPower(FractalPower.id).flash();
                            m.getPower(FractalPower.id).updateDescription();
                            spawn();
                            m.getPower(FractalPower.id).amount -= ((FractalPower)(m.getPower(FractalPower.id))).limit;
                        }
                    }
                }
                isDone = true;
            }
        });

    }

    public void usePreBattleAction()
    {
        if (AbstractDungeon.getCurrRoom() instanceof com.megacrit.cardcrawl.rooms.MonsterRoomBoss)
        {
            CardCrawlGame.music.unsilenceBGM();
            AbstractDungeon.scene.fadeOutAmbiance();
            EventHelper.musicHelper.playBGM("MIZUKI_BOSS1_INTRO", "MIZUKI_BOSS1_LOOP");
        }
        AbstractDungeon.actionManager.addToBottom(
                (AbstractGameAction)new ApplyPowerAction((AbstractCreature)this, (AbstractCreature)this,
                        (AbstractPower)new ErgodicPower((AbstractCreature)this, 0, 2)));
        AbstractDungeon.actionManager.addToBottom(
                (AbstractGameAction)new ApplyPowerAction((AbstractCreature)this, (AbstractCreature)this,
                        (AbstractPower)new FractalPower((AbstractCreature)this, 0, fractalpowerAmount)));
    }

    public void takeTurn()
    {
        AbstractPlayer p = AbstractDungeon.player;
        switch (this.nextMove)
        {
            case 0:
                //开路
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "ATTACK"));
                //addToBot((AbstractGameAction) new DamageAction((AbstractCreature)p, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                attack(this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY);
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new MakeTempCardInDiscardAction((AbstractCard) new Dazed(), 1));
                //1张眩晕塞入弃牌堆
                break;
            case 1:
                //啃食
                for (int i = 0;i < this.multiplier;i++)
                {
                    AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "ATTACK"));
                    //addToBot((AbstractGameAction) new DamageAction((AbstractCreature)p, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                    attack(this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_LIGHT);
                }
                break;
            case 2:
                //汇集
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "SKILL"));
                //addToBot((AbstractGameAction) new DamageAction((AbstractCreature)p, this.damage.get(2), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                attack(this.damage.get(2), AbstractGameAction.AttackEffect.SLASH_HEAVY);

                for (AbstractMonster m1 : (AbstractDungeon.getCurrRoom()).monsters.monsters)
                {
                    if (!m1.isDeadOrEscaped() && m1.id.equals("Mizuki:PathshaperFractal"))
                    {
                        addToBot(new SuicideAction(m1));
                    }
                }
                this.stunned = true;
                break;
            case 3:
                //晕眩
                break;
        }


        getMove(0);
    }

    private void spawn()
    {
        //生成塑路者分形
        //PathshaperFractal fracToSpawn = new PathshaperFractal((this.drawX - Settings.WIDTH * 0.75F) / Settings.xScale - 100, MathUtils.random(-20, 20));
        PathshaperFractal fracToSpawn;
        if (position == 0)
        {
            fracToSpawn = new PathshaperFractal(MathUtils.random(-300, 100), MathUtils.random(-20, 20));
            fracToSpawn.position = 1;
            position = 1;
        }
        else
        {
            fracToSpawn = new PathshaperFractal(MathUtils.random(100, 300), MathUtils.random(-20, 20));
            fracToSpawn.position = 0;
            position = 0;
        }
        this.getPower(ErgodicPower.id).flash();
        fracToSpawn.currentHealth = (int)(((float)this.currentHealth / (float)this.maxHealth) * fracToSpawn.maxHealth);
        if (fracToSpawn.currentHealth <= 0)
        {
            fracToSpawn.currentHealth = 1;
        }
        AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new SpawnMonsterAction(fracToSpawn, true));
        fracToSpawn.createIntent();
    }


    protected void getMove(int i)
    {
        if (isFirstTurn)
        {
            isFirstTurn = false;
            setMove(MOVES[0], (byte)0, Intent.ATTACK_DEBUFF, this.damage.get(0).base);
            return;
        }

        int count = 0;
        for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters)
        {
            if (!m.isDeadOrEscaped() && m.id.equals("Mizuki:PathshaperFractal"))
            {
                count++;
            }
        }
        if (count > 3)
        {
            setMove(MOVES[2], (byte)2, Intent.ATTACK, this.damage.get(2).base);
            return;
        }

        if (stunned)
        {
            setMove(MOVES[3], (byte)3, Intent.STUN);
            stunned = false;
            return;
        }

        int roll = AbstractDungeon.aiRng.random(99);
        if (roll < 50)
        {
            setMove(MOVES[0], (byte)0, Intent.ATTACK_DEBUFF, this.damage.get(0).base);
        }
        else
        {
            setMove(MOVES[1], (byte)1, Intent.ATTACK, this.damage.get(1).base, multiplier, true);
        }
    }

    public void changeState(String stateName)
    {
        if (stateName.equals("ATTACK"))
        {
            this.state.setAnimation(0, "Attack", false);
            this.state.addAnimation(0, "Idle", true, 0.0F);
        }
        if (stateName.equals("SKILL"))
        {
            this.state.setAnimation(0, "Skill", false);
            this.state.addAnimation(0, "Idle", true, 0.0F);
        }
    }

    public void die()
    {
        for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters)
        {
            if (!m.isDeadOrEscaped() && m.id.equals("Mizuki:PathshaperFractal"))
            {
                addToBot(new SuicideAction(m));
            }
        }
        useFastShakeAnimation(5.0F);
        CardCrawlGame.screenShake.rumble(4.0F);
        this.state.setAnimation(0, "Die", false);
        super.die();
        onBossVictoryLogic();
    }

    private void attack(DamageInfo damageInfo, AbstractGameAction.AttackEffect effect)
    {
        AbstractMonster m = this;
        addToBot(new AbstractGameAction()
        {
            boolean justIn = true;

            @Override
            public void update()
            {
                this.target = AbstractDungeon.player;

                if (justIn)
                {
                    this.duration = 0.5F;
                    justIn = false;
                }


                tickDuration();
                if (this.isDone)
                {
                    summon++;
                    m.getPower(ErgodicPower.id).amount++;
                    AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, effect));
                    this.target.damage(damageInfo);
                    if ((m.hasPower(ErgodicPower.id) && summon >= ((ErgodicPower)(m.getPower(ErgodicPower.id))).limit))
                    {
                        //生成塑路者分形
                        spawn();
                        summon -= m.getPower(ErgodicPower.id).amount;
                        m.getPower(ErgodicPower.id).amount -= ((ErgodicPower)(m.getPower(ErgodicPower.id))).limit;
                    }
                    else
                    {
                        m.getPower(ErgodicPower.id).flashWithoutSound();
                    }
                    addToTop((AbstractGameAction) new WaitAction(0.1F));
                    if ((AbstractDungeon.getCurrRoom()).monsters.areMonstersBasicallyDead())
                        AbstractDungeon.actionManager.clearPostCombatActions();
                }
            }
        });
    }
}
