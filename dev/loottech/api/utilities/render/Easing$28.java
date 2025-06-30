package dev.loottech.api.utilities.render;

import dev.loottech.api.utilities.render.Easing;

final class Easing.28
extends Easing {
    @Override
    public double ease(double factor) {
        return factor < 0.5 ? Math.pow((double)(2.0 * factor), (double)2.0) * (7.189819 * factor - 2.5949095) / 2.0 : (Math.pow((double)(2.0 * factor - 2.0), (double)2.0) * (3.5949095 * (factor * 2.0 - 2.0) + 2.5949095) + 2.0) / 2.0;
    }
}
