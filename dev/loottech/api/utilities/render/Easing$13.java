package dev.loottech.api.utilities.render;

import dev.loottech.api.utilities.render.Easing;

final class Easing.13
extends Easing {
    @Override
    public double ease(double factor) {
        return factor < 0.5 ? 8.0 * Math.pow((double)factor, (double)4.0) : 1.0 - Math.pow((double)(-2.0 * factor + 2.0), (double)4.0) / 2.0;
    }
}
