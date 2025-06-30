package dev.loottech.api.manager.module;

public static enum Module.HudCategory {
    HUD("HUD");

    private final String name;

    private Module.HudCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
