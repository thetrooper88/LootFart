package org.ladysnake.satin.api.managed.uniform;

import org.apiguardian.api.API;
import org.joml.Vector2f;

public interface Uniform2f {
    @API(status=API.Status.MAINTAINED, since="1.4.0")
    public void set(float var1, float var2);

    @API(status=API.Status.MAINTAINED, since="1.11.0")
    public void set(Vector2f var1);
}
