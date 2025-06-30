package dev.loottech.client.modules.client;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.client.events.KeyEvent;
import dev.loottech.client.values.impl.ValueBoolean;
import dev.loottech.client.values.impl.ValueColor;
import dev.loottech.client.values.impl.ValueNumber;
import java.awt.Color;
import net.minecraft.class_437;

@RegisterModule(name="GUI", description="The client's GUI interface for interacting with modules and settings.", category=Module.Category.CLIENT, bind=344, drawn=false)
public class ModuleGUI
extends Module {
    public static ModuleGUI INSTANCE;
    public ValueColor backgroundColor = new ValueColor("Background", "Background", "Color to use for background", new Color(0, 0, 0, 100));
    public ValueBoolean sounds = new ValueBoolean("Sounds", "", true);
    public ValueBoolean rectEnabled = new ValueBoolean("RectEnabled", "Rect Enabled", "Render a rectangle behind enabled modules.", true);
    public ValueBoolean fadeText = new ValueBoolean("FadeText", "Fade Text", "Add cool animation to the text of the GUI.", false);
    public ValueNumber fadeOffset = new ValueNumber("FadeOffset", "Fade Offset", "Offset for the text animation of the GUI.", (Number)Integer.valueOf((int)100), (Number)Integer.valueOf((int)0), (Number)Integer.valueOf((int)255));
    public ValueBoolean logo = new ValueBoolean("Logo", "Logo", "Render logo on the GUI.", false);

    public ModuleGUI() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        if (ModuleGUI.mc.field_1724 == null || ModuleGUI.mc.field_1687 == null || Managers.CLICK_GUI == null) {
            this.disable(false);
            return;
        }
        mc.method_1507((class_437)Managers.CLICK_GUI);
    }

    @Override
    public void onKey(KeyEvent event) {
        if (event.getKeyCode() == 256 && Managers.CLICK_GUI != null) {
            this.disable(false);
        }
    }
}
