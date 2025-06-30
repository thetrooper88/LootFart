package dev.loottech.api.manager.shader;

import com.google.common.base.Preconditions;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.logging.LogUtils;
import dev.loottech.api.manager.shader.FramebufferWrapper;
import dev.loottech.api.manager.shader.ManagedFramebuffer;
import dev.loottech.api.manager.shader.ManagedSamplerUniformV2;
import dev.loottech.api.manager.shader.ManagedShaderEffect;
import dev.loottech.api.manager.shader.ManagedUniformBase;
import dev.loottech.api.manager.shader.ResettableManagedShaderBase;
import dev.loottech.api.manager.shader.uniform.SamplerUniform;
import dev.loottech.api.manager.shader.uniform.SamplerUniformV2;
import dev.loottech.asm.mixins.accessor.AccessiblePassesShaderEffect;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.class_279;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_5912;

/*
 * Duplicate member names - consider using --renamedupmembers true
 */
public final class ResettableManagedShaderEffect
extends ResettableManagedShaderBase<class_279>
implements ManagedShaderEffect {
    private final Consumer<ManagedShaderEffect> initCallback;
    private final Map<String, FramebufferWrapper> managedTargets;
    private final Map<String, ManagedSamplerUniformV2> managedSamplers = new HashMap();

    public ResettableManagedShaderEffect(class_2960 location, Consumer<ManagedShaderEffect> initCallback) {
        super(location);
        this.initCallback = initCallback;
        this.managedTargets = new HashMap();
    }

    @Override
    public class_279 getShaderEffect() {
        return this.getShaderOrLog();
    }

    @Override
    protected class_279 parseShader(class_5912 resourceFactory, class_310 mc, class_2960 location) throws IOException {
        return new class_279(mc.method_1531(), (class_5912)mc.method_1478(), mc.method_1522(), location);
    }

    @Override
    public void setup(int windowWidth, int windowHeight) {
        Preconditions.checkNotNull((Object)((class_279)this.shader));
        ((class_279)this.shader).method_1259(windowWidth, windowHeight);
        for (ManagedUniformBase uniform : this.getManagedUniforms()) {
            this.setupUniform(uniform, (class_279)this.shader);
        }
        for (FramebufferWrapper buf : this.managedTargets.values()) {
            buf.findTarget((class_279)this.shader);
        }
        this.initCallback.accept((Object)this);
    }

    @Override
    public void render(float tickDelta) {
        class_279 sg = this.getShaderEffect();
        if (sg != null) {
            RenderSystem.disableBlend();
            RenderSystem.disableDepthTest();
            RenderSystem.resetTextureMatrix();
            sg.method_1258(tickDelta);
            class_310.method_1551().method_1522().method_1235(true);
            RenderSystem.disableBlend();
            RenderSystem.blendFunc((int)770, (int)771);
            RenderSystem.enableDepthTest();
        }
    }

    @Override
    public ManagedFramebuffer getTarget(String name) {
        return (ManagedFramebuffer)this.managedTargets.computeIfAbsent((Object)name, n -> {
            FramebufferWrapper ret = new FramebufferWrapper((String)n);
            if (this.shader != null) {
                ret.findTarget((class_279)this.shader);
            }
            return ret;
        });
    }

    @Override
    public void setUniformValue(String uniformName, int value) {
        this.findUniform1i(uniformName).set(value);
    }

    @Override
    public void setUniformValue(String uniformName, float value) {
        this.findUniform1f(uniformName).set(value);
    }

    @Override
    public void setUniformValue(String uniformName, float value0, float value1) {
        this.findUniform2f(uniformName).set(value0, value1);
    }

    @Override
    public void setUniformValue(String uniformName, float value0, float value1, float value2) {
        this.findUniform3f(uniformName).set(value0, value1, value2);
    }

    @Override
    public void setUniformValue(String uniformName, float value0, float value1, float value2, float value3) {
        this.findUniform4f(uniformName).set(value0, value1, value2, value3);
    }

    @Override
    public SamplerUniformV2 findSampler(String samplerName) {
        return this.manageUniform(this.managedSamplers, ManagedSamplerUniformV2::new, samplerName, "sampler");
    }

    @Override
    protected boolean setupUniform(ManagedUniformBase uniform, class_279 shader) {
        return uniform.findUniformTargets(((AccessiblePassesShaderEffect)shader).getPasses());
    }

    @Override
    protected void logInitError(IOException e) {
        LogUtils.getLogger().error("Could not create screen shader {}", (Object)this.getLocation(), (Object)e);
    }

    private class_279 getShaderOrLog() {
        if (!this.isInitialized() && !this.isErrored()) {
            this.initializeOrLog((class_5912)class_310.method_1551().method_1478());
        }
        return (class_279)this.shader;
    }

    @Override
    protected boolean setupUniform(ManagedUniformBase managedUniformBase, AutoCloseable autoCloseable) {
        return this.setupUniform(managedUniformBase, (class_279)autoCloseable);
    }

    @Override
    protected AutoCloseable parseShader(class_5912 class_59122, class_310 class_3102, class_2960 class_29602) throws IOException {
        return this.parseShader(class_59122, class_3102, class_29602);
    }

    @Override
    public SamplerUniform findSampler(String string) {
        return this.findSampler(string);
    }
}
