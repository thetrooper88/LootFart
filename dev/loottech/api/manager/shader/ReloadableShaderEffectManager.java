package dev.loottech.api.manager.shader;

import dev.loottech.api.manager.shader.ManagedCoreShader;
import dev.loottech.api.manager.shader.ManagedShaderEffect;
import dev.loottech.api.manager.shader.ResettableManagedCoreShader;
import dev.loottech.api.manager.shader.ResettableManagedShaderBase;
import dev.loottech.api.manager.shader.ResettableManagedShaderEffect;
import dev.loottech.api.manager.shader.ShaderEffectManager;
import dev.loottech.api.manager.shader.event.WindowResizeCallback;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import java.util.Set;
import java.util.function.Consumer;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_2960;
import net.minecraft.class_5912;

public final class ReloadableShaderEffectManager
implements ShaderEffectManager {
    public static final ReloadableShaderEffectManager INSTANCE = new ReloadableShaderEffectManager();
    private final Set<ResettableManagedShaderBase<?>> managedShaders = new ReferenceOpenHashSet();

    public ReloadableShaderEffectManager() {
        WindowResizeCallback.EVENT.register((client, window) -> this.onResolutionChanged(window.method_4489(), window.method_4506()));
    }

    @Override
    public ManagedShaderEffect manage(class_2960 location) {
        return this.manage(location, (Consumer<ManagedShaderEffect>)((Consumer)s -> {}));
    }

    @Override
    public ManagedShaderEffect manage(class_2960 location, Consumer<ManagedShaderEffect> initCallback) {
        ResettableManagedShaderEffect ret = new ResettableManagedShaderEffect(location, initCallback);
        this.managedShaders.add((Object)ret);
        return ret;
    }

    @Override
    public ManagedCoreShader manageCoreShader(class_2960 location) {
        return this.manageCoreShader(location, class_290.field_1580);
    }

    @Override
    public ManagedCoreShader manageCoreShader(class_2960 location, class_293 vertexFormat) {
        return this.manageCoreShader(location, vertexFormat, (Consumer<ManagedCoreShader>)((Consumer)s -> {}));
    }

    @Override
    public ManagedCoreShader manageCoreShader(class_2960 location, class_293 vertexFormat, Consumer<ManagedCoreShader> initCallback) {
        ResettableManagedCoreShader ret = new ResettableManagedCoreShader(location, vertexFormat, initCallback);
        this.managedShaders.add((Object)ret);
        return ret;
    }

    public void reload(class_5912 shaderResources) {
        for (ResettableManagedShaderBase ss : this.managedShaders) {
            ss.initializeOrLog(shaderResources);
        }
    }

    public void onResolutionChanged(int newWidth, int newHeight) {
        this.runShaderSetup(newWidth, newHeight);
    }

    private void runShaderSetup(int newWidth, int newHeight) {
        if (!this.managedShaders.isEmpty()) {
            for (ResettableManagedShaderBase ss : this.managedShaders) {
                if (!ss.isInitialized()) continue;
                ss.setup(newWidth, newHeight);
            }
        }
    }
}
