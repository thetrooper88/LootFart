package dev.loottech.api.manager.shader;

import com.mojang.logging.LogUtils;
import dev.loottech.api.manager.shader.ManagedFramebuffer;
import net.minecraft.class_1041;
import net.minecraft.class_276;
import net.minecraft.class_279;
import net.minecraft.class_310;

public final class FramebufferWrapper
implements ManagedFramebuffer {
    private final String name;
    private class_276 wrapped;

    FramebufferWrapper(String name) {
        this.name = name;
    }

    void findTarget(class_279 shaderEffect) {
        if (shaderEffect == null) {
            this.wrapped = null;
        } else {
            this.wrapped = shaderEffect.method_1264(this.name);
            if (this.wrapped == null) {
                LogUtils.getLogger().warn("No target framebuffer found with name {} in shader {}", (Object)this.name, (Object)shaderEffect.method_1260());
            }
        }
    }

    public String getName() {
        return this.name;
    }

    @Override
    public class_276 getFramebuffer() {
        return this.wrapped;
    }

    @Override
    public void beginWrite(boolean updateViewport) {
        if (this.wrapped != null) {
            this.wrapped.method_1235(updateViewport);
        }
    }

    @Override
    public void draw() {
        class_1041 window = class_310.method_1551().method_22683();
        this.draw(window.method_4489(), window.method_4506(), true);
    }

    @Override
    public void draw(int width, int height, boolean disableBlend) {
        if (this.wrapped != null) {
            this.wrapped.method_22594(width, height, disableBlend);
        }
    }

    @Override
    public void clear() {
        this.clear(class_310.field_1703);
    }

    @Override
    public void clear(boolean swallowErrors) {
        if (this.wrapped != null) {
            this.wrapped.method_1230(swallowErrors);
        }
    }
}
