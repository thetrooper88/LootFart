package dev.loottech.api.utilities.render;

import dev.loottech.api.utilities.render.Easing;

final class Easing.29
extends Easing {
    @Override
    public double ease(double factor) {
        return 1.0 - Easing.bounceOut(1.0 - factor);
    }
}
