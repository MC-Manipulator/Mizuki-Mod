package cards.Attacks;

import Impairment.NervousImpairment;
import actions.ApplyImpairmentAction;
import cards.AbstractMizukiCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import helper.ImpairementManager;
import modcore.MizukiModCore;

public class MindTouch extends AbstractMizukiCard
{
    public static final String ID = MizukiModCore.MakePath(MindTouch.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public MindTouch()
    {
        super(ID, false, cardStrings, 1, AbstractCard.CardType.ATTACK, AbstractCard.CardRarity.RARE, CardTarget.ENEMY);
        setupDamage(6);
        setupMagicNumber(1, 1, 0, 0);
        //this.exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m)
    {
        damageToEnemy(m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        if (ImpairementManager.getCreatureNervousImpairments(m) != null)
        {
            if (ImpairementManager.getCreatureNervousImpairments(m).currentcount != 0)
            {
                /*
                for (int i = 0;i < ImpairementManager.getMonsterNervousImpairments(m).currentcount;i++)
                {
                    //damageToEnemy(m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);

                }*/
                ImpairementManager.tempOncenNrvousImpairementDamage.add(this.damage);
                ImpairementManager.tempOnceNervousImpairementTimes.add(ImpairementManager.getCreatureNervousImpairments(m).currentcount);
            }
            //ImpairementManager.resetMonsterNervousImpairments(m);
        }
    }


    public void upgrade()
    {
        if (!this.upgraded)
        {
            upgradeName();
            this.selfRetain = true;
            upgradeDamage(2);
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
