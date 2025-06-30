package dev.loottech.client.elements;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.element.Element;
import dev.loottech.api.manager.element.RegisterElement;
import dev.loottech.api.utilities.ColorUtils;
import dev.loottech.client.events.Render2DEvent;
import dev.loottech.client.values.impl.ValueBoolean;
import net.minecraft.class_1297;
import net.minecraft.class_1799;

@RegisterElement(name="Armor", description="Show the player's armor info.")
public class Armor
extends Element {
    ValueBoolean durability = new ValueBoolean("Durability", "Durability", "Show armor durability.", true);

    @Override
    public void onRender2D(Render2DEvent event) {
        class_1297 riding = Armor.mc.field_1724.method_5854();
        float x = this.frame.getX();
        float y = this.frame.getY();
        for (int i = 3; i >= 0; --i) {
            class_1799 armor = (class_1799)Armor.mc.field_1724.method_31548().field_7548.get(i);
            int f = armor.method_7936();
            int f2 = armor.method_7919();
            event.getContext().method_51427(armor, (int)x, (int)y);
            event.getContext().method_51431(Armor.mc.field_1772, armor, (int)x, (int)y);
            if (this.durability.getValue() && !armor.method_7960()) {
                event.getContext().method_51448().method_22905(0.65f, 0.65f, 1.0f);
                Managers.FONT.drawWithShadow(event.getContext().method_51448(), Math.round((float)((float)(f - f2) / (float)f * 100.0f)) + "%", (x + 2.0f) * 1.5384616f, (y - 5.0f) * 1.5384616f, ColorUtils.hslToColor((float)(f - f2) / (float)f * 120.0f, 100.0f, 50.0f, 1.0f).getRGB());
                event.getContext().method_51448().method_22905(1.5384616f, 1.5384616f, 1.0f);
            }
            x += 18.0f;
        }
        this.frame.setHeight(20.0f);
        this.frame.setWidth(70.0f);
    }
}
