package dev.loottech.api.utilities;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.utilities.BlockUtils;
import dev.loottech.api.utilities.IMinecraft;
import dev.loottech.api.utilities.MathUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_2382;
import net.minecraft.class_243;
import net.minecraft.class_3532;

public class TargetUtils
implements IMinecraft {
    public static class_1309 getTarget(float range, float wallRange, boolean visible, TargetMode targetMode) {
        class_1309 targetEntity = null;
        for (class_1297 e : TargetUtils.mc.field_1687.method_18112()) {
            class_1309 entity;
            if (!(e instanceof class_1309) || (TargetUtils.mc.field_1724.method_6057((class_1297)(entity = (class_1309)e)) ? !(TargetUtils.mc.field_1724.method_5649(entity.method_23317(), entity.method_23318(), entity.method_23321()) <= (double)range) : !(TargetUtils.mc.field_1724.method_5649(entity.method_23317(), entity.method_23318(), entity.method_23321()) <= (double)wallRange))) continue;
            if (Managers.FRIEND.isFriend(e.method_5477().getString()) || entity == TargetUtils.mc.field_1724 && entity.method_5477().equals((Object)TargetUtils.mc.field_1724.method_5477()) || !(entity instanceof class_1657) || (entity.method_29504() || entity.method_6032() <= 0.0f) && !TargetUtils.mc.field_1724.method_6057((class_1297)entity) && visible || ((class_1657)entity).method_7337()) continue;
            if (targetEntity == null) {
                targetEntity = entity;
                continue;
            }
            if (targetMode == TargetMode.Range) {
                if (!(TargetUtils.mc.field_1724.method_5649(entity.method_23317(), entity.method_23318(), entity.method_23321()) < TargetUtils.mc.field_1724.method_5649(targetEntity.method_23317(), targetEntity.method_23318(), targetEntity.method_23321()))) continue;
                targetEntity = entity;
                continue;
            }
            if (!(entity.method_6032() + entity.method_6067() < targetEntity.method_6032() + targetEntity.method_6067())) continue;
            targetEntity = entity;
        }
        return targetEntity;
    }

    public static class_1657 getTarget(float range) {
        class_1657 targetPlayer = null;
        for (class_1657 player : new ArrayList((Collection)TargetUtils.mc.field_1687.method_18456())) {
            if (TargetUtils.mc.field_1724.method_5858((class_1297)player) > (double)MathUtils.square(range) || player == TargetUtils.mc.field_1724 || Managers.FRIEND.isFriend(player.method_5477().getString()) || player.method_29504() || player.method_6032() <= 0.0f) continue;
            if (targetPlayer == null) {
                targetPlayer = player;
                continue;
            }
            if (!(TargetUtils.mc.field_1724.method_5858((class_1297)player) < TargetUtils.mc.field_1724.method_5858((class_1297)targetPlayer))) continue;
            targetPlayer = player;
        }
        return targetPlayer;
    }

    public static class_238 extrapolate(class_1657 entity, int ticks) {
        if (entity == null) {
            return null;
        }
        double deltaX = entity.method_23317() - entity.field_6014;
        double deltaZ = entity.method_23321() - entity.field_5969;
        double motionX = 0.0;
        double motionZ = 0.0;
        for (double i = 1.0; i <= (double)ticks && !TargetUtils.mc.field_1687.method_39454((class_1297)entity, entity.method_5829().method_997(new class_243(deltaX * i, 0.0, deltaZ * i))); i += 0.5) {
            motionX = deltaX * i;
            motionZ = deltaZ * i;
        }
        class_243 vec3d = new class_243(motionX, 0.0, motionZ);
        if (vec3d == null) {
            return null;
        }
        return entity.method_5829().method_997(vec3d);
    }

    public static class_2338 getNearestAdjacentBlock(class_1657 player, class_2338 ... exclude) {
        class_2338 origin = player.method_24515();
        class_2338 best = null;
        double bestDistance = Double.MAX_VALUE;
        if (BlockUtils.canBreak(origin) && !Arrays.asList((Object[])exclude).contains((Object)origin)) {
            double dist = player.method_5649((double)origin.method_10263() + 0.5, (double)origin.method_10264() + 0.5, (double)origin.method_10260() + 0.5);
            best = origin;
            bestDistance = dist;
        }
        for (class_2350 dir : class_2350.values()) {
            double dist;
            class_2338 candidate = origin.method_10093(dir);
            if (!BlockUtils.canBreak(candidate) || Arrays.asList((Object[])exclude).contains((Object)candidate) || !((dist = player.method_5649((double)candidate.method_10263() + 0.5, (double)candidate.method_10264() + 0.5, (double)candidate.method_10260() + 0.5)) < bestDistance)) continue;
            bestDistance = dist;
            best = candidate;
        }
        return best;
    }

    public static class_2338 getMinePos(class_1657 player, class_2338 ... exclude) {
        class_2338 nearest = null;
        double bestDist = Double.MAX_VALUE;
        class_238 feetBox = player.method_5829().method_35575(player.method_23318());
        int minX = class_3532.method_15357((double)feetBox.field_1323);
        int maxX = class_3532.method_15357((double)feetBox.field_1320);
        int y = class_3532.method_15357((double)feetBox.field_1322);
        int minZ = class_3532.method_15357((double)feetBox.field_1321);
        int maxZ = class_3532.method_15357((double)feetBox.field_1324);
        for (int x = minX; x <= maxX; ++x) {
            for (int z = minZ; z <= maxZ; ++z) {
                double dist;
                class_2338 pos = new class_2338(x, y, z);
                if (!BlockUtils.canBreak(pos) || TargetUtils.mc.field_1687.method_8320(pos).method_26204().equals((Object)class_2246.field_9987) || Arrays.asList((Object[])exclude).contains((Object)pos) || !((dist = player.method_5649((double)pos.method_10263() + 0.5, (double)pos.method_10264() + 0.5, (double)pos.method_10260() + 0.5)) < bestDist)) continue;
                bestDist = dist;
                nearest = pos;
            }
        }
        return nearest;
    }

    private static double dist(class_2338 pos) {
        return TargetUtils.mc.field_1724.method_19538().method_1022(class_243.method_24953((class_2382)pos));
    }

    public static enum TargetMode {
        Range,
        Health;

    }
}
