package dev.loottech.asm.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import dev.loottech.api.manager.Managers;
import dev.loottech.api.utilities.Util;
import dev.loottech.client.events.RenderArmEvent;
import dev.loottech.client.events.RenderSwingAnimationEvent;
import dev.loottech.client.modules.player.Swing;
import dev.loottech.client.modules.visuals.NoRender;
import dev.loottech.client.modules.visuals.ViewModel;
import java.util.Objects;
import net.minecraft.class_1268;
import net.minecraft.class_1306;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_1764;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1935;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_742;
import net.minecraft.class_746;
import net.minecraft.class_759;
import net.minecraft.class_7833;
import net.minecraft.class_811;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(value={class_759.class}, priority=800)
public abstract class HeldItemRendererMixin {
    @Shadow
    private class_1799 field_4048;
    @Unique
    private static final class_1799 END_CRYSTAL = new class_1799((class_1935)class_1802.field_8301, 64);
    @Shadow
    @Final
    private class_310 field_4050;
    @Shadow
    private class_1799 field_4047;
    @Shadow
    private float field_4043;

    @ModifyArgs(method={"renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/network/ClientPlayerEntity;I)V"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/render/item/HeldItemRenderer;renderFirstPersonItem(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/util/Hand;FLnet/minecraft/item/ItemStack;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"))
    private void renderItemHook(Args args, @Local(argsOnly=true) class_746 player) {
        if (Managers.MODULE.getInstance(NoRender.class).isEnabled() && Managers.MODULE.getInstance(NoRender.class).mainhandTotem.getValue()) {
            class_1268 hand = (class_1268)args.get(3);
            if (hand != class_1268.field_5808) {
                return;
            }
            if (!player.method_6047().method_31574(class_1802.field_8288) || !player.method_6079().method_31574(class_1802.field_8288)) {
                return;
            }
            boolean hasCrystal = false;
            for (int i = 0; i < 9; ++i) {
                if (!player.method_31548().method_5438(i).method_31574(class_1802.field_8301)) continue;
                hasCrystal = true;
                break;
            }
            if (!hasCrystal) {
                return;
            }
            args.set(5, (Object)END_CRYSTAL);
        }
    }

    @Unique
    private static float getV(class_4587 matrices, float v, float u) {
        float v1 = v;
        if (1.0f < v1) {
            v1 = 1.0f;
        }
        if (0.1f < v1) {
            float w = class_3532.method_15374((float)((u - 0.1f) * 1.3f));
            float x = v1 - 0.1f;
            float y = w * x;
            matrices.method_46416(y * 0.0f, y * 0.004f, y * 0.0f);
        }
        return v1;
    }

    @Unique
    private static float getU(float tickDelta, @NotNull class_1799 item, @NotNull class_4587 matrices, float o, @NotNull class_310 client) {
        matrices.method_22907(class_7833.field_40716.rotationDegrees(o * 35.3f));
        matrices.method_22907(class_7833.field_40718.rotationDegrees(o * -9.785f));
        assert (null != client.field_1724);
        class_746 playerEntity = client.field_1724;
        float u = (float)item.method_7935((class_1309)playerEntity) - ((float)playerEntity.method_6014() - tickDelta + 1.0f);
        return u;
    }

    @Shadow
    protected abstract void method_3219(class_4587 var1, class_4597 var2, int var3, float var4, float var5, class_1306 var6);

    @Shadow
    protected abstract void method_3231(class_4587 var1, class_4597 var2, int var3, float var4, float var5, float var6);

    @Shadow
    protected abstract void method_3222(class_4587 var1, class_4597 var2, int var3, float var4, class_1306 var5, float var6, class_1799 var7);

    @Shadow
    protected abstract void method_3224(class_4587 var1, class_1306 var2, float var3);

    @Shadow
    public abstract void method_3233(class_1309 var1, class_1799 var2, class_811 var3, boolean var4, class_4587 var5, class_4597 var6, int var7);

    @Shadow
    protected abstract void method_3217(class_4587 var1, class_1306 var2, float var3);

    @Inject(method={"renderFirstPersonItem"}, at={@At(value="HEAD")}, cancellable=true)
    private void renderFirstPersonItem$HEAD(class_742 player, float tickDelta, float pitch, class_1268 hand, float swingProgress, class_1799 item, float equipProgress, class_4587 matrices, class_4597 vertexConsumers, int light, CallbackInfo info) {
        ViewModel module = Managers.MODULE.getInstance(ViewModel.class);
        if (module.isEnabled()) {
            info.cancel();
            if (!player.method_31550()) {
                boolean bl = class_1268.field_5808 == hand;
                class_1306 arm = bl ? player.method_6068() : player.method_6068().method_5928();
                matrices.method_22903();
                if (arm.equals((Object)player.method_6068().method_5928())) {
                    matrices.method_46416(-module.translateX.getValue().floatValue(), module.translateY.getValue().floatValue(), module.translateZ.getValue().floatValue());
                } else {
                    matrices.method_46416(module.translateX.getValue().floatValue(), module.translateY.getValue().floatValue(), module.translateZ.getValue().floatValue());
                }
                matrices.method_22907(class_7833.field_40716.rotationDegrees(module.rotateY.getValue().floatValue()));
                matrices.method_22907(class_7833.field_40714.rotationDegrees(module.rotateX.getValue().floatValue()));
                matrices.method_22907(class_7833.field_40718.rotationDegrees(module.rotateZ.getValue().floatValue()));
                matrices.method_22905(module.scaleX.getValue().floatValue(), module.scaleY.getValue().floatValue(), module.scaleZ.getValue().floatValue());
                if (item.method_7960()) {
                    if (bl && !player.method_5767()) {
                        this.method_3219(matrices, vertexConsumers, light, equipProgress, swingProgress, arm);
                    }
                } else if (item.method_31574(class_1802.field_8204)) {
                    if (bl && this.field_4048.method_7960()) {
                        this.method_3231(matrices, vertexConsumers, light, pitch, equipProgress, swingProgress);
                    } else {
                        this.method_3222(matrices, vertexConsumers, light, equipProgress, arm, swingProgress, item);
                    }
                } else if (item.method_31574(class_1802.field_8399)) {
                    int i;
                    boolean bl4 = class_1764.method_7781((class_1799)item);
                    boolean bl3 = class_1306.field_6183 == arm;
                    int n = i = bl3 ? 1 : -1;
                    if (player.method_6115() && 0 < player.method_6014() && player.method_6058() == hand) {
                        this.method_3224(matrices, arm, equipProgress);
                        matrices.method_22904((double)((float)i * -0.4785682f), (double)-0.094387f, 0.05731530860066414);
                        matrices.method_22907(class_7833.field_40714.rotationDegrees(-11.935f));
                        matrices.method_22907(class_7833.field_40716.rotationDegrees((float)i * 65.3f));
                        matrices.method_22907(class_7833.field_40718.rotationDegrees((float)i * -9.785f));
                        assert (this.field_4050.field_1724 != null);
                        class_746 playerEntity = this.field_4050.field_1724;
                        float v = (float)item.method_7935((class_1309)playerEntity) - ((float)((class_1309)Objects.requireNonNull((Object)playerEntity)).method_6014() - tickDelta + 1.0f);
                        float w = v / (float)class_1764.method_7775((class_1799)item, (class_1309)playerEntity);
                        if (1.0f < w) {
                            w = 1.0f;
                        }
                        if (0.1f < w) {
                            float x = class_3532.method_15374((float)((v - 0.1f) * 1.3f));
                            float y = w - 0.1f;
                            float k = x * y;
                            matrices.method_46416(k * 0.0f, k * 0.004f, k * 0.0f);
                        }
                        matrices.method_46416(w * 0.0f, w * 0.0f, w * 0.04f);
                        matrices.method_22905(1.0f, 1.0f, 1.0f + w * 0.2f);
                        matrices.method_22907(class_7833.field_40715.rotationDegrees((float)i * 45.0f));
                    } else {
                        float v = -0.4f * class_3532.method_15374((float)(class_3532.method_15355((float)swingProgress) * (float)Math.PI));
                        float w = 0.2f * class_3532.method_15374((float)(class_3532.method_15355((float)swingProgress) * ((float)Math.PI * 2)));
                        float x = -0.2f * class_3532.method_15374((float)(swingProgress * (float)Math.PI));
                        matrices.method_46416((float)i * v, w, x);
                        this.method_3224(matrices, arm, equipProgress);
                        this.method_3217(matrices, arm, swingProgress);
                        if (bl4 && 0.001f > swingProgress && bl) {
                            matrices.method_22904((double)((float)i * -0.641864f), 0.0, 0.0);
                            matrices.method_22907(class_7833.field_40716.rotationDegrees((float)i * 10.0f));
                        }
                    }
                    this.method_3233((class_1309)player, item, bl3 ? class_811.field_4322 : class_811.field_4321, !bl3, matrices, vertexConsumers, light);
                } else {
                    boolean bl4;
                    boolean bl2 = bl4 = class_1306.field_6183 == arm;
                    if (player.method_6115() && 0 < player.method_6014() && player.method_6058() == hand) {
                        int o = bl4 ? 1 : -1;
                        switch (item.method_7976()) {
                            case field_8952: 
                            case field_8949: {
                                this.method_3224(matrices, arm, equipProgress);
                                break;
                            }
                            case field_8950: 
                            case field_8946: {
                                this.applyEatOrDrinkTransformation(matrices, tickDelta, arm, item, (class_1657)player);
                                this.method_3224(matrices, arm, equipProgress);
                                break;
                            }
                            case field_8953: {
                                this.method_3224(matrices, arm, equipProgress);
                                matrices.method_22904((double)((float)o * -0.2785682f), 0.18344387412071228, 0.15731531381607056);
                                matrices.method_22907(class_7833.field_40714.rotationDegrees(-13.935f));
                                float u = HeldItemRendererMixin.getU(tickDelta, item, matrices, o, this.field_4050);
                                float v = u / 20.0f;
                                v = (v * v + v * 2.0f) / 3.0f;
                                v = HeldItemRendererMixin.getV(matrices, v, u);
                                matrices.method_46416(v * 0.0f, v * 0.0f, v * 0.04f);
                                matrices.method_22905(1.0f, 1.0f, 1.0f + v * 0.2f);
                                matrices.method_22907(class_7833.field_40715.rotationDegrees((float)o * 45.0f));
                                break;
                            }
                            case field_8951: {
                                this.method_3224(matrices, arm, equipProgress);
                                matrices.method_22904((double)((float)o * -0.5f), (double)0.7f, (double)0.1f);
                                matrices.method_22907(class_7833.field_40714.rotationDegrees(-55.0f));
                                float u = HeldItemRendererMixin.getU(tickDelta, item, matrices, o, this.field_4050);
                                float v = u / 10.0f;
                                v = HeldItemRendererMixin.getV(matrices, v, u);
                                matrices.method_22904(0.0, 0.0, (double)(v * 0.2f));
                                matrices.method_22905(1.0f, 1.0f, 1.0f + v * 0.2f);
                                matrices.method_22907(class_7833.field_40715.rotationDegrees((float)o * 45.0f));
                                break;
                            }
                        }
                    } else if (player.method_6123()) {
                        this.method_3224(matrices, arm, equipProgress);
                        int o = bl4 ? 1 : -1;
                        matrices.method_22904((double)((float)o * -0.4f), (double)0.8f, (double)0.3f);
                        matrices.method_22907(class_7833.field_40716.rotationDegrees((float)o * 65.0f));
                        matrices.method_22907(class_7833.field_40718.rotationDegrees((float)o * -85.0f));
                    } else {
                        float aa = -0.4f * class_3532.method_15374((float)(class_3532.method_15355((float)swingProgress) * (float)Math.PI));
                        float u = 0.2f * class_3532.method_15374((float)(class_3532.method_15355((float)swingProgress) * ((float)Math.PI * 2)));
                        float v = -0.2f * class_3532.method_15374((float)(swingProgress * (float)Math.PI));
                        int ad = bl4 ? 1 : -1;
                        float translateX = !Managers.MODULE.getInstance(Swing.class).isEnabled() || Managers.MODULE.getInstance(Swing.class).translateX.getValue() ? (float)ad * aa : 0.0f;
                        float translateY = !Managers.MODULE.getInstance(Swing.class).isEnabled() || Managers.MODULE.getInstance(Swing.class).translateY.getValue() ? u : 0.0f;
                        float translateZ = !Managers.MODULE.getInstance(Swing.class).isEnabled() || Managers.MODULE.getInstance(Swing.class).translateZ.getValue() ? v : 0.0f;
                        matrices.method_46416(translateX, translateY, translateZ);
                        this.method_3224(matrices, arm, equipProgress);
                        this.method_3217(matrices, arm, swingProgress);
                    }
                    this.method_3233((class_1309)player, item, bl4 ? class_811.field_4322 : class_811.field_4321, !bl4, matrices, vertexConsumers, light);
                }
                matrices.method_22909();
            }
        }
    }

    @ModifyArg(method={"updateHeldItems"}, at=@At(value="INVOKE", target="Lnet/minecraft/util/math/MathHelper;clamp(FFF)F", ordinal=2), index=0)
    private float hookEquipProgressMainhand(float value) {
        RenderSwingAnimationEvent event = new RenderSwingAnimationEvent();
        Managers.EVENT.call(event);
        float f = Util.mc.field_1724.method_7261(1.0f);
        float modified = event.isCanceled() ? 1.0f : f * f * f;
        return (class_1799.method_7973((class_1799)this.field_4047, (class_1799)Util.mc.field_1724.method_6047()) ? modified : 0.0f) - this.field_4043;
    }

    @Inject(method={"renderFirstPersonItem"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V")}, cancellable=true)
    private void hookRenderFirstPersonItem(class_742 player, float tickDelta, float pitch, class_1268 hand, float swingProgress, class_1799 item, float equipProgress, class_4587 matrices, class_4597 vertexConsumers, int light, CallbackInfo ci) {
        RenderArmEvent renderFirstPersonEvent = new RenderArmEvent(hand, item, equipProgress, matrices, vertexConsumers);
        Managers.EVENT.call(renderFirstPersonEvent);
        if (renderFirstPersonEvent.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method={"applyEatOrDrinkTransformation"}, at={@At(value="HEAD")}, cancellable=true)
    private void applyEatOrDrinkTransformation(class_4587 matrices, float tickDelta, class_1306 arm, class_1799 stack, class_1657 player, CallbackInfo ci) {
    }

    @Unique
    private void applyEatOrDrinkTransformation(class_4587 matrices, float tickDelta, class_1306 arm, class_1799 stack, class_1657 player) {
        float h;
        float f = (float)player.method_6014() - tickDelta + 1.0f;
        float g = f / (float)stack.method_7935((class_1309)player);
        if (g < 0.8f) {
            h = class_3532.method_15379((float)(class_3532.method_15362((float)(f / 4.0f * (float)Math.PI)) * 0.1f));
            matrices.method_46416(0.0f, h, 0.0f);
        }
        h = 1.0f - (float)Math.pow((double)g, (double)27.0);
        int i = arm == class_1306.field_6183 ? 1 : -1;
        matrices.method_46416(h * 0.6f * (float)i, h * -0.5f, h * 0.0f);
        matrices.method_22907(class_7833.field_40716.rotationDegrees((float)i * h * 90.0f));
        matrices.method_22907(class_7833.field_40714.rotationDegrees(h * 10.0f));
        matrices.method_22907(class_7833.field_40718.rotationDegrees((float)i * h * 30.0f));
    }
}
