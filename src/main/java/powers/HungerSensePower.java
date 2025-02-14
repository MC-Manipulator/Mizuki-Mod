package powers;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import modcore.MizukiModCore;
import monsters.special.SeabornsFilialGeneration;

public class HungerSensePower extends AbstractMizukiPower
{
    public static final String id = MizukiModCore.MakePath(HungerSensePower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(id);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private boolean hasPlayedAt;
    private boolean hasPlayedSk;
    private boolean hasPlayedPr;
    private boolean hasPlayedSt;
    private boolean hasPlayedCu;
    private int TypePlayed = 0;
    private boolean hasStunned = false;
    public HungerSensePower(AbstractCreature owner, int amt)
    {
        super(owner, amt, id, NAME);
        updateDescription();
        this.type = PowerType.BUFF;
        this.isTurnBased = false;
        this.hasStunned = false;
        this.TypePlayed = 0;
    }

    @Override
    public String getDescription()
    {
        if (owner != null)
        {
            return DESCRIPTIONS[0] + AbstractDungeon.player.getCharacterString().NAMES[0] + DESCRIPTIONS[1] + 2 + DESCRIPTIONS[2] + owner.name + DESCRIPTIONS[3];
        }
        else
        {
            return DESCRIPTIONS[0] + "player" + DESCRIPTIONS[1] + 2 + DESCRIPTIONS[2] + "owner" + DESCRIPTIONS[3];
        }
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action)
    {
        super.onAfterUseCard(card, action);
        if (card.type == AbstractCard.CardType.ATTACK && !hasPlayedAt)
        {
            hasPlayedAt = true;
            TypePlayed++;
        }
        else if (card.type == AbstractCard.CardType.SKILL && !hasPlayedSk)
        {
            hasPlayedSk = true;
            TypePlayed++;
        }
        else if (card.type == AbstractCard.CardType.POWER && !hasPlayedPr)
        {
            hasPlayedPr = true;
            TypePlayed++;
        }
        else if (card.type == AbstractCard.CardType.CURSE && !hasPlayedCu)
        {
            hasPlayedCu = true;
            TypePlayed++;
        }
        else if (card.type == AbstractCard.CardType.STATUS && !hasPlayedSt)
        {
            hasPlayedSt = true;
            TypePlayed++;
        }
        if (TypePlayed >= 2 && !hasStunned)
        {
            hasStunned = true;
            if (owner != null)
            {
                flash();
                owner.useFastShakeAnimation(0.5f);
                ((AbstractMonster)owner).state.setAnimation(0, "Attack_End", false);
                ((AbstractMonster)owner).state.addAnimation(0, "Idle_02", true, 0.0F);
                ((AbstractMonster)owner).setMove(SeabornsFilialGeneration.MOVES[1], (byte)1, AbstractMonster.Intent.STUN);
                ((AbstractMonster)owner).createIntent();
            }

        }
    }
}
