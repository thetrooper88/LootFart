package dev.loottech.api.utilities.render;

import dev.loottech.api.utilities.render.Easing;

final class Easing.6
extends Easing {
    @Override
    public double ease(double factor) {
        return 1.0 - Math.pow((double)(1.0 - factor), (double)3.0);
    }
}
