package dev.loottech.client.values.impl;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.utilities.ColorUtils;
import dev.loottech.client.events.ClientEvent;
import dev.loottech.client.modules.client.ModuleColor;
import dev.loottech.client.values.Value;
import dev.loottech.client.values.impl.ValueCategory;
import java.awt.Color;
import java.util.function.Supplier;

public class ValueColor
extends Value {
    private final Color defaultValue;
    private Color value;
    private boolean rainbow;
    private boolean sync;
    private final ValueCategory parent;

    public ValueColor(String name, String tag, String description, Color value) {
        super(name, tag, description, (Supplier<Boolean>)((Supplier)() -> true), null);
        this.defaultValue = value;
        this.value = value;
        this.parent = null;
        this.rainbow = false;
        this.sync = false;
    }

    public ValueColor(String name, String tag, String description, Color value, boolean rainbow, boolean sync) {
        super(name, tag, description, (Supplier<Boolean>)((Supplier)() -> true), null);
        this.defaultValue = value;
        this.value = value;
        this.parent = null;
        this.rainbow = rainbow;
        this.sync = sync;
    }

    public ValueColor(String name, String tag, String description, Color value, boolean rainbow, boolean sync, Supplier<Boolean> visible) {
        super(name, tag, description, visible, null);
        this.defaultValue = value;
        this.value = value;
        this.parent = null;
        this.rainbow = rainbow;
        this.sync = sync;
    }

    public ValueColor(String name, String tag, String description, ValueCategory parent, Color value, boolean rainbow, boolean sync) {
        super(name, tag, description, (Supplier<Boolean>)((Supplier)() -> true), parent);
        this.defaultValue = value;
        this.value = value;
        this.parent = parent;
        this.rainbow = rainbow;
        this.sync = sync;
    }

    public ValueColor(String name, String tag, String description, ValueCategory parent, Color value) {
        super(name, tag, description, (Supplier<Boolean>)((Supplier)() -> true), parent);
        this.defaultValue = value;
        this.value = value;
        this.parent = parent;
        this.rainbow = false;
        this.sync = false;
    }

    public ValueColor(String name, String description, Color value, Supplier<Boolean> visible) {
        super(name, name, description, visible, null);
        this.defaultValue = value;
        this.value = value;
        this.parent = null;
        this.rainbow = false;
        this.sync = false;
    }

    @Override
    public ValueCategory getParent() {
        return this.parent;
    }

    public Color getDefaultValue() {
        return this.defaultValue;
    }

    public Color getActualValue() {
        return this.value;
    }

    public Color getValue() {
        if (this.sync && this != ModuleColor.INSTANCE.color) {
            return new Color(ModuleColor.getColor().getRed(), ModuleColor.getColor().getGreen(), ModuleColor.getColor().getBlue(), this.value.getAlpha());
        }
        this.doRainbow();
        return this.value;
    }

    public Color getValue(int alpha) {
        return new Color(this.getValue().getRed(), this.getValue().getGreen(), this.getValue().getBlue(), alpha);
    }

    public void setValue(Color value) {
        this.value = value;
        ClientEvent event = new ClientEvent(this);
        Managers.EVENT.call(event);
    }

    public boolean isRainbow() {
        return this.rainbow;
    }

    public void setRainbow(boolean rainbow) {
        this.rainbow = rainbow;
    }

    public boolean isSync() {
        return this.sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }

    private void doRainbow() {
        if (this.rainbow) {
            Color rainbowColor = ColorUtils.rainbow(1);
            this.setValue(new Color(rainbowColor.getRed(), rainbowColor.getGreen(), rainbowColor.getBlue(), this.value.getAlpha()));
        }
    }
}
