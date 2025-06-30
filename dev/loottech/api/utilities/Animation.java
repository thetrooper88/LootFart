package dev.loottech.api.utilities;

import dev.loottech.api.utilities.render.Easing;

public class Animation {
    private final Easing easing;
    private float length;
    private long last = 0L;
    private boolean state;

    public Animation(float length) {
        this(false, length);
    }

    public Animation(boolean initial, float length) {
        this(initial, length, Easing.LINEAR);
    }

    public Animation(boolean initial, float length, Easing easing) {
        this.length = length;
        this.state = initial;
        this.easing = easing;
    }

    public boolean getState() {
        return this.state;
    }

    public void setState(boolean state) {
        this.last = (long)(!state ? (double)System.currentTimeMillis() - (1.0 - this.getFactor()) * (double)this.length : (double)System.currentTimeMillis() - this.getFactor() * (double)this.length);
        this.state = state;
    }

    public double getFactor() {
        return this.easing.ease(this.getLinearFactor());
    }

    public double getLinearFactor() {
        return this.state ? this.clamp((float)(System.currentTimeMillis() - this.last) / this.length) : this.clamp(1.0f - (float)(System.currentTimeMillis() - this.last) / this.length);
    }

    public double getCurrent() {
        return 1.0 + 1.0 * this.getFactor();
    }

    private double clamp(double in) {
        return in < 0.0 ? 0.0 : Math.min((double)in, (double)1.0);
    }

    public double getLength() {
        return this.length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public boolean isFinished() {
        return !this.getState() && this.getFactor() == 0.0 || this.getState() && this.getFactor() == 1.0;
    }

    public void reset() {
        this.last = System.currentTimeMillis();
    }
}
