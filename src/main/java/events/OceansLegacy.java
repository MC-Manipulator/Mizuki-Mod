package events;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import helper.EventHelper;
import relics.Bedrock;
import relics.Wavebreaker;

public class OceansLegacy extends AbstractImageEvent
{
    //遗产、第二层
    public static final String ID = "Mizuki:OceansLegacy";

    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString("Mizuki:OceansLegacy");

    public static final String NAME = eventStrings.NAME;

    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;

    public static final String[] OPTIONS = eventStrings.OPTIONS;

    private static final String INTRO = DESCRIPTIONS[0];

    private static final String ALL_DIALOG = DESCRIPTIONS[1];

    private static final String PART_DIALOG = DESCRIPTIONS[2];

    private CUR_SCREEN screen = CUR_SCREEN.INTRO;

    private enum CUR_SCREEN
    {
        INTRO, MAP;
    }

    public OceansLegacy()
    {
        super(NAME, INTRO, "resources/img/event/OceansLegacy.png");
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
                AbstractRelic givenRelic;
                switch (buttonPressed)
                {
                    case 0:
                        this.imageEventText.updateBodyText(ALL_DIALOG);

                        AbstractCard card = EventHelper.Inst.getRandomRejection();
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect((AbstractCard) card, (Settings.WIDTH / 2.0F), (Settings.HEIGHT / 2.0F)));
                        givenRelic = AbstractDungeon.returnRandomScreenlessRelic(AbstractRelic.RelicTier.UNCOMMON);
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((Settings.WIDTH / 2F), (Settings.HEIGHT / 2F), givenRelic);
                        break;
                    case 1:
                        this.imageEventText.updateBodyText(PART_DIALOG);

                        givenRelic = AbstractDungeon.returnRandomScreenlessRelic(AbstractRelic.RelicTier.COMMON);
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((Settings.WIDTH / 2F), (Settings.HEIGHT / 2F), givenRelic);
                        break;
                }
                this.screen = CUR_SCREEN.MAP;
                this.imageEventText.optionList.clear();
                this.imageEventText.setDialogOption(OPTIONS[2]);
                return;
            case MAP:
                openMap();
                return;
        }
        openMap();
    }
}