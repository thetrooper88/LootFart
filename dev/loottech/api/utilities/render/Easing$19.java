package dev.loottech.api.utilities.render;

import dev.loottech.api.utilities.render.Easing;

final class Easing.19
extends Easing {
    @Override
    public double ease(double factor) {
        return factor < 0.5 ? (1.0 - Math.sqrt((double)(1.0 - Math.pow((double)(2.0 * factor), (double)2.0)))) / 2.0 : (Math.sqrt((double)(1.0 - Math.pow((double)(-2.0 * factor + 2.0), (double)2.0))) + 1.0) / 2.0;
    }
}
