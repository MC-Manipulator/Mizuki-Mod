package monsters.boss.city;

import Impairment.NervousImpairment;
import actions.ApplyImpairmentAction;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.IntangiblePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import monsters.AbstractMizukiMonster;
import monsters.friendlys.ErosionproofCoatingDevice;
import monsters.friendlys.Lumen;
import monsters.special.SeabornsFilialGeneration;
import patches.FriendlyPatch;
import powers.BloodConnectedPower;
import powers.HungerSensePower;

public class SalVientoBishopQuintusBody extends AbstractMizukiMonster
{
    //昆图斯躯干
    public static final String ID = "Mizuki:SalVientoBishopQuintusBody";

    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Mizuki:SalVientoBishopQuintusBody");

    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    private boolean mainBodyHasDied = false;

    SalVientoBishopQuintus mainBody = null;

    public SalVientoBishopQuintusBody(float x, float y)
    {

        super(NAME, "Mizuki:SalVientoBishopQuintusBody", 90, 0.0F, 0.0F, 200.0F, 320.0F, null, x, y);
        this.type = EnemyType.BOSS;

        int dmg1, dmg2;

        if (AbstractDungeon.ascensionLevel >= 9)
        {
            setHp(70);
        }
        else
        {
            setHp(60);
        }

        if (AbstractDungeon.ascensionLevel >= 19)
        {
            dmg1 = 6;
            dmg2 = 13;
        }
        else if (AbstractDungeon.ascensionLevel >= 4)
        {
            dmg1 = 6;
            dmg2 = 13;
        }
        else
        {
            dmg1 = 4;
            dmg2 = 10;
        }

        this.damage.add(new DamageInfo((AbstractCreature) this, MathUtils.floor(dmg1)));
        this.damage.add(new DamageInfo((AbstractCreature) this, MathUtils.floor(dmg2)));

        //填入spine文件名
        //loadAnimation("resources/img/monster/SalVientoBishopQuintus/enemy_1521_dslily.atlas", "resources/img/monster/SalVientoBishopQuintus/enemy_1521_dslily.json", 1.5F);
        this.flipHorizontal = true;
        //this.state.setTimeScale(1.25f);
        //this.state.addAnimation(0, "Idle", true, 0.0F);
        this.nextMove = 0;
        this.mainBodyHasDied = false;
        getMove(0);
    }

    @Override
    public void update()
    {
        super.update();
        if (this.mainBody.isDying && !this.mainBodyHasDied)
        {
            this.mainBodyHasDied = true;
            addToBot((AbstractGameAction)new SuicideAction(this));
        }
    }

    public void usePreBattleAction()
    {
        //血肉相连
        AbstractDungeon.actionManager.addToBottom(
                (AbstractGameAction)new ApplyPowerAction((AbstractCreature)this, (AbstractCreature)this,
                        (AbstractPower)new BloodConnectedPower((AbstractCreature)this, -1)));
        for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters)
        {
            if (!m.isDeadOrEscaped() && m.id.equals("Mizuki:SalVientoBishopQuintus"))
            {
                mainBody = (SalVientoBishopQuintus)m;
            }
        }
    }

    @Override
    public void damage(DamageInfo info)
    {
        super.damage(info);
        if (mainBody != null)
        {
            mainBody.damage(info);
        }
    }

    public void takeTurn()
    {
        AbstractPlayer p = AbstractDungeon.player;
        AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new WaitAction(1));
        switch (this.nextMove)
        {
            case 0:
                //大潮：造成4点伤害
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(mainBody, "ATTACK"));
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new AnimateSlowAttackAction((AbstractCreature)mainBody));
                addToBot((AbstractGameAction) new DamageAction((AbstractCreature)p, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                break;
            case 1:
                //崩塌：造成10点伤害
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(mainBody, "SKILL1"));
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new AnimateSlowAttackAction((AbstractCreature)mainBody));
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new VFXAction((AbstractCreature)mainBody, (AbstractGameEffect)
                        new ShockWaveEffect(mainBody.hb.cX, mainBody.hb.cY,
                                new Color(0.3F, 0.2F, 0.4F, 1.0F),
                                ShockWaveEffect.ShockWaveType.CHAOTIC), 1.0F));
                addToBot((AbstractGameAction) new DamageAction((AbstractCreature)p, this.damage.get(1), AbstractGameAction.AttackEffect.SMASH));
                break;
        }
        getMove(this.nextMove);
    }

    protected void getMove(int i)
    {
        int roll = AbstractDungeon.aiRng.random(99);
        if (roll < 75)
        {
            setMove(MOVES[0], (byte)0, Intent.ATTACK, ((DamageInfo)this.damage.get(0)).base);
        }
        else
        {
            setMove(MOVES[1], (byte)1, Intent.ATTACK, ((DamageInfo)this.damage.get(1)).base);
        }
    }

    public void die()
    {
        isDying = true;
        useFastShakeAnimation(5.0F);
        CardCrawlGame.screenShake.rumble(4.0F);
        super.die();
        //onBossVictoryLogic();
    }
}
