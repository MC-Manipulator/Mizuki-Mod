package characters;
import basemod.helpers.RelicType;
import cards.Attacks.*;
import cards.Curses.HemopoieticInhibition;
import cards.Curses.MetastaticAberration;
import cards.Curses.Neurodegeneration;
import cards.Ingredients.CrabLegs;
import cards.Ingredients.Paddy;
import cards.Powers.*;
import cards.Skills.*;
import com.megacrit.cardcrawl.cutscenes.CutscenePanel;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import modcore.MizukiModCore;
import relics.Gameplayer;
import basemod.abstracts.CustomPlayer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.events.city.Vampires;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import relics.MizukisDice;
import ui.MizukiSkinSelectScreen;

import java.util.ArrayList;
import java.util.List;


public class Mizuki extends CustomPlayer
{
    private static final String CHARACTER_ATLAS = "resources/img/char/char_437_mizuki.atlas";
    private static final String CHARACTER_JSON = "resources/img/char/char_437_mizuki.json";
    private static final String MY_CHARACTER_SHOULDER = "resources/img/char/shoulder.png";
    public static boolean hasPlayedVoice = false;
    private static final float[] LAYER_SPEED = new float[] { -40.0F, -32.0F, 20.0F, -20.0F, 0.0F, -10.0F, -8.0F, 5.0F, -5.0F, 0.0F };
    private static final String[] ORB_TEXTURES = new String[] { "resources/img/UI/orb/layer5.png", "resources/img/UI/orb/layer4.png", "resources/img/UI/orb/layer3.png", "resources/img/UI/orb/layer2.png", "resources/img/UI/orb/layer1.png", "resources/img/UI/orb/layer6.png", "resources/img/UI/orb/layer5d.png", "resources/img/UI/orb/layer4d.png", "resources/img/UI/orb/layer3d.png", "resources/img/UI/orb/layer2d.png", "resources/img/UI/orb/layer1d.png" };
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack
     .getCharacterString("Mizuki_Mizuki");

