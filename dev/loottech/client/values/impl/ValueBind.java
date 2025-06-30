package dev.loottech.client.values.impl;

import dev.loottech.client.values.Value;
import dev.loottech.client.values.impl.ValueCategory;
import java.util.function.Supplier;

public class ValueBind
extends Value {
    private final int defaultValue;
    private int value;
    private final ValueCategory parent;

    public ValueBind(String name, String tag, String description, int value) {
        super(name, tag, description, (Supplier<Boolean>)((Supplier)() -> true), null);
        this.defaultValue = value;
        this.value = value;
        this.parent = null;
    }

    public ValueBind(String name, String tag, String description, ValueCategory parent, int value) {
        super(name, tag, description, (Supplier<Boolean>)((Supplier)() -> true), parent);
        this.defaultValue = value;
        this.value = value;
        this.parent = parent;
    }

    @Override
    public ValueCategory getParent() {
        return this.parent;
    }

    public int getDefaultValue() {
        return this.defaultValue;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
