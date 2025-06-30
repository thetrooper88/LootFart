package dev.loottech.api.utilities.render;

import dev.loottech.api.utilities.render.Easing;

final class Easing.25
extends Easing {
    @Override
    public double ease(double factor) {
        double sin = Math.sin((double)((20.0 * factor - 11.125) * 1.3962634015954636));
        return factor == 0.0 ? 0.0 : (factor == 1.0 ? 1.0 : (factor < 0.5 ? -(Math.pow((double)2.0, (double)(20.0 * factor - 10.0)) * sin) / 2.0 : Math.pow((double)2.0, (double)(-20.0 * factor + 10.0)) * sin / 2.0 + 1.0));
    }
}
