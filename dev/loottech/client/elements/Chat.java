package dev.loottech.client.elements;

import dev.loottech.api.manager.element.Element;
import dev.loottech.api.manager.element.RegisterElement;
import dev.loottech.client.events.Render3DEvent;
import net.minecraft.class_408;

@RegisterElement(name="Chat", description="Change chat position.")
public class Chat
extends Element {
    public float x;
    public float y;

    @Override
    public void onRender3D(Render3DEvent event) {
        this.x = this.frame.getX();
        this.y = !this.isOnTop() && Chat.mc.field_1755 instanceof class_408 ? this.frame.getY() - 14.0f : this.frame.getY();
        this.frame.setHeight(90.0f);
        this.frame.setWidth(300.0f);
    }
}
