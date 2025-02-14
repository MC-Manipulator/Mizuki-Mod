package helper;

import cards.AbstractFoodCard;
import cards.AbstractMizukiCard;
import cards.Food.Basic.*;
import cards.Food.Common.*;
import cards.Food.Rare.*;
import cards.Food.Uncommon.*;
import cards.Ingredients.*;
import characters.Mizuki;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import modcore.MizukiModCore;
import ui.CookingOption;
import vfx.CookingEffect;

import java.util.ArrayList;
import java.util.Iterator;

public class CookingHelper
{
    //内部类：配方，用来装载配方的各种食材的数量以及食物
    static class Formula
    {
        public int crabLegs = 0;
        public int paddy = 0;
        public int slicedMeat = 0;
        public int fowlbeastWingsMeat = 0;
        public int legMeatWithBone = 0;
        public int cartilageChunks = 0;
        public int meatEssence = 0;
        public AbstractCard food = null;

        public Formula(int crabLegs, int paddy, int slicedMeat, int fowlbeastWingsMeat, int legMeatWithBone, int cartilageChunks, int meatEssence, AbstractCard food)
        {
            this.crabLegs = crabLegs;
            this.paddy = paddy;
            this.slicedMeat = slicedMeat;
            this.fowlbeastWingsMeat = fowlbeastWingsMeat;
            this.legMeatWithBone = legMeatWithBone;
            this.cartilageChunks = cartilageChunks;
            this.meatEssence = meatEssence;
            this.food = food;
        }

        public boolean ifMatch(Formula formula)
        {
            return crabLegs == formula.crabLegs && paddy == formula.paddy
                    && slicedMeat == formula.slicedMeat && fowlbeastWingsMeat == formula.fowlbeastWingsMeat && legMeatWithBone == formula.legMeatWithBone
                    && cartilageChunks == formula.cartilageChunks && meatEssence == formula.meatEssence;
        }

        public void reset()
        {
            this.crabLegs = 0;
            this.paddy = 0;
            this.slicedMeat = 0;
            this.fowlbeastWingsMeat = 0;
            this.legMeatWithBone = 0;
            this.meatEssence = 0;
            this.cartilageChunks = 0;
            this.food = null;
        }
    }

    public static boolean ableToCook = false;
    public static boolean confirmScreenForCooking = false;
    public static boolean gridScreenForCooking = false;
    public static boolean confirmToCook = false;
    public static CookingOption CookingCampfireOption;
    public static CookingEffect currCookingEffect = null;
    public static ArrayList<Formula> formulas = null;
    public static Formula currFormula = null;
    public static ArrayList<AbstractCard> display = null;

    public static CardGroup getCookableCards()
    {
        CardGroup retVal = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        Iterator<AbstractCard> var2 = AbstractDungeon.player.masterDeck.group.iterator();
        while (var2.hasNext())
        {
            AbstractCard c = var2.next();
            if (c instanceof AbstractMizukiCard)
            {
                AbstractMizukiCard cg = (AbstractMizukiCard)c;
                if (cg.tags.contains(Mizuki.Enums.INGREDIENT_CARD))
                {
                    retVal.group.add(c);
                }
            }
        }
        return retVal;
    }

    public static void loadFormulas()
    {
        if (formulas == null)
        {
            formulas = new ArrayList<>();
        }

        /*
        formulas.add(new Formula(3, 0, 0, 0, 0, new FreshSlicedCrab()));
        formulas.add(new Formula(0, 3, 0, 0, 0, new SeasonedSolidFood()));
        formulas.add(new Formula(0, 0, 3, 0, 0, new DriedLeanMeat()));
        formulas.add(new Formula(0, 0, 0, 3, 0, new SoupGel()));
        formulas.add(new Formula(0, 0, 0, 0, 3, new CarnivalDinner()));


        formulas.add(new Formula(2, 1, 0, 0, 0, new SteamedRiceWithCrabShell()));
        formulas.add(new Formula(2, 0, 1, 0, 0, new ReheatedThickSoup()));
        formulas.add(new Formula(2, 0, 0, 1, 0, new LowTempCookedMeat()));
        formulas.add(new Formula(2, 0, 0, 0, 1, new KnightSupplement()));

        formulas.add(new Formula(1, 2, 0, 0, 0, new CrabMeatMixedRace()));
        formulas.add(new Formula(0, 2, 1, 0, 0, new ExThickPie()));
        formulas.add(new Formula(0, 2, 0, 1, 0, new RoastBeastPancake()));
        formulas.add(new Formula(0, 2, 0, 0, 1, new MealSubstitutePowder()));

        formulas.add(new Formula(1, 0, 2, 0, 0, new FlavoredJerky()));
        formulas.add(new Formula(0, 1, 2, 0, 0, new PineBranchSmokedPork()));
        formulas.add(new Formula(0, 0, 2, 1, 0, new FreshCondensedCube()));
        formulas.add(new Formula(0, 0, 2, 0, 1, new RoyalCube()));

        formulas.add(new Formula(1, 0, 0, 2, 0, new ExtractedLipidCrystal()));
        formulas.add(new Formula(0, 1, 0, 2, 0, new HighTempCookedMeat()));
        formulas.add(new Formula(0, 0, 1, 2, 0, new ConcentratedBroth()));
        formulas.add(new Formula(0, 0, 0, 2, 1, new MountainSoup()));

        formulas.add(new Formula(1, 0, 0, 0, 2, new KnightSupplement()));
        formulas.add(new Formula(0, 1, 0, 0, 2, new NanoDish()));
        formulas.add(new Formula(0, 0, 1, 0, 2, new SoupExtract()));
        formulas.add(new Formula(0, 0, 0, 1, 2, new PoetryGel()));


        formulas.add(new Formula(1, 1, 1, 0, 0, new HunterRiceBun()));
        formulas.add(new Formula(1, 1, 0, 1, 0, new FriedMixedMeatRice()));
        formulas.add(new Formula(1, 1, 0, 0, 1, new CrabExtract()));
        formulas.add(new Formula(1, 0, 1, 1, 0, new CompoundRefinedSauce()));
        formulas.add(new Formula(1, 0, 1, 0, 1, new PolysulfideGranules()));
        formulas.add(new Formula(1, 0, 0, 1, 1, new ThickSoupExtract()));
        formulas.add(new Formula(0, 1, 1, 1, 0, new VacuumCooledMeatChop()));
        formulas.add(new Formula(0, 1, 1, 0, 1, new CarbonWaterFattyAggregate()));
        formulas.add(new Formula(0, 1, 0, 1, 1, new EnergyAgent()));
        formulas.add(new Formula(0, 0, 1, 1, 1, new MoleculeCapsule()));
        */

    }

