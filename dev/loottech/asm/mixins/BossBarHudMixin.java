package dev.loottech.asm.mixins;

import dev.loottech.api.manager.Managers;
import dev.loottech.client.modules.visuals.NoRender;
import net.minecraft.class_332;
import net.minecraft.class_337;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_337.class})
public class BossBarHudMixin {
    @Inject(method={"render"}, at={@At(value="HEAD")}, cancellable=true)
    private void render(class_332 context, CallbackInfo info) {
        if (Managers.MODULE.getInstance(NoRender.class).bossBar.getValue()) {
            info.cancel();
        }
    }
}
