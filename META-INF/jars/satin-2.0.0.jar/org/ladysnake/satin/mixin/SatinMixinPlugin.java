package org.ladysnake.satin.mixin;

import java.util.List;
import java.util.Set;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public final class SatinMixinPlugin
implements IMixinConfigPlugin {
    private static final Logger LOGGER = LogManager.getLogger((String)"Satin");
    private static final boolean ALLOW_RENDER_LAYER_MIXINS;

    public void onLoad(String mixinPackage) {
    }

    public String getRefMapperConfig() {
        return null;
    }

    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.contains((CharSequence)"blockrenderlayer")) {
            return ALLOW_RENDER_LAYER_MIXINS;
        }
        return true;
    }

    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    public List<String> getMixins() {
        return List.of();
    }

    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    static {
        FabricLoader loader = FabricLoader.getInstance();
        if (loader.isModLoaded("canvas")) {
            LOGGER.warn("[Satin] Canvas is present, custom block renders will not work");
            ALLOW_RENDER_LAYER_MIXINS = false;
        } else if (loader.isModLoaded("iris")) {
            LOGGER.warn("[Satin] Iris is present, custom block renders will not work");
            ALLOW_RENDER_LAYER_MIXINS = false;
        } else {
            if (loader.isModLoaded("sodium")) {
                LOGGER.warn("[Satin] Sodium is present, custom block renders may not work");
            }
            ALLOW_RENDER_LAYER_MIXINS = true;
        }
    }
}
