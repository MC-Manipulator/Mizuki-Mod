package events;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import relics.JetBlackDanceShoes;
import relics.PureWhiteDanceShoes;

public class SingerbytheSea extends AbstractImageEvent
{
    public static final String ID = "Mizuki:SingerbytheSea";

    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString("Mizuki:SingerbytheSea");

    public static final String NAME = eventStrings.NAME;

    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;

    public static final String[] OPTIONS = eventStrings.OPTIONS;

    private static final String INTRO = DESCRIPTIONS[0];

    private static final String LISTEN_DIALOG = DESCRIPTIONS[1];

    private static final String GETCLOSE_DIALOG = DESCRIPTIONS[2];

    private static final String LEAVE_DIALOG = DESCRIPTIONS[3];

    private CUR_SCREEN screen = CUR_SCREEN.INTRO;

    private static final int loseHealthForListen = 4;

    private static final int loseHealthForGetClose = 8;

    private boolean pickCard = false;

    private enum CUR_SCREEN
    {
        INTRO, MAP;
    }

    public SingerbytheSea()
    {
        super(NAME, INTRO, "resources/img/event/SingerbytheSea.png");
        this.imageEventText.setDialogOption(OPTIONS[0] + loseHealthForListen + OPTIONS[1]);
        this.imageEventText.setDialogOption(OPTIONS[2] + loseHealthForGetClose + OPTIONS[3]);
        this.imageEventText.setDialogOption(OPTIONS[4]);
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
                        this.imageEventText.updateBodyText(LISTEN_DIALOG);
                        AbstractDungeon.player.damage(new DamageInfo((AbstractCreature) AbstractDungeon.player, loseHealthForListen));
                        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.HIGH, ScreenShake.ShakeDur.SHORT, false);
                        if (CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards())
                                .size() > 0)
                            AbstractDungeon.gridSelectScreen.open(
                                    CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck
                                            .getPurgeableCards()), 1, "沉浸在歌声当中...", false, false, false, true);
                        this.pickCard = true;
                        break;
                    case 1:
                        this.imageEventText.updateBodyText(GETCLOSE_DIALOG);
                        AbstractDungeon.player.damage(new DamageInfo((AbstractCreature) AbstractDungeon.player, loseHealthForGetClose));
                        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.HIGH, ScreenShake.ShakeDur.SHORT, false);
                        AbstractRelic givenRelic;
                        int roll = MathUtils.random(1);
                        if (roll == 1)
                        {
                            givenRelic = new JetBlackDanceShoes();
                        }
                        else
                        {
                            givenRelic = new PureWhiteDanceShoes();
                        }
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH * 0.28F, Settings.HEIGHT / 2.0F, givenRelic);
                        break;
                    case 2:
                        this.imageEventText.updateBodyText(LEAVE_DIALOG);
                        break;
                }
                this.screen = CUR_SCREEN.MAP;
                this.imageEventText.updateDialogOption(0, OPTIONS[4]);
                this.imageEventText.removeDialogOption(1);
                this.imageEventText.removeDialogOption(1);
                return;
            case MAP:
                openMap();
                return;
        }
        openMap();
    }

}
