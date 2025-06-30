package dev.loottech.api.utilities.render;

import dev.loottech.api.utilities.render.Easing;

final class Easing.23
extends Easing {
    @Override
    public double ease(double factor) {
        return factor == 0.0 ? 0.0 : (factor == 1.0 ? 1.0 : -Math.pow((double)2.0, (double)(10.0 * factor - 10.0)) * Math.sin((double)((factor * 10.0 - 10.75) * 2.0943951023931953)));
    }
}
