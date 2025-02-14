package powers;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import helper.EventHelper;
import java.util.ArrayList;



 public abstract class AbstractMizukiPower
   extends AbstractPower implements EventHelper.OnGetCardInHandSubscriber
 {
    public ArrayList<Integer> counters = new ArrayList<>();

    public AbstractMizukiPower(AbstractCreature owner, int amount, String id, String name)
    {
        this.ID = id;
        this.name = name;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        String sid = id.replace("Mizuki_", "");
        Texture img84 = ImageMaster.loadImage(String.format("resources/img/powers/%s84.png", new Object[] { sid }));
        Texture img32 = ImageMaster.loadImage(String.format("resources/img/powers/%s32.png", new Object[] { sid }));
        if (img84 != null || img32 != null)
        {
            this.region128 = new TextureAtlas.AtlasRegion(img84, 0, 0, 84, 84);
            this.region48 = new TextureAtlas.AtlasRegion(img32, 0, 0, 32, 32);
        }
        else
        {
            loadRegion("sadistic");
        }
        updateDescription();
    }

    public abstract String getDescription();

    public void updateDescription()
    {
        super.updateDescription();
        String des = getDescription();
        for (int i = 0; i < this.counters.size(); i++)
        {
            des = des.replace("!M" + i + "!", "#b" + this.counters.get(i));
        }
        des = des.replace("!M!", "#b" + this.amount);
        this.description = des;
    }


    public void onInitialApplication()
    {
        super.onInitialApplication();
        EventHelper.Subscribe((EventHelper.CustomSubscriber)this);
    }


    public void onRemove()
    {
        super.onRemove();
        EventHelper.Unsubscribe(this);
    }


    @Override
    public void OnGetCardInHand(AbstractCard card)
    {

    }
}
