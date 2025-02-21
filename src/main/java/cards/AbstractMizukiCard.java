package cards;


import basemod.BaseMod;
import basemod.abstracts.CustomSavable;
import basemod.interfaces.OnStartBattleSubscriber;
import com.badlogic.gdx.graphics.Color;
import basemod.abstracts.CustomCard;
import characters.Mizuki;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.EmpowerEffect;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;
import helper.EventHelper;
import modcore.MizukiModCore;
import patches.SaveContent;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.UUID;


public abstract class AbstractMizukiCard extends CustomCard implements EventHelper.OnLearnSubscriber, CustomSavable<ArrayList<Integer>>
{
    public int baseMagicNumber2 = 0;
    public int magicNumber2 = 0;
    public boolean upgradedMagicNumber2 = false;
    public int baseMagicNumber3 = 0;
    public int magicNumber3 = 0;
    public boolean upgradedMagicNumber3 = false;
    public int baseMagicNumber4 = 0;
    public int magicNumber4 = 0;
    public boolean upgradedMagicNumber4 = false;
    public ArrayList<Class<?>> storePower = new ArrayList<>();
    public ArrayList<UUID> storeCardUUID = new ArrayList<>();
    public ArrayList<AbstractCard> storeCard = new ArrayList<>();
    public boolean showCredits = false;
    public String creditDes = "";
    public boolean hasLearnLimit = false;
    @Override
    public ArrayList<Integer> onSave()
    {
        ArrayList<Integer> save = new ArrayList<>();
        save.add(this.baseMagicNumber);
        save.add(this.magicNumber);
        save.add(this.baseMagicNumber2);
        save.add(this.magicNumber2);
        save.add(this.baseMagicNumber3);
        save.add(this.magicNumber3);
        save.add(this.baseMagicNumber4);
        save.add(this.magicNumber4);
        return save;
    }

    @Override
    public void onLoad(ArrayList<Integer> load)
    {
        this.baseMagicNumber = load.get(0);
        this.magicNumber = load.get(1);
        this.baseMagicNumber2 = load.get(2);
        this.magicNumber2 = load.get(3);
        this.baseMagicNumber3 = load.get(4);
        this.magicNumber3 = load.get(5);
        this.baseMagicNumber4 = load.get(6);
        this.magicNumber4 = load.get(7);
    }

    @Override
    public Type savedType()
    {
        return new TypeToken<ArrayList<Integer>>(){}.getType();
    }


    public AbstractMizukiCard(String ID, boolean useTmpArt, CardStrings strings, int COST, CardType TYPE, CardRarity RARITY, CardTarget TARGET)
    {
        super(ID, strings.NAME, useTmpArt ? GetTmpImgPath(TYPE) : GetImgPath(TYPE, ID), COST, strings.DESCRIPTION, TYPE, Mizuki.Enums.MIZUKI_CARD, RARITY, TARGET);
        EventHelper.Subscribe((EventHelper.CustomSubscriber)this);
        //BaseMod.subscribe(this);
    }
    public AbstractMizukiCard(String ID, boolean useTmpArt, CardStrings strings, int COST, CardType TYPE, CardColor color, CardRarity RARITY, CardTarget TARGET)
    {
        super(ID, strings.NAME, useTmpArt ? GetTmpImgPath(TYPE) : GetImgPath(TYPE, ID), COST, strings.DESCRIPTION, TYPE, color, RARITY, TARGET);
        EventHelper.Subscribe((EventHelper.CustomSubscriber)this);
        //BaseMod.subscribe(this);
    }

