package dev.loottech.client.modules.combat;

import com.google.common.collect.Lists;
import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.module.CombatModule;
import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.api.utilities.Animation;
import dev.loottech.api.utilities.BlastResistantBlocks;
import dev.loottech.api.utilities.CacheTimer;
import dev.loottech.api.utilities.EntityUtil;
import dev.loottech.api.utilities.ExplosionUtil;
import dev.loottech.api.utilities.InventoryUtils;
import dev.loottech.api.utilities.PerSecondCounter;
import dev.loottech.api.utilities.PlayerUtils;
import dev.loottech.api.utilities.RotationUtils;
import dev.loottech.api.utilities.interfaces.EvictingQueue;
import dev.loottech.api.utilities.render.RenderBuffers;
import dev.loottech.api.utilities.render.RenderManager;
import dev.loottech.client.events.AddEntityEvent;
import dev.loottech.client.events.PacketReceiveEvent;
import dev.loottech.client.events.PacketSendEvent;
import dev.loottech.client.events.PlayerTickEvent;
import dev.loottech.client.events.Render3DEvent;
import dev.loottech.client.events.RunTickEvent;
import dev.loottech.client.modules.client.FastLatency;
import dev.loottech.client.modules.client.ModuleColor;
import dev.loottech.client.modules.combat.AutoMine;
import dev.loottech.client.modules.combat.Surround;
import dev.loottech.client.values.Priority;
import dev.loottech.client.values.impl.ValueBoolean;
import dev.loottech.client.values.impl.ValueCategory;
import dev.loottech.client.values.impl.ValueColor;
import dev.loottech.client.values.impl.ValueEnum;
import dev.loottech.client.values.impl.ValueNumber;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.minecraft.class_1268;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1303;
import net.minecraft.class_1309;
import net.minecraft.class_1511;
import net.minecraft.class_1542;
import net.minecraft.class_1657;
import net.minecraft.class_1713;
import net.minecraft.class_1743;
import net.minecraft.class_1774;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1810;
import net.minecraft.class_1829;
import net.minecraft.class_1937;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2374;
import net.minecraft.class_238;
import net.minecraft.class_2382;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_2480;
import net.minecraft.class_2596;
import net.minecraft.class_2604;
import net.minecraft.class_2606;
import net.minecraft.class_2664;
import net.minecraft.class_2680;
import net.minecraft.class_2716;
import net.minecraft.class_2767;
import net.minecraft.class_2824;
import net.minecraft.class_2868;
import net.minecraft.class_2879;
import net.minecraft.class_2885;
import net.minecraft.class_3417;
import net.minecraft.class_3419;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_8042;

