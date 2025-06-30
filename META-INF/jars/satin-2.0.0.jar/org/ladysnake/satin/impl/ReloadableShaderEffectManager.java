package org.ladysnake.satin.impl;

import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import java.util.Set;
import java.util.function.Consumer;
import net.minecraft.class_1041;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_5912;
import net.minecraft.class_761;
import org.ladysnake.satin.Satin;
import org.ladysnake.satin.api.event.ResolutionChangeCallback;
import org.ladysnake.satin.api.event.WorldRendererReloadCallback;
import org.ladysnake.satin.api.managed.ManagedCoreShader;
import org.ladysnake.satin.api.managed.ManagedShaderEffect;
import org.ladysnake.satin.api.managed.ShaderEffectManager;
import org.ladysnake.satin.impl.ResettableManagedCoreShader;
import org.ladysnake.satin.impl.ResettableManagedShaderBase;
import org.ladysnake.satin.impl.ResettableManagedShaderEffect;

public final class ReloadableShaderEffectManager
implements ShaderEffectManager,
ResolutionChangeCallback,
WorldRendererReloadCallback {
    public static final ReloadableShaderEffectManager INSTANCE = new ReloadableShaderEffectManager();
    private final Set<ResettableManagedShaderBase<?>> managedShaders = new ReferenceOpenHashSet();

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

    @Override
    public void dispose(ManagedShaderEffect shader) {
        shader.release();
        this.managedShaders.remove((Object)shader);
    }

    @Override
    public void dispose(ManagedCoreShader shader) {
        shader.release();
        this.managedShaders.remove((Object)shader);
    }

    public void reload(class_5912 shaderResources) {
        for (ResettableManagedShaderBase ss : this.managedShaders) {
            ss.initializeOrLog(shaderResources);
        }
    }

    @Override
    public void onResolutionChanged(int newWidth, int newHeight) {
        this.runShaderSetup(newWidth, newHeight);
    }

    @Override
    public void onRendererReload(class_761 renderer) {
        class_1041 window = class_310.method_1551().method_22683();
        this.runShaderSetup(window.method_4489(), window.method_4506());
    }

    private void runShaderSetup(int newWidth, int newHeight) {
        if (!Satin.areShadersDisabled() && !this.managedShaders.isEmpty()) {
            for (ResettableManagedShaderBase ss : this.managedShaders) {
                if (!ss.isInitialized()) continue;
                ss.setup(newWidth, newHeight);
            }
        }
    }
}
