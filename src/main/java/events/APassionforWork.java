package events;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.MonsterInfo;
import monsters.special.TearyDetective;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class APassionforWork extends AbstractImageEvent
{
    //工作热情？，第二层
    public static final String ID = "Mizuki:APassionforWork";

    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString("Mizuki:APassionforWork");

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

    public APassionforWork()
    {
        super(NAME, INTRO, "resources/img/event/APassionforWork.png");
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
                ArrayList<MonsterInfo> monsters = new ArrayList<>();
                monsters.add(new MonsterInfo("Snake Plant", 6.0F));
                monsters.add(new MonsterInfo("Snecko", 4.0F));
                Collections.shuffle(monsters, new Random(AbstractDungeon.miscRng.randomLong()));

                AbstractMonster mo = (MonsterHelper.getEncounter(monsters.get(0).name)).monsters.get(0);
                mo.drawX -= 200 * Settings.xScale;
                (AbstractDungeon.getCurrRoom()).monsters = new MonsterGroup(new AbstractMonster[]
                        { mo, (AbstractMonster)new TearyDetective(80.0F, 0.0F, 1) });
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
