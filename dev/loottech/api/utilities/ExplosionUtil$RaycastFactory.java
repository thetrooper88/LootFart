package dev.loottech.api.utilities;

import dev.loottech.api.utilities.ExplosionUtil;
import java.util.function.BiFunction;
import net.minecraft.class_2338;
import net.minecraft.class_3965;

@FunctionalInterface
public static interface ExplosionUtil.RaycastFactory
extends BiFunction<ExplosionUtil.ExposureRaycastContext, class_2338, class_3965> {
}
