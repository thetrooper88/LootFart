package org.ladysnake.satin.api.managed.uniform;

import org.apiguardian.api.API;
import org.joml.Vector4f;

public interface Uniform4f {
    @API(status=API.Status.MAINTAINED, since="1.4.0")
    public void set(float var1, float var2, float var3, float var4);

    @API(status=API.Status.MAINTAINED, since="1.12.0")
    public void set(Vector4f var1);
}
