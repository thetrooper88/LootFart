package dev.loottech.api.manager.shader.uniform;

import org.joml.Matrix4f;

public interface UniformMat4 {
    public void set(Matrix4f var1);

    public void setFromArray(float[] var1);
}
