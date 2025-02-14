package cards.Attacks;

import cards.AbstractMizukiCard;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.VerticalImpactEffect;
import modcore.MizukiModCore;

import java.util.ArrayList;

public class EchoOfOneAmongMany extends AbstractMizukiCard
{
    public static final String ID = MizukiModCore.MakePath(EchoOfOneAmongMany.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    ArrayList<String> cardName;

    public EchoOfOneAmongMany()
    {
        super(ID, false, cardStrings, 2, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        setupDamage(6);
        setupBlock(5);
        setupMagicNumber(1, 3, 0, 0);
        cardName = new ArrayList<>();
        this.tags.add(CardTags.HEALING);
    }

    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (damage >= 30)
        {
            addToBot((AbstractGameAction)new VFXAction((AbstractGameEffect)new VerticalImpactEffect(m.hb.cX + m.hb.width / 4.0F, m.hb.cY - m.hb.height / 4.0F)));
            damageToEnemy(m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        }
        if (damage >= 15 && damage < 30)
        {
            damageToEnemy(m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        }
        if (damage < 15)
        {
            damageToEnemy(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        }
        addToBot((AbstractGameAction) new HealAction((AbstractCreature)p, (AbstractCreature)p, magicNumber));
        gainBlock();
    }


    public void triggerOnCardPlayed(AbstractCard card)
    {
        //MizukiModCore.logger.info("The card name that is playing :" + card.cardID);
        if (cardName.isEmpty())
        {
            cardName.add(card.cardID);
        }
        else
        {
            if (cardName.contains(card.cardID))
            {
                //MizukiModCore.logger.info("Has played the same card :" + card.cardID);
                for (int i = 0;i < magicNumber2;i++)
                {
                    int roll = AbstractDungeon.cardRandomRng.random(99);
                    if (roll < 50)
                    {
                        this.baseDamage++;
                    }
                    else if (roll < 80)
                    {
                        this.baseBlock++;
                    }
                    else
                    {
                        this.baseMagicNumber++;
                    }
                    this.superFlash();
                }
            }
            else
            {
                cardName.add(card.cardID);
            }
        }

        applyPowers();
    }

    public void applyPowers()
    {
        super.applyPowers();
        initializeDescription();
    }

    public void upgrade()
    {
        if (!this.upgraded)
        {
            upgradeDamage(3);
            upgradeBlock(3);
            upgradeMagicNumber(2);
            upgradeMagicNumber2(1);
            upgradeName();
            //upgradeBaseCost(1);
        }
    }
}
