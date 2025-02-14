package vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import modcore.MizukiModCore;

import java.util.ArrayList;

public class DiceEffect extends AbstractGameEffect
{
    private float x;

    private float y;

    private static Texture img;

    //ImageMaster.loadImage("resources/img/vfx/dice.png")
    private static ArrayList<Texture> imgList1;
    private static ArrayList<Texture> imgList2;
    private static ArrayList<Texture> imgList3;
    private static ArrayList<Texture> imgList4;
    private static ArrayList<Texture> imgList5;
    private static ArrayList<Texture> imgList6;

    private static ArrayList<ArrayList<Texture>> D6imgList;

    private static ArrayList<ArrayList<Texture>> D8imgList;

    private int picnum;

    private int dice;

    private boolean D6;

    private float timer = 0.1f;

    public DiceEffect(int dice, boolean D6)
    {
        this.D6 = D6;
        this.dice = dice;
        //MizukiModCore.logger.info(D6imgList.isEmpty());
        //MizukiModCore.logger.info(D6imgList.get(this.dice - 1).isEmpty());
        if (this.D6)
        {
            img = (D6imgList.get(this.dice - 1)).get(0);
        }
        else
        {
            img = D8imgList.get(this.dice - 1).get(0);
        }
        /*
        if (img == null)
            img = ImageMaster.loadImage("resources/img/vfx/dice/dice.png");
        */


        this.startingDuration = 2.0F;
        this.duration = this.startingDuration;
        this.scale = Settings.scale * 3.0F;
        this.x = Settings.WIDTH * 0.5F - img.getWidth() / 2.0F;
        this.y = Settings.HEIGHT * 0.5F - img.getHeight() / 2.0F;

        this.color = Color.WHITE.cpy();
        this.color.a = 0;
        this.picnum = 0;
    }

    public void update()
    {
        timer -= Gdx.graphics.getDeltaTime();

        if (timer <= 0f && picnum <= 20)
        {
            picnum++;
            timer = 0.1f;
        }

        if (D6)
        {
            img = D6imgList.get(dice - 1).get(picnum);
        }
        else
        {
            img = D8imgList.get(dice - 1).get(picnum);
        }

        this.duration -= Gdx.graphics.getDeltaTime();

        if (this.duration < 0.0F)
            this.isDone = true;

        if (this.duration < 2.0F && this.duration > 1.75F)
        {
            this.color.a = Interpolation.fade.apply(1.0F, 0.0F, this.duration - 1.75F);
        }

        if (this.duration < 1.0F)
        {
            this.color.a = Interpolation.fade.apply(0.0F, 1.0F, this.duration);
        }
    }

    public void render(SpriteBatch sb)
    {
        sb.setColor(this.color);
        sb.draw(img, this.x, this.y);
        //sb.draw(img, this.x, this.y, img.getWidth() / 2.0F, img.getHeight() / 2.0F, img.getWidth(), img.getHeight(), this.scale, this.scale, this.duration * 360.0F);
    }

    public static void getImg()
    {
        D6imgList = new ArrayList<>();

        for (int i = 1;i <= 6;i++)
        {
            ArrayList<Texture> temp = new ArrayList<>();
            for (int j = 0;j <= 120;j += 6)
            {
                //MizukiModCore.logger.info("D6_" + i + "_" + j);
                if (j < 10)
                {
                    temp.add(ImageMaster.loadImage("resources/img/vfx/Dice/D6/" + i + "/Dice" + i + "00" + j + ".png"));
                }
                else if (j < 100)
                {
                    temp.add(ImageMaster.loadImage("resources/img/vfx/Dice/D6/" + i + "/Dice" + i + "0" + j + ".png"));
                }
                else
                {
                    temp.add(ImageMaster.loadImage("resources/img/vfx/Dice/D6/" + i + "/Dice" + i + j + ".png"));
                }
            }
            D6imgList.add(temp);
        }
        //MizukiModCore.logger.info(D6imgList.isEmpty());

        D8imgList = new ArrayList<>();
        for (int i = 1;i <= 8;i++)
        {
            ArrayList<Texture> temp = new ArrayList<>();
            for (int j = 0;j <= 120;j += 6)
            {
                //MizukiModCore.logger.info("D8_" + i + "_" + j);
                if (j < 10)
                {
                    temp.add(ImageMaster.loadImage("resources/img/vfx/Dice/D8/" + i + "/Dice" + i + "00" + j + ".png"));
                }
                else if (j < 100)
                {
                    temp.add(ImageMaster.loadImage("resources/img/vfx/Dice/D8/" + i + "/Dice" + i + "0" + j + ".png"));
                }
                else
                {
                    temp.add(ImageMaster.loadImage("resources/img/vfx/Dice/D8/" + i + "/Dice" + i + j + ".png"));
                }
            }
            D8imgList.add(temp);
        }
    }

    public void dispose() {}
}