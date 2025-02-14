package patches;

import basemod.ReflectionHacks;
import basemod.abstracts.AbstractCardModifier;
import cards.AbstractMizukiCard;
import cards.Skills.TowerOfTheWitchKing;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;
import modcore.MizukiModCore;
import powers.TowerOfTheWitchKingPower;

public class TowerOfTheWitchKingMod extends AbstractCardModifier
{
    TowerOfTheWitchKing sourceCard;
    boolean trigger;
    AbstractCard owner;
    AbstractCard c = new TowerOfTheWitchKing();

    public TowerOfTheWitchKingMod(AbstractCard owner, TowerOfTheWitchKing sourceCard)
    {
        this.sourceCard = sourceCard;
        this.owner = owner;
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action)
    {
        trigger = true;
    }

    @Override
    public void onUpdate(AbstractCard card)
    {
        if (AbstractDungeon.actionManager.cardsPlayedThisTurn == null)
            return;

        if (AbstractDungeon.actionManager.cardsPlayedThisTurn.contains(owner) && trigger)
        {
            for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters)
            {
                if ((((AbstractMonster)m).isDying || m.currentHealth <= 0) && !m.halfDead)
                {
                    if (AbstractDungeon.player.hasPower(TowerOfTheWitchKingPower.id) && trigger)
                    {
                        int amount = AbstractDungeon.player.getPower(TowerOfTheWitchKingPower.id).amount;
                        AbstractDungeon.player.gainGold(amount);
                        for (int i = 0; i < amount; i++)
                            AbstractDungeon.effectList.add(new GainPennyEffect(AbstractDungeon.player, m.hb.cX, m.hb.cY, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, true));
                        //AbstractDungeon.actionManager.addToBottom((AbstractGameAction) new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, TowerOfTheWitchKingPower.id, 1));
                        AbstractDungeon.actionManager.addToBottom((AbstractGameAction) new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, TowerOfTheWitchKingPower.id));
                        AbstractDungeon.player.masterDeck.removeCard(sourceCard);
                        trigger = false;
                    }
                }
            }
        }
    }

    @Override
    public void onRender(AbstractCard card, SpriteBatch sb)
    {
        boolean hovered = ((Boolean) ReflectionHacks.getPrivate(card, AbstractCard.class, "renderTip")).booleanValue();

        c.targetDrawScale = hovered ? (0.8F * card.drawScale) : (0.2F * card.drawScale);
        //card.current_x += AbstractCard.IMG_WIDTH * c.drawScale;
        //card.current_y += AbstractCard.IMG_HEIGHT * 0.5F * (card.drawScale + c.drawScale);
        c.target_x = card.current_x;
        c.target_y = card.current_y + AbstractCard.IMG_HEIGHT * 0.5F * (card.drawScale + c.drawScale);
        c.update();
        c.render(sb);
    }

    public AbstractCardModifier makeCopy()
    {
        return new TowerOfTheWitchKingMod(owner, sourceCard);
    }
}
