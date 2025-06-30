package dev.loottech.client.modules.movement;

import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.asm.mixins.accessor.PlayerMoveC2SPacketAccessor;
import dev.loottech.client.events.MovementPacketsEvent;
import dev.loottech.client.events.PacketSendEvent;
import dev.loottech.client.values.impl.ValueEnum;
import net.minecraft.class_2596;
import net.minecraft.class_2828;

@RegisterModule(name="NoFall", description="Stops you from taking fall damage.", category=Module.Category.MOVEMENT)
public class NoFall
extends Module {
    ValueEnum mode = new ValueEnum("Mode", "Mode", "", (Enum)Mode.Packet);

    @Override
    public void onMovementPackets(MovementPacketsEvent event) {
        if (!this.isFalling()) {
            return;
        }
        if (this.mode.getValue().equals((Object)Mode.Grim)) {
            this.sendPacket((class_2596<?>)new class_2828.class_2830(NoFall.mc.field_1724.method_23317(), NoFall.mc.field_1724.method_23318() + 1.0E-9, NoFall.mc.field_1724.method_23321(), NoFall.mc.field_1724.method_36454(), NoFall.mc.field_1724.method_36455(), false));
        }
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (NoFall.mc.field_1724 == null || NoFall.mc.field_1687 == null) {
            return;
        }
        if (!this.mode.getValue().equals((Object)Mode.Packet)) {
            return;
        }
        class_2596<?> class_25962 = event.getPacket();
        if (class_25962 instanceof class_2828) {
            class_2828 packet = (class_2828)class_25962;
            if (this.isFalling()) {
                ((PlayerMoveC2SPacketAccessor)packet).setOnGround(true);
            }
        }
    }

    private boolean isFalling() {
        return NoFall.mc.field_1724.field_6017 > (float)NoFall.mc.field_1724.method_5850() && !NoFall.mc.field_1724.method_24828() && !NoFall.mc.field_1724.method_6128();
    }

    private static enum Mode {
        Packet,
        Grim;

    }
}
