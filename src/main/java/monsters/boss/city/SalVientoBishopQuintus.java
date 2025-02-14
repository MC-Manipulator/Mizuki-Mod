package monsters.boss.city;

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
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.vfx.combat.IntimidateEffect;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import helper.EventHelper;
import helper.RejectionHelper;
import monsters.AbstractMizukiMonster;
import monsters.friendlys.AbstractFriendly;
import monsters.friendlys.ErosionproofCoatingDevice;
import monsters.special.*;
import patches.FriendlyPatch;
import powers.*;

import java.util.ArrayList;

public class SalVientoBishopQuintus extends AbstractMizukiMonster
{
    //昆图斯
    public static final String ID = "Mizuki:SalVientoBishopQuintus";

    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Mizuki:SalVientoBishopQuintus");

    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    public int turn = 0;

    public int impairmentCount1 = 2;

    public int impairmentCount2 = 1;

    private AbstractMonster[] tentacles = new AbstractMonster[4];

    public float[] POSX = new float[4];

    public float[] POSY = new float[4];

    public SalVientoBishopQuintus(float x, float y)
    {

        super(NAME, "Mizuki:SalVientoBishopQuintus", 90, 0.0F, 0.0F, 550.0F, 320.0F, null, x, y);
        this.type = EnemyType.BOSS;

        int dmg1, dmg2;
        int impairmentCount1, impairmentCount2;

        if (AbstractDungeon.ascensionLevel >= 9)
        {
            setHp(420);
        }
        else
        {
            setHp(400);
        }

        if (AbstractDungeon.ascensionLevel >= 19)
        {
            dmg1 = 15;
            dmg2 = 30;
            impairmentCount1 = 3;
            impairmentCount2 = 2;
        }
        else if (AbstractDungeon.ascensionLevel >= 4)
        {
            dmg1 = 15;
            dmg2 = 30;
            impairmentCount1 = 2;
            impairmentCount2 = 1;
        }
        else
        {
            dmg1 = 14;
            dmg2 = 28;
            impairmentCount1 = 2;
            impairmentCount2 = 1;
        }

        this.damage.add(new DamageInfo((AbstractCreature) this, MathUtils.floor(dmg1)));
        this.damage.add(new DamageInfo((AbstractCreature) this, MathUtils.floor(dmg2)));

        //填入spine文件名
        loadAnimation("resources/img/monster/SalVientoBishopQuintus/enemy_1521_dslily.atlas", "resources/img/monster/SalVientoBishopQuintus/enemy_1521_dslily.json", 1.5F);
        this.flipHorizontal = true;
        this.state.setTimeScale(1.25f);
        this.state.addAnimation(0, "Idle", true, 0.0F);
        this.nextMove = 0;
        this.turn = 0;
        this.impairmentCount1 = impairmentCount1;
        this.impairmentCount2 = impairmentCount2;
        getMove(0);
    }

    public void usePreBattleAction()
    {
        if (FriendlyPatch.FriendlyFields.FriendlyList.get(AbstractDungeon.player) == null);
        {
            FriendlyPatch.FriendlyFields.FriendlyList.set(AbstractDungeon.player, new ArrayList<AbstractFriendly>());
        }
        ErosionproofCoatingDevice device = new ErosionproofCoatingDevice(0, 0);
        AbstractDungeon.actionManager.addToBottom(
                (AbstractGameAction)new ApplyPowerAction((AbstractCreature)device, (AbstractCreature)this,
                        (AbstractPower)new CoatingDevicePower((AbstractCreature)device, 0)));
        FriendlyPatch.Inst().add(device);
        POSX[0] = 130;
        POSX[1] = -130;
        POSX[2] = 230;
        POSX[3] = -230;

        POSY[0] = MathUtils.random(-20F,20F);
        POSY[1] = MathUtils.random(-20F,20F);
        POSY[2] = MathUtils.random(-20F,20F);
        POSY[3] = MathUtils.random(-20F,20F);
        if (AbstractDungeon.getCurrRoom() instanceof com.megacrit.cardcrawl.rooms.MonsterRoomBoss)
        {
            CardCrawlGame.music.unsilenceBGM();
            AbstractDungeon.scene.fadeOutAmbiance();
            EventHelper.musicHelper.playBGM("MIZUKI_BOSS2_2_INTRO", "MIZUKI_BOSS2_2_LOOP");
        }
    }

