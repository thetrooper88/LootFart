package org.ladysnake.satin.api.util;

import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.nio.FloatBuffer;
import java.util.function.IntConsumer;
import net.minecraft.class_284;
import net.minecraft.class_285;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import org.apiguardian.api.API;
import org.ladysnake.satin.Satin;
import org.lwjgl.opengl.GL20;

public final class ShaderPrograms {
    private static final Int2ObjectMap<Object2IntMap<String>> uniformsCache = new Int2ObjectOpenHashMap();

    private ShaderPrograms() {
    }

    @API(status=API.Status.MAINTAINED)
    public static void useShader(int program) {
        if (Satin.areShadersDisabled()) {
            return;
        }
        class_285.method_22094((int)program);
    }

    @API(status=API.Status.EXPERIMENTAL)
    public static void setAttribValue(int program, String attribName, IntConsumer operation) {
        if (Satin.areShadersDisabled() || program == 0) {
            return;
        }
        int attrib = class_284.method_22097((int)program, (CharSequence)attribName);
        if (attrib != -1) {
            operation.accept(attrib);
        }
    }

    @API(status=API.Status.EXPERIMENTAL)
    public static void setUniformValue(int program, String uniformName, IntConsumer operation) {
        if (Satin.areShadersDisabled() || program == 0) {
            return;
        }
        int uniform = ShaderPrograms.getUniformLocation(program, uniformName);
        if (uniform != -1) {
            operation.accept(uniform);
        }
    }

    @API(status=API.Status.STABLE)
    public static void setUniform(int program, String uniformName, int value) {
        if (Satin.areShadersDisabled() || program == 0) {
            return;
        }
        int uniform = ShaderPrograms.getUniformLocation(program, uniformName);
        if (uniform != -1) {
            GL20.glUniform1i((int)uniform, (int)value);
        }
    }

    @API(status=API.Status.EXPERIMENTAL)
    public static void setUniform(int program, String uniformName, float value) {
        if (Satin.areShadersDisabled() || program == 0) {
            return;
        }
        int uniform = ShaderPrograms.getUniformLocation(program, uniformName);
        if (uniform != -1) {
            GL20.glUniform1f((int)uniform, (float)value);
        }
    }

    @API(status=API.Status.EXPERIMENTAL)
    public static void setUniform(int program, String uniformName, FloatBuffer mat4) {
        if (Satin.areShadersDisabled() || program == 0) {
            return;
        }
        int uniform = ShaderPrograms.getUniformLocation(program, uniformName);
        if (uniform != -1) {
            GL20.glUniformMatrix4fv((int)uniform, (boolean)false, (FloatBuffer)mat4);
        }
    }

    @API(status=API.Status.MAINTAINED)
    public static int getUniformLocation(int program, String uniformName) {
        int uniform;
        Object2IntMap shaderUniformsCache = (Object2IntMap)uniformsCache.get(program);
        if (shaderUniformsCache == null) {
            shaderUniformsCache = new Object2IntOpenHashMap();
            uniformsCache.put(program, (Object)shaderUniformsCache);
        }
        if (shaderUniformsCache.containsKey((Object)uniformName)) {
            uniform = shaderUniformsCache.getInt((Object)uniformName);
        } else {
            uniform = class_284.method_22096((int)program, (CharSequence)uniformName);
            shaderUniformsCache.put((Object)uniformName, uniform);
        }
        return uniform;
    }

    @API(status=API.Status.EXPERIMENTAL)
    public static void bindAdditionalTextures(int program, class_2960 ... textures) {
        for (int i = 0; i < textures.length; ++i) {
            class_2960 texture = textures[i];
            RenderSystem.activeTexture((int)(i + 33986));
            class_310.method_1551().method_1531().method_22813(texture);
            ShaderPrograms.setUniform(program, "texture" + (i + 1), i + 2);
        }
        RenderSystem.activeTexture((int)33984);
    }
}
