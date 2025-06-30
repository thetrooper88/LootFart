package dev.loottech.api.manager.module;

public static enum Module.Category {
    COMBAT("Combat"),
    PLAYER("Player"),
    MISCELLANEOUS("Miscellaneous"),
    MOVEMENT("Movement"),
    VISUALS("Visuals"),
    CLIENT("Client");

    private final String name;

    private Module.Category(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
