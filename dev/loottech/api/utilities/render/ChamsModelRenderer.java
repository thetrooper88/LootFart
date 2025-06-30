package dev.loottech.api.utilities.render;

import com.google.common.base.MoreObjects;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.loottech.api.manager.Managers;
import dev.loottech.api.utilities.Util;
import dev.loottech.api.utilities.render.RenderBuffers;
import dev.loottech.api.utilities.render.StaticBipedEntityModel;
import dev.loottech.asm.mixins.accessor.AccessorAnimalModel;
import dev.loottech.client.modules.visuals.CrystalModifier;
import dev.loottech.client.modules.visuals.Freecam;
import net.minecraft.class_1007;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1306;
import net.minecraft.class_1309;
import net.minecraft.class_1511;
import net.minecraft.class_1657;
import net.minecraft.class_1764;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2350;
import net.minecraft.class_243;
import net.minecraft.class_2960;
import net.minecraft.class_3532;
import net.minecraft.class_4050;
import net.minecraft.class_4587;
import net.minecraft.class_4592;
import net.minecraft.class_4595;
import net.minecraft.class_5597;
import net.minecraft.class_572;
import net.minecraft.class_578;
import net.minecraft.class_583;
import net.minecraft.class_591;
import net.minecraft.class_596;
import net.minecraft.class_630;
import net.minecraft.class_742;
import net.minecraft.class_7833;
import net.minecraft.class_892;
import net.minecraft.class_897;
import net.minecraft.class_918;
import net.minecraft.class_922;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Quaternionf;
import org.joml.Vector4f;

