package cards.Skills;

import actions.TowerOfTheWitchKingAction;
import basemod.abstracts.AbstractCardModifier;
import basemod.cardmods.ExhaustMod;
import basemod.cardmods.RetainMod;
import basemod.helpers.CardModifierManager;
import cards.AbstractMizukiCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.unique.ArmamentsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import helper.EventHelper;
import helper.LearnManager;
import modcore.MizukiModCore;
import powers.BreathOfTheTidePower;
import powers.TowerOfTheWitchKingPower;

import java.util.ArrayList;

public class TowerOfTheWitchKing extends AbstractMizukiCard implements EventHelper.OnPostBattleStartSubscriber
{
    public static final String ID = MizukiModCore.MakePath(TowerOfTheWitchKing.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public AbstractCard Storecard1;
    public AbstractCard Storecard2;

    public TowerOfTheWitchKing()
    {
        super(ID, false, cardStrings, 0, AbstractCard.CardType.SKILL, CardRarity.SPECIAL, AbstractCard.CardTarget.SELF);
        this.exhaust = true;
        this.selfRetain = true;
        setupMagicNumber(1, 10, 0, 0);
    }

    public void use(AbstractPlayer p, AbstractMonster m)
    {
        addToBot((AbstractGameAction)new TowerOfTheWitchKingAction(upgraded, this));
        /*
        if (upgraded)
        {
            addToBot((AbstractGameAction)new TowerOfTheWitchKingAction(upgraded, this));
        }*/
    }

    @Override
    public void OnPostBattleStart(AbstractRoom room)
    {
        if (LearnManager.ifInMasterDeck(this) && !AbstractDungeon.player.hasPower(TowerOfTheWitchKingPower.id))
        {

            addToBot((AbstractGameAction)new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                    (AbstractPower)new TowerOfTheWitchKingPower(this, AbstractDungeon.player, magicNumber2), 0));
        }
    }

    public void upgrade()
    {
        if (!this.upgraded)
        {
            upgradeName();
            upgradeMagicNumber2(10);
        }
    }
    public AbstractCard makeCopy()
    {
        return new TowerOfTheWitchKing();
    }
}
