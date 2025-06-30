package dev.loottech.client.modules.visuals;

import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.client.events.RenderCrystalEvent;
import dev.loottech.client.values.impl.ValueBoolean;
import dev.loottech.client.values.impl.ValueNumber;

@RegisterModule(name="CrystalModifier", tag="CrystalModifier", description="Change how crystals are rendered.", category=Module.Category.VISUALS)
public class CrystalModifier
extends Module {
    public final ValueNumber crystalRotationSpeed = new ValueNumber("RotationSpeed", "RotationSpeed", "Rotation Speed of end crystals.", (Number)Double.valueOf((double)1.0), (Number)Double.valueOf((double)-10.0), (Number)Double.valueOf((double)10.0));
    public final ValueNumber crystalSize = new ValueNumber("Size", "Size", "Size of end crystals.", (Number)Double.valueOf((double)1.0), (Number)Integer.valueOf((int)0), (Number)Integer.valueOf((int)5));
    public final ValueBoolean bounce = new ValueBoolean("Bounce", "Bounce", "Bounce end crystals.", true);

    @Override
    public void onRenderCrystal(RenderCrystalEvent event) {
        event.setSpin(this.crystalRotationSpeed.getValue().floatValue());
        event.setBounce(this.bounce.getValue());
        event.setScale(this.crystalSize.getValue().floatValue());
    }

    public boolean getBounce() {
        return this.bounce.getValue();
    }

    public float getSpin() {
        return this.crystalRotationSpeed.getValue().floatValue();
    }

    public float getScale() {
        return this.crystalSize.getValue().floatValue();
    }
}
