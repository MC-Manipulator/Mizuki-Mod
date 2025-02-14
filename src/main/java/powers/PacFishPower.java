package powers;

import cards.Skills.AbsurdFate;
import characters.Mizuki;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.unique.AddCardToDeckAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;
import helper.EventHelper;
import modcore.MizukiModCore;

import java.util.ArrayList;

public class PacFishPower extends AbstractMizukiPower implements EventHelper.OnEnemyDeathSubscriber, EventHelper.OnEnemyAppearSubscriber
{
    public static final String id = MizukiModCore.MakePath(PacFishPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(id);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private ArrayList<MonsterHealthOnTurnStart> monstersHealthOnTurnStart = new ArrayList<>();
    private AbstractCard sourceCard;
    private boolean hasTrigger = false;

    public PacFishPower(AbstractCard source, AbstractCreature owner, int amt)
    {
        super(owner, amt, id, NAME);
        sourceCard = source;
        updateDescription();
        monstersHealthOnTurnStart.clear();
        for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters)
        {
            //当能力添加的时候也记录一遍
            MonsterHealthOnTurnStart health = new MonsterHealthOnTurnStart(m, m.currentHealth);
            this.monstersHealthOnTurnStart.add(health);
        }
        this.type = PowerType.BUFF;
    }

    @Override
    public void atStartOfTurnPostDraw()
    {
        monstersHealthOnTurnStart.clear();
        for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters)
        {
            MonsterHealthOnTurnStart health = new MonsterHealthOnTurnStart(m, m.currentHealth);
            this.monstersHealthOnTurnStart.add(health);
            /*
            //每回合开始时，将所有敌人的生命都记录下来
            for (MonsterHealthOnTurnStart m2 : monstersHealthOnTurnStart)
            {
                if (m2.monster == m)
                {
                    m2.setMonsterHealth(m.currentHealth);
                }
            }*/
        }
    }

    @Override
    public void OnEnemyAppear(AbstractMonster monster)
    {
        MonsterHealthOnTurnStart health = new MonsterHealthOnTurnStart(monster, monster.currentHealth);
        this.monstersHealthOnTurnStart.add(health);
    }

    @Override
    public void OnEnemyDeath(AbstractMonster monster)
    {
        //当敌人死的时候，传过来死亡的敌人，依照编号判断这个敌人回合开始时是否满血，满血的话就给予金币并结束效果
        //MizukiModCore.logger.info(monster.name);
        //MizukiModCore.logger.info(monster.currentHealth);
        for (MonsterHealthOnTurnStart m2 : monstersHealthOnTurnStart)
        {
            //MizukiModCore.logger.info("储存的：" + m2.monster.name);
            //MizukiModCore.logger.info("储存的：" + m2.healthOnTurnStart);
            if (m2.monster.equals(monster))
            {
                //MizukiModCore.logger.info("识别到");
                if (m2.healthOnTurnStart == 1 && !hasTrigger)
                {
                    hasTrigger = true;
                    for (int i = 0;i < this.amount;i++)
                    {
                        AbstractDungeon.player.gainGold(monster.maxHealth);
                        for (int j = 0;j < monster.maxHealth;j++)
                            AbstractDungeon.effectList.add(new GainPennyEffect(AbstractDungeon.player, monster.hb.cX, monster.hb.cY, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, true));
                    }
                    addToBot((AbstractGameAction)new ReducePowerAction(owner, owner, this.ID, this.amount));
                    AbstractDungeon.player.masterDeck.removeCard(sourceCard);
                }
            }
        }

        /*
        if ((AbstractDungeon.getCurrRoom()).monsters.monsters.get(num).maxHealth == enemiesHealthOnTurnStart.get(num))
        {
            //MizukiModCore.logger.info((AbstractDungeon.getCurrRoom()).monsters.monsters.get(num).maxHealth);
            AbstractDungeon.player.gainGold((AbstractDungeon.getCurrRoom()).monsters.monsters.get(num).maxHealth);
            for (int i = 0; i < (AbstractDungeon.getCurrRoom()).monsters.monsters.get(num).maxHealth; i++)
                AbstractDungeon.effectList.add(new GainPennyEffect(AbstractDungeon.player, monster.hb.cX, monster.hb.cY, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, true));
            //AbstractDungeon.getCurrRoom().addGoldToRewards((AbstractDungeon.getCurrRoom()).monsters.monsters.get(num).maxHealth * this.amount);
            //addToBot((AbstractGameAction)new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
            //        (AbstractPower)new PacFishPower(AbstractDungeon.player, -this.amount), -this.amount));
            addToBot((AbstractGameAction)new ReducePowerAction(owner, owner, this.ID, this.amount));
            AbstractDungeon.player.masterDeck.removeCard(sourceCard);
        }*/
    }

    public String getDescription()
    {
        return DESCRIPTIONS[0];
    }

    public class MonsterHealthOnTurnStart
    {
        public AbstractMonster monster;
        public float healthOnTurnStart;

        public MonsterHealthOnTurnStart(AbstractMonster monster, int healthOnTurnStart)
        {
            this.monster = monster;
            this.healthOnTurnStart = (float) healthOnTurnStart / monster.maxHealth;
        }

        public void setMonsterHealth(int healthOnTurnStart)
        {
            this.healthOnTurnStart = (float) healthOnTurnStart / monster.maxHealth;
        }
    }
}
