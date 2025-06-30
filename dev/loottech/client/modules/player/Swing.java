package dev.loottech.client.modules.player;

import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.asm.mixins.accessor.AccessorBundlePacket;
import dev.loottech.client.events.PacketReceiveEvent;
import dev.loottech.client.events.RenderSwingAnimationEvent;
import dev.loottech.client.events.SwingSpeedEvent;
import dev.loottech.client.values.impl.ValueBoolean;
import dev.loottech.client.values.impl.ValueNumber;
import java.util.ArrayList;
import net.minecraft.class_2596;
import net.minecraft.class_2616;
import net.minecraft.class_8042;

@RegisterModule(name="Swing", description="", category=Module.Category.PLAYER)
public class Swing
extends Module {
    private final ValueBoolean oldSwing = new ValueBoolean("OldSwing", "Old Swing animation.", false);
    public ValueNumber swingSpeed = new ValueNumber("SwingSpeed", "SwingSpeed", "", (Number)Integer.valueOf((int)15), (Number)Integer.valueOf((int)1), (Number)Integer.valueOf((int)20));
    public ValueBoolean translateX = new ValueBoolean("TranslateX", "TranslateX", "", true);
    public ValueBoolean translateY = new ValueBoolean("TranslateY", "TranslateY", "", true);
    public ValueBoolean translateZ = new ValueBoolean("TranslateZ", "TranslateZ", "", true);

    @Override
    public void onRenderSwing(RenderSwingAnimationEvent event) {
        if (this.oldSwing.getValue()) {
            event.cancel();
        }
    }

    @Override
    public void onSwingSpeed(SwingSpeedEvent event) {
        event.cancel();
        event.setSwingSpeed(this.swingSpeed.getValue().intValue());
        event.setSelfOnly(false);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (Swing.nullCheck()) {
            return;
        }
        class_2596<?> class_25962 = event.getPacket();
        if (class_25962 instanceof class_8042) {
            class_8042 packet = (class_8042)class_25962;
            ArrayList packets = new ArrayList();
            for (class_2596 packet1 : packet.method_48324()) {
                if (packet1 instanceof class_2616) {
                    class_2616 packet2 = (class_2616)packet1;
                    if (this.oldSwing.getValue() && packet2.method_11269() == Swing.mc.field_1724.method_5628() && (packet2.method_11267() == 0 || packet2.method_11267() == 3)) continue;
                }
                packets.add((Object)packet1);
            }
            ((AccessorBundlePacket)packet).setIterable((Iterable<class_2596<?>>)packets);
        } else {
            class_25962 = event.getPacket();
            if (class_25962 instanceof class_2616) {
                class_2616 packet = (class_2616)class_25962;
                if (this.oldSwing.getValue() && packet.method_11269() == Swing.mc.field_1724.method_5628() && (packet.method_11267() == 0 || packet.method_11267() == 3)) {
                    event.cancel();
                }
            }
        }
    }
}
