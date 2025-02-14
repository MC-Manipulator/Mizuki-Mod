package cards.Skills;

import basemod.interfaces.OnPlayerTurnStartSubscriber;
import cards.AbstractMizukiCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import helper.EventHelper;
import modcore.MizukiModCore;

public class Metabolism extends AbstractMizukiCard implements EventHelper.OnHealthChangedSubscriber, OnPlayerTurnStartSubscriber
{
    public static final String ID = MizukiModCore.MakePath(Metabolism.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public int changedHealth = 0;

    public Metabolism()
    {
        super(ID, false, cardStrings, 1, AbstractCard.CardType.SKILL, AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.SELF);
        setupMagicNumber(0, 0, 0, 0);
        setupBlock(0);
        if (AbstractDungeon.player != null && AbstractDungeon.isPlayerInDungeon())
        {
            applyPowers();
        }
    }

    public void use(AbstractPlayer p, AbstractMonster m)
    {
        gainBlock(this.magicNumber2);
        if (this.upgraded)
        {
            gainBlock();
        }
    }

    @Override
    public void receiveOnPlayerTurnStart()
    {
        changedHealth = 0;
        applyPowers();
    }

    public void OnHealthChanged(int delta)
    {
        if (delta < 0)
        {
            changedHealth -= delta;
        }
        else
        {
            changedHealth += delta;
        }
        applyPowers();
    }

    @Override
    public void applyPowers()
    {
        baseMagicNumber2 = EventHelper.Inst.changedhealthinturn;
        magicNumber2 = baseMagicNumber2;
        super.applyPowers();
        initializeDescription();
    }

    public void upgrade()
    {
        if (!this.upgraded)
        {
            upgradeName();
            setupBlock(6);
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    public AbstractCard makeCopy()
    {
        return new Metabolism();
    }
}
