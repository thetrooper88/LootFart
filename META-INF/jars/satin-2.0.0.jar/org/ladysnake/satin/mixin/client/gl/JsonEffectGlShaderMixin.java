package org.ladysnake.satin.mixin.client.gl;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import java.util.List;
import java.util.Map;
import java.util.function.IntSupplier;
import net.minecraft.class_280;
import net.minecraft.class_281;
import net.minecraft.class_2960;
import net.minecraft.class_5912;
import org.ladysnake.satin.impl.SamplerAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value={class_280.class})
public abstract class JsonEffectGlShaderMixin
implements SamplerAccess {
    @Shadow
    @Final
    private Map<String, IntSupplier> field_1516;

    @Override
    public void satin$removeSampler(String name) {
        this.field_1516.remove((Object)name);
    }

    @Override
    public boolean satin$hasSampler(String name) {
        return this.field_1516.containsKey((Object)name);
    }

    @Override
    @Accessor(value="samplerNames")
    public abstract List<String> satin$getSamplerNames();

    @Override
    @Accessor(value="samplerLocations")
    public abstract List<Integer> satin$getSamplerShaderLocs();

    @WrapOperation(at={@At(value="INVOKE", target="net/minecraft/util/Identifier.ofVanilla (Ljava/lang/String;)Lnet/minecraft/util/Identifier;", ordinal=0)}, method={"<init>"})
    class_2960 constructProgramIdentifier(String arg, Operation<class_2960> original, class_5912 unused, String id) {
        if (!id.contains((CharSequence)":")) {
            return (class_2960)original.call(new Object[]{arg});
        }
        class_2960 split = class_2960.method_60654((String)id);
        return class_2960.method_60655((String)split.method_12836(), (String)("shaders/program/" + split.method_12832() + ".json"));
    }

    @WrapOperation(at={@At(value="INVOKE", target="net/minecraft/util/Identifier.ofVanilla (Ljava/lang/String;)Lnet/minecraft/util/Identifier;", ordinal=0)}, method={"loadEffect"})
    private static class_2960 constructProgramIdentifier(String arg, Operation<class_2960> original, class_5912 unused, class_281.class_282 shaderType, String id) {
        if (!arg.contains((CharSequence)":")) {
            return (class_2960)original.call(new Object[]{arg});
        }
        class_2960 split = class_2960.method_60654((String)id);
        return class_2960.method_60655((String)split.method_12836(), (String)("shaders/program/" + split.method_12832() + shaderType.method_1284()));
    }
}
