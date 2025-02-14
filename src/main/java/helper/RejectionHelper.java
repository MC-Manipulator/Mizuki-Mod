package helper;

import cards.Curses.ConcentrationDisorder;
import cards.Curses.HemopoieticInhibition;
import cards.Curses.MetastaticAberration;
import cards.Curses.Neurodegeneration;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class RejectionHelper
{
    public static AbstractCard getRandomRejection()
    {
        int roll = AbstractDungeon.cardRandomRng.random(99);


        if (roll < 25)
        {
            return new HemopoieticInhibition();
        }
        else if (roll < 50)
        {
            return new MetastaticAberration();
        }
        else if (roll < 75)
        {
            return new Neurodegeneration();
        }
        else
        {
            return new ConcentrationDisorder();
        }
    }
}
