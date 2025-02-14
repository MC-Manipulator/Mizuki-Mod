package cards.Powers;

import basemod.BaseMod;
import cards.AbstractMizukiCard;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import modcore.MizukiModCore;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.powers.AbstractPower;
import powers.EntreatyoftheHeraldPower;

import java.util.ArrayList;

public class EntreatyoftheHerald extends AbstractMizukiCard
{
    public static final String ID = MizukiModCore.MakePath(EntreatyoftheHerald.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public EntreatyoftheHerald()
    {
        super(ID, false, cardStrings, 1, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        //M是抽牌数，M2是生命恢复数
        setupMagicNumber(2, 5, 0, 0);
    }

    public void use(AbstractPlayer p, AbstractMonster m)
    {
        //addToBot((AbstractGameAction)new LoseHPAction((AbstractCreature)p, (AbstractCreature)p, 15));
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p, (AbstractPower)new EntreatyoftheHeraldPower((AbstractCreature)p, 1, magicNumber, magicNumber2), 1));
        //addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p, (AbstractPower)new DexterityPower((AbstractCreature)p, 3), 3));
    }

    public void upgrade()
    {
        if (!this.upgraded)
        {
            upgradeName();
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
            isInnate = true;
        }
    }
}