package dev.loottech.api.manager.shader;

import dev.loottech.api.utilities.interfaces.UniformFinder;
import net.minecraft.class_5944;

public interface ManagedCoreShader
extends UniformFinder {
    public class_5944 getProgram();

    public void release();
}
