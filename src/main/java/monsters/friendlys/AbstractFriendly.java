package monsters.friendlys;

import basemod.ReflectionHacks;
import characters.Mizuki;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.evacipated.cardcrawl.modthespire.lib.SpireSuper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BobEffect;
import com.megacrit.cardcrawl.vfx.combat.BlockedWordEffect;
import com.megacrit.cardcrawl.vfx.combat.HbBlockBrokenEffect;
import com.megacrit.cardcrawl.vfx.combat.StrikeEffect;
import modcore.MizukiModCore;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class AbstractFriendly extends AbstractMonster
{
    //public static final String ID = "Mizuki:BalefulBroodling";

    //private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);

    //private static final String NAME = monsterStrings.NAME;

    //private static final String[] MOVES = monsterStrings.MOVES;

    //private static final String[] DIALOG = monsterStrings.DIALOG;

    private static final int MAX_HP = 40;

    private static final float HB_X = 0.0F;

    private static final float HB_Y = 0.0F;

    private static final float HB_W = 200.0F;

    private static final float HB_H = 250.0F;

    private static final float OFFSET_X = 0.0F;

    private static final float OFFSET_Y = 0.0F;

    private static final int SELF_DAMAGE = 3;

    public int damageMultiplier = 1;

   // protected ArrayList<AnimationInfo> animationList = new ArrayList<>();

    //protected float animationTimer = 0.0F;

    //protected AnimationInfo currentAnim;

    public AbstractFriendly(String name, String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl, float offsetX, float offsetY)
    {
        super(name, id, maxHealth, hb_x, hb_y, hb_w, hb_h, imgUrl, offsetX, offsetY);
        this.isPlayer = true;
        //this.damage.add(new DamageInfo((AbstractCreature)this, 3));
        //SkinSelectScreen.Skin skin = SkinSelectScreen.getSkin();
        //loadAnimation(".atlas", ".json", 1.8F);
        //AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        //e.setTime(e.getEndTime() * MathUtils.random());
    }

    public void spawn()
    {
        /*
        if (AbstractDungeon.player.chosenClass == Kaltsit.Enums.KALTSIT)
        {
            if (MathUtils.randomBoolean())
            {
                //播放登场声音
            }
        }
        else
        {
            CardCrawlGame.sound.playA(KaltsitModCore.MakePath("Summon_2"), 0.0F);
        }*/
        init();
        showHealthBar();
        createIntent();
        AbstractMonster m = this;
        float x = AbstractDungeon.player.drawX + 200.0F * Settings.scale;
        float y = AbstractDungeon.player.drawY /*+ 100.0F * Settings.scale*/;
        //GenericHelper.MoveMonster(m, x, y);
        m.drawX = x;
        m.drawY = y;
        m.animX = 0.0F;
        m.animY = 0.0F;
        m.hb.move(m.drawX + m.hb_x, m.drawY + m.hb_y + m.hb_h / 2.0F);
        m.healthHb.move(m.hb.cX, m.hb.cY - m.hb_h / 2.0F - m.healthHb.height / 2.0F);
        m.refreshIntentHbLocation();
    }

    public void addTip()
    {
        ;
    }


    public void usePreBattleAction()
    {
        super.usePreBattleAction();
    }

    public void changeState(String stateName)
    {
        //这里设置动画
        /*
        switch (stateName)
        {
            case "Start":
            case "Attack":
            case "Skill":
            case "Skill_2":
                if (this.meltdownNextTurn)
                {
                    addAnimation("Skill_2");
                    break;
                }
                addAnimation(stateName);
                break;
            case "Die":
                this.state.setAnimation(0, stateName, false);
                break;
        }*/
    }

    public int calculateDmg(AbstractMonster mo)
    {
        float tmp = ((DamageInfo)this.damage.get(0)).base;
        DamageInfo.DamageType type = ((DamageInfo)this.damage.get(0)).type;
        for (AbstractPower p : this.powers)
            tmp = p.atDamageGive(tmp, type);
        if (mo != null)
            for (AbstractPower p : mo.powers)
                tmp = p.atDamageReceive(tmp, type);
        for (AbstractPower p : this.powers)
            tmp = p.atDamageFinalGive(tmp, type);
        if (mo != null)
            for (AbstractPower p : mo.powers)
                tmp = p.atDamageFinalReceive(tmp, type);
        return (int)Math.floor(tmp);
    }

    public void takeTurn()
    {
        /*
        if (this.gainBlockNextTurn)
        {

            for (int i = 0; i < this.damageTimes; i++)
            {
                GenericHelper.addToBotAbstract(() ->
                {
                    ((DamageInfo)this.damage.get(0)).output = calculateDmg((AbstractMonster)null);
                    addToTop((AbstractGameAction)new GainBlockAction((AbstractCreature)this, ((DamageInfo)this.damage.get(0)).output));
                });
            }
        }
        else
        {

            if (hasPower(CantAttackPower.id))
            {
                getPower(CantAttackPower.id).flash();
                return;
            }
            DamageInfo.DamageType type = this.meltdownNextTurn ? DamageInfo.DamageType.HP_LOSS : DamageInfo.DamageType.NORMAL;
            ((DamageInfo)this.damage.get(0)).type = type;
            AbstractGameAction.AttackEffect effect = this.meltdownNextTurn ? AbstractGameAction.AttackEffect.FIRE : ((((DamageInfo)this.damage.get(0)).output > 20) ? AbstractGameAction.AttackEffect.SLASH_HORIZONTAL : AbstractGameAction.AttackEffect.SLASH_HEAVY);
            if (this.damageToAllNextTurn)
            {
                addToBot((AbstractGameAction)new ChangeStateAction(this, "Skill"));

                for (int i = 0; i < this.damageTimes; i++)
                {
                    if (this.meltdownNextTurn)
                        GenericHelper.foreachAliveMonster(mo ->
                        {
                            GenericHelper.addEffect((AbstractGameEffect)new MeltDownEffect(mo));
                            return Boolean.valueOf(false);
                        });
                    GenericHelper.addToBotAbstract(() ->
                    {
                        int dmg = calculateDmg((AbstractMonster)null);
                        int[] retVal = new int[(AbstractDungeon.getMonsters()).monsters.size()];
                        Arrays.fill(retVal, dmg);
                        addToBot((AbstractGameAction)new DamageAllEnemiesAction((AbstractCreature)this, retVal, type, effect));
                    });
                }
            }
            else if (this.damageToLowestNextTurn)
            {
                addToBot((AbstractGameAction)new ChangeStateAction(this, "Attack"));

                for (int i = 0; i < this.damageTimes; i++)
                {
                    GenericHelper.addToBotAbstract(() ->
                    {
                        AbstractMonster m = GenericHelper.getLowestMonster();
                        if (m != null)
                        {
                            if (this.meltdownNextTurn)
                                GenericHelper.addEffect((AbstractGameEffect)new MeltDownEffect(m));
                            ((DamageInfo)this.damage.get(0)).output = calculateDmg(m);
                            GenericHelper.addToNext((AbstractGameAction)new DamageAction((AbstractCreature)m, this.damage.get(0), effect, true));
                        }
                    });
                }
            }
            else
            {
                addToBot((AbstractGameAction)new ChangeStateAction(this, "Attack"));

                for (int i = 0; i < this.damageTimes; i++)
                {
                    GenericHelper.addToBotAbstract(() ->
                    {
                        AbstractMonster m = GenericHelper.getRandomMonsterSafe();
                        if (m != null)
                        {
                            if (this.meltdownNextTurn)
                                GenericHelper.addEffect((AbstractGameEffect)new MeltDownEffect(m));
                            ((DamageInfo)this.damage.get(0)).output = calculateDmg(m);
                            GenericHelper.addToNext((AbstractGameAction)new DamageAction((AbstractCreature)m, this.damage.get(0), effect, true));
                        }
                    });
                }
            }
        }
        */
    }

    protected void getMove(int num)
    {
        setMove((byte)0, AbstractMonster.Intent.ATTACK, ((DamageInfo)this.damage.get(0)).base);
    }

    public void die()
    {
        die(false);
        //MonsterPatch.ReviveTimer = 5;
    }

    public void die(boolean triggerRelic)
    {
        this.powers.forEach(AbstractPower::onDeath);
        changeState("Die");
        /*
        addToBot((AbstractGameAction)new DamageAction((AbstractCreature)AbstractDungeon.player, new DamageInfo((AbstractCreature)AbstractDungeon.player, 3, DamageInfo.DamageType.THORNS)));
        if (AbstractDungeon.player.hasPower(NonDamagingRestructuringPower.id))
        {
            int intentDmg = ((Integer)ReflectionHacks.getPrivate(this, AbstractMonster.class, "intentDmg")).intValue();
            addToBot((AbstractGameAction)new DamageAllEnemiesAction(null, DamageInfo.createDamageMatrix(intentDmg), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE));
        }
        if (AbstractDungeon.player.hasPower(PerceptualCoexistencePower.id))
            this.powers.forEach(p ->
            {
                if (p.type == AbstractPower.PowerType.DEBUFF)
                    return;
                p.owner = (AbstractCreature)AbstractDungeon.player;
                if (p.amount / 2 > 0)
                {
                    p.amount /= 2;
                    addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)AbstractDungeon.player, (AbstractCreature)AbstractDungeon.player, p));
                }
            });*/
        if (!this.isDying)
        {
            this.isDying = true;
            if (this.currentHealth < 0)
                this.currentHealth = 0;
            //this.deathTimer++;
        }
    }

    /*
    @SpireOverride
    protected void updateIntentTip()
    {
        //显示意图效果提示

        int intentDmg = ((Integer) ReflectionHacks.getPrivate(this, AbstractMonster.class, "intentDmg")).intValue();
        PowerTip tip = new PowerTip();
        if (this.damageToAllNextTurn)
        {
            tip.header = MOVES[4];
            tip.body = MOVES[5].replace("D", String.valueOf(intentDmg));
        }
        else if (this.damageToLowestNextTurn)
        {
            tip.header = MOVES[2];
            tip.body = MOVES[3].replace("D", String.valueOf(intentDmg));
        }
        else if (this.gainBlockNextTurn)
        {
            tip.header = MOVES[6];
            tip.body = MOVES[7].replace("D", String.valueOf(intentDmg));
        }
        else
        {
            tip.header = MOVES[0];
            tip.body = MOVES[1].replace("D", String.valueOf(intentDmg));
        }
        tip.img = getAttackIntent();
        ReflectionHacks.setPrivate(this, AbstractMonster.class, "intentTip", tip);

    }*/

    @SpireOverride
    protected void calculateDamage(int dmg)
    {
        float tmp = dmg;
        //GenericHelper.info("raw damage:" + tmp);
        for (AbstractPower po : this.powers)
            tmp = po.atDamageGive(tmp, DamageInfo.DamageType.NORMAL);
        for (AbstractPower po : this.powers)
            tmp = po.atDamageFinalGive(tmp, DamageInfo.DamageType.NORMAL);
        dmg = MathUtils.floor(tmp);
        if (dmg < 0)
            dmg = 0;
        //GenericHelper.info("final damage:" + dmg);
        ReflectionHacks.setPrivate(this, AbstractMonster.class, "intentDmg", Integer.valueOf(dmg));
    }

    public void damage(DamageInfo info)
    {
        float damageAmount = info.output;
        if (!this.isDying)
        {
            if (damageAmount < 0.0F)
                damageAmount = 0.0F;
            boolean hadBlock = (this.currentBlock != 0);
            boolean weakenedToZero = (damageAmount == 0.0F);
            damageAmount = decrementBlock(info, (int)damageAmount);
            for (AbstractPower po : this.powers)
                damageAmount = po.atDamageFinalReceive(damageAmount, info.type);
            if (info.owner != null)
            {
                for (AbstractPower po : info.owner.powers)
                    damageAmount = po.atDamageFinalGive(damageAmount, info.type);
                for (AbstractPower po : info.owner.powers)
                    damageAmount = po.onAttackToChangeDamage(info, (int)damageAmount);
            }
            //GenericHelper.info(this.name + "+ damageAmount);
            for (AbstractPower po : this.powers)
                damageAmount = po.onAttackedToChangeDamage(info, (int)damageAmount);
            for (AbstractPower po : this.powers)
                po.wasHPLost(info, (int)damageAmount);
            if (info.owner != null)
                for (AbstractPower po : info.owner.powers)
                    po.onAttack(info, (int)damageAmount, (AbstractCreature) this);
            for (AbstractPower po : this.powers)
                damageAmount = po.onAttacked(info, (int)damageAmount);
            this.lastDamageTaken = Math.min((int)damageAmount, this.currentHealth);
            boolean probablyInstantKill = (this.currentHealth == 0);
            if (damageAmount > 0.0F)
            {
                if (info.owner != null && info.owner != this)
                    useStaggerAnimation();
                this.currentHealth = (int)(this.currentHealth - damageAmount);
                if (!probablyInstantKill)
                    AbstractDungeon.effectList.add(new StrikeEffect((AbstractCreature)this, this.hb.cX, this.hb.cY, (int)damageAmount));
                if (this.currentHealth < 0)
                    this.currentHealth = 0;
                healthBarUpdatedEvent();
            }
            else if (!probablyInstantKill)
            {
                if (weakenedToZero && this.currentBlock == 0)
                {
                    if (hadBlock)
                    {
                        AbstractDungeon.effectList.add(new BlockedWordEffect((AbstractCreature)this, this.hb.cX, this.hb.cY, TEXT[30]));
                    }
                    else
                    {
                        AbstractDungeon.effectList.add(new StrikeEffect((AbstractCreature)this, this.hb.cX, this.hb.cY, 0));
                    }
                }
                else if (Settings.SHOW_DMG_BLOCK)
                {
                    AbstractDungeon.effectList.add(new BlockedWordEffect((AbstractCreature)this, this.hb.cX, this.hb.cY, TEXT[30]));
                }
            }
            if (this.currentHealth <= 0)
            {
                die();
                if (this.currentBlock > 0)
                {
                    loseBlock();
                    AbstractDungeon.effectList.add(new HbBlockBrokenEffect(this.hb.cX - this.hb.width / 2.0F + BLOCK_ICON_X, this.hb.cY - this.hb.height / 2.0F + BLOCK_ICON_Y));
                }
            }
        }
    }

    public void applyPowers()
    {
        EnemyMoveInfo move = (EnemyMoveInfo)ReflectionHacks.getPrivate(this, AbstractMonster.class, "move");
        if (move.baseDamage > -1)
            calculateDamage(move.baseDamage);
        ReflectionHacks.setPrivate(this, AbstractMonster.class, "intentImg", getIntentImg());


        try
        {
            // 获取Method对象
            Field privateF = AbstractMonster.class.getDeclaredField("intentImg");

            // 设置accessible为true以调用私有方法
            privateF.setAccessible(true);

            if ((Texture)privateF.get(this) == null)
            {
                MizukiModCore.logger.info("NULL");
            }
            else
            {

                MizukiModCore.logger.info("NOT NULL");
            }

            // 调用私有方法
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            // 获取Method对象
            Method privateMethod = AbstractMonster.class.getDeclaredMethod("updateIntentTip");

            // 设置accessible为true以调用私有方法
            privateMethod.setAccessible(true);

            // 调用私有方法
            privateMethod.invoke(this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void applyEndOfTurnTriggers()
    {
        super.applyEndOfTurnTriggers();
        this.powers.forEach(p ->
        {
            p.atEndOfTurn(true);
            p.atEndOfTurnPreEndTurnCards(true);
            p.atEndOfRound();
        });
    }

    /*
    public void addAnimation(String anim)
    {
        this.animationList.add(new AnimationInfo(anim, this.stateData.getSkeletonData().findAnimation(anim).getDuration()));
    }
    */

    public void update()
    {
        super.update();
        updatePowers();
        this.hb.update();
        this.intentHb.update();
        this.healthHb.update();
        if (AbstractDungeon.player != null)
            this.flipHorizontal = AbstractDungeon.player.flipHorizontal;
        /*
        if (this.animationList.size() > 0 || this.currentAnim != null)
        {
            this.animationTimer -= Gdx.graphics.getDeltaTime();
            if (this.animationTimer <= 0.0F)
            {
                if (this.currentAnim != null)
                    this.animationList.remove(0);
                if (this.animationList.size() == 0)
                {
                    this.state.addAnimation(0, "Idle", true, 0.0F);
                    this.currentAnim = null;
                }
                else
                {
                    this.currentAnim = this.animationList.get(0);
                    this.animationTimer = this.currentAnim.time;
                    this.state.setAnimation(0, this.currentAnim.anim, false);
                }
            }
        }*/
    }

    /*
    @SpireOverride
    protected void renderDamageRange(SpriteBatch sb)
    {
        int intentDmg = ((Integer)ReflectionHacks.getPrivate(this, AbstractMonster.class, "intentDmg")).intValue();
        BobEffect bobEffect = (BobEffect)ReflectionHacks.getPrivate(this, AbstractMonster.class, "bobEffect");
        Color intentColor = (Color)ReflectionHacks.getPrivate(this, AbstractMonster.class, "intentColor");
        String label = this.damageToAllNextTurn ? "->ALL" : (this.damageToLowestNextTurn ? "->LOW" : "");
        if (this.damageTimes > 1)
        {
            FontHelper.renderFontLeftTopAligned(sb, FontHelper.topPanelInfoFont, intentDmg + "x" + this.damageTimes + label, this.intentHb.cX - 30.0F * Settings.scale, this.intentHb.cY + bobEffect.y - 12.0F * Settings.scale, intentColor);
        }
        else
        {
            FontHelper.renderFontLeftTopAligned(sb, FontHelper.topPanelInfoFont, intentDmg + label, this.intentHb.cX - 30.0F * Settings.scale, this.intentHb.cY + bobEffect.y - 12.0F * Settings.scale, intentColor);
        }
    }
    */

    public void render(SpriteBatch sb)
    {
        super.render(sb);
        if ((this.hb.hovered || this.intentHb.hovered) && AbstractDungeon.currMapNode != null &&
                (AbstractDungeon.getCurrRoom()).phase != AbstractRoom.RoomPhase.COMPLETE)
            renderTip(sb);
    }


    @SpireOverride
    protected Texture getIntentImg()
    {
        switch (this.intent)
        {
            case ATTACK:
            case ATTACK_BUFF:
            case ATTACK_DEBUFF:
            case ATTACK_DEFEND:
                return getAttackIntent();
            case BUFF:
                return ImageMaster.INTENT_BUFF_L;
            case DEBUFF:
                return ImageMaster.INTENT_DEBUFF_L;
            case STRONG_DEBUFF:
                return ImageMaster.INTENT_DEBUFF2_L;
            case DEFEND:
            case DEFEND_DEBUFF:
                return ImageMaster.INTENT_DEFEND_L;
            case DEFEND_BUFF:
                return ImageMaster.INTENT_DEFEND_BUFF_L;
            case ESCAPE:
                return ImageMaster.INTENT_ESCAPE_L;
            case MAGIC:
                return ImageMaster.INTENT_MAGIC_L;
            case SLEEP:
                return ImageMaster.INTENT_SLEEP_L;
            case STUN:
                return null;
            case UNKNOWN:
                return ImageMaster.INTENT_UNKNOWN_L;
        }
        return ImageMaster.INTENT_UNKNOWN_L;
        /*
        try
        {
            // 获取Method对象
            Method privateMethod = AbstractMonster.class.getDeclaredMethod("getIntentImg");

            // 设置accessible为true以调用私有方法
            privateMethod.setAccessible(true);

            // 调用私有方法
            return (Texture) privateMethod.invoke(this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;

        this.intent = this.gainBlockNextTurn ? AbstractMonster.Intent.DEFEND : AbstractMonster.Intent.ATTACK;
        return (Texture) SpireSuper.call(new Object[0]);*/
    }


    /*
    public static class AnimationInfo
    {
        public String anim;

        public float time;

        public AnimationInfo(String anim, float time)
        {
            this.anim = anim;
            this.time = time;
        }
    }
    */
}
