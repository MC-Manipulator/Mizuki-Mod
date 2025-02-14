package relics;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import modcore.MizukiModCore;

public class ChitinousRipper extends AbstractMizukiRelic
{
    public static final String ID = MizukiModCore.MakePath(ChitinousRipper.class.getSimpleName());
    public ChitinousRipper()
    {
        super(ID, AbstractRelic.RelicTier.SPECIAL, AbstractRelic.LandingSound.FLAT);
    }

    @Override
    public void atBattleStartPreDraw()
    {
        addToTop((AbstractGameAction)new RelicAboveCreatureAction((AbstractCreature)AbstractDungeon.player, this));
        for (AbstractPower p : AbstractDungeon.player.powers)
        {
            p.amount *= 2;
            p.flash();
        }
    }
}