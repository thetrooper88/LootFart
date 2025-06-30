package dev.loottech.client.values.impl;

import dev.loottech.api.manager.Managers;
import dev.loottech.client.events.ClientEvent;
import dev.loottech.client.values.Value;
import dev.loottech.client.values.impl.ValueCategory;
import java.util.function.Supplier;

public class ValueNumber
extends Value {
    public static final int INTEGER = 1;
    public static final int DOUBLE = 2;
    public static final int FLOAT = 3;
    private final Number defaultValue;
    private Number value;
    private final Number minimum;
    private final Number maximum;
    private final ValueCategory parent;

    public ValueNumber(String name, String tag, String description, Number value, Number minimum, Number maximum) {
        super(name, tag, description, (Supplier<Boolean>)((Supplier)() -> true), null);
        this.defaultValue = value;
        this.value = value;
        this.minimum = minimum;
        this.maximum = maximum;
        this.parent = null;
    }

    public ValueNumber(String name, String description, Number value, Number minimum, Number maximum) {
        super(name, name, description, (Supplier<Boolean>)((Supplier)() -> true), null);
        this.defaultValue = value;
        this.value = value;
        this.minimum = minimum;
        this.maximum = maximum;
        this.parent = null;
    }

    public ValueNumber(String name, String description, ValueCategory parent, Number value, Number minimum, Number maximum) {
        super(name, name, description, (Supplier<Boolean>)((Supplier)() -> true), parent);
        this.defaultValue = value;
        this.value = value;
        this.minimum = minimum;
        this.maximum = maximum;
        this.parent = parent;
    }

    public ValueNumber(String name, String tag, String description, ValueCategory parent, Number value, Number minimum, Number maximum) {
        super(name, tag, description, (Supplier<Boolean>)((Supplier)() -> true), parent);
        this.defaultValue = value;
        this.value = value;
        this.minimum = minimum;
        this.maximum = maximum;
        this.parent = parent;
    }

    public ValueNumber(String name, String description, Number value, Number minimum, Number maximum, Supplier<Boolean> visible) {
        super(name, name, description, visible, null);
        this.defaultValue = value;
        this.value = value;
        this.minimum = minimum;
        this.maximum = maximum;
        this.parent = null;
    }

    @Override
    public ValueCategory getParent() {
        return this.parent;
    }

    public Number getDefaultValue() {
        return this.defaultValue;
    }

    public Number getValue() {
        return this.value;
    }

    public <T extends Number> T getLiteralValue() {
        if (this.value.getClass() == Integer.class) {
            return (T)Integer.valueOf((int)this.value.intValue());
        }
        if (this.value.getClass() == Double.class) {
            return (T)Double.valueOf((double)this.value.doubleValue());
        }
        if (this.value.getClass() == Float.class) {
            return (T)Float.valueOf((float)this.value.floatValue());
        }
        throw new IllegalArgumentException("Unsupported number type: " + String.valueOf((Object)this.value.getClass()));
    }

    public void setValue(Number value) {
        this.value = value;
        ClientEvent event = new ClientEvent(this);
        Managers.EVENT.call(event);
    }

    public Number getMaximum() {
        return this.maximum;
    }

    public Number getMinimum() {
        return this.minimum;
    }

    public int getType() {
        if (this.value.getClass() == Integer.class) {
            return 1;
        }
        if (this.value.getClass() == Double.class) {
            return 2;
        }
        if (this.value.getClass() == Float.class) {
            return 3;
        }
        return -1;
    }
}
