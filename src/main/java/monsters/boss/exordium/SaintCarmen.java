package monsters.boss.exordium;

import Impairment.NervousImpairment;
import actions.ApplyImpairmentAction;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import helper.EventHelper;
import modcore.MizukiModCore;
import monsters.AbstractMizukiMonster;
import powers.AmmoPower;
import powers.StunMonsterPower;
import powers.WeakPointPower;

public class SaintCarmen extends AbstractMizukiMonster
{
    //圣徒卡门
    //圣徒卡门初始自带2层手炮弹药
    //装填：当下回合要进行装弹时，给予自身30层弱点，当弱点大于15时，进行完美装弹，获得3层手炮弹药与2点力量；若低于15点，则获得2层手炮弹药；若降低到0，则眩晕1回合并在下回合进行尽力装弹，获得1层手炮弹药。
    //爆裂铳射：造成18点伤害，并向抽牌堆中放入2张灼伤，若手炮弹药仅剩1层，仅有50%概率进行爆裂铳射，否则会进行装弹。当不具有手跑弹药时，必然进行装弹、
    //剑击：造成6点伤害
    //弱点击破：造成5点伤害，给予1层虚弱
    //剑击=>弱点击破=>爆裂铳射=>剑击=>爆裂铳射=>弱点击破

    public static final String ID = "Mizuki:SaintCarmen";

    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Mizuki:SaintCarmen");

    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    public int turn = 0;

    public int weakness = 30;

    public int maxAmmo = 3;

    public int previousHp = 0;

    public int debuffCount = 1;

    public boolean ifHasBeenBreak = false;

    public boolean ifHasWeakPointPower = false;

    public boolean isReloading = false;

    public SaintCarmen(float x, float y)
    {

        super(NAME, "Mizuki:SaintCarmen", 90, 0.0F, 0.0F, 250.0F, 320.0F, null, x, y);
        this.type = EnemyType.BOSS;

        int dmg1, dmg2, dmg3;

        if (AbstractDungeon.ascensionLevel >= 9)
        {
            setHp(130, 138);
        }
        else
        {
            setHp(111, 120);
        }

        if (AbstractDungeon.ascensionLevel >= 19)
        {
            dmg1 = 8;
            dmg2 = 7;
            dmg3 = 24;
        }
        else if (AbstractDungeon.ascensionLevel >= 4)
        {
            dmg1 = 8;
            dmg2 = 7;
            dmg3 = 24;
        }
        else
        {
            dmg1 = 6;
            dmg2 = 5;
            dmg3 = 18;
        }

        this.damage.add(new DamageInfo((AbstractCreature) this, MathUtils.floor(dmg1)));
        this.damage.add(new DamageInfo((AbstractCreature) this, MathUtils.floor(dmg2)));
        this.damage.add(new DamageInfo((AbstractCreature) this, MathUtils.floor(dmg3)));

        //填入spine文件名
        loadAnimation("resources/img/monster/SaintCarmen/enemy_2026_syudg.atlas", "resources/img/monster/SaintCarmen/enemy_2026_syudg.json", 1.5F);
        this.flipHorizontal = true;
        this.stateData.setMix("Idle", "Die", 0.1F);
        this.state.setTimeScale(1.25f);
        this.state.addAnimation(0, "Idle", true, 0.0F);
        this.nextMove = 0;
    }

