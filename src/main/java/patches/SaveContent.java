package patches;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.ArrayList;
import java.util.UUID;

public class SaveContent
{
    public UUID uuid;
    public ArrayList<Class<?>> storePower;
    public ArrayList<AbstractCard> storeCard;
    public int baseMagicNumber2 = 0;
    public int magicNumber2 = 0;
    public int baseMagicNumber3 = 0;
    public int magicNumber3 = 0;
}
