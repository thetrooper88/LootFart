package dev.loottech.asm.mixins.accessor;

import net.minecraft.class_32;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={MinecraftServer.class})
public interface MinecraftServerAccessor {
    @Accessor(value="session")
    public class_32.class_5143 getSession();
}
