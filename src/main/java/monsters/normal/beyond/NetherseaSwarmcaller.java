package monsters.normal.beyond;

import Impairment.NervousImpairment;
import actions.ApplyImpairmentAction;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import monsters.AbstractMizukiMonster;
import powers.SwarmCallPower;

public class NetherseaSwarmcaller extends AbstractMizukiMonster
{
    //深溟巢涌者

    public static final String ID = "Mizuki:NetherseaSwarmcaller";

    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Mizuki:NetherseaSwarmcaller");

    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    private int block, dazed, nervous;

    public NetherseaSwarmcaller(float x, float y)
    {

        super(NAME, "Mizuki:NetherseaSwarmcaller", 90, 0.0F, 0.0F, 150.0F, 320.0F, null, x, y);
        int nervous, block, dazed;
        this.type = AbstractMonster.EnemyType.NORMAL;


        if (AbstractDungeon.ascensionLevel >= 7)
        {
            setHp(90, 96);
        }
        else
        {
            setHp(92, 102);
        }

        if (AbstractDungeon.ascensionLevel >= 17)
        {
            block = 20;
            nervous = 2;
            dazed = 3;
        }
        else if (AbstractDungeon.ascensionLevel >= 2)
        {
            block = 15;
            nervous = 1;
            dazed = 2;
        }
        else
        {
            block = 15;
            nervous = 1;
            dazed = 2;
        }

        this.dazed = dazed;
        this.block = block;
        this.nervous = nervous;

        loadAnimation("resources/img/monster/NetherseaSwarmcaller/enemy_1234_dsubrl.atlas", "resources/img/monster/NetherseaSwarmcaller/enemy_1234_dsubrl.json", 1.8F);
        this.stateData.setMix("Idle", "Die", 0.1F);
        this.flipHorizontal = true;
        this.state.addAnimation(0, "Idle", true, 0.0F);
        this.nextMove = 0;
    }

    public void usePreBattleAction()
    {
        AbstractDungeon.actionManager.addToBottom(
                (AbstractGameAction)new ApplyPowerAction((AbstractCreature)this, (AbstractCreature)this,
                        (AbstractPower)new ArtifactPower((AbstractCreature)this, 1)));
        AbstractDungeon.actionManager.addToBottom(
                (AbstractGameAction)new ApplyPowerAction((AbstractCreature)this, (AbstractCreature)this,
                        (AbstractPower)new SwarmCallPower((AbstractCreature)this, 1)));
    }

    public void takeTurn()
    {
        AbstractPlayer p = AbstractDungeon.player;

        switch (this.nextMove)
        {
            case 0:
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "ATTACK"));
                for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters)
                {
                    if (!m.isDeadOrEscaped() && m != this)
                    {
                        addToBot((AbstractGameAction) new GainBlockAction(m, this.block));
                        AbstractDungeon.actionManager.addToBottom(
                                (AbstractGameAction)new ApplyPowerAction((AbstractCreature)m, (AbstractCreature)this,
                                        (AbstractPower)new StrengthPower((AbstractCreature)m, 2), 2));
                    }
                }
                break;
            case 1:
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "ATTACK"));
                AbstractDungeon.actionManager.addToBottom(
                        (AbstractGameAction)new MakeTempCardInDrawPileAction((AbstractCard)new Dazed(), dazed, true, true));
                addToBot(new ApplyImpairmentAction(new NervousImpairment(), p, nervous));

                break;
        }
        getMove(0);
    }

    protected void getMove(int i)
    {
        int roll = AbstractDungeon.aiRng.random(99);
        if (roll < 50)
        {
            setMove(MOVES[0], (byte)0, Intent.BUFF);
        }
        else
        {
            setMove(MOVES[1], (byte)1, Intent.DEBUFF);
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
