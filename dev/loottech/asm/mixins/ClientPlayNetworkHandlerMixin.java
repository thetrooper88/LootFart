package dev.loottech.asm.mixins;

import dev.loottech.api.manager.Managers;
import dev.loottech.asm.ducks.IClientPlayNetworkHandler;
import dev.loottech.asm.mixins.accessor.AccessorClientConnection;
import dev.loottech.client.events.ChatSendEvent;
import dev.loottech.client.events.ChunkDataEvent;
import dev.loottech.client.events.GameJoinEvent;
import net.minecraft.class_2535;
import net.minecraft.class_2596;
import net.minecraft.class_2672;
import net.minecraft.class_2678;
import net.minecraft.class_2818;
import net.minecraft.class_634;
import net.minecraft.class_638;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_634.class})
public abstract class ClientPlayNetworkHandlerMixin
implements IClientPlayNetworkHandler {
    @Shadow
    private class_638 field_3699;

    @Shadow
    public abstract class_2535 method_48296();

    @Inject(method={"sendChatMessage"}, at={@At(value="HEAD")}, cancellable=true)
    private void onSendChatMessage(String message, CallbackInfo ci) {
        ChatSendEvent event = new ChatSendEvent(message);
        Managers.EVENT.call(event);
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method={"onChunkData"}, at={@At(value="TAIL")})
    private void onChunkData(class_2672 packet, CallbackInfo info) {
        class_2818 chunk = this.field_3699.method_8497(packet.method_11523(), packet.method_11524());
        Managers.EVENT.call(new ChunkDataEvent(chunk));
    }

    @Override
    public void loottech$sendQuietPacket(class_2596<?> packet) {
        ((AccessorClientConnection)this.method_48296()).hookSendInternal(packet, null, true);
    }

    @Inject(method={"onGameJoin"}, at={@At(value="TAIL")})
    private void hookOnGameJoin(class_2678 packet, CallbackInfo ci) {
        GameJoinEvent gameJoinEvent = new GameJoinEvent();
        Managers.EVENT.call(gameJoinEvent);
    }
}
