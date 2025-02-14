package monsters.friendlys;

import Impairment.NervousImpairment;
import actions.ApplyImpairmentAction;
import basemod.ReflectionHacks;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.RegenPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import powers.CoatingDevicePower;
import powers.LanternUndyingPower;

public class ErosionproofCoatingDevice extends AbstractFriendly
{
    //镀层装置
    public static final String ID = "Mizuki:ErosionproofCoatingDevice";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    private static final String NAME = monsterStrings.NAME;
    private static final String[] MOVES = monsterStrings.MOVES;
    private static final String[] DIALOG = monsterStrings.DIALOG;
    public int activateTimes = 0;
    private int turn = 0;
    public ErosionproofCoatingDevice(float x, float y)
    {
        super(NAME, "Mizuki:Lumen", 90, 0.0F, 0.0F, 150.0F, 320.0F, null, x, y);
        setHp(40, 42);
        loadAnimation("resources/img/monster/friendly/ErosionproofCoatingDevice/trap_046_oxygen.atlas", "resources/img/monster/friendly/ErosionproofCoatingDevice/trap_046_oxygen.json", 1.5F);
        this.flipHorizontal = false;

        changeState("START");
        this.nextMove = 0;
        this.activateTimes = 0;
        this.turn = 0;
        setMove(MOVES[0], (byte)0, Intent.SLEEP);
    }

    @Override
    public void update()
    {
        super.update();
    }

    public void takeTurn()
    {
        AbstractPlayer p = AbstractDungeon.player;

        switch (this.nextMove)
        {
            case 0:
                //待机
                setMove(MOVES[0], (byte)0, Intent.SLEEP);
                break;
            case 1:
                //镀层
                addToBot(new ApplyImpairmentAction(new NervousImpairment(), p, -2));
                addToBot(new GainBlockAction(p, 15));
                turn++;
                if (turn >= 3)
                {
                    turn = 0;
                    if (activateTimes >= 2)
                    {
                        setMove(MOVES[3], (byte)3, Intent.UNKNOWN);
                        break;
                    }
                    else
                    {
                        setMove(MOVES[2], (byte)2, Intent.UNKNOWN);
                    }
                }
                else
                {
                    setMove(MOVES[1], (byte)1, Intent.BUFF);
                }
                break;
            case 2:
                //关闭
                changeState("END");
                ((CoatingDevicePower)this.getPower(CoatingDevicePower.id)).reset();
                ((CoatingDevicePower)this.getPower(CoatingDevicePower.id)).flashWithoutSound();
                setMove(MOVES[0], (byte)0, Intent.SLEEP);
                break;
            case 3:
                //损坏
                if (!this.isDeadOrEscaped())
                {
                    addToBot((AbstractGameAction)new VFXAction((AbstractGameEffect)new ExplosionSmallEffect(this.hb.cX, this.hb.cY), 0.1F));
                    addToBot((AbstractGameAction)new SuicideAction(this));
                    setMove(MOVES[3], (byte)3, Intent.UNKNOWN);
                    break;
                }
        }
        getMove(0);
    }
    @SpireOverride
    protected void updateIntentTip()
    {
        PowerTip tip = new PowerTip();
        switch (this.nextMove)
        {
            case 0:
                tip.header = MOVES[0];
                tip.body = DIALOG[2];
                tip.img = ImageMaster.INTENT_SLEEP;
                break;
            case 1:
                tip.header = MOVES[1];
                tip.body = DIALOG[3];
                tip.img = ImageMaster.INTENT_BUFF;
                break;
            case 2:
                tip.header = MOVES[2];
                tip.body = DIALOG[4];
                tip.img = ImageMaster.INTENT_UNKNOWN;
                break;
            case 3:
                tip.header = MOVES[3];
                tip.body = DIALOG[5];
                tip.img = ImageMaster.INTENT_UNKNOWN;
                break;
        }
        ReflectionHacks.setPrivate(this, AbstractMonster.class, "intentTip", tip);
    }

    protected void getMove(int i)
    {
        AbstractPlayer p = AbstractDungeon.player;
    }



    public void changeState(String stateName)
    {
        if (stateName.equals("START"))
        {
            this.state.setAnimation(0, "C_Start", false);
            this.state.addAnimation(0, "C_Ready", true, 0.0F);
        }
        if (stateName.equals("ACTIVATE"))
        {
            this.state.setAnimation(0, "C_Idle", true);
        }
        if (stateName.equals("END"))
        {
            this.state.setAnimation(0, "C_Ready", true);
        }
    }

    public void die()
    {
        this.state.setAnimation(0, "C_Die", false);
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
