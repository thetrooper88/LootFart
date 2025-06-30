package dev.loottech.asm.mixins.accessor;

import net.minecraft.class_4592;
import net.minecraft.class_630;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={class_4592.class})
public interface AccessorAnimalModel {
    @Invoker(value="getBodyParts")
    public Iterable<class_630> hookGetBodyParts();

    @Invoker(value="getHeadParts")
    public Iterable<class_630> hookGetHeadParts();

    @Accessor(value="headScaled")
    public boolean hookGetHeadScaled();

    @Accessor(value="invertedChildHeadScale")
    public float hookGetInvertedChildHeadScale();

    @Accessor(value="invertedChildBodyScale")
    public float hookGetInvertedChildBodyScale();

    @Accessor(value="childHeadYOffset")
    public float hookGetChildHeadYOffset();

    @Accessor(value="childHeadZOffset")
    public float hookGetChildHeadZOffset();

    @Accessor(value="childBodyYOffset")
    public float hookGetChildBodyYOffset();
}
