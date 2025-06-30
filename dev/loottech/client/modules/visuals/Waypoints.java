package dev.loottech.client.modules.visuals;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.api.manager.waypoint.Waypoint;
import dev.loottech.api.utilities.MathUtils;
import dev.loottech.api.utilities.PlayerUtils;
import dev.loottech.api.utilities.render.RenderBuffers;
import dev.loottech.api.utilities.render.RenderManager;
import dev.loottech.api.utilities.render.RenderUtils;
import dev.loottech.client.events.Render3DEvent;
import dev.loottech.client.modules.client.ModuleColor;
import dev.loottech.client.values.impl.ValueBoolean;
import java.awt.Color;
import net.minecraft.class_1297;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_2382;
import net.minecraft.class_243;
import net.minecraft.class_4184;

@RegisterModule(name="Waypoints", tag="Waypoints", description="Render markers at specific positions.", category=Module.Category.VISUALS)
public class Waypoints
extends Module {
    private final ValueBoolean name = new ValueBoolean("Name", "Show Waypoint name.", true);
    private final ValueBoolean distance = new ValueBoolean("Distance", "Show Distance to Waypoint.", true);
    private final ValueBoolean position = new ValueBoolean("Position", "Show Waypoint position.", true);

    @Override
    public void onRender3D(Render3DEvent event) {
        for (Waypoint waypoint : Managers.WAYPOINT.getWaypointsForServer(Managers.NETWORK.getServerIp())) {
            if (waypoint.getDimension() != PlayerUtils.getDimension()) continue;
            class_4184 camera = Waypoints.mc.field_1773.method_19418();
            class_2338 pos = waypoint.getPos();
            class_243 vec = class_243.method_24953((class_2382)waypoint.getPos().method_10086(2));
            class_238 box = new class_238((double)pos.method_10263(), -1000.0, (double)(pos.method_10260() + 1), (double)(pos.method_10263() + 1), 1000.0, (double)pos.method_10260());
            float lerpedY = (float)(RenderUtils.getInterpolatedBoundingBox((class_1297)Waypoints.mc.field_1724, (float)RenderUtils.getTickDelta()).field_1325 + 5.0);
            float scale = Math.max((float)0.015f, (float)(0.005f + (float)(camera.method_19326().method_1022(new class_243(vec.field_1352, (double)lerpedY, vec.field_1350)) * (double)0.005f)));
            RenderBuffers.preRender();
            if (Waypoints.mc.field_1724.method_19538().method_1022(class_243.method_24953((class_2382)waypoint.getPos())) <= 800.0) {
                RenderManager.drawBox(event.getMatrices(), box, ModuleColor.getColor(40).getRGB());
            }
            RenderManager.renderSign(this.buildString(waypoint), vec.field_1352, lerpedY, vec.field_1350, 0.0f, 0.0f, scale, Color.WHITE.getRGB());
            RenderBuffers.postRender();
        }
    }

    private String buildString(Waypoint waypoint) {
        String string = "";
        if (this.name.getValue()) {
            string = string + waypoint.getName();
        }
        if (this.distance.getValue()) {
            string = string + " " + MathUtils.roundToPlaces(Waypoints.mc.field_1724.method_19538().method_1022(class_243.method_24953((class_2382)waypoint.getPos())), 1) + "m";
        }
        if (this.position.getValue()) {
            string = string + " (" + waypoint.getPos().method_10263() + ", " + waypoint.getPos().method_10264() + ", " + waypoint.getPos().method_10260() + ")";
        }
        return string;
    }
}
