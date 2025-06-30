package dev.loottech.client.elements;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.element.Element;
import dev.loottech.api.manager.element.RegisterElement;
import dev.loottech.client.elements.Position;
import dev.loottech.client.events.Render2DEvent;
import dev.loottech.client.modules.client.ModuleHUDEditor;
import dev.loottech.client.values.impl.ValueBoolean;
import java.awt.Color;
import net.minecraft.class_408;

@RegisterElement(name="Direction", description="Show the direction you are looking at.")
public class Direction
extends Element {
    ValueBoolean yaw = new ValueBoolean("Yaw", "Yaw", "Show yaw", true);

    @Override
    public void onRender2D(Render2DEvent event) {
        Color col = Managers.MODULE.getInstance(ModuleHUDEditor.class).color.getValue();
        String string = this.getDirectionText();
        this.frame.setHeight(Managers.FONT.getStringHeight(string));
        this.frame.setWidth(Managers.FONT.getStringWidth(string));
        Managers.FONT.drawWithShadow(event.getContext().method_51448(), string, this.frame.getX(), this.isNear(Managers.ELEMENT.getInstance(Position.class)) && !this.isOnTop() && Direction.mc.field_1755 instanceof class_408 ? this.frame.getY() - 14.0f : this.frame.getY(), col.getRGB());
    }

    private String getDirectionText() {
        int yaw = Math.floorMod((int)((int)Direction.mc.field_1724.method_36454()), (int)360);
        if ((double)yaw >= 337.5 || (double)yaw < 22.5) {
            return "South (+Z)";
        }
        if ((double)yaw >= 22.5 && (double)yaw < 67.5) {
            return "SouthWest (-X, +Z)";
        }
        if ((double)yaw >= 67.5 && (double)yaw < 112.5) {
            return "West (-X)";
        }
        if ((double)yaw >= 112.5 && (double)yaw < 157.5) {
            return "NorthWest (-X, -Z)";
        }
        if ((double)yaw >= 157.5 && (double)yaw < 202.5) {
            return "North (-Z)";
        }
        if ((double)yaw >= 202.5 && (double)yaw < 247.5) {
            return "NorthEast (+X, -Z)";
        }
        if ((double)yaw >= 247.5 && (double)yaw < 292.5) {
            return "East (+X)";
        }
        if ((double)yaw >= 292.5 && (double)yaw < 337.5) {
            return "SouthEast (+X, +Z)";
        }
        return "Unknown";
    }
}
