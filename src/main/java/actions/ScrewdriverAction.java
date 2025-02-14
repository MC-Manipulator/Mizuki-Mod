package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import helper.CookingHelper;
import helper.DiceHelper;
import modcore.MizukiModCore;
import rewards.IngredientReward;

public class ScrewdriverAction extends AbstractGameAction
{
    private AbstractPlayer player;
    private AbstractMonster m;
    private DamageInfo info;
    private int attackTimes;
    private int rewardAmount;
    public ScrewdriverAction(int times, AbstractMonster m, DamageInfo info, int rewardAmount)
    {
        this.m = m;
        this.info = info;
        this.player = AbstractDungeon.player;
        this.attackTimes = times;
        this.rewardAmount = rewardAmount;
    }


    public void update()
    {
        if (info != null && m != null && attackTimes > 0 && m.currentHealth > 0)
        {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(m.hb.cX, m.hb.cY, AttackEffect.SLASH_DIAGONAL));
            m.damage(info);
            if ((((AbstractMonster)m).isDying || m.currentHealth <= 0) && !m.halfDead && !m.hasPower("Minion"))
            {
                for (int i = 0;i < rewardAmount;i++)
                {
                    AbstractDungeon.getCurrRoom().rewards.add(new IngredientReward(CookingHelper.getRandomIngredientString()));
                    //AbstractDungeon.effectsQueue.add(new ShowCardAndObtainEffect(c.makeStatEquivalentCopy(), Settings.WIDTH / 1.5F, Settings.HEIGHT / 2.0F));
                }
                if ((AbstractDungeon.getCurrRoom()).monsters.areMonstersBasicallyDead())
                {
                    AbstractDungeon.actionManager.clearPostCombatActions();
                }
                isDone = true;
                return;
            }
            if ((!((AbstractMonster)m).isDying || m.currentHealth > 0) && !m.halfDead)
            {
                addToTop(new ScrewdriverAction(this.attackTimes - 1, this.m, this.info, this.rewardAmount));
            }
            addToTop((AbstractGameAction)new WaitAction(0.1F));
        }
        isDone = true;
    }
}
