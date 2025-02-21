package modcore;
/*     */ import basemod.abstracts.CustomSavable;
import basemod.abstracts.CustomSavableRaw;
import basemod.eventUtil.AddEventParams;
import basemod.eventUtil.EventUtils;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.DynamicTextBlocks;
import cards.AbstractMizukiCard;
/*     */ import characters.Mizuki;
import com.badlogic.gdx.Game;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.MonsterInfo;
import com.megacrit.cardcrawl.monsters.exordium.AcidSlime_M;
import com.megacrit.cardcrawl.monsters.exordium.FungiBeast;
import com.megacrit.cardcrawl.monsters.exordium.JawWorm;
import com.megacrit.cardcrawl.rewards.RewardSave;
import events.*;
import helper.EventHelper;
import helper.FourthMagicNumber;
import helper.SecondMagicNumber;
import helper.ThirdMagicNumber;
import monsters.boss.beyond.ParanoiaIllusion;
import monsters.boss.city.SalVientoBishopQuintus;
import monsters.boss.city.SalVientoBishopQuintusBody;
import monsters.boss.city.TheEndspeaker;
import monsters.boss.exordium.Pathshaper;
import monsters.boss.exordium.SaintCarmen;
import monsters.boss.exordium.TidelinkedBishop;
import monsters.boss.exordium.TidelinkedImmortal;
import monsters.normal.beyond.NetherseaPredator;
import monsters.normal.beyond.NetherseaReefbreaker;
import monsters.normal.beyond.NetherseaSwarmcaller;
import monsters.normal.beyond.SkimmingSeaDrifter;
import monsters.normal.city.BasinSeaReaper;
import monsters.normal.beyond.PocketSeaCrawler;
import monsters.normal.city.RetchingBroodmother;
import monsters.normal.exordium.BoneSeaDrifter;
import monsters.normal.exordium.DeepSeaSlider;
import monsters.elite.beyond.TheFirstToTalk;
import patches.CardSaver;
import patches.FoodCardColorEnumPatch;
import relics.*;
/*     */ import basemod.AutoAdd;
/*     */ import basemod.BaseMod;
/*     */ import basemod.abstracts.DynamicVariable;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
/*     */ import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
/*     */ import com.google.gson.Gson;
/*     */ import com.megacrit.cardcrawl.cards.AbstractCard;
/*     */ import com.megacrit.cardcrawl.characters.AbstractPlayer;
/*     */ import com.megacrit.cardcrawl.core.CardCrawlGame;
/*     */ import com.megacrit.cardcrawl.core.Settings;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ import com.megacrit.cardcrawl.relics.AbstractRelic;
/*     */ import com.megacrit.cardcrawl.rooms.AbstractRoom;
/*     */ import com.megacrit.cardcrawl.unlock.UnlockTracker;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
/*     */
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;
import java.util.Random;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
import rewards.AbstractMizukiReward;
import rewards.IngredientReward;
import rewards.SingleCard;
import ui.MizukiConfigUI;
import ui.MizukiSkinSelectScreen;
import vfx.DiceEffect;

