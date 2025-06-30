package dev.loottech.api.utilities;

import dev.loottech.api.utilities.EnchantmentUtils;
import dev.loottech.api.utilities.IMinecraft;
import net.minecraft.class_1292;
import net.minecraft.class_1294;
import net.minecraft.class_1304;
import net.minecraft.class_1309;
import net.minecraft.class_1799;
import net.minecraft.class_1887;
import net.minecraft.class_1893;
import net.minecraft.class_2338;
import net.minecraft.class_2680;
import net.minecraft.class_3486;
import net.minecraft.class_5321;

public class InteractionUtils {
    public static double getBlockBreakingSpeed(int slot, class_2338 pos) {
        return InteractionUtils.getBlockBreakingSpeed(slot, IMinecraft.mc.field_1687.method_8320(pos));
    }

    public static double getBlockBreakingSpeed(int slot, class_2680 block) {
        float hardness;
        class_1799 tool;
        int efficiency;
        double speed = ((class_1799)IMinecraft.mc.field_1724.method_31548().field_7547.get(slot)).method_7924(block);
        if (speed > 1.0 && (efficiency = EnchantmentUtils.getLevel((class_5321<class_1887>)class_1893.field_9131, tool = IMinecraft.mc.field_1724.method_31548().method_5438(slot))) > 0 && !tool.method_7960()) {
            speed += (double)(efficiency * efficiency + 1);
        }
        if (class_1292.method_5576((class_1309)IMinecraft.mc.field_1724)) {
            speed *= (double)(1.0f + (float)(class_1292.method_5575((class_1309)IMinecraft.mc.field_1724) + 1) * 0.2f);
        }
        if (IMinecraft.mc.field_1724.method_6059(class_1294.field_5901)) {
            float k = switch (IMinecraft.mc.field_1724.method_6112(class_1294.field_5901).method_5578()) {
                case 0 -> 0.3f;
                case 1 -> 0.09f;
                case 2 -> 0.0027f;
                default -> 8.1E-4f;
            };
            speed *= (double)k;
        }
        if (IMinecraft.mc.field_1724.method_5777(class_3486.field_15517) && EnchantmentUtils.has((class_5321<class_1887>)class_1893.field_9105, class_1304.field_6169)) {
            speed /= 5.0;
        }
        if (!IMinecraft.mc.field_1724.method_24828()) {
            speed /= 5.0;
        }
        if ((hardness = block.method_26214(null, null)) == -1.0f) {
            return 0.0;
        }
        float ticks = (float)(Math.floor((double)(1.0 / (speed /= (double)(hardness / (float)(!block.method_29291() || ((class_1799)IMinecraft.mc.field_1724.method_31548().field_7547.get(slot)).method_7951(block) ? 30 : 100))))) + 1.0);
        return (long)(ticks / 20.0f * 1000.0f);
    }
}
