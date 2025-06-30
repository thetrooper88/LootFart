package dev.loottech.client.modules.visuals;

import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.client.events.CameraRotationEvent;
import dev.loottech.client.events.MouseUpdateEvent;
import dev.loottech.client.events.PerspectiveUpdateEvent;
import net.minecraft.class_3532;
import net.minecraft.class_5498;

@RegisterModule(name="FreeLook", description="Lets you look in third person without rotating player head.", category=Module.Category.VISUALS)
public class FreeLook
extends Module {
    private float cameraYaw;
    private float cameraPitch;
    private class_5498 perspective;

    @Override
    public void onEnable() {
        if (FreeLook.nullCheck()) {
            return;
        }
        this.perspective = FreeLook.mc.field_1690.method_31044();
    }

    @Override
    public void onDisable() {
        if (this.perspective != null) {
            FreeLook.mc.field_1690.method_31043(this.perspective);
        }
    }

    @Override
    public void onTick() {
        if (this.perspective != null && this.perspective != class_5498.field_26665) {
            FreeLook.mc.field_1690.method_31043(class_5498.field_26665);
        }
    }

    @Override
    public void onPerspectiveUpdate(PerspectiveUpdateEvent event) {
        if (FreeLook.mc.field_1690.method_31044() != event.getPerspective() && event.getPerspective() != class_5498.field_26664) {
            this.cameraYaw = FreeLook.mc.field_1724.method_36454();
            this.cameraPitch = FreeLook.mc.field_1724.method_36455();
        }
    }

    @Override
    public void onCameraRotation(CameraRotationEvent event) {
        if (FreeLook.mc.field_1690.method_31044() != class_5498.field_26664) {
            event.setYaw(this.cameraYaw);
            event.setPitch(this.cameraPitch);
        }
    }

    @Override
    public void onMouseUpdate(MouseUpdateEvent event) {
        if (FreeLook.mc.field_1690.method_31044() != class_5498.field_26664) {
            event.cancel();
            this.changeLookDirection(event.getCursorDeltaX(), event.getCursorDeltaY());
        }
    }

    private void changeLookDirection(double cursorDeltaX, double cursorDeltaY) {
        float f = (float)cursorDeltaY * 0.15f;
        float g = (float)cursorDeltaX * 0.15f;
        this.cameraPitch += f;
        this.cameraYaw += g;
        this.cameraPitch = class_3532.method_15363((float)this.cameraPitch, (float)-90.0f, (float)90.0f);
    }
}
