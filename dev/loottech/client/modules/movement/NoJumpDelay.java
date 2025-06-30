package dev.loottech.client.modules.movement;

import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.asm.mixins.accessor.LivingEntityAccessor;

@RegisterModule(name="NoJumpDelay", description="I shouldn't have to explain this.", category=Module.Category.MOVEMENT)
public class NoJumpDelay
extends Module {
    @Override
    public void onTick() {
        if (NoJumpDelay.nullCheck()) {
            return;
        }
        ((LivingEntityAccessor)NoJumpDelay.mc.field_1724).setJumpCooldown(0);
    }
}
