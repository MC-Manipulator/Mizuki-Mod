package events;

import cards.AbstractMizukiCard;
import cards.Attacks.ResidualLight;
import characters.Mizuki;
import com.badlogic.gdx.math.MathUtils;
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
import helper.EventHelper;
import patches.FoodCardColorEnumPatch;
import relics.DariosLantern;

public class SeabornScholar  extends AbstractImageEvent
{
    public static final String ID = "Mizuki:SeabornScholar";

    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString("Mizuki:SeabornScholar");

    public static final String NAME = eventStrings.NAME;

    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;

    public static final String[] OPTIONS = eventStrings.OPTIONS;

    private static final String INTRO = DESCRIPTIONS[0];

    private static final String AGREE_DIALOG = DESCRIPTIONS[1];

    private static final String REFUSE_DIALOG = DESCRIPTIONS[2];



    private CUR_SCREEN screen = CUR_SCREEN.INTRO;

    private enum CUR_SCREEN
    {
        INTRO, MAP;
    }

    public SeabornScholar()
    {
        super(NAME, INTRO, "resources/img/event/SeabornScholar.png");
        this.imageEventText.setDialogOption(OPTIONS[0]);
        this.imageEventText.setDialogOption(OPTIONS[1]);
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
                        AbstractCard card = null;
                        int roll = MathUtils.random(99);
                        if (roll < 60)
                        {
                            AbstractCard.CardRarity cardRarity;

                            int rollCard = AbstractDungeon.cardRandomRng.random(99);
                            if (rollCard < 50)
                            {
                                cardRarity = AbstractCard.CardRarity.COMMON;
                            }
                            else if (rollCard < 85)
                            {
                                cardRarity = AbstractCard.CardRarity.UNCOMMON;
                            }
                            else
                            {
                                cardRarity = AbstractCard.CardRarity.RARE;
                            }
                            while (true)
                            {
                                card = CardLibrary.getAnyColorCard(cardRarity);
                                if (card.color == Mizuki.Enums.MIZUKI_CARD && card.color != FoodCardColorEnumPatch.CardColorPatch.FOOD)
                                {
                                    break;
                                }
                            }
                        }
                        else
                        {
                            card = EventHelper.Inst.getRandomRejection();
                        }
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect((AbstractCard) card, (Settings.WIDTH / 2.0F), (Settings.HEIGHT / 2.0F)));
                        this.imageEventText.updateBodyText(AGREE_DIALOG);
                        this.screen = CUR_SCREEN.MAP;
                        this.imageEventText.updateDialogOption(0, OPTIONS[2]);
                        this.imageEventText.removeDialogOption(1);
                        return;
                    case 1:
                        this.imageEventText.updateBodyText(REFUSE_DIALOG);
                        this.screen = CUR_SCREEN.MAP;
                        this.imageEventText.updateDialogOption(0, OPTIONS[2]);
                        this.imageEventText.removeDialogOption(1);
                        return;
                }
            case MAP:
                openMap();
                return;
        }
        openMap();
    }

}