    private static String GetTmpImgPath(CardType t)
    {
        String type;
        switch (t)
        {
            case ATTACK:
                type = "attack";
                return String.format("resources/img/card/test_%s.png", new Object[]{type});
            case POWER:
                type = "power";
                return String.format("resources/img/card/test_%s.png", new Object[]{type});
            case STATUS:
                type = "status";
                return String.format("resources/img/card/test_%s.png", new Object[]{type});
            case CURSE:
                type = "curse";
                return String.format("resources/img/card/test_%s.png", new Object[]{type});
            case SKILL:
                type = "skill";
                return String.format("resources/img/card/test_%s.png", new Object[]{type});

        }
        throw new IllegalStateException("Unexpected value: " + t);
    }
    private static String GetImgPath(CardType t, String name)
    {
        String type;
        switch (t)
        {
            case ATTACK:
                type = "attack";
                return String.format("resources/img/card/%s/%s.png", new Object[] { type, name.replace("Mizuki_", "") });
            case POWER:
                type = "power";
                return String.format("resources/img/card/%s/%s.png", new Object[] { type, name.replace("Mizuki_", "") });
            case STATUS:
                type = "status";
                return String.format("resources/img/card/%s/%s.png", new Object[] { type, name.replace("Mizuki_", "") });
            case CURSE:
                type = "curse";
                return String.format("resources/img/card/%s/%s.png", new Object[] { type, name.replace("Mizuki_", "") });
            case SKILL:
                type = "skill";
                return String.format("resources/img/card/%s/%s.png", new Object[] { type, name.replace("Mizuki_", "") });
        }
        throw new IllegalStateException("Unexpected value: " + t);
    }
    protected void setupDamage(int amt)
    {
        this.baseDamage = amt;
        this.damage = amt;
    }
    protected void setupBlock(int amt)
    {
        this.baseBlock = amt;
        this.block = amt;
    }
    protected void setupMagicNumber(int M, int M2, int M3, int M4)
    {
        //MizukiModCore.logger.info("setupM");
        this.baseMagicNumber = M;
        this.magicNumber = this.baseMagicNumber;
        this.baseMagicNumber2 = M2;
        this.magicNumber2 = this.baseMagicNumber2;
        this.baseMagicNumber3 = M3;
        this.magicNumber3 = this.baseMagicNumber3;
        this.baseMagicNumber4 = M4;
        this.magicNumber4 = this.baseMagicNumber4;
    }

    protected void setupCredits(String des)
    {
        this.showCredits = true;
        this.creditDes = des;
    }
    protected void upgradeDescription(CardStrings cardStrings)
    {
        this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        initializeDescription();
    }
    public void damageToEnemy(AbstractMonster m, AbstractGameAction.AttackEffect effect)
    {
        addToBot((AbstractGameAction)new DamageAction((AbstractCreature)m, new DamageInfo((AbstractCreature)AbstractDungeon.player, this.damage), effect));
    }
    public void damageToAllEnemies(AbstractGameAction.AttackEffect effect)
    {
        addToBot((AbstractGameAction)new DamageAllEnemiesAction((AbstractCreature)AbstractDungeon.player, this.multiDamage, this.damageTypeForTurn, effect));
    }
    public void gainBlock()
    {
        addToBot((AbstractGameAction)new GainBlockAction((AbstractCreature)AbstractDungeon.player, this.block));
    }
    public void gainBlock(int amt)
    {
        addToBot((AbstractGameAction)new GainBlockAction((AbstractCreature)AbstractDungeon.player, amt));
    }
    public void drawCards(int amt)
    {
        addToBot((AbstractGameAction)new DrawCardAction(amt));
    }
    public void limitedUpgrade()
    {

    }
    protected void upgradeMagicNumber2(int amount)
    {
        this.baseMagicNumber2 += amount;
        this.magicNumber2 = this.baseMagicNumber2;
        this.upgradedMagicNumber2 = true;
    }
    protected void upgradeMagicNumber3(int amount)
    {
        this.baseMagicNumber3 += amount;
        this.magicNumber3 = this.baseMagicNumber3;
        this.upgradedMagicNumber3 = true;
    }
    protected void upgradeMagicNumber4(int amount)
    {
        this.baseMagicNumber4 += amount;
        this.magicNumber4 = this.baseMagicNumber4;
        this.upgradedMagicNumber4 = true;
    }
    @Override
    public void OnLearn(UUID uuid)
    {

    }

    @Override
    public AbstractCard makeStatEquivalentCopy()
    {
        AbstractMizukiCard c = (AbstractMizukiCard) super.makeStatEquivalentCopy();
        c.magicNumber2 = this.magicNumber2;
        c.baseMagicNumber2 = this.baseMagicNumber2;

        c.magicNumber3 = this.magicNumber3;
        c.baseMagicNumber3 = this.baseMagicNumber3;

        c.magicNumber4 = this.magicNumber4;
        c.baseMagicNumber4 = this.baseMagicNumber4;

        c.storePower = this.storePower;
        c.storeCardUUID = this.storeCardUUID;
        c.storeCard = this.storeCard;
        return c;
    }
}
