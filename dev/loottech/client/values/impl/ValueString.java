package dev.loottech.client.values.impl;

import dev.loottech.api.manager.Managers;
import dev.loottech.client.events.ClientEvent;
import dev.loottech.client.values.Value;
import dev.loottech.client.values.impl.ValueCategory;
import java.util.function.Supplier;

public class ValueString
extends Value {
    private final String defaultValue;
    private String value;
    private final ValueCategory parent;

    public ValueString(String name, String tag, String description, String value) {
        super(name, tag, description, (Supplier<Boolean>)((Supplier)() -> true), null);
        this.defaultValue = value;
        this.value = value;
        this.parent = null;
    }

    public ValueString(String name, String tag, String description, ValueCategory parent, String value) {
        super(name, tag, description, (Supplier<Boolean>)((Supplier)() -> true), parent);
        this.defaultValue = value;
        this.value = value;
        this.parent = parent;
    }

    public ValueString(String name, String tag, String description, String value, Supplier<Boolean> visible) {
        super(name, tag, description, visible, null);
        this.defaultValue = value;
        this.value = value;
        this.parent = null;
    }

    @Override
    public ValueCategory getParent() {
        return this.parent;
    }

    public String getDefaultValue() {
        return this.defaultValue;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
        ClientEvent event = new ClientEvent(this);
        Managers.EVENT.call(event);
    }
}
