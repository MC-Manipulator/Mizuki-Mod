package cards.Attacks;

import Impairment.NervousImpairment;
import actions.AdaptingStrikeAction;
import actions.ApplyImpairmentAction;
import cards.AbstractMizukiCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import modcore.MizukiModCore;

public class MindTentacle extends AbstractMizukiCard
{
    public static final String ID = MizukiModCore.MakePath(MindTentacle.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public MindTentacle()
    {
        super(ID, false, cardStrings, 1, AbstractCard.CardType.ATTACK, CardRarity.COMMON, AbstractCard.CardTarget.ENEMY);
        setupDamage(4);
        tags.add(CardTags.HEALING);
        this.baseMagicNumber = 1;
        this.magicNumber = this.baseMagicNumber;
    }

    public void use(AbstractPlayer p, AbstractMonster m)
    {
        damageToEnemy(m, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
        addToBot((AbstractGameAction) new ApplyImpairmentAction(new NervousImpairment(), m, magicNumber));
    }

    public void upgrade()
    {
        if (!this.upgraded)
        {
            upgradeName();
            upgradeMagicNumber(1);
        }
    }

}
