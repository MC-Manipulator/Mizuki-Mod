package monsters;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ExampleMonster
{
    //例
    public static final String ID = "Mizuki:ExampleMonster";

    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Mizuki:ExampleMonster");

    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    public ExampleMonster(float x, float y)
    {
        /*
        super(NAME, "Mizuki:ExampleMonster", 90, 0.0F, 0.0F, 150.0F, 320.0F, null, x, y);
        int dmg1;
        this.type = AbstractMonster.EnemyType.ELITE;
        */

        /*
        if (AbstractDungeon.ascensionLevel >= 8)
        {
            setHp(78, 82);
        }
        else
        {
            setHp(75, 79);
        }*/

        /*
        if (AbstractDungeon.ascensionLevel >= 18)
        {
            dmg1 = 7;
        }
        else if (AbstractDungeon.ascensionLevel >= 3)
        {
            dmg1 = 7;
        }
        else
        {
            dmg1 = 6;
        }
        */

        /*
        this.damage.add(new DamageInfo((AbstractCreature) this, MathUtils.floor(dmg1)));


        loadAnimation("resources/img/monster//.atlas", "resources/img/monster//.json", 1.5F);
        this.flipHorizontal = true;
        this.state.addAnimation(0, "Idle", true, 0.0F);
        this.nextMove = 0;
        this.stateData.setMix("Idle", "Die", 0.1F);*/
    }

    public void takeTurn()
    {
        AbstractPlayer p = AbstractDungeon.player;
        /*
        switch (this.nextMove)
        {
            case 0:
                break;
            case 1:
                break;
        }*/
        getMove(0);
    }

    protected void getMove(int i)
    {
    }

    public void die()
    {
        /*
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
        */
    }

    public void trueDie()
    {
        //super.die();
    }
}
