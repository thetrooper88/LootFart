package dev.loottech.api.utilities.render;

import dev.loottech.api.utilities.render.Easing;

final class Easing.2
extends Easing {
    @Override
    public double ease(double factor) {
        return 1.0 - Math.cos((double)(factor * Math.PI / 2.0));
    }
}
