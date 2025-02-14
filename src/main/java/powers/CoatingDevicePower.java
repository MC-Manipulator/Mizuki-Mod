package powers;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import modcore.MizukiModCore;
import monsters.friendlys.ErosionproofCoatingDevice;
import monsters.special.SeabornsFilialGeneration;

public class CoatingDevicePower extends AbstractMizukiPower
{
    public static final String id = MizukiModCore.MakePath(CoatingDevicePower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(id);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private boolean hasPlayedAt;
    private boolean hasPlayedSk;
    private boolean hasPlayedPr;
    private boolean hasPlayedSt;
    private boolean hasPlayedCu;
    private int TypePlayed = 0;
    private boolean hasActivate = false;
    private ErosionproofCoatingDevice device = null;
    public CoatingDevicePower(AbstractCreature owner, int amt)
    {
        super(owner, amt, id, NAME);
        updateDescription();
        this.type = PowerType.BUFF;
        this.isTurnBased = false;
        this.hasActivate = false;
        this.TypePlayed = 0;
        this.device = (ErosionproofCoatingDevice)owner;
    }

    @Override
    public String getDescription()
    {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action)
    {
        super.onAfterUseCard(card, action);
        if (!hasActivate)
        {
            if (card.type == AbstractCard.CardType.ATTACK && !hasPlayedAt)
            {
                hasPlayedAt = true;
                TypePlayed++;
                amount++;
                flash();
            }
            else if (card.type == AbstractCard.CardType.SKILL && !hasPlayedSk)
            {
                hasPlayedSk = true;
                TypePlayed++;
                amount++;
                flash();
            }
            else if (card.type == AbstractCard.CardType.POWER && !hasPlayedPr)
            {
                hasPlayedPr = true;
                TypePlayed++;
                amount++;
                flash();
            }
            else if (card.type == AbstractCard.CardType.CURSE && !hasPlayedCu)
            {
                hasPlayedCu = true;
                TypePlayed++;
                amount++;
                flash();
            }
            else if (card.type == AbstractCard.CardType.STATUS && !hasPlayedSt)
            {
                hasPlayedSt = true;
                TypePlayed++;
                amount++;
                flash();
            }
            if (TypePlayed >= 3)
            {
                hasActivate = true;
                if (device != null)
                {
                    device.useFastShakeAnimation(0.5f);
                    device.state.setAnimation(0, "C_Idle", true);
                    device.setMove("镀层", (byte)1, AbstractMonster.Intent.BUFF);
                    device.createIntent();
                    device.activateTimes++;
                }
            }
        }
    }

    public void reset()
    {
        hasActivate = false;
        TypePlayed = 0;
        this.amount = 0;
        hasPlayedAt = false;
        hasPlayedSk = false;
        hasPlayedPr = false;
        hasPlayedSt = false;
        hasPlayedCu = false;
    }
}
