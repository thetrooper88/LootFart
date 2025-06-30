package dev.loottech.api.utilities.render;

import dev.loottech.api.utilities.render.Easing;

final class Easing.15
extends Easing {
    @Override
    public double ease(double factor) {
        return 1.0 - Math.pow((double)(1.0 - factor), (double)5.0);
    }
}
