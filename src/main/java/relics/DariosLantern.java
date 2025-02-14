package relics;

import cards.Skills.TowerOfTheWitchKing;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.powers.AbstractPower;
import modcore.MizukiModCore;
import powers.TowerOfTheWitchKingPower;

public class DariosLantern extends AbstractMizukiRelic
{
    public static final String ID = MizukiModCore.MakePath(DariosLantern.class.getSimpleName());
    public DariosLantern()
    {
        super(ID, RelicTier.SPECIAL, LandingSound.FLAT);
    }

    @Override
    public void onEquip()
    {
        if (CardHelper.hasCardWithType(AbstractCard.CardType.CURSE))
        {
            AbstractCard c = CardHelper.returnCardOfType(AbstractCard.CardType.CURSE, AbstractDungeon.miscRng);
            AbstractDungeon.player.masterDeck.removeCard(c);
        }
    }
}