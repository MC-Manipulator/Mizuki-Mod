package events;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import relics.Bedrock;
import relics.Wavebreaker;

public class MeetinginaDream extends AbstractImageEvent
{
    //梦中相会，第二、三层
    public static final String ID = "Mizuki:MeetinginaDream";

    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString("Mizuki:MeetinginaDream");

    public static final String NAME = eventStrings.NAME;

    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;

    public static final String[] OPTIONS = eventStrings.OPTIONS;

    private static final String INTRO = DESCRIPTIONS[0];

    private static final String SOUNDWAY_DIALOG = DESCRIPTIONS[1];

    private static final String SOUNDDETAIL_DIALOG = DESCRIPTIONS[2];

    private static final String LANGUAGEMODE_DIALOG = DESCRIPTIONS[3];

    private static final String RECORDINFO_DIALOG = DESCRIPTIONS[4];

    private static final String CONCLUDEINFO_DIALOG = DESCRIPTIONS[5];

    private static final String OBSERVELOOKS_DIALOG = DESCRIPTIONS[6];

    private static final String CONCLUDELOOKS_DIALOG = DESCRIPTIONS[7];

    private static final String GETCLOSE_DIALOG = DESCRIPTIONS[8];

    private static final String COMMUNICATE_DIALOG = DESCRIPTIONS[9];

    private static final String WAKEUP_DIALOG = DESCRIPTIONS[10];

    private CUR_SCREEN screen = CUR_SCREEN.INTRO;

    private enum CUR_SCREEN
    {
        INTRO, FIRST, SECOND, MAP;
    }

    public MeetinginaDream()
    {
        super(NAME, INTRO, "resources/img/event/MeetinginaDream.png");
        this.imageEventText.setDialogOption(OPTIONS[0]);
        this.imageEventText.setDialogOption(OPTIONS[1]);
        this.imageEventText.setDialogOption(OPTIONS[3]);
        this.imageEventText.setDialogOption(OPTIONS[5]);
        this.imageEventText.setDialogOption(OPTIONS[9]);
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
                        this.imageEventText.updateBodyText(SOUNDWAY_DIALOG);
                        break;
                    case 1:
                        this.imageEventText.updateBodyText(SOUNDDETAIL_DIALOG);
                        break;
                    case 2:
                        this.imageEventText.updateBodyText(RECORDINFO_DIALOG);
                        break;
                    case 3:
                        this.imageEventText.updateBodyText(LANGUAGEMODE_DIALOG);
                        break;
                    case 4:
                        this.imageEventText.updateBodyText(WAKEUP_DIALOG);
                        this.screen = CUR_SCREEN.MAP;
                        this.imageEventText.optionList.clear();
                        this.imageEventText.setDialogOption(OPTIONS[10]);
                        return;
                }
                this.screen = CUR_SCREEN.FIRST;
                this.imageEventText.optionList.clear();
                this.imageEventText.setDialogOption(OPTIONS[2]);
                this.imageEventText.setDialogOption(OPTIONS[4]);
                this.imageEventText.setDialogOption(OPTIONS[6]);
                this.imageEventText.setDialogOption(OPTIONS[9]);
                return;
            case FIRST:
                switch (buttonPressed)
                {
                    case 0:
                        this.imageEventText.updateBodyText(LANGUAGEMODE_DIALOG);
                        break;
                    case 1:
                        this.imageEventText.updateBodyText(CONCLUDEINFO_DIALOG);
                        break;
                    case 2:
                        this.imageEventText.updateBodyText(CONCLUDELOOKS_DIALOG);
                        break;
                    case 3:
                        this.imageEventText.updateBodyText(WAKEUP_DIALOG);
                        this.screen = CUR_SCREEN.MAP;
                        this.imageEventText.optionList.clear();
                        this.imageEventText.setDialogOption(OPTIONS[10]);
                        return;
                }
                this.screen = CUR_SCREEN.SECOND;
                this.imageEventText.optionList.clear();
                this.imageEventText.setDialogOption(OPTIONS[7]);
                this.imageEventText.setDialogOption(OPTIONS[8]);
                this.imageEventText.setDialogOption(OPTIONS[9]);
                return;
            case SECOND:
                switch (buttonPressed)
                {
                    case 0:
                        this.imageEventText.updateBodyText(GETCLOSE_DIALOG);
                        break;
                    case 1:
                        this.imageEventText.updateBodyText(COMMUNICATE_DIALOG);
                        break;
                    case 3:
                        this.imageEventText.updateBodyText(WAKEUP_DIALOG);
                        this.screen = CUR_SCREEN.MAP;
                        this.imageEventText.optionList.clear();
                        this.imageEventText.setDialogOption(OPTIONS[10]);
                        return;
                }
                this.screen = CUR_SCREEN.MAP;
                this.imageEventText.optionList.clear();
                this.imageEventText.setDialogOption(OPTIONS[10]);
                return;
            case MAP:
                openMap();
                return;
        }
        openMap();
    }
    private void generateOptions()
    {
        this.imageEventText.clearRemainingOptions();
    }
}
