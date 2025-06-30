package org.ladysnake.satin.api.managed.uniform;

import net.minecraft.class_1044;
import net.minecraft.class_276;
import org.apiguardian.api.API;

@API(status=API.Status.MAINTAINED)
public interface SamplerUniform {
    @API(status=API.Status.MAINTAINED, since="1.4.0")
    public void setDirect(int var1);

    @API(status=API.Status.MAINTAINED, since="1.4.0")
    public void set(class_1044 var1);

    @API(status=API.Status.MAINTAINED, since="1.4.0")
    public void set(class_276 var1);

    @API(status=API.Status.MAINTAINED, since="1.4.0")
    public void set(int var1);
}
