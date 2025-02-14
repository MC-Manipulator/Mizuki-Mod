package monsters.special;

import Impairment.NervousImpairment;
import actions.ApplyImpairmentAction;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import helper.RejectionHelper;
import monsters.AbstractMizukiMonster;

public class Chest extends AbstractMizukiMonster
{
    //宝箱

    public static final String ID = "Mizuki:Chest";

    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Mizuki:Chest");

    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    public boolean alert = false;

    public Chest(float x, float y)
    {

        super(NAME, "Mizuki:Chest", 90, 0.0F, 0.0F, 150.0F, 320.0F, null, x, y);
        this.type = AbstractMonster.EnemyType.NORMAL;


        //生命值应当较高
        if (AbstractDungeon.ascensionLevel >= 7)
        {
            setHp(76, 78);
        }
        else
        {
            setHp(70, 75);
        }

        if (AbstractDungeon.ascensionLevel >= 17)
        {

        }
        else if (AbstractDungeon.ascensionLevel >= 2)
        {

        }
        else
        {

        }

        loadAnimation("resources/img/monster/Chest/trap_065_normbox.atlas", "resources/img/monster/Chest/trap_065_normbox.json", 1.5F);
        this.flipHorizontal = true;
        this.stateData.setMix("Idle", "Die", 0.1F);
        this.state.addAnimation(0, "Idle", true, 0.0F);
        this.nextMove = 0;
    }

    public void takeTurn()
    {
        AbstractPlayer p = AbstractDungeon.player;

        switch (this.nextMove)
        {
            case 0:
                //只会挂机
                break;
        }
        getMove(0);
    }

    protected void getMove(int i)
    {
        //设置挂机意图
        setMove(MOVES[0], (byte)0, Intent.NONE);
    }

    public void changeState(String stateName)
    {

    }

    public void die()
    {
        if (!alert)
        {
            this.state.setAnimation(0, "Die", false);
            AbstractDungeon.getCurrRoom().addRelicToRewards(AbstractRelic.RelicTier.UNCOMMON);
            AbstractDungeon.getCurrRoom().addGoldToRewards(100);
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