    public void takeTurn()
    {
        AbstractPlayer p = AbstractDungeon.player;
        AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new WaitAction(1));
        switch (this.nextMove)
        {
            case 0:
                //注视：给予对方2/3点神经损伤与2层虚弱
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "SKILL2"));
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new SFXAction("MONSTER_SNECKO_GLARE"));
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new AnimateSlowAttackAction((AbstractCreature)this));
                addToTop(new ApplyImpairmentAction(new NervousImpairment(), p, impairmentCount1));
                AbstractDungeon.actionManager.addToBottom(
                        (AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)this,
                                (AbstractPower)new WeakPower((AbstractCreature)p, 2, true), 2));
                break;
            case 1:
                //大潮：造成14点伤害，给予敌人1/2点神经损伤
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "ATTACK"));
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new AnimateSlowAttackAction((AbstractCreature)this));
                addToBot((AbstractGameAction) new DamageAction((AbstractCreature)p, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                addToTop(new ApplyImpairmentAction(new NervousImpairment(), p, impairmentCount2));
                break;
            case 2:
                //崩塌：造成28点伤害
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "SKILL1"));
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new AnimateSlowAttackAction((AbstractCreature)this));
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new VFXAction((AbstractCreature)this, (AbstractGameEffect)
                        new ShockWaveEffect(this.hb.cX, this.hb.cY,
                                new Color(0.3F, 0.2F, 0.4F, 1.0F),
                                ShockWaveEffect.ShockWaveType.CHAOTIC), 1.0F));
                addToBot((AbstractGameAction) new DamageAction((AbstractCreature)p, this.damage.get(1), AbstractGameAction.AttackEffect.SMASH));
                break;
            case 3:
                //断裂生殖：生成1只触手，触手2血具有1层无实体，回合结束特性触发后自动消失
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "SKILL3"));
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new AnimateSlowAttackAction((AbstractCreature)this));
                summon(1);
        }
        turn++;
        getMove(this.nextMove);
    }

    protected void getMove(int i)
    {
        if (turn == 0)
        {
            setMove(MOVES[3], (byte)3, Intent.UNKNOWN);
            return;
        }
        int roll = AbstractDungeon.aiRng.random(119);
        if (roll < 25)
        {
            if (i == 0)
            {
                getMove(i);
                return;
            }
            setMove(MOVES[0], (byte)0, Intent.DEBUFF);
        }
        else if (roll < 65)
        {
            setMove(MOVES[1], (byte)1, Intent.ATTACK_DEBUFF, ((DamageInfo)this.damage.get(0)).base);
        }
        else if (roll < 100)
        {
            if (i == 2)
            {
                getMove(i);
                return;
            }
            setMove(MOVES[2], (byte)2, Intent.ATTACK, ((DamageInfo)this.damage.get(1)).base);
        }
        else
        {
            int count = 0;
            for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters)
            {
                if (!m.isDeadOrEscaped() && m.id.equals("Mizuki:SeabornsFilialGeneration"))
                {
                    count++;
                }
            }
            if (i == 3 || count >= 4)
            {
                getMove(i);
                return;
            }
            setMove(MOVES[3], (byte)3, Intent.UNKNOWN);
        }
    }

    public void changeState(String stateName)
    {
        if (stateName.equals("ATTACK"))
        {
            this.state.setAnimation(0, "Attack", false);
            this.state.addAnimation(0, "Default", false, 0.0F);
            this.state.addAnimation(0, "Idle", true, 0.0F);
        }
        if (stateName.equals("SKILL1"))
        {
            this.state.setAnimation(0, "Skill_01", false);
            this.state.addAnimation(0, "Default", false, 0.0F);
            this.state.addAnimation(0, "Idle", true, 0.0F);
        }
        if (stateName.equals("SKILL2"))
        {
            this.state.setAnimation(0, "Skill_02", false);
            this.state.addAnimation(0, "Default", false, 0.0F);
            this.state.addAnimation(0, "Idle", true, 0.0F);
        }
        if (stateName.equals("SKILL3"))
        {
            this.state.setAnimation(0, "Skill_03", false);
            this.state.addAnimation(0, "Default", false, 0.0F);
            this.state.addAnimation(0, "Idle", true, 0.0F);
        }
        if (stateName.equals("SKILL4"))
        {
            this.state.setAnimation(0, "Skill_04", false);
            this.state.addAnimation(0, "Default", false, 0.0F);
            this.state.addAnimation(0, "Idle", true, 0.0F);
        }
    }

    public void die()
    {
        for (int j = 0; j < FriendlyPatch.FriendlyFields.FriendlyList.get(AbstractDungeon.player).size(); j++)
        {
            if (FriendlyPatch.FriendlyFields.FriendlyList.get(AbstractDungeon.player).get(j) instanceof ErosionproofCoatingDevice)
            {
                FriendlyPatch.FriendlyFields.FriendlyList.get(AbstractDungeon.player).remove(j);
                break;
            }
        }
        for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters)
        {
            if (!m.isDeadOrEscaped() && m.id.equals("Mizuki:SeabornsFilialGeneration"))
            {
                addToBot(new SuicideAction(m));
            }
        }
        isDying = true;
        useFastShakeAnimation(5.0F);
        CardCrawlGame.screenShake.rumble(4.0F);
        super.die();
        onBossVictoryLogic();
    }

    private void summon(int amount)
    {
        int tentaclesSpawned = 0;
        for (int i = 0; tentaclesSpawned < amount && i < this.tentacles.length; i++)
        {
            if (this.tentacles[i] == null || this.tentacles[i].isDeadOrEscaped())
            {
                SeabornsFilialGeneration tentacleToSpawn;
                if (i == 1 || i == 3)
                {
                    tentacleToSpawn = new SeabornsFilialGeneration(POSX[i], POSY[i], false);
                }
                else
                {
                    tentacleToSpawn = new SeabornsFilialGeneration(POSX[i], POSY[i]);
                }

                tentacleToSpawn.drawX = (float) Settings.WIDTH * 0.25F + POSX[i] * Settings.xScale;
                tentacleToSpawn.drawY = AbstractDungeon.floorY + POSY[i] * Settings.yScale;

                this.tentacles[i] = tentacleToSpawn;
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new SpawnMonsterAction(tentacleToSpawn, true));
                AbstractDungeon.actionManager.addToBottom(
                        (AbstractGameAction)new ApplyPowerAction((AbstractCreature)tentacleToSpawn, (AbstractCreature)this,
                                (AbstractPower)new IntangiblePower((AbstractCreature)tentacleToSpawn, 1)));
                AbstractDungeon.actionManager.addToBottom(
                        (AbstractGameAction)new ApplyPowerAction((AbstractCreature)tentacleToSpawn, (AbstractCreature)this,
                                (AbstractPower)new HungerSensePower((AbstractCreature)tentacleToSpawn, -1)));
                tentaclesSpawned++;
            }
        }
    }
}
