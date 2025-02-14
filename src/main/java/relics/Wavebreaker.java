package relics;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import cards.Skills.TowerOfTheWitchKing;
import characters.Mizuki;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.ReduceCostAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import modcore.MizukiModCore;
import patches.TowerOfTheWitchKingMod;
import patches.WavebreakerMod;
import powers.TowerOfTheWitchKingPower;

import java.util.Iterator;

public class Wavebreaker extends AbstractMizukiRelic
{
    //分浪
    public static final String ID = MizukiModCore.MakePath(Wavebreaker.class.getSimpleName());

    private static final int amount = 5;

    private static final float health1Percent = 0.5f;

    public Wavebreaker()
    {
        super(ID, RelicTier.SPECIAL, AbstractRelic.LandingSound.HEAVY);
    }

    public String getUpdatedDescription()
    {
        return this.DESCRIPTIONS[0] + (int)(health1Percent * 100) + this.DESCRIPTIONS[1] + amount + this.DESCRIPTIONS[2];
    }

    @Override
    public void onEquip()
    {
        AbstractDungeon.player.decreaseMaxHealth(MathUtils.ceil(AbstractDungeon.player.maxHealth * 0.5F));
    }

    @Override
    public void atBattleStart()
    {
        AbstractPlayer p = AbstractDungeon.player;
        addToTop((AbstractGameAction)new RelicAboveCreatureAction((AbstractCreature)AbstractDungeon.player, this));

        for (int i = 0;i < amount;i++)
        {
            AbstractCard c;
            boolean hasdone = false;
            while (!hasdone)
            {
                c = p.drawPile.getRandomCard(true);
                if (c.cost != 0 || !CardModifierManager.hasModifier(c, WavebreakerMod.ID))
                {
                    CardModifierManager.addModifier(c, (AbstractCardModifier)new WavebreakerMod());
                    c.modifyCostForCombat(-1);
                    c.applyPowers();
                    hasdone = true;
                }
                boolean hasZero = true;
                for (AbstractCard c2 : p.drawPile.group)
                {
                    if (c2.cost != 0 || !CardModifierManager.hasModifier(c2, WavebreakerMod.ID))
                    {
                        hasZero = false;
                    }
                }
                if (hasZero)
                {
                    break;
                }
            }
        }
    }
}