package dev.loottech.api.manager.shader;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.event.EventListener;
import dev.loottech.api.manager.shader.ManagedShaderEffect;
import dev.loottech.api.manager.shader.ShaderEffectManager;
import dev.loottech.api.utilities.Util;
import dev.loottech.api.utilities.interfaces.IShaderEffect;
import dev.loottech.client.modules.visuals.Shaders;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import net.minecraft.class_276;
import net.minecraft.class_279;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import org.jetbrains.annotations.NotNull;

public class ShaderManager
implements Util,
EventListener {
    private static final List<RenderTask> tasks = new ArrayList();
    private loottechFrameBuffer shaderBuffer;
    public static ManagedShaderEffect DEFAULT_OUTLINE;
    public static ManagedShaderEffect DEFAULT;

    public void renderShader(Runnable runnable, Shader mode) {
        tasks.add((Object)new RenderTask(runnable, mode));
    }

    public void renderShaders() {
        if (DEFAULT == null) {
            this.shaderBuffer = new loottechFrameBuffer(ShaderManager.mc.method_1522().field_1482, ShaderManager.mc.method_1522().field_1481);
            this.reloadShaders();
        }
        if (this.shaderBuffer == null) {
            return;
        }
        tasks.forEach(t -> this.applyShader(t.task(), t.shader()));
        tasks.clear();
    }

    public void applyShader(Runnable runnable, Shader mode) {
        class_276 MCBuffer = class_310.method_1551().method_1522();
        RenderSystem.assertOnRenderThreadOrInit();
        if (this.shaderBuffer.field_1482 != MCBuffer.field_1482 || this.shaderBuffer.field_1481 != MCBuffer.field_1481) {
            this.shaderBuffer.method_1234(MCBuffer.field_1482, MCBuffer.field_1481, false);
        }
        GlStateManager._glBindFramebuffer((int)36009, (int)this.shaderBuffer.field_1476);
        this.shaderBuffer.method_1235(true);
        runnable.run();
        this.shaderBuffer.method_1240();
        GlStateManager._glBindFramebuffer((int)36009, (int)MCBuffer.field_1476);
        MCBuffer.method_1235(false);
        ManagedShaderEffect shader = this.getShader(mode);
        class_276 mainBuffer = class_310.method_1551().method_1522();
        class_279 effect = shader.getShaderEffect();
        if (effect != null) {
            ((IShaderEffect)effect).loottech$addFakeTargetHook("bufIn", this.shaderBuffer);
        }
        class_276 outBuffer = ((class_279)Objects.requireNonNull((Object)shader.getShaderEffect())).method_1264("bufOut");
        this.setupShader(mode, shader);
        this.shaderBuffer.method_1230(false);
        mainBuffer.method_1235(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate((GlStateManager.class_4535)GlStateManager.class_4535.SRC_ALPHA, (GlStateManager.class_4534)GlStateManager.class_4534.ONE_MINUS_SRC_ALPHA, (GlStateManager.class_4535)GlStateManager.class_4535.ZERO, (GlStateManager.class_4534)GlStateManager.class_4534.ONE);
        RenderSystem.backupProjectionMatrix();
        outBuffer.method_22594(outBuffer.field_1482, outBuffer.field_1481, false);
        RenderSystem.restoreProjectionMatrix();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }

    public ManagedShaderEffect getShader(@NotNull Shader mode) {
        return DEFAULT;
    }

    public ManagedShaderEffect getShaderOutline(@NotNull Shader mode) {
        return DEFAULT_OUTLINE;
    }

    public void setupShader(Shader shader, ManagedShaderEffect effect) {
        Shaders shaders = Managers.MODULE.getInstance(Shaders.class);
        if (shader == Shader.Default) {
            effect.setUniformValue("alpha0", shaders.glow.getValue() ? -1.0f : (float)shaders.outlineColor.getValue().getAlpha() / 255.0f);
            effect.setUniformValue("lineWidth", shaders.lineWidth.getValue().intValue() * 2);
            effect.setUniformValue("quality", shaders.quality.getValue().intValue());
            effect.setUniformValue("color", (float)shaders.fillColor1.getValue().getRed() / 255.0f, (float)shaders.fillColor1.getValue().getGreen() / 255.0f, (float)shaders.fillColor1.getValue().getBlue() / 255.0f, (float)shaders.fillColor1.getValue().getAlpha() / 255.0f);
            effect.setUniformValue("outlinecolor", (float)shaders.outlineColor.getValue().getRed() / 255.0f, (float)shaders.outlineColor.getValue().getGreen() / 255.0f, (float)shaders.outlineColor.getValue().getBlue() / 255.0f, (float)shaders.outlineColor.getValue().getAlpha() / 255.0f);
            effect.render(ShaderManager.getTickDelta());
        }
    }

    public void reloadShaders() {
        DEFAULT = ShaderEffectManager.getInstance().manage(class_2960.method_60655((String)"loottech", (String)"shaders/post/outline.json"));
        DEFAULT_OUTLINE = ShaderEffectManager.getInstance().manage(class_2960.method_60655((String)"loottech", (String)"shaders/post/outline.json"), (Consumer<ManagedShaderEffect>)((Consumer)managedShaderEffect -> {
            class_279 effect = managedShaderEffect.getShaderEffect();
            if (effect == null) {
                return;
            }
            ((IShaderEffect)effect).loottech$addFakeTargetHook("bufIn", ShaderManager.mc.field_1769.method_22990());
            ((IShaderEffect)effect).loottech$addFakeTargetHook("bufOut", ShaderManager.mc.field_1769.method_22990());
        }));
    }

    public boolean fullNullCheck() {
        if (DEFAULT == null) {
            this.shaderBuffer = new loottechFrameBuffer(ShaderManager.mc.method_1522().field_1482, ShaderManager.mc.method_1522().field_1481);
            this.reloadShaders();
            return true;
        }
        return false;
    }

    public static float getTickDelta() {
        return mc.method_60646().method_60637(true);
    }

    public record RenderTask(Runnable task, Shader shader) {
    }

    public static enum Shader {
        Default;

    }

    public static class loottechFrameBuffer
    extends class_276 {
        public loottechFrameBuffer(int width, int height) {
            super(false);
            RenderSystem.assertOnRenderThreadOrInit();
            this.method_1234(width, height, true);
            this.method_1236(0.0f, 0.0f, 0.0f, 0.0f);
        }
    }
}
