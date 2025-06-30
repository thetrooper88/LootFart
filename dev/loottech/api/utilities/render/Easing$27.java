package dev.loottech.api.utilities.render;

import dev.loottech.api.utilities.render.Easing;

final class Easing.27
extends Easing {
    @Override
    public double ease(double factor) {
        double c1 = 1.70158;
        double c3 = c1 + 1.0;
        return 1.0 + c3 * Math.pow((double)(factor - 1.0), (double)3.0) + c1 * Math.pow((double)(factor - 1.0), (double)2.0);
    }
}
