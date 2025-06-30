package dev.loottech.api.utilities.render;

import dev.loottech.api.utilities.render.Easing;

final class Easing.30
extends Easing {
    @Override
    public double ease(double factor) {
        return Easing.bounceOut(factor);
    }
}
