package org.ladysnake.satin.api.util;

import java.io.IOException;
import javax.annotation.Nullable;
import net.minecraft.class_2960;
import net.minecraft.class_3300;
import org.apiguardian.api.API;
import org.ladysnake.satin.impl.ValidatingShaderLoader;

public interface ShaderLoader {
    @API(status=API.Status.MAINTAINED)
    public static ShaderLoader getInstance() {
        return ValidatingShaderLoader.INSTANCE;
    }

    @API(status=API.Status.MAINTAINED)
    public int loadShader(class_3300 var1, @Nullable class_2960 var2, @Nullable class_2960 var3) throws IOException;
}
