package dev.loottech.api.utilities;

import dev.loottech.api.utilities.IMinecraft;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.util.Set;
import net.minecraft.class_1304;
import net.minecraft.class_1309;
import net.minecraft.class_1799;
import net.minecraft.class_1887;
import net.minecraft.class_1890;
import net.minecraft.class_1893;
import net.minecraft.class_5321;
import net.minecraft.class_6880;
import net.minecraft.class_9304;
import net.minecraft.class_9334;

public class EnchantmentUtils {
    public static int getLevel(class_5321<class_1887> key, class_1799 stack) {
        if (stack.method_7960()) {
            return 0;
        }
        for (Object2IntMap.Entry enchantment : stack.method_58657().method_57539()) {
            if (!((class_6880)enchantment.getKey()).method_40225(key)) continue;
            return enchantment.getIntValue();
        }
        return 0;
    }

    public static int getLevel(class_1799 stack, class_5321<class_1887> enchantmentRegistryKey) {
        if (!stack.method_57353().method_57832(class_9334.field_49633)) {
            return 0;
        }
        for (Object2IntMap.Entry e : ((class_9304)stack.method_57353().method_57829(class_9334.field_49633)).method_57539()) {
            if (!((class_6880)e.getKey()).method_40230().isPresent() || !((class_5321)((class_6880)e.getKey()).method_40230().get()).equals(enchantmentRegistryKey)) continue;
            return e.getIntValue();
        }
        return 0;
    }

    public static boolean isFakeEnchant2b2t(class_1799 itemStack) {
        Set enchants = class_1890.method_57532((class_1799)itemStack).method_57539();
        if (enchants.size() > 1) {
            return false;
        }
        for (Object2IntMap.Entry e : enchants) {
            class_6880 enchantment = (class_6880)e.getKey();
            int lvl = e.getIntValue();
            if (lvl != 0 || !enchantment.method_40230().isPresent() || enchantment.method_40230().get() != class_1893.field_9111) continue;
            return true;
        }
        return false;
    }

    public static int getLevel(class_5321<class_1887> key, class_1304 slot, class_1309 entity) {
        return EnchantmentUtils.getLevel(key, entity.method_6118(slot));
    }

    public static int getLevel(class_5321<class_1887> key, class_1304 slot) {
        return EnchantmentUtils.getLevel(key, slot, (class_1309)IMinecraft.mc.field_1724);
    }

    public static boolean has(class_5321<class_1887> key, class_1799 stack) {
        return EnchantmentUtils.getLevel(key, stack) > 0;
    }

    public static boolean has(class_5321<class_1887> key, class_1304 slot, class_1309 entity) {
        return EnchantmentUtils.getLevel(key, slot, entity) > 0;
    }

    public static boolean has(class_5321<class_1887> key, class_1304 slot) {
        return EnchantmentUtils.getLevel(key, slot) > 0;
    }
}
