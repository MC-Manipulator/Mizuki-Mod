package events;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import monsters.special.Irene;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class LingeringGlimmer extends AbstractImageEvent
{
    public static final String ID = "Mizuki:LingeringGlimmer";

    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString("Mizuki:LingeringGlimmer");

    public static final String NAME = eventStrings.NAME;

    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;

    public static final String[] OPTIONS = eventStrings.OPTIONS;

    private static final String INTRO = DESCRIPTIONS[0];

    private static final String FORCED_BATTLE_DIALOG = DESCRIPTIONS[1] + DESCRIPTIONS[3];

    private static final String BATTLE_DIALOG = DESCRIPTIONS[1] + DESCRIPTIONS[2];

    private static final String GIVELANTERN_DIALOG = DESCRIPTIONS[4];

    private static final String APPROACH_DIALOG = DESCRIPTIONS[5];

    private static final String COMMUNICATE_DIALOG = DESCRIPTIONS[6];

    private static final String LEAVE_DIALOG = DESCRIPTIONS[7];

    private CUR_SCREEN screen = CUR_SCREEN.INTRO;

    private static final int upgradeAmount = 3;


    private static final int goldAmount = 100;

    private static final float battlePercentage = 0.5f;

    private enum CUR_SCREEN
    {
        INTRO, LANTERN, CURSE, BATTLE, APPROACH, MAP;
    }

    public LingeringGlimmer()
    {
        super(NAME, INTRO, "resources/img/event/LingeringGlimmer.png");
        if (AbstractDungeon.player.hasRelic("Mizuki_DariosLantern"))
        {
            this.imageEventText.setDialogOption(OPTIONS[1] + upgradeAmount + OPTIONS[2], !AbstractDungeon.player.hasRelic("Mizuki_DariosLantern"));
        }
        else
        {
            this.imageEventText.setDialogOption(OPTIONS[0], !AbstractDungeon.player.hasRelic("Mizuki_DariosLantern"));
        }
        this.imageEventText.setDialogOption(OPTIONS[3] + battlePercentage * 100 + OPTIONS[4]);
        this.imageEventText.setDialogOption(OPTIONS[8]);
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
                        AbstractDungeon.player.loseRelic("Mizuki_DariosLantern");
                        upgradeCards();

                        this.imageEventText.updateBodyText(GIVELANTERN_DIALOG);
                        this.screen = CUR_SCREEN.LANTERN;
                        this.imageEventText.updateDialogOption(0, OPTIONS[8]);
                        this.imageEventText.removeDialogOption(1);
                        this.imageEventText.removeDialogOption(1);
                        return;
                    case 1:
                        int roll = MathUtils.random(99);
                        if (roll < 50)
                        {
                            this.imageEventText.updateBodyText(APPROACH_DIALOG);
                            this.screen = CUR_SCREEN.APPROACH;
                            this.imageEventText.updateDialogOption(0, OPTIONS[5] + goldAmount + OPTIONS[6]);
                            this.imageEventText.removeDialogOption(1);
                            this.imageEventText.removeDialogOption(1);
                        }
                        else
                        {
                            this.imageEventText.updateBodyText(BATTLE_DIALOG);
                            this.screen = CUR_SCREEN.BATTLE;
                            this.imageEventText.updateDialogOption(0, OPTIONS[7]);
                            this.imageEventText.removeDialogOption(1);
                            this.imageEventText.removeDialogOption(1);
                        }
                        return;
                    case 2:
                        this.imageEventText.updateBodyText(LEAVE_DIALOG);
                        this.screen = CUR_SCREEN.MAP;
                        this.imageEventText.updateDialogOption(0, OPTIONS[8]);
                        this.imageEventText.removeDialogOption(1);
                        this.imageEventText.removeDialogOption(1);
                        return;
                }
            case LANTERN:
                openMap();
                return;
            case BATTLE:
                (AbstractDungeon.getCurrRoom()).monsters = new MonsterGroup(new AbstractMonster[] { (AbstractMonster)new Irene(0.0F, 0.0F) });
                (AbstractDungeon.getCurrRoom()).eliteTrigger = true;
                //(AbstractDungeon.getCurrRoom()).rewards.clear();
                //(AbstractDungeon.getCurrRoom()).rewardAllowed = false;
                enterCombatFromImage();
                return;
            case APPROACH:
                switch (buttonPressed)
                {
                    case 0:
                        AbstractDungeon.effectList.add(new RainingGoldEffect(goldAmount));
                        AbstractDungeon.player.gainGold(goldAmount);

                        this.imageEventText.updateBodyText(COMMUNICATE_DIALOG);
                        this.screen = CUR_SCREEN.MAP;
                        this.imageEventText.updateDialogOption(0, OPTIONS[9]);
                        return;
                }

            case MAP:
                openMap();
                return;
        }
        openMap();
    }
    private void upgradeCards()
    {
        AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
        ArrayList<AbstractCard> upgradableCards = new ArrayList<>();
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group)
        {
            if (c.canUpgrade())
                upgradableCards.add(c);
        }
        List<String> cardMetrics = new ArrayList<>();
        Collections.shuffle(upgradableCards, new Random(AbstractDungeon.miscRng.randomLong()));
        if (!upgradableCards.isEmpty())
        {
            if (upgradableCards.size() == 1)
            {
                ((AbstractCard)upgradableCards.get(0)).upgrade();
                cardMetrics.add(((AbstractCard)upgradableCards.get(0)).cardID);
                AbstractDungeon.player.bottledCardUpgradeCheck(upgradableCards.get(0));
                AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(((AbstractCard)upgradableCards.get(0)).makeStatEquivalentCopy()));
            }
            else
            {
                for (int i = 0; i < (Math.min(upgradableCards.size(), upgradeAmount)); i++)
                {
                    ((AbstractCard)upgradableCards.get(i)).upgrade();
                    cardMetrics.add(((AbstractCard)upgradableCards.get(i)).cardID);
                    AbstractDungeon.player.bottledCardUpgradeCheck(upgradableCards.get(i));
                    AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(((AbstractCard)upgradableCards
                            .get(i)).makeStatEquivalentCopy(), Settings.WIDTH / 3F + MathUtils.random(500) * Settings.scale, Settings.HEIGHT / 3F + MathUtils.random(250)));
                }
            }
        }
    }
}
