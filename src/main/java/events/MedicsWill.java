package events;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import relics.DariosLantern;
import relics.NightsunGrass;
import relics.PortableScriptures;

public class MedicsWill extends AbstractImageEvent
{
    //医者之志，第一层
    public static final String ID = "Mizuki:MedicsWill";

    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString("Mizuki:MedicsWill");

    public static final String NAME = eventStrings.NAME;

    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;

    public static final String[] OPTIONS = eventStrings.OPTIONS;

    private static final String INTRO = DESCRIPTIONS[0];

    private static final String CHAT_DIALOG = DESCRIPTIONS[1];

    private static final String SILENT_DIALOG = DESCRIPTIONS[2];

    private CUR_SCREEN screen = CUR_SCREEN.INTRO;

    private enum CUR_SCREEN
    {
        INTRO, MAP;
    }

    public MedicsWill()
    {
        super(NAME, INTRO, "resources/img/event/MedicsWill.png");
        this.imageEventText.setDialogOption(OPTIONS[0], (AbstractRelic) new PortableScriptures());
        this.imageEventText.setDialogOption(OPTIONS[1], (AbstractRelic) new NightsunGrass());
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
                        this.imageEventText.updateBodyText(CHAT_DIALOG);
                        this.screen = CUR_SCREEN.MAP;
                        this.imageEventText.updateDialogOption(0, OPTIONS[2]);
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((Settings.WIDTH / 2F), (Settings.HEIGHT / 2F), (AbstractRelic)new PortableScriptures());
                        AbstractRelic givenRelic = AbstractDungeon.returnRandomScreenlessRelic(AbstractRelic.RelicTier.COMMON);
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH * 0.28F, Settings.HEIGHT / 2.0F, givenRelic);
                        break;
                    case 1:
                        this.imageEventText.updateBodyText(SILENT_DIALOG);
                        this.screen = CUR_SCREEN.MAP;
                        this.imageEventText.updateDialogOption(0, OPTIONS[2]);
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((Settings.WIDTH / 2F), (Settings.HEIGHT / 2F), (AbstractRelic)new NightsunGrass());
                        break;
                }
                this.imageEventText.removeDialogOption(1);
                return;
            case MAP:
                openMap();
                return;
        }
        openMap();
    }
}
