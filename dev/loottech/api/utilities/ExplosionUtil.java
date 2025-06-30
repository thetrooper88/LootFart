package dev.loottech.api.utilities;

import dev.loottech.api.utilities.EnchantmentUtils;
import dev.loottech.api.utilities.Util;
import java.util.Set;
import java.util.function.BiFunction;
import net.minecraft.class_1280;
import net.minecraft.class_1282;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1299;
import net.minecraft.class_1309;
import net.minecraft.class_1738;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1890;
import net.minecraft.class_1893;
import net.minecraft.class_1922;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2680;
import net.minecraft.class_3965;
import net.minecraft.class_5132;
import net.minecraft.class_5134;
import net.minecraft.class_5135;
import net.minecraft.class_6880;
import org.apache.commons.lang3.mutable.MutableInt;

public class ExplosionUtil
implements Util {
    public static double getDamageTo(class_1297 entity, class_243 explosion, boolean assumeBestArmor) {
        return ExplosionUtil.getDamageTo(entity, explosion, false, assumeBestArmor);
    }

    public static double getDamageTo(class_1297 entity, class_243 explosion, boolean ignoreTerrain, boolean assumeBestArmor) {
        return ExplosionUtil.getDamageTo(entity, explosion, ignoreTerrain, 12.0f, 0, assumeBestArmor);
    }

    public static double getDamageTo(class_1297 entity, class_243 explosion, boolean ignoreTerrain, int extrapolationTicks, boolean assumeBestArmor) {
        return ExplosionUtil.getDamageTo(entity, explosion, ignoreTerrain, 12.0f, extrapolationTicks, assumeBestArmor);
    }

    public static double getDamageTo(class_1297 entity, class_243 explosion, IgnoreTerrain ignoreTerrain, boolean assumeBestArmor) {
        return ExplosionUtil.getDamageTo(entity, explosion, ignoreTerrain, 12.0f, 0, assumeBestArmor);
    }

    public static double getDamageTo(class_1297 entity, class_243 explosion, IgnoreTerrain ignoreTerrain, int extrapolationTicks, boolean assumeBestArmor) {
        return ExplosionUtil.getDamageTo(entity, explosion, ignoreTerrain, 12.0f, extrapolationTicks, assumeBestArmor);
    }

    public static double getDamageTo(class_1297 entity, class_243 explosion, IgnoreTerrain ignoreTerrain, float power, int extrapolationTicks, boolean assumeBestArmor) {
        double x = entity.method_23317();
        double y = entity.method_23318();
        double z = entity.method_23321();
        class_243 vec3d2 = class_243.field_1353;
        if (extrapolationTicks != 0) {
            double ox = (x - entity.field_6014) * (double)extrapolationTicks;
            double oy = (y - entity.field_6036) * (double)extrapolationTicks * 0.3;
            double oz = (z - entity.field_5969) * (double)extrapolationTicks;
            x += ox;
            y += oy;
            z += oz;
            vec3d2 = new class_243(ox, oy, oz);
        }
        class_243 vec3d = new class_243(x, y, z);
        double d = Math.sqrt((double)vec3d.method_1025(explosion));
        double ab = ExplosionUtil.getExposure(explosion, entity.method_5829().method_997(vec3d2), ignoreTerrain);
        double w = d / (double)power;
        double ac = (1.0 - w) * ab;
        double dmg = (int)((ac * ac + ac) / 2.0 * 7.0 * 12.0 + 1.0);
        dmg = ExplosionUtil.getReduction(entity, ExplosionUtil.mc.field_1687.method_48963().method_48807(null), dmg, assumeBestArmor);
        return Math.max((double)0.0, (double)dmg);
    }

    public static double getDamageTo(class_1297 entity, class_243 explosion, boolean ignoreTerrain, Set<class_2338> ignoreBlocks, boolean assumeBestArmor) {
        return ExplosionUtil.getDamageTo(entity, explosion, ignoreTerrain, 12.0f, ignoreBlocks, 0, assumeBestArmor);
    }

    public static double getDamageTo(class_1297 entity, class_243 explosion, boolean ignoreTerrain, Set<class_2338> ignoreBlocks, int extrapolationTicks, boolean assumeBestArmor) {
        return ExplosionUtil.getDamageTo(entity, explosion, ignoreTerrain, 12.0f, ignoreBlocks, extrapolationTicks, assumeBestArmor);
    }

    public static double getDamageTo(class_1297 entity, class_243 explosion, boolean ignoreTerrain, float power, Set<class_2338> ignoreBlocks, int extrapolationTicks, boolean assumeBestArmor) {
        double x = entity.method_23317();
        double y = entity.method_23318();
        double z = entity.method_23321();
        class_243 vec3d2 = class_243.field_1353;
        if (extrapolationTicks != 0) {
            double ox = (x - entity.field_6014) * (double)extrapolationTicks;
            double oy = (y - entity.field_6036) * (double)extrapolationTicks * 0.3;
            double oz = (z - entity.field_5969) * (double)extrapolationTicks;
            x += ox;
            y += oy;
            z += oz;
            vec3d2 = new class_243(ox, oy, oz);
        }
        class_243 vec3d = new class_243(x, y, z);
        double d = Math.sqrt((double)vec3d.method_1025(explosion));
        double ab = ExplosionUtil.getExposure(explosion, entity.method_5829().method_997(vec3d2), ignoreTerrain ? IgnoreTerrain.BLAST : IgnoreTerrain.NONE, ignoreBlocks);
        double w = d / (double)power;
        double ac = (1.0 - w) * ab;
        double dmg = (int)((ac * ac + ac) / 2.0 * 7.0 * 12.0 + 1.0);
        dmg = ExplosionUtil.getReduction(entity, ExplosionUtil.mc.field_1687.method_48963().method_48807(null), dmg, assumeBestArmor);
        return Math.max((double)0.0, (double)dmg);
    }

    public static double getDamageTo(class_1297 entity, class_243 explosion, IgnoreTerrain ignoreTerrain, Set<class_2338> ignoreBlocks, boolean assumeBestArmor) {
        return ExplosionUtil.getDamageTo(entity, explosion, ignoreTerrain, 12.0f, ignoreBlocks, 0, assumeBestArmor);
    }

    public static double getDamageTo(class_1297 entity, class_243 explosion, IgnoreTerrain ignoreTerrain, float power, Set<class_2338> ignoreBlocks, int extrapolationTicks, boolean assumeBestArmor) {
        double x = entity.method_23317();
        double y = entity.method_23318();
        double z = entity.method_23321();
        class_243 vec3d2 = class_243.field_1353;
        if (extrapolationTicks != 0) {
            double ox = (x - entity.field_6014) * (double)extrapolationTicks;
            double oy = (y - entity.field_6036) * (double)extrapolationTicks * 0.3;
            double oz = (z - entity.field_5969) * (double)extrapolationTicks;
            x += ox;
            y += oy;
            z += oz;
            vec3d2 = new class_243(ox, oy, oz);
        }
        class_243 vec3d = new class_243(x, y, z);
        double d = Math.sqrt((double)vec3d.method_1025(explosion));
        double ab = ExplosionUtil.getExposure(explosion, entity.method_5829().method_997(vec3d2), ignoreTerrain, ignoreBlocks);
        double w = d / (double)power;
        double ac = (1.0 - w) * ab;
        double dmg = (int)((ac * ac + ac) / 2.0 * 7.0 * 12.0 + 1.0);
        dmg = ExplosionUtil.getReduction(entity, ExplosionUtil.mc.field_1687.method_48963().method_48807(null), dmg, assumeBestArmor);
        return Math.max((double)0.0, (double)dmg);
    }

    public static double getDamageTo(class_1297 entity, class_243 explosion, boolean ignoreTerrain, float power, int extrapolationTicks, boolean assumeBestArmor) {
        double x = entity.method_23317();
        double y = entity.method_23318();
        double z = entity.method_23321();
        class_243 vec3d2 = class_243.field_1353;
        if (extrapolationTicks != 0) {
            double ox = (x - entity.field_6014) * (double)extrapolationTicks;
            double oy = (y - entity.field_6036) * (double)extrapolationTicks * 0.3;
            double oz = (z - entity.field_5969) * (double)extrapolationTicks;
            x += ox;
            y += oy;
            z += oz;
            vec3d2 = new class_243(ox, oy, oz);
        }
        class_243 vec3d = new class_243(x, y, z);
        double d = Math.sqrt((double)vec3d.method_1025(explosion));
        double ab = ExplosionUtil.getExposure(explosion, entity.method_5829().method_997(vec3d2), ignoreTerrain ? IgnoreTerrain.BLAST : IgnoreTerrain.NONE);
        double w = d / (double)power;
        double ac = (1.0 - w) * ab;
        double dmg = (int)((ac * ac + ac) / 2.0 * 7.0 * 12.0 + 1.0);
        dmg = ExplosionUtil.getReduction(entity, ExplosionUtil.mc.field_1687.method_48963().method_48807(null), dmg, assumeBestArmor);
        return Math.max((double)0.0, (double)dmg);
    }

    static double getDamageToPos(class_243 pos, class_1297 entity, class_243 explosion, boolean ignoreTerrain, boolean assumeBestArmor) {
        class_238 bb = entity.method_5829();
        double dx = pos.method_10216() - bb.field_1323;
        double dy = pos.method_10214() - bb.field_1322;
        double dz = pos.method_10215() - bb.field_1321;
        class_238 box = bb.method_989(dx, dy, dz);
        RaycastFactory raycastFactory = ExplosionUtil.getRaycastFactory(ignoreTerrain ? IgnoreTerrain.BLAST : IgnoreTerrain.NONE);
        double ab = ExplosionUtil.getExposure(explosion, box, raycastFactory);
        double w = Math.sqrt((double)pos.method_1025(explosion)) / 12.0;
        double ac = (1.0 - w) * ab;
        double dmg = (int)((ac * ac + ac) / 2.0 * 7.0 * 12.0 + 1.0);
        dmg = ExplosionUtil.getReduction(entity, ExplosionUtil.mc.field_1687.method_48963().method_48807(null), dmg, assumeBestArmor);
        return Math.max((double)0.0, (double)dmg);
    }

    private static double getReduction(class_1297 entity, class_1282 damageSource, double damage, boolean assumeBestArmor) {
        if (damageSource.method_5514()) {
            switch (ExplosionUtil.mc.field_1687.method_8407()) {
                case field_5805: {
                    damage = Math.min((double)(damage / 2.0 + 1.0), (double)damage);
                    break;
                }
                case field_5807: {
                    damage *= 1.5;
                }
            }
        }
        if (entity instanceof class_1309) {
            class_1309 livingEntity = (class_1309)entity;
            damage = class_1280.method_5496((class_1309)livingEntity, (float)((float)damage), (class_1282)damageSource, (float)ExplosionUtil.getArmor(livingEntity), (float)((float)livingEntity.method_45325(class_5134.field_23725)));
            damage = ExplosionUtil.getResistanceReduction(livingEntity, damage);
            damage = ExplosionUtil.getProtectionReduction((class_1297)livingEntity, damage, damageSource, assumeBestArmor);
        }
        return Math.max((double)damage, (double)0.0);
    }

    private static float getArmor(class_1309 entity) {
        return (float)Math.floor((double)entity.method_45325(class_5134.field_23724));
    }

    private static float getProtectionReduction(class_1297 player, double damage, class_1282 source, boolean assumeBestArmor) {
        if (player instanceof class_1309) {
            class_1309 livingEntity = (class_1309)player;
            float protLevel = ExplosionUtil.getProtectionAmount((Iterable<class_1799>)livingEntity.method_5661(), assumeBestArmor);
            return class_1280.method_5497((float)((float)damage), (float)protLevel);
        }
        return 0.0f;
    }

    private static float getProtectionAmount(Iterable<class_1799> equipment, boolean assumeBestArmor) {
        MutableInt mutableInt = new MutableInt();
        equipment.forEach(i -> {
            if (assumeBestArmor && EnchantmentUtils.isFakeEnchant2b2t(i)) {
                class_1738 armorItem;
                class_1792 patt0$temp = i.method_7909();
                mutableInt.add(patt0$temp instanceof class_1738 && (armorItem = (class_1738)patt0$temp).method_48398() == class_1738.class_8051.field_41936 ? 8 : 4);
            } else {
                int modifierBlast = class_1890.method_8225((class_6880)((class_6880)ExplosionUtil.mc.field_1687.method_30349().method_30530(class_1893.field_9107.method_58273()).method_40264(class_1893.field_9107).get()), (class_1799)i);
                int modifier = class_1890.method_8225((class_6880)((class_6880)ExplosionUtil.mc.field_1687.method_30349().method_30530(class_1893.field_9111.method_58273()).method_40264(class_1893.field_9111).get()), (class_1799)i);
                mutableInt.add(modifierBlast * 2 + modifier);
            }
        });
        return mutableInt.intValue();
    }

    private static double getResistanceReduction(class_1309 player, double damage) {
        class_1293 resistance = player.method_6112(class_1294.field_5907);
        if (resistance != null) {
            int lvl = resistance.method_5578() + 1;
            damage *= (double)(1.0f - (float)lvl * 0.2f);
        }
        return Math.max((double)damage, (double)0.0);
    }

    private static <T extends class_1309> class_5132 getDefaultForEntity(T entity) {
        return class_5135.method_26873((class_1299)entity.method_5864());
    }

    private static float getExposure(class_243 source, class_238 box, IgnoreTerrain ignoreTerrain, Set<class_2338> ignoreBlocks) {
        RaycastFactory raycastFactory = ExplosionUtil.getRaycastFactory(ignoreTerrain, ignoreBlocks);
        return ExplosionUtil.getExposure(source, box, raycastFactory);
    }

    private static float getExposure(class_243 source, class_238 box, IgnoreTerrain ignoreTerrain) {
        RaycastFactory raycastFactory = ExplosionUtil.getRaycastFactory(ignoreTerrain);
        return ExplosionUtil.getExposure(source, box, raycastFactory);
    }

    private static float getExposure(class_243 source, class_238 box, RaycastFactory raycastFactory) {
        double xDiff = box.field_1320 - box.field_1323;
        double yDiff = box.field_1325 - box.field_1322;
        double zDiff = box.field_1324 - box.field_1321;
        double xStep = 1.0 / (xDiff * 2.0 + 1.0);
        double yStep = 1.0 / (yDiff * 2.0 + 1.0);
        double zStep = 1.0 / (zDiff * 2.0 + 1.0);
        if (xStep > 0.0 && yStep > 0.0 && zStep > 0.0) {
            int misses = 0;
            int hits = 0;
            double xOffset = (1.0 - Math.floor((double)(1.0 / xStep)) * xStep) * 0.5;
            double zOffset = (1.0 - Math.floor((double)(1.0 / zStep)) * zStep) * 0.5;
            xStep *= xDiff;
            yStep *= yDiff;
            zStep *= zDiff;
            double startX = box.field_1323 + xOffset;
            double startY = box.field_1322;
            double startZ = box.field_1321 + zOffset;
            double endX = box.field_1320 + xOffset;
            double endY = box.field_1325;
            double endZ = box.field_1324 + zOffset;
            for (double x = startX; x <= endX; x += xStep) {
                for (double y = startY; y <= endY; y += yStep) {
                    for (double z = startZ; z <= endZ; z += zStep) {
                        class_243 position = new class_243(x, y, z);
                        if (ExplosionUtil.raycast(new ExposureRaycastContext(position, source), raycastFactory) == null) {
                            ++misses;
                        }
                        ++hits;
                    }
                }
            }
            return (float)misses / (float)hits;
        }
        return 0.0f;
    }

    private static RaycastFactory getRaycastFactory(IgnoreTerrain ignoreTerrain, Set<class_2338> ignoreBlocks) {
        if (ignoreTerrain == IgnoreTerrain.BLAST) {
            return (context, blockPos) -> {
                if (ignoreBlocks.contains(blockPos)) {
                    return null;
                }
                class_2680 blockState = ExplosionUtil.mc.field_1687.method_8320(blockPos);
                if (blockState.method_26204().method_9520() < 600.0f) {
                    return null;
                }
                return blockState.method_26220((class_1922)ExplosionUtil.mc.field_1687, blockPos).method_1092(context.start(), context.end(), blockPos);
            };
        }
        if (ignoreTerrain == IgnoreTerrain.ALL) {
            return (context, blockPos) -> null;
        }
        return (context, blockPos) -> {
            if (ignoreBlocks.contains(blockPos)) {
                return null;
            }
            class_2680 blockState = ExplosionUtil.mc.field_1687.method_8320(blockPos);
            return blockState.method_26220((class_1922)ExplosionUtil.mc.field_1687, blockPos).method_1092(context.start(), context.end(), blockPos);
        };
    }

    private static RaycastFactory getRaycastFactory(IgnoreTerrain ignoreTerrain) {
        if (ignoreTerrain == IgnoreTerrain.BLAST) {
            return (context, blockPos) -> {
                class_2680 blockState = ExplosionUtil.mc.field_1687.method_8320(blockPos);
                if (blockState.method_26204().method_9520() < 600.0f) {
                    return null;
                }
                return blockState.method_26220((class_1922)ExplosionUtil.mc.field_1687, blockPos).method_1092(context.start(), context.end(), blockPos);
            };
        }
        if (ignoreTerrain == IgnoreTerrain.ALL) {
            return (context, blockPos) -> null;
        }
        return (context, blockPos) -> {
            class_2680 blockState = ExplosionUtil.mc.field_1687.method_8320(blockPos);
            return blockState.method_26220((class_1922)ExplosionUtil.mc.field_1687, blockPos).method_1092(context.start(), context.end(), blockPos);
        };
    }

    private static class_3965 raycast(ExposureRaycastContext context, RaycastFactory raycastFactory) {
        return (class_3965)class_1922.method_17744((class_243)context.start, (class_243)context.end, (Object)((Object)context), (BiFunction)raycastFactory, ctx -> null);
    }

    public static enum IgnoreTerrain {
        ALL,
        BLAST,
        NONE;

    }

    @FunctionalInterface
    public static interface RaycastFactory
    extends BiFunction<ExposureRaycastContext, class_2338, class_3965> {
    }

    public record ExposureRaycastContext(class_243 start, class_243 end) {
    }
}
