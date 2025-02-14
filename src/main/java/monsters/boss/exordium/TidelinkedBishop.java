package monsters.boss.exordium;

import basemod.abstracts.AbstractCardModifier;
import basemod.cardmods.EtherealMod;
import basemod.cardmods.ExhaustMod;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.ClearCardQueueAction;
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
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import helper.EventHelper;
import helper.RejectionHelper;
import monsters.AbstractMizukiMonster;
import powers.AmmoPower;
import powers.StunMonsterPower;
import powers.SymbiosisPower;
import powers.WeakPointPower;

import java.util.Iterator;

public class TidelinkedBishop extends AbstractMizukiMonster
{
    //接潮主教

    public static final String ID = "Mizuki:TidelinkedBishop";

    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Mizuki:TidelinkedBishop");

    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    private int turn = 0;

    private int curseCount = 1;

    private int strengthCount = 1;

    private int blockCount = 8;

    public TidelinkedBishop(float x, float y)
    {

        super(NAME, "Mizuki:TidelinkedBishop", 90, 0.0F, 0.0F, 200.0F, 320.0F, null, x, y);
        this.type = EnemyType.BOSS;

        int dmg1;

        if (AbstractDungeon.ascensionLevel >= 9)
        {
            setHp(160);
        }
        else
        {
            setHp(150);
        }

        if (AbstractDungeon.ascensionLevel >= 19)
        {
            dmg1 = 23;
            this.strengthCount = 3;
            this.curseCount = 2;
        }
        else if (AbstractDungeon.ascensionLevel >= 4)
        {
            dmg1 = 23;
            this.strengthCount = 2;
            this.curseCount = 1;
        }
        else
        {
            dmg1 = 21;
            this.strengthCount = 2;
            this.curseCount = 1;
        }

        this.damage.add(new DamageInfo((AbstractCreature) this, MathUtils.floor(dmg1)));

        //填入spine文件名
        loadAnimation("resources/img/monster/TidelinkedBishop/enemy_2028_syevil.atlas", "resources/img/monster/TidelinkedBishop/enemy_2028_syevil.json", 1.5F);
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
        if (this.currentHealth == this.maxHealth && this.halfDead)
        {
            AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "RECOVER"));
            this.halfDead = false;
            (AbstractDungeon.getCurrRoom()).cannotLose = true;
            getMove(0);
            createIntent();
        }
    }

    @Override
    public void damage(DamageInfo info)
    {
        super.damage(info);
        if (this.currentHealth <= 0 && !this.halfDead)
        {
            AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "DIE_AND_RECOVERING"));
            this.halfDead = true;
            for (AbstractPower p : this.powers)
                p.onDeath();
            for (AbstractRelic r : AbstractDungeon.player.relics)
                r.onMonsterDeath(this);
            addToTop((AbstractGameAction)new ClearCardQueueAction());
            this.powers.removeIf(p -> p.type == AbstractPower.PowerType.DEBUFF || p.ID.equals("Shackled"));
            applyPowers();
            boolean allDead = true;
            for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters)
            {
                if (!m.halfDead)
                {
                    allDead = false;
                    break;
                }
            }
            if (!allDead)
            {
                if (this.nextMove != 4)
                {
                    setMove((byte)4, AbstractMonster.Intent.UNKNOWN);
                    createIntent();
                    AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new SetMoveAction(this, (byte)4, AbstractMonster.Intent.UNKNOWN));
                }
            }
            else
            {
                (AbstractDungeon.getCurrRoom()).cannotLose = false;
                this.halfDead = false;
                for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters)
                    m.die();
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

        (AbstractDungeon.getCurrRoom()).cannotLose = true;
        AbstractDungeon.actionManager.addToBottom(
                (AbstractGameAction)new ApplyPowerAction((AbstractCreature)this, (AbstractCreature)this,
                        new SymbiosisPower(this, 1)));
    }

    public void takeTurn()
    {
        AbstractPlayer p = AbstractDungeon.player;
        switch (this.nextMove)
        {
            case 0:
                //赐福
                for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters)
                {
                    if (!m.isDeadOrEscaped())
                    {
                        AbstractDungeon.actionManager.addToBottom(
                                (AbstractGameAction)new ApplyPowerAction((AbstractCreature)m, (AbstractCreature)m,
                                        (AbstractPower)new StrengthPower((AbstractCreature)m, strengthCount)));
                    }
                }
                break;
            case 1:
                //注视
                //将1/2张虚无的排异反应置入敌方抽牌堆。
                for (int i = 0;i < this.curseCount;i++)
                {
                    AbstractCard card = RejectionHelper.getRandomRejection();
                    CardModifierManager.addModifier(card, (AbstractCardModifier)new EtherealMod());
                    AbstractDungeon.actionManager.addToBottom(
                            (AbstractGameAction) new MakeTempCardInDrawPileAction(
                                    (AbstractCard) card, 1, true, true, false));
                }
                break;
            case 2:
                //责难
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "ATTACK"));
                addToBot((AbstractGameAction) new DamageAction((AbstractCreature)p, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                break;
            case 3:
                //警戒
                //给予全体敌人1层人工制品，全体获得8点格挡（进阶4）
                for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters)
                {
                    if (!m.isDeadOrEscaped())
                    {
                        addToBot((AbstractGameAction) new GainBlockAction(m, this.blockCount));
                        AbstractDungeon.actionManager.addToBottom(
                                (AbstractGameAction)new ApplyPowerAction((AbstractCreature)m, (AbstractCreature)this,
                                        (AbstractPower)new ArtifactPower((AbstractCreature)m, 1), 1));
                    }
                }
                break;
            case 4:
                //复活
                AbstractDungeon.actionManager.addToBottom(
                        (AbstractGameAction)new HealAction((AbstractCreature)this, (AbstractCreature)this, (int)(this.maxHealth * 0.34f)));
                break;
        }
        if (!halfDead)
            turn++;
        getMove(0);
    }

    protected void getMove(int i)
    {
        if (halfDead)
        {
            setMove((byte)4, AbstractMonster.Intent.UNKNOWN);
            return;
        }
        if (turn == 4)
        {
            turn = 1;
        }
        switch (turn)
        {
            case 0:
                //警戒
                setMove(MOVES[3], (byte)3, Intent.BUFF);
                break;
            case 1:
                //祝福
                boolean Dead = true;
                for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters)
                {
                    if (!m.halfDead && m != this)
                    {
                        Dead = false;
                        break;
                    }
                }
                if (!Dead)
                {
                    setMove(MOVES[0], (byte)0, Intent.BUFF);
                }
                else
                {
                    turn++;
                    getMove(0);
                }
                break;
            case 2:
                //注视
                setMove(MOVES[1], (byte)1, Intent.DEBUFF);
                break;
            case 3:
                //责难
                setMove(MOVES[2], (byte)2, Intent.ATTACK, ((DamageInfo)this.damage.get(0)).base);
                break;
        }
    }

    public void changeState(String stateName)
    {
        if (stateName.equals("ATTACK"))
        {
            this.state.setAnimation(0, "Attack", false);
            this.state.addAnimation(0, "Idle", true, 0.0F);
        }
        if (stateName.equals("DIE_AND_RECOVERING"))
        {
            this.state.setAnimation(0, "Die", false);
            this.state.addAnimation(0, "Die_Loop", true, 0.0F);
        }
        if (stateName.equals("RECOVER"))
        {
            this.state.setAnimation(0, "Die_Idle", false);
            this.state.addAnimation(0, "Idle", true, 0.0F);
        }
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