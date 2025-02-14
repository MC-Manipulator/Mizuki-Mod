package relics;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import helper.EventHelper;
import modcore.MizukiModCore;
import powers.ArousalPower;

public class FlashingSwords extends AbstractMizukiRelic
{
    //刀光剑影
    public static final String ID = MizukiModCore.MakePath(FlashingSwords.class.getSimpleName());

    private static final int requirement = 2;

    public FlashingSwords()
    {
        super(ID, RelicTier.UNCOMMON, AbstractRelic.LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0] + requirement + DESCRIPTIONS[1];
    }

    private int CheckCards()
    {
        AbstractPlayer p = AbstractDungeon.player;
        int curseAmount = 0;

        for (AbstractCard c2 : p.masterDeck.group)
        {
            if (c2.type == AbstractCard.CardType.CURSE)
            {
                curseAmount++;
            }
        }

        return curseAmount / requirement;
    }

    public void atTurnStart()
    {
        flash();
    }

    @Override
    public void onMasterDeckChange()
    {
        super.onMasterDeckChange();
        AbstractDungeon.player.gameHandSize -= this.counter;
        AbstractDungeon.player.masterHandSize -= this.counter;
        this.counter = CheckCards();
        AbstractDungeon.player.masterHandSize += this.counter;
        AbstractDungeon.player.gameHandSize += this.counter;
    }

    @Override
    public void onEquip()
    {
        super.onEquip();
        this.counter = CheckCards();
        AbstractDungeon.player.masterHandSize += this.counter;
        AbstractDungeon.player.gameHandSize += this.counter;
        AbstractCard card = EventHelper.Inst.getRandomRejection();
        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect((AbstractCard) card, (Settings.WIDTH / 2.0F), (Settings.HEIGHT / 2.0F)));
    }
}
