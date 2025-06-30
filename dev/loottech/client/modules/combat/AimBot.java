package dev.loottech.client.modules.combat;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.api.utilities.RotationUtils;
import dev.loottech.client.events.Render3DEvent;
import dev.loottech.client.values.impl.ValueNumber;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_243;

@RegisterModule(name="AimBot", tag="AimBot", description="Automatically set crosshair to player.", category=Module.Category.COMBAT)
public class AimBot
extends Module {
    final ValueNumber distance = new ValueNumber("Distance", "Distance", "", (Number)Double.valueOf((double)5.0), (Number)Double.valueOf((double)0.0), (Number)Double.valueOf((double)10.0));

    @Override
    public void onRender3D(Render3DEvent event) {
        for (class_1657 player : AimBot.mc.field_1687.method_18456()) {
            if (player == AimBot.mc.field_1724 || AimBot.getDistanceTo((class_1297)player) > this.distance.getValue().doubleValue()) continue;
            double lerpX = player.field_6014 + (player.method_23317() - player.field_6014) * (double)event.getTickDelta();
            double lerpY = player.field_6036 + (double)player.method_18381(player.method_18376()) + (player.method_23318() - player.field_6036) * (double)event.getTickDelta();
            double lerpZ = player.field_5969 + (player.method_23321() - player.field_5969) * (double)event.getTickDelta();
            class_243 pos = new class_243(lerpX, lerpY, lerpZ);
            float[] rot = RotationUtils.getRotations(pos.field_1352, pos.field_1351, pos.field_1350);
            Managers.ROTATION.setRotationClient(rot[0], rot[1]);
        }
    }

    public static double getDistanceTo(class_1297 to) {
        return AimBot.mc.field_1724.method_19538().method_1022(to.method_19538());
    }
}
