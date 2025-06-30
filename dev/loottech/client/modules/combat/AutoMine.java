package dev.loottech.client.modules.combat;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.module.CombatModule;
import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.api.utilities.Animation;
import dev.loottech.api.utilities.BlastResistantBlocks;
import dev.loottech.api.utilities.CacheTimer;
import dev.loottech.api.utilities.EnchantmentUtils;
import dev.loottech.api.utilities.EntityUtil;
import dev.loottech.api.utilities.InventoryUtils;
import dev.loottech.api.utilities.PositionUtil;
import dev.loottech.api.utilities.RotationUtils;
import dev.loottech.api.utilities.Util;
import dev.loottech.api.utilities.render.RenderBuffers;
import dev.loottech.api.utilities.render.RenderManager;
import dev.loottech.client.events.AttackBlockEvent;
import dev.loottech.client.events.PacketReceiveEvent;
import dev.loottech.client.events.PacketSendEvent;
import dev.loottech.client.events.PreTickEvent;
import dev.loottech.client.events.Render3DEvent;
import dev.loottech.client.modules.client.AntiCheat;
import dev.loottech.client.modules.combat.AutoCrystal;
import dev.loottech.client.values.impl.ValueBoolean;
import dev.loottech.client.values.impl.ValueColor;
import dev.loottech.client.values.impl.ValueEnum;
import dev.loottech.client.values.impl.ValueNumber;
import java.awt.Color;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.function.Supplier;
import net.minecraft.class_1268;
import net.minecraft.class_1292;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_1887;
import net.minecraft.class_1893;
import net.minecraft.class_1922;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_259;
import net.minecraft.class_2596;
import net.minecraft.class_2620;
import net.minecraft.class_2626;
import net.minecraft.class_265;
import net.minecraft.class_2680;
import net.minecraft.class_2846;
import net.minecraft.class_2868;
import net.minecraft.class_2879;
import net.minecraft.class_3532;
import net.minecraft.class_3726;
import net.minecraft.class_4050;
import net.minecraft.class_4587;
import net.minecraft.class_5321;
import org.jetbrains.annotations.NotNull;

