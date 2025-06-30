package dev.loottech.asm.mixins;

import dev.loottech.api.manager.client.InteractType;
import dev.loottech.api.utilities.Util;
import dev.loottech.asm.ducks.IPlayerInteractEntityC2SPacket;
import io.netty.buffer.Unpooled;
import net.minecraft.class_1297;
import net.minecraft.class_2540;
import net.minecraft.class_2824;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value={class_2824.class})
public abstract class PlayerInteractEntityC2SPacketMixin
implements IPlayerInteractEntityC2SPacket {
    @Shadow
    @Final
    private int field_12870;

    @Shadow
    public abstract void method_55976(class_2540 var1);

    @Override
    public class_1297 getEntity() {
        if (Util.mc.field_1687 == null) {
            return null;
        }
        return Util.mc.field_1687.method_8469(this.field_12870);
    }

    @Override
    public InteractType getType() {
        class_2540 packetBuf = new class_2540(Unpooled.buffer());
        this.method_55976(packetBuf);
        packetBuf.method_10816();
        return (InteractType)packetBuf.method_10818(InteractType.class);
    }
}
