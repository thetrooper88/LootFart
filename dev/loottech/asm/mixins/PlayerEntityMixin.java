package dev.loottech.asm.mixins;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.utilities.Util;
import dev.loottech.client.events.SprintResetEvent;
import net.minecraft.class_1299;
import net.minecraft.class_1304;
import net.minecraft.class_1306;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_1937;
import net.minecraft.class_243;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value={class_1657.class})
public class PlayerEntityMixin
extends class_1309
implements Util {
    protected PlayerEntityMixin(class_1299<? extends class_1309> entityType, class_1937 world) {
        super(entityType, world);
    }

    public Iterable<class_1799> method_5661() {
        return null;
    }

    public class_1799 method_6118(class_1304 slot) {
        return null;
    }

    public void method_5673(class_1304 slot, class_1799 stack) {
    }

    public class_1306 method_6068() {
        return null;
    }

    @Redirect(method={"attack"}, at=@At(value="INVOKE", target="Lnet/minecraft/entity/player/PlayerEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V"))
    private void hookAttack(class_1657 playerEntity, class_243 movementInput) {
        if (playerEntity instanceof class_746) {
            SprintResetEvent sprintResetEvent = new SprintResetEvent();
            Managers.EVENT.call(sprintResetEvent);
            if (!sprintResetEvent.isCanceled()) {
                PlayerEntityMixin.mc.field_1724.method_18799(PlayerEntityMixin.mc.field_1724.method_18798().method_18805(0.6, 1.0, 0.6));
            }
        }
    }

    @Redirect(method={"attack"}, at=@At(value="INVOKE", target="Lnet/minecraft/entity/player/PlayerEntity;setSprinting(Z)V"))
    private void hookAttack$1(class_1657 instance, boolean b) {
        if (instance instanceof class_746) {
            SprintResetEvent sprintResetEvent = new SprintResetEvent();
            Managers.EVENT.call(sprintResetEvent);
            if (!sprintResetEvent.isCanceled()) {
                PlayerEntityMixin.mc.field_1724.method_5728(false);
            }
        }
    }
}
