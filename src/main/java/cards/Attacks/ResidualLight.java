package cards.Attacks;

import Impairment.CorrosionImpairment;
import Impairment.NervousImpairment;
import actions.ApplyImpairmentAction;
import cards.AbstractMizukiCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import helper.ImpairementManager;
import modcore.MizukiModCore;

public class ResidualLight extends AbstractMizukiCard
{
    public static final String ID = MizukiModCore.MakePath(ResidualLight.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public ResidualLight()
    {
        super(ID, false, cardStrings, 1, AbstractCard.CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        setupDamage(6);
        setupMagicNumber(1, 1, 0, 0);
        this.exhaust = false;
    }

    public void use(AbstractPlayer p, AbstractMonster m)
    {
        damageToEnemy(m, AbstractGameAction.AttackEffect.FIRE);
        for (int i = 0;i < magicNumber;i++)
        {
            ImpairementManager.restoreCreatureImpairments((AbstractCreature)m);
        }
        if (ImpairementManager.getCreatureNervousImpairments(AbstractDungeon.player) != null)
        {
            addToBot(new ApplyImpairmentAction(new NervousImpairment(), p, -1));
            //ImpairementManager.decreaseNervousImpairment((AbstractCreature)p, 1);
        }
        if (ImpairementManager.getCreatureCorrosionImpairments(AbstractDungeon.player) != null)
        {
            addToBot(new ApplyImpairmentAction(new CorrosionImpairment(), p, -1));
            //ImpairementManager.decreaseCorrosionImpairment((AbstractCreature)p, 1);
        }
    }

    public void upgrade()
    {
        if (!this.upgraded)
        {
            upgradeName();
            upgradeDamage(2);
            upgradeMagicNumber(1);
        }
    }
}
