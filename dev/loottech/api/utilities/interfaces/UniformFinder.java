package dev.loottech.api.utilities.interfaces;

import dev.loottech.api.manager.shader.uniform.SamplerUniform;
import dev.loottech.api.manager.shader.uniform.Uniform1f;
import dev.loottech.api.manager.shader.uniform.Uniform1i;
import dev.loottech.api.manager.shader.uniform.Uniform2f;
import dev.loottech.api.manager.shader.uniform.Uniform2i;
import dev.loottech.api.manager.shader.uniform.Uniform3f;
import dev.loottech.api.manager.shader.uniform.Uniform3i;
import dev.loottech.api.manager.shader.uniform.Uniform4f;
import dev.loottech.api.manager.shader.uniform.Uniform4i;
import dev.loottech.api.manager.shader.uniform.UniformMat4;

public interface UniformFinder {
    public Uniform1i findUniform1i(String var1);

    public Uniform2i findUniform2i(String var1);

    public Uniform3i findUniform3i(String var1);

    public Uniform4i findUniform4i(String var1);

    public Uniform1f findUniform1f(String var1);

    public Uniform2f findUniform2f(String var1);

    public Uniform3f findUniform3f(String var1);

    public Uniform4f findUniform4f(String var1);

    public UniformMat4 findUniformMat4(String var1);

    public SamplerUniform findSampler(String var1);
}
