package dev.loottech.api.utilities.render;

import dev.loottech.api.utilities.render.Easing;

final class Easing.22
extends Easing {
    @Override
    public double ease(double factor) {
        return factor == 0.0 ? 0.0 : (factor == 1.0 ? 1.0 : (factor < 0.5 ? Math.pow((double)2.0, (double)(20.0 * factor - 10.0)) / 2.0 : (2.0 - Math.pow((double)2.0, (double)(-20.0 * factor + 10.0))) / 2.0));
    }
}
