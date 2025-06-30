package dev.loottech.client.modules.visuals;

import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.client.events.AmbientColorEvent;
import dev.loottech.client.modules.client.ModuleColor;
import dev.loottech.client.values.impl.ValueBoolean;
import dev.loottech.client.values.impl.ValueColor;
import java.util.function.Supplier;

@RegisterModule(name="Fullbright", tag="Fullbright", description="Make the game bright.", category=Module.Category.VISUALS)
public class Fullbright
extends Module {
    public final ValueBoolean changeColor = new ValueBoolean("Color", "Color", "Change world color or not.", false);
    public ValueColor color = new ValueColor("WorldColor", "WorldColor", "World color.", ModuleColor.getColor(), false, true, (Supplier<Boolean>)((Supplier)this.changeColor::getValue));

    @Override
    public void onAmbientColor(AmbientColorEvent event) {
        if (this.changeColor.getValue()) {
            event.cancel();
            event.setColor(this.color.getValue());
        }
    }
}
