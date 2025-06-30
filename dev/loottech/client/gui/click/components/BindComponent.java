package dev.loottech.client.gui.click.components;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.event.EventListener;
import dev.loottech.client.events.KeyEvent;
import dev.loottech.client.gui.click.manage.Component;
import dev.loottech.client.gui.click.manage.Frame;
import dev.loottech.client.values.impl.ValueBind;
import java.util.function.Supplier;
import net.minecraft.class_124;
import net.minecraft.class_332;
import org.lwjgl.glfw.GLFW;

public class BindComponent
extends Component
implements EventListener {
    private final ValueBind value;
    private boolean binding;

    public BindComponent(ValueBind value, int offset, Frame parent, Supplier<Boolean> visible) {
        super(offset, parent, visible);
        Managers.EVENT.register(this);
        this.value = value;
    }

    @Override
    public void render(class_332 context, int mouseX, int mouseY, float partialTicks) {
        String keyName;
        int code = this.value.getValue();
        if (this.binding) {
            keyName = "...";
        } else if (code == 0) {
            keyName = "NONE";
        } else if (code >= 32) {
            keyName = GLFW.glfwGetKeyName((int)code, (int)0);
            if (keyName == null) {
                keyName = "KEY_" + code;
            }
        } else {
            keyName = switch (code) {
                case 0 -> "MOUSE1";
                case 1 -> "MOUSE2";
                case 2 -> "MOUSE3";
                case 3 -> "MOUSE4";
                case 4 -> "MOUSE5";
                default -> "MOUSE" + (code + 1);
            };
        }
        Managers.FONT.drawWithShadow(context.method_51448(), this.value.getTag() + " " + String.valueOf((Object)class_124.field_1080) + keyName, (float)(this.getX() + 3), (float)(this.getY() + 3), -1);
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
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.isHovering(mouseX, mouseY)) {
            if (!this.binding) {
                this.binding = true;
            } else {
                this.value.setValue(mouseButton);
                this.binding = false;
            }
        }
    }

    @Override
    public void onKey(KeyEvent event) {
        if (this.binding) {
            int key = event.getKeyCode();
            if (key == 261 || key == 259) {
                this.value.setValue(0);
            } else if (key != 256) {
                this.value.setValue(key);
            }
            this.binding = false;
        }
    }
}
