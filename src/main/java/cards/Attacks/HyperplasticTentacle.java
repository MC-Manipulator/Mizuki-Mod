package cards.Attacks;

import actions.HyperplasticTentacleAction;
import cards.Options.DexterityOption;
import com.megacrit.cardcrawl.cards.DamageInfo;
import cards.AbstractMizukiCard;
import modcore.MizukiModCore;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class HyperplasticTentacle extends AbstractMizukiCard
{
    public static final String ID = MizukiModCore.MakePath(HyperplasticTentacle.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public HyperplasticTentacle()
    {
        super(ID, false, cardStrings, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        setupDamage(6);
        setupMagicNumber(1, 1, 0 , 0);
        this.exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m)
    {
        addToBot((AbstractGameAction)new DamageAction((AbstractCreature)m, new DamageInfo((AbstractCreature)p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SMASH));

        addToTop((AbstractGameAction)new HyperplasticTentacleAction(1, upgraded));
    }

    public void upgrade()
    {
        if (!this.upgraded)
        {
            upgradeName();
            upgradeDamage(2);
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    public AbstractCard makeCopy()
    {
        return new HyperplasticTentacle();
    }
}
