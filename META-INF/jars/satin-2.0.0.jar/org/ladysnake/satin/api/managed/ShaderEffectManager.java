package org.ladysnake.satin.api.managed;

import java.util.function.Consumer;
import net.minecraft.class_293;
import net.minecraft.class_2960;
import org.apiguardian.api.API;
import org.ladysnake.satin.api.managed.ManagedCoreShader;
import org.ladysnake.satin.api.managed.ManagedShaderEffect;
import org.ladysnake.satin.impl.ReloadableShaderEffectManager;

public interface ShaderEffectManager {
    @API(status=API.Status.STABLE)
    public static ShaderEffectManager getInstance() {
        return ReloadableShaderEffectManager.INSTANCE;
    }

    @API(status=API.Status.STABLE, since="1.0.0")
    public ManagedShaderEffect manage(class_2960 var1);

    @API(status=API.Status.STABLE, since="1.0.0")
    public ManagedShaderEffect manage(class_2960 var1, Consumer<ManagedShaderEffect> var2);

    @API(status=API.Status.EXPERIMENTAL, since="1.6.0")
    public ManagedCoreShader manageCoreShader(class_2960 var1);

    @API(status=API.Status.EXPERIMENTAL, since="1.6.0")
    public ManagedCoreShader manageCoreShader(class_2960 var1, class_293 var2);

    @API(status=API.Status.EXPERIMENTAL, since="1.6.0")
    public ManagedCoreShader manageCoreShader(class_2960 var1, class_293 var2, Consumer<ManagedCoreShader> var3);

    @API(status=API.Status.STABLE, since="1.0.0")
    public void dispose(ManagedShaderEffect var1);

    @API(status=API.Status.EXPERIMENTAL, since="1.4.0")
    public void dispose(ManagedCoreShader var1);
}
