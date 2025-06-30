package dev.loottech.client.modules.client;

import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.client.values.impl.ValueEnum;
import dev.loottech.client.values.impl.ValueNumber;

@RegisterModule(name="VanillaFont", category=Module.Category.CLIENT, tag="VanillaFont", description="Manages the vanilla font.", drawn=false)
public class VanillaFont
extends Module {
    public final ValueEnum textShadow = new ValueEnum("Text Shadow", "Text Shadow", "Change game text shadow", (Enum)ShadowModes.DEFAULT);
    public final ValueNumber shadowOffset = new ValueNumber("Shadow Offset", "Shadow Offset", "Modifies the game's text shadow offset", (Number)Double.valueOf((double)0.5), (Number)Double.valueOf((double)-2.0), (Number)Double.valueOf((double)2.0));

    public float getShadowValue() {
        if (this.textShadow.getValue().equals((Object)ShadowModes.NONE)) {
            return 0.0f;
        }
        if (this.textShadow.getValue().equals((Object)ShadowModes.CUSTOM)) {
            return this.shadowOffset.getValue().floatValue();
        }
        return 1.0f;
    }

    public static enum ShadowModes {
        NONE,
        DEFAULT,
        CUSTOM;

    }
}
