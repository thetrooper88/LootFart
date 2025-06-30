package dev.loottech.api.utilities;

import dev.loottech.api.utilities.IMinecraft;
import dev.loottech.api.utilities.PlayerUtils;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.class_1267;
import net.minecraft.class_1280;
import net.minecraft.class_1282;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1511;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_1890;
import net.minecraft.class_1922;
import net.minecraft.class_1934;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_2382;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_2680;
import net.minecraft.class_3532;
import net.minecraft.class_3965;
import net.minecraft.class_5134;
import net.minecraft.class_6880;
import net.minecraft.class_745;
import net.minecraft.class_9304;

public class DamageUtils
implements IMinecraft {
    private static final Map<String, Integer> PROTECTION_MAP = new HashMap<String, Integer>(){
        {
            this.put("protection", 1);
            this.put("blast_protection", 2);
            this.put("projectile_protection", 1);
            this.put("feather_falling", 1);
            this.put("fire_protection", 1);
        }
    };

    public static float getCrystalDamage(class_1297 entity, class_238 box, class_1511 crystal, boolean ignoreTerrain) {
        return DamageUtils.getDamage(entity, box, class_243.method_26410((class_2382)crystal.method_24515(), (double)0.0), 6.0f, null, ignoreTerrain);
    }

    public static float getCrystalDamage(class_1297 entity, class_238 box, class_2338 position, class_2338 exception, boolean ignoreTerrain) {
        return DamageUtils.getDamage(entity, box, class_243.method_26410((class_2382)position, (double)1.0), 6.0f, exception, ignoreTerrain);
    }

    public static float getDamage(class_1297 entity, class_238 box, class_243 vec3d, float power, class_2338 exception, boolean ignoreTerrain) {
        class_1657 player;
        if (DamageUtils.mc.field_1687.method_8407() == class_1267.field_5801) {
            return 0.0f;
        }
        if (!(entity instanceof class_745) && entity instanceof class_1657 && PlayerUtils.getGameMode(player = (class_1657)entity) == class_1934.field_9220) {
            return 0.0f;
        }
        float diameter = power * 2.0f;
        double distance = Math.sqrt((double)(box != null ? box.method_1005().method_1031(0.0, -0.9, 0.0).method_1025(vec3d) : entity.method_5707(vec3d))) / (double)diameter;
        if (distance > 1.0) {
            return 0.0f;
        }
        double exposure = (1.0 - distance) * (double)DamageUtils.getExposure(vec3d, entity.method_5829(), exception, ignoreTerrain);
        float damage = (int)((exposure * exposure + exposure) / 2.0 * 7.0 * (double)diameter + 1.0);
        if (damage <= 0.0f) {
            return 0.0f;
        }
        if (entity instanceof class_1309) {
            class_1309 livingEntity = (class_1309)entity;
            damage = DamageUtils.mc.field_1687.method_8407() == class_1267.field_5805 ? Math.min((float)(damage / 2.0f + 1.0f), (float)damage) : (DamageUtils.mc.field_1687.method_8407() == class_1267.field_5807 ? damage * 3.0f / 2.0f : damage);
            damage = class_1280.method_5496((class_1309)livingEntity, (float)damage, (class_1282)DamageUtils.mc.field_1687.method_48963().method_48807(null), (float)livingEntity.method_6096(), (float)((float)livingEntity.method_5996(class_5134.field_23725).method_6194()));
            damage = (float)((double)damage * (livingEntity.method_6059(class_1294.field_5907) ? 1.0 - (double)(livingEntity.method_6112(class_1294.field_5907).method_5578() + 1) * 0.2 : 1.0));
            damage = class_1280.method_5497((float)damage, (float)DamageUtils.getProtectionAmount((Iterable<class_1799>)livingEntity.method_5661()));
        }
        return Math.max((float)Math.round((float)damage), (float)0.1f);
    }

    public static int getProtectionAmount(Iterable<class_1799> armor) {
        int x = 0;
        for (class_1799 stack : armor) {
            x += DamageUtils.getProtectionAmount(stack);
        }
        return x;
    }

    public static int getProtectionAmount(class_1799 armor) {
        int x = 0;
        class_9304 enchantments = class_1890.method_57532((class_1799)armor);
        for (class_6880 enchantment : enchantments.method_57534()) {
            String id = enchantment.method_55840().replace((CharSequence)"minecraft:", (CharSequence)"");
            if (!PROTECTION_MAP.containsKey((Object)id)) continue;
            x += enchantments.method_57536(enchantment) * (Integer)PROTECTION_MAP.get((Object)id);
            break;
        }
        return x;
    }

    private static float getExposure(class_243 source, class_238 box, class_2338 exception, boolean ignoreTerrain) {
        int hitCount = 0;
        int count = 0;
        for (double k = 0.0; k <= 1.0; k += 0.4545454446934474) {
            for (double l = 0.0; l <= 1.0; l += 0.21739130885479366) {
                for (double m = 0.0; m <= 1.0; m += 0.4545454446934474) {
                    class_243 vec3d = new class_243(class_3532.method_16436((double)k, (double)box.field_1323, (double)box.field_1320) + 0.045454555306552624, class_3532.method_16436((double)l, (double)box.field_1322, (double)box.field_1325), class_3532.method_16436((double)m, (double)box.field_1321, (double)box.field_1324) + 0.045454555306552624);
                    if (DamageUtils.raycast(vec3d, source, exception, ignoreTerrain) == class_239.class_240.field_1333) {
                        ++hitCount;
                    }
                    ++count;
                }
            }
        }
        return (float)hitCount / (float)count;
    }

    private static class_239.class_240 raycast(class_243 start, class_243 end, class_2338 exception, boolean ignoreTerrain) {
        return (class_239.class_240)class_1922.method_17744((class_243)start, (class_243)end, null, (innerContext, blockPos) -> {
            class_2680 blockState;
            if (blockPos.equals((Object)exception)) {
                blockState = class_2246.field_10124.method_9564();
            } else {
                blockState = DamageUtils.mc.field_1687.method_8320(blockPos);
                if (blockState.method_26204().method_9520() < 600.0f && ignoreTerrain) {
                    blockState = class_2246.field_10124.method_9564();
                }
            }
            class_3965 hitResult = blockState.method_26220((class_1922)DamageUtils.mc.field_1687, blockPos).method_1092(start, end, blockPos);
            return hitResult == null ? null : hitResult.method_17783();
        }, innerContext -> class_239.class_240.field_1333);
    }

    public static int getRoundedDamage(class_1799 stack) {
        return (int)((float)(stack.method_7936() - stack.method_7919()) / (float)stack.method_7936() * 100.0f);
    }
}
