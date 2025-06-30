package dev.loottech.client.modules.miscellaneous;

import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.client.events.MouseDragEvent;

@RegisterModule(name="InventoryTweaks", tag="InventoryTweaks", description="Makes some tweaks to your inventory for quality-of-life improvements.", category=Module.Category.MISCELLANEOUS)
public class InventoryTweaks
extends Module {
    @Override
    public void onMouseDrag(MouseDragEvent event) {
        event.cancel();
    }
}
