package dev.loottech.api.manager.miscellaneous;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.miscellaneous.RotationCallback;
import dev.loottech.api.utilities.RotationUtils;
import dev.loottech.api.utilities.Util;
import dev.loottech.client.modules.client.AntiCheat;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.class_1268;
import net.minecraft.class_1269;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2680;
import net.minecraft.class_2846;
import net.minecraft.class_2879;
import net.minecraft.class_2885;
import net.minecraft.class_3965;

public class InteractionManager
implements Util {
    private static final Set<class_2248> SNEAK_BLOCKS = Set.of((Object[])new class_2248[]{class_2246.field_10034, class_2246.field_10443, class_2246.field_10380, class_2246.field_9980, class_2246.field_10181, class_2246.field_16333, class_2246.field_16331, class_2246.field_16336, class_2246.field_10485, class_2246.field_16329, class_2246.field_16335, class_2246.field_10223, class_2246.field_10179, class_2246.field_10603, class_2246.field_10371, class_2246.field_10605, class_2246.field_10203, class_2246.field_10055, class_2246.field_10532, class_2246.field_10373, class_2246.field_10140, class_2246.field_10320, class_2246.field_10275, class_2246.field_10063, class_2246.field_10407, class_2246.field_10051, class_2246.field_10268, class_2246.field_10068, class_2246.field_10199, class_2246.field_10600, class_2246.field_10608, class_2246.field_40285, class_2246.field_10486, class_2246.field_42740, class_2246.field_47048, class_2246.field_47049, class_2246.field_47050, class_2246.field_47052, class_2246.field_47053, class_2246.field_47062, class_2246.field_47051, class_2246.field_10323, class_2246.field_22095, class_2246.field_10453, class_2246.field_10246, class_2246.field_10017, class_2246.field_37555, class_2246.field_10137, class_2246.field_22094});

    public boolean placeBlock(class_2338 pos, int slot, boolean strictDirection, boolean clientSwing, RotationCallback rotationCallback) {
        return this.placeBlock(pos, slot, strictDirection, clientSwing, rotationCallback, false);
    }

    public boolean placeBlock(class_2338 pos, int slot, boolean strictDirection, boolean clientSwing, RotationCallback rotationCallback, boolean airPlace) {
        class_2350 direction = this.getInteractDirectionInternal(pos, strictDirection);
        if (airPlace || Managers.MODULE.getInstance(AntiCheat.class).grimAirPlace.getValue() && direction == null) {
            direction = class_2350.field_11033;
            return this.placeBlock(pos, direction, slot, clientSwing, Managers.MODULE.getInstance(AntiCheat.class).grimAirPlace.getValue(), rotationCallback);
        }
        if (direction == null) {
            return false;
        }
        class_2338 neighbor = pos.method_10093(direction.method_10153());
        return this.placeBlock(neighbor, direction, slot, clientSwing, false, rotationCallback);
    }

    public boolean placeBlock(class_2338 pos, int slot, boolean strictDirection, boolean clientSwing, boolean packet, RotationCallback rotationCallback) {
        return this.placeBlock(pos, slot, strictDirection, clientSwing, packet, false, rotationCallback);
    }

    public boolean placeBlock(class_2338 pos, int slot, boolean strictDirection, boolean clientSwing, boolean packet, boolean airPlace, RotationCallback rotationCallback) {
        class_2350 direction = this.getInteractDirectionInternal(pos, strictDirection);
        if (airPlace || Managers.MODULE.getInstance(AntiCheat.class).grimAirPlace.getValue() && direction == null) {
            direction = class_2350.field_11033;
            return this.placeBlock(pos, direction, slot, clientSwing, Managers.MODULE.getInstance(AntiCheat.class).grimAirPlace.getValue(), rotationCallback);
        }
        if (direction == null) {
            return false;
        }
        class_2338 neighbor = pos.method_10093(direction.method_10153());
        return this.placeBlock(neighbor, direction, slot, clientSwing, false, packet, rotationCallback);
    }

    public boolean placeBlock(class_2338 pos, class_2350 direction, int slot, boolean clientSwing, boolean grimAirPlace, boolean packet, RotationCallback rotationCallback) {
        class_243 hitVec = pos.method_46558().method_1019(new class_243(direction.method_23955()).method_1021(0.5));
        return this.placeBlock(new class_3965(hitVec, direction, pos, false), slot, clientSwing, grimAirPlace, packet, rotationCallback);
    }

    public boolean placeBlock(class_2338 pos, class_2350 direction, int slot, boolean clientSwing, boolean grimAirPlace, RotationCallback rotationCallback) {
        class_243 hitVec = pos.method_46558().method_1019(new class_243(direction.method_23955()).method_1021(0.5));
        return this.placeBlock(new class_3965(hitVec, direction, pos, false), slot, clientSwing, grimAirPlace, rotationCallback);
    }

    public boolean placeBlock(class_3965 hitResult, int slot, boolean clientSwing, boolean grimAirPlace, boolean packet, RotationCallback rotationCallback) {
        boolean isRotating;
        boolean isSpoofing;
        boolean bl = isSpoofing = slot != Managers.INVENTORY.getServerSlot();
        if (isSpoofing) {
            Managers.INVENTORY.setSlot(slot);
        }
        if (grimAirPlace) {
            Managers.NETWORK.sendPacket((class_2596<?>)new class_2846(class_2846.class_2847.field_12969, class_2338.field_10980, class_2350.field_11033));
        }
        boolean bl2 = isRotating = rotationCallback != null;
        if (isRotating) {
            float[] angles = RotationUtils.getRotations(hitResult.method_17784());
            rotationCallback.handleRotation(true, angles);
        }
        boolean result = this.placeBlockImmediately(hitResult, grimAirPlace ? class_1268.field_5810 : class_1268.field_5808, clientSwing, packet);
        if (isRotating) {
            float[] angles = RotationUtils.getRotations(hitResult.method_17784());
            rotationCallback.handleRotation(false, angles);
        }
        if (grimAirPlace) {
            Managers.NETWORK.sendPacket((class_2596<?>)new class_2846(class_2846.class_2847.field_12969, class_2338.field_10980, class_2350.field_11033));
        }
        if (isSpoofing) {
            Managers.INVENTORY.syncToClient();
        }
        return result;
    }

    public boolean placeBlock(class_3965 hitResult, int slot, boolean clientSwing, boolean grimAirPlace, RotationCallback rotationCallback) {
        boolean isRotating;
        boolean isSpoofing;
        boolean bl = isSpoofing = slot != Managers.INVENTORY.getServerSlot();
        if (isSpoofing) {
            Managers.INVENTORY.setSlot(slot);
        }
        if (grimAirPlace) {
            Managers.NETWORK.sendPacket((class_2596<?>)new class_2846(class_2846.class_2847.field_12969, class_2338.field_10980, class_2350.field_11033));
        }
        boolean bl2 = isRotating = rotationCallback != null;
        if (isRotating) {
            float[] angles = RotationUtils.getRotations(hitResult.method_17784());
            rotationCallback.handleRotation(true, angles);
        }
        boolean result = this.placeBlockImmediately(hitResult, grimAirPlace ? class_1268.field_5810 : class_1268.field_5808, clientSwing, true);
        if (isRotating) {
            float[] angles = RotationUtils.getRotations(hitResult.method_17784());
            rotationCallback.handleRotation(false, angles);
        }
        if (grimAirPlace) {
            Managers.NETWORK.sendPacket((class_2596<?>)new class_2846(class_2846.class_2847.field_12969, class_2338.field_10980, class_2350.field_11033));
        }
        if (isSpoofing) {
            Managers.INVENTORY.syncToClient();
        }
        return result;
    }

    public boolean placeBlockImmediately(class_3965 result, class_1268 hand, boolean clientSwing, boolean packet) {
        class_1269 actionResult;
        boolean shouldSneak;
        class_2680 state = InteractionManager.mc.field_1687.method_8320(result.method_17777());
        boolean bl = shouldSneak = InteractionManager.isSneakBlock(state) && !InteractionManager.mc.field_1724.method_5715();
        if (shouldSneak) {
            Managers.MOVEMENT.setPacketSneaking(true);
            Managers.MOVEMENT.applySneak();
        }
        class_1269 class_12692 = actionResult = packet ? this.placeBlockPacket(result, hand) : this.placeBlockInternally(result, hand);
        if (actionResult.method_23665() && actionResult.method_23666()) {
            if (clientSwing) {
                InteractionManager.mc.field_1724.method_6104(class_1268.field_5808);
            } else {
                Managers.NETWORK.sendPacket((class_2596<?>)new class_2879(class_1268.field_5808));
            }
        }
        if (shouldSneak) {
            Managers.MOVEMENT.setPacketSneaking(false);
        }
        return actionResult.method_23665();
    }

    private class_1269 placeBlockInternally(class_3965 hitResult, class_1268 hand) {
        return InteractionManager.mc.field_1761.method_2896(InteractionManager.mc.field_1724, hand, hitResult);
    }

    public class_1269 placeBlockPacket(class_3965 hitResult, class_1268 hand) {
        Managers.NETWORK.sendSequencedPacket(id -> new class_2885(hand, hitResult, id));
        return class_1269.field_5812;
    }

    public class_2350 getInteractDirection(class_2338 blockPos, boolean strictDirection) {
        class_2350 direction = this.getInteractDirectionInternal(blockPos, strictDirection);
        return direction == null ? class_2350.field_11036 : direction;
    }

    public class_2350 getInteractDirectionInternal(class_2338 blockPos, boolean strictDirection) {
        Set<class_2350> validDirections = this.getPlaceDirectionsNCP(InteractionManager.mc.field_1724.method_33571(), blockPos.method_46558());
        class_2350 interactDirection = null;
        for (class_2350 direction : class_2350.values()) {
            class_2680 state = InteractionManager.mc.field_1687.method_8320(blockPos.method_10093(direction));
            if (state.method_26215() || !state.method_26227().method_15769() || state.method_26204() == class_2246.field_10535 || state.method_26204() == class_2246.field_10105 || state.method_26204() == class_2246.field_10414 || strictDirection && !validDirections.contains((Object)direction.method_10153())) continue;
            interactDirection = direction;
            break;
        }
        if (interactDirection == null) {
            return null;
        }
        return interactDirection.method_10153();
    }

    public class_2350 getPlaceDirectionNCP(class_2338 blockPos, boolean visible) {
        class_243 eyePos = new class_243(InteractionManager.mc.field_1724.method_23317(), InteractionManager.mc.field_1724.method_23318() + (double)InteractionManager.mc.field_1724.method_5751(), InteractionManager.mc.field_1724.method_23321());
        if ((double)blockPos.method_10263() == eyePos.method_10216() && (double)blockPos.method_10264() == eyePos.method_10214() && (double)blockPos.method_10260() == eyePos.method_10215()) {
            return class_2350.field_11033;
        }
        Set<class_2350> ncpDirections = this.getPlaceDirectionsNCP(eyePos, blockPos.method_46558());
        for (class_2350 dir : ncpDirections) {
            if (visible && !InteractionManager.mc.field_1687.method_22347(blockPos.method_10093(dir))) continue;
            return dir;
        }
        return class_2350.field_11036;
    }

    public Set<class_2350> getPlaceDirectionsNCP(class_243 eyePos, class_243 blockPos) {
        return this.getPlaceDirectionsNCP(eyePos.field_1352, eyePos.field_1351, eyePos.field_1350, blockPos.field_1352, blockPos.field_1351, blockPos.field_1350);
    }

    public Set<class_2350> getPlaceDirectionsNCP(double x, double y, double z, double dx, double dy, double dz) {
        double xdiff = x - dx;
        double ydiff = y - dy;
        double zdiff = z - dz;
        HashSet dirs = new HashSet(6);
        if (ydiff > 0.5) {
            dirs.add((Object)class_2350.field_11036);
        } else if (ydiff < -0.5) {
            dirs.add((Object)class_2350.field_11033);
        } else {
            dirs.add((Object)class_2350.field_11036);
            dirs.add((Object)class_2350.field_11033);
        }
        if (xdiff > 0.5) {
            dirs.add((Object)class_2350.field_11034);
        } else if (xdiff < -0.5) {
            dirs.add((Object)class_2350.field_11039);
        } else {
            dirs.add((Object)class_2350.field_11034);
            dirs.add((Object)class_2350.field_11039);
        }
        if (zdiff > 0.5) {
            dirs.add((Object)class_2350.field_11035);
        } else if (zdiff < -0.5) {
            dirs.add((Object)class_2350.field_11043);
        } else {
            dirs.add((Object)class_2350.field_11035);
            dirs.add((Object)class_2350.field_11043);
        }
        return dirs;
    }

    public boolean isInEyeRange(class_2338 pos) {
        return (double)pos.method_10264() > InteractionManager.mc.field_1724.method_23318() + (double)InteractionManager.mc.field_1724.method_5751();
    }

    public static boolean isSneakBlock(class_2680 state) {
        return InteractionManager.isSneakBlock(state.method_26204());
    }

    public static boolean isSneakBlock(class_2248 block) {
        return SNEAK_BLOCKS.contains((Object)block);
    }
}
