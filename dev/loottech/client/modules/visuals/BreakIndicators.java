package dev.loottech.client.modules.visuals;

import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.api.utilities.BlastResistantBlocks;
import dev.loottech.api.utilities.render.RenderBuffers;
import dev.loottech.api.utilities.render.RenderManager;
import dev.loottech.client.events.PacketReceiveEvent;
import dev.loottech.client.events.Render3DEvent;
import dev.loottech.client.values.impl.ValueColor;
import java.awt.Color;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.class_1922;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_259;
import net.minecraft.class_2596;
import net.minecraft.class_2620;
import net.minecraft.class_265;
import net.minecraft.class_3532;

@RegisterModule(name="BreakIndicators", tag="BreakIndicators", description="Shows a visual indicator when a block is broken.", category=Module.Category.VISUALS)
public class BreakIndicators
extends Module {
    private final ValueColor fillColor = new ValueColor("FillColor", "FillColor", "The color of the indicator.", new Color(255, 0, 0, 45), false, false);
    private final ValueColor outlineColor = new ValueColor("OutlineColor", "OutlineColor", "The color of the outline.", new Color(255, 0, 0, 255), false, false);
    private final Map<class_2620, Long> brokenBlocks = new ConcurrentHashMap();

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        class_2620 packet;
        class_2596<?> class_25962 = event.getPacket();
        if (class_25962 instanceof class_2620 && !BlastResistantBlocks.isUnbreakable((packet = (class_2620)class_25962).method_11277())) {
            class_2620 p = this.getPacketFromPos(packet.method_11277());
            if (p != null) {
                this.brokenBlocks.replace((Object)p, (Object)System.currentTimeMillis());
            } else {
                this.brokenBlocks.put((Object)packet, (Object)System.currentTimeMillis());
            }
        }
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (BreakIndicators.mc.field_1724 == null || BreakIndicators.mc.field_1687 == null) {
            return;
        }
        RenderBuffers.preRender();
        for (Map.Entry mine : this.brokenBlocks.entrySet()) {
            class_2338 mining = ((class_2620)mine.getKey()).method_11277();
            long elapsedTime = System.currentTimeMillis() - (Long)mine.getValue();
            for (long count = this.brokenBlocks.keySet().stream().filter(p -> p.method_11280() == ((class_2620)mine.getKey()).method_11280()).count(); count > 2L; --count) {
                this.brokenBlocks.entrySet().stream().filter(p -> ((class_2620)p.getKey()).method_11280() == ((class_2620)mine.getKey()).method_11280()).min(Comparator.comparingLong(Map.Entry::getValue)).ifPresent(min -> this.brokenBlocks.remove(min.getKey(), min.getValue()));
            }
            if (BreakIndicators.mc.field_1687.method_22347(mining) || elapsedTime > 2500L) {
                this.brokenBlocks.remove(mine.getKey(), mine.getValue());
                continue;
            }
            double dist = BreakIndicators.mc.field_1724.method_5707(mining.method_46558());
            if (dist > 100.0) continue;
            class_265 outlineShape = BreakIndicators.mc.field_1687.method_8320(mining).method_26218((class_1922)BreakIndicators.mc.field_1687, mining);
            outlineShape = outlineShape.method_1110() ? class_259.method_1077() : outlineShape;
            class_238 render1 = outlineShape.method_1107();
            class_238 render = new class_238((double)mining.method_10263() + render1.field_1323, (double)mining.method_10264() + render1.field_1322, (double)mining.method_10260() + render1.field_1321, (double)mining.method_10263() + render1.field_1320, (double)mining.method_10264() + render1.field_1325, (double)mining.method_10260() + render1.field_1324);
            class_243 center = render.method_1005();
            float scale = class_3532.method_15363((float)((float)elapsedTime / 2500.0f), (float)0.0f, (float)1.0f);
            double dx = (render1.field_1320 - render1.field_1323) / 2.0;
            double dy = (render1.field_1325 - render1.field_1322) / 2.0;
            double dz = (render1.field_1324 - render1.field_1321) / 2.0;
            class_238 scaled = new class_238(center, center).method_1009(dx * (double)scale, dy * (double)scale, dz * (double)scale);
            RenderManager.renderBox(event.getMatrices(), scaled, this.fillColor.getValue().getRGB());
            RenderManager.renderBoundingBox(event.getMatrices(), scaled, 1.5f, this.outlineColor.getValue().getRGB());
        }
        RenderBuffers.postRender();
    }

    private class_2620 getPacketFromPos(class_2338 pos) {
        return (class_2620)this.brokenBlocks.keySet().stream().filter(p -> p.method_11277().equals((Object)pos)).findFirst().orElse(null);
    }
}
