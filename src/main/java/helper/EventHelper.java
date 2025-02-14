package helper;

import actions.SelectFoodAction;
import basemod.BaseMod;
import basemod.abstracts.CustomSavable;
import basemod.interfaces.*;
import cards.AbstractMizukiCard;
import cards.Curses.ConcentrationDisorder;
import cards.Curses.HemopoieticInhibition;
import cards.Curses.MetastaticAberration;
import cards.Curses.Neurodegeneration;
import characters.Mizuki;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import modcore.MizukiModCore;
import patches.CramUpDisplayCards;
import powers.StunMonsterPower;

import java.util.ArrayList;
import java.util.UUID;

public class EventHelper implements
        PostUpdateSubscriber, PreUpdateSubscriber, PreMonsterTurnSubscriber, OnPlayerTurnStartSubscriber, OnCardUseSubscriber,
        CustomSavable<Integer>, PostRenderSubscriber, PostBattleSubscriber, PostDungeonInitializeSubscriber
{
    public static ArrayList<AbstractRelic> relicList = new ArrayList<>();
    public static ArrayList<Integer> applyStunnedWhenTurnStart = new ArrayList<>();
    public static ArrayList<AbstractCreature> applyStunnedWhenTurnStartTargets = new ArrayList<>();
    public static final ArrayList<AbstractCard> showCards = new ArrayList<>();
    public static boolean PlayedATCInTurn;
    public static boolean isPlayerTurn;
    ArrayList<Boolean> previousEnemiesAliveState = new ArrayList<>();
    ArrayList<Boolean> currentEnemiesAliveState = new ArrayList<>();
    public static EventHelper Inst = new EventHelper();
    public int losthealthinturn = 0;
    public int changedhealthinturn = 0;
    public int delta = 0;
    public static int satietyDegree = 0;
    public int previoushealth = 0;
    public int currenthealth = 0;
    public static int poetryGelUsedTimes = 2;
    public static int rejectionDealingDamageTime;
    public int count = 0;
    public static final ArrayList<AbstractCard> CurrentCardsInHand = new ArrayList<>();
    public static final ArrayList<AbstractCard> NextCardsInHand = new ArrayList<>();
    public static final ArrayList<OnLearnSubscriber> ON_LEARN_SUBSCRIBERS = new ArrayList<>();
    public static final ArrayList<OnGetCardInHandSubscriber> ON_CARDINTOHAND_SUBSCRIBERS = new ArrayList<>();
    public static final ArrayList<OnHealthChangedSubscriber> ON_HEALTHCHANGED_SUBSCRIBERS = new ArrayList<>();
    public static final ArrayList<OnEnemyDeathSubscriber> ON_ENEMYDEATH_SUBSCRIBERS = new ArrayList<>();
    public static final ArrayList<OnEnemyAppearSubscriber> ON_ENEMYAPPEAR_SUBSCRIBERS = new ArrayList<>();
    public static final ArrayList<OnPostBattleStartSubscriber> ON_POSTBATTLESTART_SUBSCRIBERS = new ArrayList<>();
    public static boolean hasPlayedVoice;
    public static ArrayList<AbstractMonster> monstersList = new ArrayList<>();
    public static MusicHelper musicHelper = new MusicHelper();

    public EventHelper()
    {
        BaseMod.subscribe((ISubscriber) this);
        losthealthinturn = 0;
        changedhealthinturn = 0;
    }

    public boolean receivePreMonsterTurn(AbstractMonster abstractMonster)
    {
        MizukiModCore.logger.info("restoring");
        isPlayerTurn = false;
        ImpairementManager.restoreCreatureImpairments(abstractMonster);
        return true;
    }
    private static <T> void AddToList(ArrayList<T> list, CustomSubscriber sub, Class<T> clazz)
    {
        if (clazz.isInstance(sub))
            list.add(clazz.cast(sub));
    }
    private static <T> void RemoveFromList(ArrayList<T> list, CustomSubscriber sub, Class<T> clazz)
    {
        if (clazz.isInstance(sub) && list.contains(clazz.cast(sub)))
            list.remove(clazz.cast(sub));
    }
    public static void receiveOnBattleStart(AbstractRoom abstractRoom)
    {
        ON_LEARN_SUBSCRIBERS.clear();
        ON_CARDINTOHAND_SUBSCRIBERS.clear();
        ON_HEALTHCHANGED_SUBSCRIBERS.clear();
        ON_ENEMYDEATH_SUBSCRIBERS.clear();
        ON_ENEMYAPPEAR_SUBSCRIBERS.clear();
        ON_POSTBATTLESTART_SUBSCRIBERS.clear();
        for (AbstractCard c : AbstractDungeon.player.drawPile.group)
        {
            if (c instanceof AbstractMizukiCard)
                Subscribe((AbstractMizukiCard)c);
        }
        for (AbstractCard c : AbstractDungeon.player.hand.group)
        {
            if (c instanceof AbstractMizukiCard)
                Subscribe((AbstractMizukiCard)c);
        }
        for (AbstractCard c : AbstractDungeon.player.discardPile.group)
        {
            if (c instanceof AbstractMizukiCard)
                Subscribe((AbstractMizukiCard)c);
        }
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group)
        {
            if (c instanceof AbstractMizukiCard)
                Subscribe((AbstractMizukiCard)c);
        }

        ImpairementManager.corrosionImpairementDamage = 20;
        ImpairementManager.nervousImpairementDamage = 20;
        ImpairementManager.nervousImpairementTimes = 1;
        if (ImpairementManager.tempOncenNrvousImpairementDamage == null)
        {
            ImpairementManager.tempOncenNrvousImpairementDamage = new ArrayList<>();
        }
        if (ImpairementManager.tempOnceNervousImpairementTimes == null)
        {
            ImpairementManager.tempOnceNervousImpairementTimes = new ArrayList<>();
        }
        ImpairementManager.tempOncenNrvousImpairementDamage.clear();
        ImpairementManager.tempOnceNervousImpairementTimes.clear();
        rejectionDealingDamageTime = 0;

        //进入战斗时，随机播放水月的语音，每当播放过以后，会有一次冷却
        if (AbstractDungeon.player instanceof Mizuki)
        {
            if (!Mizuki.hasPlayedVoice)
            {
                if (hasPlayedVoice)
                {
                    hasPlayedVoice = false;
                }
                else
                {
                    int roll = AbstractDungeon.aiRng.random(99);
                    if (roll < 30)
                    {
                        CardCrawlGame.sound.play("MIZUKI_ENTERBATTLE1", 0.00F);
                        hasPlayedVoice = true;
                    }
                    else if (roll < 60)
                    {
                        CardCrawlGame.sound.play("MIZUKI_ENTERBATTLE2", 0.00F);
                        hasPlayedVoice = true;
                    }
                }
            }
        }

        EventHelper.ON_POSTBATTLESTART_SUBSCRIBERS.forEach(sub -> sub.OnPostBattleStart(AbstractDungeon.getCurrRoom()));

        monstersList.clear();
        monstersList.addAll((AbstractDungeon.getCurrRoom()).monsters.monsters);

        //饱腹度清空
        satietyDegree = 0;
        //打开食物选取界面
        if (CookingHelper.hasFoodInDeck())
        {
            AbstractDungeon.actionManager.addToTop(new SelectFoodAction());
        }
    }

    public void receivePostRender(SpriteBatch sb)
    {
        for (int i = 0;i < showCards.size();i++)
        {
            if (CramUpDisplayCards.card != null)
            {
                showCards.get(i).target_x = CramUpDisplayCards.card.current_x - (AbstractCard.IMG_WIDTH * 0.7f * (showCards.size() - 1)) / 2 + AbstractCard.IMG_WIDTH * i * 0.7f;
                showCards.get(i).target_y = CramUpDisplayCards.card.current_y + AbstractCard.IMG_HEIGHT;
                showCards.get(i).render(sb);
            }
        }/*
        for (AbstractCard card : showCards)
        {
            if (CramUpDisplayCards.card != null)
            {
                card.target_x = CramUpDisplayCards.card.current_x;
                card.target_y = CramUpDisplayCards.card.current_y + AbstractCard.IMG_HEIGHT;
                card.render(sb);
            }
        }*/
    }


    public static void Subscribe(CustomSubscriber sub)
    {
        AddToList(ON_LEARN_SUBSCRIBERS, sub, OnLearnSubscriber.class);
        AddToList(ON_CARDINTOHAND_SUBSCRIBERS, sub, OnGetCardInHandSubscriber.class);
        AddToList(ON_HEALTHCHANGED_SUBSCRIBERS, sub, OnHealthChangedSubscriber.class);
        AddToList(ON_ENEMYDEATH_SUBSCRIBERS, sub, OnEnemyDeathSubscriber.class);
        AddToList(ON_ENEMYAPPEAR_SUBSCRIBERS, sub, OnEnemyAppearSubscriber.class);
        AddToList(ON_POSTBATTLESTART_SUBSCRIBERS, sub, OnPostBattleStartSubscriber.class);
    }

    public static void Unsubscribe(CustomSubscriber sub)
    {
        RemoveFromList(ON_LEARN_SUBSCRIBERS, sub, OnLearnSubscriber.class);
        RemoveFromList(ON_CARDINTOHAND_SUBSCRIBERS, sub, OnGetCardInHandSubscriber.class);
        RemoveFromList(ON_HEALTHCHANGED_SUBSCRIBERS, sub, OnHealthChangedSubscriber.class);
        RemoveFromList(ON_ENEMYDEATH_SUBSCRIBERS, sub, OnEnemyDeathSubscriber.class);
        RemoveFromList(ON_ENEMYAPPEAR_SUBSCRIBERS, sub, OnEnemyAppearSubscriber.class);
        RemoveFromList(ON_POSTBATTLESTART_SUBSCRIBERS, sub, OnPostBattleStartSubscriber.class);
    }

    @Override
    public void receivePostDungeonInitialize()
    {
        MizukiModCore.logger.info("初始化");
        CookingHelper.gridScreenForCooking = false;
        CookingHelper.confirmScreenForCooking = false;
    }

    public static interface OnLearnSubscriber extends CustomSubscriber
    {
        void OnLearn(UUID uuid);
    }

    public static interface OnGetCardInHandSubscriber extends CustomSubscriber
    {
        void OnGetCardInHand(AbstractCard card);
    }

    public static interface OnHealthChangedSubscriber extends CustomSubscriber
    {
        void OnHealthChanged(int delta);
    }
    public static interface OnEnemyDeathSubscriber extends CustomSubscriber
    {
        void OnEnemyDeath(AbstractMonster monster);
    }
    public static interface OnEnemyAppearSubscriber extends CustomSubscriber
    {
        void OnEnemyAppear(AbstractMonster monster);
    }
    public static interface OnPostBattleStartSubscriber extends CustomSubscriber
    {
        void OnPostBattleStart(AbstractRoom room);
    }

    @Override
    public void receiveOnPlayerTurnStart()
    {
        if (!applyStunnedWhenTurnStartTargets.isEmpty())
        {
            for (int i = 0;i < EventHelper.applyStunnedWhenTurnStartTargets.size();i++)
            {
                AbstractCreature c = EventHelper.applyStunnedWhenTurnStartTargets.get(i);
                int count = EventHelper.applyStunnedWhenTurnStart.get(i);
                AbstractDungeon.actionManager.addToBottom(
                        (AbstractGameAction) new ApplyPowerAction((AbstractCreature) c, null,
                                (AbstractPower) new StunMonsterPower(c, count), count, true, AbstractGameAction.AttackEffect.SMASH));
            }
            applyStunnedWhenTurnStartTargets.clear();
            applyStunnedWhenTurnStart.clear();
        }
        losthealthinturn = 0;
        changedhealthinturn = 0;
        PlayedATCInTurn = false;
        isPlayerTurn = true;
        ImpairementManager.restoreCreatureImpairments(AbstractDungeon.player);
    }

    @Override
    public void receiveCardUsed(AbstractCard abstractCard)
    {
        if (abstractCard.type == AbstractCard.CardType.ATTACK)
        {
            PlayedATCInTurn = true;
        }
    }

    public void receivePostUpdate()
    {
        PostRelicGained();

        musicHelper.update();

        for (AbstractCard card : showCards)
            card.update();

        if (AbstractDungeon.isPlayerInDungeon() && AbstractDungeon.player != null)
        {
            currentEnemiesAliveState.clear();
            if (AbstractDungeon.currMapNode != null)
            {
                if ((AbstractDungeon.getCurrRoom()).monsters != null)
                {
                    //检测怪物变化
                    for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters)
                    {
                        if (!monstersList.contains(m))
                        {
                            EventHelper.ON_ENEMYAPPEAR_SUBSCRIBERS.forEach(sub -> sub.OnEnemyAppear(m));
                            monstersList.add(m);
                        }
                    }
                    /*
                    for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters)
                    {
                        //
                        //储存这一帧更新后的敌人存活状态
                        //
                        currentEnemiesAliveState.add(m.isDying);
                    }*/
                }
            }

            comfirmingEnemiesAliveState();


            //将当前帧更新之后的血量赋值
            currenthealth = AbstractDungeon.player.currentHealth;
            delta = currenthealth - previoushealth;
            if (delta < 0)
            {
                losthealthinturn -= delta;
                changedhealthinturn -= delta;
            }
            if (delta > 0)
            {
                changedhealthinturn += delta;
            }
            if (previoushealth != 0 && previoushealth != currenthealth && ON_HEALTHCHANGED_SUBSCRIBERS.size() != 0)
            {
                //previoushealth != 0 : 防止血量初始化的时候触发
                //previoushealth != currenthealth : 防止相等的时候触发，节省资源
                //ON_HEALTHCHANGED_SUBSCRIBERS.size() != 0 : 防止没有可触发内容的时候尝试触发，节省资源
                //如果生命发生变化，将变化的生命传入检测队列
                MizukiModCore.logger.info("Delta" + delta);
                EventHelper.ON_HEALTHCHANGED_SUBSCRIBERS.forEach(sub -> sub.OnHealthChanged(delta));
            }



            //将当前帧更新之后的手牌加入arraylist
            NextCardsInHand.clear();
            if (count != AbstractDungeon.player.hand.group.size())
            {
                MizukiModCore.logger.info(count + "= >" + AbstractDungeon.player.hand.group.size());
            }
            if (AbstractDungeon.player.hand.group.size() != 0)
            {
                for (int i = 0;i < AbstractDungeon.player.hand.group.size();i++)
                {
                    NextCardsInHand.add(AbstractDungeon.player.hand.group.get(i));
                }
            }
        }
        //检测当前帧更新之前的手牌与更新之后的手牌的差别
        comfirmingCardsInHand();


    }

    public void comfirmingCardsInHand()
    {
        for (int i = 0;i < NextCardsInHand.size();i++)
        {
            boolean exist = false;
            for (int j = 0;j < CurrentCardsInHand.size();j++)
            {
                if (NextCardsInHand.get(i).uuid == CurrentCardsInHand.get(j).uuid)
                {
                    exist = true;
                }
            }
            if (!exist)
            {
                //如果有新的牌进入手牌，传入检测队列
                AbstractCard card = NextCardsInHand.get(i);
                EventHelper.ON_CARDINTOHAND_SUBSCRIBERS.forEach(sub -> sub.OnGetCardInHand(card));
            }
        }
    }

    public void comfirmingEnemiesAliveState()
    {
        /*
        //MizukiModCore.logger.info("previousAlive" + previousEnemiesAliveState.size());
        //MizukiModCore.logger.info("currentAlive" + currentEnemiesAliveState.size());
        for (int i = 0;
             i < currentEnemiesAliveState.size()
             && currentEnemiesAliveState.size() == previousEnemiesAliveState.size();i++)
        {
            if (currentEnemiesAliveState.get(i) != previousEnemiesAliveState.get(i) && currentEnemiesAliveState.get(i))
            {
                int j = i;
                EventHelper.ON_ENEMYDEATH_SUBSCRIBERS.forEach(sub -> sub.OnEnemyDeath((AbstractDungeon.getCurrRoom()).monsters.monsters.get(j), j));
            }
        }*/
    }

    public void receivePreUpdate()
    {
        if (AbstractDungeon.isPlayerInDungeon() && AbstractDungeon.player != null)
        {
            previousEnemiesAliveState.clear();
            if (AbstractDungeon.currMapNode != null)
            {
                if ((AbstractDungeon.getCurrRoom()).monsters != null)
                {
                    for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters)
                    {
                        //
                        //储存这一帧更新前的敌人存活状态
                        //
                        previousEnemiesAliveState.add(m.isDying);
                    }
                }
            }
            previoushealth = AbstractDungeon.player.currentHealth;

            //
            //储存这一帧更新前的敌人存活状态
            //

            //将当前帧更新之前的手牌加入arraylist
            CurrentCardsInHand.clear();
            count = AbstractDungeon.player.hand.group.size();
            if (AbstractDungeon.player.hand.group.size() != 0)
            {
                for (int i = 0;i < AbstractDungeon.player.hand.group.size();i++)
                {
                    CurrentCardsInHand.add(AbstractDungeon.player.hand.group.get(i));
                }
            }
        }
    }
    public static interface CustomSubscriber {}

    @Override
    public Integer onSave()
    {
        return poetryGelUsedTimes;
    }

    @Override
    public void onLoad(Integer integer)
    {
        if (integer == null)
        {
            return;
        }
        poetryGelUsedTimes = integer;
    }

    @Override
    public void receivePostBattle(AbstractRoom abstractRoom)
    {
        ImpairementManager.getCreatureImpairments(AbstractDungeon.player).clear();
        losthealthinturn = 0;
    }

    public AbstractMizukiCard getRandomRejection()
    {
        int roll = MathUtils.random(99);
        AbstractMizukiCard card = null;
        if (roll < 25)
        {
            card = new ConcentrationDisorder();
        }
        else if (roll < 50)
        {
            card = new HemopoieticInhibition();
        }
        else if (roll < 75)
        {
            card = new MetastaticAberration();
        }
        else
        {
            card = new Neurodegeneration();
        }
        return card;
    }


    public void PostRelicGained()
    {
        if (!relicList.isEmpty())
        {
            for (AbstractRelic relic : relicList)
            {
                relic.obtain();
                relic.isObtained = true;
                relic.isAnimating = false;
                relic.isDone = false;
            }
            relicList.clear();
        }
    }
}
