package events;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import monsters.special.BladehelmKnightclubTrainee;
import relics.CathedralPuzzle;
import relics.NightsunGrass;
import relics.PortableScriptures;

public class HeartofaFool extends AbstractImageEvent
{
    //愚者之心，第二、三层
    public static final String ID = "Mizuki:HeartofaFool";

    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString("Mizuki:HeartofaFool");

    public static final String NAME = eventStrings.NAME;

    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;

    public static final String[] OPTIONS = eventStrings.OPTIONS;

    private static final String INTRO = DESCRIPTIONS[0];

    private static final String BATTLE_DIALOG = DESCRIPTIONS[1];

    private static final String REWARD_DIALOG = DESCRIPTIONS[2];

    private static final String LEAVE_DIALOG = DESCRIPTIONS[3];

    private static final String LEAVE2_DIALOG = DESCRIPTIONS[3];

    private CUR_SCREEN screen = CUR_SCREEN.INTRO;

    private int goldAmount = 250;

    private enum CUR_SCREEN
    {
        INTRO, MAP, BATTLE, REWARD;
    }

    public HeartofaFool()
    {
        super(NAME, INTRO, "resources/img/event/HeartofaFool.png");
        boolean ifHasAnyRelic = false;
        if (AbstractDungeon.player.hasRelic(PortableScriptures.ID))
        {
            this.imageEventText.setDialogOption(OPTIONS[0]);
            ifHasAnyRelic = true;
        }
        else
        {
            this.imageEventText.setDialogOption(OPTIONS[1], true);
        }

        if (AbstractDungeon.player.hasRelic(NightsunGrass.ID))
        {
            this.imageEventText.setDialogOption(OPTIONS[2]);
            ifHasAnyRelic = true;
        }
        else
        {
            this.imageEventText.setDialogOption(OPTIONS[3], true);
        }
        if (!ifHasAnyRelic)
        {
            this.imageEventText.setDialogOption(OPTIONS[9]);
        }
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
                        this.imageEventText.updateBodyText(BATTLE_DIALOG);
                        this.screen = CUR_SCREEN.BATTLE;
                        this.imageEventText.updateDialogOption(0, OPTIONS[4]);
                        this.imageEventText.removeDialogOption(1);
                        break;
                    case 1:
                        this.imageEventText.updateBodyText(REWARD_DIALOG);
                        this.screen = CUR_SCREEN.REWARD;
                        this.imageEventText.updateDialogOption(0, OPTIONS[5]);
                        this.imageEventText.updateDialogOption(1, OPTIONS[6] + goldAmount + OPTIONS[7]);
                        break;
                    case 2:
                        this.imageEventText.updateBodyText(LEAVE2_DIALOG);
                        this.screen = CUR_SCREEN.MAP;
                        this.imageEventText.updateDialogOption(0, OPTIONS[8]);
                        this.imageEventText.removeDialogOption(1);
                        this.imageEventText.removeDialogOption(1);
                        break;
                }
                return;
            case BATTLE:
                (AbstractDungeon.getCurrRoom()).monsters = new MonsterGroup(new AbstractMonster[]
                        {(AbstractMonster)new BladehelmKnightclubTrainee(80.0F, 0.0F) });
                (AbstractDungeon.getCurrRoom()).eliteTrigger = true;
                (AbstractDungeon.getCurrRoom()).rewards.clear();

                enterCombatFromImage();
                this.screen = CUR_SCREEN.MAP;
                return;
            case REWARD:
                switch (buttonPressed)
                {
                    case 0:
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((Settings.WIDTH / 2F), (Settings.HEIGHT / 2F), (AbstractRelic)new CathedralPuzzle());

                        /*
                        AbstractCard card = null;
                        AbstractCard.CardRarity cardRarity;
                        cardRarity = AbstractCard.CardRarity.RARE;
                        while (true)
                        {
                            card = CardLibrary.getAnyColorCard(cardRarity);
                            if (card.color == Mizuki.Enums.MIZUKI_CARD && card.color != FoodCardColorEnumPatch.CardColorPatch.FOOD)
                            {
                                break;
                            }
                        }
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect((AbstractCard) card, (Settings.WIDTH / 2.0F), (Settings.HEIGHT / 2.0F)));
                        */
                        break;
                    case 1:
                        AbstractDungeon.effectList.add(new RainingGoldEffect(goldAmount));
                        AbstractDungeon.player.gainGold(goldAmount);
                        break;
                }
                this.imageEventText.updateBodyText(LEAVE_DIALOG);
                this.screen = CUR_SCREEN.MAP;
                this.imageEventText.updateDialogOption(0, OPTIONS[9]);
                this.imageEventText.removeDialogOption(1);
                return;
            case MAP:
                openMap();
                return;
        }
        openMap();
    }
}
