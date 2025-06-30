package dev.loottech.asm.mixins;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.utilities.MathUtils;
import dev.loottech.client.events.RenderGameTextEvent;
import dev.loottech.client.modules.client.Font;
import net.minecraft.class_327;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_5348;
import net.minecraft.class_5481;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_327.class})
public class TextRendererMixin {
    @Inject(method={"drawLayer(Ljava/lang/String;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/font/TextRenderer$TextLayerType;II)F"}, at={@At(value="HEAD")}, cancellable=true)
    private void drawLayer$String(String text, float x, float y, int color, boolean shadow, Matrix4f matrix, class_4597 vertexConsumerProvider, class_327.class_6415 layerType, int underlineColor, int light, CallbackInfoReturnable<Float> cir) {
        class_4587 matrices = new class_4587();
        matrices.method_22903();
        matrices.method_34425(matrix);
        RenderGameTextEvent event = new RenderGameTextEvent(text, color, x, y, shadow, matrices);
        Managers.EVENT.call(event);
        matrices.method_22909();
        if (event.isCanceled()) {
            cir.setReturnValue((Object)Float.valueOf((float)(x + Managers.FONT.getWidth(text))));
        }
    }

    @Inject(method={"drawLayer(Lnet/minecraft/text/OrderedText;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/font/TextRenderer$TextLayerType;II)F"}, at={@At(value="HEAD")}, cancellable=true)
    private void drawLayer$OrderedText(class_5481 text, float x, float y, int color, boolean shadow, Matrix4f matrix, class_4597 vertexConsumerProvider, class_327.class_6415 layerType, int underlineColor, int light, CallbackInfoReturnable<Float> cir) {
        class_4587 matrices = new class_4587();
        matrices.method_22903();
        matrices.method_34425(matrix);
        RenderGameTextEvent event = new RenderGameTextEvent(MathUtils.orderedTextToString(text), color, x, y, shadow, matrices);
        Managers.EVENT.call(event);
        matrices.method_22909();
        if (event.isCanceled()) {
            cir.setReturnValue((Object)Float.valueOf((float)(x + Managers.FONT.getWidth(MathUtils.orderedTextToString(text)))));
        }
    }

    @Inject(method={"getWidth(Ljava/lang/String;)I"}, at={@At(value="HEAD")}, cancellable=true)
    private void getWidth(String text, CallbackInfoReturnable<Integer> info) {
        if (Managers.MODULE.getInstance(Font.class).isEnabled() && Managers.MODULE.getInstance(Font.class).globalFont.getValue()) {
            info.setReturnValue((Object)((int)Managers.FONT.getWidth(text)));
        }
    }

    @Inject(method={"getWidth(Lnet/minecraft/text/StringVisitable;)I"}, at={@At(value="HEAD")}, cancellable=true)
    private void getWidth(class_5348 text, CallbackInfoReturnable<Integer> info) {
        if (Managers.MODULE.getInstance(Font.class).isEnabled() && Managers.MODULE.getInstance(Font.class).globalFont.getValue()) {
            info.setReturnValue((Object)((int)Managers.FONT.getWidth(text.getString())));
        }
    }

    @Inject(method={"getWidth(Lnet/minecraft/text/OrderedText;)I"}, at={@At(value="HEAD")}, cancellable=true)
    private void getWidth(class_5481 text, CallbackInfoReturnable<Integer> info) {
        if (Managers.MODULE.getInstance(Font.class).isEnabled() && Managers.MODULE.getInstance(Font.class).globalFont.getValue()) {
            info.setReturnValue((Object)((int)Managers.FONT.getWidth(MathUtils.orderedTextToString(text))));
        }
    }
}
