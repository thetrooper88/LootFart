package dev.loottech.client.elements;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.element.Element;
import dev.loottech.api.manager.element.RegisterElement;
import dev.loottech.client.events.Render2DEvent;
import dev.loottech.client.modules.client.ModuleHUDEditor;
import java.awt.Color;
import net.minecraft.class_1937;
import net.minecraft.class_408;

@RegisterElement(name="Position", description="Show player position.")
public class Position
extends Element {
    @Override
    public void onRender2D(Render2DEvent e) {
        Color col = Managers.MODULE.getInstance(ModuleHUDEditor.class).color.getValue();
        Color w = Color.WHITE;
        String string = String.format((String)"XYZ: %s, %s, %s [%s, %s]", (Object[])new Object[]{Math.round((double)Position.mc.field_1724.method_23317()), Math.round((double)Position.mc.field_1724.method_23318()), Math.round((double)Position.mc.field_1724.method_23321()), this.getOppositeX(), this.getOppositeZ()});
        String string1 = "XYZ: ";
        String string2 = "" + Math.round((double)Position.mc.field_1724.method_23317());
        String string3 = ", ";
        String string4 = "" + Math.round((double)Position.mc.field_1724.method_23318());
        String string5 = ", ";
        String string6 = Math.round((double)Position.mc.field_1724.method_23321()) + " ";
        String string7 = "[";
        String string8 = "" + this.getOppositeX();
        String string9 = ", ";
        String string10 = "" + this.getOppositeZ();
        String string11 = "]";
        float x1 = 0.0f;
        float x2 = x1 + Managers.FONT.getWidth(string1);
        float x3 = x2 + Managers.FONT.getWidth(string2);
        float x4 = x3 + Managers.FONT.getWidth(string3);
        float x5 = x4 + Managers.FONT.getWidth(string4);
        float x6 = x5 + Managers.FONT.getWidth(string5);
        float x7 = x6 + Managers.FONT.getWidth(string6);
        float x8 = x7 + Managers.FONT.getWidth(string7);
        float x9 = x8 + Managers.FONT.getWidth(string8);
        float x10 = x9 + Managers.FONT.getWidth(string9);
        float x11 = x10 + Managers.FONT.getWidth(string10);
        this.frame.setWidth(Managers.FONT.getWidth(string));
        this.frame.setHeight(Managers.FONT.getStringHeight(string));
        this.draw(string1, e, x1, col);
        this.draw(string2, e, x2, w);
        this.draw(string3, e, x3, col);
        this.draw(string4, e, x4, w);
        this.draw(string5, e, x5, col);
        this.draw(string6, e, x6, w);
        this.draw(string7, e, x7, col);
        this.draw(string8, e, x8, w);
        this.draw(string9, e, x9, col);
        this.draw(string10, e, x10, w);
        this.draw(string11, e, x11, col);
    }

    private int getOppositeX() {
        if (Position.mc.field_1687.method_27983() == class_1937.field_25179) {
            return (int)Math.round((double)(Position.mc.field_1724.method_23317() / 8.0));
        }
        return (int)Math.round((double)(Position.mc.field_1724.method_23317() * 8.0));
    }

    private int getOppositeZ() {
        if (Position.mc.field_1687.method_27983() == class_1937.field_25179) {
            return (int)Math.round((double)(Position.mc.field_1724.method_23321() / 8.0));
        }
        return (int)Math.round((double)(Position.mc.field_1724.method_23321() * 8.0));
    }

    private void draw(String s, Render2DEvent e, float x, Color c) {
        Managers.FONT.drawWithShadow(e.getContext().method_51448(), s, this.frame.getX() + x, this.frame.getY() + this.getChatOffset(), c.getRGB());
    }

    private float getChatOffset() {
        if (Position.mc.field_1755 instanceof class_408 && !this.isOnTop()) {
            return -14.0f;
        }
        return 0.0f;
    }
}
