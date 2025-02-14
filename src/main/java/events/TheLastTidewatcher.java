package events;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.exordium.GremlinNob;
import com.megacrit.cardcrawl.monsters.exordium.Lagavulin;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import monsters.friendlys.AbstractFriendly;
import monsters.friendlys.Lumen;
import patches.FriendlyPatch;
import relics.OceanVoyage;
import relics.VortexConfluence;

import java.util.ArrayList;

public class TheLastTidewatcher  extends AbstractImageEvent
{
    public static final String ID = "Mizuki:TheLastTidewatcher";

    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString("Mizuki:TheLastTidewatcher");

    public static final String NAME = eventStrings.NAME;

    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;

    public static final String[] OPTIONS = eventStrings.OPTIONS;

    private static final String INTRO = DESCRIPTIONS[0];

    private static final String HELP_DIALOG = DESCRIPTIONS[1];

    private static final String LEAVE_DIALOG = DESCRIPTIONS[2];

    private static final String COMMUNICATE_DIALOG = DESCRIPTIONS[3];

    private static final String REMEMBER_DIALOG = DESCRIPTIONS[4];

    private static final String INQUIRE_DIALOG = DESCRIPTIONS[5];

    private CUR_SCREEN screen = CUR_SCREEN.INTRO;

    private static final int raiseMaxHealth = 8;

    private enum CUR_SCREEN
    {
        INTRO, BATTLE, MAP, COMMUNICATE;
    }

    public TheLastTidewatcher()
    {
        super(NAME, INTRO, "resources/img/event/TheLastTidewatcher.png");
        this.imageEventText.setDialogOption(OPTIONS[0]);
        this.imageEventText.setDialogOption(OPTIONS[4]);
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
                        this.imageEventText.updateBodyText(HELP_DIALOG);
                        this.screen = CUR_SCREEN.BATTLE;
                        this.imageEventText.updateDialogOption(0, OPTIONS[1]);
                        break;
                    case 1:
                        this.imageEventText.updateBodyText(LEAVE_DIALOG);
                        this.screen = CUR_SCREEN.MAP;
                        this.imageEventText.updateDialogOption(0, OPTIONS[5]);
                        break;
                }
                this.imageEventText.removeDialogOption(1);
                return;
            case COMMUNICATE:
                switch (buttonPressed)
                {
                    case 0:
                        this.imageEventText.updateBodyText(INQUIRE_DIALOG);
                        AbstractDungeon.player.increaseMaxHp(raiseMaxHealth, true);
                        this.screen = CUR_SCREEN.MAP;
                        this.imageEventText.optionList.clear();
                        this.imageEventText.setDialogOption(OPTIONS[4]);
                        break;
                    case 1:
                        this.imageEventText.updateBodyText(REMEMBER_DIALOG);
                        AbstractRelic givenRelic;
                        int roll = MathUtils.random(1);
                        if (roll == 1)
                        {
                            givenRelic = new OceanVoyage();
                        }
                        else
                        {
                            givenRelic = new VortexConfluence();
                        }
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH * 0.28F, Settings.HEIGHT / 2.0F, givenRelic);

                        this.screen = CUR_SCREEN.MAP;
                        this.imageEventText.optionList.clear();
                        this.imageEventText.setDialogOption(OPTIONS[4]);
                        break;
                }

                return;
            case BATTLE:
                this.screen = CUR_SCREEN.COMMUNICATE;
                (AbstractDungeon.getCurrRoom()).monsters = new MonsterGroup(new AbstractMonster[]
                        {
                                new Lagavulin(false),
                                new GremlinNob(-200.0F, 0.0F)
                        });
                (AbstractDungeon.getCurrRoom()).eliteTrigger = true;
                (AbstractDungeon.getCurrRoom()).rewards.clear();
                (AbstractDungeon.getCurrRoom()).rewardAllowed = false;
                Lumen i = new Lumen(0, 0);
                if (FriendlyPatch.FriendlyFields.FriendlyList.get(AbstractDungeon.player) == null);
                {
                    FriendlyPatch.FriendlyFields.FriendlyList.set(AbstractDungeon.player, new ArrayList<AbstractFriendly>());
                }
                FriendlyPatch.Inst().add(i);

                /*
                if (AbstractDungeon.ascensionLevel >= 13) {
                    AbstractDungeon.getCurrRoom().addGoldToRewards(25);
                } else {
                    AbstractDungeon.getCurrRoom().addGoldToRewards(50);
                }*/
                enterCombatFromImage();
                return;
            case MAP:

                openMap();
                return;
        }
        openMap();
    }

    public void reopen()
    {
        if (this.screen != CUR_SCREEN.MAP)
        {
            AbstractDungeon.resetPlayer();
            AbstractDungeon.player.drawX = Settings.WIDTH * 0.25F;
            AbstractDungeon.player.preBattlePrep();
            enterImageFromCombat();
            this.imageEventText.updateBodyText(COMMUNICATE_DIALOG);
            this.imageEventText.optionList.clear();
            this.imageEventText.setDialogOption(OPTIONS[2] + raiseMaxHealth + OPTIONS[3]);
            this.imageEventText.setDialogOption(OPTIONS[6]);
            CardCrawlGame.music.fadeOutTempBGM();
            boolean hasLumen = false;
            for (int j = 0;j < FriendlyPatch.FriendlyFields.FriendlyList.get(AbstractDungeon.player).size();j++)
            {
                if (FriendlyPatch.FriendlyFields.FriendlyList.get(AbstractDungeon.player).get(j) instanceof Lumen)
                {
                    FriendlyPatch.FriendlyFields.FriendlyList.get(AbstractDungeon.player).remove(j);
                    break;
                }
            }
        }
    }
}
