package rewards;

import cards.Ingredients.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

public class IngredientReward extends AbstractMizukiReward
{
    private static final String path = "resources/img/reward/";
    private int ingredientCode = 0;
    public String ingredient;

    public IngredientReward(String ingredientName)
    {
        super(path + ingredientName + ".png", getDescription(ingredientName), Enums.Mizuki_Ingredient);
        this.ingredientCode = getIngredientCode(ingredientName);
        this.ingredient = ingredientName;
    }

    @Override
    public boolean claimReward()
    {
        AbstractCard c = null;
        switch (ingredientCode)
        {
            case 0:
                c = new Paddy();
                break;
            case 1:
                c = new CrabLegs();
                break;
            case 2:
                c = new SlicedMeat();
                break;
            case 3:
                c = new LegMeatWithBone();
                break;
            case 4:
                c = new MeatEssence();
                break;
        }
        if (c != null)
        {
            AbstractDungeon.effectsQueue.add(
                    new ShowCardAndObtainEffect(c, Settings.WIDTH / 1.5F, Settings.HEIGHT / 2.0F));
        }
        return true;
    }

    public static int getIngredientCode(String ingredientName)
    {
        int i = 0;

        if (ingredientName.equals("Paddy"))
        {
            i = 0;
        }
        if (ingredientName.equals("CrabLegs"))
        {
            i = 1;
        }
        if (ingredientName.equals("SlicedMeat"))
        {
            i = 2;
        }
        if (ingredientName.equals("LegMeatWithBone"))
        {
            i = 3;
        }
        if (ingredientName.equals("MeatEssence"))
        {
            i = 4;
        }

        return i;
    }

    public static String getDescription(String ingredientName)
    {
        return rewardString.TEXT[2 + getIngredientCode(ingredientName)];
    }
}
