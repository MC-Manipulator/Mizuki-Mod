package events;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import monsters.special.Gopnik;

public class SomeTimetoKill extends AbstractImageEvent
{
    //打发时间，第二层
    public static final String ID = "Mizuki:SomeTimetoKill";

    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString("Mizuki:SomeTimetoKill");

    public static final String NAME = eventStrings.NAME;

    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;

    public static final String[] OPTIONS = eventStrings.OPTIONS;

    private static final String INTRO = DESCRIPTIONS[0];

    private static final String DRIVEAWAY_DIALOG = DESCRIPTIONS[1];

    private static final String WALKAWAY_DIALOG = DESCRIPTIONS[2];

    private CUR_SCREEN screen = CUR_SCREEN.INTRO;

    private enum CUR_SCREEN
    {
        INTRO, MAP, BATTLE;
    }

    public SomeTimetoKill()
    {
        super(NAME, INTRO, "resources/img/event/SomeTimetoKill.png");
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
                        this.imageEventText.updateBodyText(DRIVEAWAY_DIALOG);
                        this.screen = CUR_SCREEN.BATTLE;
                        this.imageEventText.updateDialogOption(0, OPTIONS[1]);
                        this.imageEventText.removeDialogOption(1);
                        return;
                    case 1:
                        this.imageEventText.updateBodyText(WALKAWAY_DIALOG);
                        this.screen = CUR_SCREEN.MAP;
                        this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                        this.imageEventText.removeDialogOption(1);
                        return;
                }
            case BATTLE:
                (AbstractDungeon.getCurrRoom()).monsters = new MonsterGroup(new AbstractMonster[]
                        {(AbstractMonster)new Gopnik(0.0F, 0.0F) });
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
