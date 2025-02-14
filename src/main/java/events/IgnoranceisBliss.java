package events;

import characters.Mizuki;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import modcore.MizukiModCore;
import relics.*;

import java.util.ArrayList;

public class IgnoranceisBliss extends AbstractImageEvent
{
    //无知是福，第二、三层
    public static final String ID = "Mizuki:IgnoranceisBliss";

    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString("Mizuki:IgnoranceisBliss");

    public static final String NAME = eventStrings.NAME;

    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;

    public static final String[] OPTIONS = eventStrings.OPTIONS;

    private static final String INTRO = DESCRIPTIONS[0];

    private static final String POWERUP_DIALOG = DESCRIPTIONS[1];

    private static final String SURVIVAL_DIALOG = DESCRIPTIONS[2];

    private static final String MIGRATION_DIALOG = DESCRIPTIONS[3];

    private static final String REPRODUCTION_DIALOG = DESCRIPTIONS[4];

    private static final String GROWTH_DIALOG = DESCRIPTIONS[5];

    private static final String LEAVE_DIALOG = DESCRIPTIONS[6];
    private static final String LEAVE2_DIALOG = DESCRIPTIONS[7];

    private CUR_SCREEN screen = CUR_SCREEN.INTRO;

    private ArrayList<Integer> option;

    private final float healPercent = 0.35F;

    private enum CUR_SCREEN
    {
        INTRO, POWERUP, MAP;
    }

    public IgnoranceisBliss()
    {
        super(NAME, INTRO, "resources/img/event/IgnoranceisBliss.png");
        this.imageEventText.setDialogOption(OPTIONS[0]);
        this.imageEventText.setDialogOption(OPTIONS[5]);
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
                        this.imageEventText.updateBodyText(POWERUP_DIALOG);
                        this.screen = CUR_SCREEN.POWERUP;
                        this.imageEventText.optionList.clear();
                        option = new ArrayList<>();
                        int roll = MathUtils.random(2,3);
                        MizukiModCore.logger.info(roll);
                        for (int i = 0;i < roll;i++)
                        {
                            int roll2 = MathUtils.random(1,4);
                            MizukiModCore.logger.info(roll2);
                            if (option.contains(roll2))
                            {
                                continue;
                            }
                            else
                            {
                                option.add(roll2);
                                if (roll2 == 1)
                                {
                                    this.imageEventText.setDialogOption(OPTIONS[1], new KingsFellowship());
                                }
                                else if (roll2 == 2)
                                {
                                    this.imageEventText.setDialogOption(OPTIONS[2], new ViviparousLily());
                                }
                                else if (roll2 == 3)
                                {
                                    this.imageEventText.setDialogOption(OPTIONS[3], new ChitinousRipper());
                                }
                                else
                                {
                                    this.imageEventText.setDialogOption(OPTIONS[4]);
                                }
                            }
                        }

                        break;
                    case 1:
                        this.imageEventText.updateBodyText(LEAVE_DIALOG);
                        this.screen = CUR_SCREEN.MAP;
                        this.imageEventText.removeDialogOption(1);
                        this.imageEventText.updateDialogOption(0, OPTIONS[6]);
                        break;
                }
                return;
            case POWERUP:
                if (option.get(buttonPressed) == 1)
                {
                    AbstractDungeon.getCurrRoom().spawnRelicAndObtain((Settings.WIDTH / 2F), (Settings.HEIGHT / 2F), (AbstractRelic)new KingsFellowship());

                }
                else if (option.get(buttonPressed) == 2)
                {
                    AbstractDungeon.getCurrRoom().spawnRelicAndObtain((Settings.WIDTH / 2F), (Settings.HEIGHT / 2F), (AbstractRelic)new ViviparousLily());

                }
                else if (option.get(buttonPressed) == 3)
                {
                    AbstractDungeon.getCurrRoom().spawnRelicAndObtain((Settings.WIDTH / 2F), (Settings.HEIGHT / 2F), (AbstractRelic)new ChitinousRipper());

                }
                else
                {
                    AbstractDungeon.player.heal(MathUtils.ceil(AbstractDungeon.player.currentHealth * healPercent), true);
                }
                this.imageEventText.updateBodyText(LEAVE2_DIALOG);
                this.screen = CUR_SCREEN.MAP;
                this.imageEventText.optionList.clear();
                this.imageEventText.setDialogOption(OPTIONS[5]);
                return;
            case MAP:
                openMap();
                return;
        }
        openMap();
    }
}
