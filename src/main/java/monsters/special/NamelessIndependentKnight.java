package monsters.special;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
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

public class NamelessIndependentKnight extends AbstractMizukiMonster
{
    //无名独立骑士，第二层

    public static final String ID = "Mizuki:NamelessIndependentKnight";

    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Mizuki:NamelessIndependentKnight");

    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    private int block = 10;

    public NamelessIndependentKnight(float x, float y)
    {

        super(NAME, "Mizuki:NamelessIndependentKnight", 90, 0.0F, 0.0F, 150.0F, 320.0F, null, x, y);
        int dmg1, dmg2, block;
        this.type = AbstractMonster.EnemyType.NORMAL;


        if (AbstractDungeon.ascensionLevel >= 7)
        {
            setHp(48, 52);
        }
        else
        {
            setHp(40, 44);
        }


        if (AbstractDungeon.ascensionLevel >= 17)
        {
            dmg1 = 12;
            dmg2 = 17;
        }
        else if (AbstractDungeon.ascensionLevel >= 2)
        {
            dmg1 = 12;
            dmg2 = 17;
        }
        else
        {
            dmg1 = 10;
            dmg2 = 15;
        }

        block = 15;

        this.block = block;

        this.damage.add(new DamageInfo((AbstractCreature) this, MathUtils.floor(dmg1)));
        this.damage.add(new DamageInfo((AbstractCreature) this, MathUtils.floor(dmg2)));

        loadAnimation("resources/img/monster/NamelessIndependentKnight/enemy_1099_nbkght.atlas", "resources/img/monster/NamelessIndependentKnight/enemy_1099_nbkght.json", 1.6F);
        this.stateData.setMix("Idle", "Die", 0.1F);
        this.flipHorizontal = true;
        this.state.addAnimation(0, "Idle", true, 0.0F);
        this.nextMove = 0;
    }

    public void takeTurn()
    {
        AbstractPlayer p = AbstractDungeon.player;

        boolean leaderExist = ifLeaderExist();


        switch (this.nextMove)
        {
            case 0:
                addToBot((AbstractGameAction) new DamageAction((AbstractCreature)p, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "ATTACK"));
                break;
            case 1:
                if (leaderExist)
                {
                    int leaderIntent = getLeaderIntent();
                    switch (leaderIntent)
                    {
                        case 0:
                        case 1:
                            AbstractDungeon.actionManager.addToBottom(
                                    (AbstractGameAction)new GainBlockAction(this, block));
                            break;
                        case 2:
                            AbstractDungeon.actionManager.addToBottom(
                                    (AbstractGameAction)new ApplyPowerAction((AbstractCreature)this, (AbstractCreature)this,
                                            (AbstractPower)new StrengthPower((AbstractCreature)this, 3), 3));

                            break;
                        case 3:
                            addToBot((AbstractGameAction) new DamageAction((AbstractCreature)p, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                            break;
                    }
                }
                else
                {
                    AbstractDungeon.actionManager.addToBottom(
                            (AbstractGameAction)new ApplyPowerAction((AbstractCreature)this, (AbstractCreature)this,
                                    (AbstractPower)new StrengthPower((AbstractCreature)this, -5), -5));
                }
                break;
        }
        getMove(0);
    }

    protected boolean ifLeaderExist()
    {
        for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters)
        {
            if (!m.isDeadOrEscaped() && m.id.equals("Mizuki:BladehelmKnightclubTrainee"))
            {
                return true;
            }
        }
        return false;
    }

    protected int getLeaderIntent()
    {
        for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters)
        {
            if (!m.isDeadOrEscaped() && m.id.equals("Mizuki:BladehelmKnightclubTrainee"))
            {
                return ((BladehelmKnightclubTrainee)m).leaderIntent;
            }
        }
        return 4;
    }

    protected void getMove(int i)
    {
        boolean leaderExist = ifLeaderExist();

        if (leaderExist)
        {
            setMove(MOVES[1], (byte)1, Intent.UNKNOWN);
        }
        else
        {
            setMove(MOVES[0], (byte)0, Intent.ATTACK, ((DamageInfo)this.damage.get(0)).base);
        }
    }


    public void changeState(String stateName)
    {
        if (stateName.equals("ATTACK"))
        {
            this.state.setAnimation(0, "Attack", false);
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
