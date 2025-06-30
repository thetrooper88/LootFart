package org.ladysnake.satin.mixin.client.gl;

import java.util.List;
import java.util.Map;
import net.minecraft.class_5944;
import org.ladysnake.satin.impl.SamplerAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_5944.class})
public abstract class CoreShaderMixin
implements SamplerAccess {
    @Shadow
    @Final
    private Map<String, Object> field_29487;

    @Override
    public void satin$removeSampler(String name) {
        this.field_29487.remove((Object)name);
    }

    @Override
    public boolean satin$hasSampler(String name) {
        return this.field_29487.containsKey((Object)name);
    }

    @Override
    @Accessor(value="samplerNames")
    public abstract List<String> satin$getSamplerNames();

    @Override
    @Accessor(value="loadedSamplerIds")
    public abstract List<Integer> satin$getSamplerShaderLocs();
}
