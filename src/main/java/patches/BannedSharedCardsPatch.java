package patches;


import cards.AbstractFoodCard;
import cards.Attacks.MindTentacle;
import cards.Curses.ConcentrationDisorder;
import cards.Curses.HemopoieticInhibition;
import cards.Curses.MetastaticAberration;
import cards.Curses.Neurodegeneration;
import cards.Food.Common.*;
import cards.Food.Rare.*;
import cards.Food.Uncommon.*;
import characters.Mizuki;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import relics.Gameplayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BannedSharedCardsPatch
{
    private static Map<AbstractPlayer.PlayerClass, List<String>> runLockedPotions = new HashMap<>();

    public static void registerRunLockedPotion(AbstractPlayer.PlayerClass playerClass, String potionId)
    {
        ((List<String>)runLockedPotions.computeIfAbsent(playerClass, _ignore -> new ArrayList())).add(potionId);
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "initializeCardPools")
    public static class CardPatch
    {
        public static void Postfix(AbstractDungeon __instance)
        {
            /*
            if (!EvilModeCharacterSelect.evilMode && !downfallMod.contentSharing_colorlessCards)
            {

            }*/
            AbstractDungeon.srcCurseCardPool.removeCard(ConcentrationDisorder.ID);
            AbstractDungeon.srcCurseCardPool.removeCard(HemopoieticInhibition.ID);
            AbstractDungeon.srcCurseCardPool.removeCard(MetastaticAberration.ID);
            AbstractDungeon.srcCurseCardPool.removeCard(Neurodegeneration.ID);

            if ((AbstractDungeon.player instanceof Mizuki))
            {
                AbstractDungeon.commonCardPool.group.removeIf(c -> (c instanceof AbstractFoodCard));
                AbstractDungeon.commonCardPool.removeCard(MindTentacle.ID);
                AbstractDungeon.srcCommonCardPool.group.removeIf(c -> (c instanceof AbstractFoodCard));

                AbstractDungeon.uncommonCardPool.group.removeIf(c -> (c instanceof AbstractFoodCard));
                AbstractDungeon.srcUncommonCardPool.group.removeIf(c -> (c instanceof AbstractFoodCard));

                AbstractDungeon.rareCardPool.group.removeIf(c -> (c instanceof AbstractFoodCard));
                AbstractDungeon.srcRareCardPool.group.removeIf(c -> (c instanceof AbstractFoodCard));

                /*
                AbstractDungeon.srcCommonCardPool.removeCard(CompoundRefinedSauce.ID);
                AbstractDungeon.srcCommonCardPool.removeCard(DriedLeanMeat.ID);
                AbstractDungeon.srcCommonCardPool.removeCard(FlavoredJerky.ID);
                AbstractDungeon.srcCommonCardPool.removeCard(HighTempCookedMeat.ID);
                AbstractDungeon.srcCommonCardPool.removeCard(LowTempCookedMeat.ID);
                AbstractDungeon.srcCommonCardPool.removeCard(MealSubstitutePowder.ID);
                AbstractDungeon.srcCommonCardPool.removeCard(ReheatedThickSoup.ID);
                AbstractDungeon.srcCommonCardPool.removeCard(VacuumCooledMeatChop.ID);

                AbstractDungeon.srcUncommonCardPool.removeCard(CarbonWaterFattyAggregate.ID);
                AbstractDungeon.srcUncommonCardPool.removeCard(ConcentratedBroth.ID);
                AbstractDungeon.srcUncommonCardPool.removeCard(CrabExtract.ID);
                AbstractDungeon.srcUncommonCardPool.removeCard(EnergyAgent.ID);
                AbstractDungeon.srcUncommonCardPool.removeCard(ExtractedLipidCrystal.ID);
                AbstractDungeon.srcUncommonCardPool.removeCard(FreshCondensedCube.ID);
                AbstractDungeon.srcUncommonCardPool.removeCard(PolysulfideGranules.ID);
                AbstractDungeon.srcUncommonCardPool.removeCard(SoupGel.ID);
                AbstractDungeon.srcUncommonCardPool.removeCard(TransferredChitosanBlock.ID);

                AbstractDungeon.srcRareCardPool.removeCard(CarnivalDinner.ID);
                AbstractDungeon.srcRareCardPool.removeCard(KnightSupplement.ID);
                AbstractDungeon.srcRareCardPool.removeCard(MoleculeCapsule.ID);
                AbstractDungeon.srcRareCardPool.removeCard(MountainSoup.ID);
                AbstractDungeon.srcRareCardPool.removeCard(NanoDish.ID);
                AbstractDungeon.srcRareCardPool.removeCard(PoetryGel.ID);
                AbstractDungeon.srcRareCardPool.removeCard(RoyalCube.ID);
                AbstractDungeon.srcRareCardPool.removeCard(SoupExtract.ID);
                AbstractDungeon.srcRareCardPool.removeCard(ThickSoupExtract.ID);*/

                /*
                ArrayList<AbstractCard> a = new ArrayList<>();
                for (AbstractCard c : AbstractDungeon.srcCommonCardPool.group)
                {
                    if (c.hasTag(Mizuki.Enums.FOOD_CARD))
                    {
                        a.add(c);
                    }
                }
                for (AbstractCard c : a)
                {
                    AbstractDungeon.srcCommonCardPool.removeCard(c.cardID);
                }

                a.clear();

                for (AbstractCard c : AbstractDungeon.srcUncommonCardPool.group)
                {
                    if (c.hasTag(Mizuki.Enums.FOOD_CARD))
                    {
                        a.add(c);
                    }
                }
                for (AbstractCard c : a)
                {
                    AbstractDungeon.srcUncommonCardPool.removeCard(c.cardID);
                }

                a.clear();

                for (AbstractCard c : AbstractDungeon.srcRareCardPool.group)
                {
                    if (c.hasTag(Mizuki.Enums.FOOD_CARD))
                    {
                        a.add(c);
                    }
                }
                for (AbstractCard c : a)
                {
                    AbstractDungeon.srcRareCardPool.removeCard(c.cardID);
                }*/
                //AbstractDungeon.srcCommonCardPool.removeCard();
                //AbstractDungeon.srcUncommonCardPool.removeCard();
                //AbstractDungeon.srcRareCardPool.removeCard();
            }
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "initializeRelicList")
    public static class RelicPatch
    {
        public static void Prefix(AbstractDungeon __instance)
        {
            if (!(AbstractDungeon.player instanceof Mizuki))
            {
                AbstractDungeon.relicsToRemoveOnStart.add(Gameplayer.ID);
            }
        }
    }

    @SpirePatch(clz = PotionHelper.class, method = "initialize")
    public static class PotionPatch
    {
        public static void Postfix(AbstractPlayer.PlayerClass chosenClass)
        {/*
            if (!EvilModeCharacterSelect.evilMode && !downfallMod.contentSharing_potions)
            {
                PotionHelper.potions.remove("hexamod:SoulburnPotion");
                PotionHelper.potions.remove("sneckomod:MuddlingPotion");
                PotionHelper.potions.remove("Slimebound:ThreeZeroPotion");
                PotionHelper.potions.remove("Guardian:BlockOnCardUsePotion");
                PotionHelper.potions.remove("champ:CounterPotion");
                PotionHelper.potions.remove("bronze:BurnAndBuffPotion");
                PotionHelper.potions.remove("gremlin:WizPotion");
                PotionHelper.potions.remove("expansioncontent:BossPotion");
            }
            BanSharedContentPatch.runLockedPotions.forEach((playerClass, potionIds) -> {
                if (chosenClass != playerClass && !HeartEvent.hasPlayedRun(playerClass))
                    PotionHelper.potions.removeAll(potionIds);
            });*/
        }
    }
}
