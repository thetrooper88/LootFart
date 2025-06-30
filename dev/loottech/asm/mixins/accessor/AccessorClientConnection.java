package dev.loottech.asm.mixins.accessor;

import net.minecraft.class_2535;
import net.minecraft.class_2596;
import net.minecraft.class_7648;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={class_2535.class})
public interface AccessorClientConnection {
    @Invoker(value="sendInternal")
    public void hookSendInternal(class_2596<?> var1, @Nullable class_7648 var2, boolean var3);
}
