package org.ladysnake.satin.impl;

import com.google.common.base.Preconditions;
import com.mojang.blaze3d.systems.RenderSystem;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.class_1044;
import net.minecraft.class_276;
import net.minecraft.class_279;
import net.minecraft.class_280;
import net.minecraft.class_283;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_5912;
import org.apiguardian.api.API;
import org.joml.Matrix4f;
import org.ladysnake.satin.Satin;
import org.ladysnake.satin.api.managed.ManagedFramebuffer;
import org.ladysnake.satin.api.managed.ManagedShaderEffect;
import org.ladysnake.satin.api.managed.uniform.SamplerUniform;
import org.ladysnake.satin.api.managed.uniform.SamplerUniformV2;
import org.ladysnake.satin.api.util.ShaderPrograms;
import org.ladysnake.satin.impl.FramebufferWrapper;
import org.ladysnake.satin.impl.ManagedSamplerUniformV2;
import org.ladysnake.satin.impl.ManagedUniformBase;
import org.ladysnake.satin.impl.ResettableManagedShaderBase;
import org.ladysnake.satin.mixin.client.AccessiblePassesShaderEffect;

/*
 * Duplicate member names - consider using --renamedupmembers true
 */
public final class ResettableManagedShaderEffect
extends ResettableManagedShaderBase<class_279>
implements ManagedShaderEffect {
    private final Consumer<ManagedShaderEffect> initCallback;
    private final Map<String, FramebufferWrapper> managedTargets;
    private final Map<String, ManagedSamplerUniformV2> managedSamplers = new HashMap();

    @API(status=API.Status.INTERNAL)
    public ResettableManagedShaderEffect(class_2960 location, Consumer<ManagedShaderEffect> initCallback) {
        super(location);
        this.initCallback = initCallback;
        this.managedTargets = new HashMap();
    }

    @Override
    @Nullable
    public class_279 getShaderEffect() {
        return this.getShaderOrLog();
    }

    @Override
    public void initialize() throws IOException {
        super.initialize((class_5912)class_310.method_1551().method_1478());
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
    public void setUniformValue(String uniformName, int value0, int value1) {
        this.findUniform2i(uniformName).set(value0, value1);
    }

    @Override
    public void setUniformValue(String uniformName, int value0, int value1, int value2) {
        this.findUniform3i(uniformName).set(value0, value1, value2);
    }

    @Override
    public void setUniformValue(String uniformName, int value0, int value1, int value2, int value3) {
        this.findUniform4i(uniformName).set(value0, value1, value2, value3);
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
    public void setUniformValue(String uniformName, Matrix4f value) {
        this.findUniformMat4(uniformName).set(value);
    }

    @Override
    public void setSamplerUniform(String samplerName, class_1044 texture) {
        this.findSampler(samplerName).set(texture);
    }

    @Override
    public void setSamplerUniform(String samplerName, class_276 textureFbo) {
        this.findSampler(samplerName).set(textureFbo);
    }

    @Override
    public void setSamplerUniform(String samplerName, int textureName) {
        this.findSampler(samplerName).set(textureName);
    }

    @Override
    public SamplerUniformV2 findSampler(String samplerName) {
        return this.manageUniform(this.managedSamplers, ManagedSamplerUniformV2::new, samplerName, "sampler");
    }

    @Override
    public void setupDynamicUniforms(Runnable dynamicSetBlock) {
        this.setupDynamicUniforms(0, dynamicSetBlock);
    }

    @Override
    public void setupDynamicUniforms(int index, Runnable dynamicSetBlock) {
        AccessiblePassesShaderEffect sg = (AccessiblePassesShaderEffect)this.getShaderEffect();
        if (sg != null) {
            class_280 sm = ((class_283)sg.getPasses().get(index)).method_1295();
            ShaderPrograms.useShader(sm.method_1270());
            dynamicSetBlock.run();
            ShaderPrograms.useShader(0);
        }
    }

    @Override
    protected boolean setupUniform(ManagedUniformBase uniform, class_279 shader) {
        return uniform.findUniformTargets(((AccessiblePassesShaderEffect)shader).getPasses());
    }

    @Override
    protected void logInitError(IOException e) {
        Satin.LOGGER.error("Could not create screen shader {}", (Object)this.getLocation(), (Object)e);
    }

    @Nullable
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
