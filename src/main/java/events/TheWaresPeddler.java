package events;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import relics.Bedrock;
import relics.PortableScriptures;
import relics.PulseoftheOcean;
import relics.Wavebreaker;

public class TheWaresPeddler extends AbstractImageEvent
{
    //变卖身家，第二、三层

    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString("Mizuki:TheWaresPeddler");

    public static final String NAME = eventStrings.NAME;

    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;

    public static final String[] OPTIONS = eventStrings.OPTIONS;

    private static final String INTRO = DESCRIPTIONS[0];

    private static final String BUY_DIALOG = DESCRIPTIONS[1];

    private static final String TRY_DIALOG = DESCRIPTIONS[2];

    private static final String LEAVE_DIALOG = DESCRIPTIONS[3];

    private CUR_SCREEN screen = CUR_SCREEN.INTRO;

    private final int healthCost = 7;

    private enum CUR_SCREEN
    {
        INTRO, MAP;
    }

    public TheWaresPeddler()
    {
        super(NAME, INTRO, "resources/img/event/TheWaresPeddler.png");
        if (AbstractDungeon.player.gold < 150)
        {
            this.imageEventText.setDialogOption(OPTIONS[5], true);
        }
        else
        {
            this.imageEventText.setDialogOption(OPTIONS[0], (AbstractRelic)new PulseoftheOcean());
        }
        this.imageEventText.setDialogOption(OPTIONS[1] + healthCost + OPTIONS[2], (AbstractRelic)new PulseoftheOcean());
        this.imageEventText.setDialogOption(OPTIONS[3]);
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
                        this.imageEventText.updateBodyText(BUY_DIALOG);
                        this.screen = CUR_SCREEN.MAP;
                        this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                        AbstractDungeon.player.loseGold(AbstractDungeon.player.gold);
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((Settings.WIDTH / 2F), (Settings.HEIGHT / 2F), (AbstractRelic)new PulseoftheOcean());
                        break;
                    case 1:
                        this.imageEventText.updateBodyText(TRY_DIALOG);
                        this.screen = CUR_SCREEN.MAP;
                        this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                        AbstractDungeon.player.decreaseMaxHealth(healthCost);
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((Settings.WIDTH / 2F), (Settings.HEIGHT / 2F), (AbstractRelic)new PulseoftheOcean());
                        break;
                    case 2:
                        this.imageEventText.updateBodyText(LEAVE_DIALOG);
                        this.screen = CUR_SCREEN.MAP;
                        this.imageEventText.updateDialogOption(0, OPTIONS[4]);
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
