package dev.loottech.asm.mixins;

import dev.loottech.api.manager.Managers;
import dev.loottech.client.events.AttackBlockEvent;
import dev.loottech.client.events.InteractItemEvent;
import dev.loottech.client.events.ItemDesyncEvent;
import dev.loottech.client.events.PacketSneakingEvent;
import net.minecraft.class_1268;
import net.minecraft.class_1269;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_636;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_636.class}, priority=1001)
public class ClientPlayerInteractionManagerMixin {
    @Inject(method={"attackBlock"}, at={@At(value="HEAD")}, cancellable=true)
    private void attackBlock(class_2338 pos, class_2350 direction, CallbackInfoReturnable<Boolean> info) {
        AttackBlockEvent event = new AttackBlockEvent(pos, direction);
        Managers.EVENT.call(event);
        if (event.isCanceled()) {
            info.setReturnValue((Object)false);
        }
    }

    @Inject(method={"interactItem"}, at={@At(value="HEAD")}, cancellable=true)
    private void onInteractItem(class_1657 player, class_1268 hand, CallbackInfoReturnable<class_1269> info) {
        InteractItemEvent event = new InteractItemEvent(hand);
        Managers.EVENT.call(event);
        if (event.toReturn != null) {
            info.setReturnValue((Object)event.toReturn);
        }
    }

    @Redirect(method={"interactBlockInternal"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayerEntity;getStackInHand(Lnet/minecraft/util/Hand;)Lnet/minecraft/item/ItemStack;"))
    private class_1799 hookRedirectInteractBlockInternal$getStackInHand(class_746 entity, class_1268 hand) {
        if (hand.equals((Object)class_1268.field_5810)) {
            return entity.method_5998(hand);
        }
        ItemDesyncEvent itemDesyncEvent = new ItemDesyncEvent();
        Managers.EVENT.call(itemDesyncEvent);
        return itemDesyncEvent.isCanceled() ? itemDesyncEvent.getServerItem() : entity.method_5998(class_1268.field_5808);
    }

    @Redirect(method={"interactBlockInternal"}, at=@At(value="INVOKE", target="Lnet/minecraft/item/ItemStack;isEmpty()Z", ordinal=0))
    private boolean hookRedirectInteractBlockInternal$getMainHandStack(class_1799 instance) {
        ItemDesyncEvent itemDesyncEvent = new ItemDesyncEvent();
        Managers.EVENT.call(itemDesyncEvent);
        return itemDesyncEvent.isCanceled() ? itemDesyncEvent.getServerItem().method_7960() : instance.method_7960();
    }

    @Redirect(method={"interactBlockInternal"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayerEntity;shouldCancelInteraction()Z"))
    private boolean hookRedirectInteractBlockInternal$shouldCancelInteraction(class_746 player) {
        PacketSneakingEvent packetSneakingEvent = new PacketSneakingEvent();
        Managers.EVENT.call(packetSneakingEvent);
        return player.method_5715() || packetSneakingEvent.isCanceled();
    }
}
