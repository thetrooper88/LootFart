package dev.loottech.api.manager.miscellaneous;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.event.EventListener;
import dev.loottech.api.utilities.EnchantmentUtils;
import dev.loottech.api.utilities.Util;
import dev.loottech.client.events.PacketSneakingEvent;
import net.minecraft.class_1297;
import net.minecraft.class_1304;
import net.minecraft.class_1887;
import net.minecraft.class_1893;
import net.minecraft.class_2596;
import net.minecraft.class_2848;
import net.minecraft.class_3532;
import net.minecraft.class_5321;

public class MovementManager
implements Util,
EventListener {
    private boolean packetSneaking;

    public void setMotionY(double y) {
        MovementManager.mc.field_1724.method_18800(MovementManager.mc.field_1724.method_18798().method_10216(), y, MovementManager.mc.field_1724.method_18798().method_10215());
    }

    public void setMotionXZ(double x, double z) {
        MovementManager.mc.field_1724.method_18800(x, MovementManager.mc.field_1724.method_18798().field_1351, z);
    }

    public void setMotionX(double x) {
        MovementManager.mc.field_1724.method_18800(x, MovementManager.mc.field_1724.method_18798().field_1351, MovementManager.mc.field_1724.method_18798().field_1350);
    }

    public void setMotionZ(double z) {
        MovementManager.mc.field_1724.method_18800(MovementManager.mc.field_1724.method_18798().field_1352, MovementManager.mc.field_1724.method_18798().field_1351, z);
    }

    public void setPacketSneaking(boolean packetSneaking) {
        this.packetSneaking = packetSneaking;
        if (packetSneaking) {
            Managers.NETWORK.sendPacket((class_2596<?>)new class_2848((class_1297)MovementManager.mc.field_1724, class_2848.class_2849.field_12979));
        } else {
            Managers.NETWORK.sendPacket((class_2596<?>)new class_2848((class_1297)MovementManager.mc.field_1724, class_2848.class_2849.field_12984));
        }
    }

    public void applySneak() {
        float modifier = class_3532.method_15363((float)(0.3f + (float)EnchantmentUtils.getLevel(MovementManager.mc.field_1724.method_6118(class_1304.field_6166), (class_5321<class_1887>)class_1893.field_38223) * 0.15f), (float)0.0f, (float)1.0f);
        MovementManager.mc.field_1724.field_3913.field_3905 *= modifier;
        MovementManager.mc.field_1724.field_3913.field_3907 *= modifier;
    }

    @Override
    public void onPacketSneak(PacketSneakingEvent event) {
        if (this.packetSneaking) {
            event.cancel();
        }
    }
}
