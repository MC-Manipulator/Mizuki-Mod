package cards.Skills;

import cards.AbstractMizukiCard;
import com.megacrit.cardcrawl.core.Settings;
import helper.ImpairementManager;
import modcore.MizukiModCore;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Defend_Mizuki extends AbstractMizukiCard
{
    public static final String ID = MizukiModCore.MakePath(Defend_Mizuki.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public Defend_Mizuki()
    {
        super(ID, false, cardStrings, 1, CardType.SKILL, CardRarity.BASIC, CardTarget.SELF);
        setupBlock(5);
        this.tags.add(CardTags.STARTER_DEFEND);
    }

    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (Settings.isDebug)
        {
            ImpairementManager.increaseCorrosionImpairment(p, 1);
        }
        gainBlock();
    }

    public void upgrade()
    {
        if (!this.upgraded)
        {
            upgradeName();
            upgradeBlock(3);
        }
    }

    public AbstractCard makeCopy()
    {
        return new Defend_Mizuki();
    }
}