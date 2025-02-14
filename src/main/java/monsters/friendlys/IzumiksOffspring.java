package monsters.friendlys;

import Impairment.CorrosionImpairment;
import actions.ApplyImpairmentAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.MonsterStrings;

public class IzumiksOffspring extends AbstractFriendly
{
    public static final String ID = "Mizuki:IzumiksOffspring";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    private static final String NAME = monsterStrings.NAME;
    private static final String[] MOVES = monsterStrings.MOVES;
    private static final String[] DIALOG = monsterStrings.DIALOG;

    public IzumiksOffspring(float x, float y)
    {
        super(NAME, "Mizuki:IzumiksOffspring", 90, 0.0F, 0.0F, 150.0F, 320.0F, null, x, y);
        setHp(40, 42);
        loadAnimation("resources/img/monster/friendly/IzumiksOffspring/enemy_2041_syjely.atlas", "resources/img/monster/friendly/IzumiksOffspring/enemy_2041_syjely.json", 1.5F);
        this.flipHorizontal = false;

        changeState("START");
        this.nextMove = 0;
    }

    public void takeTurn()
    {
        AbstractPlayer p = AbstractDungeon.player;

        switch (this.nextMove)
        {
            case 0:
                addToBot(new ApplyImpairmentAction(new CorrosionImpairment(), p, 1));
                break;
        }
        getMove(0);
    }

    protected void getMove(int i)
    {
        setMove(MOVES[0], (byte)0, Intent.DEBUFF);
    }



    public void changeState(String stateName)
    {
        if (stateName.equals("START"))
        {
            this.state.setAnimation(0, "Start", false);
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

    public void addTip()
    {
        this.tips.add(new PowerTip(DIALOG[0], DIALOG[1]));
    }
}