    public static AbstractCard getFood(ArrayList<AbstractCard> ingredientsChosen)
    {
        //确保配方都被加载
        if (formulas == null)
        {
            loadFormulas();
        }
        //确保当前配方的容器存在
        if (currFormula == null)
        {
            currFormula = new Formula(0, 0, 0, 0, 0, 0, 0, null);
        }
        //读取当前配方
        for (AbstractCard c : ingredientsChosen)
        {
            if (c instanceof CrabLegs)
            {
                currFormula.crabLegs++;
            }
            else if (c instanceof Paddy)
            {
                currFormula.paddy++;
            }
            else if (c instanceof SlicedMeat)
            {
                currFormula.slicedMeat++;
            }
            else if (c instanceof FowlbeastWingsMeat)
            {
                currFormula.fowlbeastWingsMeat++;
            }
            else if (c instanceof LegMeatWithBone)
            {
                currFormula.legMeatWithBone++;
            }
            else if (c instanceof CartilageChunks)
            {
                currFormula.cartilageChunks++;
            }
            else if (c instanceof MeatEssence)
            {
                currFormula.meatEssence++;
            }
        }
        MizukiModCore.logger.info("crablegs:" + currFormula.crabLegs + ",paddy:" + currFormula.paddy
                + ",slicedMeat:" + currFormula.slicedMeat + ",fowlbeastWingsMeat:" + currFormula.fowlbeastWingsMeat + ",legMeatWithBone:" + currFormula.legMeatWithBone
                + ",cartilageChunks:" + currFormula.cartilageChunks + ",meatEssence:" + currFormula.meatEssence);
        //判断当前配方与哪个已存在配方相符合
        for (Formula formula : formulas)
        {
            if (formula.ifMatch(currFormula))
            {
                currFormula.reset();
                MizukiModCore.logger.info(formula.food.cardID);
                return formula.food;
            }
        }
        return null;
    }

    public static AbstractCard getRandomIngredient()
    {
        int roll = AbstractDungeon.cardRandomRng.random(99);

        AbstractCard c = null;
        if (roll < 60)
        {
            c = getRandomCommonIngredient();
        }
        else if (roll < 95)
        {
            c = getRandomUncommomIngredient();
        }
        else
        {
            c = getRareIngredient();
        }
        return c;
    }

    public static String getRandomIngredientString()
    {
        AbstractCard c = getRandomIngredient();

        return ingredientToString(c);
    }

    public static String ingredientToString(AbstractCard c)
    {
        if (c instanceof Paddy)
        {
            return "Paddy";
        }
        if (c instanceof CrabLegs)
        {
            return "CrabLegs";
        }
        if (c instanceof SlicedMeat)
        {
            return "SlicedMeat";
        }
        if (c instanceof FowlbeastWingsMeat)
        {
            return "FowlbeastWingsMeat";
        }
        if (c instanceof LegMeatWithBone)
        {
            return "LegMeatWithBone";
        }
        if (c instanceof CartilageChunks)
        {
            return "CartilageChunks";
        }
        if (c instanceof MeatEssence)
        {
            return "MeatEssence";
        }

        return null;
    }

    public static AbstractCard getRandomCommonIngredient()
    {
        int roll = AbstractDungeon.cardRandomRng.random(99);

        AbstractCard c = null;
        if (roll < 70)
        {
            c = new Paddy();
        }
        else
        {
            c = new CrabLegs();
        }
        return c;
    }

    public static AbstractCard getRandomUncommomIngredient()
    {
        int roll = AbstractDungeon.cardRandomRng.random(99);

        AbstractCard c = null;
        if (roll < 70)
        {
            c = new SlicedMeat();
        }
        else
        {
            c = new LegMeatWithBone();
        }
        return c;
    }

    public static AbstractCard getRareIngredient()
    {
        return new MeatEssence();
    }

    public static CardGroup getFoodInDeck()
    {
        CardGroup retVal = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        Iterator<AbstractCard> var2 = AbstractDungeon.player.masterDeck.group.iterator();
        while (var2.hasNext())
        {
            AbstractCard c = var2.next();
            if (c instanceof AbstractFoodCard)
            {
                retVal.group.add(c);
            }
        }
        return retVal;
    }

    public static boolean hasFoodInDeck()
    {
        CardGroup retVal = getFoodInDeck();
        if (retVal.group.isEmpty())
        {
            return false;
        }
        else
        {
            return true;
        }
    }
}
