package cards.Attacks;

import basemod.abstracts.AbstractCardModifier;
import basemod.cardmods.ExhaustMod;
import basemod.helpers.CardModifierManager;
import cards.AbstractMizukiCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import modcore.MizukiModCore;
import powers.BreathOfTheTidePower;
import powers.CopyAttackCardPower;

public class EntanglementProliferation extends AbstractMizukiCard
{
    public static final String ID = MizukiModCore.MakePath(EntanglementProliferation.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public EntanglementProliferation()
    {
        super(ID, false, cardStrings, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        setupDamage(6);
        setupMagicNumber(2, 0, 0, 0);
    }

    public void use(AbstractPlayer p, AbstractMonster m)
    {
        damageToEnemy(m, AbstractGameAction.AttackEffect.LIGHTNING);

        addToBot((AbstractGameAction)new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                (AbstractPower)new CopyAttackCardPower(this, AbstractDungeon.player, this.magicNumber), this.magicNumber));
    }

    public void upgrade()
    {
        if (!this.upgraded)
        {
            upgradeMagicNumber(1);
            upgradeName();
        }
    }
}
