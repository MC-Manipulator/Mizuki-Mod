package cards.Attacks;

import basemod.interfaces.OnStartBattleSubscriber;
import cards.AbstractMizukiCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import helper.EventHelper;
import helper.LearnManager;
import powers.PacFishPower;
import modcore.MizukiModCore;
import powers.BreathOfTheTidePower;

public class PacFish extends AbstractMizukiCard implements EventHelper.OnPostBattleStartSubscriber
{
    public static final String ID = MizukiModCore.MakePath(PacFish.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public PacFish()
    {
        super(ID, false, cardStrings, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        setupDamage(6);
        setupMagicNumber(1, 0, 0, 0);
    }

    public void use(AbstractPlayer p, AbstractMonster m)
    {
        damageToEnemy(m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
    }

    @Override
    public void OnPostBattleStart(AbstractRoom room)
    {
        if (LearnManager.ifInMasterDeck(this) && !AbstractDungeon.player.hasPower(PacFishPower.id))
        {
            addToBot((AbstractGameAction)new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                    (AbstractPower)new PacFishPower(this, AbstractDungeon.player, 1), 0));
        }
    }

    public void upgrade()
    {
        if (!this.upgraded)
        {
            upgradeDamage(3);
            upgradeName();
        }
    }
}
