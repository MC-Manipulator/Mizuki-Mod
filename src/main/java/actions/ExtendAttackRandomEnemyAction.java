package actions;

import cards.AbstractMizukiCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;

import java.util.UUID;

;

public class ExtendAttackRandomEnemyAction extends AbstractGameAction
{
    private AbstractCard card;
    private int damage;
    private AttackEffect effect;

    public ExtendAttackRandomEnemyAction(AbstractCard card, int damage, AttackEffect effect)
    {
        this.card = card;
        this.damage = damage;
        this.effect = effect;
    }

    public void update()
    {
        this.target = (AbstractCreature) AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
        if (this.target != null)
        {
            if (AttackEffect.LIGHTNING == this.effect)
            {
                addToTop(new DamageAction(this.target, new DamageInfo((AbstractCreature)AbstractDungeon.player, this.damage, this.card.damageTypeForTurn), AttackEffect.NONE));
                addToTop((AbstractGameAction)new SFXAction("ORB_LIGHTNING_EVOKE", 0.1F));
                addToTop((AbstractGameAction)new VFXAction((AbstractGameEffect) new LightningEffect(this.target.hb.cX, this.target.hb.cY)));
            }
            else
            {
                addToTop(new DamageAction(this.target, new DamageInfo((AbstractCreature)AbstractDungeon.player, this.damage, this.card.damageTypeForTurn), this.effect));
            }
        }
        this.isDone = true;
    }
}
