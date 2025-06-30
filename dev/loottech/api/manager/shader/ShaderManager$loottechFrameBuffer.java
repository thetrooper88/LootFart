package dev.loottech.api.manager.shader;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.class_276;

public static class ShaderManager.loottechFrameBuffer
extends class_276 {
    public ShaderManager.loottechFrameBuffer(int width, int height) {
        super(false);
        RenderSystem.assertOnRenderThreadOrInit();
        this.method_1234(width, height, true);
        this.method_1236(0.0f, 0.0f, 0.0f, 0.0f);
    }
}
