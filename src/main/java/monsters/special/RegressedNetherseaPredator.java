package monsters.special;

import Impairment.NervousImpairment;
import actions.ApplyImpairmentAction;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import monsters.AbstractMizukiMonster;
import powers.PhantomOfNetherseaPower;

public class RegressedNetherseaPredator extends AbstractMizukiMonster
{
    //退行的深溟猎食者
    public static final String ID = "Mizuki:RegressedNetherseaPredator";

    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Mizuki:RegressedNetherseaPredator");

    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    public RegressedNetherseaPredator(float x, float y)
    {
        super(NAME, "Mizuki:RegressedNetherseaPredator", 90, 0.0F, 0.0F, 150.0F, 320.0F, null, x, y);
        this.type = AbstractMonster.EnemyType.NORMAL;

        if (AbstractDungeon.ascensionLevel >= 19)
        {
            setHp(40);
        }
        else
        {
            setHp(30);
        }

        loadAnimation("resources/img/monster/NetherseaPredator/enemy_1231_dsrunr.atlas", "resources/img/monster/NetherseaPredator/enemy_1231_dsrunr.json", 1.8F);
        this.stateData.setMix("Idle", "Die", 0.1F);
        this.flipHorizontal = true;
        this.state.addAnimation(0, "Idle", true, 0.0F);
        this.nextMove = 0;
    }

    @Override
    public void usePreBattleAction()
    {
        //深溟幻形PhantomOfNethersea

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
