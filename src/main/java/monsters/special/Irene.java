package monsters.special;

import basemod.helpers.VfxBuilder;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import helper.ImpairementManager;
import modcore.MizukiModCore;
import monsters.AbstractMizukiMonster;
import powers.LevitatePower;
import powers.StunMonsterPower;

import java.util.ArrayList;

public class Irene extends AbstractMizukiMonster
{
    public static final String ID = "Mizuki:Irene";

    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Mizuki:Irene");

    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    public int energy = 0;

    private static final Texture irene_1 = ImageMaster.loadImage("resources/img/vfx/irene_skill/irene_1.png");
    private static final Texture irene_2 = ImageMaster.loadImage("resources/img/vfx/irene_skill/irene_2.png");
    private static final Texture irene_3 = ImageMaster.loadImage("resources/img/vfx/irene_skill/irene_3.png");
    private static final Texture irene_4 = ImageMaster.loadImage("resources/img/vfx/irene_skill/irene_4.png");
    private static final Texture irene_5 = ImageMaster.loadImage("resources/img/vfx/irene_skill/irene_5.png");
    private static final Texture irene_6 = ImageMaster.loadImage("resources/img/vfx/irene_skill/irene_6.png");
    private static final Texture irene_7 = ImageMaster.loadImage("resources/img/vfx/irene_skill/irene_7.png");
    private static final Texture irene_8 = ImageMaster.loadImage("resources/img/vfx/irene_skill/irene_8.png");
    private static final Texture irene_9 = ImageMaster.loadImage("resources/img/vfx/irene_skill/irene_9.png");
    private static ArrayList<Texture> irene = new ArrayList<>();
    public Irene(float x, float y)
    {
        super(NAME, "Mizuki:Irene", 90, 0.0F, 0.0F, 150.0F, 320.0F, null, x, y);
        int dmg1, dmg2, hp;
        this.type = AbstractMonster.EnemyType.ELITE;
        if (irene.isEmpty())
        {
            irene.add(irene_1);
            irene.add(irene_2);
            irene.add(irene_3);
            irene.add(irene_4);
            irene.add(irene_5);
            irene.add(irene_6);
            irene.add(irene_7);
            irene.add(irene_8);
            irene.add(irene_9);
        }

        if (AbstractDungeon.ascensionLevel >= 18)
        {
            dmg1 = 6;
            dmg2 = 15;
        }
        else if (AbstractDungeon.ascensionLevel >= 3)
        {
            dmg1 = 6;
            dmg2 = 15;
        }
        else
        {
            dmg1 = 4;
            dmg2 = 12;
        }
        this.damage.add(new DamageInfo((AbstractCreature) this, MathUtils.floor(dmg1)));
        this.damage.add(new DamageInfo((AbstractCreature)this, MathUtils.floor(dmg2)));
        if (AbstractDungeon.ascensionLevel >= 8)
        {
            hp = 140;
        }
        else
        {
            hp = 130;
        }
        setHp(MathUtils.floor(hp));
        loadAnimation("resources/img/monster/Irene/char_4009_irene.atlas", "resources/img/monster/Irene/char_4009_irene.json", 1.5F);
        this.flipHorizontal = true;
        this.state.setAnimation(0, "Start", false);
        this.state.addAnimation(0, "Idle", true, 0.0F);
        this.nextMove = 0;
        this.energy = 0;
        this.stateData.setMix("Idle", "Die", 0.1F);
    }

