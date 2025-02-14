package cards.Attacks;

import cards.AbstractMizukiCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import helper.ImpairementManager;
import modcore.MizukiModCore;

public class Ruminate extends AbstractMizukiCard
{
    public static final String ID = MizukiModCore.MakePath(Ruminate.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public Ruminate()
    {
        super(ID, false, cardStrings, 1, AbstractCard.CardType.ATTACK, CardRarity.COMMON, AbstractCard.CardTarget.ENEMY);
        setupDamage(8);
        setupMagicNumber( 1, 6, 0, 0);
        tags.add(CardTags.HEALING);
        this.exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m)
    {
        damageToEnemy(m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        AbstractDungeon.player.decreaseMaxHealth(this.magicNumber);
        addToBot((AbstractGameAction)new HealAction((AbstractCreature)p, (AbstractCreature)p, magicNumber2));
    }

    public void upgrade()
    {
        if (!this.upgraded)
        {
            upgradeName();
            upgradeDamage(4);
            initializeDescription();
        }
    }
}
