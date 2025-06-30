package dev.loottech.asm.mixins;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.event.EventArgument;
import dev.loottech.client.events.LogoutEvent;
import dev.loottech.client.events.PacketReceiveEvent;
import dev.loottech.client.events.PacketSendEvent;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.class_2535;
import net.minecraft.class_2596;
import net.minecraft.class_2661;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_2535.class})
public class ClientConnectionMixin {
    @Inject(method={"send(Lnet/minecraft/network/packet/Packet;)V"}, at={@At(value="HEAD")}, cancellable=true)
    public void injectSend(class_2596<?> packet, CallbackInfo ci) {
        PacketSendEvent event = new PacketSendEvent(packet);
        Managers.EVENT.call(event);
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method={"channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/packet/Packet;)V"}, at={@At(value="HEAD")}, cancellable=true)
    public void injectChannelRead0(ChannelHandlerContext channelHandlerContext, class_2596<?> packet, CallbackInfo ci) {
        EventArgument event;
        if (packet instanceof class_2661) {
            event = new LogoutEvent();
            Managers.EVENT.call(event);
        }
        event = new PacketReceiveEvent(packet);
        Managers.EVENT.call(event);
        if (event.isCanceled()) {
            ci.cancel();
        }
    }
}
