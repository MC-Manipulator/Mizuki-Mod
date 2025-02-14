package actions;

import Impairment.AbstractImpairment;
import cards.Skills.Dice;
import characters.Mizuki;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import helper.DiceHelper;
import helper.ImpairementManager;
import modcore.MizukiModCore;
import patches.DicePatch;
import relics.MizukisDice;
import relics.NightsunGrass;

public class GainDiceAction extends AbstractGameAction
{
    AbstractPlayer player;
    private int count;
    public GainDiceAction(int count)
    {
        this.count = count;
        this.player = AbstractDungeon.player;
    }


    public void update()
    {
        //MizukisDice dice = (MizukisDice)player.getRelic(MizukisDice.ID);

        //DicePatch dice = Dice.getDice();
        DiceHelper.gainDice(count);

        MizukiModCore.logger.info("Gain Dice Action");

        isDone = true;
    }
}
