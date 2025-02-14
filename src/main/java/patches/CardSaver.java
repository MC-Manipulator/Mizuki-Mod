package patches;

import basemod.abstracts.CustomSavable;
import cards.AbstractMizukiCard;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class CardSaver implements CustomSavable<ArrayList<ArrayList<String>>>
{
    @Override
    public ArrayList<ArrayList<String>> onSave()
    {
        ArrayList<ArrayList<String>> temp = new ArrayList<>();
        for (AbstractCard card: AbstractDungeon.player.masterDeck.group)
        {
            ArrayList<String> temp2 = new ArrayList<>();
            if (card instanceof AbstractMizukiCard)
            {
                AbstractMizukiCard c = (AbstractMizukiCard) card;
                for (int i = 0;i < c.storeCard.size();i++)
                {
                    temp2.add(c.storeCard.get(i).cardID);
                }
            }
            temp.add(temp2);
        }

        return temp;
    }

    @Override
    public void onLoad(ArrayList<ArrayList<String>> load)
    {
        for (int i = 0;i < AbstractDungeon.player.masterDeck.group.size();i++)
        {
            if (AbstractDungeon.player.masterDeck.group.get(i) instanceof AbstractMizukiCard)
            {
                AbstractMizukiCard c = (AbstractMizukiCard) AbstractDungeon.player.masterDeck.group.get(i);
                for (int j = 0;j < load.get(i).size();j++)
                {
                    c.storeCard.add(CardLibrary.getCard(load.get(i).get(j)));
                }
            }
        }
    }

    @Override
    public Type savedType()
    {
        return new TypeToken<ArrayList<ArrayList<?>>>(){}.getType();
    }
}
