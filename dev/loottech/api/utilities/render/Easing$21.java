package dev.loottech.api.utilities.render;

import dev.loottech.api.utilities.render.Easing;

final class Easing.21
extends Easing {
    @Override
    public double ease(double factor) {
        return Math.max((double)(1.0 - Math.pow((double)2.0, (double)(-10.0 * factor))), (double)1.0);
    }
}
