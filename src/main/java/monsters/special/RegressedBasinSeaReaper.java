package monsters.special;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
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

public class RegressedBasinSeaReaper extends AbstractMizukiMonster
{
    //退行的钵海收割者
    public static final String ID = "Mizuki:RegressedBasinSeaReaper";

    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Mizuki:RegressedBasinSeaReaper");

    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    public RegressedBasinSeaReaper(float x, float y)
    {
        super(NAME, "Mizuki:RegressedBasinSeaReaper", 90, 0.0F, 0.0F, 150.0F, 320.0F, null, x, y);
        this.type = EnemyType.NORMAL;

        if (AbstractDungeon.ascensionLevel >= 19)
        {
            setHp(30);
        }
        else
        {
            setHp(20);
        }

        loadAnimation("resources/img/monster/BasinSeaReaper/enemy_1151_dslntc.atlas", "resources/img/monster/BasinSeaReaper/enemy_1151_dslntc.json", 1.5F);
        this.flipHorizontal = true;
        this.state.addAnimation(0, "Idle", true, 0.0F);
        this.nextMove = 3;
        this.stateData.setMix("Idle", "Die", 0.1F);
    }

    public void takeTurn()
    {
        AbstractPlayer p = AbstractDungeon.player;
        getMove(0);
    }

    protected void getMove(int i)
    {
        setMove(MOVES[0], (byte)0, Intent.UNKNOWN);
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
