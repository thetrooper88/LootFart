package dev.loottech.client.modules.movement;

import baritone.api.BaritoneAPI;
import baritone.api.pathing.goals.Goal;
import baritone.api.pathing.goals.GoalBlock;
import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.api.utilities.ChatUtils;
import dev.loottech.api.utilities.FindItemResult;
import dev.loottech.api.utilities.InventoryUtils;
import dev.loottech.client.events.ChunkDataEvent;
import dev.loottech.client.events.InteractItemEvent;
import dev.loottech.client.events.PacketReceiveEvent;
import dev.loottech.client.events.PreTickEvent;
import dev.loottech.client.modules.player.ChestSwap;
import dev.loottech.client.values.impl.ValueBoolean;
import dev.loottech.client.values.impl.ValueNumber;
import java.util.function.Predicate;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1304;
import net.minecraft.class_1657;
import net.minecraft.class_1713;
import net.minecraft.class_1781;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1922;
import net.minecraft.class_1923;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2382;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2708;
import net.minecraft.class_2848;
import org.jetbrains.annotations.Nullable;

@RegisterModule(name="ElytraFlight", description="Jeff Mod Skid.", category=Module.Category.MOVEMENT)
public class ElytraFlight
extends Module {
    private final ValueBoolean bounce = new ValueBoolean("Bounce", "Auto bounce efly", false);
    private final ValueBoolean autoEat = new ValueBoolean("Auto Eat", "NO DYING ON LOOTTECH!", true);
    private final ValueBoolean lockPitch = new ValueBoolean("Lock Pitch", "Auto lock pitch", false);
    private final ValueBoolean autoAdjustPitch = new ValueBoolean("Auto Adjust Pitch", "Auto adjust pitch", false);
    private final ValueNumber pitch = new ValueNumber("Pitch", "", (Number)Float.valueOf((float)90.0f), (Number)Float.valueOf((float)0.0f), (Number)Float.valueOf((float)90.0f));
    private final double speed = 100.0;
    private final ValueBoolean lockYaw = new ValueBoolean("Lock Yaw", "Auto lock yaw", false);
    private final ValueBoolean useCustomYaw = new ValueBoolean("Use Custom Yaw", "Auto use custom yaw", false);
    private final ValueNumber yaw = new ValueNumber("Yaw", "", (Number)Float.valueOf((float)0.0f), (Number)Float.valueOf((float)-180.0f), (Number)Float.valueOf((float)180.0f));
    private final ValueBoolean highwayObstaclePasser = new ValueBoolean("Highway Obstacle Passer", "Auto highway passer", false);
    private final boolean useCustomStartPos = false;
    class_2338 startPos = new class_2338(0, 0, 0);
    private final boolean awayFromStartPos = true;
    private final ValueNumber distance = new ValueNumber("Distance", "", (Number)Float.valueOf((float)10.0f), (Number)Float.valueOf((float)0.0f), (Number)Float.valueOf((float)10.0f));
    private final ValueNumber targetY = new ValueNumber("Target Y", "", (Number)Float.valueOf((float)120.0f), (Number)Float.valueOf((float)0.0f), (Number)Float.valueOf((float)125.0f));
    private final ValueBoolean avoidPortalTraps = new ValueBoolean("Avoid PortalTraps", "Auto avoid portal", false);
    private final double portalAvoidDistance = 20.0;
    private final int portalScanWidth = 5;
    private final ValueBoolean toggleElytra = new ValueBoolean("Toggle Elytra", "Auto toggle Elytra", false);
    public final ValueBoolean fakeHeadBlock = new ValueBoolean("Fake Head Block", "Auto fake head block", false);
    private final ValueBoolean autoSwapElytra = new ValueBoolean("Auto Swap Elytra", "Auto swap Elytra", false);
    int swapToDelay = 3;
    int swapBackDelay = 3;
    private boolean startSprinting;
    private class_2338 portalTrap = null;
    private boolean paused = false;
    private int swapBackSlot = -1;
    private boolean elytraToggled = false;
    private final double maxDistance = 80.0;
    private class_2338 tempPath = null;
    private int swapTicks = 0;
    private boolean swapping = false;
    private boolean waitingForChunksToLoad;
    private boolean eating = false;

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        class_2596<?> class_25962 = event.getPacket();
        if (class_25962 instanceof class_2708) {
            class_2708 packet = (class_2708)class_25962;
            this.onEnable();
        }
    }

    @Override
    public void onEnable() {
        if (ElytraFlight.mc.field_1724 == null || ElytraFlight.mc.field_1724.method_31549().field_7478) {
            return;
        }
        this.startSprinting = ElytraFlight.mc.field_1724.method_5624();
        this.tempPath = null;
        this.portalTrap = null;
        this.paused = false;
        this.swapBackSlot = -1;
        this.waitingForChunksToLoad = false;
        this.elytraToggled = false;
        if (this.bounce.getValue() && ElytraFlight.mc.field_1724.method_19538().method_18805(1.0, 0.0, 1.0).method_1033() >= 100.0) {
            if (BaritoneAPI.getProvider().getPrimaryBaritone().getElytraProcess().currentDestination() == null) {
                BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoal(null);
            }
            this.startPos = new class_2338(0, 0, 0);
            if (!this.useCustomYaw.getValue()) {
                if (ElytraFlight.mc.field_1724.method_24515().method_10262((class_2382)this.startPos) < 10000.0 || !this.highwayObstaclePasser.getValue()) {
                    double playerAngleNormalized = ElytraFlight.angleOnAxis(ElytraFlight.mc.field_1724.method_36454());
                    this.yaw.setValue((Number)Double.valueOf((double)playerAngleNormalized));
                } else {
                    class_2338 directionVec = ElytraFlight.mc.field_1724.method_24515().method_10059((class_2382)this.startPos);
                    double angle = Math.toDegrees((double)Math.atan2((double)(-directionVec.method_10263()), (double)directionVec.method_10260()));
                    double angleNormalized = ElytraFlight.angleOnAxis(angle);
                    this.yaw.setValue((Number)Double.valueOf((double)angleNormalized));
                }
            }
        }
    }

    @Override
    public void onDisable() {
        if (ElytraFlight.mc.field_1724 == null) {
            return;
        }
        if (this.bounce.getValue() && BaritoneAPI.getProvider().getPrimaryBaritone().getElytraProcess().currentDestination() == null) {
            BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoal(null);
        }
        ElytraFlight.mc.field_1724.method_5728(this.startSprinting);
        if (this.toggleElytra.getValue() && !ElytraFlight.mc.field_1724.method_6118(class_1304.field_6174).method_7909().toString().contains((CharSequence)"chestplate")) {
            Managers.MODULE.getInstance(ChestSwap.class).enable(false);
        }
    }

    public static double angleOnAxis(double yaw) {
        if (yaw < 0.0) {
            yaw += 360.0;
        }
        return Math.round((double)(yaw / 45.0)) * 45L;
    }

    public static class_243 yawToDirection(double yaw) {
        yaw = yaw * Math.PI / 180.0;
        double x = -Math.sin((double)yaw);
        double z = Math.cos((double)yaw);
        return new class_243(x, 0.0, z);
    }

    public static class_243 positionInDirection(class_243 pos, double yaw, double distance) {
        class_243 offset = ElytraFlight.yawToDirection(yaw).method_1021(distance);
        return pos.method_1019(offset);
    }

    public static class_243 getPlayerSpeed() {
        if (ElytraFlight.mc.field_1724 == null) {
            return class_243.field_1353;
        }
        double tX = ElytraFlight.mc.field_1724.method_23317() - ElytraFlight.mc.field_1724.field_6014;
        double tY = ElytraFlight.mc.field_1724.method_23318() - ElytraFlight.mc.field_1724.field_6036;
        double tZ = ElytraFlight.mc.field_1724.method_23321() - ElytraFlight.mc.field_1724.field_5969;
        return new class_243(tX *= 20.0, tY *= 20.0, tZ *= 20.0);
    }

    public static double distancePointToDirection(class_243 point, class_243 direction, @Nullable class_243 start) {
        if (start == null) {
            start = class_243.field_1353;
        }
        point = point.method_18806(new class_243(1.0, 0.0, 1.0));
        start = start.method_18806(new class_243(1.0, 0.0, 1.0));
        direction = direction.method_18806(new class_243(1.0, 0.0, 1.0));
        class_243 directionVec = point.method_1020(start);
        double projectionLength = directionVec.method_1026(direction) / direction.method_1027();
        class_243 projection = direction.method_1021(projectionLength);
        class_243 perp = directionVec.method_1020(projection);
        return perp.method_1033();
    }

    @Override
    public void onPreTick(PreTickEvent event) {
        if (ElytraFlight.mc.field_1724 == null || ElytraFlight.mc.field_1724.method_31549().field_7478) {
            return;
        }
        if (this.autoEat.getValue() && (float)ElytraFlight.mc.field_1724.method_7344().method_7586() <= 6.0f) {
            FindItemResult gappleResult = InventoryUtils.findInHotbar(class_1802.field_8367);
            FindItemResult carrotResult = InventoryUtils.findInHotbar(class_1802.field_8071);
            int targetSlot = -1;
            if (!carrotResult.found() && !gappleResult.found()) {
                return;
            }
            if (carrotResult.found()) {
                targetSlot = carrotResult.slot();
            } else if (gappleResult.found()) {
                targetSlot = gappleResult.slot();
            }
            Managers.INVENTORY.setClientSlot(targetSlot);
            ElytraFlight.mc.field_1690.field_1904.method_23481(true);
            this.eating = true;
        }
        if (this.eating && (float)ElytraFlight.mc.field_1724.method_7344().method_7586() >= 16.0f) {
            ElytraFlight.mc.field_1690.field_1904.method_23481(false);
            this.eating = false;
        }
        if (this.toggleElytra.getValue() && !this.elytraToggled) {
            if (!ElytraFlight.mc.field_1724.method_6118(class_1304.field_6174).method_7909().equals((Object)class_1802.field_8833)) {
                Managers.MODULE.getInstance(ChestSwap.class).enable(false);
            } else {
                this.elytraToggled = true;
            }
        }
        --this.swapTicks;
        if (this.swapTicks <= 0) {
            if (this.swapping && this.swapBackSlot != -1) {
                ElytraFlight.mc.field_1761.method_2919((class_1657)ElytraFlight.mc.field_1724, class_1268.field_5808);
                this.swapTicks = this.swapBackDelay;
                this.swapping = false;
            } else if (this.swapBackSlot != -1) {
                Managers.INVENTORY.click(38, 0, class_1713.field_7790);
                Managers.INVENTORY.click(this.swapBackSlot, 0, class_1713.field_7790);
                Managers.INVENTORY.click(38, 0, class_1713.field_7790);
                this.swapBackSlot = -1;
            }
        }
        if (this.enabled()) {
            ElytraFlight.mc.field_1724.method_5728(true);
        }
        if (this.bounce.getValue()) {
            if (this.tempPath != null && ElytraFlight.mc.field_1724.method_24515().method_10262((class_2382)this.tempPath) < 500.0) {
                this.tempPath = null;
                BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoal(null);
            } else if (this.tempPath != null) {
                BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath((Goal)new GoalBlock(this.tempPath));
                return;
            }
            if (this.highwayObstaclePasser.getValue() && BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().getGoal() != null) {
                return;
            }
            if (this.highwayObstaclePasser.getValue() && ElytraFlight.mc.field_1724.method_19538().method_1033() > 100.0 && (ElytraFlight.mc.field_1724.method_23318() < (double)this.targetY.getValue().intValue() || ElytraFlight.mc.field_1724.method_23318() > (double)(this.targetY.getValue().intValue() + 2) || ElytraFlight.mc.field_1724.field_5976) || this.portalTrap != null && this.portalTrap.method_10262((class_2382)ElytraFlight.mc.field_1724.method_24515()) < 400.0 || this.waitingForChunksToLoad) {
                this.waitingForChunksToLoad = false;
                this.paused = true;
                class_2338 goal = ElytraFlight.mc.field_1724.method_24515();
                double currDistance = this.distance.getValue().floatValue();
                if (this.portalTrap != null) {
                    currDistance += ElytraFlight.mc.field_1724.method_19538().method_1022(this.portalTrap.method_46558());
                    this.portalTrap = null;
                    ChatUtils.sendMessage("Pathing around portal.");
                }
                do {
                    if (currDistance > 80.0) {
                        this.tempPath = goal;
                        BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath((Goal)new GoalBlock(goal));
                        return;
                    }
                    class_243 unitYawVec = ElytraFlight.yawToDirection(this.yaw.getValue().intValue());
                    class_243 travelVec = ElytraFlight.mc.field_1724.method_19538().method_1020(this.startPos.method_46558());
                    double parallelCurrPosDot = travelVec.method_18806(new class_243(1.0, 0.0, 1.0)).method_1026(unitYawVec);
                    class_243 parallelCurrPosComponent = unitYawVec.method_1021(parallelCurrPosDot);
                    class_243 pos = this.startPos.method_46558().method_1019(parallelCurrPosComponent);
                    pos = ElytraFlight.positionInDirection(pos, this.yaw.getValue().intValue(), currDistance);
                    goal = new class_2338((int)Math.floor((double)pos.field_1352), this.targetY.getValue().intValue(), (int)Math.floor((double)pos.field_1350));
                    currDistance += 1.0;
                    if (ElytraFlight.mc.field_1687.method_8320(goal).method_26204() != class_2246.field_10243) continue;
                    this.waitingForChunksToLoad = true;
                    return;
                } while (!ElytraFlight.mc.field_1687.method_8320(goal.method_10074()).method_26212((class_1922)ElytraFlight.mc.field_1687, goal.method_10074()) || ElytraFlight.mc.field_1687.method_8320(goal).method_26204() == class_2246.field_10316 || !ElytraFlight.mc.field_1687.method_8320(goal).method_26215() || this.fakeHeadBlock.getValue() && !ElytraFlight.mc.field_1687.method_8320(goal.method_10086(2)).method_26212((class_1922)ElytraFlight.mc.field_1687, goal.method_10086(2)));
                BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath((Goal)new GoalBlock(goal));
            } else {
                this.paused = false;
                if (!this.enabled()) {
                    return;
                }
                if (ElytraFlight.mc.field_1724.method_24828()) {
                    ElytraFlight.mc.field_1724.method_6043();
                }
                if (this.lockYaw.getValue()) {
                    ElytraFlight.mc.field_1724.method_36456(this.yaw.getValue().floatValue());
                }
                if (this.lockPitch.getValue()) {
                    if (this.autoAdjustPitch.getValue()) {
                        double playerSpeed = ElytraFlight.getPlayerSpeed().method_18805(1.0, 0.0, 1.0).method_1033();
                        ElytraFlight.mc.field_1724.method_36457((float)Math.min((double)90.0, (double)Math.max((double)-90.0, (double)((100.0 - playerSpeed) * 5.0))));
                    } else {
                        ElytraFlight.mc.field_1724.method_36457(this.pitch.getValue().floatValue());
                    }
                }
            }
        }
        if (this.enabled()) {
            ElytraFlight.mc.field_1724.field_3944.method_52787((class_2596)new class_2848((class_1297)ElytraFlight.mc.field_1724, class_2848.class_2849.field_12982));
        }
    }

    public boolean enabled() {
        return this.isEnabled() && !this.paused && ElytraFlight.mc.field_1724 != null && ElytraFlight.mc.field_1724.method_6118(class_1304.field_6174).method_7909().toString().contains((CharSequence)"elytra");
    }

    @Override
    public void onChunkData(ChunkDataEvent event) {
        if (!this.avoidPortalTraps.getValue()) {
            return;
        }
        class_1923 pos = event.chunk.method_12004();
        class_2338 centerPos = pos.method_33943(this.targetY.getValue().intValue());
        class_243 moveDir = ElytraFlight.yawToDirection(this.yaw.getValue().intValue());
        double distanceToHighway = ElytraFlight.distancePointToDirection(class_243.method_24954((class_2382)centerPos), moveDir, ElytraFlight.mc.field_1724.method_19538());
        if (distanceToHighway > 21.0) {
            return;
        }
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                for (int y = this.targetY.getValue().intValue(); y < this.targetY.getValue().intValue() + 3; ++y) {
                    class_2338 posBehind;
                    class_2338 position = new class_2338(pos.field_9181 * 16 + x, y, pos.field_9180 * 16 + z);
                    if (ElytraFlight.distancePointToDirection(class_243.method_24954((class_2382)position), moveDir, ElytraFlight.mc.field_1724.method_19538()) > 5.0 || !ElytraFlight.mc.field_1687.method_8320(position).method_26204().equals((Object)class_2246.field_10316) || !ElytraFlight.mc.field_1687.method_8320(posBehind = new class_2338((int)Math.floor((double)((double)position.method_10263() + moveDir.field_1352)), position.method_10264(), (int)Math.floor((double)((double)position.method_10260() + moveDir.field_1350)))).method_26212((class_1922)ElytraFlight.mc.field_1687, posBehind) && ElytraFlight.mc.field_1687.method_8320(posBehind).method_26204() != class_2246.field_10316 || this.portalTrap != null && (!(this.portalTrap.method_10262((class_2382)posBehind) > 100.0) || !(ElytraFlight.mc.field_1724.method_24515().method_10262((class_2382)posBehind) < ElytraFlight.mc.field_1724.method_24515().method_10262((class_2382)this.portalTrap)))) continue;
                    this.portalTrap = posBehind;
                }
            }
        }
    }

    @Override
    public void onInteractItem(InteractItemEvent event) {
        if (!this.autoSwapElytra.getValue()) {
            return;
        }
        class_1799 itemStack = ElytraFlight.mc.field_1724.method_5998(event.hand);
        if (itemStack.method_7909() instanceof class_1781 && this.swapBackSlot == -1) {
            FindItemResult foundItem;
            if (!this.enabled()) {
                return;
            }
            class_1799 chestStack = ElytraFlight.mc.field_1724.method_6118(class_1304.field_6174);
            if (chestStack.method_7919() >= chestStack.method_7936() - 1 && (foundItem = InventoryUtils.find((Predicate<class_1799>)((Predicate)item -> item.method_7909() == class_1802.field_8833 && item.method_7919() < item.method_7936() - 1))).found()) {
                Managers.INVENTORY.click(foundItem.slot(), 0, class_1713.field_7790);
                Managers.INVENTORY.click(38, 0, class_1713.field_7790);
                Managers.INVENTORY.click(foundItem.slot(), 0, class_1713.field_7790);
                this.swapBackSlot = foundItem.slot();
                this.swapping = true;
                this.swapTicks = this.swapToDelay;
            }
        }
    }
}
