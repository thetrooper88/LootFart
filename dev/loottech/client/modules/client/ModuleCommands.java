package dev.loottech.client.modules.client;

import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.client.values.impl.ValueCategory;
import dev.loottech.client.values.impl.ValueEnum;
import dev.loottech.client.values.impl.ValueString;
import net.minecraft.class_124;

@RegisterModule(name="Commands", description="Let's you customize commands and sending text.", category=Module.Category.CLIENT, persistent=true, drawn=false)
public class ModuleCommands
extends Module {
    public static ModuleCommands INSTANCE;
    public final ValueCategory watermarkCategory = new ValueCategory("Watermark", "The category for the Watermark.");
    public final ValueEnum watermarkMode = new ValueEnum("WatermarkMode", "Mode", "The mode for the watermark.", this.watermarkCategory, WatermarkModes.Normal);
    public final ValueString watermarkText = new ValueString("WatermarkText", "Text", "The watermark text.", this.watermarkCategory, "LootTech");
    public final ValueString firstSymbol = new ValueString("WatermarkFirstSymbol", "First Symbol", "The first symbol on the watermark.", this.watermarkCategory, "[");
    public final ValueString secondSymbol = new ValueString("WatermarkSecondSymbol", "Second Symbol", "The second symbol on the watermark.", this.watermarkCategory, "]");
    public final ValueCategory firstWatermarkColorCategory = new ValueCategory("First Mark Color", "The colors for the first color on the Watermark.");
    public final ValueEnum firstWatermarkColor = new ValueEnum("FirstWatermarkColor", "Color", "The color of the first watermark color.", this.firstWatermarkColorCategory, ColorModes.Purple);
    public final ValueEnum firstWatermarkBrightness = new ValueEnum("FirstWatermarkBrightness", "Brightness", "The brightness of the second watermark color.", this.firstWatermarkColorCategory, LightModes.Light);
    public final ValueCategory secondWatermarkColorCategory = new ValueCategory("Second Mark Color", "The colors for the second color on the Watermark.");
    public final ValueEnum secondWatermarkColor = new ValueEnum("SecondWatermarkColor", "Color", "The color of the second watermark color.", this.secondWatermarkColorCategory, ColorModes.Purple);
    public final ValueEnum secondWatermarkBrightness = new ValueEnum("SecondWatermarkBrightness", "Brightness", "The brightness of the second watermark color.", this.secondWatermarkColorCategory, LightModes.Dark);
    public final ValueCategory firstColorCategory = new ValueCategory("First Color", "The first color in the chat sending.");
    public final ValueEnum firstColorMode = new ValueEnum("FirstColorMode", "Color", "The color for the First Color.", this.firstColorCategory, ColorModes.Purple);
    public final ValueEnum firstColorBrightness = new ValueEnum("FirstColorBrightness", "Brightness", "The brightness for the First Color.", this.firstColorCategory, LightModes.Light);
    public final ValueCategory secondColorCategory = new ValueCategory("Second Color", "The second color in the chat sending.");
    public final ValueEnum secondColorMode = new ValueEnum("SecondColorMode", "Color", "The color for the Second Color.", this.secondColorCategory, ColorModes.Purple);
    public final ValueEnum secondColorBrightness = new ValueEnum("SecondColorBrightness", "Brightness", "The brightness for the Second Color.", this.secondColorCategory, LightModes.Dark);

    public ModuleCommands() {
        INSTANCE = this;
    }

    public static class_124 getFirstColor() {
        switch (((ColorModes)ModuleCommands.INSTANCE.firstColorMode.getValue()).ordinal()) {
            case 1: {
                if (ModuleCommands.INSTANCE.firstColorBrightness.getValue().equals((Object)LightModes.Light)) {
                    return class_124.field_1063;
                }
                return class_124.field_1074;
            }
            case 2: {
                if (ModuleCommands.INSTANCE.firstColorBrightness.getValue().equals((Object)LightModes.Light)) {
                    return class_124.field_1080;
                }
                return class_124.field_1063;
            }
            case 3: {
                if (ModuleCommands.INSTANCE.firstColorBrightness.getValue().equals((Object)LightModes.Light)) {
                    return class_124.field_1078;
                }
                return class_124.field_1058;
            }
            case 4: {
                if (ModuleCommands.INSTANCE.firstColorBrightness.getValue().equals((Object)LightModes.Light)) {
                    return class_124.field_1060;
                }
                return class_124.field_1077;
            }
            case 5: {
                if (ModuleCommands.INSTANCE.firstColorBrightness.getValue().equals((Object)LightModes.Light)) {
                    return class_124.field_1075;
                }
                return class_124.field_1062;
            }
            case 6: {
                if (ModuleCommands.INSTANCE.firstColorBrightness.getValue().equals((Object)LightModes.Light)) {
                    return class_124.field_1061;
                }
                return class_124.field_1079;
            }
            case 7: {
                if (ModuleCommands.INSTANCE.firstColorBrightness.getValue().equals((Object)LightModes.Light)) {
                    return class_124.field_1054;
                }
                return class_124.field_1065;
            }
            case 9: {
                if (ModuleCommands.INSTANCE.firstColorBrightness.getValue().equals((Object)LightModes.Light)) {
                    return class_124.field_1076;
                }
                return class_124.field_1064;
            }
        }
        if (ModuleCommands.INSTANCE.firstColorBrightness.getValue().equals((Object)LightModes.Light)) {
            return class_124.field_1068;
        }
        return class_124.field_1080;
    }

    public static class_124 getSecondColor() {
        switch (((ColorModes)ModuleCommands.INSTANCE.secondColorMode.getValue()).ordinal()) {
            case 1: {
                if (ModuleCommands.INSTANCE.secondColorBrightness.getValue().equals((Object)LightModes.Light)) {
                    return class_124.field_1063;
                }
                return class_124.field_1074;
            }
            case 2: {
                if (ModuleCommands.INSTANCE.secondColorBrightness.getValue().equals((Object)LightModes.Light)) {
                    return class_124.field_1080;
                }
                return class_124.field_1063;
            }
            case 3: {
                if (ModuleCommands.INSTANCE.secondColorBrightness.getValue().equals((Object)LightModes.Light)) {
                    return class_124.field_1078;
                }
                return class_124.field_1058;
            }
            case 4: {
                if (ModuleCommands.INSTANCE.secondColorBrightness.getValue().equals((Object)LightModes.Light)) {
                    return class_124.field_1060;
                }
                return class_124.field_1077;
            }
            case 5: {
                if (ModuleCommands.INSTANCE.secondColorBrightness.getValue().equals((Object)LightModes.Light)) {
                    return class_124.field_1075;
                }
                return class_124.field_1062;
            }
            case 6: {
                if (ModuleCommands.INSTANCE.secondColorBrightness.getValue().equals((Object)LightModes.Light)) {
                    return class_124.field_1061;
                }
                return class_124.field_1079;
            }
            case 7: {
                if (ModuleCommands.INSTANCE.secondColorBrightness.getValue().equals((Object)LightModes.Light)) {
                    return class_124.field_1054;
                }
                return class_124.field_1065;
            }
            case 9: {
                if (ModuleCommands.INSTANCE.secondColorBrightness.getValue().equals((Object)LightModes.Light)) {
                    return class_124.field_1076;
                }
                return class_124.field_1064;
            }
        }
        if (ModuleCommands.INSTANCE.secondColorBrightness.getValue().equals((Object)LightModes.Light)) {
            return class_124.field_1068;
        }
        return class_124.field_1080;
    }

    public static class_124 getFirstWatermarkColor() {
        switch (((ColorModes)ModuleCommands.INSTANCE.firstWatermarkColor.getValue()).ordinal()) {
            case 1: {
                if (ModuleCommands.INSTANCE.firstWatermarkBrightness.getValue().equals((Object)LightModes.Light)) {
                    return class_124.field_1063;
                }
                return class_124.field_1074;
            }
            case 2: {
                if (ModuleCommands.INSTANCE.firstWatermarkBrightness.getValue().equals((Object)LightModes.Light)) {
                    return class_124.field_1080;
                }
                return class_124.field_1063;
            }
            case 3: {
                if (ModuleCommands.INSTANCE.firstWatermarkBrightness.getValue().equals((Object)LightModes.Light)) {
                    return class_124.field_1078;
                }
                return class_124.field_1058;
            }
            case 4: {
                if (ModuleCommands.INSTANCE.firstWatermarkBrightness.getValue().equals((Object)LightModes.Light)) {
                    return class_124.field_1060;
                }
                return class_124.field_1077;
            }
            case 5: {
                if (ModuleCommands.INSTANCE.firstWatermarkBrightness.getValue().equals((Object)LightModes.Light)) {
                    return class_124.field_1075;
                }
                return class_124.field_1062;
            }
            case 6: {
                if (ModuleCommands.INSTANCE.firstWatermarkBrightness.getValue().equals((Object)LightModes.Light)) {
                    return class_124.field_1061;
                }
                return class_124.field_1079;
            }
            case 7: {
                if (ModuleCommands.INSTANCE.firstWatermarkBrightness.getValue().equals((Object)LightModes.Light)) {
                    return class_124.field_1054;
                }
                return class_124.field_1065;
            }
            case 9: {
                if (ModuleCommands.INSTANCE.firstWatermarkBrightness.getValue().equals((Object)LightModes.Light)) {
                    return class_124.field_1076;
                }
                return class_124.field_1064;
            }
        }
        if (ModuleCommands.INSTANCE.firstWatermarkBrightness.getValue().equals((Object)LightModes.Light)) {
            return class_124.field_1068;
        }
        return class_124.field_1080;
    }

    public static class_124 getSecondWatermarkColor() {
        switch (((ColorModes)ModuleCommands.INSTANCE.secondWatermarkColor.getValue()).ordinal()) {
            case 1: {
                if (ModuleCommands.INSTANCE.secondWatermarkBrightness.getValue().equals((Object)LightModes.Light)) {
                    return class_124.field_1063;
                }
                return class_124.field_1074;
            }
            case 2: {
                if (ModuleCommands.INSTANCE.secondWatermarkBrightness.getValue().equals((Object)LightModes.Light)) {
                    return class_124.field_1080;
                }
                return class_124.field_1063;
            }
            case 3: {
                if (ModuleCommands.INSTANCE.secondWatermarkBrightness.getValue().equals((Object)LightModes.Light)) {
                    return class_124.field_1078;
                }
                return class_124.field_1058;
            }
            case 4: {
                if (ModuleCommands.INSTANCE.secondWatermarkBrightness.getValue().equals((Object)LightModes.Light)) {
                    return class_124.field_1060;
                }
                return class_124.field_1077;
            }
            case 5: {
                if (ModuleCommands.INSTANCE.secondWatermarkBrightness.getValue().equals((Object)LightModes.Light)) {
                    return class_124.field_1075;
                }
                return class_124.field_1062;
            }
            case 6: {
                if (ModuleCommands.INSTANCE.secondWatermarkBrightness.getValue().equals((Object)LightModes.Light)) {
                    return class_124.field_1061;
                }
                return class_124.field_1079;
            }
            case 7: {
                if (ModuleCommands.INSTANCE.secondWatermarkBrightness.getValue().equals((Object)LightModes.Light)) {
                    return class_124.field_1054;
                }
                return class_124.field_1065;
            }
            case 9: {
                if (ModuleCommands.INSTANCE.secondWatermarkBrightness.getValue().equals((Object)LightModes.Light)) {
                    return class_124.field_1076;
                }
                return class_124.field_1064;
            }
        }
        if (ModuleCommands.INSTANCE.secondWatermarkBrightness.getValue().equals((Object)LightModes.Light)) {
            return class_124.field_1068;
        }
        return class_124.field_1080;
    }

    public static enum WatermarkModes {
        Normal,
        Japanese,
        Custom,
        None;

    }

    public static enum ColorModes {
        Gold,
        Black,
        Gray,
        Blue,
        Green,
        Aqua,
        Red,
        Yellow,
        White,
        Purple;

    }

    public static enum LightModes {
        Light,
        Dark;

    }
}
