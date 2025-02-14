package events;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import relics.Bedrock;
import relics.NightsunGrass;
import relics.PortableScriptures;
import relics.Wavebreaker;

public class TraditionalTechnology extends AbstractImageEvent
{
    //传统技术，第二、三层
    public static final String ID = "Mizuki:TraditionalTechnology";

    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString("Mizuki:TraditionalTechnology");

    public static final String NAME = eventStrings.NAME;

    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;

    public static final String[] OPTIONS = eventStrings.OPTIONS;

    private static final String INTRO = DESCRIPTIONS[0];

    private static final String BEDROCK_DIALOG = DESCRIPTIONS[1];

    private static final String WAVEBREAKER_DIALOG = DESCRIPTIONS[2];

    private static final String LEAVE_DIALOG = DESCRIPTIONS[3];

    private CUR_SCREEN screen = CUR_SCREEN.INTRO;

    private enum CUR_SCREEN
    {
        INTRO, MAP;
    }

    public TraditionalTechnology()
    {
        super(NAME, INTRO, "resources/img/event/TraditionalTechnology.png");
        this.imageEventText.setDialogOption(OPTIONS[0], (AbstractRelic) new Bedrock());
        this.imageEventText.setDialogOption(OPTIONS[1], (AbstractRelic) new Wavebreaker());
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
                        this.imageEventText.updateBodyText(BEDROCK_DIALOG);
                        this.screen = CUR_SCREEN.MAP;
                        this.imageEventText.updateDialogOption(0, OPTIONS[2]);
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((Settings.WIDTH / 2F), (Settings.HEIGHT / 2F), (AbstractRelic)new Bedrock());
                        break;
                    case 1:
                        this.imageEventText.updateBodyText(WAVEBREAKER_DIALOG);
                        this.screen = CUR_SCREEN.MAP;
                        this.imageEventText.updateDialogOption(0, OPTIONS[2]);
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((Settings.WIDTH / 2F), (Settings.HEIGHT / 2F), (AbstractRelic)new Wavebreaker());
                        break;
                    case 2:
                        this.imageEventText.updateBodyText(LEAVE_DIALOG);
                        this.screen = CUR_SCREEN.MAP;
                        this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                        break;
                }
                this.imageEventText.removeDialogOption(2);
                this.imageEventText.removeDialogOption(1);
                return;
            case MAP:
                openMap();
                return;
        }
        openMap();
    }
}
