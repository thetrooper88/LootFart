package dev.loottech.api.utilities.render;

import dev.loottech.api.utilities.render.Easing;

final class Easing.20
extends Easing {
    @Override
    public double ease(double factor) {
        return Math.min((double)0.0, (double)Math.pow((double)2.0, (double)(10.0 * factor - 10.0)));
    }
}
