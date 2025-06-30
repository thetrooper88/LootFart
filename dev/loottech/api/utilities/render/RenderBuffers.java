package dev.loottech.api.utilities.render;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_243;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_4587;
import net.minecraft.class_5253;
import net.minecraft.class_757;
import net.minecraft.class_9801;
import org.joml.Matrix3f;
import org.joml.Matrix4d;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4d;
import org.lwjgl.opengl.GL11;

public class RenderBuffers {
    public static final Buffer QUADS = new Buffer(class_293.class_5596.field_27382, class_290.field_1576);
    public static final Buffer TEXTURE_QUADS = new Buffer(class_293.class_5596.field_27382, class_290.field_1585);
    public static final Buffer LINES = new Buffer(class_293.class_5596.field_27377, class_290.field_29337);
    private static final List<Runnable> postRenderCallbacks = new ArrayList();
    private static boolean isSetup = false;

    public static void preRender() {
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc((int)770, (int)771);
        RenderSystem.disableDepthTest();
        isSetup = true;
    }

    public static void postRender() {
        isSetup = false;
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
        RenderSystem.enableCull();
        GL11.glDisable((int)2848);
        for (Runnable callback : postRenderCallbacks) {
            callback.run();
        }
        postRenderCallbacks.clear();
    }

    public static void post(Runnable callback) {
        if (isSetup) {
            postRenderCallbacks.add((Object)callback);
        } else {
            callback.run();
        }
    }

    private static Matrix4d toMatrix4d(Matrix4f matrix4f) {
        return new Matrix4d((double)matrix4f.m00(), (double)matrix4f.m01(), (double)matrix4f.m02(), (double)matrix4f.m03(), (double)matrix4f.m10(), (double)matrix4f.m11(), (double)matrix4f.m12(), (double)matrix4f.m13(), (double)matrix4f.m20(), (double)matrix4f.m21(), (double)matrix4f.m22(), (double)matrix4f.m23(), (double)matrix4f.m30(), (double)matrix4f.m31(), (double)matrix4f.m32(), (double)matrix4f.m33());
    }

    public static class Buffer {
        private final class_293.class_5596 drawMode;
        private final class_293 vertexFormat;
        public class_287 buffer;
        private Matrix4d positionMatrix;
        private Matrix3f normalMatrix;
        private int color;

        public Buffer(class_293.class_5596 drawMode, class_293 vertexFormat) {
            this.drawMode = drawMode;
            this.vertexFormat = vertexFormat;
            this.color = -1;
        }

        public void begin(class_4587 stack) {
            this.updateMatrices(stack);
            this.buffer = class_289.method_1348().method_60827(this.drawMode, this.vertexFormat);
        }

        public void updateMatrices(class_4587 stack) {
            this.positionMatrix = RenderBuffers.toMatrix4d(stack.method_23760().method_23761());
            this.normalMatrix = stack.method_23760().method_23762();
            class_243 pos = class_310.method_1551().method_31975().field_4344.method_19326();
            this.positionMatrix.translate(-pos.field_1352, -pos.field_1351, -pos.field_1350);
        }

        public void end() {
            this.draw();
        }

        public Buffer vertex(double x, double y, double z) {
            if (this.buffer == null) {
                return this;
            }
            Vector4d vector4d = this.positionMatrix.transform(new Vector4d(x, y, z, 1.0));
            this.buffer.method_22912((float)vector4d.x(), (float)vector4d.y(), (float)vector4d.z()).method_1336(class_5253.class_5254.method_27765((int)this.color), class_5253.class_5254.method_27766((int)this.color), class_5253.class_5254.method_27767((int)this.color), class_5253.class_5254.method_27762((int)this.color));
            return this;
        }

        public Buffer vertexTex(double x, double y, double z, float u, float v) {
            if (this.buffer == null) {
                return this;
            }
            Vector4d vector4d = this.positionMatrix.transform(new Vector4d(x, y, z, 1.0));
            this.buffer.method_22912((float)vector4d.x(), (float)vector4d.y(), (float)vector4d.z()).method_22913(u, v).method_1336(class_5253.class_5254.method_27765((int)this.color), class_5253.class_5254.method_27766((int)this.color), class_5253.class_5254.method_27767((int)this.color), class_5253.class_5254.method_27762((int)this.color));
            return this;
        }

        public Buffer vertexLine(double x1, double y1, double z1, double x2, double y2, double z2) {
            if (this.buffer == null) {
                return this;
            }
            float k = (float)(x2 - x1);
            float l = (float)(y2 - y1);
            float m = (float)(z2 - z1);
            float n = class_3532.method_15355((float)(k * k + l * l + m * m));
            Vector3f vector3f = this.normalMatrix.transform(k /= n, l /= n, m /= n, new Vector3f()).normalize();
            Vector4d vector4d = this.positionMatrix.transform(new Vector4d(x1, y1, z1, 1.0));
            this.buffer.method_22912((float)vector4d.x(), (float)vector4d.y(), (float)vector4d.z()).method_22914(vector3f.x, vector3f.y, vector3f.z).method_1336(class_5253.class_5254.method_27765((int)this.color), class_5253.class_5254.method_27766((int)this.color), class_5253.class_5254.method_27767((int)this.color), class_5253.class_5254.method_27762((int)this.color));
            Vector4d vector4d2 = this.positionMatrix.transform(new Vector4d(x2, y2, z2, 1.0));
            this.buffer.method_22912((float)vector4d2.x(), (float)vector4d2.y(), (float)vector4d2.z()).method_22914(vector3f.x, vector3f.y, vector3f.z).method_1336(class_5253.class_5254.method_27765((int)this.color), class_5253.class_5254.method_27766((int)this.color), class_5253.class_5254.method_27767((int)this.color), class_5253.class_5254.method_27762((int)this.color));
            return this;
        }

        public void color(int color) {
            this.color = color;
        }

        public void draw() {
            if (this.buffer == null) {
                return;
            }
            if (this.vertexFormat == class_290.field_29337) {
                RenderSystem.setShader(class_757::method_34535);
            } else if (this.vertexFormat == class_290.field_1576) {
                RenderSystem.setShader(class_757::method_34540);
            } else if (this.vertexFormat == class_290.field_1585) {
                RenderSystem.setShader(class_757::method_34542);
            }
            class_9801 builtBuffer = this.buffer.method_60794();
            if (builtBuffer != null) {
                class_286.method_43433((class_9801)builtBuffer);
                this.buffer = null;
            }
        }
    }
}
