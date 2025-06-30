package dev.loottech.asm.mixins;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.utilities.MathUtils;
import dev.loottech.client.elements.Chat;
import dev.loottech.client.modules.client.Font;
import dev.loottech.client.modules.client.ModuleColor;
import java.util.List;
import java.util.Objects;
import net.minecraft.class_303;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_338;
import net.minecraft.class_3532;
import net.minecraft.class_7591;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_338.class})
public abstract class ChatHudMixin {
    @Shadow
    @Final
    private List<class_303.class_7590> field_2064;
    @Shadow
    @Final
    private class_310 field_2062;
    @Shadow
    private int field_2066;
    @Shadow
    private boolean field_2067;

    @Shadow
    public abstract double method_1814();

    @Shadow
    protected abstract boolean method_23677();

    @Shadow
    public abstract int method_1813();

    @Shadow
    public abstract int method_1811();

    @Shadow
    protected abstract int method_45588(double var1, double var3);

    @Shadow
    protected abstract double method_44722(double var1);

    @Shadow
    protected abstract double method_44724(double var1);

    @Shadow
    protected abstract int method_44752();

    @Shadow
    protected abstract int method_44720(class_303.class_7590 var1);

    @Shadow
    protected abstract void method_44719(class_332 var1, int var2, int var3, class_7591.class_7592 var4);

    @Unique
    private static double getMessageOpacityMultiplier(int age) {
        double d = (double)age / 200.0;
        d = 1.0 - d;
        d *= 10.0;
        d = class_3532.method_15350((double)d, (double)0.0, (double)1.0);
        d *= d;
        return d;
    }

    @Inject(method={"render"}, at={@At(value="HEAD")}, cancellable=true)
    public void render(class_332 context, int currentTick, int mouseX, int mouseY, boolean focused, CallbackInfo ci) {
        if (Managers.ELEMENT.getInstance(Chat.class).isEnabled()) {
            ci.cancel();
            Chat chat = Managers.ELEMENT.getInstance(Chat.class);
            if (this.method_23677()) {
                return;
            }
            int i = this.method_1813();
            int j = this.field_2064.size();
            if (j <= 0) {
                return;
            }
            this.field_2062.method_16011().method_15396("chat");
            float f = (float)this.method_1814();
            int k = class_3532.method_15386((float)((float)this.method_1811() / f));
            int l = context.method_51443();
            context.method_51448().method_22903();
            context.method_51448().method_22905(f, f, 1.0f);
            context.method_51448().method_46416(4.0f, 0.0f, 0.0f);
            int m = class_3532.method_15375((float)((float)(l - 40) / f));
            int n = this.method_45588(this.method_44722(mouseX), this.method_44724(mouseY));
            double d = (Double)this.field_2062.field_1690.method_42542().method_41753() * (double)0.9f + (double)0.1f;
            double e = (Double)this.field_2062.field_1690.method_42550().method_41753();
            double g = (Double)this.field_2062.field_1690.method_42546().method_41753();
            int o = this.method_44752();
            int p = (int)Math.round((double)(-8.0 * (g + 1.0) + 4.0 * g));
            int q = 0;
            for (int r = 0; r + this.field_2066 < this.field_2064.size() && r < i; ++r) {
                int t;
                int s = r + this.field_2066;
                class_303.class_7590 visible = (class_303.class_7590)this.field_2064.get(s);
                if (visible == null || (t = currentTick - visible.comp_895()) >= 200 && !focused) continue;
                double h = focused ? 1.0 : ChatHudMixin.getMessageOpacityMultiplier(t);
                int u = (int)(255.0 * h * d);
                int v = (int)(255.0 * h * e);
                ++q;
                if (u <= 3) continue;
                boolean w = false;
                int x = (int)(chat.y + chat.frame.getHeight() - (float)(r * o));
                int y = x + p;
                int renderY = x + p;
                int renderX = (int)chat.x;
                context.method_25294((int)chat.x, x - o, (int)((float)((int)chat.x) + chat.frame.getWidth()), x, v << 24);
                class_7591 messageIndicator = visible.comp_897();
                if (messageIndicator != null) {
                    int z = messageIndicator.comp_899() | u << 24;
                    context.method_25294((int)chat.x - 4, x - o, (int)chat.x - 2, x, Managers.MODULE.getInstance(ModuleColor.class).color.getValue().getRGB());
                    if (s == n && messageIndicator.comp_900() != null) {
                        int aa = this.method_44720(visible);
                        Objects.requireNonNull((Object)this.field_2062.field_1772);
                        int ab = y + 9;
                        this.method_44719(context, aa, ab, messageIndicator.comp_900());
                    }
                }
                context.method_51448().method_22903();
                context.method_51448().method_46416(0.0f, 0.0f, 50.0f);
                if (Managers.MODULE.getInstance(Font.class).isEnabled() && Managers.MODULE.getInstance(Font.class).globalFont.getValue()) {
                    Managers.FONT.drawWithShadow(context.method_51448(), MathUtils.orderedTextToString(visible.comp_896()), (float)renderX, (float)renderY, 0xFFFFFF + (u << 24));
                } else {
                    context.method_35720(this.field_2062.field_1772, visible.comp_896(), renderX, renderY, 0xFFFFFF + (u << 24));
                }
                context.method_51448().method_22909();
            }
            context.method_51448().method_22909();
            this.field_2062.method_16011().method_15407();
        }
    }
}
