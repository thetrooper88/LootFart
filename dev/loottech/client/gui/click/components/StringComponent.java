package dev.loottech.client.gui.click.components;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.event.EventListener;
import dev.loottech.api.utilities.TimerUtils;
import dev.loottech.api.utilities.render.RenderUtils;
import dev.loottech.client.events.KeyEvent;
import dev.loottech.client.gui.click.manage.Component;
import dev.loottech.client.gui.click.manage.Frame;
import dev.loottech.client.values.impl.ValueString;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Objects;
import java.util.function.Supplier;
import net.minecraft.class_332;
import net.minecraft.class_3675;
import net.minecraft.class_4587;
import org.lwjgl.glfw.GLFW;

public class StringComponent
extends Component
implements EventListener {
    private final ValueString value;
    private boolean listening;
    private String currentString = "";
    private final TimerUtils timer = new TimerUtils();
    private boolean selecting = false;
    private boolean line = false;

    public StringComponent(ValueString value, int offset, Frame parent, Supplier<Boolean> visible) {
        super(offset, parent, visible);
        Managers.EVENT.register(this);
        this.value = value;
    }

    @Override
    public void render(class_332 context, int mouseX, int mouseY, float partialTicks) {
        if (this.timer.hasTimeElapsed(400L)) {
            this.line = !this.line;
            this.timer.reset();
        }
        RenderUtils.drawRect(context.method_51448(), this.getX() + 1, this.getY(), this.getX() + this.getWidth() - 1, this.getY() + 14, Managers.CLICK_GUI.getColor());
        if (this.selecting) {
            class_4587 class_45872 = context.method_51448();
            float f = this.getX() + 3;
            float f2 = this.getY() + 3;
            float f3 = (float)(this.getX() + 3) + Managers.FONT.getWidth(this.currentString);
            float f4 = this.getY();
            Objects.requireNonNull((Object)StringComponent.mc.field_1772);
            RenderUtils.drawRect(class_45872, f, f2, f3, f4 + 9.0f + 3.0f, new Color(Color.LIGHT_GRAY.getRed(), Color.LIGHT_GRAY.getGreen(), Color.LIGHT_GRAY.getBlue(), 100));
        }
        if (this.listening) {
            Managers.FONT.drawWithShadow(context.method_51448(), this.currentString + (this.selecting ? "" : (this.line ? "|" : "")), (float)(this.getX() + 3), (float)(this.getY() + 3), Color.LIGHT_GRAY.getRGB());
        } else {
            Managers.FONT.drawWithShadow(context.method_51448(), this.value.getValue(), (float)(this.getX() + 3), (float)(this.getY() + 3), Color.LIGHT_GRAY.getRGB());
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseButton == 0 && this.isHovering(mouseX, mouseY)) {
            this.listening = !this.listening;
            this.currentString = this.value.getValue();
        }
    }

    @Override
    public void onKey(KeyEvent event) {
        if (this.listening && event.getScanCode() == 1) {
            if (event.getKeyCode() == 257) {
                this.updateString();
                this.listening = false;
            } else if (event.getKeyCode() == 259) {
                this.currentString = this.selecting ? "" : this.removeLastCharacter(this.currentString);
            } else if (event.getKeyCode() == 261 || event.getKeyCode() == 259) {
                this.currentString = this.selecting ? "" : this.removeLastCharacter(this.currentString);
            } else if (event.getKeyCode() == 86 && (class_3675.method_15987((long)mc.method_22683().method_4490(), (int)341) || class_3675.method_15987((long)mc.method_22683().method_4490(), (int)345))) {
                try {
                    this.currentString = this.currentString + String.valueOf((Object)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor));
                }
                catch (UnsupportedFlavorException | IOException exception) {
                    exception.printStackTrace();
                }
            } else {
                String keyName = GLFW.glfwGetKeyName((int)event.getKeyCode(), (int)event.getScanCode());
                if (keyName != null && this.isValidChatCharacter(keyName.charAt(0))) {
                    this.currentString = this.selecting ? keyName : this.currentString + keyName;
                }
            }
            this.selecting = false;
            if (event.getKeyCode() == 65 && (class_3675.method_15987((long)mc.method_22683().method_4490(), (int)341) || class_3675.method_15987((long)mc.method_22683().method_4490(), (int)345))) {
                this.selecting = true;
            }
        }
    }

    private boolean isValidChatCharacter(char c) {
        return c >= ' ' && c != '\u007f';
    }

    private void updateString() {
        if (this.currentString.length() > 0) {
            this.value.setValue(this.currentString);
        }
        this.currentString = "";
    }

    private String removeLastCharacter(String input) {
        if (input.length() > 0) {
            return input.substring(0, input.length() - 1);
        }
        return input;
    }

    public ValueString getValue() {
        return this.value;
    }
}
