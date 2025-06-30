package org.ladysnake.satin.mixin.client.render;

import net.minecraft.class_1921;
import net.minecraft.class_4668;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_1921.class_4688.class})
public static interface RenderLayerMixin.MultiPhaseParametersAccessor {
    @Accessor
    public class_4668.class_5939 getTexture();

    @Accessor
    public class_4668.class_5942 getProgram();

    @Accessor
    public class_4668.class_4685 getTransparency();

    @Accessor
    public class_4668.class_4672 getDepthTest();

    @Accessor
    public class_4668.class_4671 getCull();

    @Accessor
    public class_4668.class_4676 getLightmap();

    @Accessor
    public class_4668.class_4679 getOverlay();

    @Accessor
    public class_4668.class_4675 getLayering();

    @Accessor
    public class_4668.class_4678 getTarget();

    @Accessor
    public class_4668.class_4684 getTexturing();

    @Accessor
    public class_4668.class_4686 getWriteMaskState();

    @Accessor
    public class_4668.class_4677 getLineWidth();

    @Accessor
    public class_1921.class_4750 getOutlineMode();
}
