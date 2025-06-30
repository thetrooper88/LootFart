package dev.loottech.api.utilities;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.rotation.Rotation;
import dev.loottech.api.utilities.IMinecraft;
import dev.loottech.api.utilities.InventoryUtils;
import dev.loottech.api.utilities.MathUtils;
import dev.loottech.api.utilities.RotationUtils;
import dev.loottech.client.values.Priority;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.class_1268;
import net.minecraft.class_1292;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1303;
import net.minecraft.class_1309;
import net.minecraft.class_1511;
import net.minecraft.class_1542;
import net.minecraft.class_1657;
import net.minecraft.class_1750;
import net.minecraft.class_1799;
import net.minecraft.class_1887;
import net.minecraft.class_1893;
import net.minecraft.class_1922;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2374;
import net.minecraft.class_238;
import net.minecraft.class_2382;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2680;
import net.minecraft.class_2879;
import net.minecraft.class_2885;
import net.minecraft.class_3486;
import net.minecraft.class_3965;
import net.minecraft.class_5134;
import net.minecraft.class_5321;

public class BlockUtils
implements IMinecraft {
    private final Map<class_2338, Long> placeCooldowns = new ConcurrentHashMap();

    public static boolean isPlaceable(class_2338 pos, boolean ignoreEntities, boolean ignoreCrystals) {
        class_238 box;
        if (BlockUtils.mc.field_1687 == null || BlockUtils.mc.field_1724 == null) {
            return false;
        }
        if (!ignoreEntities && !BlockUtils.mc.field_1687.method_8333(null, box = new class_238(pos), entity -> {
            if (entity instanceof class_1542 || entity instanceof class_1303) {
                return false;
            }
            if (ignoreCrystals && entity.method_5864().toString().contains((CharSequence)"end_crystal")) {
                return false;
            }
            return !entity.method_7325();
        }).isEmpty()) {
            return false;
        }
        return BlockUtils.getPlaceableSide(pos) != null;
    }

    public static void placeBlock(class_2338 position, class_1268 hand, boolean rotate) {
        if (!BlockUtils.mc.field_1687.method_8320(position).method_26166(new class_1750((class_1657)BlockUtils.mc.field_1724, class_1268.field_5808, BlockUtils.mc.field_1724.method_5998(class_1268.field_5808), new class_3965(class_243.method_24954((class_2382)position), class_2350.field_11036, position, false)))) {
            return;
        }
        if (BlockUtils.getPlaceableSide(position) == null) {
            return;
        }
        if (rotate) {
            float[] r = RotationUtils.getRotations(position.method_10263(), position.method_10264(), position.method_10260());
            Rotation rotation = new Rotation(Priority.MEDIUM, r[0], r[1]);
            Managers.ROTATION.setRotation(rotation);
        }
        BlockUtils.mc.field_1724.field_3944.method_52787((class_2596)new class_2885(hand, new class_3965(class_243.method_24954((class_2382)position.method_10093((class_2350)Objects.requireNonNull((Object)BlockUtils.getPlaceableSide(position)))), ((class_2350)Objects.requireNonNull((Object)BlockUtils.getPlaceableSide(position))).method_10153(), position.method_10093((class_2350)Objects.requireNonNull((Object)BlockUtils.getPlaceableSide(position))), false), BlockUtils.mc.field_1687.method_41925().method_41937().method_41942()));
        BlockUtils.mc.field_1724.field_3944.method_52787((class_2596)new class_2879(hand));
    }

    public static boolean isPositionPlaceable(class_2338 position, boolean entityCheck, boolean sideCheck) {
        if (!BlockUtils.mc.field_1687.method_8320(position).method_26204().method_9616(BlockUtils.mc.field_1687.method_8320(position), new class_1750((class_1657)BlockUtils.mc.field_1724, class_1268.field_5808, BlockUtils.mc.field_1724.method_5998(class_1268.field_5808), new class_3965(class_243.method_24954((class_2382)position), class_2350.field_11036, position, false)))) {
            return false;
        }
        if (entityCheck) {
            for (class_1297 entity : BlockUtils.mc.field_1687.method_8390(class_1297.class, new class_238(position), class_1297::method_5805)) {
                if (entity instanceof class_1542 || entity instanceof class_1303) continue;
                return false;
            }
        }
        if (sideCheck) {
            return BlockUtils.getPlaceableSide(position) != null;
        }
        return true;
    }

    public static boolean isPositionPlaceable(class_2338 position, boolean entityCheck, boolean sideCheck, boolean ignoreCrystals) {
        if (!BlockUtils.mc.field_1687.method_8320(position).method_26204().method_9616(BlockUtils.mc.field_1687.method_8320(position), new class_1750((class_1657)BlockUtils.mc.field_1724, class_1268.field_5808, BlockUtils.mc.field_1724.method_5998(class_1268.field_5808), new class_3965(class_243.method_24954((class_2382)position), class_2350.field_11036, position, false)))) {
            return false;
        }
        if (entityCheck) {
            for (class_1297 entity : BlockUtils.mc.field_1687.method_8390(class_1297.class, new class_238(position), class_1297::method_5805)) {
                if (entity instanceof class_1542 || entity instanceof class_1303 || entity instanceof class_1511 && ignoreCrystals) continue;
                return false;
            }
        }
        if (sideCheck) {
            return BlockUtils.getPlaceableSide(position) != null;
        }
        return true;
    }

    public static class_2350 getPlaceableSide(class_2338 position) {
        for (class_2350 side : class_2350.values()) {
            if (!BlockUtils.mc.field_1687.method_8320(position.method_10093(side)).method_51366() || BlockUtils.mc.field_1687.method_8320(position.method_10093(side)).method_51176()) continue;
            return side;
        }
        return null;
    }

    public static List<class_2338> getNearbyBlocks(class_1657 player, double blockRange, boolean motion) {
        ArrayList nearbyBlocks = new ArrayList();
        int range = (int)MathUtils.roundToPlaces(blockRange, 0);
        if (motion) {
            player.method_19538().method_1019(class_243.method_24954((class_2382)new class_2382((int)player.method_18798().field_1352, (int)player.method_18798().field_1351, (int)player.method_18798().field_1350)));
        }
        for (int x = -range; x <= range; ++x) {
            for (int y = -range; y <= range - range / 2; ++y) {
                for (int z = -range; z <= range; ++z) {
                    nearbyBlocks.add((Object)class_2338.method_49638((class_2374)player.method_19538().method_1031((double)x, (double)y, (double)z)));
                }
            }
        }
        return nearbyBlocks;
    }

    public static BlockResistance getBlockResistance(class_2338 block) {
        if (BlockUtils.mc.field_1687.method_22347(block)) {
            return BlockResistance.Blank;
        }
        if (!(BlockUtils.mc.field_1687.method_8320(block).method_26204().method_36555() == -1.0f || BlockUtils.mc.field_1687.method_8320(block).method_26204().equals((Object)class_2246.field_10540) || BlockUtils.mc.field_1687.method_8320(block).method_26204().equals((Object)class_2246.field_10535) || BlockUtils.mc.field_1687.method_8320(block).method_26204().equals((Object)class_2246.field_10485) || BlockUtils.mc.field_1687.method_8320(block).method_26204().equals((Object)class_2246.field_10443))) {
            return BlockResistance.Breakable;
        }
        if (BlockUtils.mc.field_1687.method_8320(block).method_26204().equals((Object)class_2246.field_10540) || BlockUtils.mc.field_1687.method_8320(block).method_26204().equals((Object)class_2246.field_10535) || BlockUtils.mc.field_1687.method_8320(block).method_26204().equals((Object)class_2246.field_10485) || BlockUtils.mc.field_1687.method_8320(block).method_26204().equals((Object)class_2246.field_10443)) {
            return BlockResistance.Resistant;
        }
        if (BlockUtils.mc.field_1687.method_8320(block).method_26204().equals((Object)class_2246.field_9987)) {
            return BlockResistance.Unbreakable;
        }
        return null;
    }

    public static boolean canBreak(class_2338 blockPos, class_2680 state) {
        if (!BlockUtils.mc.field_1724.method_7337() && state.method_26214((class_1922)BlockUtils.mc.field_1687, blockPos) < 0.0f) {
            return false;
        }
        return !state.method_26218((class_1922)BlockUtils.mc.field_1687, blockPos).method_1110();
    }

    public static boolean canBreak(class_2338 blockPos) {
        return BlockUtils.canBreak(blockPos, BlockUtils.mc.field_1687.method_8320(blockPos));
    }

    public static double getBlockBreakingSpeed(int slot, class_2680 block, boolean isOnGround) {
        class_1799 tool;
        int efficiency;
        double speed = ((class_1799)BlockUtils.mc.field_1724.method_31548().field_7547.get(slot)).method_7924(block);
        if (speed > 1.0 && (efficiency = InventoryUtils.getEnchantmentLevel(tool = BlockUtils.mc.field_1724.method_31548().method_5438(slot), (class_5321<class_1887>)class_1893.field_9131)) > 0 && !tool.method_7960()) {
            speed += (double)(efficiency * efficiency + 1);
        }
        if (class_1292.method_5576((class_1309)BlockUtils.mc.field_1724)) {
            speed *= (double)(1.0f + (float)(class_1292.method_5575((class_1309)BlockUtils.mc.field_1724) + 1) * 0.2f);
        }
        if (BlockUtils.mc.field_1724.method_6059(class_1294.field_5901)) {
            float k = switch (BlockUtils.mc.field_1724.method_6112(class_1294.field_5901).method_5578()) {
                case 0 -> 0.3f;
                case 1 -> 0.09f;
                case 2 -> 0.0027f;
                default -> 8.1E-4f;
            };
            speed *= (double)k;
        }
        if (BlockUtils.mc.field_1724.method_5777(class_3486.field_15517)) {
            speed *= BlockUtils.mc.field_1724.method_45325(class_5134.field_51576);
        }
        if (!isOnGround) {
            speed *= BlockUtils.mc.field_1724.method_45325(class_5134.field_51576);
        }
        return speed;
    }

    public static class_3965 getBlockHitResult(class_243 pos) {
        return BlockUtils.getBlockHitResult(class_2338.method_49638((class_2374)pos));
    }

    public static class_3965 getBlockHitResult(class_2338 pos) {
        return new class_3965(class_243.method_24954((class_2382)pos.method_10093((class_2350)Objects.requireNonNull((Object)BlockUtils.getPlaceableSide(pos)))), ((class_2350)Objects.requireNonNull((Object)BlockUtils.getPlaceableSide(pos))).method_10153(), pos.method_10093((class_2350)Objects.requireNonNull((Object)BlockUtils.getPlaceableSide(pos))), BlockUtils.mc.field_1724.method_5757());
    }

    public static enum BlockResistance {
        Blank,
        Breakable,
        Resistant,
        Unbreakable;

    }
}
