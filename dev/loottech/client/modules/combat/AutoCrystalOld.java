package dev.loottech.client.modules.combat;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.module.CombatModule;
import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.api.utilities.DamageUtils;
import dev.loottech.api.utilities.InventoryUtils;
import dev.loottech.api.utilities.RotationUtils;
import dev.loottech.api.utilities.Timer;
import dev.loottech.api.utilities.render.RenderUtils;
import dev.loottech.client.events.Render3DEvent;
import dev.loottech.client.events.RunTickEvent;
import dev.loottech.client.modules.client.ModuleColor;
import dev.loottech.client.values.impl.ValueBoolean;
import dev.loottech.client.values.impl.ValueCategory;
import dev.loottech.client.values.impl.ValueColor;
import dev.loottech.client.values.impl.ValueEnum;
import dev.loottech.client.values.impl.ValueNumber;
import java.awt.Color;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1303;
import net.minecraft.class_1511;
import net.minecraft.class_1542;
import net.minecraft.class_1657;
import net.minecraft.class_1802;
import net.minecraft.class_1937;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2374;
import net.minecraft.class_238;
import net.minecraft.class_2382;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2824;
import net.minecraft.class_2879;
import net.minecraft.class_3959;
import net.minecraft.class_3965;

@RegisterModule(name="AutoCrystalOld", tag="AutoCrystalOld", description="Automatically place and break crystals at selected target.", category=Module.Category.COMBAT)
public class AutoCrystalOld
extends CombatModule {
    private final ValueCategory placeCategory = new ValueCategory("Place", "The category for placing crystals.");
    private final ValueNumber placeDelay = new ValueNumber("Delay", "Delay", "Delay in ms between placing crystals.", this.placeCategory, (Number)Float.valueOf((float)0.0f), (Number)Float.valueOf((float)0.0f), (Number)Float.valueOf((float)1000.0f));
    private final ValueBoolean sequential = new ValueBoolean("Sequential", "Sequential", "Place a crystal immediately after breaking one.", this.placeCategory, false);
    public final ValueBoolean forcePlace = new ValueBoolean("ForcePlace", "ForcePlace", "Place after AutoMine breaks a player's block.", this.placeCategory, false);
    public final ValueNumber placeRange = new ValueNumber("PlaceRange", "PlaceRange", "The range to place crystals at", this.placeCategory, (Number)Double.valueOf((double)4.0), (Number)Double.valueOf((double)0.0), (Number)Double.valueOf((double)6.0));
    private final ValueBoolean strictDirection = new ValueBoolean("StrictDirection", "StrictDirection", "", this.placeCategory, false);
    private final ValueBoolean placeRotate = new ValueBoolean("Rotate", "Rotate", "", this.placeCategory, true);
    private final ValueBoolean placeSwing = new ValueBoolean("Swing", "Swing", "", this.placeCategory, true);
    private final ValueEnum swap = new ValueEnum("Swap", "Swap", "method of switching to end crystal", this.placeCategory, SwapModes.Silent);
    private final ValueCategory breakCategory = new ValueCategory("Break", "The category for breaking crystals.");
    private final ValueNumber breakDelay = new ValueNumber("Delay", "Delay", "Delay in ms between breaking crystals.", this.breakCategory, (Number)Float.valueOf((float)0.0f), (Number)Float.valueOf((float)0.0f), (Number)Float.valueOf((float)1000.0f));
    private final ValueNumber breakRange = new ValueNumber("BreakRange", "BreakRange", "The range to break crystals at", this.breakCategory, (Number)Double.valueOf((double)4.0), (Number)Double.valueOf((double)0.0), (Number)Double.valueOf((double)6.0));
    private final ValueBoolean breakRotate = new ValueBoolean("Rotate", "Rotate", "", this.breakCategory, true);
    private final ValueBoolean breakSwing = new ValueBoolean("Swing", "Swing", "", this.breakCategory, true);
    private final ValueCategory targetingCategory = new ValueCategory("Targeting", "The category for targeting settings.");
    private final ValueNumber targetRange = new ValueNumber("TargetRange", "TargetRange", "The range to target players at", this.placeCategory, (Number)Double.valueOf((double)10.0), (Number)Double.valueOf((double)0.0), (Number)Double.valueOf((double)20.0));
    private final ValueNumber calculationRange = new ValueNumber("CalculationRange", "Calculation Range", "The range from the target to calculate a position.", this.targetingCategory, (Number)Double.valueOf((double)5.0), (Number)Double.valueOf((double)1.0), (Number)Double.valueOf((double)10.0));
    private final ValueNumber minDmg = new ValueNumber("MinDamage", "MinimumDamage", "The minimum damage to deal to enemy to place a crystal", this.targetingCategory, (Number)Double.valueOf((double)4.0), (Number)Double.valueOf((double)0.0), (Number)Double.valueOf((double)20.0));
    private final ValueNumber maxSelf = new ValueNumber("MaxSelfDamage", "MaxSelfDamage", "The maximum ammount of damage to do to self", this.targetingCategory, (Number)Double.valueOf((double)8.0), (Number)Double.valueOf((double)0.0), (Number)Double.valueOf((double)20.0));
    private final ValueBoolean friends = new ValueBoolean("FriendProtect", "FriendProtect", "Don't Target Friends", this.targetingCategory, true);
    private final ValueBoolean pauseEating = new ValueBoolean("PauseEating", "PauseEating", "pause if player is eating", this.targetingCategory, true);
    private final ValueCategory renderCategory = new ValueCategory("Render", "The category for rendering.");
    private final ValueBoolean renderPos = new ValueBoolean("Render Pos", "RenderPos", "Render the position it will place at", this.renderCategory, true);
    private final ValueColor fillColor = new ValueColor("Fill", "FillColor", "The color that the box will be filled with", this.renderCategory, ModuleColor.getColor(30), false, true);
    private final ValueColor lineColor = new ValueColor("Line", "LineColor", "The color that the line will be", this.renderCategory, ModuleColor.getColor(255), false, true);
    private final ValueBoolean damageRender = new ValueBoolean("DamageRender", "DamageRender", "Render damage at the box", this.renderCategory, true);
    private final ValueBoolean textShadow = new ValueBoolean("TextShadow", "TextShadow", "Render damage at the box", this.renderCategory, true);
    class_1657 target;
    class_2338 targetPos = null;
    double currentDmg = 0.0;
    Timer placeTimer = new Timer();
    Timer breakTimer = new Timer();
    private int crystalsPlaced = 0;
    private long lastCpsTime = System.currentTimeMillis();
    private int currentCps = 0;

    public AutoCrystalOld() {
        super(250);
    }

    public static double distanceTo(class_1657 to) {
        return AutoCrystalOld.mc.field_1724.method_19538().method_1022(to.method_19538());
    }

    public static double distanceTo(class_2338 to) {
        return AutoCrystalOld.mc.field_1724.method_19538().method_1022(class_243.method_24953((class_2382)to));
    }

    @Override
    public String getHudInfo() {
        String targetName = this.target != null ? this.target.method_5477().method_54160() + ", " : "";
        String dmg = this.currentDmg != 0.0 ? this.currentDmg + ", " : "";
        String cpsStr = "" + this.currentCps / 2;
        return targetName + dmg + cpsStr;
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (this.targetPos != null && this.currentDmg != 0.0 && this.renderPos.getValue()) {
            RenderUtils.drawBox(event.getMatrices(), this.targetPos, this.lineColor.getValue(), 1.0);
            RenderUtils.drawBoxFilled(event.getMatrices(), this.targetPos, this.fillColor.getValue());
            if (this.damageRender.getValue()) {
                RenderUtils.drawTextIn3D(event.getMatrices(), 0.0f, 0.0f, "" + this.currentDmg, class_243.method_24953((class_2382)this.targetPos), Color.WHITE, this.textShadow.getValue(), 1.0f);
            }
        }
    }

    @Override
    public void onRunTick(RunTickEvent event) {
        long now = System.currentTimeMillis();
        if (now - this.lastCpsTime >= 1000L) {
            this.currentCps = this.crystalsPlaced;
            this.crystalsPlaced = 0;
            this.lastCpsTime = now;
        }
        if (AutoCrystalOld.mc.field_1724 == null || AutoCrystalOld.mc.field_1687 == null) {
            return;
        }
        if (this.pauseEating.getValue() && AutoCrystalOld.mc.field_1724.method_6115()) {
            return;
        }
        int crystalSlot = InventoryUtils.findInHotbar(class_1802.field_8301).slot();
        if (crystalSlot == -1) {
            return;
        }
        class_1657 bestTarget = null;
        class_2338 bestPos = null;
        double bestDamage = 0.0;
        for (class_1297 entity : AutoCrystalOld.mc.field_1687.method_18112()) {
            if (entity.method_19538().method_1022(AutoCrystalOld.mc.field_1724.method_19538()) > this.targetRange.getValue().doubleValue()) continue;
            if (entity instanceof class_1657) {
                class_2338 pos;
                class_1657 player = (class_1657)entity;
                if (player == AutoCrystalOld.mc.field_1724 || player.method_29504() || AutoCrystalOld.distanceTo(player) > this.targetRange.getValue().doubleValue() || this.friends.getValue() && Managers.FRIEND.isFriend(player.method_5477().method_54160()) || (pos = this.calculatePlacePos(player)) == null || pos.equals((Object)player.method_24515()) || AutoCrystalOld.distanceTo(pos) > this.placeRange.getValue().doubleValue()) continue;
                float dmg = (float)this.currentDmg;
                if ((double)dmg > bestDamage) {
                    bestDamage = dmg;
                    bestTarget = player;
                    bestPos = pos;
                }
            }
            if (!(entity instanceof class_1511) || !(AutoCrystalOld.distanceTo(entity.method_24515()) < this.breakRange.getValue().doubleValue()) || this.targetPos == null || !this.targetPos.method_19769((class_2374)entity.method_19538(), 3.5)) continue;
            if (this.breakTimer.passedMs(this.breakDelay.getValue().longValue())) {
                this.attackInternal((class_1511)entity, class_1268.field_5808);
            }
            this.breakTimer.reset();
        }
        if (bestTarget != null && bestPos != null) {
            this.target = bestTarget;
            this.targetPos = bestPos;
            this.placeCrystal(bestPos, crystalSlot);
        } else {
            this.target = null;
            this.targetPos = null;
            this.currentDmg = 0.0;
        }
    }

    private void attackInternal(class_1511 crystalEntity, class_1268 hand) {
        this.attackInternal(crystalEntity, crystalEntity.method_5628(), hand);
    }

    private void attackInternal(class_1511 entity, int crystalEntity, class_1268 hand) {
        hand = hand != null ? hand : class_1268.field_5808;
        class_1511 entity2 = new class_1511((class_1937)AutoCrystalOld.mc.field_1687, 0.0, 0.0, 0.0);
        entity2.method_5838(crystalEntity);
        if (this.breakRotate.getValue()) {
            float[] r = RotationUtils.getRotations(entity.method_19538());
            Managers.ROTATION.setRotationSilent(r[0], r[1]);
        }
        class_2824 packet = class_2824.method_34206((class_1297)entity2, (boolean)AutoCrystalOld.mc.field_1724.method_5715());
        Managers.NETWORK.sendPacket((class_2596<?>)packet);
        if (this.breakSwing.getValue()) {
            AutoCrystalOld.mc.field_1724.method_6104(hand);
        } else {
            Managers.NETWORK.sendPacket((class_2596<?>)new class_2879(hand));
        }
        if (this.sequential.getValue()) {
            this.placeCrystal(entity.method_24515());
        }
    }

    /*
     * Exception decompiling
     */
    private void placeCrystal(class_2338 pos, int slot) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter$TooOptimisticMatchException
         */
        throw new IllegalStateException("Decompilation failed");
    }

    private class_2350 getPlaceDirection(class_2338 blockPos) {
        int x = blockPos.method_10263();
        int y = blockPos.method_10264();
        int z = blockPos.method_10260();
        if (this.strictDirection.getValue()) {
            if (AutoCrystalOld.mc.field_1724.method_23318() >= (double)blockPos.method_10264()) {
                return class_2350.field_11036;
            }
            class_3965 result = AutoCrystalOld.mc.field_1687.method_17742(new class_3959(AutoCrystalOld.mc.field_1724.method_33571(), new class_243((double)x + 0.5, (double)y + 0.5, (double)z + 0.5), class_3959.class_3960.field_17559, class_3959.class_242.field_1348, (class_1297)AutoCrystalOld.mc.field_1724));
            if (result != null && result.method_17783() == class_239.class_240.field_1332) {
                return result.method_17780();
            }
        } else {
            if (AutoCrystalOld.mc.field_1687.method_24794(blockPos)) {
                return class_2350.field_11033;
            }
            class_3965 result = AutoCrystalOld.mc.field_1687.method_17742(new class_3959(AutoCrystalOld.mc.field_1724.method_33571(), new class_243((double)x + 0.5, (double)y + 0.5, (double)z + 0.5), class_3959.class_3960.field_17559, class_3959.class_242.field_1348, (class_1297)AutoCrystalOld.mc.field_1724));
            if (result != null && result.method_17783() == class_239.class_240.field_1332) {
                return result.method_17780();
            }
        }
        return class_2350.field_11036;
    }

    public void placeCrystal(class_2338 pos) {
        int crystalSlot = InventoryUtils.findInHotbar(class_1802.field_8301).slot();
        if (crystalSlot == -1) {
            return;
        }
        this.placeCrystal(pos, crystalSlot);
    }

    public class_2338 calculatePlacePos(class_1657 player) {
        class_2338 bestPos = null;
        double maxDamage = 0.0;
        class_2338 playerPos = player.method_24515();
        int range = this.calculationRange.getValue().intValue();
        for (int x = -range; x <= range; ++x) {
            for (int y = -range; y <= range; ++y) {
                for (int z = -range; z <= range; ++z) {
                    float selfDmg;
                    float targetDmg;
                    class_2338 pos = playerPos.method_10069(x, y, z);
                    if (!this.canPlaceCrystal(pos, player) || (targetDmg = DamageUtils.getCrystalDamage((class_1297)player, null, pos, null, false)) <= this.minDmg.getValue().floatValue() || (selfDmg = DamageUtils.getCrystalDamage((class_1297)AutoCrystalOld.mc.field_1724, null, pos, null, false)) > this.maxSelf.getValue().floatValue() || !((double)targetDmg > maxDamage)) continue;
                    bestPos = pos;
                    maxDamage = targetDmg;
                    this.currentDmg = targetDmg;
                }
            }
        }
        return bestPos;
    }

    @Override
    public void onDisable() {
        Managers.INVENTORY.syncToClient();
        this.target = null;
        this.targetPos = null;
        this.currentDmg = 0.0;
        this.currentCps = 0;
    }

    private boolean canPlaceCrystal(class_2338 pos, class_1657 entity) {
        class_2248 block = AutoCrystalOld.mc.field_1687.method_8320(pos).method_26204();
        if (block != class_2246.field_10540 && block != class_2246.field_9987) {
            return false;
        }
        class_238 crystalBox = new class_238(pos.method_10084());
        List<class_1297> list = this.getEntitiesBlockingCrystal(crystalBox);
        return list.isEmpty();
    }

    private List<class_1297> getEntitiesBlockingCrystal(class_238 box) {
        CopyOnWriteArrayList entities = new CopyOnWriteArrayList((Collection)AutoCrystalOld.mc.field_1687.method_8335(null, box));
        for (class_1297 entity : entities) {
            if (entity != null && entity.method_5805() && (!(entity instanceof class_1303) || !(entity instanceof class_1542) || entity.field_6012 > 10)) continue;
            entities.remove((Object)entity);
        }
        return entities;
    }

    private static enum SwapModes {
        Silent,
        Normal,
        Off;

    }
}
