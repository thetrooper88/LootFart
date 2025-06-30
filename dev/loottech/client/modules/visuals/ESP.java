package dev.loottech.client.modules.visuals;

import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.api.utilities.RangeUtils;
import dev.loottech.api.utilities.render.Interpolation;
import dev.loottech.api.utilities.render.RenderBuffers;
import dev.loottech.api.utilities.render.RenderManager;
import dev.loottech.api.utilities.render.RenderUtils;
import dev.loottech.client.events.Render3DEvent;
import dev.loottech.client.modules.client.ModuleColor;
import dev.loottech.client.values.impl.ValueColor;
import dev.loottech.client.values.impl.ValueEnum;
import dev.loottech.client.values.impl.ValueNumber;
import java.awt.Color;
import net.minecraft.class_1297;
import net.minecraft.class_1542;
import net.minecraft.class_243;

@RegisterModule(name="ESP", tag="ESP", description="Summon lightning at player deaths.", category=Module.Category.VISUALS)
public class ESP
extends Module {
    private final ValueNumber range = new ValueNumber("Range", "Maximum range to render entites in.", (Number)Float.valueOf((float)50.0f), (Number)Float.valueOf((float)1.0f), (Number)Float.valueOf((float)100.0f));
    private final ValueEnum items = new ValueEnum("Items", "Render an esp for dropped items.", ItemModes.Box);
    private final ValueColor itemTextColor = new ValueColor("ItemTextColor", "ItemTextColor", "", Color.WHITE, false, false);
    private final ValueNumber itemTextScale = new ValueNumber("ItemTextScale", "Scale of the item nametag.", (Number)Float.valueOf((float)1.0f), (Number)Float.valueOf((float)0.1f), (Number)Float.valueOf((float)5.0f));
    private final ValueColor boxFill = new ValueColor("Fill", "Fill", "", ModuleColor.getColor(30), false, true);
    private final ValueColor boxLine = new ValueColor("Line", "Line", "", ModuleColor.getColor(255), false, true);
    private final ValueNumber lineWidth = new ValueNumber("LineWidth", "Render outline width.", (Number)Float.valueOf((float)1.0f), (Number)Float.valueOf((float)0.5f), (Number)Float.valueOf((float)8.0f));

    @Override
    public void onRender3D(Render3DEvent event) {
        if (ESP.mc.field_1773 == null || mc.method_1560() == null) {
            return;
        }
        RenderBuffers.preRender();
        for (class_1297 entity : ESP.mc.field_1687.method_18112()) {
            if (!RenderManager.isBoxVisible(entity.method_5829()) || !RangeUtils.isInRange(entity.method_19538(), this.range.getValue().doubleValue()) || this.items.getValue().equals((Object)ItemModes.None) || !(entity instanceof class_1542)) continue;
            if (this.items.getValue().equals((Object)ItemModes.Box) || this.items.getValue().equals((Object)ItemModes.Both)) {
                RenderManager.renderBoundingBox(event.getMatrices(), RenderUtils.getInterpolatedBoundingBox(entity), this.lineWidth.getValue().floatValue(), this.boxLine.getValue().getRGB());
                RenderManager.renderBox(event.getMatrices(), RenderUtils.getInterpolatedBoundingBox(entity), this.boxFill.getValue().getRGB());
            }
            if (!this.items.getValue().equals((Object)ItemModes.Text) && !this.items.getValue().equals((Object)ItemModes.Both)) continue;
            int count = ((class_1542)entity).method_6983().method_7947();
            String text = ((class_1542)entity).method_6983().method_7964().getString() + (String)(count > 1 ? " x" + count : "");
            class_243 lerpedPos = Interpolation.getInterpolatedPosition(entity, RenderUtils.getTickDelta()).method_1031(0.0, 0.25, 0.0);
            int color = this.itemTextColor.getValue(255).getRGB();
            RenderManager.renderSign(text, lerpedPos, 0.0f, 0.0f, this.itemTextScale.getValue().floatValue() / 41.0f, color);
        }
        RenderBuffers.postRender();
    }

    private static enum ItemModes {
        Box,
        Text,
        Both,
        None;

    }
}
