package dev.loottech.client.elements;

import dev.loottech.api.manager.element.Element;
import dev.loottech.api.manager.element.RegisterElement;
import dev.loottech.client.events.Render2DEvent;
import dev.loottech.client.values.impl.ValueNumber;
import net.minecraft.class_2960;
import net.minecraft.class_332;

@RegisterElement(name="Logo", description="Show the client logo.")
public class Logo
extends Element {
    ValueNumber scale = new ValueNumber("Scale", "Scale of the logo.", (Number)Float.valueOf((float)1.0f), (Number)Float.valueOf((float)0.1f), (Number)Float.valueOf((float)5.0f));

    @Override
    public void onRender2D(Render2DEvent event) {
        int x = (int)this.frame.getX();
        int y = (int)this.frame.getY();
        class_332 context = event.getContext();
        class_2960 id = class_2960.method_60655((String)"loottech", (String)"logo.png");
        context.method_25290(id, x, y - 11, 0.0f, 0.0f, (int)(100.0f * this.scale.getValue().floatValue()), (int)(64.0f * this.scale.getValue().floatValue()), (int)(100.0f * this.scale.getValue().floatValue()), (int)(64.0f * this.scale.getValue().floatValue()));
        this.frame.setWidth(100.0f * this.scale.getValue().floatValue());
        this.frame.setHeight(64.0f * this.scale.getValue().floatValue());
    }
}
