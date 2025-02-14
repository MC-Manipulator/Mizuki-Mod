package monsters.special;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import monsters.AbstractMizukiMonster;
import powers.ArtsEnhancingPower;

public class SarkazBladeweaver extends AbstractMizukiMonster
{
    //萨卡兹魔剑士
    //源石技艺强化：下一次攻击造成生命流失
    //法术劈砍：10点伤害
    //法术劈砍=>法术劈砍=>源石技艺强化=>法术劈砍=>

    public static final String ID = "Mizuki:SarkazBladeweaver";

    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Mizuki:SarkazBladeweaver");

    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    public int turn = 0;

    public boolean alarm = false;

    public SarkazBladeweaver(float x, float y)
    {

        super(NAME, "Mizuki:SarkazBladeweaver", 90, 0.0F, 0.0F, 250.0F, 320.0F, null, x, y);
        this.type = AbstractMonster.EnemyType.NORMAL;

        int dmg1;

        if (AbstractDungeon.ascensionLevel >= 7)
        {
            setHp(90, 94);
        }
        else
        {
            setHp(80, 85);
        }

        if (AbstractDungeon.ascensionLevel >= 17)
        {
            dmg1 = 12;
        }
        else if (AbstractDungeon.ascensionLevel >= 2)
        {
            dmg1 = 12;
        }
        else
        {
            dmg1 = 10;
        }

        this.damage.add(new DamageInfo((AbstractCreature) this, MathUtils.floor(dmg1)));

        loadAnimation("resources/img/monster/SarkazBladeweaver/enemy_1075_dmgswd.atlas", "resources/img/monster/SarkazBladeweaver/enemy_1075_dmgswd.json", 1.2F);
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
                AbstractDungeon.actionManager.addToBottom(
                        (AbstractGameAction)new ApplyPowerAction((AbstractCreature)this, (AbstractCreature)this,
                                (AbstractPower)new ArtsEnhancingPower((AbstractCreature)this, 1)));
                break;
            case 1:
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "ATTACK"));
                addToBot((AbstractGameAction) new DamageAction((AbstractCreature)p, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                break;
            case 2:
                DamageInfo dinfo = new DamageInfo(this, this.getIntentDmg(), DamageInfo.DamageType.HP_LOSS);

                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "ATTACK"));
                addToBot((AbstractGameAction) new DamageAction((AbstractCreature)p,
                        dinfo,
                        AbstractGameAction.AttackEffect.SLASH_HEAVY));
                break;
        }
        turn++;
        getMove(0);
    }

    protected void getMove(int i)
    {
        if (turn >= 4)
        {
            turn = 0;
        }
        //法术劈砍=>法术劈砍=>源石技艺强化=>法术劈砍
        switch (turn)
        {
            case 2:
                //0号技能：源石技艺强化
                setMove(MOVES[0], (byte)0, Intent.BUFF);
                break;
            case 0:
            case 1:
                //1号技能：法术劈砍
                setMove(MOVES[1], (byte)1, Intent.ATTACK, ((DamageInfo)this.damage.get(0)).base);
                break;
            case 3:
                //1号技能：法术劈砍
                setMove(MOVES[1], (byte)2, Intent.ATTACK, ((DamageInfo)this.damage.get(0)).base);
                break;
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
