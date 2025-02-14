package cards.Attacks;

import cards.AbstractMizukiCard;
import cards.Skills.BodyChange;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import modcore.MizukiModCore;

public class EnhancedTentacle extends AbstractMizukiCard
{
    public static final String ID = MizukiModCore.MakePath(EnhancedTentacle.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public EnhancedTentacle()
    {
        super(ID, false, cardStrings, 1, CardType.ATTACK, CardRarity.SPECIAL, CardTarget.ENEMY);
        setupDamage(10);

        this.baseMagicNumber = 10;
        this.magicNumber = this.baseMagicNumber;

        this.exhaust = true;
    }
    public EnhancedTentacle(int damage)
    {
        super(ID, false, cardStrings, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        setupDamage(damage);

        this.baseMagicNumber = damage;
        this.magicNumber = this.baseMagicNumber;
        this.exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m)
    {
        addToBot((AbstractGameAction)new DamageAction((AbstractCreature)m, new DamageInfo((AbstractCreature)p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SMASH));
        for (AbstractCard c : AbstractDungeon.player.discardPile.group)
        {
            if (c instanceof BodyChange)
            {
                AbstractDungeon.player.increaseMaxHp(this.magicNumber, true);
                addToTop((AbstractGameAction)new ExhaustSpecificCardAction(c, AbstractDungeon.player.discardPile));
                break;
            }
        }
    }

    public void upgrade()
    {
        if (!this.upgraded)
        {
            upgradeName();
            upgradeDamage(5);
            upgradeMagicNumber(5);
        }
    }

}
