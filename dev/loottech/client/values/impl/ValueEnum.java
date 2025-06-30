package dev.loottech.client.values.impl;

import dev.loottech.api.manager.Managers;
import dev.loottech.client.events.ClientEvent;
import dev.loottech.client.values.Value;
import dev.loottech.client.values.impl.ValueCategory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Supplier;

public class ValueEnum
extends Value {
    private final Enum defaultValue;
    private Enum value;
    private final ValueCategory parent;

    public ValueEnum(String name, String tag, String description, Enum value) {
        super(name, tag, description, (Supplier<Boolean>)((Supplier)() -> true), null);
        this.defaultValue = value;
        this.value = value;
        this.parent = null;
    }

    public ValueEnum(String name, String tag, String description, ValueCategory parent, Enum value) {
        super(name, tag, description, (Supplier<Boolean>)((Supplier)() -> true), parent);
        this.defaultValue = value;
        this.value = value;
        this.parent = parent;
    }

    public ValueEnum(String name, String description, Enum value) {
        super(name, name, description, (Supplier<Boolean>)((Supplier)() -> true), null);
        this.defaultValue = value;
        this.value = value;
        this.parent = null;
    }

    public ValueEnum(String name, String description, ValueCategory parent, Enum value) {
        super(name, name, description, (Supplier<Boolean>)((Supplier)() -> true), parent);
        this.defaultValue = value;
        this.value = value;
        this.parent = parent;
    }

    public ValueEnum(String name, String description, Enum value, Supplier<Boolean> visible) {
        super(name, name, description, visible, null);
        this.defaultValue = value;
        this.value = value;
        this.parent = null;
    }

    @Override
    public ValueCategory getParent() {
        return this.parent;
    }

    public Enum getDefaultValue() {
        return this.defaultValue;
    }

    public Enum getValue() {
        return this.value;
    }

    public void setValue(Enum value) {
        this.value = value;
        ClientEvent event = new ClientEvent(this);
        Managers.EVENT.call(event);
    }

    public Enum getEnum(String name) {
        for (Enum value : this.getEnums()) {
            if (!value.name().equals((Object)name)) continue;
            return value;
        }
        return null;
    }

    public ArrayList<Enum> getEnums() {
        return new ArrayList((Collection)Arrays.asList((Object[])((Enum[])this.value.getClass().getEnumConstants())));
    }
}
