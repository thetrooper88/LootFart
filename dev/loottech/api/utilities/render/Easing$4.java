package dev.loottech.api.utilities.render;

import dev.loottech.api.utilities.render.Easing;

final class Easing.4
extends Easing {
    @Override
    public double ease(double factor) {
        return -(Math.cos((double)(Math.PI * factor)) - 1.0) / 2.0;
    }
}
