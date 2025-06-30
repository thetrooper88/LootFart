package dev.loottech.api.manager.shader.uniform;

import org.joml.Vector4f;

public interface Uniform4f {
    public void set(float var1, float var2, float var3, float var4);

    public void set(Vector4f var1);
}
