package relics;

import actions.EchoOfExplorationAction;
import characters.Mizuki;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;
import modcore.MizukiModCore;
import patches.FoodCardColorEnumPatch;

import java.util.ArrayList;

public class DoctorSilverSeal extends AbstractMizukiRelic
{
    //博士银印
    public static final String ID = MizukiModCore.MakePath(DoctorSilverSeal.class.getSimpleName());

    public DoctorSilverSeal()
    {
        super(ID, RelicTier.UNCOMMON, AbstractRelic.LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart()
    {
        addToBot(new AbstractGameAction()
        {
            public int cardAmount = 3;

            private boolean justIn = true;

            @Override
            public void update()
            {
                if (justIn)
                {
                    this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
                    justIn = false;
                }
                if (this.duration == this.startDuration)
                {
                    AbstractDungeon.cardRewardScreen.customCombatOpen(generateCardChoices(), "招募一张牌吧...", true);
                    tickDuration();
                    return;
                }
                if (AbstractDungeon.cardRewardScreen.discoveryCard != null)
                {
                    AbstractCard disCard = AbstractDungeon.cardRewardScreen.discoveryCard.makeStatEquivalentCopy();
                    disCard.current_x = -1000.0F * Settings.xScale;
                    if (AbstractDungeon.player.hand.size() < 10)
                    {
                        AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(disCard, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                    }
                    else
                    {
                        AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(disCard, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                    }
                    AbstractDungeon.cardRewardScreen.discoveryCard = null;
                }
                isDone = true;
                tickDuration();
            }

            private ArrayList<AbstractCard> generateCardChoices()
            {
                ArrayList<AbstractCard> derp = new ArrayList<>();
                while (derp.size() != cardAmount)
                {
                    AbstractCard.CardRarity cardRarity;
                    boolean dupe = false;
                    cardRarity = AbstractCard.CardRarity.RARE;

                    AbstractCard tmp;
                    while (true)
                    {
                        tmp = CardLibrary.getAnyColorCard(cardRarity);
                        if (tmp.color == Mizuki.Enums.MIZUKI_CARD &&
                                tmp.color != FoodCardColorEnumPatch.CardColorPatch.FOOD &&
                                !tmp.tags.contains(AbstractCard.CardTags.HEALING))
                        {
                            break;
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
        });

    }
}