@SpireInitializer
public class MizukiModCore
        implements EditCharactersSubscriber, EditStringsSubscriber, EditCardsSubscriber, EditRelicsSubscriber, EditKeywordsSubscriber, OnStartBattleSubscriber, AddAudioSubscriber, PostInitializeSubscriber, StartActSubscriber, StartGameSubscriber
{

    public MizukiModCore()
    {
        BaseMod.subscribe((ISubscriber)this);
        BaseMod.addColor(Mizuki.Enums.MIZUKI_CARD, BLUE, BLUE, BLUE, BLUE, BLUE, BLUE, BLUE, "resources/img/512/bg_attack_512.png", "resources/img/512/bg_skill_512.png", "resources/img/512/bg_power_512.png", "resources/img/char/cost_orb.png", "resources/img/1024/bg_attack.png", "resources/img/1024/bg_skill.png", "resources/img/1024/bg_power.png", "resources/img/char/card_orb.png", "resources/img/char/small_orb.png");
        BaseMod.addColor(FoodCardColorEnumPatch.CardColorPatch.FOOD, YELLOW, YELLOW, YELLOW, YELLOW, YELLOW, YELLOW, YELLOW, "resources/img/512/bg_attack_512.png", "resources/img/512/bg_skill_512.png", "resources/img/512/bg_power_512.png", "resources/img/char/cost_orb.png", "resources/img/1024/bg_attack.png", "resources/img/1024/bg_skill.png", "resources/img/1024/bg_power.png", "resources/img/char/card_orb.png", "resources/img/char/small_orb.png");

        BaseMod.addSaveField("MizukiCardSaver", (CustomSavableRaw)new CardSaver());
        //BaseMod.addSaveField("MizukiPowerSaver", (CustomSavableRaw)new PowerSaver());
    }



    public static final Logger logger = LogManager.getLogger(Mizuki.class);
    private static final String MY_CHARACTER_BUTTON = "resources/img/char/Button.png";
    private static final String MY_CHARACTER_PORTRAIT = "resources/img/char/Portrait.png";
    private static final String BG_ATTACK_512 = "resources/img/512/bg_attack_512.png";
    private static final String BG_POWER_512 = "resources/img/512/bg_power_512.png";
    private static final String BG_SKILL_512 = "resources/img/512/bg_skill_512.png";
    private static final String small_orb = "resources/img/char/small_orb.png";
    private static final String BG_ATTACK_1024 = "resources/img/1024/bg_attack.png";
    private static final String BG_POWER_1024 = "resources/img/1024/bg_power.png";
    private static final String BG_SKILL_1024 = "resources/img/1024/bg_skill.png";
    private static final String big_orb = "resources/img/char/card_orb.png";
    private static final String energy_orb = "resources/img/char/cost_orb.png";
    public static final Color BLUE = new Color(0.52F, 0.80F, 0.98F, 1.0F);
    public static final Color YELLOW = new Color(1F, 0.98F, 0.80F, 1.0F);
//	135,206,250
    public static boolean TutorialClosed = false;

    public static boolean originalMod;

    public static Properties originalModDefaults = new Properties();

    public static MizukiConfigUI Config;

    public static void initialize()
    {
        new MizukiModCore();

        try
        {
            Properties defaults = new Properties();
            defaults.setProperty("tutorialClosed", "false");
            SpireConfig config = new SpireConfig("Mizuki", "Common", defaults);
            TutorialClosed = config.getBool("tutorialClosed");
        }

        catch (IOException var2)
        {
            var2.printStackTrace();
        }
    }

    public void receiveEditCharacters()
    {
        logger.info("===正在添加人物===");
        logger.info("正在添加" + Mizuki.Enums.Mizuki.toString());
        BaseMod.addCharacter((AbstractPlayer)new Mizuki(CardCrawlGame.playerName), MY_CHARACTER_BUTTON, MY_CHARACTER_PORTRAIT, Mizuki.Enums.Mizuki);

        logger.info("===人物添加完成===");
    }

    public void receiveEditStrings()
    {
        String lang = "zh";
        if (Settings.language == Settings.GameLanguage.ZHS || Settings.language == Settings.GameLanguage.ZHT)
        {
            lang = "zh";
        }

        BaseMod.loadCustomStringsFile(RelicStrings.class, "resources/localization/Relics_" + lang + ".json");

        BaseMod.loadCustomStringsFile(CardStrings.class, "resources/localization/Cards_" + lang + ".json");

        BaseMod.loadCustomStringsFile(PowerStrings.class, "resources/localization/Powers_" + lang + ".json");

        BaseMod.loadCustomStringsFile(EventStrings.class, "resources/localization/Events_" + lang + ".json");

        BaseMod.loadCustomStringsFile(CharacterStrings.class, "resources/localization/Char_" + lang + ".json");

        BaseMod.loadCustomStringsFile(OrbStrings.class, "resources/localization/Orbs_" + lang + ".json");

        BaseMod.loadCustomStringsFile(UIStrings.class, "resources/localization/UI_" + lang + ".json");

        BaseMod.loadCustomStringsFile(MonsterStrings.class, "resources/localization/Monsters_" + lang + ".json");
    }


    public void receiveEditCards()
    {
        logger.info("===正在添加卡牌===");


        BaseMod.addDynamicVariable((DynamicVariable)new SecondMagicNumber());
        BaseMod.addDynamicVariable((DynamicVariable)new ThirdMagicNumber());
        BaseMod.addDynamicVariable((DynamicVariable)new FourthMagicNumber());
        AutoAdd cards = new AutoAdd("Mizuki");
        cards.packageFilter(AbstractMizukiCard.class).setDefaultSeen(true).any(AbstractMizukiCard.class, (info, card) ->
        {
            if (card != null)
            {
                BaseMod.addCard((AbstractCard)card);
                if (info.seen)
                {
                    UnlockTracker.unlockCard(card.cardID);
                }
            }
        });
        logger.info("===卡牌添加完成===");
        DynamicTextBlocks.registerCustomCheck("mizuki:LastBite", card ->
        {
            if (card.magicNumber > 1)
            {
                return 1;
            }
            else
            {
                return -1;
            }
        });
    }

    public void receiveEditRelics()
    {
        logger.info("===遗物===");

        BaseMod.addRelic((AbstractRelic)new Gameplayer(), RelicType.SHARED);
        BaseMod.addRelic((AbstractRelic)new DariosLantern(), RelicType.SHARED);
        BaseMod.addRelic((AbstractRelic)new Bedrock(), RelicType.SHARED);
        BaseMod.addRelic((AbstractRelic)new CathedralPuzzle(), RelicType.SHARED);
        BaseMod.addRelic((AbstractRelic)new KingsFellowship(), RelicType.SHARED);
        BaseMod.addRelic((AbstractRelic)new NightsunGrass(), RelicType.SHARED);
        BaseMod.addRelic((AbstractRelic)new PortableScriptures(), RelicType.SHARED);
        BaseMod.addRelic((AbstractRelic)new PulseoftheOcean(), RelicType.SHARED);
        BaseMod.addRelic((AbstractRelic)new Wavebreaker(), RelicType.SHARED);

        BaseMod.addRelic((AbstractRelic)new ViviparousLily(), RelicType.SHARED);
        BaseMod.addRelic((AbstractRelic)new ChitinousRipper(), RelicType.SHARED);
        BaseMod.addRelic((AbstractRelic)new PureWhiteDanceShoes(), RelicType.SHARED);
        BaseMod.addRelic((AbstractRelic)new JetBlackDanceShoes(), RelicType.SHARED);
        BaseMod.addRelic((AbstractRelic)new MizukisDice(), RelicType.SHARED);
        BaseMod.addRelic((AbstractRelic)new MizukisD8(), RelicType.SHARED);

        BaseMod.addRelic((AbstractRelic)new VortexConfluence(), RelicType.SHARED);
        BaseMod.addRelic((AbstractRelic)new OceanVoyage(), RelicType.SHARED);

        BaseMod.addRelic((AbstractRelic)new DeepSeaSireSculpture(), RelicType.SHARED);
        BaseMod.addRelic((AbstractRelic)new CastlesOffspring(), RelicType.SHARED);
        BaseMod.addRelic((AbstractRelic)new DuckLordsGoldenBrick(), RelicType.SHARED);
        BaseMod.addRelic((AbstractRelic)new NachzehrersCane(), RelicType.SHARED);
        BaseMod.addRelic((AbstractRelic)new OldFan(), RelicType.SHARED);

        BaseMod.addRelic((AbstractRelic)new FlashingSwords(), RelicType.SHARED);
        BaseMod.addRelic((AbstractRelic)new DoctorSilverSeal(), RelicType.SHARED);
        BaseMod.addRelic((AbstractRelic)new BlackTulip(), RelicType.SHARED);
        BaseMod.addRelic((AbstractRelic)new Unleashings(), RelicType.SHARED);
        BaseMod.addRelic((AbstractRelic)new PathfinderFins(), RelicType.SHARED);
        BaseMod.addRelic((AbstractRelic)new BlankCoral(), RelicType.SHARED);

        logger.info("===遗物完成===");
    }


    public void receiveAddAudio()
    {
        BaseMod.addAudio("MIZUKI_ThrowDice", "resources/sound/mizuki_throwdice.wav");
        BaseMod.addAudio("MIZUKI_DuckLord_Run", "resources/sound/e_skill_duckrun.wav");
        BaseMod.addAudio("MIZUKI_DuckLord_Die", "resources/sound/e_skill_duckdie.wav");
        BaseMod.addAudio("MIZUKI_Gopnik_HitCast", "resources/sound/e_skill_bearhitcast.wav");
        BaseMod.addAudio("MIZUKI_Gopnik_Hit", "resources/sound/e_skill_bearhit.wav");
        BaseMod.addAudio("MIZUKI_Gopnik_Die", "resources/sound/e_skill_beardie.wav");
        BaseMod.addAudio("MIZUKI_ATTACK", "resources/sound/p_imp_tentacle_n.wav");
        BaseMod.addAudio("MIZUKI_SKILL1", "resources/sound/p_imp_tentacle_h.wav");
        BaseMod.addAudio("MIZUKI_SKILL3", "resources/sound/p_imp_tentacle_s.wav");
        BaseMod.addAudio("MIZUKI_CHOOSE", "resources/sound/mizuki_choose.wav");
        BaseMod.addAudio("MIZUKI_EAT", "resources/sound/mizuki_eat.wav");
        BaseMod.addAudio("MIZUKI_Irene_Skill3_Attack", "resources/sound/p_skill_darkgun_shot.wav");
        BaseMod.addAudio("MIZUKI_Irene_Skill3_Hit", "resources/sound/p_skill_darkgun_hit.wav");
        BaseMod.addAudio("MIZUKI_Irene_Skill3_End", "resources/sound/p_skill_darkgun_end.wav");

        BaseMod.addAudio("LUMEN_ATTACK", "resources/sound/p_atk_fishheal_n.wav");
        BaseMod.addAudio("LUMEN_SKILL2", "resources/sound/p_imp_fishheal_h.wav");
        BaseMod.addAudio("LUMEN_SKILL1", "resources/sound/p_atk_fishheal_g.wav");
        BaseMod.addAudio("LUMEN_SKILL3_HIT", "resources/sound/p_imp_fishheal_s.wav");
        //BaseMod.addAudio("MIZUKI_ATTACK_NORMAL", "resources/sound/mizuki_choose.wav");
        //BaseMod.addAudio("MIZUKI_ATTACK_HEAVY", "resources/sound/mizuki_choose.wav");
        //BaseMod.addAudio("MIZUKI_STRIK", "resources/sound/mizuki_choose.wav");
        BaseMod.addAudio("MIZUKI_ENDBOSSBATTLE", "resources/sound/ENDBOSSBATTLE.wav");
        BaseMod.addAudio("MIZUKI_ENDBATTLE1", "resources/sound/ENDBATTLE1.wav");
        BaseMod.addAudio("MIZUKI_ENDBATTLE2", "resources/sound/ENDBATTLE2.wav");
        BaseMod.addAudio("MIZUKI_ENTERBATTLE1", "resources/sound/ENTERBATTLE1.wav");
        BaseMod.addAudio("MIZUKI_ENTERBATTLE2", "resources/sound/ENTERBATTLE2.wav");
    }

    public void receivePostInitialize()
    {
        receiveAddPotion();
        receiveAddMonster();
        receiveAddEvent();
        receiveEditKeywords();
        receiveImg();
        receiveAddReward();
        receiveConfig();
    }

    private void receiveConfig()
    {
        Config = new MizukiConfigUI();
        Config.Initialize();
        Config.InitializeOptions();
        MizukiSkinSelectScreen.Inst.Load();
    }



    private void receiveImg()
    {
        DiceEffect.getImg();
    }

    private void receiveAddPotion()
    {
    }

    private void receiveAddReward()
    {
        BaseMod.registerCustomReward(
                AbstractMizukiReward.Enums.Mizuki_SingleCard,
                (rewardSave) -> { // this handles what to do when this quest type is loaded.
                    return new SingleCard(AbstractCard.CardType.valueOf(rewardSave.id));
                },
                (customReward) -> { // this handles what to do when this quest type is saved.
                    return new RewardSave(customReward.type.toString(), (customReward instanceof SingleCard) ? ((SingleCard)customReward).rewardCardType.toString() : "ATTACK");
                });

        BaseMod.registerCustomReward(
                AbstractMizukiReward.Enums.Mizuki_Ingredient,
                (rewardSave) -> { // this handles what to do when this quest type is loaded.
                    return new IngredientReward(String.valueOf(rewardSave.id));
                },
                (customReward) -> { // this handles what to do when this quest type is saved.
                    return new RewardSave(customReward.type.toString(), (customReward instanceof IngredientReward) ? ((IngredientReward)customReward).ingredient.toString() : "Paddy");
                });
    }

    private void receiveAddMonster()
    {
        logger.info("===正在添加敌人==");
        BaseMod.addMonster("Mizuki:RetchingBroodmother", () -> new RetchingBroodmother(-133.0F, 0.0F));
        BaseMod.addStrongMonsterEncounter(TheCity.ID, new MonsterInfo("Mizuki:RetchingBroodmother", 5));

        BaseMod.addMonster("Mizuki:BasinSeaReaper", () -> new BasinSeaReaper(-133.0F, 0.0F));
        BaseMod.addMonsterEncounter(TheCity.ID, new MonsterInfo("Mizuki:BasinSeaReaper", 5));

        BaseMod.addMonster("Mizuki:2_BasinSeaReaper", () -> new MonsterGroup(new AbstractMonster[]
                {
                new BasinSeaReaper(-133.0F, 0.0F),
                new BasinSeaReaper(+133.0F, 0.0F)
        }));
        BaseMod.addStrongMonsterEncounter(TheCity.ID, new MonsterInfo("Mizuki:2_BasinSeaReaper", 5));

        BaseMod.addMonster("Mizuki:DeepSeaSlider", () -> new DeepSeaSlider(0, 0.0F));
        BaseMod.addMonsterEncounter(Exordium.ID, new MonsterInfo("Mizuki:DeepSeaSlider", 5));

        BaseMod.addMonster("Mizuki:BoneSeaDrifter", () -> new BoneSeaDrifter(0, 0.0F));
        BaseMod.addMonsterEncounter(Exordium.ID, new MonsterInfo("Mizuki:BoneSeaDrifter", 5));

        BaseMod.addMonster("Mizuki:BoneSeaDrifter_And_AcidSlime_M", () -> new MonsterGroup(new AbstractMonster[]
                {
                        new BoneSeaDrifter(-133.0F, 0.0F),
                        new AcidSlime_M(+133.0F, 0.0F)
                }));
        BaseMod.addStrongMonsterEncounter(Exordium.ID, new MonsterInfo("Mizuki:BoneSeaDrifter_And_AcidSlime_M", 5));

        BaseMod.addMonster("Mizuki:DeepSeaSlider_And_AcidSlime_M", () -> new MonsterGroup(new AbstractMonster[]
                {
                        new DeepSeaSlider(-133.0F, 0.0F),
                        new AcidSlime_M(+133.0F, 0.0F)
                }));
        BaseMod.addStrongMonsterEncounter(Exordium.ID, new MonsterInfo("Mizuki:DeepSeaSlider_And_AcidSlime_M", 5));

        BaseMod.addMonster("Mizuki:BoneSeaDrifter_And_FungiBeast", () -> new MonsterGroup(new AbstractMonster[]
                {
                        new BoneSeaDrifter(-133.0F, 0.0F),
                        new FungiBeast(+133.0F, 0.0F)
                }));
        BaseMod.addStrongMonsterEncounter(Exordium.ID, new MonsterInfo("Mizuki:BoneSeaDrifter_And_FungiBeast", 5));

        BaseMod.addMonster("Mizuki:DeepSeaSlider_And_FungiBeast", () -> new MonsterGroup(new AbstractMonster[]
                {
                        new DeepSeaSlider(-133.0F, 0.0F),
                        new FungiBeast(+133.0F, 0.0F)
                }));
        BaseMod.addStrongMonsterEncounter(Exordium.ID, new MonsterInfo("Mizuki:DeepSeaSlider_And_FungiBeast", 5));

        BaseMod.addMonster("Mizuki:DeepSeaSlider_And_BoneSeaDrifter", () -> new MonsterGroup(new AbstractMonster[]
                {
                        new DeepSeaSlider(-133.0F, 0.0F),
                        new BoneSeaDrifter(+133.0F, 0.0F)
                }));
        BaseMod.addStrongMonsterEncounter(Exordium.ID, new MonsterInfo("Mizuki:DeepSeaSlider_And_BoneSeaDrifter", 5));

        BaseMod.addMonster("Mizuki:2_SkimmingSeaDrifter", () -> new MonsterGroup(new AbstractMonster[]
                {
                        new SkimmingSeaDrifter(-203.0F, 0.0F),
                        new SkimmingSeaDrifter(+143.0F, 0.0F)
                }));
        BaseMod.addMonsterEncounter(TheBeyond.ID, new MonsterInfo("Mizuki:2_SkimmingSeaDrifter", 5));

        BaseMod.addMonster("Mizuki:3_SkimmingSeaDrifter", () -> new MonsterGroup(new AbstractMonster[]
                {
                        new SkimmingSeaDrifter(-500.0F, 0.0F),
                        new SkimmingSeaDrifter(-150.0F, 0.0F),
                        new SkimmingSeaDrifter(+203.0F, 0.0F)
                }));
        BaseMod.addStrongMonsterEncounter(TheBeyond.ID, new MonsterInfo("Mizuki:3_SkimmingSeaDrifter", 5));

        BaseMod.addMonster("Mizuki:JawWorms_And_NetherseaSwarmcaller", () -> new MonsterGroup(new AbstractMonster[]
                {
                        new JawWorm(-490.0F, 5.0F, true),
                        new NetherseaSwarmcaller(-150.0F, 0.0F),
                        new JawWorm(+203.0F, -20.0F, true)
                }));
        BaseMod.addStrongMonsterEncounter(TheBeyond.ID, new MonsterInfo("Mizuki:JawWorms_And_NetherseaSwarmcaller", 5));

        BaseMod.addMonster("Mizuki:NetherseaReefbreaker", () -> new NetherseaReefbreaker(0.0F, 0.0F));
        BaseMod.addStrongMonsterEncounter(TheBeyond.ID, new MonsterInfo("Mizuki:NetherseaReefbreaker", 5));

        BaseMod.addMonster("Mizuki:3_NetherseaPredator", () -> new MonsterGroup(new AbstractMonster[]
                {
                        new NetherseaPredator(-470, 20.0F),
                        new NetherseaPredator(-160.0F, 0.0F),
                        new NetherseaPredator(+213.0F, -10.0F)
                }));
        BaseMod.addStrongMonsterEncounter(TheBeyond.ID, new MonsterInfo("Mizuki:3_NetherseaPredator", 5));

        BaseMod.addMonster("Mizuki:2_PocketSeaCrawler", () -> new MonsterGroup(new AbstractMonster[]
                {
                        new PocketSeaCrawler(-160.0F, -10.0F),
                        new PocketSeaCrawler(+213.0F, 10.0F)
                }));
        BaseMod.addStrongMonsterEncounter(TheBeyond.ID, new MonsterInfo("Mizuki:2_PocketSeaCrawler", 5));

        BaseMod.addMonster("Mizuki:TheFirstToTalk", () -> new MonsterGroup(new AbstractMonster[]
                {
                        new TheFirstToTalk(-50.0F, -10.0F),
                }));
        BaseMod.addEliteEncounter(TheBeyond.ID, new MonsterInfo("Mizuki:TheFirstToTalk", 5));

        BaseMod.addMonster("Mizuki:SaintCarmen", () -> new MonsterGroup(new AbstractMonster[]
                {
                        new SaintCarmen(-50.0F, -10.0F),
                }));
        //BaseMod.addBoss(Exordium.ID, "Mizuki:SaintCarmen", "resources/img/UI/bossIcon/SaintCarmenIcon.png", "resources/img/UI/bossIcon/SaintCarmenIconOutline.png");

        BaseMod.addMonster("Mizuki:TidelinkedBishop", () -> new MonsterGroup(new AbstractMonster[]
                {
                        new TidelinkedImmortal(-150.0F, 10.0F),
                        new TidelinkedBishop(150.0F, -10.0F)
                }));
        //BaseMod.addBoss(Exordium.ID, "Mizuki:TidelinkedBishop", "resources/img/UI/bossIcon/TidelinkedBishopIcon.png", "resources/img/UI/bossIcon/TidelinkedBishopIconOutline.png");

        BaseMod.addMonster("Mizuki:Pathshaper", () -> new MonsterGroup(new AbstractMonster[]
                {
                        new Pathshaper(0, -10.0F)
                }));
        //BaseMod.addBoss(Exordium.ID, "Mizuki:Pathshaper", "resources/img/UI/bossIcon/PathshaperIcon.png", "resources/img/UI/bossIcon/PathshaperIconOutline.png");

        BaseMod.addMonster("Mizuki:TheEndspeaker", () -> new MonsterGroup(new AbstractMonster[]
                {
                        new TheEndspeaker(0.0F, 0.0F)
                }));
        //BaseMod.addBoss(TheCity.ID, "Mizuki:TheEndspeaker", "resources/img/UI/bossIcon/TheEndspeakerIcon.png", "resources/img/UI/bossIcon/TheEndspeakerIconOutline.png");


        BaseMod.addMonster("Mizuki:SalVientoBishopQuintus", () -> new MonsterGroup(new AbstractMonster[]
                {
                        new SalVientoBishopQuintusBody(-300.0F, 30.0F),
                        new SalVientoBishopQuintus(0.0F, 0.0F),
                        new SalVientoBishopQuintusBody(300.0F, 30.0F)
                }));
        //BaseMod.addBoss(TheCity.ID, "Mizuki:SalVientoBishopQuintus", "resources/img/UI/bossIcon/SalVientoBishopQuintusIcon.png", "resources/img/UI/bossIcon/SalVientoBishopQuintusIconOutline.png");

        BaseMod.addMonster("Mizuki:ParanoiaIllusion", () -> new MonsterGroup(new AbstractMonster[]
                {
                        new ParanoiaIllusion(0.0F, 0.0F)
                }));
        //BaseMod.addBoss(TheBeyond.ID, "Mizuki:ParanoiaIllusion", "resources/img/UI/bossIcon/ParanoiaIllusionIcon.png", "resources/img/UI/bossIcon/ParanoiaIllusionIconOutline.png");

        logger.info("===敌人已添加==");
    }

    private void receiveAddEvent()
    {
        logger.info("===正在添加事件==");
        BaseMod.addEvent((new AddEventParams.Builder("Mizuki:AFaintSpark", AFaintSpark.class))
                .eventType(EventUtils.EventType.ONE_TIME)
                .dungeonID("Exordium")
                .playerClass(Mizuki.Enums.Mizuki)
                .spawnCondition(() -> (!MizukiModCore.originalMod && CardCrawlGame.dungeon instanceof Exordium))
                .create());
        BaseMod.addEvent((new AddEventParams.Builder("Mizuki:BootyBay", BootyBay.class))
                .eventType(EventUtils.EventType.NORMAL)
                .dungeonID("Exordium")
                .playerClass(Mizuki.Enums.Mizuki)
                .spawnCondition(() -> (!MizukiModCore.originalMod))
                .create());
        BaseMod.addEvent((new AddEventParams.Builder("Mizuki:Camp", Camp.class))
                .eventType(EventUtils.EventType.NORMAL)
                .dungeonID("Exordium")
                .playerClass(Mizuki.Enums.Mizuki)
                .spawnCondition(() -> (!MizukiModCore.originalMod))
                .create());
        BaseMod.addEvent((new AddEventParams.Builder("Mizuki:ForeignGrave", ForeignGrave.class))
                .eventType(EventUtils.EventType.NORMAL)
                .dungeonID("Exordium")
                .playerClass(Mizuki.Enums.Mizuki)
                .spawnCondition(() -> (!MizukiModCore.originalMod))
                .create());
        BaseMod.addEvent((new AddEventParams.Builder("Mizuki:PuppyDogEyes", PuppyDogEyes.class))
                .eventType(EventUtils.EventType.NORMAL)
                .dungeonID("Exordium")
                .playerClass(Mizuki.Enums.Mizuki)
                .spawnCondition(() -> (!MizukiModCore.originalMod))
                .create());
        /*BaseMod.addEvent((new AddEventParams.Builder("Mizuki:SeabornScholar", SeabornScholar.class))
                .eventType(EventUtils.EventType.NORMAL)
                .dungeonID("Exordium")
                .playerClass(Mizuki.Enums.Mizuki)
                .create());*/
        /*BaseMod.addEvent((new AddEventParams.Builder("Mizuki:SingerbytheSea", SingerbytheSea.class))
                .eventType(EventUtils.EventType.NORMAL)
                .dungeonID("Exordium")
                .playerClass(Mizuki.Enums.Mizuki)
                .create());*/
        /*BaseMod.addEvent((new AddEventParams.Builder("Mizuki:TheLastTidewatcher", TheLastTidewatcher.class))
                .eventType(EventUtils.EventType.NORMAL)
                .dungeonID("Exordium")
                .playerClass(Mizuki.Enums.Mizuki)
                .create());*/
        BaseMod.addEvent((new AddEventParams.Builder("Mizuki:MedicsWill", MedicsWill.class))
                .eventType(EventUtils.EventType.ONE_TIME)
                .dungeonID("Exordium")
                .spawnCondition(() -> (!MizukiModCore.originalMod && CardCrawlGame.dungeon instanceof Exordium))
                .playerClass(Mizuki.Enums.Mizuki)
                .create());

        logger.info("===第一层可出现事件===");
        logger.info(AFaintSpark.NAME);
        logger.info(BootyBay.NAME);
        logger.info(Camp.NAME);
        logger.info(ForeignGrave.NAME);
        logger.info(PuppyDogEyes.NAME);
        logger.info(MedicsWill.NAME);
        logger.info("===事件已添加===");



        BaseMod.addEvent((new AddEventParams.Builder("Mizuki:LingeringGlimmer", LingeringGlimmer.class))
                .eventType(EventUtils.EventType.ONE_TIME)
                .playerClass(Mizuki.Enums.Mizuki)
                .dungeonID("TheCity")
                .spawnCondition(() -> (!MizukiModCore.originalMod && CardCrawlGame.dungeon instanceof TheCity))
                .create());
        BaseMod.addEvent((new AddEventParams.Builder("Mizuki:OceansLegacy", OceansLegacy.class))
                .eventType(EventUtils.EventType.NORMAL)
                .dungeonID("TheCity")
                .playerClass(Mizuki.Enums.Mizuki)
                .spawnCondition(() -> (!MizukiModCore.originalMod))
                .create());
        BaseMod.addEvent((new AddEventParams.Builder("Mizuki:SomeTimetoKill", SomeTimetoKill.class))
                .eventType(EventUtils.EventType.NORMAL)
                .dungeonIDs("TheCity")
                .playerClass(Mizuki.Enums.Mizuki)
                .spawnCondition(() -> (!MizukiModCore.originalMod))
                .create());
        BaseMod.addEvent((new AddEventParams.Builder("Mizuki:APassionforWork", APassionforWork.class))
                .eventType(EventUtils.EventType.NORMAL)
                .dungeonIDs("TheCity")
                .playerClass(Mizuki.Enums.Mizuki)
                .spawnCondition(() -> (!MizukiModCore.originalMod))
                .create());
        BaseMod.addEvent((new AddEventParams.Builder("Mizuki:DuckforceTest", DuckforceTest.class))
                .eventType(EventUtils.EventType.NORMAL)
                .dungeonIDs("TheCity")
                .playerClass(Mizuki.Enums.Mizuki)
                .spawnCondition(() -> (!MizukiModCore.originalMod))
                .create());
        BaseMod.addEvent((new AddEventParams.Builder("Mizuki:IgnoranceisBliss", IgnoranceisBliss.class))
                .eventType(EventUtils.EventType.NORMAL)
                .dungeonIDs("TheCity")
                .playerClass(Mizuki.Enums.Mizuki)
                .spawnCondition(() -> (!MizukiModCore.originalMod))
                .create());
        BaseMod.addEvent((new AddEventParams.Builder("Mizuki:MeetinginaDream", MeetinginaDream.class))
                .eventType(EventUtils.EventType.NORMAL)
                .dungeonIDs("TheCity")
                .playerClass(Mizuki.Enums.Mizuki)
                .spawnCondition(() -> (!MizukiModCore.originalMod))
                .create());

        logger.info("===第二层可出现事件===");
        logger.info(LingeringGlimmer.NAME);
        logger.info(OceansLegacy.NAME);
        logger.info(SomeTimetoKill.NAME);
        logger.info(APassionforWork.NAME);
        logger.info(DuckforceTest.NAME);
        logger.info(IgnoranceisBliss.NAME);
        logger.info(MeetinginaDream.NAME);
        logger.info("===事件已添加===");



        BaseMod.addEvent((new AddEventParams.Builder("Mizuki:SeabornScholar", SeabornScholar.class))
                .eventType(EventUtils.EventType.NORMAL)
                .dungeonIDs("TheCity", "Exordium")
                .playerClass(Mizuki.Enums.Mizuki)
                .spawnCondition(() -> (!MizukiModCore.originalMod))
                .create());
        BaseMod.addEvent((new AddEventParams.Builder("Mizuki:SingerbytheSea", SingerbytheSea.class))
                .eventType(EventUtils.EventType.NORMAL)
                .dungeonIDs("TheCity", "Exordium")
                .playerClass(Mizuki.Enums.Mizuki)
                .spawnCondition(() -> (!MizukiModCore.originalMod))
                .create());
        BaseMod.addEvent((new AddEventParams.Builder("Mizuki:LonelyElder", LonelyElder.class))
                .eventType(EventUtils.EventType.NORMAL)
                .dungeonIDs("TheCity", "Exordium")
                .playerClass(Mizuki.Enums.Mizuki)
                .spawnCondition(() -> (!MizukiModCore.originalMod))
                .create());
        BaseMod.addEvent((new AddEventParams.Builder("Mizuki:TheLastTidewatcher", TheLastTidewatcher.class))
                .eventType(EventUtils.EventType.NORMAL)
                .dungeonIDs("TheCity", "Exordium")
                .playerClass(Mizuki.Enums.Mizuki)
                .spawnCondition(() -> (!MizukiModCore.originalMod))
                .create());
        logger.info("===第一、二层都可出现事件===");
        logger.info(SeabornScholar.NAME);
        logger.info(SingerbytheSea.NAME);
        logger.info(LonelyElder.NAME);
        logger.info(TheLastTidewatcher.NAME);
        logger.info("===事件已添加==");


        BaseMod.addEvent((new AddEventParams.Builder("Mizuki:BusinessEmpire", BusinessEmpire.class))
                .eventType(EventUtils.EventType.NORMAL)
                .dungeonIDs("TheBeyond")
                .playerClass(Mizuki.Enums.Mizuki)
                .spawnCondition(() -> (!MizukiModCore.originalMod))
                .create());
        logger.info("===第三层可出现事件===");
        logger.info(BusinessEmpire.NAME);
        logger.info("===事件已添加==");


        BaseMod.addEvent((new AddEventParams.Builder("Mizuki:TraditionalTechnology", TraditionalTechnology.class))
                .eventType(EventUtils.EventType.NORMAL)
                .dungeonIDs("TheCity", "TheBeyond")
                .playerClass(Mizuki.Enums.Mizuki)
                .spawnCondition(() -> (!MizukiModCore.originalMod))
                .create());
        BaseMod.addEvent((new AddEventParams.Builder("Mizuki:TheWaresPeddler", TheWaresPeddler.class))
                .eventType(EventUtils.EventType.NORMAL)
                .dungeonIDs("TheCity", "TheBeyond")
                .playerClass(Mizuki.Enums.Mizuki)
                .spawnCondition(() -> (!MizukiModCore.originalMod))
                .create());
        BaseMod.addEvent((new AddEventParams.Builder("Mizuki:NeitherBlacknorWhite", NeitherBlacknorWhite.class))
                .eventType(EventUtils.EventType.NORMAL)
                .dungeonIDs("TheCity", "TheBeyond")
                .playerClass(Mizuki.Enums.Mizuki)
                .spawnCondition(() -> (!MizukiModCore.originalMod))
                .create());
        BaseMod.addEvent((new AddEventParams.Builder("Mizuki:HeartofaFool", HeartofaFool.class))
                .eventType(EventUtils.EventType.ONE_TIME)
                .spawnCondition(() -> ((CardCrawlGame.dungeon instanceof TheCity))
                        && !MizukiModCore.originalMod && (AbstractDungeon.player.hasRelic(PortableScriptures.ID) || AbstractDungeon.player.hasRelic(NightsunGrass.ID)))
                .playerClass(Mizuki.Enums.Mizuki)
                .dungeonIDs("TheCity")
                .endsWithRewardsUI(true)
                .create());
        logger.info("===第二、三层可出现事件===");
        logger.info(TraditionalTechnology.NAME);
        logger.info(TheWaresPeddler.NAME);
        logger.info(NeitherBlacknorWhite.NAME);
        logger.info(HeartofaFool.NAME);
        logger.info("===事件已添加==");

        BaseMod.addEvent((new AddEventParams.Builder("Mizuki:DancingChests", DancingChests.class))
                .eventType(EventUtils.EventType.NORMAL)
                .dungeonIDs("TheCity")
                .playerClass(Mizuki.Enums.Mizuki)
                .spawnCondition(() -> (!MizukiModCore.originalMod))
                .create());
    }

    public void receiveEditKeywords()
    {
        Gson gson = new Gson();
        String lang = "zh";
        /*
        String lang = "eng";
        if (Settings.language == Settings.GameLanguage.ZHS || Settings.language == Settings.GameLanguage.ZHT) {
            lang = "zh";
        } else if (Settings.language == Settings.GameLanguage.KOR) {
            lang = "kr";
        }*/
        logger.info("===正在添加关键词===");

        logger.info(lang);
        String json = Gdx.files.internal("resources/localization/Keywords_" + lang + ".json").readString(String.valueOf(StandardCharsets.UTF_8));

        logger.info("2");
        Keyword[] keywords = (Keyword[])gson.fromJson(json, Keyword[].class);
        logger.info("3");
        if (keywords != null)
            for (Keyword keyword : keywords)
            {
                logger.info(String.format("正在添加关键词：%s" , new Object[] { keyword.NAMES[0] }));
                BaseMod.addKeyword("mizuki", keyword.NAMES[0], keyword.NAMES, keyword.DESCRIPTION);
            }
        logger.info("===关键词已添加===");
    }

    public static String MakePath(String id)
    {
        return "Mizuki_" + id;
    }

    public void receiveOnBattleStart(AbstractRoom abstractRoom)
    {
        EventHelper.receiveOnBattleStart(abstractRoom);
    }

    private static ArrayList<String> bossList;

    public void OriginalModRelicRemove()
    {


        MizukiModCore.logger.info("原版模式移出遗物");
    }

    @Override
    public void receiveStartGame()
    {
        if (!Config.RelicAppear.Get() && !(AbstractDungeon.player instanceof Mizuki))
            OriginalModRelicRemove();
    }

    @Override
    public void receiveStartAct()
    {
        if (!Config.RelicAppear.Get() && !(AbstractDungeon.player instanceof Mizuki))
            OriginalModRelicRemove();
        if (AbstractDungeon.player instanceof Mizuki && !MizukiModCore.originalMod)
        {
            if (AbstractDungeon.actNum <= 3)
            {
                Method setBoss = null;
                if (bossList == null)
                {
                    bossList = new ArrayList<>();
                }
                try
                {
                    if (CardCrawlGame.dungeon instanceof com.megacrit.cardcrawl.dungeons.Exordium)
                    {
                        bossList.clear();
                        bossList.add(SaintCarmen.ID);
                        bossList.add(TidelinkedBishop.ID);
                        bossList.add(Pathshaper.ID);
                        Collections.shuffle(bossList, new Random(AbstractDungeon.monsterRng.randomLong()));
                        AbstractDungeon.bossKey = bossList.get(0);
                        MizukiModCore.logger.info("setboss");
                        setBoss = AbstractDungeon.class.getDeclaredMethod("setBoss", String.class);
                        setBoss.setAccessible(true);
                        setBoss.invoke(CardCrawlGame.dungeon, AbstractDungeon.bossKey);
                    }
                    if (CardCrawlGame.dungeon instanceof com.megacrit.cardcrawl.dungeons.TheCity)
                    {

                        bossList.clear();

                        bossList.add(TheEndspeaker.ID);
                        bossList.add(SalVientoBishopQuintus.ID);
                        /*bossList.add(.ID);*/

                        Collections.shuffle(bossList, new Random(AbstractDungeon.monsterRng.randomLong()));
                        AbstractDungeon.bossKey = bossList.get(0);
                        MizukiModCore.logger.info("setboss");
                        setBoss = AbstractDungeon.class.getDeclaredMethod("setBoss", String.class);
                        setBoss.setAccessible(true);
                        setBoss.invoke(CardCrawlGame.dungeon, AbstractDungeon.bossKey);

                    }
                    if (CardCrawlGame.dungeon instanceof com.megacrit.cardcrawl.dungeons.TheBeyond)
                    {

                        bossList.clear();
                        bossList.add(ParanoiaIllusion.ID);
                        bossList.add(ParanoiaIllusion.ID);
                        bossList.add(ParanoiaIllusion.ID);
                        /*
                        if ()
                        {
                            bossList.add(.ID);
                        }
                        if ()
                        {
                            bossList.add(.ID);
                        }*/
                        Collections.shuffle(bossList, new Random(AbstractDungeon.monsterRng.randomLong()));
                        AbstractDungeon.bossKey = bossList.get(0);
                        MizukiModCore.logger.info("setboss");
                        setBoss = AbstractDungeon.class.getDeclaredMethod("setBoss", String.class);
                        setBoss.setAccessible(true);
                        setBoss.invoke(CardCrawlGame.dungeon, AbstractDungeon.bossKey);

                    }
                    if (CardCrawlGame.dungeon instanceof com.megacrit.cardcrawl.dungeons.TheEnding)
                    {
                        /*
                        bossList.clear();
                        if ()
                        {
                            bossList.add(.ID);
                        }
                        Collections.shuffle(bossList, new Random(AbstractDungeon.monsterRng.randomLong()));
                        AbstractDungeon.bossKey = bossList.get(0);
                        MizukiModCore.logger.info("setboss");
                        setBoss = AbstractDungeon.class.getDeclaredMethod("setBoss", String.class);
                        setBoss.setAccessible(true);
                        setBoss.invoke(CardCrawlGame.dungeon, AbstractDungeon.bossKey);
                        */
                    }
                }
                catch (NoSuchMethodException|IllegalAccessException|java.lang.reflect.InvocationTargetException e)
                {
                    e.printStackTrace();
                }
            }
        }
        if (AbstractDungeon.player instanceof Mizuki && MizukiModCore.originalMod)
        {
            if (AbstractDungeon.actNum <= 3)
            {
                Method setBoss = null;
                if (bossList == null)
                {
                    bossList = new ArrayList<>();
                }
                try
                {
                    if (CardCrawlGame.dungeon instanceof com.megacrit.cardcrawl.dungeons.Exordium)
                    {
                        bossList.remove(SaintCarmen.ID);
                        bossList.remove(TidelinkedBishop.ID);
                        bossList.remove(Pathshaper.ID);
                    }
                    if (CardCrawlGame.dungeon instanceof com.megacrit.cardcrawl.dungeons.TheCity)
                    {
                        bossList.remove(TheEndspeaker.ID);
                        bossList.remove(SalVientoBishopQuintus.ID);
                    }
                    if (CardCrawlGame.dungeon instanceof com.megacrit.cardcrawl.dungeons.TheBeyond)
                    {
                        bossList.remove(ParanoiaIllusion.ID);
                    }
                    Collections.shuffle(bossList, new Random(AbstractDungeon.monsterRng.randomLong()));
                    setBoss = AbstractDungeon.class.getDeclaredMethod("setBoss", String.class);
                    setBoss.setAccessible(true);
                    setBoss.invoke(CardCrawlGame.dungeon, AbstractDungeon.bossKey);
                }
                catch (NoSuchMethodException|IllegalAccessException|java.lang.reflect.InvocationTargetException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    /*
    public void onLoad(Boolean arg0)
    {
        originalMod = arg0.booleanValue();
    }

    public Boolean onSave()
    {
        return Boolean.valueOf(originalMod);
    }*/
}