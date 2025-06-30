package dev.loottech.api.utilities.render;

import dev.loottech.api.utilities.render.Easing;

final class Easing.26
extends Easing {
    @Override
    public double ease(double factor) {
        return 2.70158 * Math.pow((double)factor, (double)3.0) - 1.70158 * factor * factor;
    }
}
