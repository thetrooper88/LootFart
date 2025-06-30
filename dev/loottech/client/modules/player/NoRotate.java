package dev.loottech.client.modules.player;

import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.asm.mixins.accessor.PlayerPositionLookS2CPacketAccessor;
import dev.loottech.client.events.PacketReceiveEvent;
import net.minecraft.class_2596;
import net.minecraft.class_2708;
import net.minecraft.class_2709;

@RegisterModule(name="NoRotate", tag="NoRotate", description="Dont rotate clientside.", category=Module.Category.PLAYER)
public class NoRotate
extends Module {
    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (NoRotate.nullCheck()) {
            return;
        }
        class_2596<?> class_25962 = event.getPacket();
        if (class_25962 instanceof class_2708) {
            class_2708 packet = (class_2708)class_25962;
            if (packet.method_11733().contains((Object)class_2709.field_12401)) {
                ((PlayerPositionLookS2CPacketAccessor)packet).setYaw(0.0f);
            } else {
                ((PlayerPositionLookS2CPacketAccessor)packet).setYaw(NoRotate.mc.field_1724.method_36454());
            }
            if (packet.method_11733().contains((Object)class_2709.field_12397)) {
                ((PlayerPositionLookS2CPacketAccessor)packet).setPitch(0.0f);
            } else {
                ((PlayerPositionLookS2CPacketAccessor)packet).setPitch(NoRotate.mc.field_1724.method_36455());
            }
        }
    }
}
