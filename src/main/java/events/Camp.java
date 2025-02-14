package events;

import characters.Mizuki;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import patches.FoodCardColorEnumPatch;

public class Camp extends AbstractImageEvent
{
    public static final String ID = "Mizuki:Camp";

    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString("Mizuki:Camp");

    public static final String NAME = eventStrings.NAME;

    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;

    public static final String[] OPTIONS = eventStrings.OPTIONS;

    private static final String INTRO = DESCRIPTIONS[0];

    private static final String SLEEP_DIALOG = DESCRIPTIONS[1];

    private static final String RECALL_DIALOG = DESCRIPTIONS[2];

    private static final int recoverHealth = 20;

    private CUR_SCREEN screen = CUR_SCREEN.INTRO;

    private enum CUR_SCREEN
    {
        INTRO, MAP;
    }

    public Camp()
    {
        super(NAME, INTRO, "resources/img/event/Camp.png");
        this.imageEventText.setDialogOption(OPTIONS[0] + recoverHealth + OPTIONS[1]);
        this.imageEventText.setDialogOption(OPTIONS[2]);
    }

    public void update()
    {
        super.update();
        if (this.waitForInput)
            buttonEffect(GenericEventDialog.getSelectedOption());
    }

    protected void buttonEffect(int buttonPressed)
    {
        switch (this.screen)
        {
            case INTRO:
                switch (buttonPressed)
                {
                    case 0:
                        this.imageEventText.updateBodyText(SLEEP_DIALOG);
                        AbstractDungeon.player.heal(recoverHealth, true);
                        break;
                    case 1:
                        this.imageEventText.updateBodyText(RECALL_DIALOG);
                        AbstractCard card = null;
                        int roll = MathUtils.random(99);
                        AbstractCard.CardRarity cardRarity;

                        int rollCard = AbstractDungeon.cardRandomRng.random(99);
                        if (rollCard < 60)
                        {
                            cardRarity = AbstractCard.CardRarity.COMMON;
                        }
                        else
                        {
                            cardRarity = AbstractCard.CardRarity.UNCOMMON;
                        }
                        while (true)
                        {
                            card = CardLibrary.getAnyColorCard(cardRarity);
                            if (card.color == Mizuki.Enums.MIZUKI_CARD && card.color != FoodCardColorEnumPatch.CardColorPatch.FOOD)
                            {
                                break;
                            }
                        }
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect((AbstractCard) card, (Settings.WIDTH / 2.0F), (Settings.HEIGHT / 2.0F)));
                        break;
                }
                this.screen = CUR_SCREEN.MAP;
                this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                this.imageEventText.removeDialogOption(1);
                return;
            case MAP:
                openMap();
                return;
        }
        openMap();
    }
}
