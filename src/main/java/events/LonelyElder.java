package events;

import characters.Mizuki;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import patches.FoodCardColorEnumPatch;

public class LonelyElder extends AbstractImageEvent
{
    public static final String ID = "Mizuki:LonelyElder";

    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString("Mizuki:LonelyElder");

    public static final String NAME = eventStrings.NAME;

    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;

    public static final String[] OPTIONS = eventStrings.OPTIONS;

    private static final String INTRO = DESCRIPTIONS[0];

    private static final String CHECKHEALTH_DIALOG = DESCRIPTIONS[1] + DESCRIPTIONS[3] + DESCRIPTIONS[4];

    private static final String CHECKGOLD_DIALOG = DESCRIPTIONS[2] + DESCRIPTIONS[5] + DESCRIPTIONS[6];

    private static final String CHECKEQUIPMENT_DIALOG = DESCRIPTIONS[2] + DESCRIPTIONS[7] + DESCRIPTIONS[8];

    private static final String LEAVE_DIALOG = DESCRIPTIONS[9];

    private CUR_SCREEN screen = CUR_SCREEN.INTRO;

    private static final int recoverHealth = 25;

    private static final int getGold = 50;

    private boolean pickCard = false;
    private enum CUR_SCREEN
    {
        INTRO, HEALTH, GOLD, EQUIPMENT, CARD, MAP
    }

    public LonelyElder()
    {
        super(NAME, INTRO, "resources/img/event/LonelyElder.png");
        this.imageEventText.setDialogOption(OPTIONS[0]);
    }

    public void update()
    {
        super.update();
        if (this.waitForInput)
            buttonEffect(GenericEventDialog.getSelectedOption());
        if (this.pickCard &&
                !AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty())
        {
            AbstractCard upgCard;
            AbstractDungeon.effectsQueue.add(new UpgradeShineEffect(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
            ((AbstractCard)AbstractDungeon.gridSelectScreen.selectedCards.get(0)).upgrade();
            upgCard = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            AbstractDungeon.player.bottledCardUpgradeCheck(upgCard);
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            this.pickCard = false;
        }
    }

    protected void buttonEffect(int buttonPressed)
    {
        switch (this.screen)
        {
            case INTRO:
                if (AbstractDungeon.player.currentHealth < AbstractDungeon.player.maxHealth / 2.0F)
                {
                    this.imageEventText.updateBodyText(CHECKHEALTH_DIALOG);
                    this.imageEventText.updateDialogOption(0, OPTIONS[1] + recoverHealth + OPTIONS[2]);
                    this.screen = CUR_SCREEN.HEALTH;
                    return;
                }
                else if (AbstractDungeon.player.gold < 100)
                {
                    this.imageEventText.updateBodyText(CHECKGOLD_DIALOG);
                    this.imageEventText.updateDialogOption(0, OPTIONS[3] + getGold + OPTIONS[4]);
                    this.screen = CUR_SCREEN.GOLD;
                    return;
                }
                else if (AbstractDungeon.player.masterDeck.hasUpgradableCards())
                {
                    this.imageEventText.updateBodyText(CHECKEQUIPMENT_DIALOG);
                    this.imageEventText.updateDialogOption(0, OPTIONS[5]);
                    this.screen = CUR_SCREEN.EQUIPMENT;
                    return;
                }
                else
                {
                    this.imageEventText.updateBodyText(CHECKEQUIPMENT_DIALOG);
                    this.imageEventText.updateDialogOption(0, OPTIONS[7]);
                    this.screen = CUR_SCREEN.CARD;
                    return;
                }
            case HEALTH:
                this.imageEventText.updateBodyText(LEAVE_DIALOG);
                this.imageEventText.updateDialogOption(0, OPTIONS[6]);
                AbstractDungeon.player.heal(recoverHealth, true);
                this.screen = CUR_SCREEN.MAP;
                return;
            case GOLD:
                this.imageEventText.updateBodyText(LEAVE_DIALOG);
                this.imageEventText.updateDialogOption(0, OPTIONS[6]);
                AbstractDungeon.effectList.add(new RainingGoldEffect(getGold));
                AbstractDungeon.player.gainGold(getGold);
                this.screen = CUR_SCREEN.MAP;
                return;
            case EQUIPMENT:
                this.imageEventText.updateBodyText(LEAVE_DIALOG);
                this.imageEventText.updateDialogOption(0, OPTIONS[6]);
                this.screen = CUR_SCREEN.MAP;
                if (CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards())
                        .size() > 0)
                    AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck
                            .getUpgradableCards(), 1, "老者的细心检查...", true, false, false, false);
                pickCard = true;
                return;
            case CARD:
                this.imageEventText.updateBodyText(LEAVE_DIALOG);
                this.imageEventText.updateDialogOption(0, OPTIONS[6]);
                this.screen = CUR_SCREEN.MAP;
                AbstractCard card = CardLibrary.getAnyColorCard(AbstractCard.CardRarity.RARE);
                while (true)
                {
                    card = CardLibrary.getAnyColorCard(AbstractCard.CardRarity.RARE);
                    if (card.color == Mizuki.Enums.MIZUKI_CARD && card.color != FoodCardColorEnumPatch.CardColorPatch.FOOD)
                    {
                        break;
                    }
                }
                AbstractDungeon.effectList.add(new ShowCardAndObtainEffect((AbstractCard) card, (Settings.WIDTH / 2.0F), (Settings.HEIGHT / 2.0F)));
                return;
            case MAP:
                openMap();
                return;
        }
        openMap();
    }
}
