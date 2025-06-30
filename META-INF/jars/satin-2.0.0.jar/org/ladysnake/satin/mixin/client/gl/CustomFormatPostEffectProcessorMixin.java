package org.ladysnake.satin.mixin.client.gl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.class_1060;
import net.minecraft.class_279;
import net.minecraft.class_2960;
import net.minecraft.class_3518;
import org.ladysnake.satin.impl.CustomFormatFramebuffers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value={class_279.class})
public class CustomFormatPostEffectProcessorMixin {
    @Inject(method={"parseTarget"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/gl/PostEffectProcessor;addTarget(Ljava/lang/String;II)V", ordinal=1)}, locals=LocalCapture.CAPTURE_FAILSOFT)
    private void satin$parseCustomTargetFormat(JsonElement jsonTarget, CallbackInfo ci, JsonObject jsonObject) {
        String format = class_3518.method_15253((JsonObject)jsonObject, (String)"satin:format", null);
        if (format != null) {
            CustomFormatFramebuffers.prepareCustomFormat(format);
        }
    }

    @Inject(method={"parseEffect"}, slice={@Slice(from=@At(value="CONSTANT:FIRST", args={"stringValue=targets"}), to=@At(value="CONSTANT:FIRST", args={"stringValue=passes"}))}, at={@At(value="INVOKE", target="Lnet/minecraft/util/InvalidHierarchicalFileException;wrap(Ljava/lang/Exception;)Lnet/minecraft/util/InvalidHierarchicalFileException;")}, allow=1)
    private void satin$cleanupCustomTargetFormat(class_1060 textureManager, class_2960 id, CallbackInfo ci) {
        CustomFormatFramebuffers.clearCustomFormat();
    }
}
