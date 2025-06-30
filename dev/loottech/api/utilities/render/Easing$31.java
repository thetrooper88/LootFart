package dev.loottech.api.utilities.render;

import dev.loottech.api.utilities.render.Easing;

final class Easing.31
extends Easing {
    @Override
    public double ease(double factor) {
        return factor < 0.5 ? (1.0 - Easing.bounceOut(1.0 - 2.0 * factor)) / 2.0 : (1.0 + Easing.bounceOut(2.0 * factor - 1.0)) / 2.0;
    }
}
