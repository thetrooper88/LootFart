package org.ladysnake.satin.api.managed.uniform;

import org.apiguardian.api.API;
import org.joml.Vector3f;

public interface Uniform3f {
    @API(status=API.Status.MAINTAINED, since="1.4.0")
    public void set(float var1, float var2, float var3);

    @API(status=API.Status.MAINTAINED, since="1.12.0")
    public void set(Vector3f var1);
}
