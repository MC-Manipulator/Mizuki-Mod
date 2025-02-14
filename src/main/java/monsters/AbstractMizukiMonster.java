package monsters;

import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.lang.reflect.Field;

public class AbstractMizukiMonster extends AbstractMonster
{
    public AbstractMizukiMonster(String name, String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl, float offsetX, float offsetY)
    {
        super(name, id, maxHealth, hb_x, hb_y, hb_w, hb_h, imgUrl, offsetX, offsetY);
    }

    public void takeTurn()
    {

    }

    protected void getMove(int i)
    {

    }
}
