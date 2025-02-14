package monsters.boss.city;

import Impairment.CorrosionImpairment;
import Impairment.NervousImpairment;
import actions.ApplyImpairmentAction;
import basemod.abstracts.AbstractCardModifier;
import basemod.cardmods.EtherealMod;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.ClearCardQueueAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.VampireDamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
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
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.vfx.combat.IntimidateEffect;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import helper.EventHelper;
import helper.RejectionHelper;
import monsters.AbstractMizukiMonster;
import monsters.special.RegressedBasinSeaReaper;
import monsters.special.RegressedNetherseaPredator;
import monsters.special.RegressedNetherseaReefbreaker;
import monsters.special.RegressedPocketSeaCrawler;
import powers.*;

public class TheEndspeaker extends AbstractMizukiMonster
{
    //屠谕者
    public static final String ID = "Mizuki:TheEndspeaker";

    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Mizuki:TheEndspeaker");

    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    private int stage = 0;

    private int turn = 0;

    RegressedBasinSeaReaper mons1;

    RegressedPocketSeaCrawler mons2;

    RegressedNetherseaPredator mons3;

    RegressedNetherseaReefbreaker mons4;

    public TheEndspeaker(float x, float y)
    {

        super(NAME, "Mizuki:TheEndspeaker", 90, 0.0F, 0.0F, 150.0F, 320.0F, null, x, y);
        this.type = EnemyType.BOSS;

        int dmg1, dmg2, dmg3, dmg4, dmg5;

        if (AbstractDungeon.ascensionLevel >= 9)
        {
            setHp(80);
        }
        else
        {
            setHp(70);
        }

        if (AbstractDungeon.ascensionLevel >= 19)
        {
            dmg1 = 12;
            dmg2 = 12;
            dmg3 = 10;
            dmg4 = 5;
            dmg5 = 27;
        }
        else if (AbstractDungeon.ascensionLevel >= 4)
        {
            dmg1 = 12;
            dmg2 = 12;
            dmg3 = 10;
            dmg4 = 5;
            dmg5 = 27;
        }
        else
        {
            dmg1 = 10;
            dmg2 = 12;
            dmg3 = 10;
            dmg4 = 5;
            dmg5 = 25;
        }

        this.damage.add(new DamageInfo((AbstractCreature) this, MathUtils.floor(dmg1)));
        this.damage.add(new DamageInfo((AbstractCreature) this, MathUtils.floor(dmg2)));
        this.damage.add(new DamageInfo((AbstractCreature) this, MathUtils.floor(dmg3)));
        this.damage.add(new DamageInfo((AbstractCreature) this, MathUtils.floor(dmg4)));
        this.damage.add(new DamageInfo((AbstractCreature) this, MathUtils.floor(dmg5)));

        //填入spine文件名
        loadAnimation("resources/img/monster/TheEndspeaker/enemy_1529_dsdevr.atlas", "resources/img/monster/TheEndspeaker/enemy_1529_dsdevr.json", 1.5F);
        this.flipHorizontal = true;
        this.state.setTimeScale(1.25f);
        this.state.addAnimation(0, "Idle_1", true, 0.0F);
        this.nextMove = 0;
    }

