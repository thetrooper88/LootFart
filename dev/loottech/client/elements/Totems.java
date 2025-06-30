package dev.loottech.client.elements;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.element.Element;
import dev.loottech.api.manager.element.RegisterElement;
import dev.loottech.api.utilities.InventoryUtils;
import dev.loottech.api.utilities.render.RenderManager;
import dev.loottech.client.events.Render2DEvent;
import dev.loottech.client.modules.client.ModuleHUDEditor;
import dev.loottech.client.values.impl.ValueBoolean;
import dev.loottech.client.values.impl.ValueNumber;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1935;
import net.minecraft.class_332;

@RegisterElement(name="Totems", description="Display how many totems u have.")
public class Totems
extends Element {
    public ValueBoolean whiteCol = new ValueBoolean("White", "White", "Make the number color white.", true);
    public ValueNumber scale = new ValueNumber("Scale", "Scale of the totem and text.", (Number)Float.valueOf((float)1.0f), (Number)Float.valueOf((float)0.25f), (Number)Float.valueOf((float)3.0f));

    @Override
    public void onRender2D(Render2DEvent event) {
        float scaleValue = this.scale.getValue().floatValue();
        float x = this.frame.getX();
        float y = this.frame.getY();
        class_332 context = new class_332(mc, mc.method_22940().method_23000());
        String totems = String.valueOf((int)InventoryUtils.count(class_1802.field_8288));
        context.method_51448().method_22903();
        context.method_51448().method_22905(scaleValue, scaleValue, 1.0f);
        float scaledX = x / scaleValue;
        float scaledY = y / scaleValue;
        context.method_51427(new class_1799((class_1935)class_1802.field_8288), (int)scaledX, (int)(scaledY - 1.0f));
        context.method_51448().method_46416(-3.0f, 0.0f, 200.0f);
        RenderManager.renderText(context, totems, scaledX + 19.0f - RenderManager.textWidth(totems), scaledY + 9.0f, this.whiteCol.getValue() ? -1 : Managers.MODULE.getInstance(ModuleHUDEditor.class).color.getValue().getRGB());
        context.method_51448().method_46416(3.0f, 0.0f, -200.0f);
        context.method_51448().method_22909();
        this.frame.setHeight(15.0f * scaleValue);
        this.frame.setWidth(15.0f * scaleValue);
    }
}
