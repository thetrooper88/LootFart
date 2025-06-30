package org.ladysnake.satin.api.managed;

import net.minecraft.class_1921;
import net.minecraft.class_5944;
import org.apiguardian.api.API;
import org.ladysnake.satin.api.managed.uniform.UniformFinder;

@API(status=API.Status.EXPERIMENTAL, since="1.6.0")
public interface ManagedCoreShader
extends UniformFinder {
    public class_5944 getProgram();

    public void release();

    public class_1921 getRenderLayer(class_1921 var1);
}
