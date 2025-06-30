package org.ladysnake.satin.impl;

import com.mojang.blaze3d.platform.GlStateManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import javax.annotation.Nullable;
import net.minecraft.class_2960;
import net.minecraft.class_3300;
import org.ladysnake.satin.Satin;
import org.ladysnake.satin.api.util.ShaderLinkException;
import org.ladysnake.satin.api.util.ShaderLoader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL20;

public final class ValidatingShaderLoader
implements ShaderLoader {
    public static final ShaderLoader INSTANCE = new ValidatingShaderLoader();

    @Override
    public int loadShader(class_3300 resourceManager, @Nullable class_2960 vertexLocation, @Nullable class_2960 fragmentLocation) throws IOException {
        String log;
        int programId = GlStateManager.glCreateProgram();
        int vertexShaderId = 0;
        int fragmentShaderId = 0;
        if (vertexLocation != null) {
            vertexShaderId = GlStateManager.glCreateShader((int)35633);
            ARBShaderObjects.glShaderSourceARB((int)vertexShaderId, (CharSequence)this.fromFile(resourceManager, vertexLocation));
            ARBShaderObjects.glCompileShaderARB((int)vertexShaderId);
            ARBShaderObjects.glAttachObjectARB((int)programId, (int)vertexShaderId);
            log = GL20.glGetShaderInfoLog((int)vertexShaderId, (int)1024);
            if (!log.isEmpty()) {
                Satin.LOGGER.error("Could not compile vertex shader {}: {}", (Object)vertexLocation, (Object)log);
            }
        }
        if (fragmentLocation != null) {
            fragmentShaderId = GlStateManager.glCreateShader((int)35632);
            ARBShaderObjects.glShaderSourceARB((int)fragmentShaderId, (CharSequence)this.fromFile(resourceManager, fragmentLocation));
            ARBShaderObjects.glCompileShaderARB((int)fragmentShaderId);
            ARBShaderObjects.glAttachObjectARB((int)programId, (int)fragmentShaderId);
            log = GL20.glGetShaderInfoLog((int)fragmentShaderId, (int)1024);
            if (!log.isEmpty()) {
                Satin.LOGGER.error("Could not compile fragment shader {}: {}", (Object)fragmentLocation, (Object)log);
            }
        }
        GlStateManager.glLinkProgram((int)programId);
        if (GL20.glGetProgrami((int)programId, (int)35714) == 0) {
            throw new ShaderLinkException("Error linking Shader code: " + GL20.glGetProgramInfoLog((int)programId, (int)1024));
        }
        if (vertexShaderId != 0) {
            GL20.glDetachShader((int)programId, (int)vertexShaderId);
            GL20.glDeleteShader((int)vertexShaderId);
        }
        if (fragmentShaderId != 0) {
            GL20.glDetachShader((int)programId, (int)fragmentShaderId);
            GL20.glDeleteShader((int)fragmentShaderId);
        }
        GL20.glValidateProgram((int)programId);
        if (GL20.glGetProgrami((int)programId, (int)35715) == 0) {
            Satin.LOGGER.warn("Warning validating Shader code: {}", (Object)GL20.glGetProgramInfoLog((int)programId, (int)1024));
        }
        return programId;
    }

    private String fromFile(class_3300 resourceManager, class_2960 fileLocation) throws IOException {
        StringBuilder source = new StringBuilder();
        try (InputStream in = resourceManager.getResourceOrThrow(fileLocation).method_14482();
             BufferedReader reader = new BufferedReader((Reader)new InputStreamReader(in, StandardCharsets.UTF_8));){
            String line;
            while ((line = reader.readLine()) != null) {
                source.append(line).append('\n');
            }
        }
        return source.toString();
    }
}