@RegisterModule(name="AutoMine", tag="AutoMine", description="Automatically mine blocks using packets", category=Module.Category.COMBAT)
public class AutoMine
extends CombatModule {
    private final CacheTimer remineTimer = new CacheTimer();
    private final Queue<MineData> autoMineQueue = new ArrayDeque();
    ValueBoolean multitaskConfig = new ValueBoolean("Multitask", "Allows mining while using items", false);
    ValueBoolean autoConfig = new ValueBoolean("Auto", "Automatically mines nearby players feet", false);
    ValueBoolean avoidSelfConfig = new ValueBoolean("AvoidSelf", "Avoids mining blocks in your surround", false, (Supplier<Boolean>)((Supplier)() -> this.autoConfig.getValue()));
    ValueBoolean strictDirectionConfig = new ValueBoolean("StrictDirection", "Only mines on visible faces", false, (Supplier<Boolean>)((Supplier)() -> this.autoConfig.getValue()));
    ValueNumber enemyRangeConfig = new ValueNumber("EnemyRange", "Range to search for targets", (Number)Float.valueOf((float)5.0f), (Number)Float.valueOf((float)1.0f), (Number)Float.valueOf((float)10.0f), (Supplier<Boolean>)((Supplier)() -> this.autoConfig.getValue()));
    ValueBoolean antiCrawlConfig = new ValueBoolean("AntiCrawl", "Attempts to stop player from crawling", false, (Supplier<Boolean>)((Supplier)() -> this.autoConfig.getValue()));
    ValueBoolean headConfig = new ValueBoolean("TargetBody", "Attempts to mine players face blocks", false, (Supplier<Boolean>)((Supplier)() -> this.autoConfig.getValue()));
    ValueBoolean aboveHeadConfig = new ValueBoolean("TargetHead", "Attempts to mine above players head", false, (Supplier<Boolean>)((Supplier)() -> this.autoConfig.getValue()));
    ValueBoolean doubleBreakConfig = new ValueBoolean("DoubleBreak", "Allows you to mine two blocks at once", false);
    ValueNumber mineTicksConfig = new ValueNumber("MiningTicks", "The max number of ticks to hold a pickaxe for the packet mine", (Number)Integer.valueOf((int)20), (Number)Integer.valueOf((int)5), (Number)Integer.valueOf((int)60));
    ValueEnum remineConfig = new ValueEnum("Remine", "Remines already mined blocks", RemineMode.NORMAL);
    ValueBoolean packetInstantConfig = new ValueBoolean("Fast", "Instant mines faster", false, (Supplier<Boolean>)((Supplier)() -> this.remineConfig.getValue() == RemineMode.INSTANT));
    ValueNumber rangeConfig = new ValueNumber("Range", "The range to mine blocks", (Number)Float.valueOf((float)4.0f), (Number)Float.valueOf((float)0.1f), (Number)Float.valueOf((float)6.0f));
    ValueNumber speedConfig = new ValueNumber("Speed", "The speed to mine blocks", (Number)Float.valueOf((float)1.0f), (Number)Float.valueOf((float)0.1f), (Number)Float.valueOf((float)1.0f));
    ValueEnum swapConfig = new ValueEnum("AutoSwap", "Swaps to the best tool once the mining is complete", Swap.SILENT);
    ValueBoolean swapBeforeConfig = new ValueBoolean("SwapBefore", "Swaps before fully done mining", false);
    ValueBoolean rotateConfig = new ValueBoolean("Rotate", "Rotates when mining the block", true);
    ValueBoolean switchResetConfig = new ValueBoolean("SwitchReset", "Resets mining after switching items", false);
    ValueColor colorConfig = new ValueColor("MineColor", "MineColor", "The mine render color", Color.RED, false, false);
    ValueColor colorDoneConfig = new ValueColor("DoneColor", "DoneColor", "The done render color", Color.GREEN, false, false);
    int fadeTime = 250;
    private class_1657 playerTarget;
    private MineData packetMine;
    private MineData instantMine;
    private boolean packetSwapBack;
    private boolean manualOverride;
    private boolean changedInstantMine;
    private boolean waitForPacketMine;
    private boolean packetMineStuck;
    private boolean antiCrawlOverride;
    private int antiCrawlTicks;
    private int autoMineTickDelay;
    private MineAnimation packetMineAnim = new MineAnimation(MineData.empty(), new Animation(true, 200.0f));
    private MineAnimation instantMineAnim = new MineAnimation(MineData.empty(), new Animation(true, 200.0f));

    public AutoMine() {
        super(900);
    }

    @Override
    public String getHudInfo() {
        if (this.instantMine != null) {
            return String.format((String)"%.1f", (Object[])new Object[]{Float.valueOf((float)Math.min((float)this.instantMine.getBlockDamage(), (float)1.0f))});
        }
        return "";
    }

    @Override
    public void onDisable() {
        this.autoMineQueue.clear();
        this.playerTarget = null;
        this.packetMine = null;
        if (this.instantMine != null) {
            this.abortMining(this.instantMine);
            this.instantMine = null;
        }
        this.packetMineAnim = new MineAnimation(MineData.empty(), new Animation(true, 200.0f));
        this.instantMineAnim = new MineAnimation(MineData.empty(), new Animation(true, 200.0f));
        this.autoMineTickDelay = 0;
        this.antiCrawlTicks = 0;
        this.manualOverride = false;
        this.antiCrawlOverride = false;
        this.waitForPacketMine = false;
        this.packetMineStuck = false;
        if (this.packetSwapBack) {
            Managers.INVENTORY.syncToClient();
            this.packetSwapBack = false;
        }
    }

    @Override
    public void onPreTick(PreTickEvent event) {
        double distance;
        float damageDelta;
        if (AutoMine.mc.field_1724.method_7337()) {
            return;
        }
        class_1657 currentTarget = this.getClosestPlayer(this.enemyRangeConfig.getValue().floatValue());
        boolean targetChanged = this.playerTarget != null && this.playerTarget != currentTarget;
        this.playerTarget = currentTarget;
        if (this.isInstantMineComplete()) {
            if (this.changedInstantMine) {
                this.changedInstantMine = false;
            }
            if (this.waitForPacketMine) {
                this.waitForPacketMine = false;
            }
        }
        --this.autoMineTickDelay;
        --this.antiCrawlTicks;
        if (this.packetMine != null && this.packetMine.getTicksMining() > this.mineTicksConfig.getValue().intValue()) {
            this.packetMineStuck = true;
            this.packetMineAnim.animation.setState(false);
            if (this.packetSwapBack) {
                Managers.INVENTORY.syncToClient();
                this.packetSwapBack = false;
            }
            this.packetMine = null;
            if (!this.isInstantMineComplete()) {
                this.waitForPacketMine = true;
            }
        }
        if (this.packetMine != null) {
            damageDelta = this.calcBlockBreakingDelta(this.packetMine.getState(), (class_1922)AutoMine.mc.field_1687, this.packetMine.getPos());
            this.packetMine.addBlockDamage(damageDelta);
            int slot = this.packetMine.getBestSlot();
            float damageDone = this.packetMine.getBlockDamage() + (this.swapBeforeConfig.getValue() || this.packetMineStuck ? damageDelta : 0.0f);
            if (damageDone >= 1.0f && slot != -1 && !this.checkMultitask()) {
                Managers.INVENTORY.setSlot(slot);
                this.packetSwapBack = true;
                if (this.packetMineStuck) {
                    this.packetMineStuck = false;
                }
            }
        }
        if (this.packetSwapBack) {
            if (this.packetMine != null && this.canMine(this.packetMine.getState())) {
                this.packetMine.markAttemptedMine();
            } else {
                Managers.INVENTORY.syncToClient();
                this.packetSwapBack = false;
                this.packetMineAnim.animation.setState(false);
                this.packetMine = null;
                if (!this.isInstantMineComplete()) {
                    this.waitForPacketMine = true;
                }
            }
        }
        if (this.instantMine != null && ((distance = AutoMine.mc.field_1724.method_33571().method_1022(this.instantMine.getPos().method_46558())) > (double)this.rangeConfig.getValue().floatValue() || this.instantMine.getTicksMining() > this.mineTicksConfig.getValue().intValue())) {
            this.abortMining(this.instantMine);
            this.instantMineAnim.animation.setState(false);
            this.instantMine = null;
        }
        if (this.instantMine != null) {
            damageDelta = this.calcBlockBreakingDelta(this.instantMine.getState(), (class_1922)AutoMine.mc.field_1687, this.instantMine.getPos());
            this.instantMine.addBlockDamage(damageDelta);
            if (this.instantMine.getBlockDamage() >= this.speedConfig.getValue().floatValue()) {
                boolean passedRemine;
                boolean canMine = this.canMine(this.instantMine.getState());
                boolean canPlace = AutoMine.mc.field_1687.method_8628(this.instantMine.getState(), this.instantMine.getPos(), class_3726.method_16194());
                if (canMine) {
                    this.instantMine.markAttemptedMine();
                } else {
                    this.instantMine.resetMiningTicks();
                    if (this.remineConfig.getValue() == RemineMode.NORMAL || this.remineConfig.getValue() == RemineMode.FAST) {
                        this.instantMine.setTotalBlockDamage(0.0f, 0.0f);
                    }
                    if (this.manualOverride) {
                        this.manualOverride = false;
                        this.abortMining(this.instantMine);
                        this.instantMineAnim.animation.setState(false);
                        this.instantMine = null;
                    }
                }
                boolean bl = passedRemine = this.remineConfig.getValue() == RemineMode.INSTANT || this.remineTimer.passed((Number)Integer.valueOf((int)500));
                if (this.instantMine != null && (this.remineConfig.getValue() == RemineMode.INSTANT && this.packetInstantConfig.getValue() && this.packetMine == null && canPlace || canMine && passedRemine) && (!this.checkMultitask() || this.multitaskConfig.getValue() || this.swapConfig.getValue() == Swap.OFF) && !AutoMine.mc.field_1687.method_8320(this.instantMine.pos).method_26215()) {
                    this.stopMining(this.instantMine);
                    this.remineTimer.reset();
                    if (Managers.MODULE.getInstance(AutoCrystal.class).isEnabled() && Managers.MODULE.getInstance(AutoCrystal.class).shouldPreForcePlace()) {
                        Managers.MODULE.getInstance(AutoCrystal.class).placeCrystalForTarget(this.playerTarget, this.instantMine.getPos().method_10074());
                    }
                    if (this.remineConfig.getValue() == RemineMode.FAST) {
                        this.startMining(this.instantMine);
                    }
                }
            }
        }
        if (this.manualOverride && (this.instantMine == null || this.instantMine.getGoal() != MiningGoal.MANUAL)) {
            this.manualOverride = false;
        }
        if (this.antiCrawlOverride && (this.instantMine == null || this.instantMine.getGoal() != MiningGoal.PREVENT_CRAWL)) {
            this.antiCrawlOverride = false;
        }
        if (this.autoConfig.getValue()) {
            MineData nextMine;
            if (!this.autoMineQueue.isEmpty() && this.autoMineTickDelay <= 0 && (nextMine = (MineData)this.autoMineQueue.poll()) != null) {
                this.startMining(nextMine);
                this.autoMineTickDelay = 5;
            }
            class_2338 antiCrawlPos = this.getAntiCrawlPos(this.playerTarget);
            if (this.antiCrawlOverride) {
                if (AutoMine.mc.field_1724.method_18376().equals((Object)class_4050.field_18079)) {
                    this.antiCrawlTicks = 10;
                }
                if (this.antiCrawlTicks <= 0 || !this.isInstantMineComplete() && antiCrawlPos != null && !this.instantMine.getPos().equals((Object)antiCrawlPos)) {
                    this.antiCrawlOverride = false;
                }
            }
            if (this.autoMineQueue.isEmpty() && !this.manualOverride && !this.antiCrawlOverride) {
                if (this.antiCrawlConfig.getValue() && AutoMine.mc.field_1724.method_18376().equals((Object)class_4050.field_18079) && antiCrawlPos != null) {
                    MineData data = new MineData(antiCrawlPos, this.strictDirectionConfig.getValue() ? Managers.INTERACTION.getInteractDirection(antiCrawlPos, false) : class_2350.field_11036, MiningGoal.PREVENT_CRAWL);
                    if (this.isInstantMineComplete() || !this.instantMine.equals(data)) {
                        this.startAutoMine(data);
                        this.antiCrawlOverride = true;
                    }
                } else if (this.playerTarget != null && !targetChanged) {
                    boolean bedrockPhased;
                    class_2338 targetPos = EntityUtil.getRoundedBlockPos((class_1297)this.playerTarget);
                    boolean bl = bedrockPhased = PositionUtil.isBedrock(this.playerTarget.method_5829(), targetPos) && !this.playerTarget.method_20448();
                    if (!this.isInstantMineComplete() && this.checkDataY(this.instantMine, targetPos, bedrockPhased)) {
                        this.abortMining(this.instantMine);
                        this.instantMineAnim.animation.setState(false);
                        this.instantMine = null;
                    } else if (this.packetMine != null && this.checkDataY(this.packetMine, targetPos, bedrockPhased)) {
                        this.packetMineAnim.animation.setState(false);
                        if (this.packetSwapBack) {
                            Managers.INVENTORY.syncToClient();
                            this.packetSwapBack = false;
                        }
                        this.packetMine = null;
                        this.waitForPacketMine = false;
                    } else {
                        List<class_2338> phasedBlocks = this.getPhaseBlocks(this.playerTarget, targetPos, bedrockPhased);
                        if (!phasedBlocks.isEmpty()) {
                            class_2338 pos1 = (class_2338)phasedBlocks.removeFirst();
                            MineData bestMine = new MineData(pos1, this.strictDirectionConfig.getValue() ? Managers.INTERACTION.getInteractDirection(pos1, false) : class_2350.field_11036);
                            if (this.packetMine == null && this.doubleBreakConfig.getValue() || this.isInstantMineComplete()) {
                                this.startAutoMine(bestMine);
                            }
                        } else {
                            List<class_2338> miningBlocks = this.getMiningBlocks(this.playerTarget, targetPos, bedrockPhased);
                            MineData bestMine = this.getInstantMine(miningBlocks, bedrockPhased);
                            if (bestMine != null && (this.packetMine == null && !this.changedInstantMine && this.doubleBreakConfig.getValue() || this.isInstantMineComplete())) {
                                this.startAutoMine(bestMine);
                            }
                        }
                    }
                } else {
                    if (!this.isInstantMineComplete() && this.instantMine.getGoal() == MiningGoal.MINING_ENEMY) {
                        this.abortMining(this.instantMine);
                        this.instantMineAnim.animation.setState(false);
                        this.instantMine = null;
                    }
                    if (this.packetMine != null && this.packetMine.getGoal() == MiningGoal.MINING_ENEMY) {
                        this.packetMineAnim.animation.setState(false);
                        if (this.packetSwapBack) {
                            Managers.INVENTORY.syncToClient();
                            this.packetSwapBack = false;
                        }
                        this.packetMine = null;
                        this.waitForPacketMine = false;
                    }
                }
            }
        }
    }

    @Override
    public void onAttackBlock(AttackBlockEvent event) {
        if (AutoMine.mc.field_1724.method_7337() || AutoMine.mc.field_1724.method_7325()) {
            return;
        }
        event.cancel();
        if (AutoMine.mc.field_1687.method_8320(event.getPos()).method_26214((class_1922)AutoMine.mc.field_1687, event.getPos()) == -1.0f || !this.canMine(AutoMine.mc.field_1687.method_8320(event.getPos())) || this.isMining(event.getPos()) || Managers.MODULE.getInstance(AntiCheat.class).grimCCFix.getValue() && Managers.NETWORK.isCrystalPvpCC() && event.getPos().method_10264() > 100) {
            return;
        }
        MineData data = new MineData(event.getPos(), event.getDirection(), MiningGoal.MANUAL);
        if (this.instantMine != null && this.instantMine.getGoal() == MiningGoal.MINING_ENEMY || this.packetMine != null && this.packetMine.getGoal() == MiningGoal.MINING_ENEMY) {
            this.manualOverride = true;
        }
        if (!this.doubleBreakConfig.getValue()) {
            this.instantMine = data;
            this.startMining(this.instantMine);
            AutoMine.mc.field_1724.method_23667(class_1268.field_5808, false);
            return;
        }
        boolean updateChanged = false;
        if (!this.isInstantMineComplete() && !this.changedInstantMine) {
            if (this.packetMine == null) {
                this.packetMine = this.instantMine.copy();
                this.packetMineAnim = new MineAnimation(this.packetMine, new Animation(true, this.fadeTime));
            } else {
                updateChanged = true;
            }
        }
        this.instantMine = data;
        this.startMining(this.instantMine);
        AutoMine.mc.field_1724.method_23667(class_1268.field_5808, false);
        if (updateChanged) {
            this.changedInstantMine = true;
        }
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacket() instanceof class_2868 && this.switchResetConfig.getValue() && this.instantMine != null) {
            this.instantMine.setTotalBlockDamage(0.0f, 0.0f);
        }
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        class_2626 packet;
        class_2596<?> class_25962 = event.getPacket();
        if (class_25962 instanceof class_2626 && this.canMine((packet = (class_2626)class_25962).method_11308()) && this.antiCrawlOverride && packet.method_11309().equals((Object)this.getAntiCrawlPos(this.playerTarget))) {
            this.antiCrawlTicks = 10;
        }
        if (this.antiCrawlConfig.getValue() && (class_25962 = event.getPacket()) instanceof class_2620) {
            packet = (class_2620)class_25962;
            this.handleAntiCrawlBlockBreaking((class_2620)packet);
        }
    }

    private void handleAntiCrawlBlockBreaking(class_2620 packet) {
        class_2680 headState;
        if (AutoMine.mc.field_1724 == null || AutoMine.mc.field_1687 == null) {
            return;
        }
        class_2338 playerPos = EntityUtil.getRoundedBlockPos((class_1297)AutoMine.mc.field_1724);
        class_2338 breakingPos = packet.method_11277();
        class_2338 feetPos = playerPos;
        class_2338 headPos = playerPos.method_10084();
        if (breakingPos.equals((Object)feetPos) && this.isInTwoBlockSpace() && this.isAutoMineBlock((headState = AutoMine.mc.field_1687.method_8320(headPos)).method_26204()) && this.canMine(headState)) {
            class_2350 direction = Managers.INTERACTION.getInteractDirection(headPos, this.strictDirectionConfig.getValue());
            MineData headMineData = new MineData(headPos, direction, MiningGoal.PREVENT_CRAWL);
            if (this.instantMine != null && this.instantMine.getGoal() != MiningGoal.PREVENT_CRAWL) {
                this.abortMining(this.instantMine);
            }
            this.startAutoMine(headMineData);
            this.antiCrawlTicks = 10;
        }
    }

    private boolean isInTwoBlockSpace() {
        class_2338 playerPos;
        if (AutoMine.mc.field_1724 == null || AutoMine.mc.field_1687 == null) {
            return false;
        }
        class_2338 feetPos = playerPos = EntityUtil.getRoundedBlockPos((class_1297)AutoMine.mc.field_1724);
        class_2338 headPos = playerPos.method_10084();
        class_2680 feetState = AutoMine.mc.field_1687.method_8320(feetPos);
        class_2680 headState = AutoMine.mc.field_1687.method_8320(headPos);
        boolean feetSolid = !feetState.method_26215() && feetState.method_51366();
        boolean headSolid = !headState.method_26215() && headState.method_51366();
        return feetSolid && headSolid;
    }

    public void startAutoMine(MineData data) {
        if (!this.canMine(data.getState()) || this.isMining(data.getPos())) {
            return;
        }
        if (!this.doubleBreakConfig.getValue()) {
            this.instantMine = data;
            this.autoMineQueue.offer((Object)data);
            return;
        }
        if (this.changedInstantMine && !this.isInstantMineComplete() || this.waitForPacketMine) {
            return;
        }
        boolean updateChanged = false;
        if (!this.isInstantMineComplete() && !this.changedInstantMine) {
            if (this.packetMine == null) {
                this.packetMine = this.instantMine.copy();
                this.packetMineAnim = new MineAnimation(this.packetMine, new Animation(true, this.fadeTime));
            } else {
                updateChanged = true;
            }
        }
        this.instantMine = data;
        this.autoMineQueue.offer((Object)data);
        if (updateChanged) {
            this.changedInstantMine = true;
        }
    }

    public MineData getInstantMine(List<class_2338> miningBlocks, boolean bedrockPhased) {
        PriorityQueue validInstantMines = new PriorityQueue();
        for (class_2338 blockPos : miningBlocks) {
            double dist;
            class_2680 state1 = AutoMine.mc.field_1687.method_8320(blockPos);
            if (!this.isAutoMineBlock(state1.method_26204()) || (dist = AutoMine.mc.field_1724.method_33571().method_1022(blockPos.method_46558())) > (double)this.rangeConfig.getValue().floatValue()) continue;
            class_2680 state2 = AutoMine.mc.field_1687.method_8320(blockPos.method_10074());
            if (!bedrockPhased && !state2.method_27852(class_2246.field_10540) && !state2.method_27852(class_2246.field_9987)) continue;
            class_2350 direction = this.strictDirectionConfig.getValue() ? Managers.INTERACTION.getInteractDirection(blockPos, true) : class_2350.field_11036;
            validInstantMines.add((Object)new MineData(blockPos, direction));
        }
        if (validInstantMines.isEmpty()) {
            return null;
        }
        return (MineData)validInstantMines.peek();
    }

    public List<class_2338> getPhaseBlocks(class_1657 player, class_2338 playerPos, boolean targetBedrockPhased) {
        List<class_2338> phaseBlocks = PositionUtil.getAllInBox(player.method_5829(), targetBedrockPhased && this.headConfig.getValue() ? playerPos.method_10084() : playerPos);
        phaseBlocks.removeIf(p -> {
            class_2680 state = AutoMine.mc.field_1687.method_8320(p);
            if (!this.isAutoMineBlock(state.method_26204()) || !this.canMine(state) || this.isMining((class_2338)p)) {
                return true;
            }
            double dist = AutoMine.mc.field_1724.method_33571().method_1022(p.method_46558());
            if (dist > (double)this.rangeConfig.getValue().floatValue()) {
                return true;
            }
            return this.avoidSelfConfig.getValue() && this.intersectsPlayer((class_2338)p);
        });
        if (targetBedrockPhased && this.aboveHeadConfig.getValue()) {
            phaseBlocks.add((Object)playerPos.method_10086(2));
        }
        return phaseBlocks;
    }

    public List<class_2338> getMiningBlocks(class_1657 player, class_2338 playerPos, boolean bedrockPhased) {
        ArrayList miningBlocks;
        ArrayList surroundingBlocks = this.getSurroundNoDown(player, this.rangeConfig.getValue().floatValue());
        if (bedrockPhased) {
            class_2680 belowFeet;
            ArrayList facePlaceBlocks = new ArrayList();
            if (this.headConfig.getValue()) {
                facePlaceBlocks.addAll((Collection)surroundingBlocks.stream().map(class_2338::method_10084).toList());
            }
            if (this.canMine(belowFeet = AutoMine.mc.field_1687.method_8320(playerPos.method_10074()))) {
                facePlaceBlocks.add((Object)playerPos.method_10074());
            }
            miningBlocks = facePlaceBlocks;
        } else {
            miningBlocks = surroundingBlocks;
        }
        miningBlocks.removeIf(p -> this.avoidSelfConfig.getValue() && this.intersectsPlayer((class_2338)p));
        return miningBlocks;
    }

    private class_2338 getAntiCrawlPos(class_1657 playerTarget) {
        boolean playerBelow;
        if (!AutoMine.mc.field_1724.method_24828()) {
            return null;
        }
        class_2338 crawlingPos = EntityUtil.getRoundedBlockPos((class_1297)AutoMine.mc.field_1724);
        boolean bl = playerBelow = playerTarget != null && EntityUtil.getRoundedBlockPos((class_1297)playerTarget).method_10264() < crawlingPos.method_10264();
        if (playerBelow) {
            class_2680 state = AutoMine.mc.field_1687.method_8320(crawlingPos.method_10074());
            if (this.isAutoMineBlock(state.method_26204()) && this.canMine(state)) {
                return crawlingPos.method_10074();
            }
        } else {
            class_2680 state = AutoMine.mc.field_1687.method_8320(crawlingPos.method_10084());
            if (this.isAutoMineBlock(state.method_26204()) && this.canMine(state)) {
                return crawlingPos.method_10084();
            }
        }
        return null;
    }

    private boolean checkDataY(MineData data, class_2338 targetPos, boolean bedrockPhased) {
        return data.getGoal() == MiningGoal.MINING_ENEMY && !bedrockPhased && data.getPos().method_10264() != targetPos.method_10264();
    }

    private boolean intersectsPlayer(class_2338 pos) {
        List<class_2338> playerBlocks = this.getPlayerBlocks((class_1657)AutoMine.mc.field_1724);
        List<class_2338> surroundingBlocks = this.getSurroundNoDown((class_1657)AutoMine.mc.field_1724);
        return playerBlocks.contains((Object)pos) || surroundingBlocks.contains((Object)pos);
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (AutoMine.mc.field_1724.method_7337() || AutoMine.mc.field_1724.method_7325()) {
            return;
        }
        RenderBuffers.preRender();
        if (this.instantMineAnim != null && this.instantMineAnim.animation().getFactor() > (double)0.01f) {
            this.renderMiningData(event.getMatrices(), event.getTickDelta(), this.instantMineAnim, true);
        }
        if (this.doubleBreakConfig.getValue() && this.packetMineAnim != null && this.packetMineAnim.animation().getFactor() > (double)0.01f) {
            this.renderMiningData(event.getMatrices(), event.getTickDelta(), this.packetMineAnim, false);
        }
        RenderBuffers.postRender();
    }

    public void renderMiningData(class_4587 matrixStack, float tickDelta, MineAnimation mineAnimation, boolean instantMine) {
        MineData data = mineAnimation.data();
        Animation animation = mineAnimation.animation();
        int boxAlpha = (int)(40.0 * animation.getFactor());
        int lineAlpha = (int)(100.0 * animation.getFactor());
        int boxColor = data.getBlockDamage() >= 0.95f || !this.canMine(data.getState()) ? this.colorDoneConfig.getValue(boxAlpha).getRGB() : this.colorConfig.getValue(boxAlpha).getRGB();
        int lineColor = data.getBlockDamage() >= 0.95f || !this.canMine(data.getState()) ? this.colorDoneConfig.getValue(lineAlpha).getRGB() : this.colorConfig.getValue(lineAlpha).getRGB();
        class_2338 mining = data.getPos();
        class_265 outlineShape = class_259.method_1077();
        if (!instantMine || data.getBlockDamage() < this.speedConfig.getValue().floatValue()) {
            outlineShape = data.getState().method_26218((class_1922)AutoMine.mc.field_1687, mining);
            outlineShape = outlineShape.method_1110() ? class_259.method_1077() : outlineShape;
        }
        class_238 render1 = outlineShape.method_1107();
        class_243 center = render1.method_996(mining).method_1005();
        float total = instantMine ? this.speedConfig.getValue().floatValue() : 1.0f;
        float scale = instantMine && data.getBlockDamage() >= this.speedConfig.getValue().floatValue() || !this.canMine(data.getState()) ? 1.0f : class_3532.method_15363((float)((data.getBlockDamage() + (data.getBlockDamage() - data.getLastDamage()) * tickDelta) / total), (float)0.0f, (float)1.0f);
        double dx = (render1.field_1320 - render1.field_1323) / 2.0;
        double dy = (render1.field_1325 - render1.field_1322) / 2.0;
        double dz = (render1.field_1324 - render1.field_1321) / 2.0;
        class_238 scaled = new class_238(center, center).method_1009(dx * (double)scale, dy * (double)scale, dz * (double)scale);
        RenderManager.renderBox(matrixStack, scaled, boxColor);
        RenderManager.renderBoundingBox(matrixStack, scaled, 1.5f, lineColor);
    }

    public void startMining(MineData data) {
        if (this.rotateConfig.getValue()) {
            float[] rotations = RotationUtils.getRotations(data.getPos().method_46558());
            this.setRotationSilent(rotations[0], rotations[1]);
        }
        if (this.doubleBreakConfig.getValue()) {
            Managers.NETWORK.sendPacket((class_2596<?>)new class_2846(class_2846.class_2847.field_12973, data.getPos(), data.getDirection(), AutoMine.mc.field_1687.method_41925().method_41937().method_41942()));
            Managers.NETWORK.sendPacket((class_2596<?>)new class_2846(class_2846.class_2847.field_12968, data.getPos(), data.getDirection(), AutoMine.mc.field_1687.method_41925().method_41937().method_41942()));
            Managers.NETWORK.sendPacket((class_2596<?>)new class_2846(class_2846.class_2847.field_12971, data.getPos(), data.getDirection(), AutoMine.mc.field_1687.method_41925().method_41937().method_41942()));
        } else {
            Managers.NETWORK.sendPacket((class_2596<?>)new class_2846(class_2846.class_2847.field_12968, data.getPos(), data.getDirection()));
        }
        Managers.NETWORK.sendPacket((class_2596<?>)new class_2846(class_2846.class_2847.field_12973, data.getPos(), data.getDirection()));
        Managers.NETWORK.sendPacket((class_2596<?>)new class_2879(class_1268.field_5808));
        Managers.NETWORK.sendPacket((class_2596<?>)new class_2879(class_1268.field_5808));
        Managers.NETWORK.sendPacket((class_2596<?>)new class_2879(class_1268.field_5808));
        if (this.rotateConfig.getValue()) {
            Managers.ROTATION.setRotationSilentSync();
        }
        this.instantMineAnim = new MineAnimation(data, new Animation(true, this.fadeTime));
    }

    public void abortMining(MineData data) {
        Managers.NETWORK.sendPacket((class_2596<?>)new class_2846(class_2846.class_2847.field_12971, data.getPos(), data.getDirection(), AutoMine.mc.field_1687.method_41925().method_41937().method_41942()));
    }

    public void stopMining(MineData data) {
        int slot;
        if (this.rotateConfig.getValue()) {
            float[] rotations = RotationUtils.getRotations(data.getPos().method_46558());
            this.setRotationSilent(rotations[0], rotations[1]);
        }
        if ((slot = data.getBestSlot()) != -1) {
            this.swapTo(slot);
        }
        this.stopMiningInternal(data);
        if (slot != -1) {
            this.swapSync(slot);
        }
        if (this.rotateConfig.getValue()) {
            Managers.ROTATION.setRotationSilentSync();
        }
    }

    private void stopMiningInternal(MineData data) {
        Managers.NETWORK.sendPacket((class_2596<?>)new class_2846(class_2846.class_2847.field_12973, data.getPos(), data.getDirection(), AutoMine.mc.field_1687.method_41925().method_41937().method_41942()));
        Managers.NETWORK.sendPacket((class_2596<?>)new class_2846(class_2846.class_2847.field_12973, data.getPos(), data.getDirection(), AutoMine.mc.field_1687.method_41925().method_41937().method_41942()));
    }

    public boolean isInstantMineComplete() {
        return this.instantMine == null || this.instantMine.getBlockDamage() >= this.speedConfig.getValue().floatValue() && !this.canMine(this.instantMine.getState());
    }

    public class_2338 getMiningBlock() {
        double damage;
        if (this.instantMine != null && (damage = (double)(this.instantMine.getBlockDamage() / this.speedConfig.getValue().floatValue())) > 0.75) {
            return this.instantMine.getPos();
        }
        return null;
    }

    /*
     * Exception decompiling
     */
    private void swapTo(int slot) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter$TooOptimisticMatchException
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * Exception decompiling
     */
    private void swapSync(int slot) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter$TooOptimisticMatchException
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public boolean isSilentSwapping() {
        return this.packetSwapBack;
    }

    private boolean isMining(class_2338 blockPos) {
        return this.instantMine != null && this.instantMine.getPos().equals((Object)blockPos) || this.packetMine != null && this.packetMine.getPos().equals((Object)blockPos);
    }

    private boolean isAutoMineBlock(class_2248 block) {
        return !BlastResistantBlocks.isUnbreakable(block);
    }

    public boolean canMine(class_2680 state) {
        return !state.method_26215() && state.method_26227().method_15769();
    }

    public float calcBlockBreakingDelta(class_2680 state, class_1922 world, class_2338 pos) {
        if (this.swapConfig.getValue() == Swap.OFF) {
            return state.method_26165((class_1657)AutoMine.mc.field_1724, (class_1922)AutoMine.mc.field_1687, pos);
        }
        float f = state.method_26214(world, pos);
        if (f == -1.0f) {
            return 0.0f;
        }
        int i = this.canHarvest(state) ? 30 : 100;
        return this.getBlockBreakingSpeed(state) / f / (float)i;
    }

    private boolean canHarvest(class_2680 state) {
        if (state.method_29291()) {
            int tool = InventoryUtils.getBestTool(state);
            return AutoMine.mc.field_1724.method_31548().method_5438(tool).method_7951(state);
        }
        return true;
    }

    public List<class_2338> getSurroundNoDown(class_1657 player) {
        return this.getSurroundNoDown(player, 0.0f);
    }

    public List<class_2338> getSurroundNoDown(class_1657 player, float range) {
        HashSet surroundBlocks = new HashSet();
        HashSet playerBlocks = new HashSet(this.getPlayerBlocks(player));
        for (class_2338 pos : playerBlocks) {
            if (range > 0.0f && AutoMine.mc.field_1724.method_33571().method_1025(pos.method_46558()) > (double)(range * range)) continue;
            for (class_2350 dir : class_2350.values()) {
                class_2338 pos1;
                if (!dir.method_10166().method_10179() || playerBlocks.contains((Object)(pos1 = pos.method_10093(dir)))) continue;
                surroundBlocks.add((Object)pos1);
            }
        }
        return new ArrayList((Collection)surroundBlocks);
    }

    public List<class_2338> getPlayerBlocks(class_1657 entity) {
        class_2338 playerPos = PositionUtil.getRoundedBlockPos(entity.method_23317(), entity.method_23318(), entity.method_23321());
        ArrayList playerBlocks = new ArrayList();
        playerBlocks.addAll(PositionUtil.getAllInBox(entity.method_5829(), playerPos));
        return playerBlocks;
    }

    private float getBlockBreakingSpeed(class_2680 block) {
        class_1799 stack;
        int i;
        int tool = InventoryUtils.getBestTool(block);
        float f = AutoMine.mc.field_1724.method_31548().method_5438(tool).method_7924(block);
        if (f > 1.0f && (i = EnchantmentUtils.getLevel(stack = AutoMine.mc.field_1724.method_31548().method_5438(tool), (class_5321<class_1887>)class_1893.field_9131)) > 0 && !stack.method_7960()) {
            f += (float)(i * i + 1);
        }
        if (class_1292.method_5576((class_1309)AutoMine.mc.field_1724)) {
            f *= 1.0f + (float)(class_1292.method_5575((class_1309)AutoMine.mc.field_1724) + 1) * 0.2f;
        }
        if (AutoMine.mc.field_1724.method_6059(class_1294.field_5901)) {
            float g = switch (AutoMine.mc.field_1724.method_6112(class_1294.field_5901).method_5578()) {
                case 0 -> 0.3f;
                case 1 -> 0.09f;
                case 2 -> 0.0027f;
                default -> 8.1E-4f;
            };
            f *= g;
        }
        if (!AutoMine.mc.field_1724.method_24828()) {
            f /= 5.0f;
        }
        return f;
    }

    public static enum RemineMode {
        INSTANT,
        NORMAL,
        FAST;

    }

    public static enum Swap {
        NORMAL,
        SILENT,
        SILENT_ALT,
        OFF;

    }

    public record MineAnimation(MineData data, Animation animation) {
    }

    public static class MineData
    implements Comparable<MineData> {
        private final class_2338 pos;
        private final class_2350 direction;
        private final MiningGoal goal;
        private int ticksMining;
        private float blockDamage;
        private float lastDamage;

        public MineData(class_2338 pos, class_2350 direction) {
            this.pos = pos;
            this.direction = direction;
            this.goal = MiningGoal.MINING_ENEMY;
        }

        public MineData(class_2338 pos, class_2350 direction, MiningGoal goal) {
            this.pos = pos;
            this.direction = direction;
            this.goal = goal;
        }

        public static MineData empty() {
            return new MineData(class_2338.field_10980, class_2350.field_11036);
        }

        private double getPriority() {
            double dist = Util.mc.field_1724.method_33571().method_1025(this.pos.method_10074().method_46558());
            if (dist <= (double)Managers.MODULE.getInstance(AutoCrystal.class).getPlaceRange()) {
                return 10.0;
            }
            return 0.0;
        }

        public int compareTo(@NotNull MineData o) {
            return Double.compare((double)this.getPriority(), (double)o.getPriority());
        }

        public boolean equals(Object obj) {
            MineData d;
            return obj instanceof MineData && (d = (MineData)obj).getPos().equals((Object)this.pos);
        }

        public void resetMiningTicks() {
            this.ticksMining = 0;
        }

        public void markAttemptedMine() {
            ++this.ticksMining;
        }

        public void addBlockDamage(float blockDamage) {
            this.lastDamage = this.blockDamage;
            this.blockDamage += blockDamage;
        }

        public void setTotalBlockDamage(float blockDamage, float lastDamage) {
            this.blockDamage = blockDamage;
            this.lastDamage = lastDamage;
        }

        public class_2338 getPos() {
            return this.pos;
        }

        public class_2350 getDirection() {
            return this.direction;
        }

        public MiningGoal getGoal() {
            return this.goal;
        }

        public int getTicksMining() {
            return this.ticksMining;
        }

        public float getBlockDamage() {
            return this.blockDamage;
        }

        public float getLastDamage() {
            return this.lastDamage;
        }

        public MineData copy() {
            MineData data = new MineData(this.pos, this.direction, this.goal);
            data.setTotalBlockDamage(this.blockDamage, this.lastDamage);
            return data;
        }

        public class_2680 getState() {
            return Util.mc.field_1687.method_8320(this.pos);
        }

        public int getBestSlot() {
            return InventoryUtils.getBestToolNoFallback(this.getState());
        }

        public int compareTo(@NotNull Object object) {
            return this.compareTo((MineData)object);
        }
    }

    public static enum MiningGoal {
        MANUAL,
        MINING_ENEMY,
        PREVENT_CRAWL;

    }

    public static enum Selection {
        WHITELIST,
        BLACKLIST,
        ALL;

    }
}
