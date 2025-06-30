package dev.loottech.api.utilities.render;

import dev.loottech.api.utilities.render.Easing;

final class Easing.5
extends Easing {
    @Override
    public double ease(double factor) {
        return Math.pow((double)factor, (double)3.0);
    }
}
