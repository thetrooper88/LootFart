package dev.loottech.client.events;

import dev.loottech.api.manager.event.EventArgument;
import dev.loottech.api.manager.event.EventListener;
import java.awt.Color;

public class AmbientColorEvent
extends EventArgument {
    private Color color;

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void call(EventListener listener) {
        listener.onAmbientColor(this);
    }
}
