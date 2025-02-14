package relics;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import modcore.MizukiModCore;
import patches.WavebreakerMod;

public class OldFan extends AbstractMizukiRelic
{
    //老蒲扇
    public static final String ID = MizukiModCore.MakePath(OldFan.class.getSimpleName());

    public OldFan()
    {
        super(ID, RelicTier.RARE, LandingSound.FLAT);
        PowerTip p = new PowerTip();
        p.header = DESCRIPTIONS[1];
        p.body = DESCRIPTIONS[2];
        tips.add(p);
        //消耗、保留、虚无、固有
    }

    private int CheckCards()
    {
        AbstractPlayer p = AbstractDungeon.player;
        boolean hasExhaust = false;
        boolean hasInnate = false;
        boolean hasEthereal = false;
        boolean hasSelfRetain = false;
        int strengthAmount = 0;

        for (AbstractCard c2 : p.masterDeck.group)
        {
            if (c2.exhaust && !hasExhaust)
            {
                hasExhaust = true;
                strengthAmount++;
            }
            if (c2.isInnate && !hasInnate)
            {
                hasInnate = true;
                strengthAmount++;
            }
            if (c2.isEthereal && !hasEthereal)
            {
                hasEthereal = true;
                strengthAmount++;
            }
            if (c2.selfRetain && !hasSelfRetain)
            {
                hasSelfRetain = true;
                strengthAmount++;
            }
        }

        return strengthAmount;
    }

    @Override
    public void onMasterDeckChange()
    {
        super.onMasterDeckChange();
        this.counter = CheckCards();
    }

    @Override
    public void atBattleStart()
    {
        int strengthAmount = CheckCards();
        if (strengthAmount != 0)
        {
            flash();
            addToBot((AbstractGameAction) new RelicAboveCreatureAction((AbstractCreature) AbstractDungeon.player, this));
            addToBot((AbstractGameAction) new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                    new StrengthPower(AbstractDungeon.player, strengthAmount),
                    strengthAmount));
        }
    }

    @Override
    public void onEquip()
    {
        this.counter = CheckCards();
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }
}
