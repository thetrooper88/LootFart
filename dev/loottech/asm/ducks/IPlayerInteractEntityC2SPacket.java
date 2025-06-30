package dev.loottech.asm.ducks;

import dev.loottech.api.manager.client.InteractType;
import dev.loottech.asm.ducks.IMixin;
import net.minecraft.class_1297;

@IMixin
public interface IPlayerInteractEntityC2SPacket {
    public class_1297 getEntity();

    public InteractType getType();
}
