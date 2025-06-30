package dev.loottech.api.manager.shader.uniform;

import org.joml.Vector3f;

public interface Uniform3f {
    public void set(float var1, float var2, float var3);

    public void set(Vector3f var1);
}
