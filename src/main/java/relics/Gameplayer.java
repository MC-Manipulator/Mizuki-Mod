package relics;
import cards.Skills.TowerOfTheWitchKing;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import modcore.MizukiModCore;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import powers.TowerOfTheWitchKingPower;

public class Gameplayer extends AbstractMizukiRelic
{
    public static final String ID = MizukiModCore.MakePath(Gameplayer.class.getSimpleName());

    public Gameplayer()
    {
        super(ID, RelicTier.UNCOMMON, LandingSound.MAGICAL);
    }

    public void atBattleStartPreDraw()
    {
        TowerOfTheWitchKing card = new TowerOfTheWitchKing();
        addToBot((AbstractGameAction)new RelicAboveCreatureAction((AbstractCreature) AbstractDungeon.player, this));
        addToBot((AbstractGameAction)new MakeTempCardInHandAction((AbstractCard) card, 1, false));
        addToBot((AbstractGameAction)new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                (AbstractPower)new TowerOfTheWitchKingPower(card, AbstractDungeon.player, card.magicNumber2), 0));
    }
}