public class ChamsModelRenderer
implements Util {
    private static final float SINE_45_DEGREES = (float)Math.sin((double)0.7853981633974483);
    private static final class_4587 matrices = new class_4587();
    private static final Vector4f pos1 = new Vector4f();
    private static final Vector4f pos2 = new Vector4f();
    private static final Vector4f pos3 = new Vector4f();
    private static final Vector4f pos4 = new Vector4f();

    public static void renderStaticPlayerModel(class_4587 matrixStack, class_742 entity, StaticBipedEntityModel playerModel, float tickDelta, int color, int lineColor, float lineWidth, boolean lines, boolean fill, boolean shine) {
        double offsetX = playerModel.getX();
        double offsetY = playerModel.getY();
        double offsetZ = playerModel.getZ();
        matrices.method_22903();
        class_897 entityRenderer = mc.method_1561().method_3953((class_1297)entity);
        if (entityRenderer instanceof class_1007) {
            class_1007 renderer = (class_1007)entityRenderer;
            float animationProgress = renderer.method_4045((class_1309)entity, tickDelta);
            ChamsModelRenderer.setupPlayerTransforms(entity, matrices, animationProgress, entity.method_43078(), tickDelta);
            matrices.method_22905(-1.0f, -1.0f, 1.0f);
            renderer.method_4217(entity, matrices, tickDelta);
            matrices.method_22904(0.0, (double)-1.501f, 0.0);
            ChamsModelRenderer.render(matrixStack, playerModel.field_3398, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
            ChamsModelRenderer.render(matrixStack, playerModel.field_3391, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
            ChamsModelRenderer.render(matrixStack, playerModel.field_27433, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
            ChamsModelRenderer.render(matrixStack, playerModel.field_3401, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
            ChamsModelRenderer.render(matrixStack, playerModel.field_3397, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
            ChamsModelRenderer.render(matrixStack, playerModel.field_3392, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
        }
        matrices.method_22909();
    }

    public static void setupPlayerTransforms(class_742 abstractClientPlayerEntity, class_4587 matrixStack, float f, float g, float h) {
        float i = abstractClientPlayerEntity.method_6024(h);
        float j = abstractClientPlayerEntity.method_5695(h);
        if (abstractClientPlayerEntity.method_6128()) {
            ChamsModelRenderer.setupTransforms((class_1309)abstractClientPlayerEntity, matrixStack, f, g, h);
            float k = (float)abstractClientPlayerEntity.method_6003() + h;
            float l = class_3532.method_15363((float)(k * k / 100.0f), (float)0.0f, (float)1.0f);
            if (!abstractClientPlayerEntity.method_6123()) {
                matrixStack.method_22907(class_7833.field_40714.rotationDegrees(l * (-90.0f - j)));
            }
            class_243 vec3d = abstractClientPlayerEntity.method_5828(h);
            class_243 vec3d2 = abstractClientPlayerEntity.method_49339(h);
            double d = vec3d2.method_37268();
            double e = vec3d.method_37268();
            if (d > 0.0 && e > 0.0) {
                double m = (vec3d2.field_1352 * vec3d.field_1352 + vec3d2.field_1350 * vec3d.field_1350) / Math.sqrt((double)(d * e));
                double n = vec3d2.field_1352 * vec3d.field_1350 - vec3d2.field_1350 * vec3d.field_1352;
                matrixStack.method_22907(class_7833.field_40716.rotation((float)(Math.signum((double)n) * Math.acos((double)m))));
            }
        } else if (i > 0.0f) {
            ChamsModelRenderer.setupTransforms((class_1309)abstractClientPlayerEntity, matrixStack, f, g, h);
            float k = abstractClientPlayerEntity.method_5799() ? -90.0f - j : -90.0f;
            float l = class_3532.method_16439((float)i, (float)0.0f, (float)k);
            matrixStack.method_22907(class_7833.field_40714.rotationDegrees(l));
            if (abstractClientPlayerEntity.method_20232()) {
                matrixStack.method_46416(0.0f, -1.0f, 0.3f);
            }
        } else {
            ChamsModelRenderer.setupTransforms((class_1309)abstractClientPlayerEntity, matrixStack, f, g, h);
        }
    }

    public static void setupTransforms(class_1309 entity, class_4587 matrices, float animationProgress, float bodyYaw, float tickDelta) {
        if (entity.method_32314()) {
            bodyYaw += (float)(Math.cos((double)((double)entity.field_6012 * 3.25)) * Math.PI * (double)0.4f);
        }
        if (!entity.method_41328(class_4050.field_18078)) {
            matrices.method_22907(class_7833.field_40716.rotationDegrees(180.0f - bodyYaw));
        }
        if (entity.field_6213 > 0) {
            float f;
            float f2 = ((float)entity.field_6213 + tickDelta - 1.0f) / 20.0f * 1.6f;
            f2 = class_3532.method_15355((float)f2);
            if (f > 1.0f) {
                f2 = 1.0f;
            }
            matrices.method_22907(class_7833.field_40718.rotationDegrees(f2 * 90.0f));
        } else if (entity.method_6123()) {
            matrices.method_22907(class_7833.field_40714.rotationDegrees(-90.0f - entity.method_36455()));
            matrices.method_22907(class_7833.field_40716.rotationDegrees(((float)entity.field_6012 + tickDelta) * -75.0f));
        } else if (entity.method_41328(class_4050.field_18078)) {
            class_2350 direction = entity.method_18401();
            float g = direction != null ? ChamsModelRenderer.getYaw(direction) : bodyYaw;
            matrices.method_22907(class_7833.field_40716.rotationDegrees(g));
            matrices.method_22907(class_7833.field_40718.rotationDegrees(90.0f));
            matrices.method_22907(class_7833.field_40716.rotationDegrees(270.0f));
        } else if (class_922.method_38563((class_1309)entity)) {
            matrices.method_46416(0.0f, entity.method_17682() + 0.1f, 0.0f);
            matrices.method_22907(class_7833.field_40718.rotationDegrees(180.0f));
        }
    }

    private static float getYaw(class_2350 direction) {
        return switch (direction) {
            case class_2350.field_11035 -> 90.0f;
            case class_2350.field_11039 -> 0.0f;
            case class_2350.field_11043 -> 270.0f;
            case class_2350.field_11034 -> 180.0f;
            default -> 0.0f;
        };
    }

    public static void render(class_4587 matrixStack, class_1297 entity, float tickDelta, int color, int lineColor, float lineWidth, boolean lines, boolean fill, boolean shine) {
        class_922 renderer;
        double offsetX = class_3532.method_16436((double)tickDelta, (double)entity.field_6038, (double)entity.method_23317());
        double offsetY = class_3532.method_16436((double)tickDelta, (double)entity.field_5971, (double)entity.method_23318());
        double offsetZ = class_3532.method_16436((double)tickDelta, (double)entity.field_5989, (double)entity.method_23321());
        matrices.method_22903();
        class_897 entityRenderer = mc.method_1561().method_3953(entity);
        if (entityRenderer instanceof class_922) {
            float animationProgress;
            class_1297 class_12972;
            renderer = (class_922)entityRenderer;
            class_1309 livingEntity = (class_1309)entity;
            class_583 model = renderer.method_4038();
            if (entityRenderer instanceof class_1007) {
                class_1007 r = (class_1007)entityRenderer;
                class_591 playerModel = (class_591)r.method_4038();
                playerModel.field_3400 = entity.method_18276();
                class_572.class_573 armPose = class_1007.method_4210((class_742)((class_742)entity), (class_1268)class_1268.field_5808);
                class_572.class_573 armPose2 = class_1007.method_4210((class_742)((class_742)entity), (class_1268)class_1268.field_5810);
                if (armPose.method_30156()) {
                    class_572.class_573 class_5732 = armPose2 = livingEntity.method_6079().method_7960() ? class_572.class_573.field_3409 : class_572.class_573.field_3410;
                }
                if (livingEntity.method_6068() == class_1306.field_6183) {
                    playerModel.field_3395 = armPose;
                    playerModel.field_3399 = armPose2;
                } else {
                    playerModel.field_3395 = armPose2;
                    playerModel.field_3399 = armPose;
                }
            }
            model.field_3447 = livingEntity.method_6055(tickDelta);
            model.field_3449 = livingEntity.method_5765();
            model.field_3448 = livingEntity.method_6109();
            boolean rotating = entity == ChamsModelRenderer.mc.field_1724 && Managers.ROTATION.isRotating();
            float bodyYaw = rotating ? Managers.ROTATION.getRotationYaw() : class_3532.method_17821((float)tickDelta, (float)livingEntity.field_6220, (float)livingEntity.field_6283);
            float headYaw = rotating ? Managers.ROTATION.getRotationYaw() : class_3532.method_17821((float)tickDelta, (float)livingEntity.field_6259, (float)livingEntity.field_6241);
            float yaw = headYaw - bodyYaw;
            if (livingEntity.method_5765() && (class_12972 = livingEntity.method_5854()) instanceof class_1309) {
                class_1309 livingEntity2 = (class_1309)class_12972;
                bodyYaw = class_3532.method_17821((float)tickDelta, (float)livingEntity2.field_6220, (float)livingEntity2.field_6283);
                yaw = headYaw - bodyYaw;
                animationProgress = class_3532.method_15393((float)yaw);
                if (animationProgress < -85.0f) {
                    animationProgress = -85.0f;
                }
                if (animationProgress >= 85.0f) {
                    animationProgress = 85.0f;
                }
                bodyYaw = headYaw - animationProgress;
                if (animationProgress * animationProgress > 2500.0f) {
                    bodyYaw = (float)((double)bodyYaw + (double)animationProgress * 0.2);
                }
                yaw = headYaw - bodyYaw;
            }
            float pitch = rotating ? Managers.ROTATION.getRotationPitch() : class_3532.method_16439((float)tickDelta, (float)livingEntity.field_6004, (float)livingEntity.method_36455());
            animationProgress = renderer.method_4045(livingEntity, tickDelta);
            float limbDistance = 0.0f;
            float limbAngle = 0.0f;
            if (!livingEntity.method_5765() && livingEntity.method_5805()) {
                limbDistance = livingEntity.field_42108.method_48570(tickDelta);
                limbAngle = livingEntity.field_42108.method_48572(tickDelta);
                if (livingEntity.method_6109()) {
                    limbAngle *= 3.0f;
                }
                if (limbDistance > 1.0f) {
                    limbDistance = 1.0f;
                }
            }
            model.method_2816((class_1297)livingEntity, limbAngle, limbDistance, tickDelta);
            model.method_2819((class_1297)livingEntity, limbAngle, limbDistance, animationProgress, yaw, pitch);
            renderer.method_4058(livingEntity, matrices, animationProgress, bodyYaw, tickDelta, 1.0f);
            matrices.method_22905(-1.0f, -1.0f, 1.0f);
            renderer.method_4042(livingEntity, matrices, tickDelta);
            matrices.method_22904(0.0, (double)-1.501f, 0.0);
            if (model instanceof class_4592) {
                class_4592 m = (class_4592)model;
                if (m.field_3448) {
                    class_572 mo;
                    float g;
                    matrices.method_22903();
                    if (((AccessorAnimalModel)m).hookGetHeadScaled()) {
                        g = 1.5f / ((AccessorAnimalModel)m).hookGetInvertedChildHeadScale();
                        matrices.method_22905(g, g, g);
                    }
                    matrices.method_22904(0.0, (double)(((AccessorAnimalModel)m).hookGetChildHeadYOffset() / 16.0f), (double)(((AccessorAnimalModel)m).hookGetChildHeadZOffset() / 16.0f));
                    if (model instanceof class_572) {
                        mo = (class_572)model;
                        ChamsModelRenderer.render(matrixStack, mo.field_3398, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
                    } else {
                        ((AccessorAnimalModel)m).hookGetHeadParts().forEach(modelPart -> ChamsModelRenderer.render(matrixStack, modelPart, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine));
                    }
                    matrices.method_22909();
                    matrices.method_22903();
                    g = 1.0f / ((AccessorAnimalModel)m).hookGetInvertedChildBodyScale();
                    matrices.method_22905(g, g, g);
                    matrices.method_22904(0.0, (double)(((AccessorAnimalModel)m).hookGetChildBodyYOffset() / 16.0f), 0.0);
                    if (model instanceof class_572) {
                        mo = (class_572)model;
                        ChamsModelRenderer.render(matrixStack, mo.field_3391, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
                        ChamsModelRenderer.render(matrixStack, mo.field_27433, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
                        ChamsModelRenderer.render(matrixStack, mo.field_3401, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
                        ChamsModelRenderer.render(matrixStack, mo.field_3397, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
                        ChamsModelRenderer.render(matrixStack, mo.field_3392, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
                    } else {
                        ((AccessorAnimalModel)m).hookGetBodyParts().forEach(modelPart -> ChamsModelRenderer.render(matrixStack, modelPart, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine));
                    }
                    matrices.method_22909();
                } else if (model instanceof class_572) {
                    class_572 mo = (class_572)model;
                    ChamsModelRenderer.render(matrixStack, mo.field_3398, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
                    ChamsModelRenderer.render(matrixStack, mo.field_3391, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
                    ChamsModelRenderer.render(matrixStack, mo.field_27433, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
                    ChamsModelRenderer.render(matrixStack, mo.field_3401, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
                    ChamsModelRenderer.render(matrixStack, mo.field_3397, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
                    ChamsModelRenderer.render(matrixStack, mo.field_3392, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
                } else {
                    ((AccessorAnimalModel)m).hookGetHeadParts().forEach(modelPart -> ChamsModelRenderer.render(matrixStack, modelPart, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine));
                    ((AccessorAnimalModel)m).hookGetBodyParts().forEach(modelPart -> ChamsModelRenderer.render(matrixStack, modelPart, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine));
                }
            } else if (model instanceof class_5597) {
                class_5597 m = (class_5597)model;
                ChamsModelRenderer.render(matrixStack, m.method_32008(), offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
            } else if (model instanceof class_4595) {
                class_4595 m = (class_4595)model;
                m.method_22960().forEach(modelPart -> ChamsModelRenderer.render(matrixStack, (class_630)modelPart, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine));
            } else if (model instanceof class_578) {
                class_578 m = (class_578)model;
                if (m.field_3448) {
                    matrices.method_22903();
                    matrices.method_22905(0.71428573f, 0.64935064f, 0.7936508f);
                    matrices.method_22904(0.0, 1.3125, (double)0.22f);
                    ChamsModelRenderer.render(matrixStack, m.field_27443, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
                    matrices.method_22909();
                    matrices.method_22903();
                    matrices.method_22905(0.625f, 0.45454544f, 0.45454544f);
                    matrices.method_22904(0.0, 2.0625, 0.0);
                    ChamsModelRenderer.render(matrixStack, m.field_27444, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
                    matrices.method_22909();
                    matrices.method_22903();
                    matrices.method_22905(0.45454544f, 0.41322312f, 0.45454544f);
                    matrices.method_22904(0.0, 2.0625, 0.0);
                    ChamsModelRenderer.render(matrixStack, m.field_27445, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
                    ChamsModelRenderer.render(matrixStack, m.field_27446, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
                    ChamsModelRenderer.render(matrixStack, m.field_27447, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
                    ChamsModelRenderer.render(matrixStack, m.field_27448, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
                    ChamsModelRenderer.render(matrixStack, m.field_27449, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
                    ChamsModelRenderer.render(matrixStack, m.field_27450, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
                    matrices.method_22909();
                } else {
                    ChamsModelRenderer.render(matrixStack, m.field_27443, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
                    ChamsModelRenderer.render(matrixStack, m.field_27444, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
                    ChamsModelRenderer.render(matrixStack, m.field_27445, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
                    ChamsModelRenderer.render(matrixStack, m.field_27446, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
                    ChamsModelRenderer.render(matrixStack, m.field_27447, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
                    ChamsModelRenderer.render(matrixStack, m.field_27448, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
                    ChamsModelRenderer.render(matrixStack, m.field_27449, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
                    ChamsModelRenderer.render(matrixStack, m.field_27450, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
                }
            } else if (model instanceof class_596) {
                class_596 m = (class_596)model;
                if (m.field_3448) {
                    matrices.method_22903();
                    matrices.method_22905(0.56666666f, 0.56666666f, 0.56666666f);
                    matrices.method_22904(0.0, 1.375, 0.125);
                    ChamsModelRenderer.render(matrixStack, m.field_27486, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
                    ChamsModelRenderer.render(matrixStack, m.field_27488, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
                    ChamsModelRenderer.render(matrixStack, m.field_27487, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
                    ChamsModelRenderer.render(matrixStack, m.field_3530, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
                    matrices.method_22909();
                    matrices.method_22903();
                    matrices.method_22905(0.4f, 0.4f, 0.4f);
                    matrices.method_22904(0.0, 2.25, 0.0);
                    ChamsModelRenderer.render(matrixStack, m.field_27480, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
                    ChamsModelRenderer.render(matrixStack, m.field_27481, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
                    ChamsModelRenderer.render(matrixStack, m.field_27482, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
                    ChamsModelRenderer.render(matrixStack, m.field_27483, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
                    ChamsModelRenderer.render(matrixStack, m.field_3528, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
                    ChamsModelRenderer.render(matrixStack, m.field_27484, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
                    ChamsModelRenderer.render(matrixStack, m.field_27485, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
                    ChamsModelRenderer.render(matrixStack, m.field_3524, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
                    matrices.method_22909();
                } else {
                    matrices.method_22903();
                    matrices.method_22905(0.6f, 0.6f, 0.6f);
                    matrices.method_22904(0.0, 1.0, 0.0);
                    ChamsModelRenderer.render(matrixStack, m.field_27480, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
                    ChamsModelRenderer.render(matrixStack, m.field_27481, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
                    ChamsModelRenderer.render(matrixStack, m.field_27482, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
                    ChamsModelRenderer.render(matrixStack, m.field_27483, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
                    ChamsModelRenderer.render(matrixStack, m.field_3528, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
                    ChamsModelRenderer.render(matrixStack, m.field_27484, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
                    ChamsModelRenderer.render(matrixStack, m.field_27485, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
                    ChamsModelRenderer.render(matrixStack, m.field_27486, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
                    ChamsModelRenderer.render(matrixStack, m.field_27487, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
                    ChamsModelRenderer.render(matrixStack, m.field_27488, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
                    ChamsModelRenderer.render(matrixStack, m.field_3524, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
                    ChamsModelRenderer.render(matrixStack, m.field_3530, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
                    matrices.method_22909();
                }
            }
        }
        if (entityRenderer instanceof class_892) {
            renderer = (class_892)entityRenderer;
            class_1511 crystalEntity = (class_1511)entity;
            matrices.method_22903();
            float h = Managers.MODULE.getInstance(CrystalModifier.class).isEnabled() && !Managers.MODULE.getInstance(CrystalModifier.class).getBounce() ? -1.0f : class_892.method_23155((class_1511)crystalEntity, (float)tickDelta);
            float j = ((float)crystalEntity.field_7034 + tickDelta) * (Managers.MODULE.getInstance(CrystalModifier.class).isEnabled() ? Managers.MODULE.getInstance(CrystalModifier.class).getSpin() : 1.0f) * 3.0f;
            matrices.method_22903();
            if (Managers.MODULE.getInstance(CrystalModifier.class).isEnabled()) {
                float crystalScale = Managers.MODULE.getInstance(CrystalModifier.class).getScale();
                matrices.method_22905(crystalScale, crystalScale, crystalScale);
            }
            matrices.method_22905(2.0f, 2.0f, 2.0f);
            matrices.method_46416(0.0f, -0.5f, 0.0f);
            matrices.method_22907(class_7833.field_40716.rotationDegrees(j));
            matrices.method_22904(0.0, (double)(1.5f + h / 2.0f), 0.0);
            matrices.method_22907(new Quaternionf().setAngleAxis(1.0471976f, SINE_45_DEGREES, 0.0f, SINE_45_DEGREES));
            ChamsModelRenderer.render(matrixStack, renderer.field_21004, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
            matrices.method_22905(0.875f, 0.875f, 0.875f);
            matrices.method_22907(new Quaternionf().setAngleAxis(1.0471976f, SINE_45_DEGREES, 0.0f, SINE_45_DEGREES));
            matrices.method_22907(class_7833.field_40716.rotationDegrees(j));
            ChamsModelRenderer.render(matrixStack, renderer.field_21004, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
            matrices.method_22905(0.875f, 0.875f, 0.875f);
            matrices.method_22907(new Quaternionf().setAngleAxis(1.0471976f, SINE_45_DEGREES, 0.0f, SINE_45_DEGREES));
            matrices.method_22907(class_7833.field_40716.rotationDegrees(j));
            matrices.method_22909();
            matrices.method_22909();
        }
        matrices.method_22909();
    }

    public static void renderHand(class_4587 matrixStack, float tickDelta, int lineColor, int color, float lineWidth, boolean lines, boolean fill, boolean shine) {
        class_1306 arm;
        float j;
        boolean bl3;
        boolean bl1;
        if (!ChamsModelRenderer.mc.field_1690.method_31044().method_31034() || Managers.MODULE.getInstance(Freecam.class).isEnabled()) {
            return;
        }
        double d = ((Integer)ChamsModelRenderer.mc.field_1690.method_41808().method_41753()).intValue();
        matrixStack.method_34425(ChamsModelRenderer.mc.field_1773.method_22973(d));
        matrixStack.method_34426();
        if (((Boolean)ChamsModelRenderer.mc.field_1690.method_42448().method_41753()).booleanValue()) {
            class_1657 playerEntity = (class_1657)mc.method_1560();
            float f = playerEntity.field_5973 - playerEntity.field_6039;
            float g = -(playerEntity.field_5973 + f * tickDelta);
            float h = class_3532.method_16439((float)tickDelta, (float)playerEntity.field_7505, (float)playerEntity.field_7483);
            matrixStack.method_46416(class_3532.method_15374((float)(g * (float)Math.PI)) * h * 0.5f, -Math.abs((float)(class_3532.method_15362((float)(g * (float)Math.PI)) * h)), 0.0f);
            matrixStack.method_22907(class_7833.field_40718.rotationDegrees(class_3532.method_15374((float)(g * (float)Math.PI)) * h * 3.0f));
            matrixStack.method_22907(class_7833.field_40714.rotationDegrees(Math.abs((float)(class_3532.method_15362((float)(g * (float)Math.PI - 0.2f)) * h)) * 5.0f));
        }
        float h1 = class_3532.method_16439((float)tickDelta, (float)ChamsModelRenderer.mc.field_1724.field_3914, (float)ChamsModelRenderer.mc.field_1724.field_3916);
        float i1 = class_3532.method_16439((float)tickDelta, (float)ChamsModelRenderer.mc.field_1724.field_3931, (float)ChamsModelRenderer.mc.field_1724.field_3932);
        matrixStack.method_22907(class_7833.field_40714.rotationDegrees((ChamsModelRenderer.mc.field_1724.method_5695(tickDelta) - h1) * 0.1f));
        matrixStack.method_22907(class_7833.field_40716.rotationDegrees((ChamsModelRenderer.mc.field_1724.method_5705(tickDelta) - i1) * 0.1f));
        float f1 = ChamsModelRenderer.mc.field_1724.method_6055(tickDelta);
        class_1268 hand = (class_1268)MoreObjects.firstNonNull((Object)ChamsModelRenderer.mc.field_1724.field_6266, (Object)class_1268.field_5808);
        class_1799 itemStack = ChamsModelRenderer.mc.field_1724.method_6047();
        class_1799 itemStack2 = ChamsModelRenderer.mc.field_1724.method_6079();
        boolean bl = itemStack.method_31574(class_1802.field_8102) || itemStack2.method_31574(class_1802.field_8102);
        boolean bl2 = itemStack.method_31574(class_1802.field_8399) || itemStack2.method_31574(class_1802.field_8399);
        HandRenderType handRenderType = HandRenderType.RENDER_BOTH_HANDS;
        if (!bl && !bl2) {
            handRenderType = HandRenderType.RENDER_BOTH_HANDS;
        } else if (ChamsModelRenderer.mc.field_1724.method_6115()) {
            class_1799 itemStack1 = ChamsModelRenderer.mc.field_1724.method_6030();
            class_1268 hand1 = ChamsModelRenderer.mc.field_1724.method_6058();
            handRenderType = itemStack1.method_31574(class_1802.field_8102) || itemStack1.method_31574(class_1802.field_8399) ? HandRenderType.shouldOnlyRender(hand1) : (hand == class_1268.field_5808 && ChamsModelRenderer.mc.field_1724.method_6079().method_31574(class_1802.field_8399) && class_1764.method_7781((class_1799)ChamsModelRenderer.mc.field_1724.method_6079()) ? HandRenderType.RENDER_MAIN_HAND_ONLY : HandRenderType.RENDER_BOTH_HANDS);
        } else if (itemStack.method_31574(class_1802.field_8399) && class_1764.method_7781((class_1799)itemStack)) {
            handRenderType = HandRenderType.RENDER_MAIN_HAND_ONLY;
        }
        class_1007 playerEntityRenderer = (class_1007)mc.method_1561().method_3953((class_1297)ChamsModelRenderer.mc.field_1724);
        if (handRenderType.renderMainHand) {
            bl1 = hand == class_1268.field_5808;
            bl3 = ChamsModelRenderer.mc.field_1724.field_6266 == class_1268.field_5808;
            j = bl3 ? f1 : 0.0f;
            float k = 1.0f - class_3532.method_16439((float)tickDelta, (float)ChamsModelRenderer.mc.field_1773.field_4012.field_4053, (float)ChamsModelRenderer.mc.field_1773.field_4012.field_4043);
            class_1306 class_13062 = arm = bl1 ? ChamsModelRenderer.mc.field_1724.method_6068() : ChamsModelRenderer.mc.field_1724.method_6068().method_5928();
            if (itemStack.method_7960() && bl1 && !ChamsModelRenderer.mc.field_1724.method_5767()) {
                ChamsModelRenderer.renderFirstPersonItem(matrixStack, tickDelta, playerEntityRenderer, arm, j, k, lineColor, color, lineWidth, lines, fill, shine);
            }
        }
        if (handRenderType.renderOffHand) {
            bl1 = hand == class_1268.field_5810;
            bl3 = ChamsModelRenderer.mc.field_1724.field_6266 == class_1268.field_5808;
            j = bl3 ? f1 : 0.0f;
            float k = 1.0f - class_3532.method_16439((float)tickDelta, (float)ChamsModelRenderer.mc.field_1773.field_4012.field_4051, (float)ChamsModelRenderer.mc.field_1773.field_4012.field_4052);
            class_1306 class_13063 = arm = bl1 ? ChamsModelRenderer.mc.field_1724.method_6068().method_5928() : ChamsModelRenderer.mc.field_1724.method_6068();
            if (itemStack.method_7960() && bl1 && !ChamsModelRenderer.mc.field_1724.method_5767()) {
                ChamsModelRenderer.renderFirstPersonItem(matrixStack, tickDelta, playerEntityRenderer, arm, j, k, lineColor, color, lineWidth, lines, fill, shine);
            }
        }
    }

    public static void renderFirstPersonItem(class_4587 matrixStack, float tickDelta, class_1007 playerEntityRenderer, class_1306 arm, float swingProgress, float equipProgress, int lineColor, int color, float lineWidth, boolean lines, boolean fill, boolean shine) {
        RenderSystem.disableDepthTest();
        double offsetX = ChamsModelRenderer.mc.field_1773.method_19418().method_19326().field_1352;
        double offsetY = ChamsModelRenderer.mc.field_1773.method_19418().method_19326().field_1351;
        double offsetZ = ChamsModelRenderer.mc.field_1773.method_19418().method_19326().field_1350;
        matrices.method_22903();
        boolean bl = arm != class_1306.field_6182;
        float f = bl ? 1.0f : -1.0f;
        float g = class_3532.method_15355((float)swingProgress);
        float h = -0.3f * class_3532.method_15374((float)(g * (float)Math.PI));
        float i = 0.4f * class_3532.method_15374((float)(g * ((float)Math.PI * 2)));
        float j = -0.4f * class_3532.method_15374((float)(swingProgress * (float)Math.PI));
        matrices.method_46416(f * (h + 0.64000005f), i + -0.6f + equipProgress * -0.6f, j + -0.71999997f);
        matrices.method_22907(class_7833.field_40716.rotationDegrees(f * 45.0f));
        float k = class_3532.method_15374((float)(swingProgress * swingProgress * (float)Math.PI));
        float l = class_3532.method_15374((float)(g * (float)Math.PI));
        matrices.method_22907(class_7833.field_40716.rotationDegrees(f * l * 70.0f));
        matrices.method_22907(class_7833.field_40718.rotationDegrees(f * k * -20.0f));
        matrices.method_46416(f * -1.0f, 3.6f, 3.5f);
        matrices.method_22907(class_7833.field_40718.rotationDegrees(f * 120.0f));
        matrices.method_22907(class_7833.field_40714.rotationDegrees(200.0f));
        matrices.method_22907(class_7833.field_40716.rotationDegrees(f * -135.0f));
        matrices.method_46416(f * 5.6f, 0.0f, 0.0f);
        playerEntityRenderer.method_4218((class_742)ChamsModelRenderer.mc.field_1724);
        class_591 model = (class_591)playerEntityRenderer.method_4038();
        model.field_3447 = 0.0f;
        model.field_3400 = false;
        model.field_3396 = 0.0f;
        model.method_17087((class_1309)ChamsModelRenderer.mc.field_1724, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
        if (arm == class_1306.field_6183) {
            model.field_3401.field_3654 = 0.0f;
            ChamsModelRenderer.render(matrixStack, model.field_3401, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
            model.field_3486.field_3654 = 0.0f;
        } else {
            model.field_27433.field_3654 = 0.0f;
            ChamsModelRenderer.render(matrixStack, model.field_27433, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
            model.field_3484.field_3654 = 0.0f;
        }
        matrices.method_22909();
    }

    public static void render(class_4587 matrixStack, class_630 part, double offsetX, double offsetY, double offsetZ, int color, int lineColor, float lineWidth, boolean lines, boolean fill, boolean shine) {
        if (!part.field_3665 || part.field_3663.isEmpty() && part.field_3661.isEmpty()) {
            return;
        }
        matrices.method_22903();
        part.method_22703(matrices);
        for (class_630.class_628 cuboid : part.field_3663) {
            ChamsModelRenderer.renderModelPart(matrixStack, cuboid, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
        }
        for (class_630 child : part.field_3661.values()) {
            ChamsModelRenderer.render(matrixStack, child, offsetX, offsetY, offsetZ, color, lineColor, lineWidth, lines, fill, shine);
        }
        matrices.method_22909();
    }

    private static void renderModelPart(class_4587 matrixStack, class_630.class_628 cuboid, double offsetX, double offsetY, double offsetZ, int color, int lineColor, float lineWidth, boolean lines, boolean fill, boolean shine) {
        Matrix4f matrix = matrices.method_23760().method_23761();
        for (class_630.class_593 quad : cuboid.field_3649) {
            pos1.set(quad.field_3502[0].field_3605.x / 16.0f, quad.field_3502[0].field_3605.y / 16.0f, quad.field_3502[0].field_3605.z / 16.0f, 1.0f);
            pos1.mul((Matrix4fc)matrix);
            pos2.set(quad.field_3502[1].field_3605.x / 16.0f, quad.field_3502[1].field_3605.y / 16.0f, quad.field_3502[1].field_3605.z / 16.0f, 1.0f);
            pos2.mul((Matrix4fc)matrix);
            pos3.set(quad.field_3502[2].field_3605.x / 16.0f, quad.field_3502[2].field_3605.y / 16.0f, quad.field_3502[2].field_3605.z / 16.0f, 1.0f);
            pos3.mul((Matrix4fc)matrix);
            pos4.set(quad.field_3502[3].field_3605.x / 16.0f, quad.field_3502[3].field_3605.y / 16.0f, quad.field_3502[3].field_3605.z / 16.0f, 1.0f);
            pos4.mul((Matrix4fc)matrix);
            if (fill) {
                if (shine) {
                    RenderBuffers.TEXTURE_QUADS.begin(matrixStack);
                    RenderSystem.setShaderTexture((int)0, (class_2960)class_918.field_43087);
                    RenderBuffers.TEXTURE_QUADS.color(color);
                    RenderBuffers.TEXTURE_QUADS.vertexTex(offsetX + (double)ChamsModelRenderer.pos1.x, offsetY + (double)ChamsModelRenderer.pos1.y, offsetZ + (double)ChamsModelRenderer.pos1.z, 0.0f, 0.0f).vertexTex(offsetX + (double)ChamsModelRenderer.pos2.x, offsetY + (double)ChamsModelRenderer.pos2.y, offsetZ + (double)ChamsModelRenderer.pos2.z, 0.0f, 1.0f).vertexTex(offsetX + (double)ChamsModelRenderer.pos3.x, offsetY + (double)ChamsModelRenderer.pos3.y, offsetZ + (double)ChamsModelRenderer.pos3.z, 1.0f, 1.0f).vertexTex(offsetX + (double)ChamsModelRenderer.pos4.x, offsetY + (double)ChamsModelRenderer.pos4.y, offsetZ + (double)ChamsModelRenderer.pos4.z, 1.0f, 0.0f);
                    RenderBuffers.TEXTURE_QUADS.end();
                } else {
                    RenderBuffers.QUADS.begin(matrixStack);
                    RenderBuffers.QUADS.color(color);
                    RenderBuffers.QUADS.vertex(offsetX + (double)ChamsModelRenderer.pos1.x, offsetY + (double)ChamsModelRenderer.pos1.y, offsetZ + (double)ChamsModelRenderer.pos1.z).vertex(offsetX + (double)ChamsModelRenderer.pos2.x, offsetY + (double)ChamsModelRenderer.pos2.y, offsetZ + (double)ChamsModelRenderer.pos2.z).vertex(offsetX + (double)ChamsModelRenderer.pos3.x, offsetY + (double)ChamsModelRenderer.pos3.y, offsetZ + (double)ChamsModelRenderer.pos3.z).vertex(offsetX + (double)ChamsModelRenderer.pos4.x, offsetY + (double)ChamsModelRenderer.pos4.y, offsetZ + (double)ChamsModelRenderer.pos4.z);
                    RenderBuffers.QUADS.end();
                }
            }
            if (!lines) continue;
            RenderBuffers.LINES.begin(matrixStack);
            RenderSystem.lineWidth((float)lineWidth);
            RenderBuffers.LINES.color(lineColor);
            RenderBuffers.LINES.vertexLine(offsetX + (double)ChamsModelRenderer.pos1.x, offsetY + (double)ChamsModelRenderer.pos1.y, offsetZ + (double)ChamsModelRenderer.pos1.z, offsetX + (double)ChamsModelRenderer.pos2.x, offsetY + (double)ChamsModelRenderer.pos2.y, offsetZ + (double)ChamsModelRenderer.pos2.z);
            RenderBuffers.LINES.vertexLine(offsetX + (double)ChamsModelRenderer.pos2.x, offsetY + (double)ChamsModelRenderer.pos2.y, offsetZ + (double)ChamsModelRenderer.pos2.z, offsetX + (double)ChamsModelRenderer.pos3.x, offsetY + (double)ChamsModelRenderer.pos3.y, offsetZ + (double)ChamsModelRenderer.pos3.z);
            RenderBuffers.LINES.vertexLine(offsetX + (double)ChamsModelRenderer.pos3.x, offsetY + (double)ChamsModelRenderer.pos3.y, offsetZ + (double)ChamsModelRenderer.pos3.z, offsetX + (double)ChamsModelRenderer.pos4.x, offsetY + (double)ChamsModelRenderer.pos4.y, offsetZ + (double)ChamsModelRenderer.pos4.z);
            RenderBuffers.LINES.vertexLine(offsetX + (double)ChamsModelRenderer.pos1.x, offsetY + (double)ChamsModelRenderer.pos1.y, offsetZ + (double)ChamsModelRenderer.pos1.z, offsetX + (double)ChamsModelRenderer.pos1.x, offsetY + (double)ChamsModelRenderer.pos1.y, offsetZ + (double)ChamsModelRenderer.pos1.z);
            RenderBuffers.LINES.end();
        }
    }

    private static enum HandRenderType {
        RENDER_BOTH_HANDS(true, true),
        RENDER_MAIN_HAND_ONLY(true, false),
        RENDER_OFF_HAND_ONLY(false, true);

        final boolean renderMainHand;
        final boolean renderOffHand;

        private HandRenderType(boolean renderMainHand, boolean renderOffHand) {
            this.renderMainHand = renderMainHand;
            this.renderOffHand = renderOffHand;
        }

        public static HandRenderType shouldOnlyRender(class_1268 hand) {
            return hand == class_1268.field_5808 ? RENDER_MAIN_HAND_ONLY : RENDER_OFF_HAND_ONLY;
        }
    }
}
