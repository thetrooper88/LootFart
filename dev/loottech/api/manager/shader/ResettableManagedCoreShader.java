package dev.loottech.api.manager.shader;

import com.google.common.base.Preconditions;
import com.mojang.logging.LogUtils;
import dev.loottech.api.manager.shader.ManagedCoreShader;
import dev.loottech.api.manager.shader.ManagedSamplerUniformV1;
import dev.loottech.api.manager.shader.ManagedUniformBase;
import dev.loottech.api.manager.shader.ResettableManagedShaderBase;
import dev.loottech.api.manager.shader.uniform.SamplerUniform;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import net.fabricmc.fabric.impl.client.rendering.FabricShaderProgram;
import net.minecraft.class_293;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_5912;
import net.minecraft.class_5944;

/*
 * Duplicate member names - consider using --renamedupmembers true
 */
public final class ResettableManagedCoreShader
extends ResettableManagedShaderBase<class_5944>
implements ManagedCoreShader {
    private final Consumer<ManagedCoreShader> initCallback;
    private final class_293 vertexFormat;
    private final Map<String, ManagedSamplerUniformV1> managedSamplers = new HashMap();

    public ResettableManagedCoreShader(class_2960 location, class_293 vertexFormat, Consumer<ManagedCoreShader> initCallback) {
        super(location);
        this.vertexFormat = vertexFormat;
        this.initCallback = initCallback;
    }

    @Override
    protected class_5944 parseShader(class_5912 resourceManager, class_310 mc, class_2960 location) throws IOException {
        return new FabricShaderProgram(resourceManager, this.getLocation(), this.vertexFormat);
    }

    @Override
    public void setup(int newWidth, int newHeight) {
        Preconditions.checkNotNull((Object)((class_5944)this.shader));
        for (ManagedUniformBase uniform : this.getManagedUniforms()) {
            this.setupUniform(uniform, (class_5944)this.shader);
        }
        this.initCallback.accept((Object)this);
    }

    @Override
    public class_5944 getProgram() {
        return (class_5944)this.shader;
    }

    @Override
    protected boolean setupUniform(ManagedUniformBase uniform, class_5944 shader) {
        return uniform.findUniformTarget(shader);
    }

    @Override
    public SamplerUniform findSampler(String samplerName) {
        return this.manageUniform(this.managedSamplers, ManagedSamplerUniformV1::new, samplerName, "sampler");
    }

    @Override
    protected void logInitError(IOException e) {
        LogUtils.getLogger().error("Could not create shader program {}", (Object)this.getLocation(), (Object)e);
    }

    @Override
    protected boolean setupUniform(ManagedUniformBase managedUniformBase, AutoCloseable autoCloseable) {
        return this.setupUniform(managedUniformBase, (class_5944)autoCloseable);
    }

    @Override
    protected AutoCloseable parseShader(class_5912 class_59122, class_310 class_3102, class_2960 class_29602) throws IOException {
        return this.parseShader(class_59122, class_3102, class_29602);
    }
}
