package dev.loottech.asm.mixins;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.utilities.Util;
import dev.loottech.client.modules.client.ModuleColor;
import java.awt.Color;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_437;
import net.minecraft.class_442;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_442.class})
public abstract class TitleScreenMixin
extends class_437 {
    public TitleScreenMixin(class_2561 title) {
        super(title);
    }

    @Inject(method={"render"}, at={@At(value="TAIL")})
    private void onRender(class_332 context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        String a = "LootTech 0.73 beta";
        String b = " by ";
        String c = "jaxui";
        float x1 = (float)(Util.mc.field_1755.field_22789 / 2) - Managers.FONT.getWidth(a + b + c) / 2.0f;
        float x2 = x1 + Managers.FONT.getWidth(a);
        float x3 = x2 + Managers.FONT.getWidth(b);
        int y = 2;
        Managers.FONT.drawWithShadow(context.method_51448(), a, x1, (float)y, ModuleColor.getColor().getRGB());
        Managers.FONT.drawWithShadow(context.method_51448(), b, x2, (float)y, Color.GRAY.getRGB());
        Managers.FONT.drawWithShadow(context.method_51448(), c, x3, (float)y, Color.WHITE.getRGB());
    }
}
