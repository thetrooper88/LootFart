package dev.loottech.client.modules.visuals;

import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.client.events.PacketReceiveEvent;
import dev.loottech.client.values.impl.ValueBoolean;
import dev.loottech.client.values.impl.ValueColor;
import dev.loottech.client.values.impl.ValueNumber;
import java.awt.Color;
import net.minecraft.class_2761;

@RegisterModule(name="SkyBox", tag="SkyBox", description="", category=Module.Category.VISUALS)
public class SkyBox
extends Module {
    public ValueBoolean sky = new ValueBoolean("Sky", "Sky", "Change sky color or not.", true);
    public ValueColor skyColor = new ValueColor("SkyColor", "SkyColor", "", Color.WHITE, false, true);
    public ValueBoolean time = new ValueBoolean("Time", "Time", "Change time or not.", true);
    public ValueNumber worldTime = new ValueNumber("WorldTime", "WorldTime", "Time value.", (Number)Integer.valueOf((int)16000), (Number)Integer.valueOf((int)0), (Number)Integer.valueOf((int)24000));

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (!SkyBox.nullCheck() && event.getPacket() instanceof class_2761 && this.time.getValue()) {
            SkyBox.mc.field_1687.method_8435(this.worldTime.getValue().longValue());
            event.cancel();
        }
    }
}
