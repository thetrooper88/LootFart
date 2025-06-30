package org.ladysnake.satin.api.managed.uniform;

import org.apiguardian.api.API;
import org.ladysnake.satin.api.managed.uniform.SamplerUniform;
import org.ladysnake.satin.api.managed.uniform.Uniform1f;
import org.ladysnake.satin.api.managed.uniform.Uniform1i;
import org.ladysnake.satin.api.managed.uniform.Uniform2f;
import org.ladysnake.satin.api.managed.uniform.Uniform2i;
import org.ladysnake.satin.api.managed.uniform.Uniform3f;
import org.ladysnake.satin.api.managed.uniform.Uniform3i;
import org.ladysnake.satin.api.managed.uniform.Uniform4f;
import org.ladysnake.satin.api.managed.uniform.Uniform4i;
import org.ladysnake.satin.api.managed.uniform.UniformMat4;

public interface UniformFinder {
    @API(status=API.Status.MAINTAINED, since="1.4.0")
    public Uniform1i findUniform1i(String var1);

    @API(status=API.Status.MAINTAINED, since="1.4.0")
    public Uniform2i findUniform2i(String var1);

    @API(status=API.Status.MAINTAINED, since="1.4.0")
    public Uniform3i findUniform3i(String var1);

    @API(status=API.Status.MAINTAINED, since="1.4.0")
    public Uniform4i findUniform4i(String var1);

    @API(status=API.Status.MAINTAINED, since="1.4.0")
    public Uniform1f findUniform1f(String var1);

    @API(status=API.Status.MAINTAINED, since="1.4.0")
    public Uniform2f findUniform2f(String var1);

    @API(status=API.Status.MAINTAINED, since="1.4.0")
    public Uniform3f findUniform3f(String var1);

    @API(status=API.Status.MAINTAINED, since="1.4.0")
    public Uniform4f findUniform4f(String var1);

    @API(status=API.Status.MAINTAINED, since="1.4.0")
    public UniformMat4 findUniformMat4(String var1);

    @API(status=API.Status.EXPERIMENTAL, since="1.4.0")
    public SamplerUniform findSampler(String var1);
}