@RegisterModule(name="AutoCrystal", tag="AutoCrystal", description="Automatically kill players with end crystals.", category=Module.Category.COMBAT)
public class AutoCrystal
extends CombatModule {
    private static final class_238 FULL_CRYSTAL_BB = new class_238(0.0, 0.0, 0.0, 1.0, 2.0, 1.0);
    private static final class_238 HALF_CRYSTAL_BB = new class_238(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
    private final CacheTimer lastAttackTimer = new CacheTimer();
    private final CacheTimer lastPlaceTimer = new CacheTimer();
    private final CacheTimer lastSwapTimer = new CacheTimer();
    private final CacheTimer autoSwapTimer = new CacheTimer();
    private final Deque<Long> attackLatency = new EvictingQueue(20);
    private final Map<Integer, Long> attackPackets = Collections.synchronizedMap((Map)new ConcurrentHashMap());
    private final Map<class_2338, Long> placePackets = Collections.synchronizedMap((Map)new ConcurrentHashMap());
    private final PerSecondCounter crystalCounter = new PerSecondCounter();
    private final Map<class_2338, Animation> fadeList = new HashMap();
    private final Map<Integer, Integer> antiStuckCrystals = new HashMap();
    private final List<AntiStuckData> stuckCrystals = new CopyOnWriteArrayList();
    private final ExecutorService executor = Executors.newFixedThreadPool((int)2);
    private ValueCategory miscCategory = new ValueCategory("Misc", "");
    ValueBoolean multitaskConfig = new ValueBoolean("Multitask", "Allows mining while using items", this.miscCategory, false);
    ValueBoolean whileMiningConfig = new ValueBoolean("WhileMining", "Allows attacking while mining blocks", this.miscCategory, false);
    ValueNumber targetRangeConfig = new ValueNumber("EnemyRange", "Range to search for potential enemies", this.miscCategory, (Number)Float.valueOf((float)10.0f), (Number)Float.valueOf((float)1.0f), (Number)Float.valueOf((float)13.0f));
    ValueBoolean instantConfig = new ValueBoolean("Instant", "Instantly attacks crystals when they spawn", this.miscCategory, true);
    ValueEnum sequentialConfig = new ValueEnum("Sequential", "Places a crystal after spawn", this.miscCategory, (Enum)Sequential.NONE);
    ValueBoolean raytraceConfig = new ValueBoolean("Raytrace", "Raytrace to crystal position", this.miscCategory, false);
    ValueBoolean swingConfig = new ValueBoolean("Swing", "Swing hand when placing and attacking crystals", this.miscCategory, true);
    ValueBoolean rotateConfig = new ValueBoolean("Rotate", "Rotate to breaking and placing", this.miscCategory, false);
    private ValueCategory placeCategory = new ValueCategory("Place", "");
    ValueBoolean placeConfig = new ValueBoolean("Place", "Places crystals to damage enemies. Place settings will only function if this setting is enabled.", this.placeCategory, true);
    ValueNumber placeSpeedConfig = new ValueNumber("PlaceSpeed", "Speed to place crystals", this.placeCategory, (Number)Float.valueOf((float)20.0f), (Number)Float.valueOf((float)0.1f), (Number)Float.valueOf((float)20.0f));
    ValueNumber placeRangeConfig = new ValueNumber("PlaceRange", "Range to place crystals", this.placeCategory, (Number)Float.valueOf((float)4.0f), (Number)Float.valueOf((float)0.1f), (Number)Float.valueOf((float)6.0f));
    ValueNumber placeWallRangeConfig = new ValueNumber("PlaceWallRange", "Range to place crystals through walls", this.placeCategory, (Number)Float.valueOf((float)4.0f), (Number)Float.valueOf((float)0.1f), (Number)Float.valueOf((float)6.0f));
    ValueBoolean placeRangeEyeConfig = new ValueBoolean("PlaceRangeEye", "Calculates place ranges starting from the eye position of the player", this.placeCategory, false);
    ValueBoolean placeRangeCenterConfig = new ValueBoolean("PlaceRangeCenter", "Calculates place ranges to the center of the block", this.placeCategory, true);
    ValueEnum autoSwapConfig = new ValueEnum("Swap", "Swaps to an end crystal before placing if the player is not holding one", this.placeCategory, (Enum)Swap.OFF);
    ValueBoolean antiSurroundConfig = new ValueBoolean("AntiSurround", "Places crystals on AutoMine targets.", this.placeCategory, false);
    ValueEnum forcePlaceConfig = new ValueEnum("ForcePlace", "Attempts to replace crystals in surrounds", this.placeCategory, (Enum)ForcePlace.NONE);
    boolean breakValidConfig = false;
    ValueBoolean strictDirectionConfig = new ValueBoolean("StrictDirection", "Interacts with only visible directions when placing crystals", this.placeCategory, false);
    ValueEnum placementsConfig = new ValueEnum("Placements", "Version for place criteria, Protocol for 1.12", this.placeCategory, (Enum)Placements.NATIVE);
    private ValueCategory breakCategory = new ValueCategory("Break", "");
    float breakSpeedConfig = 20.0f;
    ValueNumber attackDelayConfig = new ValueNumber("AttackDelay", "Added delays", this.breakCategory, (Number)Float.valueOf((float)0.0f), (Number)Float.valueOf((float)0.0f), (Number)Float.valueOf((float)5.0f));
    ValueNumber attackFactorConfig = new ValueNumber("AttackFactor", "Factor of attack delay", this.breakCategory, (Number)Integer.valueOf((int)0), (Number)Integer.valueOf((int)0), (Number)Integer.valueOf((int)3));
    float attackLimitConfig = 10.0f;
    ValueBoolean breakDelayConfig = new ValueBoolean("BreakDelay", "Uses attack latency to calculate break delays", this.breakCategory, false);
    float breakTimeoutConfig = 3.0f;
    float minTimeoutConfig = 5.0f;
    int ticksExistedConfig = 0;
    ValueNumber breakRangeConfig = new ValueNumber("BreakRange", "Range to break crystals", this.breakCategory, (Number)Float.valueOf((float)4.0f), (Number)Float.valueOf((float)0.1f), (Number)Float.valueOf((float)6.0f));
    ValueNumber maxYOffsetConfig = new ValueNumber("MaxYOffset", "Maximum crystal y-offset difference", this.breakCategory, (Number)Float.valueOf((float)5.0f), (Number)Float.valueOf((float)1.0f), (Number)Float.valueOf((float)10.0f));
    ValueNumber breakWallRangeConfig = new ValueNumber("BreakWallRange", "Range to break crystals through walls", this.breakCategory, (Number)Float.valueOf((float)0.1f), (Number)Float.valueOf((float)4.0f), (Number)Float.valueOf((float)6.0f));
    ValueEnum antiWeaknessConfig = new ValueEnum("AntiWeakness", "Swap to tools before attacking crystals", this.breakCategory, (Enum)Swap.OFF);
    float swapDelayConfig = 0.0f;
    ValueBoolean inhibitConfig = new ValueBoolean("Inhibit", "Prevents excessive attacks", this.breakCategory, true);
    private ValueCategory damageCategory = new ValueCategory("Targeting", "");
    ValueNumber minDamageConfig = new ValueNumber("MinDamage", "Minimum damage required to consider attacking or placing an end crystal", this.damageCategory, (Number)Float.valueOf((float)4.0f), (Number)Float.valueOf((float)1.0f), (Number)Float.valueOf((float)10.0f));
    ValueNumber maxLocalDamageConfig = new ValueNumber("MaxLocalDamage", "The maximum player damage", this.damageCategory, (Number)Float.valueOf((float)12.0f), (Number)Float.valueOf((float)1.0f), (Number)Float.valueOf((float)20.0f));
    ValueBoolean assumeArmorConfig = new ValueBoolean("AssumeBestArmor", "Assumes Prot 0 armor is max armor", this.damageCategory, false);
    ValueBoolean armorBreakerConfig = new ValueBoolean("ArmorBreaker", "Attempts to break enemy armor with crystals", this.damageCategory, true);
    ValueNumber armorScaleConfig = new ValueNumber("ArmorScale", "Armor damage scale before attempting to break enemy armor with crystals", this.damageCategory, (Number)Float.valueOf((float)5.0f), (Number)Float.valueOf((float)1.0f), (Number)Float.valueOf((float)20.0f));
    ValueNumber lethalMultiplier = new ValueNumber("LethalMultiplier", "If we can kill an enemy with this many crystals, disregard damage values", this.damageCategory, (Number)Float.valueOf((float)1.5f), (Number)Float.valueOf((float)0.0f), (Number)Float.valueOf((float)4.0f));
    ValueBoolean safetyConfig = new ValueBoolean("Safety", "Accounts for total player safety when attacking and placing crystals", this.damageCategory, true);
    ValueBoolean safetyOverride = new ValueBoolean("SafetyOverride", "Overrides the safety checks if the crystal will kill an enemy", this.damageCategory, false);
    ValueBoolean blockDestructionConfig = new ValueBoolean("BlockDestruction", "Accounts for explosion block destruction when calculating damages", this.damageCategory, false);
    ValueBoolean selfExtrapolateConfig = new ValueBoolean("SelfExtrapolate", "Accounts for motion when calculating self damage", this.damageCategory, false);
    ValueNumber extrapolateTicksConfig = new ValueNumber("ExtrapolationTicks", "Accounts for motion when calculating enemy positions, not fully accurate.", this.damageCategory, (Number)Integer.valueOf((int)0), (Number)Integer.valueOf((int)0), (Number)Integer.valueOf((int)10));
    ValueBoolean playersConfig = new ValueBoolean("Players", "Target players", this.damageCategory, true);
    ValueBoolean monstersConfig = new ValueBoolean("Monsters", "Target monsters", this.damageCategory, false);
    ValueBoolean neutralsConfig = new ValueBoolean("Neutrals", "Target neutrals", this.damageCategory, false);
    ValueBoolean animalsConfig = new ValueBoolean("Animals", "Target animals", this.damageCategory, false);
    ValueBoolean shulkersConfig = new ValueBoolean("Shulkers", "Target shulker boxes", this.damageCategory, false);
    private ValueCategory renderCategory = new ValueCategory("Render", "");
    ValueBoolean renderConfig = new ValueBoolean("Render", "Renders the current placement", this.renderCategory, true);
    ValueColor fillColor = new ValueColor("Fill", "Fill", "Box fill color", ModuleColor.getColor(30), false, true);
    ValueColor outlineColor = new ValueColor("Line", "Line", "Box line color", ModuleColor.getColor(), false, true);
    int fadeTimeConfig = 250;
    ValueBoolean disableDeathConfig = new ValueBoolean("DisableOnDeath", "Disables during disconnect/death", this.renderCategory, false);
    ValueBoolean debugDamageConfig = new ValueBoolean("Damage", "Renders damage", this.renderCategory, false);
    private DamageData<class_1511> attackCrystal;
    private DamageData<class_2338> placeCrystal;
    private class_2338 renderPos;
    private double renderDamage;
    private class_2338 renderSpawnPos;
    private class_243 crystalRotation;
    private boolean attackRotate;
    private boolean rotated;
    private float[] silentRotations;
    private float calculatePlaceCrystalTime = 0.0f;
    private long predictId;

    public AutoCrystal() {
        super(Priority.HIGH);
    }

    @Override
    public String getHudInfo() {
        return String.format((String)"%dms, %d", (Object[])new Object[]{this.lastAttackTimer.passed((Number)Float.valueOf((float)((20.0f - this.breakSpeedConfig) * 50.0f + 2000.0f))) ? 0 : this.getBreakMs(), this.crystalCounter.getPerSecond()});
    }

    @Override
    public void onDisable() {
        this.renderPos = null;
        this.attackCrystal = null;
        this.placeCrystal = null;
        this.crystalRotation = null;
        this.silentRotations = null;
        this.calculatePlaceCrystalTime = 0.0f;
        this.stuckCrystals.clear();
        this.attackPackets.clear();
        this.antiStuckCrystals.clear();
        this.placePackets.clear();
        this.attackLatency.clear();
        this.fadeList.clear();
        this.setStage("NONE");
    }

    @Override
    public void onLogout() {
        if (this.disableDeathConfig.getValue()) {
            this.disable(false);
        } else {
            this.onDisable();
        }
    }

    @Override
    public void onPlayerUpdate(PlayerTickEvent event) {
        if (AutoCrystal.mc.field_1724.method_7325() || this.isSilentSwap() && Managers.MODULE.getInstance(AutoMine.class).isSilentSwapping()) {
            return;
        }
        for (AntiStuckData d : this.stuckCrystals) {
            double dist = AutoCrystal.mc.field_1724.method_5707(d.pos());
            double diff = d.stuckDist() - dist;
            if (!(diff > 0.5)) continue;
            this.stuckCrystals.remove((Object)d);
        }
        if (AutoCrystal.mc.field_1724.method_6115() && AutoCrystal.mc.field_1724.method_6058() == class_1268.field_5808 || AutoCrystal.mc.field_1690.field_1886.method_1434() || PlayerUtils.isHotbarKeysPressed()) {
            this.autoSwapTimer.reset();
        }
        this.renderPos = null;
        ArrayList entities = Lists.newArrayList((Iterable)AutoCrystal.mc.field_1687.method_18112());
        List<class_2338> blocks = this.getSphere(this.placeRangeEyeConfig.getValue() ? AutoCrystal.mc.field_1724.method_33571() : AutoCrystal.mc.field_1724.method_19538());
        long timePre = System.nanoTime();
        if (this.placeConfig.getValue()) {
            this.placeCrystal = this.calculatePlaceCrystal(blocks, (List<class_1297>)entities);
        }
        this.attackCrystal = this.calculateAttackCrystal((List<class_1297>)entities);
        if (this.attackCrystal == null) {
            class_1511 crystalEntity;
            if (this.placeCrystal != null && (crystalEntity = this.intersectingCrystalCheck(this.placeCrystal.getDamageData())) != null) {
                double self = ExplosionUtil.getDamageTo((class_1297)AutoCrystal.mc.field_1724, crystalEntity.method_19538(), this.blockDestructionConfig.getValue(), this.selfExtrapolateConfig.getValue() ? this.extrapolateTicksConfig.getValue().intValue() : 0, false);
                if (!this.safetyConfig.getValue() || !this.playerDamageCheck(self)) {
                    this.attackCrystal = new DamageData<class_1511>(crystalEntity, this.placeCrystal.getAttackTarget(), this.placeCrystal.getDamage(), self, crystalEntity.method_24515().method_10074(), false);
                }
            }
            this.calculatePlaceCrystalTime = System.nanoTime() - timePre;
        }
        if (this.inhibitConfig.getValue() && this.attackCrystal != null && this.attackPackets.containsKey((Object)this.attackCrystal.getDamageData().method_5628())) {
            float delay;
            if ((double)this.attackDelayConfig.getValue().floatValue() > 0.0) {
                float attackFactor = 50.0f / Math.max((float)1.0f, (float)this.attackFactorConfig.getValue().intValue());
                delay = this.attackDelayConfig.getValue().floatValue() * attackFactor;
            } else {
                delay = 1000.0f - this.breakSpeedConfig * 50.0f;
            }
            this.lastAttackTimer.setDelay((Number)Float.valueOf((float)(delay + 100.0f)));
            this.attackPackets.remove((Object)this.attackCrystal.getDamageData().method_5628());
        }
        float breakDelay = this.getBreakDelay();
        if (this.breakDelayConfig.getValue()) {
            breakDelay = Math.max((float)(this.minTimeoutConfig * 50.0f), (float)((float)this.getBreakMs() + this.breakTimeoutConfig * 50.0f));
        }
        boolean bl = this.attackRotate = this.attackCrystal != null && (double)this.attackDelayConfig.getValue().floatValue() <= 0.0 && this.lastAttackTimer.passed((Number)Float.valueOf((float)breakDelay));
        if (this.attackCrystal != null) {
            this.crystalRotation = ((class_1511)this.attackCrystal.damageData).method_19538();
        } else if (this.placeCrystal != null) {
            this.crystalRotation = ((class_2338)this.placeCrystal.damageData).method_46558().method_1031(0.0, 0.5, 0.0);
        }
        if (this.rotateConfig.getValue() && this.crystalRotation != null && (this.placeCrystal == null || this.canHoldCrystal())) {
            float[] rotations = RotationUtils.getRotations(this.crystalRotation);
            this.rotated = true;
            this.crystalRotation = null;
            this.setRotation(rotations[0], rotations[1]);
        } else {
            this.silentRotations = null;
        }
        if (this.isRotationBlocked() || !this.rotated && this.rotateConfig.getValue()) {
            return;
        }
        class_1268 hand = this.getCrystalHand();
        if (this.attackCrystal != null && this.attackRotate) {
            this.attackCrystal(this.attackCrystal.getDamageData(), hand);
            this.setStage("ATTACKING");
            this.lastAttackTimer.reset();
        }
        boolean placeRotate = this.lastPlaceTimer.passed((Number)Float.valueOf((float)(1000.0f - this.placeSpeedConfig.getValue().floatValue() * 50.0f)));
        if (this.placeCrystal != null) {
            this.renderPos = this.placeCrystal.getDamageData();
            this.renderDamage = this.placeCrystal.getDamage();
            if (placeRotate) {
                this.placeCrystal(this.placeCrystal.getDamageData(), hand);
                this.setStage("PLACING");
                this.lastPlaceTimer.reset();
            }
        }
    }

    @Override
    public void onRunTick(RunTickEvent event) {
        if (AutoCrystal.mc.field_1724 == null) {
            return;
        }
        class_1268 hand = this.getCrystalHand();
        if ((double)this.attackDelayConfig.getValue().floatValue() > 0.0) {
            float attackFactor = 50.0f / Math.max((float)1.0f, (float)this.attackFactorConfig.getValue().floatValue());
            if (this.attackCrystal != null && this.lastAttackTimer.passed((Number)Float.valueOf((float)(this.attackDelayConfig.getValue().floatValue() * attackFactor)))) {
                this.attackCrystal(this.attackCrystal.getDamageData(), hand);
                this.lastAttackTimer.reset();
            }
        }
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (this.renderConfig.getValue()) {
            RenderBuffers.preRender();
            class_2338 renderPos1 = null;
            double factor = 0.0;
            for (Map.Entry set : this.fadeList.entrySet()) {
                if (set.getKey() == this.renderPos) continue;
                if (((Animation)set.getValue()).getFactor() > factor) {
                    renderPos1 = (class_2338)set.getKey();
                    factor = ((Animation)set.getValue()).getFactor();
                }
                ((Animation)set.getValue()).setState(false);
                int boxAlpha = (int)(40.0 * ((Animation)set.getValue()).getFactor());
                int lineAlpha = (int)(100.0 * ((Animation)set.getValue()).getFactor());
                Color boxColor = this.fillColor.getValue(boxAlpha);
                Color lineColor = this.outlineColor.getValue(lineAlpha);
                RenderManager.renderBox(event.getMatrices(), (class_2338)set.getKey(), boxColor.getRGB());
                RenderManager.renderBoundingBox(event.getMatrices(), (class_2338)set.getKey(), 1.5f, lineColor.getRGB());
            }
            if (this.debugDamageConfig.getValue() && renderPos1 != null) {
                RenderManager.renderSign(String.format((String)"%.1f", (Object[])new Object[]{this.renderDamage}), renderPos1.method_46558(), 0.0f, 0.0f, new Color(255, 255, 255, (int)(255.0 * factor)).getRGB());
            }
            RenderBuffers.postRender();
            this.fadeList.entrySet().removeIf(e -> ((Animation)e.getValue()).getFactor() == 0.0);
            if (this.renderPos != null && this.isHoldingCrystal()) {
                Animation animation = new Animation(true, this.fadeTimeConfig);
                this.fadeList.put((Object)this.renderPos, (Object)animation);
            }
        }
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (AutoCrystal.mc.field_1724 == null || AutoCrystal.mc.field_1687 == null) {
            return;
        }
        Iterator iterator = event.getPacket();
        if (iterator instanceof class_8042) {
            class_8042 packet = (class_8042)iterator;
            for (class_2596 packet1 : packet.method_48324()) {
                this.handleServerPackets(packet1);
            }
        } else {
            this.handleServerPackets(event.getPacket());
        }
    }

    private void handleServerPackets(class_2596<?> serverPacket) {
        Long attackTime;
        class_2664 packet;
        if (serverPacket instanceof class_2664) {
            packet = (class_2664)serverPacket;
            for (class_1297 entity : Lists.newArrayList((Iterable)AutoCrystal.mc.field_1687.method_18112())) {
                if (!(entity instanceof class_1511) || !(entity.method_5649(packet.method_11475(), packet.method_11477(), packet.method_11478()) < 144.0)) continue;
                mc.method_40000(() -> AutoCrystal.mc.field_1687.method_2945(entity.method_5628(), class_1297.class_5529.field_26999));
                this.antiStuckCrystals.remove((Object)entity.method_5628());
                attackTime = (Long)this.attackPackets.remove((Object)entity.method_5628());
                if (attackTime == null) continue;
                this.attackLatency.add((Object)(System.currentTimeMillis() - attackTime));
            }
        }
        if (serverPacket instanceof class_2767 && (packet = (class_2767)serverPacket).method_11894().comp_349() == class_3417.field_15152.comp_349() && packet.method_11888() == class_3419.field_15245) {
            for (class_1297 entity : Lists.newArrayList((Iterable)AutoCrystal.mc.field_1687.method_18112())) {
                if (!(entity instanceof class_1511) || !(entity.method_5649(packet.method_11890(), packet.method_11889(), packet.method_11893()) < 144.0)) continue;
                mc.method_40000(() -> AutoCrystal.mc.field_1687.method_2945(entity.method_5628(), class_1297.class_5529.field_26999));
                this.antiStuckCrystals.remove((Object)entity.method_5628());
                attackTime = (Long)this.attackPackets.remove((Object)entity.method_5628());
                if (attackTime == null) continue;
                this.attackLatency.add((Object)(System.currentTimeMillis() - attackTime));
            }
        }
        if (serverPacket instanceof class_2716) {
            packet = (class_2716)serverPacket;
            Iterator iterator = packet.method_36548().iterator();
            while (iterator.hasNext()) {
                int id = (Integer)iterator.next();
                this.antiStuckCrystals.remove((Object)id);
                attackTime = (Long)this.attackPackets.remove((Object)id);
                if (attackTime == null) continue;
                this.attackLatency.add((Object)(System.currentTimeMillis() - attackTime));
            }
        }
        if (serverPacket instanceof class_2606 && (long)(packet = (class_2606)serverPacket).method_11183() > this.predictId) {
            this.predictId = packet.method_11183();
        }
        if (serverPacket instanceof class_2604 && (long)(packet = (class_2604)serverPacket).method_11167() > this.predictId) {
            this.predictId = packet.method_11167();
        }
    }

    @Override
    public void onAddEntity(AddEntityEvent event) {
        class_2338 blockPos;
        class_1297 class_12972 = event.getEntity();
        if (!(class_12972 instanceof class_1511)) {
            return;
        }
        class_1511 crystalEntity = (class_1511)class_12972;
        class_243 crystalPos = crystalEntity.method_19538();
        this.renderSpawnPos = blockPos = class_2338.method_49638((class_2374)crystalPos.method_1031(0.0, -1.0, 0.0));
        Long time = (Long)this.placePackets.remove((Object)blockPos);
        boolean bl = this.attackRotate = time != null;
        if (this.attackRotate) {
            this.crystalCounter.updateCounter();
        }
        if (!this.instantConfig.getValue()) {
            return;
        }
        if (this.attackRotate) {
            class_1268 hand = this.getCrystalHand();
            this.attackInternal(crystalEntity, hand);
            this.setStage("ATTACKING");
            this.lastAttackTimer.reset();
            if (this.sequentialConfig.getValue() == Sequential.NORMAL) {
                this.placeSequentialCrystal(hand);
            }
        }
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (AutoCrystal.mc.field_1724 == null) {
            return;
        }
        if (event.getPacket() instanceof class_2868) {
            this.lastSwapTimer.reset();
        }
    }

    public boolean isAttacking() {
        return this.attackCrystal != null;
    }

    public boolean isPlacing() {
        return this.placeCrystal != null && this.isHoldingCrystal();
    }

    public void attackCrystal(class_1511 entity, class_1268 hand) {
        if (this.attackCheckPre(hand)) {
            return;
        }
        class_1293 weakness = AutoCrystal.mc.field_1724.method_6112(class_1294.field_5911);
        class_1293 strength = AutoCrystal.mc.field_1724.method_6112(class_1294.field_5910);
        if (weakness != null && (strength == null || weakness.method_5578() > strength.method_5578())) {
            int slot = -1;
            for (int i = 0; i < 9; ++i) {
                class_1799 stack = AutoCrystal.mc.field_1724.method_31548().method_5438(i);
                if (stack.method_7960() || !(stack.method_7909() instanceof class_1829) && !(stack.method_7909() instanceof class_1743) && !(stack.method_7909() instanceof class_1810)) continue;
                slot = i;
                break;
            }
            if (slot != -1) {
                boolean canSwap;
                boolean bl = canSwap = slot != Managers.INVENTORY.getServerSlot() && (this.antiWeaknessConfig.getValue() != Swap.NORMAL || this.autoSwapTimer.passed((Number)Integer.valueOf((int)500)));
                if (this.antiWeaknessConfig.getValue() != Swap.OFF && canSwap) {
                    if (this.antiWeaknessConfig.getValue() == Swap.SILENT_ALT) {
                        AutoCrystal.mc.field_1761.method_2906(AutoCrystal.mc.field_1724.field_7498.field_7763, slot + 36, AutoCrystal.mc.field_1724.method_31548().field_7545, class_1713.field_7791, (class_1657)AutoCrystal.mc.field_1724);
                    } else if (this.antiWeaknessConfig.getValue() == Swap.SILENT) {
                        Managers.INVENTORY.setSlot(slot);
                    } else {
                        Managers.INVENTORY.setClientSlot(slot);
                    }
                }
                this.attackInternal(entity, class_1268.field_5808);
                if (canSwap) {
                    if (this.antiWeaknessConfig.getValue() == Swap.SILENT_ALT) {
                        AutoCrystal.mc.field_1761.method_2906(AutoCrystal.mc.field_1724.field_7498.field_7763, slot + 36, AutoCrystal.mc.field_1724.method_31548().field_7545, class_1713.field_7791, (class_1657)AutoCrystal.mc.field_1724);
                    } else if (this.antiWeaknessConfig.getValue() == Swap.SILENT) {
                        Managers.INVENTORY.syncToClient();
                    }
                }
                if (this.sequentialConfig.getValue() == Sequential.STRICT) {
                    this.placeSequentialCrystal(hand);
                }
            }
        } else {
            this.attackInternal(entity, hand);
            if (this.sequentialConfig.getValue() == Sequential.STRICT) {
                this.placeSequentialCrystal(hand);
            }
        }
    }

    private void attackInternal(class_1511 crystalEntity, class_1268 hand) {
        this.attackInternal(crystalEntity.method_5628(), hand);
    }

    private void attackInternal(int crystalEntity, class_1268 hand) {
        hand = hand != null ? hand : class_1268.field_5808;
        class_1511 entity2 = new class_1511((class_1937)AutoCrystal.mc.field_1687, 0.0, 0.0, 0.0);
        entity2.method_5838(crystalEntity);
        class_2824 packet = class_2824.method_34206((class_1297)entity2, (boolean)AutoCrystal.mc.field_1724.method_5715());
        Managers.NETWORK.sendPacket((class_2596<?>)packet);
        if (this.swingConfig.getValue()) {
            AutoCrystal.mc.field_1724.method_6104(hand);
        } else {
            Managers.NETWORK.sendPacket((class_2596<?>)new class_2879(hand));
        }
        this.attackPackets.put((Object)crystalEntity, (Object)System.currentTimeMillis());
        Integer antiStuckCount = (Integer)this.antiStuckCrystals.get((Object)crystalEntity);
        if (antiStuckCount != null) {
            this.antiStuckCrystals.replace((Object)crystalEntity, (Object)(antiStuckCount + 1));
        } else {
            this.antiStuckCrystals.put((Object)crystalEntity, (Object)1);
        }
    }

    private void placeSequentialCrystal(class_1268 hand) {
        int latency;
        if (this.placeCrystal == null) {
            return;
        }
        int n = latency = Managers.MODULE.getInstance(FastLatency.class).isEnabled() ? (int)Managers.MODULE.getInstance(FastLatency.class).getLatency() : Managers.NETWORK.getClientLatency();
        if (!Managers.NETWORK.is2b2t() || latency >= 50) {
            this.placeCrystal(this.placeCrystal.getBlockPos(), hand);
        }
    }

    private void placeCrystal(class_2338 blockPos, class_1268 hand) {
        if (this.isRotationBlocked() || !this.rotated && this.rotateConfig.getValue()) {
            return;
        }
        this.placeCrystal(blockPos, hand, true);
    }

    public void placeCrystal(class_2338 blockPos, class_1268 hand, boolean checkPlacement) {
        if (checkPlacement && this.checkCanUseCrystal()) {
            return;
        }
        class_2350 sidePlace = this.getPlaceDirection(blockPos);
        class_3965 result = new class_3965(blockPos.method_46558(), sidePlace, blockPos, false);
        if (this.autoSwapConfig.getValue() != Swap.OFF && hand != class_1268.field_5810 && this.getCrystalHand() == null) {
            if (this.isSilentSwap() && InventoryUtils.count(class_1802.field_8301) == 0) {
                return;
            }
            int crystalSlot = this.getCrystalSlot();
            if (crystalSlot != -1) {
                boolean canSwap;
                boolean bl = canSwap = crystalSlot != Managers.INVENTORY.getServerSlot() && (this.autoSwapConfig.getValue() != Swap.NORMAL || this.autoSwapTimer.passed((Number)Integer.valueOf((int)500)));
                if (canSwap) {
                    if (this.autoSwapConfig.getValue() == Swap.SILENT_ALT) {
                        AutoCrystal.mc.field_1761.method_2906(AutoCrystal.mc.field_1724.field_7498.field_7763, crystalSlot + 36, AutoCrystal.mc.field_1724.method_31548().field_7545, class_1713.field_7791, (class_1657)AutoCrystal.mc.field_1724);
                    } else if (this.autoSwapConfig.getValue() == Swap.SILENT) {
                        Managers.INVENTORY.setSlot(crystalSlot);
                    } else {
                        Managers.INVENTORY.setClientSlot(crystalSlot);
                    }
                }
                this.placeInternal(result, class_1268.field_5808);
                this.placePackets.put((Object)blockPos, (Object)System.currentTimeMillis());
                if (canSwap) {
                    if (this.autoSwapConfig.getValue() == Swap.SILENT_ALT) {
                        AutoCrystal.mc.field_1761.method_2906(AutoCrystal.mc.field_1724.field_7498.field_7763, crystalSlot + 36, AutoCrystal.mc.field_1724.method_31548().field_7545, class_1713.field_7791, (class_1657)AutoCrystal.mc.field_1724);
                    } else if (this.autoSwapConfig.getValue() == Swap.SILENT) {
                        Managers.INVENTORY.syncToClient();
                    }
                }
            }
        } else if (this.isHoldingCrystal()) {
            this.placeInternal(result, hand);
            this.placePackets.put((Object)blockPos, (Object)System.currentTimeMillis());
        }
    }

    private void placeInternal(class_3965 result, class_1268 hand) {
        if (hand == null) {
            return;
        }
        Managers.NETWORK.sendSequencedPacket(id -> new class_2885(hand, result, id));
        if (this.swingConfig.getValue()) {
            AutoCrystal.mc.field_1724.method_6104(hand);
        } else {
            Managers.NETWORK.sendPacket((class_2596<?>)new class_2879(hand));
        }
    }

    private boolean isSilentSwap() {
        return this.autoSwapConfig.getValue() == Swap.SILENT || this.autoSwapConfig.getValue() == Swap.SILENT_ALT;
    }

    private int getCrystalSlot() {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            class_1799 stack = AutoCrystal.mc.field_1724.method_31548().method_5438(i);
            if (!(stack.method_7909() instanceof class_1774)) continue;
            slot = i;
            break;
        }
        return slot;
    }

    private class_2350 getPlaceDirection(class_2338 blockPos) {
        int x = blockPos.method_10263();
        int y = blockPos.method_10264();
        int z = blockPos.method_10260();
        if (this.strictDirectionConfig.getValue()) {
            if (AutoCrystal.mc.field_1724.method_23318() >= (double)blockPos.method_10264()) {
                return class_2350.field_11036;
            }
            class_3965 result = AutoCrystal.mc.field_1687.method_17742(new class_3959(AutoCrystal.mc.field_1724.method_33571(), new class_243((double)x + 0.5, (double)y + 0.5, (double)z + 0.5), class_3959.class_3960.field_17559, class_3959.class_242.field_1348, (class_1297)AutoCrystal.mc.field_1724));
            if (result != null && result.method_17783() == class_239.class_240.field_1332) {
                return result.method_17780();
            }
        } else {
            if (AutoCrystal.mc.field_1687.method_24794(blockPos)) {
                return class_2350.field_11033;
            }
            class_3965 result = AutoCrystal.mc.field_1687.method_17742(new class_3959(AutoCrystal.mc.field_1724.method_33571(), new class_243((double)x + 0.5, (double)y + 0.5, (double)z + 0.5), class_3959.class_3960.field_17559, class_3959.class_242.field_1348, (class_1297)AutoCrystal.mc.field_1724));
            if (result != null && result.method_17783() == class_239.class_240.field_1332) {
                return result.method_17780();
            }
        }
        return class_2350.field_11036;
    }

    private DamageData<class_1511> calculateAttackCrystal(List<class_1297> entities) {
        if (entities.isEmpty()) {
            return null;
        }
        ArrayList validData = new ArrayList();
        DamageData<class_1511> data = null;
        for (class_1297 crystal : entities) {
            boolean attacked;
            if (!(crystal instanceof class_1511)) continue;
            class_1511 crystal1 = (class_1511)crystal;
            if (!crystal.method_5805() || this.stuckCrystals.stream().anyMatch(d -> d.id() == crystal.method_5628())) continue;
            Long time = (Long)this.attackPackets.get((Object)crystal.method_5628());
            boolean bl = attacked = time != null && time < (long)this.getBreakMs();
            if ((crystal.field_6012 < this.ticksExistedConfig || attacked) && this.inhibitConfig.getValue() || this.attackRangeCheck(crystal1)) continue;
            double selfDamage = ExplosionUtil.getDamageTo((class_1297)AutoCrystal.mc.field_1724, crystal.method_19538(), this.blockDestructionConfig.getValue(), this.selfExtrapolateConfig.getValue() ? this.extrapolateTicksConfig.getValue().intValue() : 0, false);
            boolean unsafeToPlayer = this.playerDamageCheck(selfDamage);
            if (unsafeToPlayer && !this.safetyOverride.getValue()) continue;
            for (class_1297 entity : entities) {
                double damage;
                class_1657 player;
                double dist;
                double crystalDist;
                if (entity == null || !entity.method_5805() || entity == AutoCrystal.mc.field_1724 || !this.isValidTarget(entity) || Managers.FRIEND.isFriend(entity.method_5820()) || (crystalDist = crystal.method_5858(entity)) > 144.0 || (dist = AutoCrystal.mc.field_1724.method_5858(entity)) > (double)(this.targetRangeConfig.getValue().floatValue() * this.targetRangeConfig.getValue().floatValue())) continue;
                boolean antiSurround = false;
                if (this.antiSurroundConfig.getValue() && entity instanceof class_1657 && !BlastResistantBlocks.isUnbreakable((player = (class_1657)entity).method_24515())) {
                    HashSet miningPositions = new HashSet();
                    class_2338 miningBlock = Managers.MODULE.getInstance(AutoMine.class).getMiningBlock();
                    if (Managers.MODULE.getInstance(AutoMine.class).isEnabled() && miningBlock != null) {
                        miningPositions.add((Object)miningBlock);
                    }
                    if (Managers.BLOCK.getMines(0.75f).contains((Object)player.method_24515().method_10084())) {
                        miningPositions.add((Object)player.method_24515().method_10084());
                    }
                    for (class_2338 miningBlockPos : miningPositions) {
                        if (!Managers.MODULE.getInstance(Surround.class).getSurroundNoDown(player).contains((Object)miningBlockPos)) continue;
                        for (class_2350 direction : class_2350.values()) {
                            class_2338 pos1 = miningBlockPos.method_10093(direction);
                            if (!crystal.method_24515().equals((Object)pos1.method_10074())) continue;
                            antiSurround = true;
                        }
                    }
                }
                if (this.checkOverrideSafety(unsafeToPlayer, damage = ExplosionUtil.getDamageTo(entity, crystal.method_19538(), this.blockDestructionConfig.getValue(), this.extrapolateTicksConfig.getValue().intValue(), this.assumeArmorConfig.getValue()), entity)) continue;
                DamageData<class_1511> currentData = new DamageData<class_1511>(crystal1, entity, damage, selfDamage, crystal1.method_24515().method_10074(), antiSurround);
                validData.add(currentData);
                if (data != null && !(damage > data.getDamage())) continue;
                data = currentData;
            }
        }
        if (data == null || this.targetDamageCheck(data)) {
            if (this.antiSurroundConfig.getValue()) {
                return (DamageData)validData.stream().filter(DamageData::isAntiSurround).min(Comparator.comparingDouble(d -> AutoCrystal.mc.field_1724.method_5707(d.getBlockPos().method_46558()))).orElse(null);
            }
            return null;
        }
        return data;
    }

    private boolean attackRangeCheck(class_1511 entity) {
        return this.attackRangeCheck(entity.method_19538());
    }

    private boolean attackRangeCheck(class_243 entityPos) {
        double breakRange = this.breakRangeConfig.getValue().floatValue();
        double breakWallRange = this.breakWallRangeConfig.getValue().floatValue();
        class_243 playerPos = AutoCrystal.mc.field_1724.method_33571();
        double dist = playerPos.method_1022(entityPos);
        if (dist > breakRange) {
            return true;
        }
        double yOff = Math.abs((double)(entityPos.method_10214() - AutoCrystal.mc.field_1724.method_23318()));
        if (yOff > (double)this.maxYOffsetConfig.getValue().floatValue()) {
            return true;
        }
        class_3965 result = AutoCrystal.mc.field_1687.method_17742(new class_3959(playerPos, entityPos, class_3959.class_3960.field_17558, class_3959.class_242.field_1348, (class_1297)AutoCrystal.mc.field_1724));
        return result.method_17783() != class_239.class_240.field_1333 && dist > breakWallRange * breakWallRange;
    }

    private DamageData<class_2338> calculatePlaceCrystal(List<class_2338> placeBlocks, List<class_1297> entities) {
        if (placeBlocks.isEmpty() || entities.isEmpty()) {
            return null;
        }
        ArrayList validData = new ArrayList();
        DamageData data = null;
        for (class_2338 pos : placeBlocks) {
            if (!this.canUseCrystalOnBlock(pos) || this.placeRangeCheck(pos) || this.intersectingAntiStuckCheck(pos)) continue;
            double selfDamage = ExplosionUtil.getDamageTo((class_1297)AutoCrystal.mc.field_1724, this.crystalDamageVec(pos), this.blockDestructionConfig.getValue(), this.selfExtrapolateConfig.getValue() ? this.extrapolateTicksConfig.getValue().intValue() : 0, false);
            boolean unsafeToPlayer = this.playerDamageCheck(selfDamage);
            if (unsafeToPlayer && !this.safetyOverride.getValue()) continue;
            for (class_1297 entity : entities) {
                double damage;
                class_1657 player;
                double dist;
                double blockDist;
                if (entity == null || !entity.method_5805() || entity == AutoCrystal.mc.field_1724 || !this.isValidTarget(entity) || Managers.FRIEND.isFriend(entity.method_5820()) || (blockDist = pos.method_19770((class_2374)entity.method_19538())) > 144.0 || (dist = AutoCrystal.mc.field_1724.method_5858(entity)) > (double)(this.targetRangeConfig.getValue().floatValue() * this.targetRangeConfig.getValue().floatValue())) continue;
                boolean antiSurround = false;
                if (this.antiSurroundConfig.getValue() && entity instanceof class_1657 && !BlastResistantBlocks.isUnbreakable((player = (class_1657)entity).method_24515())) {
                    HashSet miningPositions = new HashSet();
                    class_2338 miningBlock = Managers.MODULE.getInstance(AutoMine.class).getMiningBlock();
                    if (Managers.MODULE.getInstance(AutoMine.class).isEnabled() && miningBlock != null) {
                        miningPositions.add((Object)miningBlock);
                    }
                    if (Managers.BLOCK.getMines(0.75f).contains((Object)player.method_24515().method_10084())) {
                        miningPositions.add((Object)player.method_24515().method_10084());
                    }
                    for (class_2338 miningBlockPos : miningPositions) {
                        if (!Managers.MODULE.getInstance(Surround.class).getSurroundNoDown(player).contains((Object)miningBlockPos)) continue;
                        for (class_2350 direction : class_2350.values()) {
                            class_2338 pos1 = miningBlockPos.method_10093(direction);
                            if (!pos.equals((Object)pos1.method_10074())) continue;
                            antiSurround = true;
                        }
                    }
                }
                if (this.checkOverrideSafety(unsafeToPlayer, damage = ExplosionUtil.getDamageTo(entity, this.crystalDamageVec(pos), this.blockDestructionConfig.getValue(), this.extrapolateTicksConfig.getValue().intValue(), this.assumeArmorConfig.getValue()), entity)) continue;
                DamageData currentData = new DamageData(pos, entity, damage, selfDamage, antiSurround);
                validData.add(currentData);
                if (data != null && !(damage > data.getDamage())) continue;
                data = currentData;
            }
        }
        if (data == null || this.targetDamageCheck(data)) {
            if (this.antiSurroundConfig.getValue()) {
                return (DamageData)validData.stream().filter(DamageData::isAntiSurround).min(Comparator.comparingDouble(d -> AutoCrystal.mc.field_1724.method_5707(d.getBlockPos().method_46558()))).orElse(null);
            }
            return null;
        }
        return data;
    }

    private boolean placeRangeCheck(class_2338 pos) {
        double dist;
        double placeRange = this.placeRangeConfig.getValue().floatValue();
        double placeWallRange = this.placeWallRangeConfig.getValue().floatValue();
        class_243 player = this.placeRangeEyeConfig.getValue() ? AutoCrystal.mc.field_1724.method_33571() : AutoCrystal.mc.field_1724.method_19538();
        double d = dist = this.placeRangeCenterConfig.getValue() ? player.method_1025(pos.method_46558()) : pos.method_40081(player.field_1352, player.field_1351, player.field_1350);
        if (dist > placeRange * placeRange) {
            return true;
        }
        class_243 raytrace = class_243.method_24954((class_2382)pos).method_1031(0.5, (double)2.7f, 0.5);
        class_3965 result = AutoCrystal.mc.field_1687.method_17742(new class_3959(AutoCrystal.mc.field_1724.method_33571(), raytrace, class_3959.class_3960.field_17558, class_3959.class_242.field_1348, (class_1297)AutoCrystal.mc.field_1724));
        float maxDist = this.breakRangeConfig.getValue().floatValue() * this.breakRangeConfig.getValue().floatValue();
        if (result != null && result.method_17783() == class_239.class_240.field_1332 && !result.method_17777().equals((Object)pos)) {
            maxDist = this.breakWallRangeConfig.getValue().floatValue() * this.breakWallRangeConfig.getValue().floatValue();
            if (!this.raytraceConfig.getValue() || dist > placeWallRange * placeWallRange) {
                return true;
            }
        }
        return this.breakValidConfig && dist > (double)maxDist;
    }

    public void placeCrystalForTarget(class_1657 target, class_2338 blockPos) {
        if (target == null || target.method_29504() || this.placeRangeCheck(blockPos) || !this.canUseCrystalOnBlock(blockPos)) {
            return;
        }
        double selfDamage = ExplosionUtil.getDamageTo((class_1297)AutoCrystal.mc.field_1724, this.crystalDamageVec(blockPos), this.blockDestructionConfig.getValue(), (Set<class_2338>)Set.of((Object)blockPos), this.selfExtrapolateConfig.getValue() ? this.extrapolateTicksConfig.getValue().intValue() : 0, false);
        if (this.playerDamageCheck(selfDamage)) {
            return;
        }
        double damage = ExplosionUtil.getDamageTo((class_1297)target, this.crystalDamageVec(blockPos), this.blockDestructionConfig.getValue(), (Set<class_2338>)Set.of((Object)blockPos), this.extrapolateTicksConfig.getValue().intValue(), this.assumeArmorConfig.getValue());
        if (damage < (double)this.minDamageConfig.getValue().floatValue() && !this.isCrystalLethalTo(damage, (class_1309)target) || this.placeCrystal != null && this.placeCrystal.getDamage() >= damage) {
            return;
        }
        float[] rotations = RotationUtils.getRotations(blockPos.method_46558());
        this.setRotation(rotations[0], rotations[1]);
        this.placeCrystal(blockPos, class_1268.field_5808, false);
        this.fadeList.put((Object)blockPos, (Object)new Animation(true, this.fadeTimeConfig));
    }

    private boolean checkOverrideSafety(boolean unsafeToPlayer, double damage, class_1297 entity) {
        return this.safetyOverride.getValue() && unsafeToPlayer && damage < (double)EntityUtil.getHealth(entity) + 0.5;
    }

    private boolean targetDamageCheck(DamageData<?> crystal) {
        class_1309 entity;
        double minDmg = this.minDamageConfig.getValue().floatValue();
        class_1297 class_12972 = crystal.getAttackTarget();
        if (class_12972 instanceof class_1309 && this.isCrystalLethalTo(crystal, entity = (class_1309)class_12972)) {
            minDmg = 2.0;
        }
        return crystal.getDamage() < minDmg;
    }

    private boolean playerDamageCheck(double playerDamage) {
        if (!AutoCrystal.mc.field_1724.method_7337()) {
            float health = AutoCrystal.mc.field_1724.method_6032() + AutoCrystal.mc.field_1724.method_6067();
            if (this.safetyConfig.getValue() && playerDamage >= (double)(health + 0.5f)) {
                return true;
            }
            return playerDamage > (double)this.maxLocalDamageConfig.getValue().floatValue();
        }
        return false;
    }

    private boolean isFeetSurrounded(class_1309 entity) {
        class_2338 pos1 = entity.method_24515();
        if (!AutoCrystal.mc.field_1687.method_8320(pos1).method_45474()) {
            return true;
        }
        for (class_2350 direction : class_2350.values()) {
            class_2338 pos2;
            if (!direction.method_10166().method_10179() || !AutoCrystal.mc.field_1687.method_8320(pos2 = pos1.method_10093(direction)).method_45474()) continue;
            return false;
        }
        return true;
    }

    private boolean checkAntiTotem(double damage, class_1309 entity) {
        long time;
        class_1657 p;
        float phealth;
        if (entity instanceof class_1657 && (phealth = EntityUtil.getHealth((class_1297)(p = (class_1657)entity))) <= 2.0f && (double)phealth - damage < 0.5 && (time = Managers.TOTEM.getLastPopTime((class_1297)p)) != -1L) {
            return System.currentTimeMillis() - time <= 500L;
        }
        return false;
    }

    private boolean isCrystalLethalTo(DamageData<?> crystal, class_1309 entity) {
        return this.isCrystalLethalTo(crystal.getDamage(), entity);
    }

    private boolean isCrystalLethalTo(double damage, class_1309 entity) {
        float health = entity.method_6032() + entity.method_6067();
        if (damage * (double)(1.0f + this.lethalMultiplier.getValue().floatValue()) >= (double)(health + 0.5f)) {
            return true;
        }
        if (this.armorBreakerConfig.getValue()) {
            for (class_1799 armorStack : entity.method_5661()) {
                int n = armorStack.method_7919();
                int n1 = armorStack.method_7936();
                float durability = (float)(n1 - n) / (float)n1 * 100.0f;
                if (!(durability < this.armorScaleConfig.getValue().floatValue())) continue;
                return true;
            }
        }
        if (this.shulkersConfig.getValue() && entity instanceof class_1657) {
            for (class_2338 pos : this.getSphere(3.0, entity.method_19538())) {
                class_2680 state = AutoCrystal.mc.field_1687.method_8320(pos);
                if (!(state.method_26204() instanceof class_2480)) continue;
                return true;
            }
        }
        return false;
    }

    private boolean attackCheckPre(class_1268 hand) {
        if (!this.lastSwapTimer.passed((Number)Float.valueOf((float)(this.swapDelayConfig * 25.0f)))) {
            return true;
        }
        if (hand == class_1268.field_5808) {
            return this.checkCanUseCrystal();
        }
        return false;
    }

    private boolean checkCanUseCrystal() {
        return !this.multitaskConfig.getValue() && this.checkMultitask() || !this.whileMiningConfig.getValue() && AutoCrystal.mc.field_1761.method_2923();
    }

    private boolean isHoldingCrystal() {
        if (!(this.checkCanUseCrystal() || this.autoSwapConfig.getValue() != Swap.SILENT && this.autoSwapConfig.getValue() != Swap.SILENT_ALT)) {
            return true;
        }
        return this.getCrystalHand() != null;
    }

    private class_243 crystalDamageVec(class_2338 pos) {
        return class_243.method_24954((class_2382)pos).method_1031(0.5, 1.0, 0.5);
    }

    private boolean isValidTarget(class_1297 e) {
        return e instanceof class_1657 && this.playersConfig.getValue() || EntityUtil.isMonster(e) && this.monstersConfig.getValue() || EntityUtil.isNeutral(e) && this.neutralsConfig.getValue() || EntityUtil.isPassive(e) && this.animalsConfig.getValue();
    }

    public boolean canUseCrystalOnBlock(class_2338 pos) {
        class_2680 state = AutoCrystal.mc.field_1687.method_8320(pos);
        if (!state.method_27852(class_2246.field_10540) && !state.method_27852(class_2246.field_9987)) {
            return false;
        }
        return this.isCrystalHitboxClear(pos);
    }

    public boolean isCrystalHitboxClear(class_2338 pos) {
        class_2338 p2 = pos.method_10084();
        class_2680 state2 = AutoCrystal.mc.field_1687.method_8320(p2);
        if (this.placementsConfig.getValue() == Placements.PROTOCOL && !AutoCrystal.mc.field_1687.method_22347(p2.method_10084())) {
            return false;
        }
        if (!AutoCrystal.mc.field_1687.method_22347(p2) && !state2.method_27852(class_2246.field_10036)) {
            return false;
        }
        class_238 bb = Managers.NETWORK.isCrystalPvpCC() ? HALF_CRYSTAL_BB : FULL_CRYSTAL_BB;
        double d = p2.method_10263();
        double e = p2.method_10264();
        double f = p2.method_10260();
        List<class_1297> list = this.getEntitiesBlockingCrystal(new class_238(d, e, f, d + bb.field_1320, e + bb.field_1325, f + bb.field_1324));
        return list.isEmpty();
    }

    private List<class_1297> getEntitiesBlockingCrystal(class_238 box) {
        CopyOnWriteArrayList entities = new CopyOnWriteArrayList((Collection)AutoCrystal.mc.field_1687.method_8335(null, box));
        for (class_1297 entity : entities) {
            class_1511 entity1;
            if (entity == null || !entity.method_5805() || entity instanceof class_1303 || this.forcePlaceConfig.getValue() != ForcePlace.NONE && entity instanceof class_1542 && entity.field_6012 <= 10) {
                entities.remove((Object)entity);
                continue;
            }
            if (!(entity instanceof class_1511) || !(entity1 = (class_1511)entity).method_5829().method_994(box)) continue;
            Integer antiStuckAttacks = (Integer)this.antiStuckCrystals.get((Object)entity1.method_5628());
            if (!this.attackRangeCheck(entity1) && (antiStuckAttacks == null || (float)antiStuckAttacks.intValue() <= this.attackLimitConfig * 10.0f)) {
                entities.remove((Object)entity);
                continue;
            }
            double dist = AutoCrystal.mc.field_1724.method_5858((class_1297)entity1);
            this.stuckCrystals.add((Object)new AntiStuckData(entity1.method_5628(), entity1.method_24515(), entity1.method_19538(), dist));
        }
        return entities;
    }

    private boolean intersectingAntiStuckCheck(class_2338 blockPos) {
        if (this.stuckCrystals.isEmpty()) {
            return false;
        }
        return this.stuckCrystals.stream().anyMatch(d -> d.blockPos().equals((Object)blockPos.method_10084()));
    }

    private class_1511 intersectingCrystalCheck(class_2338 pos) {
        return (class_1511)AutoCrystal.mc.field_1687.method_8335(null, new class_238(pos)).stream().filter(e -> e instanceof class_1511).min(Comparator.comparingDouble(e -> AutoCrystal.mc.field_1724.method_5739(e))).orElse(null);
    }

    private List<class_2338> getSphere(class_243 origin) {
        double rad = Math.ceil((double)this.placeRangeConfig.getValue().floatValue());
        return this.getSphere(rad, origin);
    }

    private List<class_2338> getSphere(double rad, class_243 origin) {
        ArrayList sphere = new ArrayList();
        for (double x = -rad; x <= rad; x += 1.0) {
            for (double y = -rad; y <= rad; y += 1.0) {
                for (double z = -rad; z <= rad; z += 1.0) {
                    class_2382 pos = new class_2382((int)(origin.method_10216() + x), (int)(origin.method_10214() + y), (int)(origin.method_10215() + z));
                    class_2338 p = new class_2338(pos);
                    sphere.add((Object)p);
                }
            }
        }
        return sphere;
    }

    private boolean canHoldCrystal() {
        return this.isHoldingCrystal() || this.autoSwapConfig.getValue() != Swap.OFF && this.getCrystalSlot() != -1;
    }

    private class_1268 getCrystalHand() {
        class_1799 offhand = AutoCrystal.mc.field_1724.method_6079();
        class_1799 mainhand = AutoCrystal.mc.field_1724.method_6047();
        if (offhand.method_7909() instanceof class_1774) {
            return class_1268.field_5810;
        }
        if (mainhand.method_7909() instanceof class_1774) {
            return class_1268.field_5808;
        }
        return null;
    }

    public float getBreakDelay() {
        return 1000.0f - this.breakSpeedConfig * 50.0f;
    }

    public void setStage(String crystalStage) {
    }

    public int getBreakMs() {
        if (this.attackLatency.isEmpty()) {
            return 0;
        }
        float avg = 0.0f;
        ArrayList latencyCopy = Lists.newArrayList(this.attackLatency);
        if (!latencyCopy.isEmpty()) {
            Iterator iterator = latencyCopy.iterator();
            while (iterator.hasNext()) {
                float t = ((Long)iterator.next()).longValue();
                avg += t;
            }
            avg /= (float)latencyCopy.size();
        }
        return (int)avg;
    }

    public boolean shouldPreForcePlace() {
        return this.forcePlaceConfig.getValue() == ForcePlace.PRE;
    }

    public float getPlaceRange() {
        return this.placeRangeConfig.getValue().floatValue();
    }

    public static enum Sequential {
        NORMAL,
        STRICT,
        NONE;

    }

    public static enum Swap {
        NORMAL,
        SILENT,
        SILENT_ALT,
        OFF;

    }

    public static enum ForcePlace {
        PRE,
        POST,
        NONE;

    }

    public static enum Placements {
        NATIVE,
        PROTOCOL;

    }

    private static class DamageData<T> {
        private final List<String> tags = new ArrayList();
        private T damageData;
        private class_1297 attackTarget;
        private class_2338 blockPos;
        private double damage;
        private double selfDamage;
        private boolean antiSurround;

        public DamageData() {
        }

        public DamageData(class_2338 damageData, class_1297 attackTarget, double damage, double selfDamage, boolean antiSurround) {
            this.damageData = damageData;
            this.attackTarget = attackTarget;
            this.damage = damage;
            this.selfDamage = selfDamage;
            this.blockPos = damageData;
            this.antiSurround = antiSurround;
        }

        public DamageData(T damageData, class_1297 attackTarget, double damage, double selfDamage, class_2338 blockPos, boolean antiSurround) {
            this.damageData = damageData;
            this.attackTarget = attackTarget;
            this.damage = damage;
            this.selfDamage = selfDamage;
            this.blockPos = blockPos;
            this.antiSurround = antiSurround;
        }

        public void setDamageData(T damageData, class_1297 attackTarget, double damage, double selfDamage) {
            this.damageData = damageData;
            this.attackTarget = attackTarget;
            this.damage = damage;
            this.selfDamage = selfDamage;
        }

        public T getDamageData() {
            return this.damageData;
        }

        public class_1297 getAttackTarget() {
            return this.attackTarget;
        }

        public double getDamage() {
            return this.damage;
        }

        public double getSelfDamage() {
            return this.selfDamage;
        }

        public class_2338 getBlockPos() {
            return this.blockPos;
        }

        public boolean isAntiSurround() {
            return this.antiSurround;
        }
    }

    private record AntiStuckData(int id, class_2338 blockPos, class_243 pos, double stuckDist) {
    }

    private class PlaceCrystalTask
    implements Callable<DamageData<class_2338>> {
        private final List<class_2338> threadSafeBlocks;
        private final List<class_1297> threadSafeEntities;

        public PlaceCrystalTask(List<class_2338> threadSafeBlocks, List<class_1297> threadSafeEntities) {
            this.threadSafeBlocks = threadSafeBlocks;
            this.threadSafeEntities = threadSafeEntities;
        }

        public DamageData<class_2338> call() throws Exception {
            return AutoCrystal.this.calculatePlaceCrystal(this.threadSafeBlocks, this.threadSafeEntities);
        }

        public Object call() throws Exception {
            return this.call();
        }
    }

    private class AttackCrystalTask
    implements Callable<DamageData<class_1511>> {
        private final List<class_1297> threadSafeEntities;

        public AttackCrystalTask(List<class_1297> threadSafeEntities) {
            this.threadSafeEntities = threadSafeEntities;
        }

        public DamageData<class_1511> call() throws Exception {
            return AutoCrystal.this.calculateAttackCrystal(this.threadSafeEntities);
        }

        public Object call() throws Exception {
            return this.call();
        }
    }

    public static enum Rotate {
        FULL,
        SEMI,
        OFF;

    }
}
