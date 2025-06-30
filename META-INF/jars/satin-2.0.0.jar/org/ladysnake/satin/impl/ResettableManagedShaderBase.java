package org.ladysnake.satin.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import javax.annotation.CheckForNull;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_5912;
import org.apiguardian.api.API;
import org.ladysnake.satin.Satin;
import org.ladysnake.satin.api.managed.uniform.Uniform1f;
import org.ladysnake.satin.api.managed.uniform.Uniform1i;
import org.ladysnake.satin.api.managed.uniform.Uniform2f;
import org.ladysnake.satin.api.managed.uniform.Uniform2i;
import org.ladysnake.satin.api.managed.uniform.Uniform3f;
import org.ladysnake.satin.api.managed.uniform.Uniform3i;
import org.ladysnake.satin.api.managed.uniform.Uniform4f;
import org.ladysnake.satin.api.managed.uniform.Uniform4i;
import org.ladysnake.satin.api.managed.uniform.UniformFinder;
import org.ladysnake.satin.api.managed.uniform.UniformMat4;
import org.ladysnake.satin.impl.ManagedUniform;
import org.ladysnake.satin.impl.ManagedUniformBase;

public abstract class ResettableManagedShaderBase<S extends AutoCloseable>
implements UniformFinder {
    private final class_2960 location;
    private final Map<String, ManagedUniform> managedUniforms = new HashMap();
    private final List<ManagedUniformBase> allUniforms = new ArrayList();
    private boolean errored;
    @CheckForNull
    protected S shader;

    public ResettableManagedShaderBase(class_2960 location) {
        this.location = location;
    }

    @API(status=API.Status.INTERNAL)
    public void initializeOrLog(class_5912 mgr) {
        try {
            this.initialize(mgr);
        }
        catch (IOException e) {
            this.errored = true;
            this.logInitError(e);
        }
    }

    protected abstract void logInitError(IOException var1);

    protected void initialize(class_5912 resourceManager) throws IOException {
        this.release();
        class_310 mc = class_310.method_1551();
        this.shader = this.parseShader(resourceManager, mc, this.location);
        this.setup(mc.method_22683().method_4489(), mc.method_22683().method_4506());
    }

    protected abstract S parseShader(class_5912 var1, class_310 var2, class_2960 var3) throws IOException;

    public void release() {
        if (this.isInitialized()) {
            try {
                assert (this.shader != null);
                this.shader.close();
                this.shader = null;
            }
            catch (Exception e) {
                throw new RuntimeException("Failed to release shader " + String.valueOf((Object)this.location), (Throwable)e);
            }
        }
        this.errored = false;
    }

    protected Collection<ManagedUniformBase> getManagedUniforms() {
        return this.allUniforms;
    }

    protected abstract boolean setupUniform(ManagedUniformBase var1, S var2);

    public boolean isInitialized() {
        return this.shader != null;
    }

    public boolean isErrored() {
        return this.errored;
    }

    public class_2960 getLocation() {
        return this.location;
    }

    protected <U extends ManagedUniformBase> U manageUniform(Map<String, U> uniformMap, Function<String, U> factory, String uniformName, String uniformKind) {
        boolean found;
        ManagedUniformBase existing = (ManagedUniformBase)uniformMap.get((Object)uniformName);
        if (existing != null) {
            return (U)existing;
        }
        ManagedUniformBase ret = (ManagedUniformBase)factory.apply((Object)uniformName);
        if (this.shader != null && !(found = this.setupUniform(ret, this.shader))) {
            Satin.LOGGER.warn("No {} found with name {} in shader {}", (Object)uniformKind, (Object)uniformName, (Object)this.location);
        }
        uniformMap.put((Object)uniformName, (Object)ret);
        this.allUniforms.add((Object)ret);
        return (U)ret;
    }

    @Override
    public Uniform1i findUniform1i(String uniformName) {
        return this.manageUniform(this.managedUniforms, name -> new ManagedUniform((String)name, 1), uniformName, "uniform");
    }

    @Override
    public Uniform2i findUniform2i(String uniformName) {
        return this.manageUniform(this.managedUniforms, name -> new ManagedUniform((String)name, 2), uniformName, "uniform");
    }

    @Override
    public Uniform3i findUniform3i(String uniformName) {
        return this.manageUniform(this.managedUniforms, name -> new ManagedUniform((String)name, 3), uniformName, "uniform");
    }

    @Override
    public Uniform4i findUniform4i(String uniformName) {
        return this.manageUniform(this.managedUniforms, name -> new ManagedUniform((String)name, 4), uniformName, "uniform");
    }

    @Override
    public Uniform1f findUniform1f(String uniformName) {
        return this.manageUniform(this.managedUniforms, name -> new ManagedUniform((String)name, 1), uniformName, "uniform");
    }

    @Override
    public Uniform2f findUniform2f(String uniformName) {
        return this.manageUniform(this.managedUniforms, name -> new ManagedUniform((String)name, 2), uniformName, "uniform");
    }

    @Override
    public Uniform3f findUniform3f(String uniformName) {
        return this.manageUniform(this.managedUniforms, name -> new ManagedUniform((String)name, 3), uniformName, "uniform");
    }

    @Override
    public Uniform4f findUniform4f(String uniformName) {
        return this.manageUniform(this.managedUniforms, name -> new ManagedUniform((String)name, 4), uniformName, "uniform");
    }

    @Override
    public UniformMat4 findUniformMat4(String uniformName) {
        return this.manageUniform(this.managedUniforms, name -> new ManagedUniform((String)name, 16), uniformName, "uniform");
    }

    @API(status=API.Status.INTERNAL)
    public abstract void setup(int var1, int var2);

    public String toString() {
        return "%s[%s]".formatted(new Object[]{this.getClass().getSimpleName(), this.location});
    }
}
