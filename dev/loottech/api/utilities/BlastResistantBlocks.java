package dev.loottech.api.utilities;

import dev.loottech.api.utilities.Util;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import java.util.Collection;
import java.util.Set;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2680;

public class BlastResistantBlocks
implements Util {
    private static final Set<class_2248> BLAST_RESISTANT = new ReferenceOpenHashSet((Collection)Set.of((Object)class_2246.field_10540, (Object)class_2246.field_10535, (Object)class_2246.field_10485, (Object)class_2246.field_10443, (Object)class_2246.field_10327));
    private static final Set<class_2248> UNBREAKABLE = new ReferenceOpenHashSet((Collection)Set.of((Object)class_2246.field_9987, (Object)class_2246.field_10525, (Object)class_2246.field_10395, (Object)class_2246.field_10398, (Object)class_2246.field_10499));

    public static boolean isBreakable(class_2338 pos) {
        if (BlastResistantBlocks.mc.field_1687 == null) {
            return false;
        }
        return BlastResistantBlocks.isBreakable(BlastResistantBlocks.mc.field_1687.method_8320(pos).method_26204());
    }

    public static boolean isBreakable(class_2248 block) {
        return !UNBREAKABLE.contains((Object)block);
    }

    public static boolean isUnbreakable(class_2338 pos) {
        if (BlastResistantBlocks.mc.field_1687 == null) {
            return false;
        }
        return BlastResistantBlocks.isUnbreakable(BlastResistantBlocks.mc.field_1687.method_8320(pos).method_26204());
    }

    public static boolean isUnbreakable(class_2248 block) {
        return UNBREAKABLE.contains((Object)block);
    }

    public static boolean isBlastResistant(class_2338 pos) {
        if (BlastResistantBlocks.mc.field_1687 == null) {
            return false;
        }
        return BlastResistantBlocks.isBlastResistant(BlastResistantBlocks.mc.field_1687.method_8320(pos).method_26204());
    }

    public static boolean isBlastResistant(class_2680 state) {
        return BlastResistantBlocks.isBlastResistant(state.method_26204());
    }

    public static boolean isBlastResistant(class_2248 block) {
        return BLAST_RESISTANT.contains((Object)block);
    }
}
