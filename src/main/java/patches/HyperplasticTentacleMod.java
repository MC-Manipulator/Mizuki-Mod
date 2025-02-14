package patches;

import basemod.abstracts.AbstractCardModifier;
import cards.AbstractMizukiCard;
import cards.Attacks.HyperplasticTentacle;
import cards.Powers.EchoOfAssimilation;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import powers.TowerOfTheWitchKingPower;

public class HyperplasticTentacleMod extends AbstractCardModifier
{
    private boolean upgraded = false;

    public HyperplasticTentacleMod(boolean upgraded)
    {
        this.upgraded = upgraded;
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action)
    {
        AbstractMizukiCard c = new HyperplasticTentacle();
        if (upgraded)
        {
            c.upgrade();
            AbstractDungeon.actionManager.addToBottom((AbstractGameAction) new MakeTempCardInDrawPileAction(c, 1, true, true));
        }
        else
        {
            AbstractDungeon.actionManager.addToBottom((AbstractGameAction) new MakeTempCardInDrawPileAction(c, 1, true, true));
        }
    }

    public AbstractCardModifier makeCopy()
    {
        return new HyperplasticTentacleMod(upgraded);
    }
}
