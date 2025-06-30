package org.ladysnake.satin.api.managed;

import javax.annotation.Nullable;
import net.minecraft.class_1921;
import net.minecraft.class_276;
import org.apiguardian.api.API;

@API(status=API.Status.EXPERIMENTAL, since="1.4.0")
public interface ManagedFramebuffer {
    @Nullable
    public class_276 getFramebuffer();

    public void beginWrite(boolean var1);

    public void copyDepthFrom(class_276 var1);

    public void draw();

    public void draw(int var1, int var2, boolean var3);

    public void clear();

    public void clear(boolean var1);

    public class_1921 getRenderLayer(class_1921 var1);
}
