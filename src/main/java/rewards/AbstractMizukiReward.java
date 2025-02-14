package rewards;

import basemod.abstracts.CustomReward;
import basemod.helpers.RelicType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.rewards.RewardItem;

public abstract class AbstractMizukiReward extends CustomReward
{
    protected static final UIStrings rewardString = CardCrawlGame.languagePack.getUIString("Mizuki_Reward");
    public AbstractMizukiReward(String path, String description, RewardType type)
    {
        super(getIcon(path), description, type);
    }

    @Override
    public boolean claimReward()
    {
        return true;
    }

    private static Texture getIcon(String path)
    {
        //path:"resources/img/reward/picture.png"
        Texture ICON = null;
        ICON = ImageMaster.loadImage(path);
        return ICON;
    }

    public static class Enums
    {
        @SpireEnum
        public static RewardItem.RewardType Mizuki_Ingredient;
        @SpireEnum
        public static RewardItem.RewardType Mizuki_SingleCard;
    }
}
