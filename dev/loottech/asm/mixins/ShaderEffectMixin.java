package dev.loottech.asm.mixins;

import dev.loottech.api.utilities.interfaces.IShaderEffect;
import dev.loottech.asm.mixins.accessor.IPostEffectPass;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.class_276;
import net.minecraft.class_279;
import net.minecraft.class_283;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_279.class})
public class ShaderEffectMixin
implements IShaderEffect {
    @Unique
    private final List<String> fakedBufferNames = new ArrayList();
    @Shadow
    @Final
    private Map<String, class_276> field_1495;
    @Shadow
    @Final
    private List<class_283> field_1497;

    @Override
    public void loottech$addFakeTargetHook(String name, class_276 buffer) {
        class_276 previousFramebuffer = (class_276)this.field_1495.get((Object)name);
        if (previousFramebuffer == buffer) {
            return;
        }
        if (previousFramebuffer != null) {
            for (class_283 pass : this.field_1497) {
                if (pass.field_1536 == previousFramebuffer) {
                    ((IPostEffectPass)pass).setInput(buffer);
                }
                if (pass.field_1538 != previousFramebuffer) continue;
                ((IPostEffectPass)pass).setOutput(buffer);
            }
            this.field_1495.remove((Object)name);
            this.fakedBufferNames.remove((Object)name);
        }
        this.field_1495.put((Object)name, (Object)buffer);
        this.fakedBufferNames.add((Object)name);
    }

    @Inject(method={"close"}, at={@At(value="HEAD")})
    void deleteFakeBuffersHook(CallbackInfo ci) {
        for (String fakedBufferName : this.fakedBufferNames) {
            this.field_1495.remove((Object)fakedBufferName);
        }
    }
}
