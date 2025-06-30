package dev.loottech.client.modules.visuals;

import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.api.utilities.render.RenderBuffers;
import dev.loottech.api.utilities.render.RenderUtils;
import dev.loottech.client.events.Render3DEvent;
import dev.loottech.client.values.impl.ValueBoolean;
import dev.loottech.client.values.impl.ValueColor;
import dev.loottech.client.values.impl.ValueNumber;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import java.awt.Color;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2680;

@RegisterModule(name="PhaseESP", tag="PhaseESP", description="", category=Module.Category.VISUALS)
public class PhaseESP
extends Module {
    public final ValueBoolean diagonals = new ValueBoolean("Diagonals", "Diagonals", "Render diagonals.", true);
    public final ValueNumber lineWidth = new ValueNumber("LineWidth", "LineWidth", "", (Number)Double.valueOf((double)2.0), (Number)Double.valueOf((double)0.1), (Number)Double.valueOf((double)4.0));
    public final ValueColor unsafeConfig = new ValueColor("Unsafe", "Unsafe", "Unsafe hole color.", Color.RED);
    public final ValueColor obsidianConfig = new ValueColor("Obsidian", "Obsidian", "Obsidian hole color.", Color.YELLOW);
    public final ValueColor bedrockConfig = new ValueColor("Bedrock", "Bedrock", "Bedrock hole color.", Color.GREEN);
    private final Set<class_2338> renderedBlocks = new HashSet();
    private static final Set<class_2248> UNBREAKABLE = new ReferenceOpenHashSet((Collection)Set.of((Object)class_2246.field_9987, (Object)class_2246.field_10525, (Object)class_2246.field_10395, (Object)class_2246.field_10398, (Object)class_2246.field_10499));

    @Override
    public void onRender3D(Render3DEvent event) {
        if (PhaseESP.mc.field_1724 == null || PhaseESP.mc.field_1687 == null || !PhaseESP.mc.field_1724.method_24828()) {
            return;
        }
        RenderBuffers.preRender();
        class_2338 playerPos = PhaseESP.mc.field_1724.method_24515();
        class_243 pos = PhaseESP.mc.field_1724.method_19538();
        double dx = pos.method_10216() - (double)playerPos.method_10263();
        double dz = pos.method_10215() - (double)playerPos.method_10260();
        for (class_2350 direction : class_2350.values()) {
            Color color;
            class_2338 blockPos;
            if (!direction.method_10166().method_10179() || PhaseESP.mc.field_1687.method_8320(blockPos = playerPos.method_10093(direction)).method_45474() || (color = this.getPhaseColor(blockPos)) == null) continue;
            double x = blockPos.method_10263();
            double y = blockPos.method_10264();
            double z = blockPos.method_10260();
            if (direction == class_2350.field_11034 && dx >= 0.65) {
                RenderUtils.drawBox(event.getMatrices(), x, y, z, x, y, z + 1.0, this.lineWidth.getValue().floatValue(), color);
                continue;
            }
            if (direction == class_2350.field_11039 && dx <= 0.35) {
                RenderUtils.drawBox(event.getMatrices(), x + 1.0, y, z, x + 1.0, y, z + 1.0, this.lineWidth.getValue().floatValue(), color);
                continue;
            }
            if (direction == class_2350.field_11035 && dz >= 0.65) {
                RenderUtils.drawBox(event.getMatrices(), x, y, z, x + 1.0, y, z, this.lineWidth.getValue().floatValue(), color);
                continue;
            }
            if (direction != class_2350.field_11043 || !(dz <= 0.35)) continue;
            RenderUtils.drawBox(event.getMatrices(), x, y, z + 1.0, x + 1.0, y, z + 1.0, this.lineWidth.getValue().floatValue(), color);
        }
        if (this.diagonals.getValue()) {
            Color color4;
            Color color3;
            Color color2;
            double x = playerPos.method_10263();
            double y = playerPos.method_10264();
            double z = playerPos.method_10260();
            class_2338 currentPos = playerPos.method_10093(class_2350.field_11039).method_10093(class_2350.field_11043);
            Color color1 = this.getPhaseColor(currentPos);
            if (color1 != null && dx <= 0.35 && dz <= 0.35) {
                RenderUtils.drawBox(event.getMatrices(), new class_238(x, y, z, x, y + 1.0, z), color1, (double)this.lineWidth.getValue().floatValue());
            }
            if ((color2 = this.getPhaseColor(currentPos = playerPos.method_10093(class_2350.field_11039).method_10093(class_2350.field_11035))) != null && dx <= 0.35 && dz >= 0.65) {
                RenderUtils.drawBox(event.getMatrices(), new class_238(x, y, z + 1.0, x, y + 1.0, z + 1.0), color2, (double)this.lineWidth.getValue().floatValue());
            }
            if ((color3 = this.getPhaseColor(currentPos = playerPos.method_10093(class_2350.field_11034).method_10093(class_2350.field_11043))) != null && dx >= 0.65 && dz <= 0.35) {
                RenderUtils.drawBox(event.getMatrices(), x + 1.0, y, z, x + 1.0, y + 1.0, z, this.lineWidth.getValue().floatValue(), color3);
            }
            if ((color4 = this.getPhaseColor(currentPos = playerPos.method_10093(class_2350.field_11034).method_10093(class_2350.field_11035))) != null && dx >= 0.65 && dz >= 0.65) {
                RenderUtils.drawBox(event.getMatrices(), x + 1.0, y, z + 1.0, x + 1.0, y + 1.0, z + 1.0, this.lineWidth.getValue().floatValue(), color4);
            }
        }
        RenderBuffers.postRender();
    }

    private Color getPhaseColor(class_2338 blockPos) {
        class_2680 state1 = PhaseESP.mc.field_1687.method_8320(blockPos);
        if (state1.method_26215()) {
            return null;
        }
        class_2680 state = PhaseESP.mc.field_1687.method_8320(blockPos.method_10074());
        Color color = null;
        if (state.method_45474()) {
            color = this.unsafeConfig.getValue();
        }
        color = PhaseESP.isUnbreakable(state.method_26204()) ? this.bedrockConfig.getValue() : this.obsidianConfig.getValue();
        return color;
    }

    public static boolean isUnbreakable(class_2248 block) {
        return UNBREAKABLE.contains((Object)block);
    }
}
