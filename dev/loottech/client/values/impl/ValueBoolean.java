package dev.loottech.client.values.impl;

import dev.loottech.api.manager.Managers;
import dev.loottech.client.events.ClientEvent;
import dev.loottech.client.values.Value;
import dev.loottech.client.values.impl.ValueCategory;
import java.util.function.Supplier;

public class ValueBoolean
extends Value {
    private final boolean defaultValue;
    private boolean value;
    private final ValueCategory parent;

    public ValueBoolean(String name, String description, boolean defaultValue, ValueCategory parent) {
        super(name, name, description, (Supplier<Boolean>)((Supplier)() -> true), parent);
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        this.parent = parent;
    }

    public ValueBoolean(String name, String description, boolean defaultValue) {
        super(name, name, description, (Supplier<Boolean>)((Supplier)() -> true), null);
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        this.parent = null;
    }

    public ValueBoolean(String name, String description, ValueCategory parent, boolean defaultValue) {
        super(name, name, description, (Supplier<Boolean>)((Supplier)() -> true), parent);
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        this.parent = parent;
    }

    public ValueBoolean(String name, String tag, String description, boolean value) {
        super(name, tag, description, (Supplier<Boolean>)((Supplier)() -> true), null);
        this.defaultValue = value;
        this.value = value;
        this.parent = null;
    }

    public ValueBoolean(String name, String tag, String description, ValueCategory parent, boolean value) {
        super(name, tag, description, (Supplier<Boolean>)((Supplier)() -> true), parent);
        this.defaultValue = value;
        this.value = value;
        this.parent = parent;
    }

    public ValueBoolean(String name, String desciption, boolean value, Supplier<Boolean> visible) {
        super(name, name, desciption, visible, null);
        this.defaultValue = value;
        this.value = value;
        this.parent = null;
    }

    @Override
    public ValueCategory getParent() {
        return this.parent;
    }

    public boolean getDefaultValue() {
        return this.defaultValue;
    }

    public boolean getValue() {
        return this.value;
    }

    public void setValue(boolean value) {
        this.value = value;
        ClientEvent event = new ClientEvent(this);
        Managers.EVENT.call(event);
    }
}
