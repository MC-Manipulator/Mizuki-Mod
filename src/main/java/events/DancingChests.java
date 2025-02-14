package events;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import monsters.special.SarkazSentinel;

public class DancingChests extends AbstractImageEvent
{
    //宝箱之舞
    public static final String ID = "Mizuki:DancingChests";

    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString("Mizuki:DancingChests");

    public static final String NAME = eventStrings.NAME;

    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;

    public static final String[] OPTIONS = eventStrings.OPTIONS;

    private static final String INTRO = DESCRIPTIONS[0];

    private static final String ACCEPT_DIALOG = DESCRIPTIONS[1];

    private static final String REJECT_DIALOG = DESCRIPTIONS[2];

    private static final int recoverHealth = 20;

    private CUR_SCREEN screen = CUR_SCREEN.INTRO;

    private enum CUR_SCREEN
    {
        INTRO, BATTLE, MAP;
    }

    public DancingChests()
    {
        super(NAME, INTRO, "resources/img/event/DancingChests.png");
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
                        this.imageEventText.updateBodyText(ACCEPT_DIALOG);
                        this.screen = CUR_SCREEN.BATTLE;
                        this.imageEventText.updateDialogOption(0, OPTIONS[1]);
                        this.imageEventText.removeDialogOption(1);
                        break;
                    case 1:
                        this.imageEventText.updateBodyText(REJECT_DIALOG);
                        this.screen = CUR_SCREEN.MAP;
                        this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                        this.imageEventText.removeDialogOption(1);
                        break;
                }
                return;
            case BATTLE:
                (AbstractDungeon.getCurrRoom()).monsters = new MonsterGroup(
                        new AbstractMonster[] {
                                (AbstractMonster)new SarkazSentinel(-133.0F, 0.0F)});


                //(AbstractDungeon.getCurrRoom()).monsters = CardCrawlGame.dungeon.getEliteMonsterForRoomCreation();
                (AbstractDungeon.getCurrRoom()).eliteTrigger = true;
                (AbstractDungeon.getCurrRoom()).rewards.clear();

                enterCombatFromImage();
                this.screen = CUR_SCREEN.MAP;
                return;
            case MAP:
                openMap();
                return;
        }
        openMap();
    }
}
