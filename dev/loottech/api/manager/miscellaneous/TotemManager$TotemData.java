package dev.loottech.api.manager.miscellaneous;

public static class TotemManager.TotemData {
    private final long lastPopTime;
    private final int pops;

    public TotemManager.TotemData(long lastPopTime, int pops) {
        this.lastPopTime = lastPopTime;
        this.pops = pops;
    }

    public int getPops() {
        return this.pops;
    }

    public long getLastPopTime() {
        return this.lastPopTime;
    }
}
