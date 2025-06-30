package dev.loottech.api.utilities.render;

import dev.loottech.api.utilities.render.Easing;

final class Easing.3
extends Easing {
    @Override
    public double ease(double factor) {
        return Math.sin((double)(factor * Math.PI / 2.0));
    }
}
