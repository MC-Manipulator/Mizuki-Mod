package events;

import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;

public class ForeignGrave  extends AbstractImageEvent
{
    public static final String ID = "Mizuki:ForeignGrave";

    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString("Mizuki:ForeignGrave");

    public static final String NAME = eventStrings.NAME;

    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;

    public static final String[] OPTIONS = eventStrings.OPTIONS;

    private static final String INTRO = DESCRIPTIONS[0];

    private static final String MOURN_DIALOG = DESCRIPTIONS[1];

    private static final String LEAVE_DIALOG = DESCRIPTIONS[2];

    private CUR_SCREEN screen = CUR_SCREEN.INTRO;

    private boolean pickCard = false;

    private enum CUR_SCREEN
    {
        INTRO, MAP;
    }

    public ForeignGrave()
    {
        super(NAME, INTRO, "resources/img/event/ForeignGrave.png");
        this.imageEventText.setDialogOption(OPTIONS[0]);
        this.imageEventText.setDialogOption(OPTIONS[1]);
    }

    public void update()
    {
        super.update();
        if (this.waitForInput)
            buttonEffect(GenericEventDialog.getSelectedOption());
        if (this.pickCard && !AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty())
        {
            AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(AbstractDungeon.gridSelectScreen.selectedCards.get(0), (Settings.WIDTH / 2.0F), (Settings.HEIGHT / 2.0F)));
            AbstractDungeon.player.masterDeck.removeCard(AbstractDungeon.gridSelectScreen.selectedCards.get(0));
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            this.pickCard = false;
        }
    }

    protected void buttonEffect(int buttonPressed)
    {
        switch (this.screen)
        {
            case INTRO:
                switch (buttonPressed)
                {
                    case 0:
                        this.imageEventText.updateBodyText(MOURN_DIALOG);
                        this.imageEventText.updateDialogOption(0, OPTIONS[1]);
                        this.screen = CUR_SCREEN.MAP;
                        if (CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards())
                                .size() > 0)
                            AbstractDungeon.gridSelectScreen.open(
                                    CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck
                                            .getPurgeableCards()), 1, "虔诚的祈祷", false, false, false, true);
                        this.pickCard = true;
                        break;
                    case 1:
                        this.imageEventText.updateBodyText(LEAVE_DIALOG);
                        this.imageEventText.updateDialogOption(0, OPTIONS[2]);
                        this.screen = CUR_SCREEN.MAP;
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
