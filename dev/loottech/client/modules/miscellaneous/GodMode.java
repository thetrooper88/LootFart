package dev.loottech.client.modules.miscellaneous;

import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.client.events.PacketSendEvent;
import net.minecraft.class_2793;

@RegisterModule(name="GodMode2b2t", tag="GodMode2b2t", description="makes you invincible after you die on 2b2t", category=Module.Category.MISCELLANEOUS)
public class GodMode
extends Module {
    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacket() instanceof class_2793) {
            event.cancel();
        }
    }
}
