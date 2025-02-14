package patches;

import basemod.abstracts.CustomSavable;
import cards.AbstractMizukiCard;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class PowerSaver implements CustomSavable<ArrayList<ArrayList<Class<?>>>>
{
    @Override
    public ArrayList<ArrayList<Class<?>>> onSave()
    {
        ArrayList<ArrayList<Class<?>>> temp = new ArrayList<>();
        for (AbstractCard card: AbstractDungeon.player.masterDeck.group)
        {
            ArrayList<Class<?>> temp2 = new ArrayList<>();
            if (card instanceof AbstractMizukiCard)
            {
                AbstractMizukiCard c = (AbstractMizukiCard) card;
                temp2.addAll(c.storePower);
            }
            temp.add(temp2);
        }
        return temp;

    }

    @Override
    public void onLoad(ArrayList<ArrayList<Class<?>>> load)
    {
        for (int i = 0;i < AbstractDungeon.player.masterDeck.group.size();i++)
        {
            if (AbstractDungeon.player.masterDeck.group.get(i) instanceof AbstractMizukiCard)
            {
                AbstractMizukiCard c = (AbstractMizukiCard) AbstractDungeon.player.masterDeck.group.get(i);
                c.storePower.addAll(load.get(i));
            }
        }
    }

    @Override
    public Type savedType()
    {
        return new TypeToken<ArrayList<ArrayList<Class<?>>>>(){}.getType();
    }
}
