package dev.loottech.api.utilities.render;

import dev.loottech.api.utilities.render.Easing;

final class Easing.9
extends Easing {
    @Override
    public double ease(double factor) {
        return 1.0 - (1.0 - factor) * (1.0 - factor);
    }
}
