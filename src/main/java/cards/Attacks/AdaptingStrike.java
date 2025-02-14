package cards.Attacks;

import cards.AbstractMizukiCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import modcore.MizukiModCore;
import actions.AdaptingStrikeAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;

public class AdaptingStrike extends AbstractMizukiCard
{
    public static final String ID = MizukiModCore.MakePath(AdaptingStrike.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public AdaptingStrike()
    {
        super(ID, false, cardStrings, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        setupDamage(12);
        this.exhaust = true;
        this.tags.add(CardTags.STRIKE);
        this.baseMagicNumber = 1;
        this.magicNumber = this.baseMagicNumber;
    }

    public void use(AbstractPlayer p, AbstractMonster m)
    {
        addToBot((AbstractGameAction)new AdaptingStrikeAction((AbstractCreature)m, new DamageInfo((AbstractCreature)p, this.damage, this.damageTypeForTurn), this.magicNumber));

    }

    public void upgrade()
    {
        if (!this.upgraded)
        {
            upgradeName();
            upgradeDamage(3);
            upgradeMagicNumber(1);
        }
    }

}
