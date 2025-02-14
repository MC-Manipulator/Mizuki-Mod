package cards.Powers;

import basemod.interfaces.OnStartBattleSubscriber;
import cards.AbstractMizukiCard;
import cards.Attacks.EnhancedTentacle;
import cards.Skills.AbsurdFate;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import helper.EventHelper;
import helper.LearnManager;
import modcore.MizukiModCore;
import powers.BreathOfTheTidePower;
import powers.CountertransferencePower;
import powers.EnvironmentAnalyzingPower;
import powers.PacFishPower;

public class BreathOfTheTide extends AbstractMizukiCard implements EventHelper.OnPostBattleStartSubscriber
{
    public static final String ID = MizukiModCore.MakePath(BreathOfTheTide.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public BreathOfTheTide()
    {
        super(ID, false, cardStrings, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        setupMagicNumber(3, 0, 0, 0);
        this.isInnate = true;
        this.isEthereal = true;
        this.cardsToPreview = (AbstractCard)new AbsurdFate();
    }

    public void use(AbstractPlayer p, AbstractMonster m)
    {
        MizukiModCore.logger.info(new PlatedArmorPower(AbstractDungeon.player, this.magicNumber).ID);
        addToBot((AbstractGameAction)new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                (AbstractPower)new PlatedArmorPower(AbstractDungeon.player, this.magicNumber), this.magicNumber));
    }

    @Override
    public void OnPostBattleStart(AbstractRoom room)
    {
        if (LearnManager.ifInMasterDeck(this) && !AbstractDungeon.player.hasPower(BreathOfTheTidePower.id))
        {
            addToBot((AbstractGameAction)new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                    (AbstractPower)new BreathOfTheTidePower(this, AbstractDungeon.player, 1), 0));
        }
    }

    public void upgrade()
    {
        if (!this.upgraded)
        {
            upgradeMagicNumber(1);
            upgradeBaseCost(0);
            upgradeName();
        }
    }
}
