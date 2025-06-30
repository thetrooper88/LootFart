package dev.loottech.api.manager.shader;

import dev.loottech.api.manager.shader.ManagedFramebuffer;
import dev.loottech.api.manager.shader.uniform.SamplerUniform;
import dev.loottech.api.manager.shader.uniform.SamplerUniformV2;
import dev.loottech.api.utilities.interfaces.UniformFinder;
import net.minecraft.class_279;

/*
 * Duplicate member names - consider using --renamedupmembers true
 */
public interface ManagedShaderEffect
extends UniformFinder {
    public class_279 getShaderEffect();

    public void release();

    public void render(float var1);

    public ManagedFramebuffer getTarget(String var1);

    public void setUniformValue(String var1, int var2);

    public void setUniformValue(String var1, float var2);

    public void setUniformValue(String var1, float var2, float var3);

    public void setUniformValue(String var1, float var2, float var3, float var4);

    public void setUniformValue(String var1, float var2, float var3, float var4, float var5);

    @Override
    public SamplerUniformV2 findSampler(String var1);

    @Override
    default public SamplerUniform findSampler(String string) {
        return this.findSampler(string);
    }
}
