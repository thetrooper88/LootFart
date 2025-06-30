package dev.loottech.api.utilities;

import dev.loottech.api.utilities.IMinecraft;
import dev.loottech.client.modules.client.ModuleCommands;
import net.minecraft.class_124;
import net.minecraft.class_2561;
import net.minecraft.class_5250;

public class ChatUtils
implements IMinecraft {
    public static void sendMessage(String message) {
        if (ChatUtils.mc.field_1724 == null || ChatUtils.mc.field_1687 == null || ChatUtils.mc.field_1705 == null) {
            return;
        }
        class_5250 component = class_2561.method_43470((String)(ChatUtils.getWatermark() + (!ModuleCommands.INSTANCE.watermarkMode.getValue().equals((Object)ModuleCommands.WatermarkModes.None) ? " " : "") + String.valueOf((Object)ModuleCommands.getFirstColor()) + message));
        ChatUtils.mc.field_1705.method_1743().method_1812((class_2561)component);
    }

    public static void sendMessage(class_2561 message) {
        if (ChatUtils.mc.field_1724 == null || ChatUtils.mc.field_1687 == null || ChatUtils.mc.field_1705 == null) {
            return;
        }
        ChatUtils.mc.field_1705.method_1743().method_1812(message);
    }

    public static void sendMessage(String message, int id) {
        if (ChatUtils.mc.field_1724 == null || ChatUtils.mc.field_1687 == null || ChatUtils.mc.field_1705 == null) {
            return;
        }
        class_5250 component = class_2561.method_43470((String)(ChatUtils.getWatermark() + (!ModuleCommands.INSTANCE.watermarkMode.getValue().equals((Object)ModuleCommands.WatermarkModes.None) ? " " : "") + String.valueOf((Object)ModuleCommands.getFirstColor()) + message));
        ChatUtils.mc.field_1705.method_1743().method_1812((class_2561)component);
    }

    public static void sendMessage(String message, String name) {
        if (ChatUtils.mc.field_1724 == null || ChatUtils.mc.field_1687 == null || ChatUtils.mc.field_1705 == null) {
            return;
        }
        class_5250 component = class_2561.method_43470((String)(ChatUtils.getWatermark() + (!ModuleCommands.INSTANCE.watermarkMode.getValue().equals((Object)ModuleCommands.WatermarkModes.None) ? " " : "") + String.valueOf((Object)class_124.field_1075) + "[" + name + "]: " + String.valueOf((Object)ModuleCommands.getFirstColor()) + message));
        ChatUtils.mc.field_1705.method_1743().method_1812((class_2561)component);
    }

    public static void sendMessage(String message, String name, int id) {
        if (ChatUtils.mc.field_1724 == null || ChatUtils.mc.field_1687 == null || ChatUtils.mc.field_1705 == null) {
            return;
        }
        class_5250 component = class_2561.method_43470((String)(ChatUtils.getWatermark() + (!ModuleCommands.INSTANCE.watermarkMode.getValue().equals((Object)ModuleCommands.WatermarkModes.None) ? " " : "") + String.valueOf((Object)class_124.field_1075) + "[" + name + "]: " + String.valueOf((Object)ModuleCommands.getFirstColor()) + message));
        ChatUtils.mc.field_1705.method_1743().method_1812((class_2561)component);
    }

    public static void sendRawMessage(String message) {
        if (ChatUtils.mc.field_1724 == null || ChatUtils.mc.field_1687 == null || ChatUtils.mc.field_1705 == null) {
            return;
        }
        class_5250 component = class_2561.method_43470((String)message);
        ChatUtils.mc.field_1724.method_43496((class_2561)component);
    }

    public static String getWatermark() {
        if (!ModuleCommands.INSTANCE.watermarkMode.getValue().equals((Object)ModuleCommands.WatermarkModes.None)) {
            return String.valueOf((Object)ModuleCommands.getSecondWatermarkColor()) + ModuleCommands.INSTANCE.firstSymbol.getValue() + String.valueOf((Object)ModuleCommands.getFirstWatermarkColor()) + (ModuleCommands.INSTANCE.watermarkMode.getValue().equals((Object)ModuleCommands.WatermarkModes.Custom) ? ModuleCommands.INSTANCE.watermarkText.getValue() : (ModuleCommands.INSTANCE.watermarkMode.getValue().equals((Object)ModuleCommands.WatermarkModes.Japanese) ? "\u30ef\u30f3\u30c0\u30fc\u30db\u30a8\u30fc\u30eb" : "LootTech")) + String.valueOf((Object)ModuleCommands.getSecondWatermarkColor()) + ModuleCommands.INSTANCE.secondSymbol.getValue();
        }
        return "";
    }
}
