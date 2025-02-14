package patches;

import basemod.abstracts.AbstractCardModifier;
import cards.Powers.EchoOfAssimilation;
import cards.Skills.Assimilate;
import cards.Skills.TowerOfTheWitchKing;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;
import modcore.MizukiModCore;
import powers.TowerOfTheWitchKingPower;

public class AssimilationMod extends AbstractCardModifier
{
    EchoOfAssimilation EOA;
    public int targetamount = 0;
    public int requireamount = 0;
    public int processamount = 0;
    public AssimilationMod(EchoOfAssimilation card, int target)
    {
        this.targetamount = target;
        this.EOA = card;
        /*
        MizukiModCore.logger.info("INITIALIZE MOD");
        MizukiModCore.logger.info("target MOD" + targetamount);
        MizukiModCore.logger.info("require MOD" + requireamount);
        MizukiModCore.logger.info("process MOD" + processamount);

         */
    }

    public void onUpdate(AbstractCard card)
    {
        if (((EchoOfAssimilation)card).mod == null)
        {
            ((EchoOfAssimilation)card).mod = this;
            ((EchoOfAssimilation)card).targetamount = targetamount;
            ((EchoOfAssimilation)card).requireamount = requireamount;
            ((EchoOfAssimilation)card).processamount = processamount;
        }
    }

    public String modifyDescription(String rawDescription, AbstractCard card)
    {
        return this.EOA.strings.EXTENDED_DESCRIPTION[targetamount - 1];
    }

    public AbstractCardModifier makeCopy()
    {
        AssimilationMod mod = new AssimilationMod(EOA, targetamount);
        mod.targetamount = this.targetamount;
        mod.requireamount = this.requireamount;
        mod.processamount = this.processamount;
        return mod;
    }
}