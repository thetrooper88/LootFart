package dev.loottech.asm.mixins;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.miscellaneous.ContainerManager;
import dev.loottech.api.utilities.MathUtils;
import dev.loottech.api.utilities.render.IconRenderer;
import dev.loottech.asm.ducks.IDrawContext;
import dev.loottech.client.modules.client.Font;
import dev.loottech.client.modules.visuals.ShulkerTooltips;
import net.minecraft.class_1799;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import net.minecraft.class_5481;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(value={class_332.class})
public abstract class DrawContextMixin
implements IDrawContext {
    @Shadow
    @Final
    private class_4587 field_44657;
    @Unique
    IconRenderer iconRenderer;
    @Unique
    boolean adjustSize = false;

    @Override
    public void loottech$setAdjustSize(boolean newValue) {
        this.adjustSize = newValue;
    }

    @Inject(method={"drawText(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;IIIZ)I"}, at={@At(value="HEAD")})
    private void drawText(class_327 textRenderer, String text, int x, int y, int color, boolean shadow, CallbackInfoReturnable<Integer> info) {
        if (Managers.MODULE.getInstance(Font.class).isEnabled() && Managers.MODULE.getInstance(Font.class).globalFont.getValue()) {
            if (shadow) {
                Managers.FONT.draw(this.field_44657, text, (float)x, (float)y, color);
            } else {
                Managers.FONT.drawWithShadow(this.field_44657, text, (float)x, (float)y, color);
            }
        }
    }

    @Inject(method={"drawText(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/OrderedText;IIIZ)I"}, at={@At(value="HEAD")})
    private void drawText(class_327 textRenderer, class_5481 text, int x, int y, int color, boolean shadow, CallbackInfoReturnable<Integer> info) {
        if (Managers.MODULE.getInstance(Font.class).isEnabled() && Managers.MODULE.getInstance(Font.class).globalFont.getValue()) {
            if (shadow) {
                Managers.FONT.draw(this.field_44657, MathUtils.orderedTextToString(text), (float)x, (float)y, color);
            } else {
                Managers.FONT.drawWithShadow(this.field_44657, MathUtils.orderedTextToString(text), (float)x, (float)y, color);
            }
        }
    }

    @Inject(at={@At(value="INVOKE", target="net/minecraft/item/ItemStack.isItemBarVisible()Z")}, method={"drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V"})
    private void renderShulkerItemOverlay(class_327 renderer, class_1799 stack, int x, int y, @Nullable String countLabel, CallbackInfo info) {
        if (Managers.MODULE.getInstance(ShulkerTooltips.class).isEnabled() && Managers.MODULE.getInstance(ShulkerTooltips.class).majorityItemConfig.getValue()) {
            ContainerManager parser = new ContainerManager(stack);
            class_1799 displayStack = parser.getDisplayStack();
            if (displayStack == null) {
                return;
            }
            this.iconRenderer = new IconRenderer(parser, displayStack, x, y);
            this.iconRenderer.renderOptional((class_332)this);
        }
    }

    @ModifyArgs(method={"drawItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;IIII)V"}, at=@At(value="INVOKE", target="net/minecraft/client/util/math/MatrixStack.translate(FFF)V"))
    private void injectedTranslateXYZ(Args args) {
        if (Managers.MODULE.getInstance(ShulkerTooltips.class).isEnabled() && Managers.MODULE.getInstance(ShulkerTooltips.class).majorityItemConfig.getValue() && this.adjustSize) {
            args.set(0, (Object)Float.valueOf((float)(((Float)args.get(0)).floatValue() + this.iconRenderer.xOffset)));
            args.set(1, (Object)Float.valueOf((float)(((Float)args.get(1)).floatValue() + this.iconRenderer.yOffset)));
            args.set(2, (Object)Float.valueOf((float)this.iconRenderer.zOffset));
        }
    }

    @ModifyArgs(method={"drawItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;IIII)V"}, at=@At(value="INVOKE", target="net/minecraft/client/util/math/MatrixStack.scale(FFF)V"))
    private void injectedScale(Args args) {
        if (Managers.MODULE.getInstance(ShulkerTooltips.class).isEnabled() && Managers.MODULE.getInstance(ShulkerTooltips.class).majorityItemConfig.getValue() && this.adjustSize) {
            args.set(0, (Object)Float.valueOf((float)this.iconRenderer.scale));
            args.set(1, (Object)Float.valueOf((float)(-this.iconRenderer.scale)));
            args.set(2, (Object)Float.valueOf((float)this.iconRenderer.scale));
        }
    }
}
