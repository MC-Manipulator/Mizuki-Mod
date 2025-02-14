package events;

import characters.Mizuki;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import helper.EventHelper;
import patches.FoodCardColorEnumPatch;
import relics.AbstractMizukiRelic;
import relics.Bedrock;
import relics.Wavebreaker;

import java.util.ArrayList;

public class NeitherBlacknorWhite extends AbstractImageEvent
{
    //是非黑白，第二、三层

    public static final String ID = "Mizuki:NeitherBlacknorWhite";

    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString("Mizuki:NeitherBlacknorWhite");

    public static final String NAME = eventStrings.NAME;

    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;

    public static final String[] OPTIONS = eventStrings.OPTIONS;

    private static final String INTRO = DESCRIPTIONS[0];

    private static final String GOOD_DIALOG = DESCRIPTIONS[1];

    private static final String BAD_DIALOG = DESCRIPTIONS[2];

    private static final String GOODLEAVE_DIALOG = DESCRIPTIONS[3];

    private static final String BADLEAVE_DIALOG = DESCRIPTIONS[4];

    private static final String LEAVE_DIALOG = DESCRIPTIONS[5];

    private CUR_SCREEN screen = CUR_SCREEN.INTRO;

    AbstractPlayer player = AbstractDungeon.player;

    private final int goldAmount = 100;

    private enum CUR_SCREEN
    {
        INTRO, GOOD, BAD, MAP;
    }

    public NeitherBlacknorWhite()
    {
        super(NAME, INTRO, "resources/img/event/NeitherBlacknorWhite.png");
        this.imageEventText.setDialogOption(OPTIONS[0]);
        this.imageEventText.setDialogOption(OPTIONS[7]);
        player = AbstractDungeon.player;
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
                        if (AbstractDungeon.player.currentHealth >= AbstractDungeon.player.maxHealth * 0.5F)
                        {
                            this.imageEventText.updateBodyText(GOOD_DIALOG);
                            this.screen = CUR_SCREEN.GOOD;
                            this.imageEventText.optionList.clear();
                            this.imageEventText.setDialogOption(OPTIONS[1]);
                            this.imageEventText.setDialogOption(OPTIONS[2]);
                        }
                        else
                        {
                            this.imageEventText.updateBodyText(BAD_DIALOG);
                            this.screen = CUR_SCREEN.BAD;
                            this.imageEventText.optionList.clear();
                            this.imageEventText.setDialogOption(OPTIONS[3] + goldAmount + OPTIONS[4]);
                            this.imageEventText.setDialogOption(OPTIONS[5]);
                        }
                        break;
                    case 1:
                        this.screen = CUR_SCREEN.MAP;
                        this.imageEventText.updateBodyText(LEAVE_DIALOG);
                        this.imageEventText.optionList.clear();
                        this.imageEventText.setDialogOption(OPTIONS[6]);
                        break;
                }
                return;
            case GOOD:
                ArrayList<AbstractCard> rewardCards;
                switch (buttonPressed)
                {
                    case 0:
                        rewardCards = generateCardChoices(AbstractCard.CardType.ATTACK);
                        if (!rewardCards.isEmpty())
                            AbstractDungeon.cardRewardScreen.open(rewardCards, null, "来自审判庭的礼物");
                        break;
                    case 1:
                        rewardCards = generateCardChoices(AbstractCard.CardType.SKILL);
                        if (!rewardCards.isEmpty())
                            AbstractDungeon.cardRewardScreen.open(rewardCards, null, "来自审判庭的礼物");
                        break;
                }

                this.imageEventText.updateBodyText(GOODLEAVE_DIALOG);
                this.imageEventText.optionList.clear();
                this.imageEventText.setDialogOption(OPTIONS[7]);
                this.screen = CUR_SCREEN.MAP;
                return;
            case BAD:
                AbstractCard card = EventHelper.Inst.getRandomRejection();
                AbstractDungeon.effectList.add(new ShowCardAndObtainEffect((AbstractCard) card, (Settings.WIDTH / 2.0F), (Settings.HEIGHT / 2.0F)));
                switch (buttonPressed)
                {
                    case 0:
                        AbstractDungeon.effectList.add(new RainingGoldEffect(goldAmount));
                        AbstractDungeon.player.gainGold(goldAmount);
                        break;
                    case 1:
                        AbstractRelic givenRelic = AbstractDungeon.returnRandomScreenlessRelic(AbstractRelic.RelicTier.UNCOMMON);
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH * 0.28F, Settings.HEIGHT / 2.0F, givenRelic);
                        break;
                }

                this.imageEventText.updateBodyText(BADLEAVE_DIALOG);
                this.imageEventText.optionList.clear();
                this.imageEventText.setDialogOption(OPTIONS[7]);
                this.screen = CUR_SCREEN.MAP;
                return;
            case MAP:
                openMap();
                return;
        }
        openMap();
    }

    private ArrayList<AbstractCard> generateCardChoices(AbstractCard.CardType type)
    {
        ArrayList<AbstractCard> derp = new ArrayList<>();
        while (derp.size() != 3)
        {
            AbstractCard.CardRarity cardRarity;
            boolean dupe = false;

            int roll = AbstractDungeon.cardRandomRng.random(99);
            if (roll < 55)
            {
                cardRarity = AbstractCard.CardRarity.COMMON;
            }
            else if (roll < 85)
            {
                cardRarity = AbstractCard.CardRarity.UNCOMMON;
            }
            else
            {
                cardRarity = AbstractCard.CardRarity.RARE;
            }
            AbstractCard tmp;
            while (true)
            {
                tmp = CardLibrary.getAnyColorCard(cardRarity);
                if (tmp.color == Mizuki.Enums.MIZUKI_CARD && tmp.color != FoodCardColorEnumPatch.CardColorPatch.FOOD && !tmp.tags.contains(AbstractCard.CardTags.HEALING))
                {
                    if (tmp.type == type)
                    {
                        break;
                    }
                }
            }

            for (AbstractCard c : derp)
            {
                if (c.cardID.equals(tmp.cardID))
                {
                    dupe = true;
                    break;
                }
            }
            if (!dupe)
            {
                AbstractCard tmp2 = tmp.makeCopy();
                derp.add(tmp2);
            }
        }

        return derp;
    }
}
