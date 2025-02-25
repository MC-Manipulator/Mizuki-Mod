package ui;

import basemod.BaseMod;
import basemod.abstracts.CustomSavable;
import basemod.abstracts.CustomSavableRaw;
import basemod.interfaces.ISubscriber;
import basemod.interfaces.PostInitializeSubscriber;
import characters.Mizuki;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.esotericsoftware.spine.*;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import modcore.MizukiModCore;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Properties;

public class MizukiSkinSelectScreen implements ISubscriber
{
    private static final String[] TEXT;

    private static final ArrayList<Skin> SKINS = new ArrayList<>();

    public static MizukiSkinSelectScreen Inst;

    public Hitbox leftHb;

    public Hitbox rightHb;

    public TextureAtlas atlas;

    public Skeleton skeleton;

    public AnimationStateData stateData;

    public AnimationState state;

    public String curName = "";

    public String nextName = "";

    public int index;

    public static Properties skinSelectionDefaults = new Properties();

    static
    {
        TEXT = (CardCrawlGame.languagePack.getUIString(MizukiModCore.MakePath(MizukiSkinSelectScreen.class.getSimpleName()))).TEXT;
        SKINS.add(new Skin(0, ""));
        SKINS.add(new Skin(1, "_sale_7"));
        Inst = new MizukiSkinSelectScreen();
    }

    public static Skin getSkin()
    {
        return SKINS.get(Inst.index);
    }

    public MizukiSkinSelectScreen()
    {
        this.index = 0;
        refresh();
        this.leftHb = new Hitbox(70.0F * Settings.scale, 70.0F * Settings.scale);
        this.rightHb = new Hitbox(70.0F * Settings.scale, 70.0F * Settings.scale);
        BaseMod.subscribe((ISubscriber)this);
        //BaseMod.addSaveField(MizukiModCore.MakePath("skin"), (CustomSavableRaw) this);
    }

