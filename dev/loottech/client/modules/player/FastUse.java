package dev.loottech.client.modules.player;

import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.api.utilities.Timer;
import dev.loottech.asm.mixins.accessor.MinecraftClientAccessor;
import dev.loottech.client.values.impl.ValueNumber;

@RegisterModule(name="FastUse", category=Module.Category.PLAYER, tag="FastUse", description="Use items faster.")
public class FastUse
extends Module {
    private final ValueNumber delay = new ValueNumber("Delay", "Delay", "", (Number)Integer.valueOf((int)1), (Number)Integer.valueOf((int)0), (Number)Integer.valueOf((int)10));
    private final Timer startTimer = new Timer();

    @Override
    public void onTick() {
        if (!FastUse.mc.field_1690.field_1904.method_1434()) {
            this.startTimer.reset();
        } else if (this.startTimer.passedMs(this.delay.getValue().longValue()) && (double)((MinecraftClientAccessor)mc).hookGetItemUseCooldown() > this.delay.getValue().doubleValue()) {
            ((MinecraftClientAccessor)mc).hookSetItemUseCooldown(this.delay.getValue().intValue());
        }
    }
}
