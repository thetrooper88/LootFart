package dev.loottech.api.manager.shader.uniform;

import dev.loottech.api.manager.shader.uniform.SamplerUniform;
import java.util.function.IntSupplier;

public interface SamplerUniformV2
extends SamplerUniform {
    public void set(IntSupplier var1);
}
