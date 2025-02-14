package monsters.normal.beyond;

import Impairment.CorrosionImpairment;
import actions.ApplyImpairmentAction;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import monsters.AbstractMizukiMonster;
import powers.LowAltitudeHoveringPower;

public class SkimmingSeaDrifter extends AbstractMizukiMonster
{
    //掠海漂移体
    //近地悬浮Low-Altitude Hovering


    public static final String ID = "Mizuki:SkimmingSeaDrifter";

    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Mizuki:SkimmingSeaDrifter");

    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    private int turns;

    private boolean hasEnhanced;

    public SkimmingSeaDrifter(float x, float y)
    {

        super(NAME, "Mizuki:SkimmingSeaDrifter", 90, 0.0F, 0.0F, 250.0F, 200.0F, null, x, y);
        int dmg1, dmg2;
        this.type = AbstractMonster.EnemyType.NORMAL;


        if (AbstractDungeon.ascensionLevel >= 7)
        {
            setHp(60, 69);
        }
        else
        {
            setHp(58, 64);
        }


        if (AbstractDungeon.ascensionLevel >= 17)
        {
            dmg1 = 12;
        }
        else if (AbstractDungeon.ascensionLevel >= 2)
        {
            dmg1 = 12;
        }
        else
        {
            dmg1 = 10;
        }

        if (AbstractDungeon.ascensionLevel >= 17)
        {
            dmg2 = 6;
        }
        else if (AbstractDungeon.ascensionLevel >= 2)
        {
            dmg2 = 6;
        }
        else
        {
            dmg2 = 5;
        }

        this.damage.add(new DamageInfo((AbstractCreature) this, MathUtils.floor(dmg1)));
        this.damage.add(new DamageInfo((AbstractCreature) this, MathUtils.floor(dmg2)));

        loadAnimation("resources/img/monster/SkimmingSeaDrifter/enemy_2025_syufo.atlas", "resources/img/monster/SkimmingSeaDrifter/enemy_2025_syufo.json", 1.5F);
        this.flipHorizontal = true;
        this.state.addAnimation(0, "Idle_01", true, 0.0F);
        this.nextMove = 0;
        this.turns = 0;
    }

    public void usePreBattleAction()
    {
        AbstractDungeon.actionManager.addToBottom(
                (AbstractGameAction)new ApplyPowerAction((AbstractCreature)this, (AbstractCreature)this,
                        (AbstractPower)new LowAltitudeHoveringPower((AbstractCreature)this, 1)));
    }

    public void takeTurn()
    {
        AbstractPlayer p = AbstractDungeon.player;

        switch (this.nextMove)
        {
            case 0:
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "ATTACK"));
                addToBot((AbstractGameAction) new DamageAction((AbstractCreature)p, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));

                addToBot(new ApplyImpairmentAction(new CorrosionImpairment(), p, 1));
                break;
            case 1:
                AbstractDungeon.actionManager.addToBottom(
                        (AbstractGameAction)new ApplyPowerAction((AbstractCreature)this, (AbstractCreature)this,
                                (AbstractPower)new StrengthPower((AbstractCreature)this, 2), 2));

                break;
            case 2:
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "ATTACK"));
                addToBot((AbstractGameAction) new DamageAction((AbstractCreature)p, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "ATTACK"));
                addToBot((AbstractGameAction) new DamageAction((AbstractCreature)p, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_LIGHT));

                addToBot(new ApplyImpairmentAction(new CorrosionImpairment(), p, 2));
                break;
        }
        getMove(0);
    }

    protected void getMove(int i)
    {
        if (AbstractDungeon.ascensionLevel >= 17 )
        {
            if (turns == 3)
            {
                turns = 0;
            }
            if (turns == 0)
            {
                setMove(MOVES[1], (byte)1, AbstractMonster.Intent.BUFF);
                turns++;
                return;
            }
            if (turns == 1)
            {
                setMove(MOVES[0], (byte)0, AbstractMonster.Intent.ATTACK_DEBUFF, ((DamageInfo)this.damage.get(0)).base);
                turns++;
                return;
            }
            if (turns == 2)
            {
                setMove(MOVES[2], (byte)2, AbstractMonster.Intent.ATTACK_DEBUFF, ((DamageInfo)this.damage.get(1)).base,2, true);
                turns++;
                return;
            }
        }


        int roll = AbstractDungeon.aiRng.random(99);
        if (roll < 33)
        {
            setMove(MOVES[0], (byte)0, AbstractMonster.Intent.ATTACK_DEBUFF, ((DamageInfo)this.damage.get(0)).base);
        }
        else if (roll < 66)
        {
            setMove(MOVES[1], (byte)1, AbstractMonster.Intent.BUFF);
        }
        else
        {
            setMove(MOVES[2], (byte)2, AbstractMonster.Intent.ATTACK_DEBUFF, ((DamageInfo)this.damage.get(1)).base,2, true);
        }
    }


    public void changeState(String stateName)
    {
        if (this.hasPower(LowAltitudeHoveringPower.id))
        {
            if (stateName.equals("ATTACK"))
            {
                this.state.setAnimation(0, "Attack_01", false);
                this.state.addAnimation(0, "Idle_01", true, 0.0F);
            }
        }
        else
        {
            if (stateName.equals("ATTACK"))
            {
                this.state.setAnimation(0, "Attack_02", false);
                this.state.addAnimation(0, "Idle_02", true, 0.0F);
            }
        }
        if (stateName.equals("GROUNDED"))
        {
            this.state.setAnimation(0, "Change", false);
            this.state.addAnimation(0, "Idle_02", true, 0.0F);
        }
    }

    public void die()
    {
        if (this.hasPower(LowAltitudeHoveringPower.id))
        {
            this.state.setAnimation(0, "Die_01", false);
        }
        else
        {
            this.state.setAnimation(0, "Die_02", false);
        }
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
}