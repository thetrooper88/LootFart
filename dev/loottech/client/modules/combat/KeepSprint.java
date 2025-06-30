package dev.loottech.client.modules.combat;

import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.client.events.SprintResetEvent;

@RegisterModule(name="KeepSprint", tag="KeepSprint", description="Don't reset sprinting after attack.", category=Module.Category.COMBAT)
public class KeepSprint
extends Module {
    @Override
    public void onResetSprint(SprintResetEvent event) {
        event.cancel();
    }
}
