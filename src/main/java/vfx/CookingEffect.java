package vfx;

import cards.AbstractMizukiCard;
import characters.Mizuki;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.FastCardObtainEffect;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import helper.CookingHelper;
import modcore.MizukiModCore;

import java.util.Iterator;
import java.util.ArrayList;

public class CookingEffect extends AbstractGameEffect
{
    public static final String[] TEXT;

    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("Mizuki_CookingCampfireOption");

    static
    {
        TEXT = uiStrings.TEXT;
    }

    public boolean openedScreen = false;

    public boolean CookableSelect = false;

    public boolean socketSelect = false;

    public boolean confirmSelect = false;

    public ArrayList<AbstractMizukiCard> ingredientsChosen;

    private Color screenColor;

    public CookingEffect()
    {
        this.screenColor = AbstractDungeon.fadeColor.cpy();
        this.duration = 1.5F;
        this.screenColor.a = 0.0F;
        AbstractDungeon.overlayMenu.proceedButton.hide();
    }

    public void update()
    {
        if (!AbstractDungeon.isScreenUp)
        {
            this.duration -= Gdx.graphics.getDeltaTime();
            updateBlackScreenColor();
        }
        if (!AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty() && this.CookableSelect && CookingHelper.ableToCook)
        {
            MizukiModCore.logger.info("COMPLETE SELECTING INGREDIENTS");
            CookableSelect = false;
            CookingHelper.gridScreenForCooking = false;
            CookingHelper.ableToCook = false;
            AbstractCard cg = CookingHelper.getFood(AbstractDungeon.gridSelectScreen.selectedCards);
            AbstractDungeon.effectsQueue.add(new ShowCardAndObtainEffect(cg.makeStatEquivalentCopy(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards)
            {
                AbstractDungeon.player.masterDeck.removeCard(c);
                c.stopGlowing();
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            ((RestRoom)AbstractDungeon.getCurrRoom()).fadeIn();
        }
        if (this.duration < 1.0F && !this.openedScreen)
        {
            this.openedScreen = true;
            CardGroup cookableCards = CookingHelper.getCookableCards();
            this.CookableSelect = true;
            CookingHelper.gridScreenForCooking = true;
            CookingHelper.confirmScreenForCooking = false;
            for (AbstractCard c : cookableCards.group)
                c.stopGlowing();
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            AbstractDungeon.gridSelectScreen.open(cookableCards, 4, TEXT[3], false, false, true, false);
        }
        if (this.duration < 0.0F)
        {
            this.isDone = true;
            if (CampfireUI.hidden)
            {
                CardGroup cookableCards = CookingHelper.getCookableCards();
                for (AbstractCard c : cookableCards.group)
                    c.stopGlowing();
                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                AbstractRoom.waitTimer = 0.0F;
                if (AbstractDungeon.getCurrRoom() instanceof RestRoom)
                {
                    CookingHelper.CookingCampfireOption.reCheck();
                    ((RestRoom)AbstractDungeon.getCurrRoom()).campfireUI.reopen();
                    ((RestRoom)AbstractDungeon.getCurrRoom()).cutFireSound();
                }
            }
        }
    }

    private void updateBlackScreenColor()
    {
        if (this.duration > 1.0F)
        {
            this.screenColor.a = Interpolation.fade.apply(1.0F, 0.0F, (this.duration - 1.0F) * 2.0F);
        }
        else
        {
            this.screenColor.a = Interpolation.fade.apply(0.0F, 1.0F, this.duration / 1.5F);
        }
    }

    public void render(SpriteBatch sb)
    {
        sb.setColor(this.screenColor);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0F, 0.0F, Settings.WIDTH, Settings.HEIGHT);
        if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.GRID)
            AbstractDungeon.gridSelectScreen.render(sb);
    }

    public void dispose()
    {

    }
}
