package dev.loottech.api.manager.shader;

import dev.loottech.api.manager.shader.ManagedCoreShader;
import dev.loottech.api.manager.shader.ManagedShaderEffect;
import dev.loottech.api.manager.shader.ReloadableShaderEffectManager;
import java.util.function.Consumer;
import net.minecraft.class_293;
import net.minecraft.class_2960;

public interface ShaderEffectManager {
    public static ShaderEffectManager getInstance() {
        return ReloadableShaderEffectManager.INSTANCE;
    }

    public ManagedShaderEffect manage(class_2960 var1);

    public ManagedShaderEffect manage(class_2960 var1, Consumer<ManagedShaderEffect> var2);

    public ManagedCoreShader manageCoreShader(class_2960 var1);

    public ManagedCoreShader manageCoreShader(class_2960 var1, class_293 var2);

    public ManagedCoreShader manageCoreShader(class_2960 var1, class_293 var2, Consumer<ManagedCoreShader> var3);
}
