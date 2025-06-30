package dev.loottech.client.values.impl;

import dev.loottech.client.values.Value;
import java.util.function.Supplier;

public class ValueCategory
extends Value {
    private boolean open = false;

    public ValueCategory(String name, String description) {
        super(name, name, description, (Supplier<Boolean>)((Supplier)() -> true), null);
    }

    public boolean isOpen() {
        return this.open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}
