package patches;

import basemod.ReflectionHacks;
import characters.Mizuki;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.IntentFlashAction;
import com.megacrit.cardcrawl.actions.common.ShowMoveNameAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.CtBehavior;
import modcore.MizukiModCore;
import monsters.friendlys.AbstractFriendly;
import monsters.friendlys.ErosionproofCoatingDevice;

import java.util.ArrayList;

public class FriendlyPatch
{

    public static ArrayList<AbstractFriendly> Inst()
    {
        if (AbstractDungeon.player == null)
            return null;
        return (ArrayList<AbstractFriendly>)FriendlyFields.FriendlyList.get(AbstractDungeon.player);
    }
    /*
    public static void ReduceReviveTime(int amt)
    {
        if (AbstractDungeon.player.chosenClass == Kaltsit.Enums.KALTSIT || AbstractDungeon.player
                .hasRelic("PrismaticShard")) {
            ReviveTimer -= amt;
            if (Inst() == null) {
                if (ReviveTimer <= 0) {
                    ReviveTimer = 0;
                    Mon3trFields.Mon3tr.set(AbstractDungeon.player, new Mon3tr());
                    ((Mon3tr)Objects.<Mon3tr>requireNonNull(Inst())).spawn();
                }
                CalciteModel mod = (CalciteModel)Mon3trFields.Calcite.get(AbstractDungeon.player);
                if (mod != null)
                    mod.reduce();
                int roll = MathUtils.random(0, 2);
                if (roll == 0) {
                    CardCrawlGame.sound.play("BUFF_1");
                } else if (roll == 1) {
                    CardCrawlGame.sound.play("BUFF_2");
                } else {
                    CardCrawlGame.sound.play("BUFF_3");
                }
            }
        }
    }*/

    @SpirePatch(clz = AbstractPlayer.class, method = "<class>")
    public static class FriendlyFields
    {
        public static SpireField<ArrayList<AbstractFriendly>> FriendlyList = new SpireField<ArrayList<AbstractFriendly>>(() -> null);
        //在玩家的类里存放友方单位
        //public static SpireField<Mon3tr> Mon3tr = new SpireField(() -> null);

        //public static SpireField<CalciteModel> Calcite = new SpireField(() -> null);
    }

    @SpirePatch(clz = AbstractRoom.class, method = "update")
    public static class MonsterUpdatePatch
    {
        public static void Postfix(AbstractRoom _inst)
        {
            //更新友方单位的数据与动画
            ArrayList<AbstractFriendly> f = FriendlyPatch.Inst();
            if (f != null)
            {
                for (AbstractFriendly aF : f)
                {
                    aF.update();
                    aF.updateAnimations();
                }
            }

            /*
            Mon3tr m = MonsterPatch.Inst();
            if (m != null) {
                m.update();
                m.updateAnimations();
                if (m.isDead)
                    MonsterPatch.Mon3trFields.Mon3tr.set(AbstractDungeon.player, null);
            } else {
                CalciteModel mod = (CalciteModel)MonsterPatch.Mon3trFields.Calcite.get(AbstractDungeon.player);
                if (mod != null)
                    mod.update();
            }*/
        }
    }

    @SpirePatch(clz = AbstractRoom.class, method = "render", paramtypez = {SpriteBatch.class})
    public static class MonsterRenderPatch
    {
        @SpireInsertPatch(rloc = 12)
        public static void Insert(AbstractRoom _inst, SpriteBatch sb)
        {
            if (_inst.monsters != null)
            {

                ArrayList<AbstractFriendly> f = FriendlyPatch.Inst();
                if (f != null)
                {
                    for (AbstractFriendly abstractFriendly : f)
                    {
                        abstractFriendly.render(sb);
                    }
                }
                /*
                Mon3tr m = MonsterPatch.Inst();
                if (m != null)
                {
                    m.render(sb);
                }
                else
                {
                    CalciteModel mod = (CalciteModel)MonsterPatch.Mon3trFields.Calcite.get(AbstractDungeon.player);
                    if (mod != null)
                        mod.render(sb);
                }*/
            }
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "applyPreCombatLogic")
    public static class MonsterStartOfBattlePatch
    {

        public static void Postfix(AbstractPlayer _inst)
        {
            if (FriendlyFields.FriendlyList.get(AbstractDungeon.player) == null)
            {
                FriendlyFields.FriendlyList.set(AbstractDungeon.player, new ArrayList<AbstractFriendly>());
            }
            else
            {
                for (AbstractFriendly f : FriendlyFields.FriendlyList.get(AbstractDungeon.player))
                {
                    f.spawn();
                }
            }

            /*
            if (AbstractDungeon.player.chosenClass == Kaltsit.Enums.KALTSIT || AbstractDungeon.player
                    .hasRelic("PrismaticShard"))
            {
                MonsterPatch.Mon3trFields.Mon3tr.set(AbstractDungeon.player, null);
                MonsterPatch.Mon3trFields.Calcite.set(AbstractDungeon.player, new CalciteModel());
                if (_inst.hasRelic(Calcite.ID))
                {
                    MonsterPatch.ReviveTimer = 2;
                }
                else
                {
                    MonsterPatch.ReviveTimer = 5;
                }
            }
            else
            {
                MonsterPatch.Mon3trFields.Mon3tr.set(AbstractDungeon.player, null);
                MonsterPatch.Mon3trFields.Calcite.set(AbstractDungeon.player, null);
            }*/
        }
    }

