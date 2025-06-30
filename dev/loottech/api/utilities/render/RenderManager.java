package dev.loottech.api.utilities.render;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.loottech.api.manager.Managers;
import dev.loottech.api.utilities.Util;
import dev.loottech.api.utilities.render.RenderBuffers;
import dev.loottech.api.utilities.render.ScissorStack;
import dev.loottech.asm.mixins.accessor.AccessorTextRenderer;
import dev.loottech.asm.mixins.accessor.AccessorWorldRenderer;
import dev.loottech.client.modules.client.Font;
import java.awt.Color;
import net.minecraft.class_1041;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_259;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_2960;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_4184;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_5253;
import net.minecraft.class_757;
import net.minecraft.class_7833;
import net.minecraft.class_8030;
import net.minecraft.class_9801;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public class RenderManager
implements Util {
    public static final class_289 TESSELLATOR = RenderSystem.renderThreadTesselator();
    public static final ScissorStack SCISSOR_STACK = new ScissorStack();

    public static void post(Runnable callback) {
        RenderBuffers.post(callback);
    }

    public static void renderBox(class_4587 matrices, class_2338 p, int color) {
        RenderManager.renderBox(matrices, new class_238(p), color);
    }

    public static void renderBox(class_4587 matrices, class_238 box, int color) {
        if (!RenderManager.isBoxVisible(box)) {
            return;
        }
        matrices.method_22903();
        RenderManager.drawBox(matrices, box, color);
        matrices.method_22909();
    }

    public static void drawBox(class_4587 matrices, class_238 box, int color) {
        RenderManager.drawBox(matrices, box.field_1323, box.field_1322, box.field_1321, box.field_1320, box.field_1325, box.field_1324, color);
    }

    public static void drawBox(class_4587 matrices, double x1, double y1, double z1, double x2, double y2, double z2, int color) {
        RenderBuffers.QUADS.begin(matrices);
        RenderBuffers.QUADS.color(color);
        RenderBuffers.QUADS.vertex(x1, y1, z1).vertex(x2, y1, z1).vertex(x2, y1, z2).vertex(x1, y1, z2);
        RenderBuffers.QUADS.vertex(x1, y2, z1).vertex(x1, y2, z2).vertex(x2, y2, z2).vertex(x2, y2, z1);
        RenderBuffers.QUADS.vertex(x1, y1, z1).vertex(x1, y2, z1).vertex(x2, y2, z1).vertex(x2, y1, z1);
        RenderBuffers.QUADS.vertex(x2, y1, z1).vertex(x2, y2, z1).vertex(x2, y2, z2).vertex(x2, y1, z2);
        RenderBuffers.QUADS.vertex(x1, y1, z2).vertex(x2, y1, z2).vertex(x2, y2, z2).vertex(x1, y2, z2);
        RenderBuffers.QUADS.vertex(x1, y1, z1).vertex(x1, y1, z2).vertex(x1, y2, z2).vertex(x1, y2, z1);
        RenderBuffers.QUADS.end();
    }

    public static void renderSide(class_4587 matrices, float x1, float y1, float z1, float x2, float y2, float z2, class_2350 direction, int color) {
        matrices.method_22903();
        RenderManager.drawSide(matrices, x1, y1, z1, x2, y2, z2, direction, color);
        matrices.method_22909();
    }

    public static void renderSide(class_4587 matrices, double x1, double y1, double z1, double x2, double y2, double z2, class_2350 direction, int color) {
        matrices.method_22903();
        RenderManager.drawSide(matrices, x1, y1, z1, x2, y2, z2, direction, color);
        matrices.method_22909();
    }

    public static void drawSide(class_4587 matrices, double x1, double y1, double z1, double x2, double y2, double z2, class_2350 direction, int color) {
        RenderBuffers.QUADS.begin(matrices);
        RenderBuffers.QUADS.color(color);
        if (direction.method_10166().method_10178()) {
            RenderBuffers.QUADS.vertex(x1, y1, z1).vertex(x2, y1, z1).vertex(x2, y1, z2).vertex(x1, y1, z2);
        } else if (direction == class_2350.field_11043 || direction == class_2350.field_11035) {
            RenderBuffers.QUADS.vertex(x1, y1, z1).vertex(x1, y2, z1).vertex(x2, y2, z1).vertex(x2, y1, z1);
        } else {
            RenderBuffers.QUADS.vertex(x1, y1, z1).vertex(x1, y1, z2).vertex(x1, y2, z2).vertex(x1, y2, z1);
        }
        RenderBuffers.QUADS.end();
    }

    public static void renderPlane(class_4587 matrices, double x1, double y1, double z1, double x2, double y2, double z2, int color) {
        matrices.method_22903();
        RenderManager.drawPlane(matrices, x1, y1, z1, x2, y2, z2, color);
        matrices.method_22909();
    }

    public static void drawPlane(class_4587 matrices, double x1, double y1, double z1, double x2, double y2, double z2, int color) {
        RenderBuffers.QUADS.begin(matrices);
        RenderBuffers.QUADS.color(color);
        RenderBuffers.QUADS.vertex(x1, y1, z1).vertex(x1, y2, z1).vertex(x2, y2, z2).vertex(x2, y1, z2);
        RenderBuffers.QUADS.end();
    }

    public static void renderBoundingCross(class_4587 matrices, class_2338 p, float width, int color) {
        RenderManager.renderBoundingCross(matrices, new class_238(p), width, color);
    }

    public static void renderBoundingCross(class_4587 matrices, class_238 box, float width, int color) {
        if (!RenderManager.isBoxVisible(box)) {
            return;
        }
        matrices.method_22903();
        RenderSystem.lineWidth((float)width);
        RenderManager.drawBoundingCross(matrices, box, color);
        matrices.method_22909();
    }

    public static void drawBoundingCross(class_4587 matrices, class_238 box, int color) {
        RenderManager.drawBoundingCross(matrices, box.field_1323, box.field_1322, box.field_1321, box.field_1320, box.field_1325, box.field_1324, color);
    }

    public static void drawBoundingCross(class_4587 matrices, double x1, double y1, double z1, double x2, double y2, double z2, int color) {
        RenderBuffers.LINES.begin(matrices);
        RenderBuffers.LINES.color(color);
        RenderBuffers.LINES.vertexLine(x1, y1, z1, x2, y1, z2);
        RenderBuffers.LINES.vertexLine(x2, y1, z1, x1, y1, z2);
        RenderBuffers.LINES.end();
    }

    public static void renderBoundingBox(class_4587 matrices, class_2338 p, float width, int color) {
        RenderManager.renderBoundingBox(matrices, new class_238(p), width, color);
    }

    public static void renderBoundingBox(class_4587 matrices, class_238 box, float width, int color) {
        if (!RenderManager.isBoxVisible(box)) {
            return;
        }
        matrices.method_22903();
        RenderSystem.lineWidth((float)width);
        RenderManager.drawBoundingBox(matrices, box, color);
        matrices.method_22909();
    }

    public static void drawBoundingBox(class_4587 matrices, class_238 box, int color) {
        RenderManager.drawBoundingBox(matrices, box.field_1323, box.field_1322, box.field_1321, box.field_1320, box.field_1325, box.field_1324, color);
    }

    public static void drawBoundingBox(class_4587 matrices, double x1, double y1, double z1, double x2, double y2, double z2, int color) {
        RenderBuffers.LINES.begin(matrices);
        RenderBuffers.LINES.color(color);
        double dx = x2 - x1;
        double dy = y2 - y1;
        double dz = z2 - z1;
        if (dy == 0.0) {
            RenderBuffers.LINES.vertexLine(x1, y1, z1, x2, y1, z1);
            RenderBuffers.LINES.vertexLine(x1, y1, z1, x1, y1, z2);
            RenderBuffers.LINES.vertexLine(x2, y1, z2, x1, y1, z2);
            RenderBuffers.LINES.vertexLine(x2, y1, z2, x2, y1, z1);
            RenderBuffers.LINES.end();
            return;
        }
        class_259.method_1081((double)0.0, (double)0.0, (double)0.0, (double)dx, (double)dy, (double)dz).method_1104((minX, minY, minZ, maxX, maxY, maxZ) -> RenderBuffers.LINES.vertexLine(minX + x1, minY + y1, minZ + z1, maxX + x1, maxY + y1, maxZ + z1));
        RenderBuffers.LINES.end();
    }

    public static void renderLine(class_4587 matrices, class_243 s, class_243 d, float width, int color) {
        RenderManager.renderLine(matrices, s.field_1352, s.field_1351, s.field_1350, d.field_1352, d.field_1351, d.field_1350, width, color);
    }

    public static void renderLine(class_4587 matrices, double x1, double y1, double z1, double x2, double y2, double z2, float width, int color) {
        matrices.method_22903();
        RenderSystem.lineWidth((float)width);
        RenderManager.drawLine(matrices, x1, y1, z1, x2, y2, z2, color);
        matrices.method_22909();
    }

    public static void drawLine(class_4587 matrices, double x1, double y1, double z1, double x2, double y2, double z2, int color) {
        matrices.method_22903();
        RenderBuffers.LINES.begin(matrices);
        RenderBuffers.LINES.color(color);
        RenderBuffers.LINES.vertexLine(x1, y1, z1, x2, y2, z2);
        RenderBuffers.LINES.end();
        matrices.method_22909();
    }

    public static void renderSign(String text, class_243 pos, float xoffset, float yoffset, int color) {
        RenderManager.renderSign(text, pos.method_10216(), pos.method_10214(), pos.method_10215(), xoffset, yoffset, color);
    }

    public static void renderSign(String text, class_243 pos, float xoffset, float yoffset, float scaling, int color) {
        RenderManager.renderSign(text, pos.method_10216(), pos.method_10214(), pos.method_10215(), xoffset, yoffset, scaling, color);
    }

    public static void renderSign(String text, class_243 pos, float xoffset, float yoffset, Color color) {
        RenderManager.renderSign(text, pos.field_1352, pos.field_1351, pos.field_1350, xoffset, yoffset, color.getRGB());
    }

    public static void renderSign(String text, double x, double y, double z, float xoffset, float yoffset, int color) {
        class_4184 camera = RenderManager.mc.field_1773.method_19418();
        class_243 pos = camera.method_19326();
        double dist = Math.sqrt((double)pos.method_1028(x, y, z));
        float scaling = (float)((double)0.0018f + (double)0.003f * dist);
        if (dist <= 8.0) {
            scaling = 0.0245f;
        }
        RenderManager.renderSign(text, x, y, z, xoffset, yoffset, scaling, color);
    }

    public static void renderSign(String text, double x, double y, double z, float xoffset, float yoffset, float scaling, int color) {
        class_4184 camera = RenderManager.mc.field_1773.method_19418();
        class_243 pos = camera.method_19326();
        class_4587 matrices = new class_4587();
        matrices.method_22907(class_7833.field_40714.rotationDegrees(camera.method_19329()));
        matrices.method_22907(class_7833.field_40716.rotationDegrees(camera.method_19330() + 180.0f));
        matrices.method_22904(x - pos.method_10216(), y - pos.method_10214(), z - pos.method_10215());
        matrices.method_22907(class_7833.field_40716.rotationDegrees(-camera.method_19330()));
        matrices.method_22907(class_7833.field_40714.rotationDegrees(camera.method_19329()));
        matrices.method_22905(-scaling, -scaling, -1.0f);
        float hwidth = (float)RenderManager.mc.field_1772.method_1727(text) / 2.0f;
        RenderManager.post(() -> {
            GL11.glDepthFunc((int)519);
            if (Managers.MODULE.isModuleEnabled(Font.class)) {
                Managers.FONT.drawWithShadow(matrices, text, -hwidth + xoffset, yoffset - 2.75f, color);
            } else {
                class_4597.class_4598 vertexConsumers = mc.method_22940().method_23000();
                ((AccessorTextRenderer)RenderManager.mc.field_1772).hookDrawLayer(text, -hwidth + xoffset, yoffset, class_327.method_27515((int)color), true, matrices.method_23760().method_23761(), (class_4597)vertexConsumers, class_327.class_6415.field_33994, 0, 0xF000F0);
                vertexConsumers.method_22993();
                ((AccessorTextRenderer)RenderManager.mc.field_1772).hookDrawLayer(text, -hwidth + xoffset, yoffset, class_327.method_27515((int)color), false, matrices.method_23760().method_23761(), (class_4597)vertexConsumers, class_327.class_6415.field_33994, 0, 0xF000F0);
                vertexConsumers.method_22993();
            }
            GL11.glDepthFunc((int)515);
        });
    }

    public static boolean isBoxVisible(class_238 box) {
        return ((AccessorWorldRenderer)RenderManager.mc.field_1769).getFrustum().method_23093(box);
    }

    public static void rect(class_4587 matrices, double x1, double y1, double x2, double y2, int color) {
        RenderManager.rect(matrices, x1, y1, x2, y2, 0.0, color);
    }

    public static void borderedRect(class_4587 matrices, double x1, double y1, double x2, double y2, int borderColor, double thickness) {
        RenderManager.rect(matrices, x1 - thickness, y1 - thickness, thickness, y2 + thickness * 2.0, borderColor);
        RenderManager.rect(matrices, x1 + x2, y1 - thickness, thickness, y2 + thickness * 2.0, borderColor);
        RenderManager.rect(matrices, (float)x1, (float)(y1 - thickness - 1.0), (float)x2, (float)thickness, borderColor);
        RenderManager.rect(matrices, (float)x1, (float)(y1 + y2 + 1.0), (float)x2, (float)thickness, borderColor);
    }

    public static void borderedRectLine(class_4587 matrices, double x1, double y1, double x2, double y2, int borderColor) {
        RenderManager.rectLine(matrices, x1, y1, 0.0, y2, borderColor);
        RenderManager.rectLine(matrices, x1 + x2, y1, 0.0, y2, borderColor);
        RenderManager.rectLine(matrices, x1, y1, x2, 0.0, borderColor);
        RenderManager.rectLine(matrices, x1, y1 + y2, x2, 0.0, borderColor);
    }

    public static void rect(class_4587 matrices, double x1, double y1, double x2, double y2, double z, int color) {
        double i;
        x2 += x1;
        y2 += y1;
        Matrix4f matrix4f = matrices.method_23760().method_23761();
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
        float f = (float)class_5253.class_5254.method_27762((int)color) / 255.0f;
        float g = (float)class_5253.class_5254.method_27765((int)color) / 255.0f;
        float h = (float)class_5253.class_5254.method_27766((int)color) / 255.0f;
        float j = (float)class_5253.class_5254.method_27767((int)color) / 255.0f;
        RenderSystem.enableBlend();
        RenderSystem.setShader(class_757::method_34540);
        class_287 bufferBuilder = TESSELLATOR.method_60827(class_293.class_5596.field_27382, class_290.field_1576);
        bufferBuilder.method_22918(matrix4f, (float)x1, (float)y1, (float)z).method_22915(g, h, j, f);
        bufferBuilder.method_22918(matrix4f, (float)x1, (float)y2, (float)z).method_22915(g, h, j, f);
        bufferBuilder.method_22918(matrix4f, (float)x2, (float)y2, (float)z).method_22915(g, h, j, f);
        bufferBuilder.method_22918(matrix4f, (float)x2, (float)y1, (float)z).method_22915(g, h, j, f);
        class_286.method_43433((class_9801)bufferBuilder.method_60800());
        RenderSystem.disableBlend();
    }

    public static void rectLine(class_4587 matrices, double x1, double y1, double x2, double y2, int color) {
        double i;
        x2 += x1;
        y2 += y1;
        Matrix4f matrix4f = matrices.method_23760().method_23761();
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
        float f = (float)class_5253.class_5254.method_27762((int)color) / 255.0f;
        float g = (float)class_5253.class_5254.method_27765((int)color) / 255.0f;
        float h = (float)class_5253.class_5254.method_27766((int)color) / 255.0f;
        float j = (float)class_5253.class_5254.method_27767((int)color) / 255.0f;
        RenderSystem.enableBlend();
        RenderSystem.setShader(class_757::method_34540);
        class_287 bufferBuilder = TESSELLATOR.method_60827(class_293.class_5596.field_29344, class_290.field_1576);
        bufferBuilder.method_22918(matrix4f, (float)x1, (float)y1, 0.0f).method_22915(g, h, j, f);
        bufferBuilder.method_22918(matrix4f, (float)x2, (float)y2, 0.0f).method_22915(g, h, j, f);
        class_286.method_43433((class_9801)bufferBuilder.method_60800());
        RenderSystem.disableBlend();
    }

    public static void rectGradient(class_4587 matrices, double x1, double y1, double x2, double y2, int color, int color1) {
        double i;
        x2 += x1;
        y2 += y1;
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
        RenderManager.fillGradientQuad(matrices, (float)x1, (float)y1, (float)x2, (float)y2, color1, color, true);
    }

    public static void fillGradientQuad(class_332 context, float x1, float y1, float x2, float y2, int startColor, int endColor, boolean sideways) {
        RenderManager.fillGradientQuad(context.method_51448(), x1, y1, x2, y2, startColor, endColor, sideways);
    }

    public static void fillGradientQuad(class_4587 matrixStack, float x1, float y1, float x2, float y2, int startColor, int endColor, boolean sideways) {
        float f = (float)(startColor >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(startColor >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(startColor >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(startColor & 0xFF) / 255.0f;
        float f4 = (float)(endColor >> 24 & 0xFF) / 255.0f;
        float f5 = (float)(endColor >> 16 & 0xFF) / 255.0f;
        float f6 = (float)(endColor >> 8 & 0xFF) / 255.0f;
        float f7 = (float)(endColor & 0xFF) / 255.0f;
        Matrix4f posMatrix = matrixStack.method_23760().method_23761();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(class_757::method_34540);
        class_287 bufferBuilder = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1576);
        if (sideways) {
            bufferBuilder.method_22918(posMatrix, x1, y1, 0.0f).method_22915(f1, f2, f3, f);
            bufferBuilder.method_22918(posMatrix, x1, y2, 0.0f).method_22915(f1, f2, f3, f);
            bufferBuilder.method_22918(posMatrix, x2, y2, 0.0f).method_22915(f5, f6, f7, f4);
            bufferBuilder.method_22918(posMatrix, x2, y1, 0.0f).method_22915(f5, f6, f7, f4);
        } else {
            bufferBuilder.method_22918(posMatrix, x2, y1, 0.0f).method_22915(f1, f2, f3, f);
            bufferBuilder.method_22918(posMatrix, x1, y1, 0.0f).method_22915(f1, f2, f3, f);
            bufferBuilder.method_22918(posMatrix, x1, y2, 0.0f).method_22915(f5, f6, f7, f4);
            bufferBuilder.method_22918(posMatrix, x2, y2, 0.0f).method_22915(f5, f6, f7, f4);
        }
        class_286.method_43433((class_9801)bufferBuilder.method_60800());
        RenderSystem.disableBlend();
    }

    public static void rectTextured(class_4587 matrix, class_2960 identifier, float x0, float x1, float y0, float y1, float z, float u0, float u1, float v0, float v1, float red, float green, float blue, float alpha) {
        Matrix4f matrix4f = matrix.method_23760().method_23761();
        RenderSystem.setShaderTexture((int)0, (class_2960)identifier);
        RenderSystem.setShader(class_757::method_34543);
        RenderSystem.enableBlend();
        class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
        buffer.method_22918(matrix4f, x0, y0, z).method_22915(red, green, blue, alpha).method_22913(u0, v0);
        buffer.method_22918(matrix4f, x0, y1, z).method_22915(red, green, blue, alpha).method_22913(u0, v1);
        buffer.method_22918(matrix4f, x1, y1, z).method_22915(red, green, blue, alpha).method_22913(u1, v1);
        buffer.method_22918(matrix4f, x1, y0, z).method_22915(red, green, blue, alpha).method_22913(u1, v0);
        class_286.method_43433((class_9801)buffer.method_60800());
        RenderSystem.disableBlend();
    }

    public static void enableScissor(double x1, double y1, double x2, double y2) {
        x1 = Math.floor((double)x1);
        y1 = Math.floor((double)y1);
        x2 = Math.ceil((double)x2);
        y2 = Math.ceil((double)y2);
        RenderManager.setScissor(SCISSOR_STACK.push(new class_8030((int)x1, (int)y1, (int)(x2 - x1), (int)(y2 - y1))));
    }

    public static void disableScissor() {
        RenderManager.setScissor(SCISSOR_STACK.pop());
    }

    private static void setScissor(class_8030 rect) {
        if (rect != null) {
            class_1041 window = mc.method_22683();
            int i = window.method_4506();
            double d = window.method_4495();
            double e = (double)rect.method_49620() * d;
            double f = (double)i - (double)rect.method_49619() * d;
            double g = (double)rect.comp_1196() * d;
            double h = (double)rect.comp_1197() * d;
            RenderSystem.enableScissor((int)((int)e), (int)((int)f), (int)Math.max((int)0, (int)((int)g)), (int)Math.max((int)0, (int)((int)h)));
        } else {
            RenderSystem.disableScissor();
        }
    }

    public static void renderText(class_332 context, String text, float x, float y, int color) {
        if (Managers.MODULE.isModuleEnabled(Font.class)) {
            Managers.FONT.drawWithShadow(context.method_51448(), text, x, y, color);
            return;
        }
        context.method_51433(RenderManager.mc.field_1772, text, (int)x, (int)y, color, true);
    }

    public static float textWidth(String text) {
        return Managers.FONT.getWidth(text);
    }

    public static float textHeight() {
        return Managers.FONT.getStringHeight("A");
    }
}
