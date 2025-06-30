package org.ladysnake.satin.api.managed;

import java.io.IOException;
import javax.annotation.Nullable;
import net.minecraft.class_1044;
import net.minecraft.class_276;
import net.minecraft.class_279;
import org.apiguardian.api.API;
import org.joml.Matrix4f;
import org.ladysnake.satin.api.managed.ManagedFramebuffer;
import org.ladysnake.satin.api.managed.uniform.SamplerUniform;
import org.ladysnake.satin.api.managed.uniform.SamplerUniformV2;
import org.ladysnake.satin.api.managed.uniform.UniformFinder;

/*
 * Duplicate member names - consider using --renamedupmembers true
 */
public interface ManagedShaderEffect
extends UniformFinder {
    @Nullable
    @API(status=API.Status.MAINTAINED, since="1.0.0")
    public class_279 getShaderEffect();

    @API(status=API.Status.MAINTAINED, since="1.0.0")
    public void initialize() throws IOException;

    @API(status=API.Status.MAINTAINED, since="1.0.0")
    public boolean isInitialized();

    @API(status=API.Status.MAINTAINED, since="1.0.0")
    public boolean isErrored();

    @API(status=API.Status.EXPERIMENTAL, since="1.0.0")
    public void release();

    @API(status=API.Status.STABLE, since="1.0.0")
    public void render(float var1);

    @API(status=API.Status.EXPERIMENTAL, since="1.4.0")
    public ManagedFramebuffer getTarget(String var1);

    @API(status=API.Status.EXPERIMENTAL, since="1.0.0")
    public void setupDynamicUniforms(Runnable var1);

    @API(status=API.Status.EXPERIMENTAL, since="1.0.0")
    public void setupDynamicUniforms(int var1, Runnable var2);

    @API(status=API.Status.STABLE, since="1.0.0")
    public void setUniformValue(String var1, int var2);

    @API(status=API.Status.STABLE, since="1.0.0")
    public void setUniformValue(String var1, int var2, int var3);

    @API(status=API.Status.STABLE, since="1.0.0")
    public void setUniformValue(String var1, int var2, int var3, int var4);

    @API(status=API.Status.STABLE, since="1.0.0")
    public void setUniformValue(String var1, int var2, int var3, int var4, int var5);

    @API(status=API.Status.STABLE, since="1.0.0")
    public void setUniformValue(String var1, float var2);

    @API(status=API.Status.STABLE, since="1.0.0")
    public void setUniformValue(String var1, float var2, float var3);

    @API(status=API.Status.STABLE, since="1.0.0")
    public void setUniformValue(String var1, float var2, float var3, float var4);

    @API(status=API.Status.STABLE, since="1.0.0")
    public void setUniformValue(String var1, float var2, float var3, float var4, float var5);

    @API(status=API.Status.STABLE, since="1.0.0")
    public void setUniformValue(String var1, Matrix4f var2);

    @API(status=API.Status.STABLE, since="1.0.0")
    public void setSamplerUniform(String var1, class_1044 var2);

    @API(status=API.Status.STABLE, since="1.0.0")
    public void setSamplerUniform(String var1, class_276 var2);

    @API(status=API.Status.STABLE, since="1.0.0")
    public void setSamplerUniform(String var1, int var2);

    @Override
    public SamplerUniformV2 findSampler(String var1);

    @Override
    default public SamplerUniform findSampler(String string) {
        return this.findSampler(string);
    }
}
