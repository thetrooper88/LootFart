package dev.loottech.asm.mixins;

import dev.loottech.api.manager.Managers;
import dev.loottech.client.events.RenderCrystalEvent;
import dev.loottech.client.modules.visuals.Chams;
import net.minecraft.class_1511;
import net.minecraft.class_1921;
import net.minecraft.class_2338;
import net.minecraft.class_2960;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_4597;
import net.minecraft.class_4608;
import net.minecraft.class_630;
import net.minecraft.class_7833;
import net.minecraft.class_892;
import net.minecraft.class_895;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_892.class})
public class EndCrystalEntityRendererMixin {
    @Shadow
    @Final
    private class_630 field_21003;
    @Shadow
    @Final
    private class_630 field_21004;
    @Mutable
    @Shadow
    @Final
    private static class_1921 field_21736;
    @Shadow
    @Final
    private class_630 field_21005;
    @Shadow
    @Final
    private static class_2960 field_4663;
    @Shadow
    @Final
    private static float field_21002;
    @Unique
    final class_2960 BLANK = class_2960.method_60654((String)"textures/blank.png");

    @Inject(method={"render(Lnet/minecraft/entity/decoration/EndCrystalEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void hookRender(class_1511 endCrystalEntity, float f, float g, class_4587 matrixStack, class_4597 vertexConsumerProvider, int i, CallbackInfo ci) {
        RenderCrystalEvent renderCrystalEvent = new RenderCrystalEvent(endCrystalEntity, f, g, matrixStack, i, this.field_21003, this.field_21004);
        Managers.EVENT.call(renderCrystalEvent);
        ci.cancel();
        if (!renderCrystalEvent.isCanceled()) {
            Chams module = Managers.MODULE.getInstance(Chams.class);
            field_21736 = class_1921.method_23580((class_2960)(module.isEnabled() && !module.wallsConfig.getValue() ? this.BLANK : field_4663));
            matrixStack.method_22903();
            float h = !renderCrystalEvent.getBounce() ? -1.0f : class_892.method_23155((class_1511)endCrystalEntity, (float)g);
            float j = ((float)endCrystalEntity.field_7034 + g) * renderCrystalEvent.getSpin();
            class_4588 vertexConsumer = vertexConsumerProvider.getBuffer(field_21736);
            matrixStack.method_22903();
            matrixStack.method_22905(renderCrystalEvent.getScale(), renderCrystalEvent.getScale(), renderCrystalEvent.getScale());
            matrixStack.method_22905(2.0f, 2.0f, 2.0f);
            matrixStack.method_46416(0.0f, -0.5f, 0.0f);
            int k = class_4608.field_21444;
            if (endCrystalEntity.method_6836()) {
                this.field_21005.method_22698(matrixStack, vertexConsumer, i, k);
            }
            matrixStack.method_22907(class_7833.field_40716.rotationDegrees(j));
            matrixStack.method_46416(0.0f, 1.5f + h / 2.0f, 0.0f);
            matrixStack.method_22907(new Quaternionf().setAngleAxis(1.0471976f, field_21002, 0.0f, field_21002));
            this.field_21004.method_22698(matrixStack, vertexConsumer, i, k);
            float l = 0.875f;
            matrixStack.method_22905(0.875f, 0.875f, 0.875f);
            matrixStack.method_22907(new Quaternionf().setAngleAxis(1.0471976f, field_21002, 0.0f, field_21002));
            matrixStack.method_22907(class_7833.field_40716.rotationDegrees(j));
            this.field_21004.method_22698(matrixStack, vertexConsumer, i, k);
            matrixStack.method_22905(0.875f, 0.875f, 0.875f);
            matrixStack.method_22907(new Quaternionf().setAngleAxis(1.0471976f, field_21002, 0.0f, field_21002));
            matrixStack.method_22907(class_7833.field_40716.rotationDegrees(j));
            this.field_21003.method_22698(matrixStack, vertexConsumer, i, k);
            matrixStack.method_22905(1.0f / renderCrystalEvent.getScale(), 1.0f / renderCrystalEvent.getScale(), 1.0f / renderCrystalEvent.getScale());
            matrixStack.method_22909();
            matrixStack.method_22909();
            class_2338 blockPos = endCrystalEntity.method_6838();
            if (blockPos != null) {
                float m = (float)blockPos.method_10263() + 0.5f;
                float n = (float)blockPos.method_10264() + 0.5f;
                float o = (float)blockPos.method_10260() + 0.5f;
                float p = (float)((double)m - endCrystalEntity.method_23317());
                float q = (float)((double)n - endCrystalEntity.method_23318());
                float r = (float)((double)o - endCrystalEntity.method_23321());
                matrixStack.method_46416(p, q, r);
                class_895.method_3917((float)(-p), (float)(-q + h), (float)(-r), (float)g, (int)endCrystalEntity.field_7034, (class_4587)matrixStack, (class_4597)vertexConsumerProvider, (int)i);
            }
        }
    }
}
