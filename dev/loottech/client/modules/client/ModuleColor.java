package dev.loottech.client.modules.client;

import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.client.values.impl.ValueCategory;
import dev.loottech.client.values.impl.ValueColor;
import dev.loottech.client.values.impl.ValueNumber;
import java.awt.Color;

@RegisterModule(name="Color", description="Manages the client's global color.", category=Module.Category.CLIENT, persistent=true, drawn=false)
public class ModuleColor
extends Module {
    public static ModuleColor INSTANCE;
    public final ValueColor color = new ValueColor("Color", "Color", "The client's global color.", new Color(255, 36, 36, 255));
    public final ValueColor friendColor = new ValueColor("FriendColor", "FriendColor", "The friend global color.", new Color(68, 255, 232));
    ValueCategory rainbowCategory = new ValueCategory("Rainbow", "Manage rainbow");
    public ValueNumber rainbowOffset = new ValueNumber("RainbowOffset", "Offset", "", this.rainbowCategory, (Number)Integer.valueOf((int)255), (Number)Integer.valueOf((int)0), (Number)Integer.valueOf((int)1000));
    public ValueNumber rainbowSat = new ValueNumber("RainbowSaturation", "Saturation", "", this.rainbowCategory, (Number)Integer.valueOf((int)255), (Number)Integer.valueOf((int)0), (Number)Integer.valueOf((int)255));
    public ValueNumber rainbowBri = new ValueNumber("RainbowBrightness", "Brightness", "", this.rainbowCategory, (Number)Integer.valueOf((int)255), (Number)Integer.valueOf((int)0), (Number)Integer.valueOf((int)255));

    public ModuleColor() {
        INSTANCE = this;
    }

    public static Color getColor() {
        Color color = INSTANCE == null ? new Color(255, 255, 255) : ModuleColor.INSTANCE.color.getValue();
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), 255);
    }

    public static Color getFriendColor() {
        Color color = INSTANCE == null ? new Color(68, 255, 232) : ModuleColor.INSTANCE.friendColor.getValue();
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), 255);
    }

    public static Color getColor(int alpha) {
        Color color = INSTANCE == null ? new Color(255, 255, 255) : ModuleColor.INSTANCE.color.getValue();
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }
}
