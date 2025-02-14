package monsters.special;

import Impairment.CorrosionImpairment;
import actions.ApplyImpairmentAction;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FadingPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import modcore.MizukiModCore;
import monsters.AbstractMizukiMonster;
import powers.DyingPower;
import powers.ErgodicPower;
import powers.FractalPower;

public class PathshaperFractal extends AbstractMizukiMonster
{
    //塑路者分形
    public static final String ID = "Mizuki:PathshaperFractal";

    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Mizuki:PathshaperFractal");

    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    private int block = 0;

    private int summon = 0;

    public int position = 0;

    public PathshaperFractal(float x, float y)
    {

        super(NAME, "Mizuki:PathshaperFractal", 90, 0.0F, 0.0F, 150.0F, 320.0F, null, x, y);

        if (AbstractDungeon.ascensionLevel >= 8)
        {
            setHp(25);
        }
        else
        {
            setHp(20);
        }

        int dmg1, dmg2, block;

        if (AbstractDungeon.ascensionLevel >= 19)
        {
            dmg1 = 5;
            dmg2 = 2;
            block = 3;
        }
        else if (AbstractDungeon.ascensionLevel >= 4)
        {
            dmg1 = 5;
            dmg2 = 2;
            block = 2;
        }
        else
        {
            dmg1 = 4;
            dmg2 = 2;
            block = 2;
        }


        this.damage.add(new DamageInfo((AbstractCreature) this, MathUtils.floor(dmg1)));
        this.damage.add(new DamageInfo((AbstractCreature) this, MathUtils.floor(dmg2)));
        this.block = block;

        loadAnimation("resources/img/monster/PathshaperFractal/enemy_2033_syboys.atlas", "resources/img/monster/PathshaperFractal/enemy_2033_syboys.json", 2F);
        this.flipHorizontal = true;
        this.state.addAnimation(0, "Idle", true, 0.0F);
        this.stateData.setMix("Idle", "Die", 0.1F);
        AbstractDungeon.actionManager.addToBottom(
                (AbstractGameAction)new ApplyPowerAction((AbstractCreature)this, (AbstractCreature)this,
                        (AbstractPower)new FadingPower((AbstractCreature)this, 3)));
        AbstractDungeon.actionManager.addToBottom(
                (AbstractGameAction)new ApplyPowerAction((AbstractCreature)this, (AbstractCreature)this,
                        (AbstractPower)new ErgodicPower((AbstractCreature)this, 0, 2)));
        getMove(0);
    }

    public void takeTurn()
    {
        AbstractPlayer p = AbstractDungeon.player;

        switch (this.nextMove)
        {
            case 0:
                //开路
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "ATTACK"));
                attack(this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HEAVY);
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new MakeTempCardInDiscardAction((AbstractCard) new Dazed(), 1));
                break;
                //1张眩晕塞入弃牌堆
            case 1:
                //反哺
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "SKILL"));
                attack(this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_LIGHT);
                for (AbstractMonster m1 : (AbstractDungeon.getCurrRoom()).monsters.monsters)
                {
                    if (!m1.isDeadOrEscaped() && m1.id.equals("Mizuki:Pathshaper"))
                    {
                        addToBot(new GainBlockAction(m1, this.block));
                    }
                }
                break;
        }
        /*
        summon++;
        if ((this.hasPower(ErgodicPower.id) && summon >= this.getPower(ErgodicPower.id).amount))
        {
            //生成塑路者分形
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
            AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new SpawnMonsterAction(fracToSpawn, true));
            fracToSpawn.createIntent();
            summon = 0;
        }*/
        getMove(0);
    }

    protected void getMove(int i)
    {
        int roll = AbstractDungeon.aiRng.random(99);
        if (roll < 50)
        {
            setMove(MOVES[0], (byte)0, Intent.ATTACK_DEBUFF, damage.get(0).base);
        }
        else
        {
            setMove(MOVES[1], (byte)1, Intent.ATTACK_DEFEND, damage.get(1).base);
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
        this.state.setAnimation(0, "Die", false);
        super.die();
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
