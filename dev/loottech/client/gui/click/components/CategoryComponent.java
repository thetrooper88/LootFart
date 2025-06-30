package dev.loottech.client.gui.click.components;

import dev.loottech.api.manager.Managers;
import dev.loottech.client.gui.click.manage.Component;
import dev.loottech.client.gui.click.manage.Frame;
import dev.loottech.client.values.impl.ValueCategory;
import java.util.function.Supplier;
import net.minecraft.class_332;

public class CategoryComponent
extends Component {
    private final ValueCategory value;

    public CategoryComponent(ValueCategory value, int offset, Frame parent, Supplier<Boolean> visible) {
        super(offset, parent, visible);
        this.value = value;
    }

    @Override
    public void render(class_332 context, int mouseX, int mouseY, float partialTicks) {
        Managers.FONT.drawWithShadow(context.method_51448(), this.value.getName(), (float)(this.getX() + 3), (float)(this.getY() + 3), -1);
        Managers.FONT.drawWithShadow(context.method_51448(), this.value.isOpen() ? "-" : "+", (float)((int)((float)(this.getX() + this.getWidth() - 3) - Managers.FONT.getWidth(this.value.isOpen() ? "+" : "-"))), (float)(this.getY() + 3), -1);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (this.isHovering(mouseX, mouseY) && mouseButton == 1) {
            this.value.setOpen(!this.value.isOpen());
            this.getParent().refresh();
        }
    }
}
