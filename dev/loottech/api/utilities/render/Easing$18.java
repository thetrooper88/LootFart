package dev.loottech.api.utilities.render;

import dev.loottech.api.utilities.render.Easing;

final class Easing.18
extends Easing {
    @Override
    public double ease(double factor) {
        return Math.sqrt((double)(1.0 - Math.pow((double)(factor - 1.0), (double)2.0)));
    }
}
