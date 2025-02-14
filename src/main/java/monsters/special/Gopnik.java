package monsters.special;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import monsters.AbstractMizukiMonster;
import powers.ChargePower;
import powers.StealthPower;

public class Gopnik extends AbstractMizukiMonster
{
    //高普尼克
    public static final String ID = "Mizuki:Gopnik";

    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Mizuki:Gopnik");

    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    public int sp = 0;

    public Gopnik(float x, float y)
    {
        super(NAME, "Mizuki:Gopnik", 90, 0.0F, 0.0F, 150.0F, 320.0F, null, x, y);
        int dmg1, hp;
        this.type = AbstractMonster.EnemyType.ELITE;


        if (AbstractDungeon.ascensionLevel >= 18)
        {
            dmg1 = 26;
        }
        else if (AbstractDungeon.ascensionLevel >= 3)
        {
            dmg1 = 26;
        }
        else
        {
            dmg1 = 24;
        }

        this.damage.add(new DamageInfo((AbstractCreature) this, MathUtils.floor(dmg1)));

        if (AbstractDungeon.ascensionLevel >= 8)
        {
            hp = 178;
        }
        else
        {
            hp = 170;
        }

        setHp(MathUtils.floor(hp));
        loadAnimation("resources/img/monster/Gopnik/enemy_2002_bearmi.atlas", "resources/img/monster/Gopnik/enemy_2002_bearmi.json", 1.5F);
        this.flipHorizontal = true;
        this.state.addAnimation(0, "Idle", true, 0.0F);
        this.nextMove = 0;
        this.stateData.setMix("Idle", "Die", 0.1F);
        this.sp = 0;
    }

    public Gopnik(float x, float y, int type)
    {
        this(x, y);
        if (type == 0)
        {
            return;
        }
        else
        {
            int dmg1, dmg2, hp;
            if (AbstractDungeon.ascensionLevel >= 18)
            {
                dmg1 = 35;
            }
            else if (AbstractDungeon.ascensionLevel >= 3)
            {
                dmg1 = 35;
            }
            else
            {
                dmg1 = 30;
            }

            this.damage.clear();
            this.damage.add(new DamageInfo((AbstractCreature) this, MathUtils.floor(dmg1)));

            if (AbstractDungeon.ascensionLevel >= 8)
            {
                hp = 210;
            }
            else
            {
                hp = 200;
            }
            setHp(MathUtils.floor(hp));
        }
    }

    public void takeTurn()
    {
        AbstractPlayer p = AbstractDungeon.player;
        switch (this.nextMove)
        {
            case 0:
                addToBot(new AbstractGameAction()
                {

                    boolean justIn = true;
                    @Override
                    public void update()
                    {
                        if (justIn)
                        {
                            justIn = false;
                            CardCrawlGame.sound.play("MIZUKI_Gopnik_HitCast", 0.05F);
                            this.duration = 0.8f;
                        }
                        this.duration -= Gdx.graphics.getDeltaTime();
                        if (this.duration <= 0)
                        {
                            isDone = true;
                        }
                    }
                });
                this.state.setAnimation(0, "Attack", false);
                this.state.addAnimation(0, "Idle", true, 0.0F);
                addToBot((AbstractGameAction) new DamageAction((AbstractCreature)p, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                addToBot(new AbstractGameAction()
                {
                    @Override
                    public void update()
                    {
                        CardCrawlGame.sound.play("MIZUKI_Gopnik_Hit", 0.05F);
                        isDone = true;
                    }
                });
                break;
            case 1:
                AbstractDungeon.actionManager.addToBottom(
                        (AbstractGameAction)new ApplyPowerAction((AbstractCreature)this, (AbstractCreature)this,
                                (AbstractPower)new ChargePower((AbstractCreature)this, 1), 1));
                break;
        }
        getMove(0);
    }

    protected void getMove(int i)
    {
        if (sp < 2)
        {
            //重拳
            setMove(MOVES[0], (byte)0, AbstractMonster.Intent.ATTACK, ((DamageInfo)this.damage.get(0)).base);
            sp++;
        }
        else
        {
            //蓄力
            sp = 0;
            setMove(MOVES[1], (byte)1, Intent.BUFF);
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
                CardCrawlGame.sound.play("MIZUKI_Gopnik_Die", 0.05F);
                isDone = true;
            }
        });
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
        if (AbstractDungeon.ascensionLevel >= 13)
        {
            AbstractDungeon.getCurrRoom().addGoldToRewards(50);
        }
        else
        {
            AbstractDungeon.getCurrRoom().addGoldToRewards(100);
        }
        AbstractDungeon.getCurrRoom().addRelicToRewards(AbstractRelic.RelicTier.UNCOMMON);
        super.die();
    }
}