    @Override
    public void damage(DamageInfo info)
    {
        super.damage(info);
        if (this.currentHealth <= 0 && !this.halfDead)
        {
            if (stage == 0)
            {
                mons1 = new RegressedBasinSeaReaper(200, 0);
                mons2 = new RegressedPocketSeaCrawler(-200, 0);
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new SpawnMonsterAction(mons1, true));
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new SpawnMonsterAction(mons2, true));
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "DIE1"));
            }
            if (stage == 1)
            {
                this.powers.removeIf(p -> p.ID.equals(PocketSeaStingPower.id));
                this.powers.removeIf(p -> p.ID.equals(DreamInterruptedPower.id));
                this.powers.removeIf(p -> p.ID.equals(ErosionPower.id));
                mons3 = new RegressedNetherseaPredator(200, 0);
                mons4 = new RegressedNetherseaReefbreaker(-200, 0);
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new SpawnMonsterAction(mons3, true));
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new SpawnMonsterAction(mons4, true));
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "DIE2"));
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

            if (stage < 3)
            {
                if (this.nextMove != 10)
                {
                    setMove((byte)10, Intent.BUFF);
                    createIntent();
                    AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new SetMoveAction(this, (byte)10, AbstractMonster.Intent.BUFF));
                }
            }
            else
            {
                this.halfDead = false;
                (AbstractDungeon.getCurrRoom()).cannotLose = false;
                this.die();
            }
        }
    }

    public void usePreBattleAction()
    {
        (AbstractDungeon.getCurrRoom()).cannotLose = true;
        if (AbstractDungeon.getCurrRoom() instanceof com.megacrit.cardcrawl.rooms.MonsterRoomBoss)
        {
            CardCrawlGame.music.unsilenceBGM();
            AbstractDungeon.scene.fadeOutAmbiance();
            EventHelper.musicHelper.playBGM("MIZUKI_BOSS2_1_INTRO", "MIZUKI_BOSS2_1_LOOP");
        }
    }

    public void takeTurn()
    {
        AbstractPlayer p = AbstractDungeon.player;
        switch (this.nextMove)
        {
            case 0:
                //学习
                AbstractDungeon.actionManager.addToBottom(
                        (AbstractGameAction)new ApplyPowerAction((AbstractCreature)this, (AbstractCreature)this,
                                (AbstractPower)new StrengthPower(this, 1)));
                break;
            case 1:
                //言语
                AbstractCard card = RejectionHelper.getRandomRejection();
                AbstractDungeon.actionManager.addToBottom(
                        (AbstractGameAction) new MakeTempCardInDrawPileAction(
                                (AbstractCard) card, 1, true, true, false));
                break;
            case 2:
                //拍打
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "ATTACK1"));
                transforming(this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT);
                //addToBot((AbstractGameAction) new DamageAction((AbstractCreature)p, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                break;
            case 3:
                //威吓
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new SFXAction("MONSTER_SNECKO_GLARE"));
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new VFXAction((AbstractCreature)this, (AbstractGameEffect)
                        new IntimidateEffect(this.hb.cX, this.hb.cY), 0.5F));

                addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)this, (AbstractPower)
                        new StrengthPower((AbstractCreature)p, -3), -3));
                if (p != null && !p.hasPower("Artifact"))
                    addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)this, (AbstractPower)
                            new GainStrengthPower((AbstractCreature)p, 3), 3));
                addToTop(new ApplyImpairmentAction(new NervousImpairment(), p, 1));
                break;
            case 4:
                //甩尾
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "ATTACK1"));
                transforming(this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_HEAVY);
                //addToBot((AbstractGameAction) new DamageAction((AbstractCreature)p, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                addToTop(new ApplyImpairmentAction(new NervousImpairment(), p, 1));
                break;
            case 5:
                //撕裂
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "ATTACK1"));
                transforming(this.damage.get(2), AbstractGameAction.AttackEffect.SLASH_HEAVY);
                //addToBot((AbstractGameAction) new DamageAction((AbstractCreature)p, this.damage.get(2), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "ATTACK1"));
                transforming(this.damage.get(2), AbstractGameAction.AttackEffect.SLASH_HEAVY);
                //addToBot((AbstractGameAction) new DamageAction((AbstractCreature)p, this.damage.get(2), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                break;
            case 6:
                //咆哮
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new SFXAction("VO_AWAKENEDONE_3"));
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new VFXAction((AbstractCreature)this, (AbstractGameEffect)
                        new ShockWaveEffect(this.hb.cX, this.hb.cY,
                                new Color(0.3F, 0.2F, 0.4F, 1.0F),
                                ShockWaveEffect.ShockWaveType.CHAOTIC), 1.0F));
                AbstractDungeon.actionManager.addToBottom(
                        (AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)this,
                                (AbstractPower)new WeakPower((AbstractCreature)p, 2, true), 2));
                AbstractDungeon.actionManager.addToBottom(
                        (AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)this,
                                (AbstractPower)new VulnerablePower((AbstractCreature)p, 2, true), 2));
                AbstractDungeon.actionManager.addToBottom(
                        (AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)this,
                                (AbstractPower)new FrailPower((AbstractCreature)p, 2, true), 2));
                break;
            case 7:
                //撕咬
                for (int i = 0;i < 4;i++)
                {
                    AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "ATTACK2"));
                    AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new WaitAction(0.4F));
                    AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new VFXAction((AbstractGameEffect) new BiteEffect(AbstractDungeon.player.hb.cX +
                            MathUtils.random(-25.0F, 25.0F) * Settings.scale, AbstractDungeon.player.hb.cY +
                            MathUtils.random(-25.0F, 25.0F) * Settings.scale, Color.GOLD
                            .cpy()), 0.0F));
                    AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new VampireDamageAction((AbstractCreature)p, this.damage
                            .get(3), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                }
                break;
            case 8:
                //洗礼
                addToBot(new GainBlockAction(this, 20));
                break;
            case 9:
                //撞击
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "ATTACK2"));
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new VampireDamageAction((AbstractCreature)p, this.damage
                        .get(4), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                break;
            case 10:
                //复活
                addToBot(new WaitAction(1.5f));
                turn = 0;
                halfDead = false;
                if (stage == 1)
                {
                    AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "START1"));
                    if (AbstractDungeon.ascensionLevel >= 9)
                    {
                        this.maxHealth = 100;
                    }
                    else
                    {
                        this.maxHealth = 90;
                    }
                    AbstractDungeon.actionManager.addToBottom(
                            (AbstractGameAction)new ApplyPowerAction((AbstractCreature)this, (AbstractCreature)this,
                                    (AbstractPower)new ErosionPower((AbstractCreature)this, 1)));
                }
                if (stage == 2)
                {
                    AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "START2"));
                    if (AbstractDungeon.ascensionLevel >= 9)
                    {
                        this.maxHealth = 120;
                    }
                    else
                    {
                        this.maxHealth = 110;
                    }
                    AbstractDungeon.actionManager.addToBottom(
                            (AbstractGameAction)new ApplyPowerAction((AbstractCreature)this, (AbstractCreature)this,
                                    (AbstractPower)new TransformingPower((AbstractCreature)this, 1)));
                }
                eat();
                AbstractDungeon.actionManager.addToBottom(
                        (AbstractGameAction)new HealAction((AbstractCreature)this, (AbstractCreature)this, (int)this.maxHealth));
                break;
        }
        getMove(0);
        if (!halfDead)
            turn++;
    }

    protected void getMove(int i)
    {
        if (stage == 0)
        {
            int roll = AbstractDungeon.aiRng.random(99);
            if (roll < 50)
            {
                setMove(MOVES[0], (byte)0, Intent.BUFF);
            }
            else
            {
                setMove(MOVES[1], (byte)1, Intent.DEBUFF);
            }
            return;
        }
        if (stage == 1)
        {
            if (turn > 4)
            {
                turn = 0;
            }
            switch (turn)
            {
                case 0:
                    //威吓
                    setMove(MOVES[3], (byte)3, Intent.DEBUFF);
                    break;
                case 3:
                case 1:
                    //拍打
                    setMove(MOVES[2], (byte)2, Intent.ATTACK, ((DamageInfo)this.damage.get(0)).base);
                    break;
                case 2:
                    //甩尾
                    setMove(MOVES[4], (byte)4, Intent.ATTACK_DEBUFF, ((DamageInfo)this.damage.get(1)).base);
                    break;
                case 4:
                    //撕裂
                    setMove(MOVES[5], (byte)5, Intent.ATTACK, ((DamageInfo)this.damage.get(2)).base, 2, true);
                    break;
            }
            return;
        }
        if (stage == 2)
        {
            if (turn > 3)
            {
                turn = 0;
            }
            switch (turn)
            {
                case 0:
                    //咆哮
                    setMove(MOVES[6], (byte)6, Intent.DEBUFF);
                    break;
                case 1:
                    //撕咬
                    setMove(MOVES[7], (byte)7, Intent.ATTACK, ((DamageInfo)this.damage.get(3)).base, 4, true);
                    break;
                case 2:
                    //洗礼
                    setMove(MOVES[8], (byte)8, Intent.DEFEND);
                    break;
                case 3:
                    //撞击
                    setMove(MOVES[9], (byte)9, Intent.ATTACK, ((DamageInfo)this.damage.get(4)).base);
                    break;
            }
        }
    }

    public void changeState(String stateName)
    {
        if (stateName.equals("DIE1"))
        {
            this.state.setAnimation(0, "Die_1", false);
            this.state.addAnimation(0, "Die", true, 0.0F);
        }
        if (stateName.equals("START1"))
        {
            this.state.setAnimation(0, "Start_2", false);
            this.state.addAnimation(0, "Idle_2", true, 0.0F);
        }

        if (stateName.equals("DIE2"))
        {
            this.state.setAnimation(0, "Die_2", false);
            this.state.addAnimation(0, "Die", true, 0.0F);
        }
        if (stateName.equals("START2"))
        {
            this.state.setAnimation(0, "Start_4", false);
            this.state.addAnimation(0, "Idle_4", true, 0.0F);
        }

        if (stateName.equals("DIE3"))
        {
            this.state.setAnimation(0, "Die_4", false);
        }

        if (stateName.equals("ATTACK1"))
        {
            this.state.setAnimation(0, "Attack_2", false);
            this.state.addAnimation(0, "Idle_2", true, 0.0F);
        }
        if (stateName.equals("ATTACK2"))
        {
            this.state.setAnimation(0, "Attack_4", false);
            this.state.addAnimation(0, "Idle_4", true, 0.0F);
        }

    }

    private void transforming(DamageInfo damageinfo, AbstractGameAction.AttackEffect attackEffect)
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
                    AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, attackEffect));
                    this.target.damage(damageinfo);
                    if (this.target.lastDamageTaken > 0)
                    {
                        addToTop(new ApplyImpairmentAction(new NervousImpairment(), target, m.getPower(ErosionPower.id).amount));
                        addToTop((AbstractGameAction)new WaitAction(0.1F));
                    }
                    addToTop((AbstractGameAction) new WaitAction(0.1F));
                    if ((AbstractDungeon.getCurrRoom()).monsters.areMonstersBasicallyDead())
                        AbstractDungeon.actionManager.clearPostCombatActions();
                }
            }
        });
    }

    private void eat()
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
                    if (stage == 1)
                    {
                        if (mons1.currentHealth <= 0 && mons2.currentHealth <= 0)
                        {
                            return;
                        }
                        if ((mons1.currentHealth > 0 && mons2.currentHealth <= 0) || (mons1.currentHealth >= mons2.currentHealth))
                        {
                            addToBot((AbstractGameAction) new DamageAction(mons1, new DamageInfo(m, mons1.maxHealth), AttackEffect.NONE));
                            AbstractDungeon.actionManager.addToBottom(
                                    (AbstractGameAction)new ApplyPowerAction((AbstractCreature)m, (AbstractCreature)m,
                                            (AbstractPower)new DreamInterruptedPower((AbstractCreature)m, 1, false)));
                            AbstractDungeon.actionManager.addToBottom((AbstractGameAction) new VFXAction((AbstractGameEffect) new BiteEffect(mons1.hb.cX +
                                    MathUtils.random(-25.0F, 25.0F) * Settings.scale, mons1.hb.cY +
                                    MathUtils.random(-25.0F, 25.0F) * Settings.scale, Color.GOLD
                                    .cpy()), 0.0F));
                            if (mons2.currentHealth > 0)
                            {
                                addToBot(new SuicideAction(mons2));
                            }
                        }
                        if ((mons1.currentHealth <= 0 && mons2.currentHealth > 0) || (mons1.currentHealth <= mons2.currentHealth))
                        {
                            addToBot((AbstractGameAction) new DamageAction(mons2, new DamageInfo(m, mons2.maxHealth), AttackEffect.NONE));
                            addToBot(new ApplyPowerAction(m, m, new PocketSeaStingPower(m, (int)(m.maxHealth * 0.25))));
                            //addToBot(new ApplyPowerAction(m, m, new StrengthPower(m, 3)));
                            AbstractDungeon.actionManager.addToBottom((AbstractGameAction) new VFXAction((AbstractGameEffect) new BiteEffect(mons2.hb.cX +
                                    MathUtils.random(-25.0F, 25.0F) * Settings.scale, mons2.hb.cY +
                                    MathUtils.random(-25.0F, 25.0F) * Settings.scale, Color.GOLD
                                    .cpy()), 0.0F));
                            if (mons1.currentHealth > 0)
                            {
                                addToBot(new SuicideAction(mons1));
                            }
                        }

                    }
                    if (stage == 2)
                    {
                        if ((mons3.isDeadOrEscaped() && mons4.isDeadOrEscaped()))
                        {
                            return;
                        }
                        if ((!mons3.isDeadOrEscaped() && mons4.isDeadOrEscaped()) || (mons3.currentHealth >= mons4.currentHealth))
                        {
                            addToBot((AbstractGameAction) new DamageAction(mons3, new DamageInfo(m, mons3.maxHealth), AttackEffect.NONE));
                            AbstractDungeon.actionManager.addToBottom(
                                    (AbstractGameAction)new ApplyPowerAction((AbstractCreature)m, (AbstractCreature)m,
                                            (AbstractPower)new PhantomOfNetherseaPower((AbstractCreature)m, -1)));
                            AbstractDungeon.actionManager.addToBottom((AbstractGameAction) new VFXAction((AbstractGameEffect) new BiteEffect(mons3.hb.cX +
                                    MathUtils.random(-25.0F, 25.0F) * Settings.scale, mons3.hb.cY +
                                    MathUtils.random(-25.0F, 25.0F) * Settings.scale, Color.GOLD
                                    .cpy()), 0.0F));
                            if (!mons4.isDeadOrEscaped())
                            {
                                addToBot(new SuicideAction(mons4));
                            }
                        }
                        if ((mons3.isDeadOrEscaped() && !mons4.isDeadOrEscaped()) || (mons3.currentHealth <= mons4.currentHealth))
                        {
                            addToBot((AbstractGameAction) new DamageAction(mons4, new DamageInfo(m, mons4.maxHealth), AttackEffect.NONE));
                            AbstractDungeon.actionManager.addToBottom(
                                    (AbstractGameAction)new ApplyPowerAction((AbstractCreature)m, (AbstractCreature)m,
                                            (AbstractPower)new DeadlyRhythmPower((AbstractCreature)m, 1)));
                            AbstractDungeon.actionManager.addToBottom((AbstractGameAction) new VFXAction((AbstractGameEffect) new BiteEffect(mons4.hb.cX +
                                    MathUtils.random(-25.0F, 25.0F) * Settings.scale, mons4.hb.cY +
                                    MathUtils.random(-25.0F, 25.0F) * Settings.scale, Color.GOLD
                                    .cpy()), 0.0F));
                            if (!mons3.isDeadOrEscaped())
                            {
                                addToBot(new SuicideAction(mons3));
                            }
                        }
                    }
                    addToTop((AbstractGameAction) new WaitAction(0.1F));
                    if ((AbstractDungeon.getCurrRoom()).monsters.areMonstersBasicallyDead())
                        AbstractDungeon.actionManager.clearPostCombatActions();
                }
            }
        });
    }

    public void die()
    {
        if (!(AbstractDungeon.getCurrRoom()).cannotLose)
        {
            isDying = true;
            useFastShakeAnimation(5.0F);
            CardCrawlGame.screenShake.rumble(4.0F);
            super.die();
            onBossVictoryLogic();
        }
    }
}