    @SpirePatch(clz = GameActionManager.class, method = "getNextAction")
    public static class StartOfTurnPatch
    {
        @SpireInsertPatch(rloc = 240)
        public static void Insert(GameActionManager _inst)
        {
            ArrayList<AbstractFriendly> f = FriendlyPatch.Inst();
            if (f != null)
            {
                for (AbstractFriendly aF : f)
                {
                    if (!aF.hasPower("Barricade") && !aF.hasPower("Blur"))
                        aF.loseBlock();
                    aF.applyStartOfTurnPowers();
                    aF.applyStartOfTurnPostDrawPowers();
                    aF.createIntent();
                    aF.addToBot(new AbstractGameAction()
                    {
                        @Override
                        public void update()
                        {
                            aF.applyPowers();
                            isDone = true;
                        }
                    });
                }
            }
            //友方单位回合开始时扳机
            /*
            Mon3tr m = MonsterPatch.Inst();
            if (m != null)
            {
                if (!m.hasPower("Barricade") && !m.hasPower("Blur"))
                    m.loseBlock();
                m.applyStartOfTurnPowers();
                m.applyStartOfTurnPostDrawPowers();
                GenericHelper.addToBotAbstract(() ->
                {
                    m.gainBlockNextTurn = false;
                    m.damageToAllNextTurn = false;
                    m.damageToLowestNextTurn = false;
                    m.meltdownNextTurn = false;
                    m.damageTimes = 1;
                    m.applyPowers();
                });
            }*/
        }
    }

    @SpirePatch(clz = GameActionManager.class, method = "callEndOfTurnActions")
    public static class EndOfTurnPatch
    {
        public static void Postfix(GameActionManager _inst)
        {
            ArrayList<AbstractFriendly> f = FriendlyPatch.Inst();
            if (f != null)
            {
                for (AbstractFriendly aF : f)
                {
                    aF.addToBot((AbstractGameAction)new ShowMoveNameAction(aF));
                    aF.addToBot((AbstractGameAction)new IntentFlashAction(aF));
                    aF.takeTurn();
                    aF.applyEndOfTurnTriggers();
                }
            }
            //友方单位回合结束扳机触发
            /*
            Mon3tr m = MonsterPatch.Inst();
            if (m != null)
            {
                m.takeTurn();
                m.applyEndOfTurnTriggers();
            }
            */
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "onModifyPower")
    public static class OnModifyPowerPatch
    {
        public static void Postfix()
        {
            if (AbstractDungeon.player != null && AbstractDungeon.getMonsters() != null)
            {
                ArrayList<AbstractFriendly> f = FriendlyPatch.Inst();
                if (f != null)
                {
                    for (AbstractFriendly aF : f)
                    {
                        aF.applyPowers();
                    }
                }
                //友方单位更新
                /*
                Mon3tr m = MonsterPatch.Inst();
                if (m != null)
                    m.applyPowers();

                */
            }
        }
    }

    @SpirePatch(clz = AbstractMonster.class, method = "renderTip")
    public static class RenderTipPatch
    {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(AbstractMonster _inst, SpriteBatch sb)
        {
            //添加提示

            if (_inst instanceof AbstractFriendly)
            {
                if (_inst != null)
                {
                    ((AbstractFriendly)_inst).addTip();
                }
            }
        }

        private static class Locator extends SpireInsertLocator
        {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(ArrayList.class, "add");
                return new int[] { LineFinder.findAllInOrder(ctMethodToPatch, (Matcher)methodCallMatcher)[0] };
            }
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "renderHoverReticle")
    public static class ReticlePatch
    {
        public static void Postfix(AbstractPlayer _inst, SpriteBatch sb)
        {
            //渲染选框
            /*
            if (_inst.hoveredCard.target == Kaltsit.Enums.SELF_AND_MON3TR)
            {
                _inst.renderReticle(sb);
                Mon3tr m = MonsterPatch.Inst();
                if (m != null)
                    m.renderReticle(sb);
            }
            else if (_inst.hoveredCard.target == Kaltsit.Enums.MON3TR)
            {
                Mon3tr m = MonsterPatch.Inst();
                if (m != null)
                    m.renderReticle(sb);
            }
            */
        }
    }

    @SpirePatch(clz = UseCardAction.class, method = "update")
    public static class TriggerPowerPatch
    {
        @SpirePrefixPatch
        public static void Prefix(UseCardAction _inst)
        {
            float duration = (float) ReflectionHacks.getPrivateInherited(_inst, UseCardAction.class, "duration");
            AbstractCard targetCard = (AbstractCard) ReflectionHacks.getPrivate(_inst, UseCardAction.class, "targetCard");
            if (duration == 0.15F)
            {
                for (int j = 0; j < FriendlyPatch.FriendlyFields.FriendlyList.get(AbstractDungeon.player).size(); j++)
                {
                    for (AbstractPower p : FriendlyPatch.FriendlyFields.FriendlyList.get(AbstractDungeon.player).get(j).powers)
                    {
                        if (!targetCard.dontTriggerOnUseCard)
                            p.onAfterUseCard(targetCard, _inst);
                    }
                }
            }
        }
    }
}