    @Override
    public void update()
    {
        super.update();
        if (!ifHasWeakPointPower)
        {
            ifHasWeakPointPower = this.hasPower(WeakPointPower.id);
            return;
        }
        if (isReloading && !ifHasBeenBreak)
        {
            if (previousHp > currentHealth)
            {
                //减少弱点能力
                AbstractPower weakPoterPower = this.getPower(WeakPointPower.id);
                if ((previousHp - this.currentHealth) >= weakPoterPower.amount)
                {
                    addToBot((AbstractGameAction) new RemoveSpecificPowerAction(this, this, WeakPointPower.id));
                }
                else
                {
                    AbstractDungeon.actionManager.addToBottom((AbstractGameAction)
                            new ReducePowerAction(this, this, WeakPointPower.id, previousHp - this.currentHealth));
                }
            }
            previousHp = this.currentHealth;

            if (this.hasPower(StunMonsterPower.id))
            {
                //如果被击晕的话，直接击破，并移除弱点能力，但不会给予晕眩
                this.moveName = MOVES[2];
                ifHasBeenBreak = true;
                addToBot((AbstractGameAction) new RemoveSpecificPowerAction(this, this, WeakPointPower.id));
                return;
            }
            if (this.hasPower(WeakPointPower.id))
            {
                AbstractPower weakneesPower = this.getPower(WeakPointPower.id);
                if (weakneesPower.amount >= 15)
                {
                    this.moveName = MOVES[0];
                }
                else
                {
                    this.moveName = MOVES[1];
                }
            }
            else
            {
                this.moveName = MOVES[2];
                //被击破后给予晕眩
                AbstractDungeon.actionManager.addToBottom(
                        (AbstractGameAction)new ApplyPowerAction((AbstractCreature)this, (AbstractCreature)this,
                                (AbstractPower)new StunMonsterPower((AbstractCreature)this, 1)));
                ifHasBeenBreak = true;
            }
        }
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
                        (AbstractPower)new AmmoPower((AbstractCreature)this, 2)));
    }

    public void takeTurn()
    {
        AbstractPlayer p = AbstractDungeon.player;
        switch (this.nextMove)
        {
            case 0:
                //剑击
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "ATTACK"));
                addToBot((AbstractGameAction) new DamageAction((AbstractCreature)p, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                break;
            case 1:
                //弱点击破
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "ATTACK"));
                addToBot((AbstractGameAction) new DamageAction((AbstractCreature)p, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                AbstractDungeon.actionManager.addToBottom(
                        (AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)this,
                                (AbstractPower)new WeakPower((AbstractCreature)p, debuffCount, true), debuffCount));
                break;
            case 2:
                //装填
                AbstractPower weakneesPower = this.getPower(WeakPointPower.id);

                if (!ifHasBeenBreak)
                {
                    if (weakneesPower.amount >= 15)
                    {
                        //完美装填获得3层手炮弹药与2点力量
                        reload();
                        reload();
                        reload();
                        AbstractDungeon.actionManager.addToBottom(
                                (AbstractGameAction)new ApplyPowerAction((AbstractCreature)this, (AbstractCreature)this,
                                        (AbstractPower)new StrengthPower((AbstractCreature)this, 2)));
                    }
                    else
                    {
                        //装填，获得2层手炮弹药
                        reload();
                        reload();
                    }
                }
                else
                {
                    //尽力装填，获得1层手炮弹药
                    reload();
                }

                //如果有弱点能力，移除该能力
                if (this.hasPower(WeakPointPower.id))
                {
                    addToBot((AbstractGameAction) new RemoveSpecificPowerAction(this, this, WeakPointPower.id));
                }
                isReloading = false;
                ifHasBeenBreak = false;
                break;
            case 3:
                //爆裂铳射
                fire();
                break;
        }
        turn++;
        getMove(0);
    }

    protected void getMove(int i)
    {
        if (turn >= 6)
        {
            turn = 0;
        }
        //剑击=>弱点击破=>爆裂铳射=>剑击=>爆裂铳射=>弱点击破
        switch (turn)
        {
            case 0:
            case 3:
                setMove(MOVES[5], (byte)0, Intent.ATTACK, ((DamageInfo)this.damage.get(0)).base);
                //0号技能：剑击
                break;
            case 1:
            case 4:
                setMove(MOVES[4], (byte)1, Intent.ATTACK_DEBUFF, ((DamageInfo)this.damage.get(1)).base);
                //1号技能：弱点击破
                break;
            case 2:
            case 5:
                if (hasPower(AmmoPower.id))
                {
                    AbstractPower ammoPower = this.getPower(AmmoPower.id);
                    int roll = MathUtils.random(99);
                    //当手炮弹药只有1层时，只有50%的概率使用爆裂铳射
                    if (ammoPower.amount > 1 || (ammoPower.amount > 0  && roll >= 50))
                    {
                        //3号技能：爆裂铳射
                        setMove(MOVES[3], (byte)3, Intent.ATTACK_DEBUFF, ((DamageInfo)this.damage.get(2)).base);
                        return;
                    }
                }
                //2号技能：装填
                isReloading = true;
                ifHasWeakPointPower = false;
                AbstractDungeon.actionManager.addToBottom(
                        (AbstractGameAction)new ApplyPowerAction((AbstractCreature)this, (AbstractCreature)this,
                                (AbstractPower)new WeakPointPower((AbstractCreature)this, weakness)));
                setMove(MOVES[0], (byte)2, Intent.BUFF);
                break;
        }
    }

    public void changeState(String stateName)
    {
        if (stateName.equals("ATTACK"))
        {
            this.state.setAnimation(0, "Attack_2", false);
            this.state.addAnimation(0, "Idle", true, 0.0F);
        }
        if (stateName.equals("RELOAD"))
        {
            this.state.setAnimation(0, "Skill", false);
            this.state.addAnimation(0, "Idle", true, 0.0F);
        }
        if (stateName.equals("FIRE"))
        {
            this.state.setAnimation(0, "Attack_1", false);
            this.state.addAnimation(0, "Idle", true, 0.0F);
        }
    }

    public void die()
    {
        useFastShakeAnimation(5.0F);
        CardCrawlGame.screenShake.rumble(4.0F);
        this.state.setAnimation(0, "Die", false);
        super.die();
        onBossVictoryLogic();
    }


    private void fire()
    {
        AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "FIRE"));
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
                    AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AttackEffect.BLUNT_LIGHT));
                    AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AttackEffect.FIRE));
                    this.target.damage(damage.get(2));

                    addToTop((AbstractGameAction) new WaitAction(0.1F));

                    AbstractDungeon.actionManager.addToBottom((AbstractGameAction) new MakeTempCardInDrawPileAction((AbstractCard) new Burn(), 2, true, true, false));
                    AbstractPower ammoPower = m.getPower(AmmoPower.id);
                    if (ammoPower.amount == 1)
                    {
                        addToBot((AbstractGameAction) new RemoveSpecificPowerAction(m, m, AmmoPower.id));
                    }
                    else
                    {
                        //减少1层手炮弹药
                        addToBot((AbstractGameAction) new ReducePowerAction(m, m, AmmoPower.id, 1));
                    }

                    if ((AbstractDungeon.getCurrRoom()).monsters.areMonstersBasicallyDead())
                        AbstractDungeon.actionManager.clearPostCombatActions();
                }
            }
        });
    }

    private void reload()
    {
        AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "RELOAD"));
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
                    this.duration = 0.6F;
                    justIn = false;
                }
                tickDuration();
                if (this.isDone)
                {
                    addToTop((AbstractGameAction) new WaitAction(0.1F));

                    AbstractDungeon.actionManager.addToTop(
                            (AbstractGameAction)new ApplyPowerAction((AbstractCreature)m, (AbstractCreature)m,
                                    (AbstractPower)new AmmoPower((AbstractCreature)m, 1)));

                    if ((AbstractDungeon.getCurrRoom()).monsters.areMonstersBasicallyDead())
                        AbstractDungeon.actionManager.clearPostCombatActions();
                }
            }
        });
    }
}
