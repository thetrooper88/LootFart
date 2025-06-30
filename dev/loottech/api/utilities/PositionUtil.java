package dev.loottech.api.utilities;

import dev.loottech.api.utilities.BlastResistantBlocks;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_3532;

public class PositionUtil {
    public static class_238 enclosingBox(List<class_2338> posList) {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int minZ = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        int maxZ = Integer.MIN_VALUE;
        for (class_2338 blockPos : posList) {
            if (blockPos.method_10263() < minX) {
                minX = blockPos.method_10263();
            }
            if (blockPos.method_10264() < minY) {
                minY = blockPos.method_10264();
            }
            if (blockPos.method_10260() < minZ) {
                minZ = blockPos.method_10260();
            }
            if (blockPos.method_10263() > maxX) {
                maxX = blockPos.method_10263();
            }
            if (blockPos.method_10264() > maxY) {
                maxY = blockPos.method_10264();
            }
            if (blockPos.method_10260() <= maxZ) continue;
            maxZ = blockPos.method_10260();
        }
        return new class_238((double)minX, (double)minY, (double)minZ, (double)maxX, (double)maxY, (double)maxZ);
    }

    public static class_2338 getRoundedBlockPos(class_243 vec) {
        return PositionUtil.getRoundedBlockPos(vec.method_10216(), vec.method_10214(), vec.method_10215());
    }

    public static class_2338 getRoundedBlockPos(double x, double y, double z) {
        int flooredX = class_3532.method_15357((double)x);
        int flooredY = (int)Math.round((double)y);
        int flooredZ = class_3532.method_15357((double)z);
        return new class_2338(flooredX, flooredY, flooredZ);
    }

    public static boolean isBedrock(class_238 box, class_2338 pos) {
        return PositionUtil.getAllInBox(box, pos).stream().anyMatch(BlastResistantBlocks::isUnbreakable);
    }

    public static List<class_2338> getAllInBox(class_238 box, class_2338 pos) {
        ArrayList intersections = new ArrayList();
        int x = (int)Math.floor((double)box.field_1323);
        while ((double)x < Math.ceil((double)box.field_1320)) {
            int z = (int)Math.floor((double)box.field_1321);
            while ((double)z < Math.ceil((double)box.field_1324)) {
                intersections.add((Object)new class_2338(x, pos.method_10264(), z));
                ++z;
            }
            ++x;
        }
        return intersections;
    }

    public static List<class_2338> getAllInBox(class_238 box) {
        ArrayList intersections = new ArrayList();
        int x = (int)Math.floor((double)box.field_1323);
        while ((double)x < Math.ceil((double)box.field_1320)) {
            int y = (int)Math.floor((double)box.field_1322);
            while ((double)y < Math.ceil((double)box.field_1325)) {
                int z = (int)Math.floor((double)box.field_1321);
                while ((double)z < Math.ceil((double)box.field_1324)) {
                    intersections.add((Object)new class_2338(x, y, z));
                    ++z;
                }
                ++y;
            }
            ++x;
        }
        return intersections;
    }
}
