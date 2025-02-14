package events;

import cards.Attacks.ResidualLight;
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
import relics.DariosLantern;

public class AFaintSpark extends AbstractImageEvent
{
    public static final String ID = "Mizuki:AFaintSpark";

    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString("Mizuki:AFaintSpark");

    public static final String NAME = eventStrings.NAME;

    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;

    public static final String[] OPTIONS = eventStrings.OPTIONS;

    private static final String INTRO = DESCRIPTIONS[0];

    private static final String RISK_DIALOG = DESCRIPTIONS[1];

    private static final String REMIND_DIALOG = DESCRIPTIONS[2];

    private static final String GIVEUP_DIALOG = DESCRIPTIONS[3];

    private CUR_SCREEN screen = CUR_SCREEN.INTRO;

    private enum CUR_SCREEN
    {
        INTRO, MAP;
    }

    public AFaintSpark()
    {
        super(NAME, INTRO, "resources/img/event/AFaintSpark.png");
        this.imageEventText.setDialogOption(OPTIONS[0], CardLibrary.getCopy("Mizuki_ResidualLight"));
        this.imageEventText.setDialogOption(OPTIONS[1], (AbstractRelic) new DariosLantern());
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
        ResidualLight residualLight;
        switch (this.screen)
        {
            case INTRO:
                switch (buttonPressed)
                {
                    case 0:
                        residualLight = new ResidualLight();
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect((AbstractCard) residualLight, (Settings.WIDTH / 2), (Settings.HEIGHT / 2)));
                        this.imageEventText.updateBodyText(RISK_DIALOG);
                        this.screen = CUR_SCREEN.MAP;
                        this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                        this.imageEventText.removeDialogOption(1);
                        this.imageEventText.removeDialogOption(1);
                        return;
                    case 1:
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((Settings.WIDTH / 2), (Settings.HEIGHT / 2), (AbstractRelic)new DariosLantern());
                        logMetricObtainRelic(ID, "Remind", (AbstractRelic)new DariosLantern());

                        this.imageEventText.updateBodyText(REMIND_DIALOG);
                        this.screen = CUR_SCREEN.MAP;
                        this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                        this.imageEventText.removeDialogOption(1);
                        this.imageEventText.removeDialogOption(1);
                        return;
                    case 2:
                        this.imageEventText.updateBodyText(GIVEUP_DIALOG);
                        this.screen = CUR_SCREEN.MAP;
                        this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                        this.imageEventText.removeDialogOption(1);
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
