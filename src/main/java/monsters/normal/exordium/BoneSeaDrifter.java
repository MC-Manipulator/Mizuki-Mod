package monsters.normal.exordium;

import Impairment.CorrosionImpairment;
import actions.ApplyImpairmentAction;
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
import powers.AcidPower;

public class BoneSeaDrifter extends AbstractMizukiMonster
{
    //骨海漂流体

    public static final String ID = "Mizuki:BoneSeaDrifter";

    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Mizuki:BoneSeaDrifter");

    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    private int turns, acid, block;

    private boolean hasEnhanced;

    public BoneSeaDrifter(float x, float y)
    {

        super(NAME, "Mizuki:BoneSeaDrifter", 90, 0.0F, 0.0F, 150.0F, 320.0F, null, x, y);
        int dmg1, dmg2, acid, block;
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
            dmg1 = 8;
            dmg2 = 12;
            acid = 2;
            block = 8;
        }
        else if (AbstractDungeon.ascensionLevel >= 2)
        {
            dmg1 = 8;
            dmg2 = 12;
            acid = 1;
            block = 6;
        }
        else
        {
            dmg1 = 6;
            dmg2 = 10;
            acid = 1;
            block = 6;
        }


        this.block = block;
        this.acid = acid;
        this.damage.add(new DamageInfo((AbstractCreature) this, MathUtils.floor(dmg1)));
        this.damage.add(new DamageInfo((AbstractCreature) this, MathUtils.floor(dmg2)));

        loadAnimation("resources/img/monster/BoneSeaDrifter/enemy_2021_syfish.atlas", "resources/img/monster/BoneSeaDrifter/enemy_2021_syfish.json", 1.8F);
        this.stateData.setMix("Idle", "Die", 0.1F);
        this.flipHorizontal = true;
        this.state.addAnimation(0, "Idle", true, 0.0F);
        this.nextMove = 0;
        this.turns = 0;
    }

    public void takeTurn()
    {
        AbstractPlayer p = AbstractDungeon.player;

        switch (this.nextMove)
        {
            case 0:
                hasEnhanced = true;
                AbstractDungeon.actionManager.addToBottom(
                        (AbstractGameAction)new ApplyPowerAction((AbstractCreature)this, (AbstractCreature)this,
                                (AbstractPower)new StrengthPower((AbstractCreature)this, 2), 2));
                AbstractDungeon.actionManager.addToBottom(
                        (AbstractGameAction)new ApplyPowerAction((AbstractCreature)this, (AbstractCreature)this,
                                (AbstractPower)new AcidPower((AbstractCreature)this, acid), acid));
                break;
            case 1:
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "ATTACK"));
                addToBot((AbstractGameAction) new DamageAction((AbstractCreature)p, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                addToBot((AbstractGameAction) new GainBlockAction(this, block));
                break;
            case 2:
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "ATTACK"));
                addToBot((AbstractGameAction) new DamageAction((AbstractCreature)p, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                addToBot(new ApplyImpairmentAction(new CorrosionImpairment(), p, 1));
                break;
        }
        getMove(0);
    }

    protected void getMove(int i)
    {
        if (AbstractDungeon.ascensionLevel >= 17)
        {
            if (turns == 0)
            {
                setMove(MOVES[0], (byte)0, Intent.BUFF);
                turns++;
                return;
            }
            if (hasEnhanced)
            {
                setMove(MOVES[2], (byte)2, AbstractMonster.Intent.ATTACK_DEBUFF, ((DamageInfo)this.damage.get(1)).base);
                hasEnhanced = false;
                return;
            }
            int roll = AbstractDungeon.aiRng.random(99);
            if (roll < 25)
            {
                setMove(MOVES[0], (byte)0, Intent.BUFF);
            }
            else if (roll < 60)
            {
                setMove(MOVES[1], (byte)1, Intent.ATTACK_DEFEND, ((DamageInfo)this.damage.get(0)).base);
            }
            else
            {
                setMove(MOVES[2], (byte)2, AbstractMonster.Intent.ATTACK_DEBUFF, ((DamageInfo)this.damage.get(1)).base);
            }
            return;
        }


        int roll = AbstractDungeon.aiRng.random(99);
        if (hasEnhanced)
        {
            if (roll < 46)
            {
                setMove(MOVES[1], (byte)1, Intent.ATTACK_DEFEND, ((DamageInfo)this.damage.get(0)).base);
            }
            else
            {
                setMove(MOVES[2], (byte)2, AbstractMonster.Intent.ATTACK_DEBUFF, ((DamageInfo)this.damage.get(1)).base);
            }
            hasEnhanced = false;
        }
        else
        {
            if (roll < 25)
            {
                setMove(MOVES[0], (byte)0, Intent.BUFF);
            }
            else if (roll < 60)
            {
                setMove(MOVES[1], (byte)1, Intent.ATTACK_DEFEND, ((DamageInfo)this.damage.get(0)).base);
            }
            else
            {
                setMove(MOVES[2], (byte)2, AbstractMonster.Intent.ATTACK_DEBUFF, ((DamageInfo)this.damage.get(1)).base);
            }
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
