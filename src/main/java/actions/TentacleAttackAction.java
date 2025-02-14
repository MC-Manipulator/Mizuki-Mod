package actions;

import basemod.helpers.VfxBuilder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import java.util.ArrayList;

public class TentacleAttackAction extends AbstractGameAction
{

    private AbstractMonster m = null;

    private static ArrayList<Texture> tentacle = new ArrayList<>();

    public TentacleAttackAction(AbstractMonster m)
    {
        if (tentacle.isEmpty())
        {
            for (int i = 1;i <= 15;i++ )
            {
                tentacle.add(ImageMaster.loadImage("resources/img/vfx/tentacle/tentacle_" + i + ".png"));
            }
        }
        this.m = m;
    }

    public int j = 0;
    public void tentacleUpdate()
    {
        AbstractGameEffect tentacleEffect = new VfxBuilder(tentacle.get(j), m.hb.cX, m.hb.cY, 0.04f)
                .setScale(2.5f)
                .whenComplete((VfxBuilder vb) ->
                {
                    if (j < 15)
                        this.tentacleUpdate();
                })
                .build();
        AbstractDungeon.effectsQueue.add(tentacleEffect);
        if (j < 15)
            j++;
    }

    @Override
    public void update()
    {
        tentacleUpdate();
        isDone = true;
    }
}
