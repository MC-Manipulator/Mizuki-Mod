package monsters.normal.city;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import monsters.AbstractMizukiMonster;
import powers.DreamInterruptedPower;


public class BasinSeaReaper extends AbstractMizukiMonster
{
    //钵海收割者
    public static final String ID = "Mizuki:BasinSeaReaper";

    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Mizuki:BasinSeaReaper");

    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    private boolean isOut = false;

    private boolean asleep;

    private boolean isOutTriggered = false;

    private int idleCount = 0;
    public BasinSeaReaper(float x, float y)
    {

        super(NAME, "Mizuki:BasinSeaReaper", 90, 0.0F, 0.0F, 150.0F, 320.0F, null, x, y);
        int dmg1, dmg2, dmg3;
        this.type = EnemyType.NORMAL;



        if (AbstractDungeon.ascensionLevel >= 7)
        {
            setHp(106, 110);
        }
        else
        {
            setHp(100, 104);
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
            dmg2 = 21;
        }
        else if (AbstractDungeon.ascensionLevel >= 2)
        {
            dmg2 = 21;
        }
        else
        {
            dmg2 = 18;
        }

        if (AbstractDungeon.ascensionLevel >= 17)
        {
            dmg3 = 6;
        }
        else if (AbstractDungeon.ascensionLevel >= 2)
        {
            dmg3 = 6;
        }
        else
        {
            dmg3 = 5;
        }

        this.damage.add(new DamageInfo((AbstractCreature) this, MathUtils.floor(dmg1)));
        this.damage.add(new DamageInfo((AbstractCreature) this, MathUtils.floor(dmg2)));
        this.damage.add(new DamageInfo((AbstractCreature) this, MathUtils.floor(dmg3)));

        loadAnimation("resources/img/monster/BasinSeaReaper/enemy_1151_dslntc.atlas", "resources/img/monster/BasinSeaReaper/enemy_1151_dslntc.json", 1.5F);
        this.flipHorizontal = true;
        this.state.addAnimation(0, "Idle", true, 0.0F);
        this.nextMove = 3;
        this.stateData.setMix("Idle", "Die", 0.1F);
        this.isOut = false;
        this.isOutTriggered = false;
    }

    public void takeTurn()
    {
        AbstractPlayer p = AbstractDungeon.player;

        switch (this.nextMove)
        {
            case 0:
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "ATTACK"));
                addToBot((AbstractGameAction) new DamageAction((AbstractCreature)p, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                AbstractDungeon.actionManager.addToBottom(
                        (AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)this,
                                (AbstractPower)new VulnerablePower((AbstractCreature)p, 2, true), 2));
                break;
            case 1:
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "ATTACK"));
                addToBot((AbstractGameAction) new DamageAction((AbstractCreature)p, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                break;
            case 2:
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "ATTACK"));
                addToBot((AbstractGameAction) new DamageAction((AbstractCreature)p, this.damage.get(2), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "ATTACK"));
                addToBot((AbstractGameAction) new DamageAction((AbstractCreature)p, this.damage.get(2), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                AbstractDungeon.actionManager.addToBottom(
                        (AbstractGameAction)new MakeTempCardInDrawPileAction((AbstractCard)new Dazed(), 2, true, true));
                break;
            case 3:
                idleCount++;
                if (this.idleCount >= 4)
                {
                    this.isOutTriggered = true;
                    AbstractDungeon.actionManager.addToBottom(
                            (AbstractGameAction)new ApplyPowerAction((AbstractCreature)this, (AbstractCreature)this,
                                    (AbstractPower)new DreamInterruptedPower((AbstractCreature)this, 1, false)));
                    AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "AWAKE"));
                }
                break;
        }
        getMove(0);
    }

    protected void getMove(int i)
    {
        if (!this.isOutTriggered)
        {
            setMove(MOVES[3], (byte)3, Intent.SLEEP);
        }
        else
        {
            int roll = AbstractDungeon.aiRng.random(99);
            if (roll < 33)
            {
                setMove(MOVES[0], (byte)0, Intent.ATTACK_DEBUFF, ((DamageInfo)this.damage.get(0)).base);
            }
            else if (roll < 66)
            {
                setMove(MOVES[1], (byte)1, Intent.ATTACK, ((DamageInfo)this.damage.get(1)).base);
            }
            else
            {
                setMove(MOVES[2], (byte)2, Intent.ATTACK_DEBUFF, ((DamageInfo)this.damage.get(2)).base,2, true);
            }
        }
    }

    public void damage(DamageInfo info)
    {
        int previousHealth = this.currentHealth;
        super.damage(info);
        if (this.currentHealth != previousHealth && !this.isOutTriggered)
        {
            this.isOutTriggered = true;
            getMove(0);
            createIntent();
            AbstractDungeon.actionManager.addToBottom(
                    (AbstractGameAction)new ApplyPowerAction((AbstractCreature)this, (AbstractCreature)this,
                            (AbstractPower)new DreamInterruptedPower((AbstractCreature)this, 1, true)));
            AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "AWAKE"));
        }
    }

    public void changeState(String stateName)
    {
        if (stateName.equals("ATTACK"))
        {
            this.state.setAnimation(0, "Skill_Attack", false);
            this.state.addAnimation(0, "Skill_Idle", true, 0.0F);
        }
        else if (stateName.equals("AWAKE") && !this.isDying)
        {
            this.isOut = true;
            this.state.setAnimation(0, "Skill_Begin", false);
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
}