    private void Save()
    {
        try
        {
            MizukiModCore.logger.info("保存皮肤选项:" + this.index);
            SpireConfig config = new SpireConfig("Mizuki", "Mizuki-SkinSelection", skinSelectionDefaults);
            config.setInt("Mizuki-SkinNum", this.index);
            config.save();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void Load()
    {
        try
        {
            SpireConfig config = new SpireConfig("Mizuki", "Mizuki-SkinSelection", skinSelectionDefaults);
            config.load();
            this.index = config.getInt("Mizuki-SkinNum");
            MizukiModCore.logger.info("读取皮肤选项:" + this.index);
            refresh();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void loadAnimation(String atlasUrl, String skeletonUrl, float scale)
    {
        this.atlas = new TextureAtlas(Gdx.files.internal(atlasUrl));
        SkeletonJson json = new SkeletonJson(this.atlas);
        json.setScale(Settings.renderScale / scale);
        SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal(skeletonUrl));
        this.skeleton = new Skeleton(skeletonData);
        this.skeleton.setColor(Color.WHITE);
        this.stateData = new AnimationStateData(skeletonData);
        this.state = new AnimationState(this.stateData);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        e.setTimeScale(1.2F);
    }

    public void refresh()
    {
        Skin skin = SKINS.get(this.index);
        this.curName = skin.name;
        loadAnimation(skin.charPath + ".atlas", skin.charPath + ".json", 1.5F);
        this.nextName = ((Skin)SKINS.get(nextIndex())).name;
        if (AbstractDungeon.player instanceof Mizuki)
        {
            Mizuki m = (Mizuki)AbstractDungeon.player;
            m.refreshSkin();
        }
    }

    public int prevIndex()
    {
        return (this.index - 1 < 0) ? (SKINS.size() - 1) : (this.index - 1);
    }

    public int nextIndex()
    {
        return (this.index + 1 > SKINS.size() - 1) ? 0 : (this.index + 1);
    }

    public void update()
    {
        float centerX = Settings.WIDTH * 0.8F;
        float centerY = Settings.HEIGHT * 0.5F;
        this.leftHb.move(centerX - 200.0F * Settings.scale, centerY);
        this.rightHb.move(centerX + 200.0F * Settings.scale, centerY);
        updateInput();
    }

    private void updateInput()
    {
        if (CardCrawlGame.chosenCharacter == Mizuki.Enums.Mizuki)
        {
            this.leftHb.update();
            this.rightHb.update();
            if (this.leftHb.clicked)
            {
                this.leftHb.clicked = false;
                CardCrawlGame.sound.play("UI_CLICK_1");
                this.index = prevIndex();
                Save();
                refresh();
            }
            if (this.rightHb.clicked)
            {
                this.rightHb.clicked = false;
                CardCrawlGame.sound.play("UI_CLICK_1");
                this.index = nextIndex();
                Save();
                refresh();
            }
            if (InputHelper.justClickedLeft)
            {
                if (this.leftHb.hovered)
                    this.leftHb.clickStarted = true;
                if (this.rightHb.hovered)
                    this.rightHb.clickStarted = true;
            }
        }
    }

    public void render(SpriteBatch sb)
    {
        float centerX = Settings.WIDTH * 0.8F;
        float centerY = Settings.HEIGHT * 0.5F;
        renderSkin(sb, centerX, centerY);
        FontHelper.renderFontCentered(sb, FontHelper.cardTitleFont, TEXT[0], centerX, centerY + 250.0F * Settings.scale, Color.WHITE, 1.25F);
        Color color = Settings.GOLD_COLOR.cpy();
        color.a /= 2.0F;
        float dist = 100.0F * Settings.scale;
        FontHelper.renderFontCentered(sb, FontHelper.cardTitleFont, this.curName, centerX, centerY - 20F, Settings.GOLD_COLOR);
        //FontHelper.renderFontCentered(sb, FontHelper.cardTitleFont, this.nextName, centerX + dist * 1.5F, centerY - dist, color);
        if (this.leftHb.hovered)
        {
            sb.setColor(Color.LIGHT_GRAY);
        }
        else
        {
            sb.setColor(Color.WHITE);
        }
        sb.draw(ImageMaster.CF_LEFT_ARROW, this.leftHb.cX - 24.0F, this.leftHb.cY - 24.0F, 24.0F, 24.0F, 48.0F, 48.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 48, 48, false, false);
        if (this.rightHb.hovered)
        {
            sb.setColor(Color.LIGHT_GRAY);
        }
        else
        {
            sb.setColor(Color.WHITE);
        }
        sb.draw(ImageMaster.CF_RIGHT_ARROW, this.rightHb.cX - 24.0F, this.rightHb.cY - 24.0F, 24.0F, 24.0F, 48.0F, 48.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 48, 48, false, false);
        this.rightHb.render(sb);
        this.leftHb.render(sb);
    }

    public void renderSkin(SpriteBatch sb, float x, float y)
    {
        if (this.atlas == null)
            return;
        this.state.update(Gdx.graphics.getDeltaTime());
        this.state.apply(this.skeleton);
        this.skeleton.updateWorldTransform();
        this.skeleton.setPosition(x, y);
        sb.end();
        CardCrawlGame.psb.begin();
        AbstractCreature.sr.draw(CardCrawlGame.psb, this.skeleton);
        CardCrawlGame.psb.end();
        sb.begin();
    }

    public static class Skin
    {
        public String charPath;

        public String monsterPath;

        public String calcitePath;

        public String shoulder;

        public Color meltColor;

        public String name;

        public Skin(int index, String charPath)
        {
            this.charPath = "resources/img/char/char_437_mizuki" + charPath;
            this.shoulder = "resources/img/char/shoulder.png";
            this.name = MizukiSkinSelectScreen.TEXT[index + 1];
        }
    }
}
