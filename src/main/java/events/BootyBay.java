package events;

import characters.Mizuki;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import modcore.MizukiModCore;

public class BootyBay extends AbstractImageEvent
{
    public static final String ID = "Mizuki:BootyBay";

    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString("Mizuki:BootyBay");

    public static final String NAME = eventStrings.NAME;

    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;

    public static final String[] OPTIONS = eventStrings.OPTIONS;

    private static final String INTRO = DESCRIPTIONS[0];

    private static final String AGREE_DIALOG = DESCRIPTIONS[1];

    private static final String DISAGREE_DIALOG = DESCRIPTIONS[2];

    private CUR_SCREEN screen = CUR_SCREEN.INTRO;

    private enum CUR_SCREEN
    {
        INTRO, MAP, BATTLE;
    }

    public BootyBay()
    {
        super(NAME, INTRO, "resources/img/event/BootyBay.png");
        this.imageEventText.setDialogOption(OPTIONS[0]);
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
                        this.imageEventText.updateBodyText(AGREE_DIALOG);
                        this.screen = CUR_SCREEN.BATTLE;
                        this.imageEventText.updateDialogOption(0, OPTIONS[1]);
                        break;
                    case 1:
                        this.imageEventText.updateBodyText(DISAGREE_DIALOG);
                        this.screen = CUR_SCREEN.MAP;
                        this.imageEventText.updateDialogOption(0, OPTIONS[2]);
                        break;
                }
                this.imageEventText.removeDialogOption(1);
                return;
            case BATTLE:
                this.screen = CUR_SCREEN.MAP;
                (AbstractDungeon.getCurrRoom()).monsters = CardCrawlGame.dungeon.getEliteMonsterForRoomCreation();
                (AbstractDungeon.getCurrRoom()).rewards.clear();
                (AbstractDungeon.getCurrRoom()).eliteTrigger = true;
                /*
                if (AbstractDungeon.ascensionLevel >= 13) {
                    AbstractDungeon.getCurrRoom().addGoldToRewards(25);
                } else {
                    AbstractDungeon.getCurrRoom().addGoldToRewards(50);
                }*/
                AbstractDungeon.getCurrRoom().addRelicToRewards(AbstractRelic.RelicTier.UNCOMMON);
                AbstractDungeon.lastCombatMetricKey = "Booty Bay Battle";
                enterCombatFromImage();
                return;
            case MAP:
                openMap();
                return;
        }
        openMap();
    }
}
