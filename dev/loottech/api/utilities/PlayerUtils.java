package dev.loottech.api.utilities;

import com.google.common.collect.Sets;
import dev.loottech.api.manager.Managers;
import dev.loottech.api.utilities.IMinecraft;
import dev.loottech.api.utilities.RotationUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.class_1268;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1511;
import net.minecraft.class_1657;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1934;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_2382;
import net.minecraft.class_243;
import net.minecraft.class_2560;
import net.minecraft.class_2596;
import net.minecraft.class_2680;
import net.minecraft.class_2824;
import net.minecraft.class_2848;
import net.minecraft.class_2879;
import net.minecraft.class_2885;
import net.minecraft.class_304;
import net.minecraft.class_3483;
import net.minecraft.class_3532;
import net.minecraft.class_3965;
import net.minecraft.class_640;
import net.minecraft.class_7134;

public class PlayerUtils
implements IMinecraft {
    public static Set<class_2248> RIGHT_CLICKABLE_BLOCKS = Sets.newHashSet((Object[])new class_2248[]{class_2246.field_10034, class_2246.field_10380, class_2246.field_10443, class_2246.field_10199, class_2246.field_10407, class_2246.field_10063, class_2246.field_10203, class_2246.field_10600, class_2246.field_10275, class_2246.field_10051, class_2246.field_10140, class_2246.field_10320, class_2246.field_10532, class_2246.field_10268, class_2246.field_10605, class_2246.field_10373, class_2246.field_10055, class_2246.field_10068, class_2246.field_10371, class_2246.field_10535, class_2246.field_16332, class_2246.field_10057, class_2246.field_10278, class_2246.field_10417, class_2246.field_10493, class_2246.field_10553, class_2246.field_10066, class_2246.field_10494, class_2246.field_10377, class_2246.field_10450, class_2246.field_10188, class_2246.field_10291, class_2246.field_10513, class_2246.field_10041, class_2246.field_10196, class_2246.field_10457, class_2246.field_10333, class_2246.field_10200, class_2246.field_10228, class_2246.field_10363, class_2246.field_10179, class_2246.field_10223, class_2246.field_10327, class_2246.field_10461, class_2246.field_10527, class_2246.field_10288, class_2246.field_10109, class_2246.field_10141, class_2246.field_10561, class_2246.field_10621, class_2246.field_10326, class_2246.field_10180, class_2246.field_10230, class_2246.field_10410, class_2246.field_10610, class_2246.field_10019, class_2246.field_10069, class_2246.field_10120, class_2246.field_10356, class_2246.field_10181, class_2246.field_10149, class_2246.field_10521, class_2246.field_10352, class_2246.field_10627, class_2246.field_10232, class_2246.field_10403, class_2246.field_10183, class_2246.field_10485, class_2246.field_10081, class_2246.field_10312, class_2246.field_10263, class_2246.field_10525, class_2246.field_10395, class_2246.field_9980, class_2246.field_10608, class_2246.field_10486, class_2246.field_10246, class_2246.field_10017, class_2246.field_10137, class_2246.field_10323, class_2246.field_10183, class_2246.field_10284, class_2246.field_10401, class_2246.field_10231, class_2246.field_10391, class_2246.field_10330, class_2246.field_10265, class_2246.field_10544, class_2246.field_10587, class_2246.field_10121, class_2246.field_10187, class_2246.field_10411, class_2246.field_10088, class_2246.field_22104, class_2246.field_22106, class_2246.field_22105, class_2246.field_22107, class_2246.field_16333, class_2246.field_16334, class_2246.field_16336, class_2246.field_16337, class_2246.field_16330, class_2246.field_10083, class_2246.field_16335, class_2246.field_16329});

    public static float getLocalPlayerHealth() {
        return PlayerUtils.mc.field_1724.method_6032() + PlayerUtils.mc.field_1724.method_6067();
    }

    public static int getDimension() {
        if (PlayerUtils.mc.field_1687.method_8597().equals((Object)class_7134.field_37666)) {
            return 0;
        }
        if (PlayerUtils.mc.field_1687.method_8597().equals((Object)class_7134.field_37667)) {
            return 1;
        }
        return 3;
    }

    public static int computeFallDamage(float fallDistance, float damageMultiplier) {
        if (PlayerUtils.mc.field_1724.method_5864().method_20210(class_3483.field_42971)) {
            return 0;
        }
        class_1293 statusEffectInstance = PlayerUtils.mc.field_1724.method_6112(class_1294.field_5913);
        float f = statusEffectInstance == null ? 0.0f : (float)(statusEffectInstance.method_5578() + 1);
        return class_3532.method_15386((float)((fallDistance - 3.0f - f) * damageMultiplier));
    }

    public static boolean isHolding(class_1792 item) {
        class_1799 itemStack = PlayerUtils.mc.field_1724.method_6047();
        if (!itemStack.method_7960() && itemStack.method_7909() == item) {
            return true;
        }
        itemStack = PlayerUtils.mc.field_1724.method_6079();
        return !itemStack.method_7960() && itemStack.method_7909() == item;
    }

    public static boolean isHotbarKeysPressed() {
        for (class_304 binding : PlayerUtils.mc.field_1690.field_1852) {
            if (!binding.method_1434()) continue;
            return true;
        }
        return false;
    }

    public static void placeBlock(class_2338 position, class_2350 direction, class_1268 hand, Runnable runnable, boolean rotate, boolean crystalDestruction) {
        boolean sneak;
        class_2338 offsetPosition;
        class_243 vec3d = position.method_46558();
        if (direction == null) {
            direction = class_2350.field_11036;
            offsetPosition = position;
        } else {
            offsetPosition = position.method_10093(direction);
            vec3d = vec3d.method_1031((double)direction.method_10148() / 2.0, (double)direction.method_10164() / 2.0, (double)direction.method_10165() / 2.0);
        }
        if (rotate) {
            Managers.ROTATION.setRotationSilent(RotationUtils.getRotations((double)position.method_10263() + 0.5, (double)position.method_10264() + 0.5, (double)position.method_10260() + 0.5)[0], RotationUtils.getRotations((double)position.method_10263() + 0.5, (double)position.method_10264() + 0.5, (double)position.method_10260() + 0.5)[1]);
        }
        if (crystalDestruction) {
            PlayerUtils.destroyCrystals(position);
        }
        if (runnable != null) {
            runnable.run();
        }
        boolean sprint = PlayerUtils.mc.field_1724.method_5624();
        boolean bl = sneak = RIGHT_CLICKABLE_BLOCKS.contains((Object)PlayerUtils.mc.field_1687.method_8320(offsetPosition).method_26204()) && !PlayerUtils.mc.field_1724.method_5715();
        if (sprint) {
            Managers.NETWORK.sendPacket((class_2596<?>)new class_2848((class_1297)PlayerUtils.mc.field_1724, class_2848.class_2849.field_12985));
        }
        if (sneak) {
            Managers.NETWORK.sendPacket((class_2596<?>)new class_2848((class_1297)PlayerUtils.mc.field_1724, class_2848.class_2849.field_12979));
        }
        class_3965 blockHitResult = new class_3965(vec3d, direction.method_10153(), offsetPosition, false);
        Managers.NETWORK.sendPacket((class_2596<?>)new class_2885(hand, blockHitResult, PlayerUtils.mc.field_1687.method_41925().method_41937().method_41942()));
        Managers.NETWORK.sendPacket((class_2596<?>)new class_2879(hand));
        if (rotate) {
            Managers.ROTATION.setRotationSilentSync();
        }
        if (sneak) {
            Managers.NETWORK.sendPacket((class_2596<?>)new class_2848((class_1297)PlayerUtils.mc.field_1724, class_2848.class_2849.field_12984));
        }
        if (sprint) {
            Managers.NETWORK.sendPacket((class_2596<?>)new class_2848((class_1297)PlayerUtils.mc.field_1724, class_2848.class_2849.field_12981));
        }
    }

    public static void destroyCrystals(class_2338 position) {
        block1: {
            List surroundingCrystals = PlayerUtils.mc.field_1687.method_8335(null, new class_238(position)).stream().filter(entity -> entity instanceof class_1511).toList();
            if (surroundingCrystals.isEmpty()) {
                return;
            }
            Iterator iterator = surroundingCrystals.iterator();
            if (!iterator.hasNext()) break block1;
            class_1297 entity2 = (class_1297)iterator.next();
            Managers.NETWORK.sendPacket((class_2596<?>)class_2824.method_34206((class_1297)entity2, (boolean)PlayerUtils.mc.field_1724.method_5715()));
            Managers.NETWORK.sendPacket((class_2596<?>)new class_2879(class_1268.field_5808));
        }
    }

    public static class_1657 getTarget(float range) {
        class_1657 optimalPlayer = null;
        for (class_1657 player : new ArrayList((Collection)PlayerUtils.mc.field_1687.method_18456())) {
            if (!(PlayerUtils.mc.field_1724.method_5739((class_1297)player) <= range) || Managers.FRIEND.isFriend(player.method_5477().getString()) || player.method_29504() || player.method_6032() <= 0.0f && player == PlayerUtils.mc.field_1724 || player.method_5477().equals((Object)PlayerUtils.mc.field_1724.method_5477()) || player.field_6235 != 0) continue;
            if (optimalPlayer == null) {
                optimalPlayer = player;
                continue;
            }
            if (!(PlayerUtils.mc.field_1724.method_5858((class_1297)player) < PlayerUtils.mc.field_1724.method_5858((class_1297)optimalPlayer))) continue;
            optimalPlayer = player;
        }
        return optimalPlayer;
    }

    public static List<class_2338> getSphere(float range, boolean sphere, boolean hollow) {
        ArrayList blocks = new ArrayList();
        int x = PlayerUtils.mc.field_1724.method_24515().method_10263() - (int)range;
        while ((float)x <= (float)PlayerUtils.mc.field_1724.method_24515().method_10263() + range) {
            int z = PlayerUtils.mc.field_1724.method_24515().method_10260() - (int)range;
            while ((float)z <= (float)PlayerUtils.mc.field_1724.method_24515().method_10260() + range) {
                int y = sphere ? PlayerUtils.mc.field_1724.method_24515().method_10264() - (int)range : PlayerUtils.mc.field_1724.method_24515().method_10264();
                int n = y;
                while ((float)y < (float)PlayerUtils.mc.field_1724.method_24515().method_10264() + range) {
                    double distance = (PlayerUtils.mc.field_1724.method_24515().method_10263() - x) * (PlayerUtils.mc.field_1724.method_24515().method_10263() - x) + (PlayerUtils.mc.field_1724.method_24515().method_10260() - z) * (PlayerUtils.mc.field_1724.method_24515().method_10260() - z) + (sphere ? (PlayerUtils.mc.field_1724.method_24515().method_10264() - y) * (PlayerUtils.mc.field_1724.method_24515().method_10264() - y) : 0);
                    if (distance < (double)(range * range) && (!hollow || distance >= ((double)range - 1.0) * ((double)range - 1.0))) {
                        blocks.add((Object)new class_2338(x, y, z));
                    }
                    ++y;
                }
                ++z;
            }
            ++x;
        }
        return blocks;
    }

    public static class_1934 getGameMode(class_1657 player) {
        class_640 playerListEntry = mc.method_1562().method_2871(player.method_5667());
        return playerListEntry == null ? class_1934.field_9220 : playerListEntry.method_2958();
    }

    public static int getPing(class_1657 player) {
        if (mc.method_1562() == null) {
            return 0;
        }
        class_640 playerListEntry = mc.method_1562().method_2871(player.method_5667());
        if (playerListEntry == null) {
            return 0;
        }
        return playerListEntry.method_2959();
    }

    public static class_2350 getDirection(class_2338 position, List<class_2338> exceptions, boolean strictDirection) {
        List<class_2350> strictDirections = new List<class_2350>();
        if (strictDirection) {
            strictDirections = PlayerUtils.getStrictDirections(PlayerUtils.mc.field_1724.method_33571(), class_243.method_24953((class_2382)position));
        }
        for (class_2350 direction : class_2350.values()) {
            class_2338 offset = position.method_10093(direction);
            if (strictDirection && !strictDirections.contains((Object)direction.method_10153()) || PlayerUtils.mc.field_1687.method_8320(offset).method_45474() && (exceptions == null || !exceptions.contains((Object)offset))) continue;
            return direction;
        }
        return null;
    }

    public static List<class_2350> getStrictDirections(class_243 eyePos, class_243 blockPos) {
        ArrayList directions = new ArrayList();
        double differenceX = eyePos.method_10216() - blockPos.method_10216();
        double differenceY = eyePos.method_10214() - blockPos.method_10214();
        double differenceZ = eyePos.method_10215() - blockPos.method_10215();
        if (differenceY > 0.5) {
            directions.add((Object)class_2350.field_11036);
        } else if (differenceY < -0.5) {
            directions.add((Object)class_2350.field_11033);
        } else {
            directions.add((Object)class_2350.field_11036);
            directions.add((Object)class_2350.field_11033);
        }
        if (differenceX > 0.5) {
            directions.add((Object)class_2350.field_11034);
        } else if (differenceX < -0.5) {
            directions.add((Object)class_2350.field_11039);
        } else {
            directions.add((Object)class_2350.field_11034);
            directions.add((Object)class_2350.field_11039);
        }
        if (differenceZ > 0.5) {
            directions.add((Object)class_2350.field_11035);
        } else if (differenceZ < -0.5) {
            directions.add((Object)class_2350.field_11043);
        } else {
            directions.add((Object)class_2350.field_11035);
            directions.add((Object)class_2350.field_11043);
        }
        return directions;
    }

    public static boolean inWeb(double expandBb) {
        for (class_2338 blockPos : PlayerUtils.getAllInBox(PlayerUtils.mc.field_1724.method_5829().method_1014(expandBb))) {
            class_2680 state = PlayerUtils.mc.field_1687.method_8320(blockPos);
            if (!(state.method_26204() instanceof class_2560)) continue;
            return true;
        }
        return false;
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
