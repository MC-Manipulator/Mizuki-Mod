package powers;

import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import modcore.MizukiModCore;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;


public class HysterotraumaticPower extends AbstractMizukiPower
{
    public static final String id = MizukiModCore.MakePath(HysterotraumaticPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(id);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public HysterotraumaticPower(AbstractCreature owner, int amt)
    {
        super(owner, amt, id, NAME);
        updateDescription();
        this.type = PowerType.BUFF;
    }

    public String getDescription()
    {
        return DESCRIPTIONS[0];
    }

    public void onPlayCard(AbstractCard card, AbstractMonster m)
    {
        super.onPlayCard(card, m);
        if (m != null && card.type == AbstractCard.CardType.ATTACK)
        {
            int[] m_health = new int[10];
            int i = 0;
            boolean islowest = true;
            for (AbstractMonster m2 : (AbstractDungeon.getCurrRoom()).monsters.monsters)
            {

                if (!m2.isDeadOrEscaped())
                {
                    m_health[i++] = m2.currentHealth;
                }
            }
            for (int j = 0;j < i;j++)
            {
                if ( m.currentHealth > m_health[j] )
                {
                    islowest = false;
                    break;
                }
            }
            if (islowest)
            {
                flash();
                addToTop((AbstractGameAction)new LoseHPAction(m , this.owner, this.amount, AbstractGameAction.AttackEffect.POISON));
            }
        }
    }

    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target)
    {
        /*
        if (damageAmount >= 0 && target != this.owner && info.type == DamageInfo.DamageType.NORMAL)
        {
            int cant = 0;
            int[] i = new int[10];
            int x = 0;
            for (AbstractMonster m2 : (AbstractDungeon.getCurrRoom()).monsters.monsters)
            {

                if (!m2.isDeadOrEscaped())
                {
                    i[x++] = m2.currentHealth;
                }
            }
            for (int j = 0;j < x;j++)
            {
                if ( target.currentHealth > i[j] )
                {
                    cant = 1;
                    break;
                }
            }

            if (cant == 0)
            {
                flash();
                addToTop((AbstractGameAction)new LoseHPAction(target , this.owner, this.amount, AbstractGameAction.AttackEffect.NONE));
            }
        }*/
    }
}
