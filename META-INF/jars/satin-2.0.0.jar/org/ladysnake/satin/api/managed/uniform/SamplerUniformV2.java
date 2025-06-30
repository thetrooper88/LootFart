package org.ladysnake.satin.api.managed.uniform;

import java.util.function.IntSupplier;
import org.apiguardian.api.API;
import org.ladysnake.satin.api.managed.uniform.SamplerUniform;

public interface SamplerUniformV2
extends SamplerUniform {
    @API(status=API.Status.EXPERIMENTAL, since="1.4.0")
    public void set(IntSupplier var1);
}
