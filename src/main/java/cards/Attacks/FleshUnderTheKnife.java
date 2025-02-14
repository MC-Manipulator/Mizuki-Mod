package cards.Attacks;

import cards.AbstractMizukiCard;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
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
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import helper.CookingHelper;
import modcore.MizukiModCore;
import rewards.IngredientReward;

public class FleshUnderTheKnife extends AbstractMizukiCard
{
    public static final String ID = MizukiModCore.MakePath(FleshUnderTheKnife.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public FleshUnderTheKnife()
    {
        super(ID, false, cardStrings, 2, AbstractCard.CardType.ATTACK, CardRarity.UNCOMMON, AbstractCard.CardTarget.ENEMY);
        setupDamage(18);
        setupMagicNumber(1, 0, 0, 0);
        this.exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m)
    {
        AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new VFXAction((AbstractGameEffect) new BiteEffect(m.hb.cX +
                MathUtils.random(-50.0F, 50.0F) * Settings.scale, m.hb.cY +
                MathUtils.random(-50.0F, 50.0F) * Settings.scale, Color.CHARTREUSE
                .cpy()), 0.2F));

        DamageInfo info = new DamageInfo((AbstractCreature)AbstractDungeon.player, this.damage);

        addToBot(new AbstractGameAction()
        {
            @Override
            public void update()
            {
                if (m != null)
                {
                    //AbstractDungeon.effectList.add(new FlashAtkImgEffect(m.hb.cX, m.hb.cY, AttackEffect.SLASH_DIAGONAL));
                    m.damage(info);
                    //addToTop((AbstractGameAction)new WaitAction(0.1F));
                    if (m.currentHealth < m.maxHealth / 2F)
                    {
                        for (int i = 0;i < magicNumber;i++)
                        {
                            String s = null;
                            do
                            {
                                s = CookingHelper.getRandomIngredientString();
                            }
                            while (s.equals("CrabLegs") || s.equals("Paddy"));

                            MizukiModCore.logger.info(s);
                            AbstractDungeon.getCurrRoom().rewards.add(new IngredientReward(s));
                        }
                    }
                    if ((AbstractDungeon.getCurrRoom()).monsters.areMonstersBasicallyDead())
                        AbstractDungeon.actionManager.clearPostCombatActions();
                }
                isDone = true;
            }
        });

        //addToBot((AbstractGameAction)new DamageAction((AbstractCreature)m, new DamageInfo((AbstractCreature)AbstractDungeon.player, this.damage), AbstractGameAction.AttackEffect.NONE, true));
    }

    public void upgrade()
    {
        if (!this.upgraded)
        {
            upgradeName();
            upgradeDamage(6);
            //upgradeMagicNumber(1);
        }
    }
}
