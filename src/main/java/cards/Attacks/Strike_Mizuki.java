package cards.Attacks;

import cards.AbstractMizukiCard;
import com.megacrit.cardcrawl.core.Settings;
import helper.ImpairementManager;
import modcore.MizukiModCore;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Strike_Mizuki extends AbstractMizukiCard
{
    public static final String ID = MizukiModCore.MakePath(Strike_Mizuki.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public Strike_Mizuki()
    {
        super(ID, false, cardStrings, 1, CardType.ATTACK, CardRarity.BASIC, CardTarget.ENEMY);
        setupDamage(6);
        this.tags.add(CardTags.STARTER_STRIKE);
        this.tags.add(CardTags.STRIKE);
    }

    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (Settings.isDebug)
        {
            ImpairementManager.increaseNervousImpairment(p, 1);
        }
        damageToEnemy(m, AbstractGameAction.AttackEffect.LIGHTNING);
    }

    public void upgrade()
    {
        if (!this.upgraded)
        {
            upgradeName();
            upgradeDamage(3);
        }
    }
}
