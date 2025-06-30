package dev.loottech.asm.mixins;

import dev.loottech.api.manager.Managers;
import dev.loottech.client.modules.visuals.NoRender;
import net.minecraft.class_689;
import net.minecraft.class_691;
import net.minecraft.class_702;
import net.minecraft.class_703;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_702.class})
public class ParticleManagerMixin {
    @Inject(at={@At(value="HEAD")}, method={"addParticle(Lnet/minecraft/client/particle/Particle;)V"}, cancellable=true)
    public void addParticleHook(class_703 p, CallbackInfo e) {
        if ((p instanceof class_691 || p instanceof class_689) && Managers.MODULE.getInstance(NoRender.class).explosions.getValue()) {
            e.cancel();
        }
    }
}
