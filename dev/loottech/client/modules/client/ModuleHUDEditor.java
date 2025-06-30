package dev.loottech.client.modules.client;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.client.modules.client.ModuleColor;
import dev.loottech.client.values.impl.ValueColor;
import net.minecraft.class_124;
import net.minecraft.class_437;

@RegisterModule(name="HUDEditor", tag="HUD Editor", description="The client's HUD Editor.", category=Module.Category.CLIENT)
public class ModuleHUDEditor
extends Module {
    public static ModuleHUDEditor INSTANCE;
    public final ValueColor color = new ValueColor("Color", "Color", "Global color for the hud.", ModuleColor.getColor(), false, true);

    public ModuleHUDEditor() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if (ModuleHUDEditor.mc.field_1724 == null || ModuleHUDEditor.mc.field_1687 == null) {
            this.disable(false);
            return;
        }
        mc.method_1507((class_437)Managers.HUD_EDITOR);
        this.disable(false);
    }

    public class_124 getSecondColor() {
        return class_124.field_1068;
    }

    public static enum secondColors {
        Normal,
        Gray,
        DarkGray,
        White;

    }
}
