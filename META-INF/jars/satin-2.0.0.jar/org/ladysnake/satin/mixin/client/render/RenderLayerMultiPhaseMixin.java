package org.ladysnake.satin.mixin.client.render;

import java.util.function.Consumer;
import net.minecraft.class_1921;
import net.minecraft.class_293;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.satin.impl.RenderLayerDuplicator;
import org.ladysnake.satin.mixin.client.render.RenderLayerAccessor;
import org.ladysnake.satin.mixin.client.render.RenderLayerMixin;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value={class_1921.class_4687.class})
public abstract class RenderLayerMultiPhaseMixin
extends class_1921
implements RenderLayerDuplicator.SatinRenderLayer {
    @Shadow
    @Final
    private class_1921.class_4688 field_21403;

    public RenderLayerMultiPhaseMixin(String name, class_293 vertexFormat, class_293.class_5596 drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
        super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
    }

    @Override
    public class_1921 satin$copy(String newName, @Nullable class_293 vertexFormat, Consumer<class_1921.class_4688.class_4689> op) {
        return RenderLayerAccessor.satin$of(newName, vertexFormat == null ? this.method_23031() : vertexFormat, this.method_23033(), this.method_22722(), this.method_23037(), ((RenderLayerAccessor)((Object)this)).isTranslucent(), this.satin$copyPhaseParameters(op));
    }

    @Override
    public class_1921.class_4688 satin$copyPhaseParameters(Consumer<class_1921.class_4688.class_4689> op) {
        RenderLayerMixin.MultiPhaseParametersAccessor access = (RenderLayerMixin.MultiPhaseParametersAccessor)this.field_21403;
        class_1921.class_4688.class_4689 builder = class_1921.class_4688.method_23598().method_34577(access.getTexture()).method_34578(access.getProgram()).method_23615(access.getTransparency()).method_23604(access.getDepthTest()).method_23603(access.getCull()).method_23608(access.getLightmap()).method_23611(access.getOverlay()).method_23607(access.getLayering()).method_23610(access.getTarget()).method_23614(access.getTexturing()).method_23616(access.getWriteMaskState()).method_23609(access.getLineWidth());
        op.accept((Object)builder);
        return builder.method_24297(access.getOutlineMode());
    }
}
