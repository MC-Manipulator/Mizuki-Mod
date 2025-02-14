package monsters.special;

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
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import monsters.AbstractMizukiMonster;
import powers.AmmoPower;
import powers.WeakPointPower;

public class SarkazGrudgebearer extends AbstractMizukiMonster
{
    //萨卡兹积怨者
    //积怨：提升力量
    //仇恨连击：3x9点伤害
    //积怨=>仇恨连击


    public static final String ID = "Mizuki:SarkazGrudgebearer";

    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Mizuki:SarkazGrudgebearer");

    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    public int turn = 0;

    public boolean alarm = false;

    public SarkazGrudgebearer(float x, float y)
    {

        super(NAME, "Mizuki:SarkazGrudgebearer", 90, 0.0F, 0.0F, 250.0F, 320.0F, null, x, y);
        this.type = EnemyType.ELITE;

        int dmg1;

        //生命值应当较高
        if (AbstractDungeon.ascensionLevel >= 8)
        {
            setHp(100, 100);
        }
        else
        {
            setHp(90, 95);
        }

        if (AbstractDungeon.ascensionLevel >= 18)
        {
            dmg1 = 11;
        }
        else if (AbstractDungeon.ascensionLevel >= 3)
        {
            dmg1 = 11;
        }
        else
        {
            dmg1 = 9;
        }

        this.damage.add(new DamageInfo((AbstractCreature) this, MathUtils.floor(dmg1)));

        loadAnimation("resources/img/monster/SarkazGrudgebearer/enemy_1074_dbskar.atlas", "resources/img/monster/SarkazGrudgebearer/enemy_1074_dbskar.json", 1.2F);
        this.flipHorizontal = true;
        this.stateData.setMix("Idle", "Die", 0.1F);
        this.state.setAnimation(0, "Skill_Begin", false);
        this.state.setAnimation(0, "Skill_Idle", true);
        this.nextMove = 0;
    }

    public void takeTurn()
    {
        AbstractPlayer p = AbstractDungeon.player;

        switch (this.nextMove)
        {
            case 0:
                AbstractDungeon.actionManager.addToBottom(
                        (AbstractGameAction)new ApplyPowerAction((AbstractCreature)this, (AbstractCreature)this,
                                (AbstractPower)new StrengthPower((AbstractCreature)this, 2)));
                break;
            case 1:
                attack();
                attack();
                attack();
                break;
        }
        turn++;
        getMove(0);
    }

    protected void getMove(int i)
    {
        if (turn >= 2)
        {
            turn = 0;
        }
        //积怨=>仇恨连击
        switch (turn)
        {
            case 0:
                //0号技能：积怨
                setMove(MOVES[0], (byte)0, Intent.BUFF);
                break;
            case 1:
                //1号技能：仇恨连击
                setMove(MOVES[1], (byte)1, Intent.ATTACK, ((DamageInfo)this.damage.get(0)).base, 3, true);
                break;
        }
    }

    public void changeState(String stateName)
    {
        if (stateName.equals("ATTACK"))
        {
            this.state.setAnimation(0, "Skill_Attack", false);
            this.state.addAnimation(0, "Skill_Idle", true, 0.0F);
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

    private void attack()
    {
        AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "ATTACK"));
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
                    this.duration = 0.7F;
                    justIn = false;
                }

                tickDuration();
                if (this.isDone)
                {
                    AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AttackEffect.BLUNT_LIGHT));
                    AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AttackEffect.POISON));
                    this.target.damage(damage.get(0));

                    addToTop((AbstractGameAction) new WaitAction(0.1F));
                    if ((AbstractDungeon.getCurrRoom()).monsters.areMonstersBasicallyDead())
                        AbstractDungeon.actionManager.clearPostCombatActions();
                }
            }
        });
    }
}
