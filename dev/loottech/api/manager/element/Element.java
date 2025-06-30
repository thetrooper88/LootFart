package dev.loottech.api.manager.element;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.element.RegisterElement;
import dev.loottech.api.manager.module.Module;
import dev.loottech.client.gui.hud.ElementFrame;
import dev.loottech.client.values.Value;
import java.util.ArrayList;
import net.minecraft.class_310;

public abstract class Element
extends Module {
    protected static final class_310 mc = class_310.method_1551();
    private final ArrayList<Value> values;
    public ElementFrame frame;

    public Element() {
        RegisterElement annotation = (RegisterElement)this.getClass().getAnnotation(RegisterElement.class);
        this.name = annotation.name();
        this.tag.setValue(annotation.tag().equals((Object)"4GquuoBHl7gkSDaNeMb5") ? annotation.name() : annotation.tag());
        this.description = annotation.description();
        this.values = new ArrayList();
    }

    public void snap() {
        float frameHalfWidth = this.frame.getWidth() / 2.0f;
        int screenHalfWidth = mc.method_22683().method_4486() / 2;
        if (Math.abs((float)((float)screenHalfWidth - (this.frame.getX() + frameHalfWidth))) <= 8.0f) {
            this.frame.setX((float)screenHalfWidth - frameHalfWidth);
        }
    }

    public void setFrame(ElementFrame frame) {
        this.frame = frame;
    }

    public float align(String string) {
        return this.isOnRight() ? this.frame.getX() + this.frame.getWidth() - Managers.FONT.getWidth(string) : this.frame.getX();
    }

    public boolean isSnapped() {
        float frameHalfWidth = this.frame.getWidth() / 2.0f;
        int screenHalfWidth = mc.method_22683().method_4486() / 2;
        return Math.abs((float)((float)screenHalfWidth - (this.frame.getX() + frameHalfWidth))) <= 8.0f;
    }

    public boolean isOnRight() {
        return this.frame.getX() >= (float)mc.method_22683().method_4486() / 2.0f;
    }

    public boolean isOnTop() {
        return this.frame.getY() <= (float)mc.method_22683().method_4502() / 2.0f;
    }

    public boolean isNear(Element element) {
        return Math.abs((float)(this.frame.getX() - element.frame.getX())) <= 20.0f && Math.abs((float)(this.frame.getY() - element.frame.getY())) <= 20.0f;
    }

    @Override
    public ArrayList<Value> getValues() {
        return this.values;
    }
}
