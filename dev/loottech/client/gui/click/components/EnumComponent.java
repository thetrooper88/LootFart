package dev.loottech.client.gui.click.components;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.utilities.render.RenderUtils;
import dev.loottech.client.gui.click.manage.Component;
import dev.loottech.client.gui.click.manage.Frame;
import dev.loottech.client.values.impl.ValueEnum;
import java.util.function.Supplier;
import net.minecraft.class_124;
import net.minecraft.class_332;

public class EnumComponent
extends Component {
    private final ValueEnum value;
    private int enumSize;

    public EnumComponent(ValueEnum value, int offset, Frame parent, Supplier<Boolean> visible) {
        super(offset, parent, visible);
        this.value = value;
    }

    @Override
    public void render(class_332 context, int mouseX, int mouseY, float partialTicks) {
        RenderUtils.drawRect(context.method_51448(), this.getX() + 1, this.getY(), this.getX() + this.getWidth() - 1, this.getY() + 14, Managers.CLICK_GUI.getColor());
        Managers.FONT.drawWithShadow(context.method_51448(), this.value.getTag() + " " + String.valueOf((Object)class_124.field_1080) + this.value.getValue().toString(), (float)(this.getX() + 3), (float)(this.getY() + 3), -1);
    }

    @Override
    public void update(int mouseX, int mouseY, float partialTicks) {
        super.update(mouseX, mouseY, partialTicks);
        if (this.value.getParent() != null) {
            this.setVisible(this.value.getParent().isOpen());
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseX >= this.getX() && mouseX <= this.getX() + this.getWidth() && mouseY >= this.getY() && mouseY <= this.getY() + this.getHeight()) {
            if (mouseButton == 0) {
                int maxIndex = this.value.getEnums().size() - 1;
                ++this.enumSize;
                if (this.enumSize > maxIndex) {
                    this.enumSize = 0;
                }
                this.value.setValue((Enum)this.value.getEnums().get(this.enumSize));
            } else if (mouseButton == 1) {
                int maxIndex = this.value.getEnums().size() - 1;
                --this.enumSize;
                if (this.enumSize < 0) {
                    this.enumSize = maxIndex;
                }
                this.value.setValue((Enum)this.value.getEnums().get(this.enumSize));
            }
        }
    }

    public ValueEnum getValue() {
        return this.value;
    }
}