    public void takeTurn()
    {
        AbstractPlayer p = AbstractDungeon.player;
        if (this.nextMove == 1)
        {
            this.state.setAnimation(0, "Attack", false);
            this.state.addAnimation(0, "Idle", true, 0.0F);
            this.energy++;
            addToBot((AbstractGameAction) new DamageAction((AbstractCreature)p, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
            addToBot((AbstractGameAction) new DamageAction((AbstractCreature)p, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
            addToBot((AbstractGameAction)new TalkAction((AbstractCreature)this, DIALOG[0], 0.3F, 3.0F));
        }
        if (this.nextMove == 2)
        {
            this.state.setAnimation(0, "Attack", false);
            this.state.addAnimation(0, "Idle", true, 0.0F);
            this.energy++;
            addToBot((AbstractGameAction) new DamageAction((AbstractCreature)p, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
            addToBot((AbstractGameAction) new DamageAction((AbstractCreature)p, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
            addToBot((AbstractGameAction)new TalkAction(true, DIALOG[1], 2.0F, 2.0F));
        }
        if (this.nextMove == 3)
        {
            AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ApplyPowerAction((AbstractCreature)this, (AbstractCreature)this, (AbstractPower)new ArtifactPower((AbstractCreature)this, 2), 2));
            ImpairementManager.resetCreatureNervousImpairments(this);
            AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new RemoveDebuffsAction((AbstractCreature)this));
            this.energy++;
        }
        if (this.nextMove == 4)
        {
            AbstractDungeon.actionManager.addToBottom(
                    (AbstractGameAction)new ApplyPowerAction((AbstractCreature)this, (AbstractCreature)this,
                            (AbstractPower)new StrengthPower((AbstractCreature)this, 2), 2));
            this.energy += 2;
        }
        if (this.nextMove == 5)
        {
            this.state.setAnimation(0, "Skill_1", false);
            this.state.addAnimation(0, "Idle", true, 0.0F);
            addToBot((AbstractGameAction) new DamageAction((AbstractCreature)p, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HEAVY));
            addToBot((AbstractGameAction) new DamageAction((AbstractCreature)p, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
            addToBot(new AbstractGameAction()
            {
                @Override
                public void update()
                {
                    CardCrawlGame.sound.play("MIZUKI_Irene_Skill3_Hit", 0.05F);
                    isDone = true;
                }
            });
            AbstractDungeon.actionManager.addToBottom(
                    (AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)this,
                            (AbstractPower)new LevitatePower((AbstractCreature)p, 1), 1));
            this.energy += 1;
        }
        if (this.nextMove == 6)
        {
            this.state.setAnimation(0, "Skill_2", false);
            this.state.addAnimation(0, "Idle", true, 0.0F);
            addToBot((AbstractGameAction) new DamageAction((AbstractCreature)p, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_HEAVY));
            AbstractDungeon.actionManager.addToBottom(
                    (AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)this,
                            (AbstractPower)new LevitatePower((AbstractCreature)p, 1), 1));
            this.energy += 1;
        }
        if (this.nextMove == 7)
        {
            addToBot(new AbstractGameAction()
            {
                @Override
                public void update()
                {
                    state.setAnimation(0, "Skill_3_Start", false);
                    state.addAnimation(0, "Skill_3_Attack", false, 0.0F);
                    state.addAnimation(0, "Idle", true, 0.0F);
                    isDone = true;
                }
            });
            int ran = AbstractDungeon.aiRng.random(1, 4);
            if (ran == 1)
            {
                addToBot((AbstractGameAction)new TalkAction(this, DIALOG[2], 2.0F, 2.0F));
            }
            if (ran == 2)
            {
                addToBot((AbstractGameAction)new TalkAction(this, DIALOG[3], 2.0F, 2.0F));
            }
            if (ran == 3)
            {
                addToBot((AbstractGameAction)new TalkAction(this, DIALOG[4], 2.0F, 2.0F));
            }
            if (ran == 4)
            {
                addToBot((AbstractGameAction)new TalkAction(this, DIALOG[5], 2.0F, 2.0F));
            }
            addToBot(new AbstractGameAction()
            {
                @Override
                public void update()
                {
                    CardCrawlGame.sound.play("MIZUKI_Irene_Skill3_Attack", 0.05F);
                    isDone = true;
                }
            });
            for (int i = 0;i < 8;i++)
            {

                AbstractGameEffect irene_attack = new VfxBuilder(irene.get(MathUtils.random(0, 8)), this.hb.cX, this.hb.cY, 0.4f)
                        .fadeIn(0.2f)
                        .moveX(this.hb.cX, this.hb.cX + MathUtils.random(-20, 20) * Settings.scale)
                        .moveY(this.hb.cY, this.hb.cY + MathUtils.random(-20, 20) * Settings.scale)
                        .setScale(1.8f)
                        .build();

                addToBot(new AbstractGameAction()
                {
                    @Override
                    public void update()
                    {
                        AbstractDungeon.effectsQueue.add(irene_attack);
                        isDone = true;
                    }
                });
                addToBot((AbstractGameAction) new DamageAction((AbstractCreature)p, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                addToBot(new AbstractGameAction()
                {
                    @Override
                    public void update()
                    {
                        CardCrawlGame.sound.play("MIZUKI_Irene_Skill3_Hit", 0.05F);
                        isDone = true;
                    }
                });
            }
            addToBot(new AbstractGameAction()
            {
                @Override
                public void update()
                {
                    CardCrawlGame.sound.play("MIZUKI_Irene_Skill3_End", 0.05F);
                    isDone = true;
                }
            });
            this.energy = 0;
        }
        if (this.nextMove == 8)
        {
            this.state.setAnimation(0, "Attack", false);
            this.state.addAnimation(0, "Idle", true, 0.0F);
            this.energy++;
            addToBot((AbstractGameAction) new DamageAction((AbstractCreature)p, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
            addToBot((AbstractGameAction) new DamageAction((AbstractCreature)p, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        }
        getMove(0);
    }

    protected void getMove(int i)
    {
        if (this.nextMove < 2)
        {
            setMove(MOVES[5], (byte)++this.nextMove, AbstractMonster.Intent.ATTACK, ((DamageInfo)this.damage.get(0)).base, 2, true);
            return;
        }
        if (this.energy == 0)
        {
            setMove(MOVES[5], (byte)8, AbstractMonster.Intent.ATTACK, ((DamageInfo)this.damage.get(0)).base, 2, true);
            return;
        }

        if (this.nextMove >= 2)
        {
            if (this.energy < 3)
            {
                if (ImpairementManager.getCreatureImpairments(this) != null)
                {
                    if (ImpairementManager.getCreatureNervousImpairments(this) != null)
                    {
                        if (ImpairementManager.getCreatureNervousImpairments(this).currentcount > 2)
                        {
                            setMove(MOVES[0], (byte)3, Intent.BUFF);
                            return;
                        }
                    }
                }
                else if (this.currentHealth <= this.maxHealth / 2.0F)
                {
                    setMove(MOVES[0], (byte)3, Intent.BUFF);
                    return;
                }
                setMove(MOVES[1], (byte)4, Intent.BUFF);
                return;
            }
            if (this.energy < 7)
            {
                int ran = AbstractDungeon.aiRng.random(0, 2);
                if (ran == 0)
                {
                    setMove(MOVES[2], (byte)5, AbstractMonster.Intent.ATTACK_DEBUFF, ((DamageInfo)this.damage.get(1)).base);
                }
                else if (ran == 1)
                {
                    setMove(MOVES[3], (byte)6, AbstractMonster.Intent.ATTACK_DEBUFF, ((DamageInfo)this.damage.get(1)).base);
                }
                else
                {
                    setMove(MOVES[5], (byte)8, AbstractMonster.Intent.ATTACK, ((DamageInfo)this.damage.get(0)).base, 2, true);
                }
                return;
            }
            if (this.energy > 7)
            {
                setMove(MOVES[4], (byte)7, Intent.ATTACK, ((DamageInfo)this.damage.get(0)).base, 8, true);
            }
        }
    }

    public void die()
    {
        this.state.setAnimation(0, "Die", false);
        //addToBot((AbstractGameAction)new TalkAction((AbstractCreature)this, DIALOG[7], 0.3F, 3.0F));
        //addToBot((AbstractGameAction)new WaitAction(3.0F));
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
