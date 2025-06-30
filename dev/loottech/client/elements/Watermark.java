package dev.loottech.client.elements;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.element.Element;
import dev.loottech.api.manager.element.RegisterElement;
import dev.loottech.client.events.Render2DEvent;
import dev.loottech.client.modules.client.ModuleHUDEditor;
import dev.loottech.client.values.impl.ValueBoolean;
import dev.loottech.client.values.impl.ValueString;

@RegisterElement(name="Watermark", description="Client watermark.")
public class Watermark
extends Element {
    private final ValueBoolean custom = new ValueBoolean("Custom", "Custom", "Custom watermark.", false);
    private final ValueString customString = new ValueString("Value", "Value", "Custom Watermark value", "LootTech");

    @Override
    public void onRender2D(Render2DEvent event) {
        String string = this.custom.getValue() ? this.customString.getValue() : "LootTech Beta 0.73";
        this.frame.setWidth(Managers.FONT.getWidth(string));
        this.frame.setHeight(Managers.FONT.getStringHeight(string));
        Managers.FONT.drawWithShadow(event.getContext().method_51448(), string, this.frame.getX(), this.frame.getY(), Managers.MODULE.getInstance(ModuleHUDEditor.class).color.getValue().getRGB());
        this.snap();
    }
}
