package cards.Skills;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import cards.AbstractMizukiCard;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import helper.EventHelper;
import modcore.MizukiModCore;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import powers.HidingPower;


public class Hiding extends AbstractMizukiCard
{
    public static final String ID = MizukiModCore.MakePath(Hiding.class.getSimpleName());
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public Hiding()
    {
        super(ID, false, cardStrings, 2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        this.exhaust = true;
        //setupBlock(7);
    }


    public void use(AbstractPlayer p, AbstractMonster m)
    {
        //addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)m, (AbstractCreature)p, (AbstractPower)new WeakPower((AbstractCreature)m, 1, false), 1));
        //addToBot((AbstractGameAction)new GainBlockAction((AbstractCreature)p, (AbstractCreature)p, this.block));
        //addToBot((AbstractGameAction)new ChangeStanceAction("Untargetable"));
        if (!EventHelper.PlayedATCInTurn)
        {
            addToBot((AbstractGameAction)new ChangeStanceAction("Untargetable"));
            //addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p, (AbstractPower)new HidingPower((AbstractCreature)p, 1), 1));
        }
        else
        {
            addToBot((AbstractGameAction)new TalkAction(true, CardCrawlGame.languagePack.getCharacterString("Mizuki_Mizuki").TEXT[5], 2.0F, 2.0F));
        }
    }

    public void upgrade()
    {
        if (!this.upgraded)
        {
            upgradeName();
            upgradeBaseCost(1);
            //upgradeBlock(0);
        }
    }

    @Override
    public void triggerOnGlowCheck()
    {
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();

        if (!EventHelper.PlayedATCInTurn)
            this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
    }
}