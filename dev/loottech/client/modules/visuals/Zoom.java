package dev.loottech.client.modules.visuals;

import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.client.events.PreTickEvent;
import dev.loottech.client.values.impl.ValueBoolean;
import dev.loottech.client.values.impl.ValueNumber;

@RegisterModule(name="Zoom", description="Make you zoom in on shi like optifine.", category=Module.Category.VISUALS)
public class Zoom
extends Module {
    private final ValueNumber zoom = new ValueNumber("Zoom", "Value of zoom", (Number)Integer.valueOf((int)30), (Number)Integer.valueOf((int)10), (Number)Integer.valueOf((int)50));
    private final ValueBoolean hideHud = new ValueBoolean("Hide HUD", "Hide the hud while zoomed.", true);
    private final ValueBoolean smoothCamera = new ValueBoolean("Smooth", "Makes your camera movements smooth.", true);
    private boolean flag1 = true;
    private boolean flag;
    private int defaultFov = 100;

    @Override
    public void onPreTick(PreTickEvent event) {
        if (Zoom.mc.field_1755 == null) {
            if (this.flag1) {
                this.defaultFov = (Integer)Zoom.mc.field_1690.method_41808().method_41753();
                this.flag1 = false;
            }
            Zoom.mc.field_1690.field_1914 = this.smoothCamera.getValue();
            Zoom.mc.field_1690.field_1842 = this.hideHud.getValue();
            Zoom.mc.field_1690.method_41808().method_41748((Object)this.zoom.getValue().intValue());
            this.flag = true;
        }
    }

    @Override
    public void onDisable() {
        if (this.flag) {
            Zoom.mc.field_1690.field_1842 = false;
            Zoom.mc.field_1690.field_1914 = false;
            Zoom.mc.field_1690.method_41808().method_41748((Object)this.defaultFov);
            this.flag = false;
            this.flag1 = false;
        }
    }
}