    public Mizuki(String name)
    {
        super(name, Enums.Mizuki, ORB_TEXTURES, "resources/img/UI/orb/vfx.png", LAYER_SPEED, null, null);
        this.dialogX = this.drawX + 0.0F * Settings.scale;
        this.dialogY = this.drawY + 150.0F * Settings.scale;
        initializeClass(null, "resources/img/char/shoulder.png", "resources/img/char/shoulder.png", null,
            getLoadout(), 0.0F, 0.0F, 200.0F, 220.0F, new EnergyManager(3));

        loadAnimation("resources/img/char/char_437_mizuki.atlas", "resources/img/char/char_437_mizuki.json", 1.5F);
        this.stateData.setMix("Idle", "Die", 0.1F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        e.setTimeScale(1.5F);
        refreshSkin();
    }

    public static class Enums
    {
        @SpireEnum
        public static PlayerClass Mizuki;
        @SpireEnum(name = "MIZUKI_BlUE")
        public static AbstractCard.CardColor MIZUKI_CARD;
        @SpireEnum(name = "MIZUKI_BlUE")
        public static CardLibrary.LibraryType MIZUKI_LIBRARY;
        @SpireEnum
        public static AbstractCard.CardTags INGREDIENT_CARD;
        public static AbstractCard.CardTags FOOD_CARD;

        @SpireEnum
        public static AbstractCard.CardTags LEARNING_CARD;

        @SpireEnum
        public static RelicType MIZUKI_RELIC;
    }

    public ArrayList<String> getStartingDeck()
    {
        ArrayList<String> retVal = new ArrayList<>();
        /*
        retVal.add(DissociativeIdentityDisorder.ID);
        retVal.add(CramUp.ID);
        */

        if (Settings.isDebug)
        {
            /*
            retVal.add(Arousal.ID);
            retVal.add(Screwdriver.ID);
            retVal.add(AbsurdFate.ID);
            retVal.add(PsychedelicMelody.ID);
            retVal.add(TargetedEvolution.ID);
            retVal.add(FleshUnderTheKnife.ID);
            retVal.add(Countertransference.ID);
            retVal.add(PreciousDaily.ID);
            retVal.add(ResidualLight.ID);*/


            retVal.add(PainAdaptation.ID);
            retVal.add(AbsurdFate.ID);
            retVal.add(AbsurdFate.ID);
            retVal.add(AbsurdFate.ID);
            retVal.add(AbsurdFate.ID);
        }
        else
        {
            retVal.add(Strike_Mizuki.ID);
            retVal.add(Strike_Mizuki.ID);
            retVal.add(Strike_Mizuki.ID);
            retVal.add(Strike_Mizuki.ID);
            retVal.add(Strike_Mizuki.ID);
            retVal.add(Defend_Mizuki.ID);
            retVal.add(Defend_Mizuki.ID);
            retVal.add(Defend_Mizuki.ID);
            retVal.add(Defend_Mizuki.ID);
            retVal.add(Arousal.ID);
            retVal.add(PrisonersDilemma.ID);
        }
        return retVal;
    }

    public void playDeathAnimation()
    {
        this.state.setAnimation(0, "Die", false);
    }

    @Override
    public void onVictory()
    {
        super.onVictory();

        //战斗胜利时，随机播放水月的语音，每当播放过以后，会有一次冷却
        if (AbstractDungeon.getCurrRoom() instanceof com.megacrit.cardcrawl.rooms.MonsterRoomBoss)
        {
            CardCrawlGame.sound.play("MIZUKI_ENDBOSSBATTLE", 0.00F);
            return;
        }
        if (hasPlayedVoice)
        {
            hasPlayedVoice = false;
        }
        else
        {
            int roll = AbstractDungeon.aiRng.random(99);
            if (roll < 30)
            {
                CardCrawlGame.sound.play("MIZUKI_ENDBATTLE1", 0.00F);
                hasPlayedVoice = true;
            }
            else if (roll < 60)
            {
                CardCrawlGame.sound.play("MIZUKI_ENDBATTLE2", 0.00F);
                hasPlayedVoice = true;
            }
        }
    }

    public void useFastAttackAnimation()
    {
        CardCrawlGame.sound.play("MIZUKI_ATTACK", 0.05F);
        this.state.setAnimation(0, "Attack", false);
        this.state.addAnimation(0, "Idle", true, 0.0F);
    }

    public void useSlowAttackAnimation()
    {
        CardCrawlGame.sound.play("MIZUKI_SKILL3", 0.05F);
        this.state.setAnimation(0, "Skill_1", false);
        this.state.addAnimation(0, "Idle", true, 0.0F);
    }


    public ArrayList<String> getStartingRelics()
    {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(MizukisDice.ID);
        return retVal;
    }

    public CharSelectInfo getLoadout()
    {

        return new CharSelectInfo(characterStrings.NAMES[0], characterStrings.TEXT[0], 75, 75, 0, 99, 5, (AbstractPlayer)this,
                    getStartingRelics(), getStartingDeck(), false);
    }


    public String getTitle(PlayerClass playerClass)
    {
        return characterStrings.NAMES[0];
    }


    public AbstractCard.CardColor getCardColor()
    {
        return Enums.MIZUKI_CARD;
    }


    public AbstractCard getStartCardForEvent()
    {
        return (AbstractCard)new Strike_Mizuki();
    }


    public Color getCardTrailColor()
    {
        return MizukiModCore.BLUE;
    }


    public int getAscensionMaxHPLoss()
    {
        return 5;
    }


    public BitmapFont getEnergyNumFont()
    {
        return FontHelper.energyNumFontBlue;
    }

    public void doCharSelectScreenSelectEffect()
    {

        CardCrawlGame.sound.playV("MIZUKI_CHOOSE", 0.8F);

        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT, false);
    }


    public String getCustomModeCharacterButtonSoundKey()
    {
        return "ATTACK_HEAVY";
    }

    public String getLocalizedCharacterName()
    {
        return characterStrings.NAMES[0];
    }


    public AbstractPlayer newInstance()
    {
        return (AbstractPlayer)new Mizuki(this.name);
    }


    public String getSpireHeartText()
    {
        return characterStrings.TEXT[1];
    }


    public Color getSlashAttackColor()
    {
        return MizukiModCore.BLUE;
    }


    public String getVampireText()
    {
        return Vampires.DESCRIPTIONS[1];
    }


    public Color getCardRenderColor()
    {
        return MizukiModCore.BLUE;
    }

    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect()
    {
        return new AbstractGameAction.AttackEffect[] { AbstractGameAction.AttackEffect.SLASH_HEAVY};
    }

    public void refreshSkin()
    {
        MizukiSkinSelectScreen.Skin skin = MizukiSkinSelectScreen.getSkin();
        loadAnimation(skin.charPath + ".atlas", skin.charPath + ".json", 1.5F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        e.setTimeScale(1.2F);
    }

    @Override
    public List<CutscenePanel> getCutscenePanels()
    {
        List<CutscenePanel> panels = new ArrayList<>();
        panels.add(new CutscenePanel("resources/img/char/MizukiVic1.png"));
        panels.add(new CutscenePanel("resources/img/char/MizukiVic2.png"));
        panels.add(new CutscenePanel("resources/img/char/MizukiVic3.png"));
        return panels;
    }
}
