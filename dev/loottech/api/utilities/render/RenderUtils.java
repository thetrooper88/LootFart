package dev.loottech.api.utilities.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.loottech.api.utilities.Util;
import dev.loottech.api.utilities.render.RenderBuffers;
import dev.loottech.api.utilities.render.RenderManager;
import dev.loottech.asm.mixins.accessor.AccessorWorldRenderer;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.class_1297;
import net.minecraft.class_1799;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_3532;
import net.minecraft.class_4184;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_4604;
import net.minecraft.class_5253;
import net.minecraft.class_757;
import net.minecraft.class_761;
import net.minecraft.class_7833;
import net.minecraft.class_9801;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Matrix4d;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3f;
import org.joml.Vector4d;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

public class RenderUtils
implements Util {
    public static class_4604 camera = new class_4604(new Matrix4f(), new Matrix4f());
    private static boolean isSetup = false;
    public static final Buffer QUADS = new Buffer(class_293.class_5596.field_27382, class_290.field_1576);
    private static final List<Runnable> postRenderCallbacks = new ArrayList();

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

    private static Matrix4d toMatrix4d(Matrix4f matrix4f) {
        return new Matrix4d((double)matrix4f.m00(), (double)matrix4f.m01(), (double)matrix4f.m02(), (double)matrix4f.m03(), (double)matrix4f.m10(), (double)matrix4f.m11(), (double)matrix4f.m12(), (double)matrix4f.m13(), (double)matrix4f.m20(), (double)matrix4f.m21(), (double)matrix4f.m22(), (double)matrix4f.m23(), (double)matrix4f.m30(), (double)matrix4f.m31(), (double)matrix4f.m32(), (double)matrix4f.m33());
    }

    @NotNull
    public static class_243 worldSpaceToScreenSpace(@NotNull class_243 pos, class_4587 matrices) {
        class_4184 camera = RenderUtils.mc.method_1561().field_4686;
        int displayHeight = mc.method_22683().method_4507();
        int[] viewport = new int[4];
        GL11.glGetIntegerv((int)2978, (int[])viewport);
        Vector3f target = new Vector3f();
        double deltaX = pos.field_1352 - camera.method_19326().field_1352;
        double deltaY = pos.field_1351 - camera.method_19326().field_1351;
        double deltaZ = pos.field_1350 - camera.method_19326().field_1350;
        Vector4f transformedCoordinates = new Vector4f((float)deltaX, (float)deltaY, (float)deltaZ, 1.0f).mul((Matrix4fc)matrices.method_23760().method_23761());
        Matrix4f matrixProj = matrices.method_23760().method_23761();
        Matrix4f matrixModel = matrices.method_23760().method_23761();
        matrixProj.mul((Matrix4fc)matrixModel).project(transformedCoordinates.x(), transformedCoordinates.y(), transformedCoordinates.z(), viewport, target);
        return new class_243((double)target.x / RenderUtils.getScaleFactor(), (double)((float)displayHeight - target.y) / RenderUtils.getScaleFactor(), (double)target.z);
    }

    public static void renderBox(class_4587 matrices, class_2338 p, int color) {
        RenderUtils.renderBox(matrices, new class_238(p), color);
    }

    public static boolean isFrustumVisible(class_238 box) {
        return ((AccessorWorldRenderer)RenderUtils.mc.field_1769).getFrustum().method_23093(box);
    }

    public static void renderBox(class_4587 matrices, class_238 box, int color) {
        if (!RenderUtils.isFrustumVisible(box)) {
            return;
        }
        matrices.method_22903();
        RenderUtils.drawBox(matrices, box, color);
        matrices.method_22909();
    }

    public static void drawBox(class_4587 matrices, class_238 box, int color) {
        RenderUtils.drawBox(matrices, box.field_1323, box.field_1322, box.field_1321, box.field_1320, box.field_1325, box.field_1324, color);
    }

    public static void drawBox(class_4587 matrices, double x1, double y1, double z1, double x2, double y2, double z2, int color) {
        RenderUtils.preRender();
        QUADS.begin(matrices);
        QUADS.color(color);
        QUADS.vertex(x1, y1, z1).vertex(x2, y1, z1).vertex(x2, y1, z2).vertex(x1, y1, z2);
        QUADS.vertex(x1, y2, z1).vertex(x1, y2, z2).vertex(x2, y2, z2).vertex(x2, y2, z1);
        QUADS.vertex(x1, y1, z1).vertex(x1, y2, z1).vertex(x2, y2, z1).vertex(x2, y1, z1);
        QUADS.vertex(x2, y1, z1).vertex(x2, y2, z1).vertex(x2, y2, z2).vertex(x2, y1, z2);
        QUADS.vertex(x1, y1, z2).vertex(x2, y1, z2).vertex(x2, y2, z2).vertex(x1, y2, z2);
        QUADS.vertex(x1, y1, z1).vertex(x1, y1, z2).vertex(x1, y2, z2).vertex(x1, y2, z1);
        QUADS.end();
        RenderUtils.postRender();
    }

    public static void drawItem(class_332 drawContext, class_1799 itemStack, int x, int y, float scale, boolean overlay, String countOverride) {
        class_4587 matrices = drawContext.method_51448();
        matrices.method_22903();
        int scaledX = (int)((float)x / scale);
        int scaledY = (int)((float)y / scale);
        drawContext.method_51427(itemStack, scaledX, scaledY);
        if (overlay) {
            drawContext.method_51432(RenderUtils.mc.field_1772, itemStack, scaledX, scaledY, countOverride);
        }
        matrices.method_22909();
    }

    public static void drawItem(class_332 drawContext, class_1799 itemStack, int x, int y, float scale, boolean overlay) {
        RenderUtils.drawItem(drawContext, itemStack, x, y, scale, overlay, null);
    }

    public static float getTickDelta() {
        return mc.method_60646().method_60637(true);
    }

    public static class_238 getInterpolatedBoundingBox(class_1297 entity) {
        return RenderUtils.getInterpolatedBoundingBox(entity, RenderUtils.getTickDelta());
    }

    public static class_238 getInterpolatedBoundingBox(class_1297 entity, float tickDelta) {
        class_243 lastPos = new class_243(entity.field_6038, entity.field_5971, entity.field_5989);
        class_243 currentPos = entity.method_19538();
        class_243 interpolatedPos = lastPos.method_35590(currentPos, (double)tickDelta);
        float width = entity.method_17681();
        float height = entity.method_17682();
        return new class_238(interpolatedPos.field_1352 - (double)(width / 2.0f), interpolatedPos.field_1351, interpolatedPos.field_1350 - (double)(width / 2.0f), interpolatedPos.field_1352 + (double)(width / 2.0f), interpolatedPos.field_1351 + (double)height, interpolatedPos.field_1350 + (double)(width / 2.0f));
    }

    public static void setup2DRender(boolean disableDepth) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        if (disableDepth) {
            RenderSystem.disableDepthTest();
        }
    }

    public static void end2DRender() {
        RenderSystem.disableBlend();
        RenderSystem.enableDepthTest();
    }

    public static void bindTexture(class_2960 identifier) {
        RenderSystem.setShaderTexture((int)0, (class_2960)identifier);
    }

    public static void renderQuad(class_4587 matrices, float left, float top, float right, float bottom, Color color, class_243 pos) {
        Matrix4f matrix = matrices.method_23760().method_23761();
        class_287 buffer = RenderSystem.renderThreadTesselator().method_60827(class_293.class_5596.field_27382, class_290.field_1576);
        class_4184 cam = RenderUtils.mc.field_1773.method_19418();
        RenderBuffers.preRender();
        float distance = (float)RenderUtils.mc.method_1561().field_4686.method_19326().method_1022(pos);
        float scaling = distance <= 8.0f ? 0.0245f : 0.0018f + 0.003f * distance;
        matrices.method_22904(pos.field_1352 - cam.method_19326().field_1352, pos.field_1351 - cam.method_19326().field_1351, pos.field_1350 - cam.method_19326().field_1350);
        matrices.method_22907(mc.method_1561().method_24197());
        matrices.method_22905(scaling, -scaling, scaling);
        buffer.method_22918(matrix, left, top, 0.0f).method_39415(color.getRGB());
        buffer.method_22918(matrix, left, bottom, 0.0f).method_39415(color.getRGB());
        buffer.method_22918(matrix, right, bottom, 0.0f).method_39415(color.getRGB());
        buffer.method_22918(matrix, right, top, 0.0f).method_39415(color.getRGB());
        class_286.method_43433((class_9801)buffer.method_60800());
        RenderBuffers.postRender();
    }

    public static void renderOutline(class_4587 matrices, float left, float top, float right, float bottom, Color color, class_243 pos) {
        Matrix4f matrix = matrices.method_23760().method_23761();
        class_287 buffer = RenderSystem.renderThreadTesselator().method_60827(class_293.class_5596.field_27382, class_290.field_1576);
        class_4184 cam = RenderUtils.mc.field_1773.method_19418();
        RenderBuffers.preRender();
        buffer.method_22918(matrix, left, top, 0.0f).method_39415(color.getRGB());
        buffer.method_22918(matrix, left, bottom, 0.0f).method_39415(color.getRGB());
        buffer.method_22918(matrix, left + 0.5f, bottom, 0.0f).method_39415(color.getRGB());
        buffer.method_22918(matrix, left + 0.5f, top, 0.0f).method_39415(color.getRGB());
        buffer.method_22918(matrix, right - 0.5f, top, 0.0f).method_39415(color.getRGB());
        buffer.method_22918(matrix, right - 0.5f, bottom, 0.0f).method_39415(color.getRGB());
        buffer.method_22918(matrix, right, bottom, 0.0f).method_39415(color.getRGB());
        buffer.method_22918(matrix, right, top, 0.0f).method_39415(color.getRGB());
        buffer.method_22918(matrix, left, bottom - 0.5f, 0.0f).method_39415(color.getRGB());
        buffer.method_22918(matrix, left, bottom, 0.0f).method_39415(color.getRGB());
        buffer.method_22918(matrix, right, bottom, 0.0f).method_39415(color.getRGB());
        buffer.method_22918(matrix, right, bottom - 0.5f, 0.0f).method_39415(color.getRGB());
        buffer.method_22918(matrix, left, top, 0.0f).method_39415(color.getRGB());
        buffer.method_22918(matrix, left, top + 0.5f, 0.0f).method_39415(color.getRGB());
        buffer.method_22918(matrix, right, top + 0.5f, 0.0f).method_39415(color.getRGB());
        buffer.method_22918(matrix, right, top, 0.0f).method_39415(color.getRGB());
        class_286.method_43433((class_9801)buffer.method_60800());
        RenderBuffers.postRender();
    }

    public static void rect(class_4587 stack, float x1, float y1, float x2, float y2, int color) {
        RenderUtils.rectFilled(stack, x1, y1, x2, y2, color);
    }

    public static void rect(class_4587 stack, float x1, float y1, float x2, float y2, int color, float width) {
        RenderUtils.drawHorizontalLine(stack, x1, x2, y1, color, width);
        RenderUtils.drawVerticalLine(stack, x2, y1, y2, color, width);
        RenderUtils.drawHorizontalLine(stack, x1, x2, y2, color, width);
        RenderUtils.drawVerticalLine(stack, x1, y1, y2, color, width);
    }

    protected static void drawHorizontalLine(class_4587 matrices, float x1, float x2, float y, int color) {
        if (x2 < x1) {
            float i = x1;
            x1 = x2;
            x2 = i;
        }
        RenderUtils.rectFilled(matrices, x1, y, x2 + 1.0f, y + 1.0f, color);
    }

    protected static void drawVerticalLine(class_4587 matrices, float x, float y1, float y2, int color) {
        if (y2 < y1) {
            float i = y1;
            y1 = y2;
            y2 = i;
        }
        RenderUtils.rectFilled(matrices, x, y1 + 1.0f, x + 1.0f, y2, color);
    }

    protected static void drawHorizontalLine(class_4587 matrices, float x1, float x2, float y, int color, float width) {
        if (x2 < x1) {
            float i = x1;
            x1 = x2;
            x2 = i;
        }
        RenderUtils.rectFilled(matrices, x1, y, x2 + width, y + width, color);
    }

    protected static void drawVerticalLine(class_4587 matrices, float x, float y1, float y2, int color, float width) {
        if (y2 < y1) {
            float i = y1;
            y1 = y2;
            y2 = i;
        }
        RenderUtils.rectFilled(matrices, x, y1 + width, x + width, y2, color);
    }

    public static void rectFilled(class_4587 matrix, float x1, float y1, float x2, float y2, int color) {
        float i;
        if (x1 < x2) {
            i = x1;
            x1 = x2;
            x2 = i;
        }
        if (y1 < y2) {
            i = y1;
            y1 = y2;
            y2 = i;
        }
        float f = (float)(color >> 24 & 0xFF) / 255.0f;
        float g = (float)(color >> 16 & 0xFF) / 255.0f;
        float h = (float)(color >> 8 & 0xFF) / 255.0f;
        float j = (float)(color & 0xFF) / 255.0f;
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(class_757::method_34540);
        class_287 bufferBuilder = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1576);
        bufferBuilder.method_22918(matrix.method_23760().method_23761(), x1, y2, 0.0f).method_22915(g, h, j, f);
        bufferBuilder.method_22918(matrix.method_23760().method_23761(), x2, y2, 0.0f).method_22915(g, h, j, f);
        bufferBuilder.method_22918(matrix.method_23760().method_23761(), x2, y1, 0.0f).method_22915(g, h, j, f);
        bufferBuilder.method_22918(matrix.method_23760().method_23761(), x1, y1, 0.0f).method_22915(g, h, j, f);
        class_286.method_43433((class_9801)bufferBuilder.method_60800());
        RenderSystem.disableBlend();
    }

    public static void drawBoxFilled(class_4587 stack, class_238 box, Color c) {
        float minX = (float)(box.field_1323 - RenderUtils.mc.method_1561().field_4686.method_19326().method_10216());
        float minY = (float)(box.field_1322 - RenderUtils.mc.method_1561().field_4686.method_19326().method_10214());
        float minZ = (float)(box.field_1321 - RenderUtils.mc.method_1561().field_4686.method_19326().method_10215());
        float maxX = (float)(box.field_1320 - RenderUtils.mc.method_1561().field_4686.method_19326().method_10216());
        float maxY = (float)(box.field_1325 - RenderUtils.mc.method_1561().field_4686.method_19326().method_10214());
        float maxZ = (float)(box.field_1324 - RenderUtils.mc.method_1561().field_4686.method_19326().method_10215());
        class_289 tessellator = class_289.method_1348();
        RenderBuffers.preRender();
        class_287 bufferBuilder = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1576);
        bufferBuilder.method_22918(stack.method_23760().method_23761(), minX, minY, minZ).method_39415(c.getRGB());
        bufferBuilder.method_22918(stack.method_23760().method_23761(), maxX, minY, minZ).method_39415(c.getRGB());
        bufferBuilder.method_22918(stack.method_23760().method_23761(), maxX, minY, maxZ).method_39415(c.getRGB());
        bufferBuilder.method_22918(stack.method_23760().method_23761(), minX, minY, maxZ).method_39415(c.getRGB());
        bufferBuilder.method_22918(stack.method_23760().method_23761(), minX, maxY, minZ).method_39415(c.getRGB());
        bufferBuilder.method_22918(stack.method_23760().method_23761(), minX, maxY, maxZ).method_39415(c.getRGB());
        bufferBuilder.method_22918(stack.method_23760().method_23761(), maxX, maxY, maxZ).method_39415(c.getRGB());
        bufferBuilder.method_22918(stack.method_23760().method_23761(), maxX, maxY, minZ).method_39415(c.getRGB());
        bufferBuilder.method_22918(stack.method_23760().method_23761(), minX, minY, minZ).method_39415(c.getRGB());
        bufferBuilder.method_22918(stack.method_23760().method_23761(), minX, maxY, minZ).method_39415(c.getRGB());
        bufferBuilder.method_22918(stack.method_23760().method_23761(), maxX, maxY, minZ).method_39415(c.getRGB());
        bufferBuilder.method_22918(stack.method_23760().method_23761(), maxX, minY, minZ).method_39415(c.getRGB());
        bufferBuilder.method_22918(stack.method_23760().method_23761(), maxX, minY, minZ).method_39415(c.getRGB());
        bufferBuilder.method_22918(stack.method_23760().method_23761(), maxX, maxY, minZ).method_39415(c.getRGB());
        bufferBuilder.method_22918(stack.method_23760().method_23761(), maxX, maxY, maxZ).method_39415(c.getRGB());
        bufferBuilder.method_22918(stack.method_23760().method_23761(), maxX, minY, maxZ).method_39415(c.getRGB());
        bufferBuilder.method_22918(stack.method_23760().method_23761(), minX, minY, maxZ).method_39415(c.getRGB());
        bufferBuilder.method_22918(stack.method_23760().method_23761(), maxX, minY, maxZ).method_39415(c.getRGB());
        bufferBuilder.method_22918(stack.method_23760().method_23761(), maxX, maxY, maxZ).method_39415(c.getRGB());
        bufferBuilder.method_22918(stack.method_23760().method_23761(), minX, maxY, maxZ).method_39415(c.getRGB());
        bufferBuilder.method_22918(stack.method_23760().method_23761(), minX, minY, minZ).method_39415(c.getRGB());
        bufferBuilder.method_22918(stack.method_23760().method_23761(), minX, minY, maxZ).method_39415(c.getRGB());
        bufferBuilder.method_22918(stack.method_23760().method_23761(), minX, maxY, maxZ).method_39415(c.getRGB());
        bufferBuilder.method_22918(stack.method_23760().method_23761(), minX, maxY, minZ).method_39415(c.getRGB());
        class_286.method_43433((class_9801)bufferBuilder.method_60800());
        RenderBuffers.postRender();
    }

    public static void drawBoxFilled(class_4587 stack, class_243 vec, Color c) {
        RenderUtils.drawBoxFilled(stack, class_238.method_29968((class_243)vec), c);
    }

    public static void drawBoxFilled(class_4587 stack, class_2338 bp, Color c) {
        RenderUtils.drawBoxFilled(stack, new class_238(bp), c);
    }

    public static void drawBox(class_4587 stack, double x1, double y1, double z1, double x2, double y2, double z2, float lw, Color c) {
        RenderUtils.drawBox(stack, new class_238(x1, y1, z1, x2, y2, z2), c, (double)lw);
    }

    public static void drawBox(class_4587 stack, class_238 box, Color c, double lineWidth) {
        float minX = (float)(box.field_1323 - RenderUtils.mc.method_1561().field_4686.method_19326().method_10216());
        float minY = (float)(box.field_1322 - RenderUtils.mc.method_1561().field_4686.method_19326().method_10214());
        float minZ = (float)(box.field_1321 - RenderUtils.mc.method_1561().field_4686.method_19326().method_10215());
        float maxX = (float)(box.field_1320 - RenderUtils.mc.method_1561().field_4686.method_19326().method_10216());
        float maxY = (float)(box.field_1325 - RenderUtils.mc.method_1561().field_4686.method_19326().method_10214());
        float maxZ = (float)(box.field_1324 - RenderUtils.mc.method_1561().field_4686.method_19326().method_10215());
        RenderBuffers.preRender();
        RenderSystem.lineWidth((float)((float)lineWidth));
        RenderSystem.setShader(class_757::method_34535);
        RenderSystem.defaultBlendFunc();
        class_287 bufferBuilder = class_289.method_1348().method_60827(class_293.class_5596.field_27377, class_290.field_29337);
        class_761.method_22980((class_4587)stack, (class_4588)bufferBuilder, (double)minX, (double)minY, (double)minZ, (double)maxX, (double)maxY, (double)maxZ, (float)((float)c.getRed() / 255.0f), (float)((float)c.getGreen() / 255.0f), (float)((float)c.getBlue() / 255.0f), (float)((float)c.getAlpha() / 255.0f));
        class_286.method_43433((class_9801)bufferBuilder.method_60800());
        RenderBuffers.postRender();
    }

    public static void drawBoxRaw(class_4587 stack, class_238 box, Color c, float lineWidth) {
        RenderBuffers.preRender();
        RenderSystem.lineWidth((float)lineWidth);
        RenderSystem.setShader(class_757::method_34535);
        RenderSystem.defaultBlendFunc();
        class_287 bufferBuilder = class_289.method_1348().method_60827(class_293.class_5596.field_27377, class_290.field_29337);
        class_761.method_22980((class_4587)stack, (class_4588)bufferBuilder, (double)((float)box.field_1323), (double)((float)box.field_1322), (double)((float)box.field_1321), (double)((float)box.field_1320), (double)((float)box.field_1325), (double)((float)box.field_1324), (float)((float)c.getRed() / 255.0f), (float)((float)c.getGreen() / 255.0f), (float)((float)c.getBlue() / 255.0f), (float)((float)c.getAlpha() / 255.0f));
        class_286.method_43433((class_9801)bufferBuilder.method_60800());
        RenderBuffers.postRender();
    }

    public static void drawBox(class_4587 stack, class_243 vec, Color c, double lineWidth) {
        RenderUtils.drawBox(stack, class_238.method_29968((class_243)vec), c, lineWidth);
    }

    public static void drawBox(class_4587 stack, class_2338 bp, Color c, double lineWidth) {
        RenderUtils.drawBox(stack, new class_238(bp), c, lineWidth);
    }

    public static class_4587 matrixFrom(class_243 pos) {
        class_4587 matrices = new class_4587();
        class_4184 camera = RenderUtils.mc.field_1773.method_19418();
        matrices.method_22907(class_7833.field_40714.rotationDegrees(camera.method_19329()));
        matrices.method_22907(class_7833.field_40716.rotationDegrees(camera.method_19330() + 180.0f));
        matrices.method_22904(pos.method_10216() - camera.method_19326().field_1352, pos.method_10214() - camera.method_19326().field_1351, pos.method_10215() - camera.method_19326().field_1350);
        return matrices;
    }

    public static void setupRender() {
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate((GlStateManager.class_4535)GlStateManager.class_4535.SRC_ALPHA, (GlStateManager.class_4534)GlStateManager.class_4534.ONE_MINUS_SRC_ALPHA, (GlStateManager.class_4535)GlStateManager.class_4535.ONE, (GlStateManager.class_4534)GlStateManager.class_4534.ZERO);
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask((boolean)false);
    }

    public static void endRender() {
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
        RenderSystem.depthMask((boolean)true);
    }

    public static double getScaleFactor() {
        return mc.method_22683().method_4495();
    }

    public static void drawTextIn3D(class_4587 matrices, float x, float y, String text, class_243 pos, Color fillColor, Boolean shadow, float scale) {
        RenderManager.renderSign(text, pos, x, y, scale / 41.0f, fillColor.getRGB());
    }

    public static void drawRect(class_4587 matrices, float x, float y, float width, float height, Color color) {
        class_289 tessellator = class_289.method_1348();
        class_287 bufferBuilder = tessellator.method_60827(class_293.class_5596.field_27382, class_290.field_1576);
        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(class_757::method_34540);
        matrices.method_22903();
        bufferBuilder.method_22912(x, height, 0.0f).method_1336(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        bufferBuilder.method_22912(width, height, 0.0f).method_1336(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        bufferBuilder.method_22912(width, y, 0.0f).method_1336(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        bufferBuilder.method_22912(x, y, 0.0f).method_1336(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        matrices.method_22909();
        class_286.method_43433((class_9801)bufferBuilder.method_60800());
        RenderSystem.disableBlend();
        RenderSystem.enableCull();
    }

    public static void drawOutline(class_4587 matrices, float x, float y, float width, float height, float lineWidth, Color color) {
        RenderUtils.drawRect(matrices, x + lineWidth, y, x - lineWidth, y + lineWidth, color);
        RenderUtils.drawRect(matrices, x + lineWidth, y, width - lineWidth, y + lineWidth, color);
        RenderUtils.drawRect(matrices, x, y, x + lineWidth, height, color);
        RenderUtils.drawRect(matrices, width - lineWidth, y, width, height, color);
        RenderUtils.drawRect(matrices, x + lineWidth, height - lineWidth, width - lineWidth, height, color);
    }

    public static void drawSidewaysGradient(class_4587 matrices, float x, float y, float width, float height, Color startColor, Color endColor) {
        RenderBuffers.preRender();
        class_289 tessellator = class_289.method_1348();
        class_287 bufferBuilder = tessellator.method_60827(class_293.class_5596.field_27382, class_290.field_1576);
        Matrix4f matrix = matrices.method_23760().method_23761();
        RenderUtils.drawSidewaysPart(bufferBuilder, matrix, x, y, width, height, startColor, endColor);
        class_286.method_43433((class_9801)bufferBuilder.method_60800());
        RenderBuffers.postRender();
    }

    private static void drawSidewaysPart(class_287 bufferBuilder, Matrix4f matrix, float x, float y, float width, float height, Color startColor, Color endColor) {
        bufferBuilder.method_22918(matrix, x, y, 0.0f).method_22915((float)startColor.getRed() / 255.0f, (float)startColor.getGreen() / 255.0f, (float)startColor.getBlue() / 255.0f, (float)startColor.getAlpha() / 255.0f);
        bufferBuilder.method_22918(matrix, x + width, y, 0.0f).method_22915((float)endColor.getRed() / 255.0f, (float)endColor.getGreen() / 255.0f, (float)endColor.getBlue() / 255.0f, (float)endColor.getAlpha() / 255.0f);
        bufferBuilder.method_22918(matrix, x + width, y + height, 0.0f).method_22915((float)endColor.getRed() / 255.0f, (float)endColor.getGreen() / 255.0f, (float)endColor.getBlue() / 255.0f, (float)endColor.getAlpha() / 255.0f);
        bufferBuilder.method_22918(matrix, x, y + height, 0.0f).method_22915((float)startColor.getRed() / 255.0f, (float)startColor.getGreen() / 255.0f, (float)startColor.getBlue() / 255.0f, (float)startColor.getAlpha() / 255.0f);
    }

    public static void drawCircle(float x, float y, float radius, Color color) {
        RenderSystem.enableBlend();
        RenderSystem.disableDepthTest();
        RenderSystem.blendFunc((int)770, (int)771);
        class_289 tessellator = class_289.method_1348();
        class_287 bufferBuilder = tessellator.method_60827(class_293.class_5596.field_27382, class_290.field_1592);
        RenderSystem.setShader(class_757::method_34539);
        RenderSystem.setShaderColor((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), (float)((float)color.getAlpha() / 255.0f));
        double degree = Math.PI / 180;
        for (double i = 0.0; i <= 90.0; i += 1.0) {
            bufferBuilder.method_22912((float)((double)x + Math.sin((double)(i * degree)) * (double)radius), (float)((double)y + Math.cos((double)(i * degree)) * (double)radius), 0.0f);
            bufferBuilder.method_22912((float)((double)x + Math.sin((double)(i * degree)) * (double)radius), (float)((double)y - Math.cos((double)(i * degree)) * (double)radius), 0.0f);
            bufferBuilder.method_22912((float)((double)x - Math.sin((double)(i * degree)) * (double)radius), (float)((double)y - Math.cos((double)(i * degree)) * (double)radius), 0.0f);
            bufferBuilder.method_22912((float)((double)x - Math.sin((double)(i * degree)) * (double)radius), (float)((double)y + Math.cos((double)(i * degree)) * (double)radius), 0.0f);
        }
        class_286.method_43433((class_9801)bufferBuilder.method_60800());
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }

    public static void drawBlock(class_2338 position, Color color) {
        RenderUtils.drawBlock(new class_238(position), color);
    }

    public static void drawBlock(class_238 bb, Color color) {
        camera.method_23088(((class_1297)Objects.requireNonNull((Object)mc.method_1560())).method_23317(), mc.method_1560().method_23318(), mc.method_1560().method_23321());
        RenderBuffers.preRender();
        class_289 tessellator = class_289.method_1348();
        class_287 bufferBuilder = tessellator.method_60827(class_293.class_5596.field_27382, class_290.field_1576);
        bufferBuilder.method_22912((float)bb.field_1323, (float)bb.field_1322, (float)bb.field_1321).method_22915((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
        bufferBuilder.method_22912((float)bb.field_1320, (float)bb.field_1322, (float)bb.field_1321).method_22915((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
        bufferBuilder.method_22912((float)bb.field_1320, (float)bb.field_1325, (float)bb.field_1321).method_22915((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
        bufferBuilder.method_22912((float)bb.field_1323, (float)bb.field_1325, (float)bb.field_1321).method_22915((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
        bufferBuilder.method_22912((float)bb.field_1323, (float)bb.field_1322, (float)bb.field_1324).method_22915((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
        bufferBuilder.method_22912((float)bb.field_1320, (float)bb.field_1322, (float)bb.field_1324).method_22915((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
        bufferBuilder.method_22912((float)bb.field_1320, (float)bb.field_1325, (float)bb.field_1324).method_22915((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
        bufferBuilder.method_22912((float)bb.field_1323, (float)bb.field_1325, (float)bb.field_1324).method_22915((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
        class_286.method_43433((class_9801)bufferBuilder.method_60800());
        RenderBuffers.postRender();
    }

    public static void drawBlockOutline(class_2338 position, Color color, float width) {
        RenderUtils.drawBlockOutline(RenderUtils.getRenderBB(position), color, width);
    }

    public static void drawBlockOutline(class_238 bb, Color color, float width) {
        float red = (float)color.getRed() / 255.0f;
        float green = (float)color.getGreen() / 255.0f;
        float blue = (float)color.getBlue() / 255.0f;
        float alpha = (float)color.getAlpha() / 255.0f;
        camera.method_23088(((class_1297)Objects.requireNonNull((Object)mc.method_1560())).method_23317(), mc.method_1560().method_23318(), mc.method_1560().method_23321());
        if (camera.method_23093(new class_238(bb.field_1323 + RenderUtils.mc.method_1561().field_4686.method_19326().field_1352, bb.field_1322 + RenderUtils.mc.method_1561().field_4686.method_19326().field_1351, bb.field_1321 + RenderUtils.mc.method_1561().field_4686.method_19326().field_1350, bb.field_1320 + RenderUtils.mc.method_1561().field_4686.method_19326().field_1352, bb.field_1325 + RenderUtils.mc.method_1561().field_4686.method_19326().field_1351, bb.field_1324 + RenderUtils.mc.method_1561().field_4686.method_19326().field_1350))) {
            RenderBuffers.preRender();
            RenderSystem.lineWidth((float)width);
            class_289 tessellator = class_289.method_1348();
            class_287 bufferbuilder = tessellator.method_60827(class_293.class_5596.field_27377, class_290.field_1576);
            bufferbuilder.method_22912((float)bb.field_1323, (float)bb.field_1322, (float)bb.field_1321).method_22915(red, green, blue, alpha);
            bufferbuilder.method_22912((float)bb.field_1323, (float)bb.field_1322, (float)bb.field_1324).method_22915(red, green, blue, alpha);
            bufferbuilder.method_22912((float)bb.field_1320, (float)bb.field_1322, (float)bb.field_1324).method_22915(red, green, blue, alpha);
            bufferbuilder.method_22912((float)bb.field_1320, (float)bb.field_1322, (float)bb.field_1321).method_22915(red, green, blue, alpha);
            bufferbuilder.method_22912((float)bb.field_1323, (float)bb.field_1322, (float)bb.field_1321).method_22915(red, green, blue, alpha);
            bufferbuilder.method_22912((float)bb.field_1323, (float)bb.field_1325, (float)bb.field_1321).method_22915(red, green, blue, alpha);
            bufferbuilder.method_22912((float)bb.field_1323, (float)bb.field_1325, (float)bb.field_1324).method_22915(red, green, blue, alpha);
            bufferbuilder.method_22912((float)bb.field_1323, (float)bb.field_1322, (float)bb.field_1324).method_22915(red, green, blue, alpha);
            bufferbuilder.method_22912((float)bb.field_1320, (float)bb.field_1322, (float)bb.field_1324).method_22915(red, green, blue, alpha);
            bufferbuilder.method_22912((float)bb.field_1320, (float)bb.field_1325, (float)bb.field_1324).method_22915(red, green, blue, alpha);
            bufferbuilder.method_22912((float)bb.field_1323, (float)bb.field_1325, (float)bb.field_1324).method_22915(red, green, blue, alpha);
            bufferbuilder.method_22912((float)bb.field_1320, (float)bb.field_1325, (float)bb.field_1324).method_22915(red, green, blue, alpha);
            bufferbuilder.method_22912((float)bb.field_1320, (float)bb.field_1325, (float)bb.field_1321).method_22915(red, green, blue, alpha);
            bufferbuilder.method_22912((float)bb.field_1320, (float)bb.field_1322, (float)bb.field_1321).method_22915(red, green, blue, alpha);
            bufferbuilder.method_22912((float)bb.field_1320, (float)bb.field_1325, (float)bb.field_1321).method_22915(red, green, blue, alpha);
            bufferbuilder.method_22912((float)bb.field_1323, (float)bb.field_1325, (float)bb.field_1321).method_22915(red, green, blue, alpha);
            class_286.method_43433((class_9801)bufferbuilder.method_60800());
            RenderBuffers.postRender();
        }
    }

    public static class_238 getRenderBB(Object position) {
        if (position instanceof class_2338) {
            return new class_238((double)((class_2338)position).method_10263() - RenderUtils.mc.method_1561().field_4686.method_19326().field_1352, (double)((class_2338)position).method_10264() - RenderUtils.mc.method_1561().field_4686.method_19326().field_1351, (double)((class_2338)position).method_10260() - RenderUtils.mc.method_1561().field_4686.method_19326().field_1350, (double)(((class_2338)position).method_10263() + 1) - RenderUtils.mc.method_1561().field_4686.method_19326().field_1352, (double)(((class_2338)position).method_10264() + 1) - RenderUtils.mc.method_1561().field_4686.method_19326().field_1351, (double)(((class_2338)position).method_10260() + 1) - RenderUtils.mc.method_1561().field_4686.method_19326().field_1350);
        }
        if (position instanceof class_238) {
            return new class_238(((class_238)position).field_1323 - RenderUtils.mc.method_1561().field_4686.method_19326().field_1352, ((class_238)position).field_1322 - RenderUtils.mc.method_1561().field_4686.method_19326().field_1351, ((class_238)position).field_1321 - RenderUtils.mc.method_1561().field_4686.method_19326().field_1350, ((class_238)position).field_1320 - RenderUtils.mc.method_1561().field_4686.method_19326().field_1352, ((class_238)position).field_1325 - RenderUtils.mc.method_1561().field_4686.method_19326().field_1351, ((class_238)position).field_1324 - RenderUtils.mc.method_1561().field_4686.method_19326().field_1350);
        }
        return null;
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
            this.positionMatrix = RenderUtils.toMatrix4d(stack.method_23760().method_23761());
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
