package dev.loottech.api.utilities.render;

import dev.loottech.api.utilities.render.Easing;

final class Easing.24
extends Easing {
    @Override
    public double ease(double factor) {
        return factor == 0.0 ? 0.0 : (factor == 1.0 ? 1.0 : Math.pow((double)2.0, (double)(-10.0 * factor)) * Math.sin((double)((factor * 10.0 - 0.75) * 2.0943951023931953)) + 1.0);
    }
}
