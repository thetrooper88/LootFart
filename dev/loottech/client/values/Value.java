package dev.loottech.client.values;

import dev.loottech.client.values.impl.ValueCategory;
import java.util.function.Supplier;

public class Value {
    private final String name;
    private final String tag;
    private final String description;
    private final Supplier<Boolean> visible;
    private final ValueCategory parent;

    public Value(String name, String tag, String description, Supplier<Boolean> visible, ValueCategory parent) {
        this.name = name;
        this.tag = tag;
        this.description = description;
        this.visible = visible;
        this.parent = parent;
    }

    public boolean isVisible() {
        return this.visible == null || (Boolean)this.visible.get() != false;
    }

    public String getName() {
        return this.name;
    }

    public String getTag() {
        return this.tag;
    }

    public String getDescription() {
        return this.description;
    }

    public ValueCategory getParent() {
        return this.parent;
    }
}
