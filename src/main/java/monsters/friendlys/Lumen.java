package monsters.friendlys;

import Impairment.CorrosionImpairment;
import actions.ApplyImpairmentAction;
import basemod.ReflectionHacks;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import powers.LanternUndyingPower;

public class Lumen extends AbstractFriendly
{
    public static final String ID = "Mizuki:Lumen";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    private static final String NAME = monsterStrings.NAME;
    private static final String[] MOVES = monsterStrings.MOVES;
    private static final String[] DIALOG = monsterStrings.DIALOG;
    public boolean lanternUndying = false;

    public Lumen(float x, float y)
    {
        super(NAME, "Mizuki:Lumen", 90, 0.0F, 0.0F, 150.0F, 320.0F, null, x, y);
        setHp(40, 42);
        loadAnimation("resources/img/monster/friendly/Lumen/char_4042_lumen.atlas", "resources/img/monster/friendly/Lumen/char_4042_lumen.json", 1.5F);
        this.flipHorizontal = false;

        changeState("START");
        this.nextMove = 0;
        lanternUndying = false;
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
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "ATTACK1"));
                addToBot(new AbstractGameAction()
                {
                    @Override
                    public void update()
                    {
                        CardCrawlGame.sound.play("LUMEN_ATTACK", 0.05F);
                        isDone = true;
                    }
                });
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, 15));
                break;
            case 1:
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "ATTACK2"));
                addToBot(new AbstractGameAction()
                {
                    @Override
                    public void update()
                    {
                        CardCrawlGame.sound.play("LUMEN_SKILL1", 0.05F);
                        isDone = true;
                    }
                });
                AbstractDungeon.actionManager.addToBottom(
                        (AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)this,
                                (AbstractPower)new RegenPower((AbstractCreature)p, 5), 5));
                break;
            case 2:
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "ATTACK3"));
                addToBot(new AbstractGameAction()
                {
                    @Override
                    public void update()
                    {
                        CardCrawlGame.sound.play("LUMEN_SKILL2", 0.05F);
                        isDone = true;
                    }
                });
                AbstractDungeon.actionManager.addToBottom(
                        (AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)this,
                                (AbstractPower)new ArtifactPower((AbstractCreature)p, 3), 3));
                break;
            case 3:
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "SKILLBEGIN"));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new LanternUndyingPower(this, 3)));
                lanternUndying = true;
                break;
            case 4:
                addToBot(new AbstractGameAction()
                {
                    @Override
                    public void update()
                    {
                        CardCrawlGame.sound.play("LUMEN_ATTACK", 0.05F);
                        isDone = true;
                    }
                });
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "SKILLATTACK"));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, 20));
                //AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, this, new StrengthPower(p, 2)));
                boolean hasDebuff = false;
                for (AbstractPower pr : p.powers)
                {
                    if (pr.type == AbstractPower.PowerType.DEBUFF)
                    {
                        hasDebuff = true;
                        addToBot(new RemoveSpecificPowerAction(p, this, pr.ID));
                    }
                }
                if (hasDebuff)
                {
                    addToBot(new AbstractGameAction()
                    {
                        @Override
                        public void update()
                        {
                            CardCrawlGame.sound.play("LUMEN_SKILL3_HIT", 0.05F);
                            isDone = true;
                        }
                    });
                    addToBot(new ReducePowerAction(this, this, LanternUndyingPower.id, 1));
                    if (this.getPower(LanternUndyingPower.id).amount <= 1)
                    {
                        lanternUndying = false;
                    }
                }
                break;
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
                tip.img = ImageMaster.INTENT_DEFEND;
                break;
            case 1:
                tip.header = MOVES[1];
                tip.body = DIALOG[3];
                tip.img = ImageMaster.INTENT_BUFF;
                break;
            case 2:
                tip.header = MOVES[2];
                tip.body = DIALOG[4];
                tip.img = ImageMaster.INTENT_BUFF;
                break;
            case 3:
                tip.header = MOVES[3];
                tip.body = DIALOG[5];
                tip.img = ImageMaster.INTENT_BUFF;
                break;
            case 4:
                tip.header = MOVES[4];
                tip.body = DIALOG[6];
                tip.img = ImageMaster.INTENT_DEFEND_BUFF;
                break;
        }
        ReflectionHacks.setPrivate(this, AbstractMonster.class, "intentTip", tip);
    }

    protected void getMove(int i)
    {

        if (lanternUndying)
        {
            setMove(MOVES[4], (byte)4, Intent.DEFEND_BUFF);
            return;
        }

        AbstractPlayer p = AbstractDungeon.player;
        boolean hasDebuff = false;
        for (AbstractPower pr : p.powers)
        {
            if (pr.type == AbstractPower.PowerType.DEBUFF)
            {
                hasDebuff = true;
                addToBot(new RemoveSpecificPowerAction(p, this, pr.ID));
            }
        }
        if (hasDebuff)
        {
            int roll = AbstractDungeon.aiRng.random(99);
            if (roll < 30)
            {
                setMove(MOVES[0], (byte)0, Intent.DEFEND);
            }
            else if (roll < 40)
            {
                setMove(MOVES[1], (byte)1, Intent.BUFF);
            }
            else if (roll < 80)
            {
                setMove(MOVES[2], (byte)2, Intent.BUFF);
            }
            else
            {
                setMove(MOVES[3], (byte)3, Intent.BUFF);
            }
            return;
        }

        if (p.currentHealth <= p.maxHealth)
        {
            int roll = AbstractDungeon.aiRng.random(99);
            if (roll < 40)
            {
                setMove(MOVES[0], (byte)0, Intent.DEFEND);
            }
            else if (roll < 60)
            {
                setMove(MOVES[1], (byte)1, Intent.BUFF);
            }
            else if (roll < 80)
            {
                setMove(MOVES[2], (byte)2, Intent.BUFF);
            }
            else
            {
                setMove(MOVES[3], (byte)3, Intent.BUFF);
            }
            return;
        }

        int roll = AbstractDungeon.aiRng.random(99);
        if (roll < 30)
        {
            setMove(MOVES[0], (byte)0, Intent.DEFEND);
        }
        else if (roll < 50)
        {
            setMove(MOVES[1], (byte)1, Intent.BUFF);
        }
        else if (roll < 80)
        {
            setMove(MOVES[2], (byte)2, Intent.BUFF);
        }
        else
        {
            setMove(MOVES[3], (byte)3, Intent.BUFF);
        }
    }



    public void changeState(String stateName)
    {
        if (stateName.equals("START"))
        {
            this.state.setAnimation(0, "Start", false);
            this.state.addAnimation(0, "Idle", true, 0.0F);
        }
        if (stateName.equals("ATTACK1"))
        {
            this.state.setAnimation(0, "Attack", false);
            this.state.addAnimation(0, "Idle", true, 0.0F);
        }
        if (stateName.equals("ATTACK2"))
        {
            this.state.setAnimation(0, "Skill_1", false);
            this.state.addAnimation(0, "Idle", true, 0.0F);
        }
        if (stateName.equals("ATTACK3"))
        {
            this.state.setAnimation(0, "Skill_2", false);
            this.state.addAnimation(0, "Idle", true, 0.0F);
        }
        if (stateName.equals("SKILLBEGIN"))
        {
            this.state.setAnimation(0, "Skill_3_Begin", false);
            this.state.addAnimation(0, "Skill_3_Idle", true, 0.0F);
        }
        if (stateName.equals("SKILLATTACK"))
        {
            this.state.setAnimation(0, "Skill_3_Loop", false);
            this.state.addAnimation(0, "Skill_3_Idle", true, 0.0F);
        }
        if (stateName.equals("SKILLEND"))
        {
            this.state.setAnimation(0, "Skill_3_End", false);
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
