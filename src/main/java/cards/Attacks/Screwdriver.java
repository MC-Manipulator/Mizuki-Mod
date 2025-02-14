package cards.Attacks;

import actions.ScrewdriverAction;
import cards.AbstractMizukiCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import helper.CookingHelper;
import modcore.MizukiModCore;

import java.util.ArrayList;

public class Screwdriver extends AbstractMizukiCard
{
    public static final String ID = MizukiModCore.MakePath(Screwdriver.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public Screwdriver()
    {
        super(ID, false, cardStrings, 1, AbstractCard.CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        setupDamage(2);
        setupMagicNumber(4, 1, 0, 0);
    }

    public void use(AbstractPlayer p, AbstractMonster m)
    {
        DamageInfo info = new DamageInfo((AbstractCreature)AbstractDungeon.player, this.damage);
        addToBot(new ScrewdriverAction(this.magicNumber, m, info, this.magicNumber2));

        /*
        addToBot(new AbstractGameAction()
        {
            @Override
            public void update()
            {
                if (m != null)
                {
                    AbstractDungeon.effectList.add(new FlashAtkImgEffect(m.hb.cX, m.hb.cY, AttackEffect.SLASH_DIAGONAL));
                    m.damage(info);
                    addToTop((AbstractGameAction)new WaitAction(0.1F));
                    if ((((AbstractMonster)m).isDying || m.currentHealth <= 0) && !m.halfDead && !m.hasPower("Minion"))
                    {
                        for (int i = 0;i < magicNumber2;i++)
                        {
                            AbstractCard c = CookingHelper.getRandomIngredient();
                            AbstractDungeon.effectsQueue.add(
                                    new ShowCardAndObtainEffect(c.makeStatEquivalentCopy(), Settings.WIDTH / 1.5F, Settings.HEIGHT / 2.0F));
                        }
                    }
                    if ((AbstractDungeon.getCurrRoom()).monsters.areMonstersBasicallyDead())
                        AbstractDungeon.actionManager.clearPostCombatActions();
                }
                isDone = true;
            }
        });*/


        /*
        addToBot(new AbstractGameAction()
        {
            @Override
            public void update()
            {
                if (m != null)
                {
                    AbstractDungeon.effectList.add(new FlashAtkImgEffect(m.hb.cX, m.hb.cY, AttackEffect.SLASH_HORIZONTAL));
                    m.damage(info);
                    addToTop((AbstractGameAction)new WaitAction(0.1F));
                    if ((((AbstractMonster)m).isDying || m.currentHealth <= 0) && !m.halfDead && !m.hasPower("Minion"))
                    {
                        for (int i = 0;i < magicNumber2;i++)
                        {
                            AbstractCard c = CookingHelper.getRandomIngredient();
                            AbstractDungeon.effectsQueue.add(
                                    new ShowCardAndObtainEffect(c.makeStatEquivalentCopy(), Settings.WIDTH / 1.5F, Settings.HEIGHT / 2.0F));
                        }
                    }
                    else
                    {
                        DamageInfo info = new DamageInfo((AbstractCreature)AbstractDungeon.player, damage);
                        addToBot(new AbstractGameAction()
                        {
                            @Override
                            public void update()
                            {
                                if (m != null)
                                {
                                    AbstractDungeon.effectList.add(new FlashAtkImgEffect(m.hb.cX, m.hb.cY, AttackEffect.SLASH_DIAGONAL));
                                    m.damage(info);
                                    addToTop((AbstractGameAction)new WaitAction(0.1F));
                                    if ((((AbstractMonster)m).isDying || m.currentHealth <= 0) && !m.halfDead && !m.hasPower("Minion"))
                                    {
                                        for (int i = 0;i < magicNumber2;i++)
                                        {
                                            AbstractCard c = CookingHelper.getRandomIngredient();
                                            AbstractDungeon.effectsQueue.add(
                                                    new ShowCardAndObtainEffect(c.makeStatEquivalentCopy(), Settings.WIDTH / 1.5F, Settings.HEIGHT / 2.0F));
                                        }
                                    }
                                    else
                                    {
                                        DamageInfo info = new DamageInfo((AbstractCreature)AbstractDungeon.player, damage);
                                        addToBot(new AbstractGameAction()
                                        {
                                            @Override
                                            public void update()
                                            {
                                                if (m != null)
                                                {
                                                    AbstractDungeon.effectList.add(new FlashAtkImgEffect(m.hb.cX, m.hb.cY, AttackEffect.SLASH_DIAGONAL));
                                                    m.damage(info);
                                                    addToTop((AbstractGameAction)new WaitAction(0.1F));
                                                    if ((((AbstractMonster)m).isDying || m.currentHealth <= 0) && !m.halfDead && !m.hasPower("Minion"))
                                                    {
                                                        for (int i = 0;i < magicNumber2;i++)
                                                        {
                                                            AbstractCard c = CookingHelper.getRandomIngredient();
                                                            AbstractDungeon.effectsQueue.add(
                                                                    new ShowCardAndObtainEffect(c.makeStatEquivalentCopy(), Settings.WIDTH / 1.5F, Settings.HEIGHT / 2.0F));
                                                        }
                                                    }
                                                    else
                                                    {
                                                        DamageInfo info = new DamageInfo((AbstractCreature)AbstractDungeon.player, damage);
                                                        addToBot(new AbstractGameAction()
                                                        {
                                                            @Override
                                                            public void update()
                                                            {
                                                                if (m != null)
                                                                {
                                                                    AbstractDungeon.effectList.add(new FlashAtkImgEffect(m.hb.cX, m.hb.cY, AttackEffect.SLASH_DIAGONAL));
                                                                    m.damage(info);
                                                                    addToTop((AbstractGameAction)new WaitAction(0.1F));
                                                                    if ((((AbstractMonster)m).isDying || m.currentHealth <= 0) && !m.halfDead && !m.hasPower("Minion"))
                                                                    {
                                                                        for (int i = 0;i < magicNumber2;i++)
                                                                        {
                                                                            AbstractCard c = CookingHelper.getRandomIngredient();
                                                                            AbstractDungeon.effectsQueue.add(
                                                                                    new ShowCardAndObtainEffect(c.makeStatEquivalentCopy(), Settings.WIDTH / 1.5F, Settings.HEIGHT / 2.0F));
                                                                        }
                                                                    }
                                                                    if ((AbstractDungeon.getCurrRoom()).monsters.areMonstersBasicallyDead())
                                                                        AbstractDungeon.actionManager.clearPostCombatActions();
                                                                }
                                                                isDone = true;
                                                            }
                                                        });

                                                    }
                                                    if ((AbstractDungeon.getCurrRoom()).monsters.areMonstersBasicallyDead())
                                                        AbstractDungeon.actionManager.clearPostCombatActions();
                                                }
                                                isDone = true;
                                            }
                                        });
                                    }
                                    if ((AbstractDungeon.getCurrRoom()).monsters.areMonstersBasicallyDead())
                                        AbstractDungeon.actionManager.clearPostCombatActions();
                                }
                                isDone = true;
                            }
                        });
                    }
                    if ((AbstractDungeon.getCurrRoom()).monsters.areMonstersBasicallyDead())
                        AbstractDungeon.actionManager.clearPostCombatActions();
                }
                isDone = true;
            }
        });

/*
        for (int i = 0;i < magicNumber;i++)
        {
            damageToEnemy(m, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
        }
        if ((m.isDying || m.currentHealth <= 0) && !m.halfDead && !m.hasPower("Minion"))
        {
            for (int i = 0;i < magicNumber2;i++)
            {
                AbstractCard c = CookingHelper.getRandomIngredient();
                AbstractDungeon.effectsQueue.add(
                        new ShowCardAndObtainEffect(c.makeStatEquivalentCopy(), Settings.WIDTH / 1.5F, Settings.HEIGHT / 2.0F));
            }
        }
        if ((AbstractDungeon.getCurrRoom()).monsters.areMonstersBasicallyDead())
            AbstractDungeon.actionManager.clearPostCombatActions();*/
    }

    public void upgrade()
    {
        if (!this.upgraded)
        {
            upgradeName();
            upgradeDamage(1);
            upgradeMagicNumber2(1);
        }
    }
}
