package dev.loottech.asm.mixins;

import com.mojang.serialization.Codec;
import dev.loottech.api.manager.Managers;
import dev.loottech.client.events.PerspectiveUpdateEvent;
import java.io.File;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_315;
import net.minecraft.class_5498;
import net.minecraft.class_7172;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_315.class})
public abstract class GameOptionsMixin {
    @Mutable
    @Shadow
    @Final
    private class_7172<Integer> field_1826;

    @Inject(method={"<init>"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/option/GameOptions;load()V", shift=At.Shift.BEFORE)})
    private void hookInit(class_310 client, File optionsFile, CallbackInfo ci) {
        this.field_1826 = new class_7172("options.fov", class_7172.method_42399(), (optionText, value) -> switch (value) {
            case 70 -> class_315.method_41783((class_2561)optionText, (class_2561)class_2561.method_43471((String)"options.fov.min"));
            case 110 -> class_315.method_41783((class_2561)optionText, (class_2561)class_2561.method_43471((String)"options.fov.max"));
            default -> class_315.method_41782((class_2561)optionText, (int)value);
        }, (class_7172.class_7178)new class_7172.class_7174(30, 180), Codec.DOUBLE.xmap(value -> (int)(value * 40.0 + 70.0), value -> ((double)value.intValue() - 70.0) / 40.0), (Object)70, value -> class_310.method_1551().field_1769.method_3292());
    }

    @Inject(method={"setPerspective"}, at={@At(value="HEAD")})
    private void hookSetPerspective(class_5498 perspective, CallbackInfo ci) {
        PerspectiveUpdateEvent perspectiveEvent = new PerspectiveUpdateEvent(perspective);
        Managers.EVENT.call(perspectiveEvent);
    }
}
