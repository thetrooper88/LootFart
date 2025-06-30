package dev.loottech.api.utilities.render;

import dev.loottech.api.utilities.render.Easing;

final class Easing.17
extends Easing {
    @Override
    public double ease(double factor) {
        return 1.0 - Math.sqrt((double)(1.0 - Math.pow((double)factor, (double)2.0)));
    }
}
