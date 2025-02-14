package cards.Attacks;

import Impairment.AbstractImpairment;
import Impairment.NervousImpairment;
import actions.ApplyImpairmentAction;
import cards.AbstractMizukiCard;
import modcore.MizukiModCore;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class Mirage extends AbstractMizukiCard
{
    public static final String ID = MizukiModCore.MakePath(Mirage.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final int costHealth = 6;

    public Mirage()
    {
        super(ID, false, cardStrings, 1, CardType.ATTACK, CardRarity.RARE, CardTarget.ALL_ENEMY);
        setupDamage(15);
        setupMagicNumber(1, costHealth, 0, 0);
        this.isMultiDamage = true;
        this.isEthereal = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m)
    {
        int count = 0;
        for (AbstractMonster m2 : (AbstractDungeon.getCurrRoom()).monsters.monsters)
        {
            if (!m2.isDeadOrEscaped())
            {
                addToBot((AbstractGameAction) new ApplyImpairmentAction(new NervousImpairment(), m2, magicNumber, AbstractImpairment.ImpairmentType.Nervous));
                count++;
            }
        }
        if (count <= 1)
        {
            addToBot((AbstractGameAction)new LoseHPAction((AbstractCreature)p, (AbstractCreature)p, magicNumber2));
        }

        damageToAllEnemies(AbstractGameAction.AttackEffect.POISON);
        addToBot(new AbstractGameAction()
        {
            @Override
            public void update()
            {
                CardCrawlGame.sound.play("MIZUKI_SKILL3", 0.05F);
                isDone = true;
            }
        });
    }


    public void upgrade()
    {
        if (!this.upgraded)
        {
            upgradeName();
            upgradeMagicNumber(1);
            //upgradeDamage(3);
        }
    }
}
