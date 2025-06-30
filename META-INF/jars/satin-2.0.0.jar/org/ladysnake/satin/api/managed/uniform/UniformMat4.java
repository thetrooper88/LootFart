package org.ladysnake.satin.api.managed.uniform;

import org.apiguardian.api.API;
import org.joml.Matrix4f;

public interface UniformMat4 {
    @API(status=API.Status.MAINTAINED, since="1.4.0")
    public void set(Matrix4f var1);

    @API(status=API.Status.MAINTAINED, since="1.15.0")
    public void setFromArray(float[] var1);
}
