package monsters.normal.beyond;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import monsters.AbstractMizukiMonster;
import powers.DeadlyRhythmPower;
import powers.StealthPower;

public class NetherseaReefbreaker extends AbstractMizukiMonster
{
    //深溟裂礁者

    public static final String ID = "Mizuki:NetherseaReefbreaker";

    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Mizuki:NetherseaReefbreaker");

    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    private int turn = 0;

    public NetherseaReefbreaker(float x, float y)
    {

        super(NAME, "Mizuki:NetherseaReefbreaker", 90, 0.0F, 0.0F, 150.0F, 320.0F, null, x, y);
        int dmg1, dmg2;
        this.type = AbstractMonster.EnemyType.NORMAL;


        if (AbstractDungeon.ascensionLevel >= 7)
        {
            setHp(190, 190);
        }
        else
        {
            setHp(170, 170);
        }

        dmg1 = 10;
        if (AbstractDungeon.ascensionLevel >= 17)
        {
            dmg2 = 12;
        }
        else if (AbstractDungeon.ascensionLevel >= 2)
        {
            dmg2 = 12;
        }
        else
        {
            dmg2 = 10;
        }

        this.damage.add(new DamageInfo((AbstractCreature) this, MathUtils.floor(dmg1)));
        this.damage.add(new DamageInfo((AbstractCreature)this, MathUtils.floor(dmg2)));
        loadAnimation("resources/img/monster/NetherseaReefbreaker/enemy_1235_dsbskr.atlas", "resources/img/monster/NetherseaReefbreaker/enemy_1235_dsbskr.json", 1.2F);
        this.stateData.setMix("Idle", "Die", 0.1F);
        this.flipHorizontal = true;
        this.state.addAnimation(0, "Idle", true, 0.0F);
        this.nextMove = 0;
        this.turn = 0;
    }

    public void usePreBattleAction()
    {
        AbstractDungeon.actionManager.addToBottom(
                (AbstractGameAction)new ApplyPowerAction((AbstractCreature)this, (AbstractCreature)this,
                        (AbstractPower)new DeadlyRhythmPower((AbstractCreature)this, 2)));
    }

    public void takeTurn()
    {
        AbstractPlayer p = AbstractDungeon.player;

        switch (this.nextMove)
        {
            case 0:
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "ATTACK"));
                addToBot((AbstractGameAction) new DamageAction((AbstractCreature)p, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "ATTACK"));
                addToBot((AbstractGameAction) new DamageAction((AbstractCreature)p, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HEAVY));

                break;
            case 1:
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "ATTACK2"));
                addToBot((AbstractGameAction) new DamageAction((AbstractCreature)p, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_HEAVY));

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
            case 2:
                AbstractDungeon.actionManager.addToBottom(
                        (AbstractGameAction)new ApplyPowerAction((AbstractCreature)this, (AbstractCreature)this,
                                (AbstractPower)new StealthPower((AbstractCreature)this, 1), 1));
                AbstractDungeon.actionManager.addToBottom(
                        (AbstractGameAction)new ApplyPowerAction((AbstractCreature)this, (AbstractCreature)this,
                                (AbstractPower)new StrengthPower((AbstractCreature)this, 1), 1));
                break;
        }
        getMove(0);
    }

    protected void getMove(int i)
    {
        if (AbstractDungeon.ascensionLevel >= 17)
        {
            turn++;
            if (turn >= 3)
            {
                setMove(MOVES[2], (byte)2, Intent.BUFF);
                turn = 0;
            }
        }
        int roll = AbstractDungeon.aiRng.random(99);
        if (roll < 40)
        {
            setMove(MOVES[0], (byte)0, Intent.ATTACK, ((DamageInfo)this.damage.get(0)).base, 2, true);
        }
        else if (roll < 80)
        {
            setMove(MOVES[1], (byte)1, Intent.ATTACK, ((DamageInfo)this.damage.get(1)).base);
        }
        else
        {
            setMove(MOVES[2], (byte)2, Intent.BUFF);
        }
    }


    public void changeState(String stateName)
    {
        if (stateName.equals("ATTACK"))
        {
            this.state.setAnimation(0, "Attack", false);
            this.state.addAnimation(0, "Idle", true, 0.0F);
        }
        if (stateName.equals("ATTACK2"))
        {
            this.state.setAnimation(0, "Attack_2", false);
            this.state.addAnimation(0, "Idle", true, 0.0F);
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
}
