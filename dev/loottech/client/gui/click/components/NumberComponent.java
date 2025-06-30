package dev.loottech.client.gui.click.components;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.event.EventListener;
import dev.loottech.api.utilities.MathUtils;
import dev.loottech.api.utilities.TimerUtils;
import dev.loottech.api.utilities.render.RenderUtils;
import dev.loottech.client.events.KeyEvent;
import dev.loottech.client.gui.click.manage.Component;
import dev.loottech.client.gui.click.manage.Frame;
import dev.loottech.client.values.impl.ValueNumber;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Objects;
import java.util.function.Supplier;
import net.minecraft.class_124;
import net.minecraft.class_332;
import net.minecraft.class_3675;
import net.minecraft.class_4587;
import org.lwjgl.glfw.GLFW;

public class NumberComponent
extends Component
implements EventListener {
    private final ValueNumber value;
    private float sliderWidth;
    private boolean dragging;
    private boolean listening;
    private String currentString = "";
    private final TimerUtils timer = new TimerUtils();
    private final TimerUtils backTimer = new TimerUtils();
    private boolean selecting = false;
    private boolean line = false;
    private boolean binding = false;
    private int boundMouseButton = -1;

    public NumberComponent(ValueNumber value, int offset, Frame parent, Supplier<Boolean> visible) {
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
        RenderUtils.drawRect(context.method_51448(), this.getX() + 1, this.getY(), (float)(this.getX() + 1) + this.sliderWidth, this.getY() + 14, Managers.CLICK_GUI.getColor());
        if (this.selecting) {
            class_4587 class_45872 = context.method_51448();
            float f = (float)(this.getX() + 3) + (float)NumberComponent.mc.field_1772.method_1727(this.value.getTag() + " ");
            float f2 = this.getY() + 3;
            float f3 = (float)(this.getX() + 3) + (float)NumberComponent.mc.field_1772.method_1727(this.value.getTag() + " ") + (float)NumberComponent.mc.field_1772.method_1727(this.currentString);
            float f4 = this.getY();
            Objects.requireNonNull((Object)NumberComponent.mc.field_1772);
            RenderUtils.drawRect(class_45872, f, f2, f3, f4 + 9.0f + 3.0f, new Color(Color.LIGHT_GRAY.getRed(), Color.LIGHT_GRAY.getGreen(), Color.LIGHT_GRAY.getBlue(), 100));
        }
        if (this.listening) {
            Managers.FONT.drawWithShadow(context.method_51448(), this.value.getTag() + " " + String.valueOf((Object)class_124.field_1080) + this.currentString + (this.selecting ? "" : (this.line ? "|" : "")), (float)(this.getX() + 3), (float)(this.getY() + 3), -1);
        } else {
            Managers.FONT.drawWithShadow(context.method_51448(), this.value.getTag() + " " + String.valueOf((Object)class_124.field_1080) + String.valueOf((Object)this.value.getValue()) + (this.value.getType() == 1 ? ".0" : ""), (float)(this.getX() + 3), (float)(this.getY() + 3), -1);
        }
    }

    @Override
    public void update(int mouseX, int mouseY, float partialTicks) {
        super.update(mouseX, mouseY, partialTicks);
        if (this.value.getParent() != null) {
            this.setVisible(this.value.getParent().isOpen());
        }
        double difference = Math.min((int)98, (int)Math.max((int)0, (int)(mouseX - this.getX())));
        if (this.value.getType() == 1) {
            this.sliderWidth = 98.0f * (float)(this.value.getValue().intValue() - this.value.getMinimum().intValue()) / (float)(this.value.getMaximum().intValue() - this.value.getMinimum().intValue());
            if (this.dragging) {
                if (difference == 0.0) {
                    this.value.setValue(this.value.getMinimum());
                } else {
                    int value = (int)MathUtils.roundToPlaces(difference / 98.0 * (double)(this.value.getMaximum().intValue() - this.value.getMinimum().intValue()) + (double)this.value.getMinimum().intValue(), 0);
                    this.value.setValue((Number)Integer.valueOf((int)value));
                }
            }
        } else if (this.value.getType() == 2) {
            this.sliderWidth = (float)(98.0 * (this.value.getValue().doubleValue() - this.value.getMinimum().doubleValue()) / (this.value.getMaximum().doubleValue() - this.value.getMinimum().doubleValue()));
            if (this.dragging) {
                if (difference == 0.0) {
                    this.value.setValue(this.value.getMinimum());
                } else {
                    double value = MathUtils.roundToPlaces(difference / 98.0 * (this.value.getMaximum().doubleValue() - this.value.getMinimum().doubleValue()) + this.value.getMinimum().doubleValue(), 2);
                    this.value.setValue((Number)Double.valueOf((double)value));
                }
            }
        } else if (this.value.getType() == 3) {
            this.sliderWidth = 98.0f * (this.value.getValue().floatValue() - this.value.getMinimum().floatValue()) / (this.value.getMaximum().floatValue() - this.value.getMinimum().floatValue());
            if (this.dragging) {
                if (difference == 0.0) {
                    this.value.setValue(this.value.getMinimum());
                } else {
                    float value = (float)MathUtils.roundToPlaces(difference / 98.0 * (double)(this.value.getMaximum().floatValue() - this.value.getMinimum().floatValue()) + (double)this.value.getMinimum().floatValue(), 2);
                    this.value.setValue((Number)Float.valueOf((float)value));
                }
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (this.isHovering(mouseX, mouseY)) {
            if (mouseButton == 0) {
                this.dragging = true;
            } else if (mouseButton == 1) {
                this.listening = !this.listening;
                this.currentString = this.value.getValue().toString();
            } else if (this.binding) {
                this.boundMouseButton = mouseButton;
                this.binding = false;
                System.out.println("Bound to mouse button: " + mouseButton);
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        this.dragging = false;
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

    private void updateString() {
        block16: {
            if (this.currentString.length() > 0) {
                if (this.value.getType() == 1) {
                    try {
                        if (Integer.parseInt((String)this.currentString) <= this.value.getMaximum().intValue() && Integer.parseInt((String)this.currentString) >= this.value.getMinimum().intValue()) {
                            this.value.setValue((Number)Integer.valueOf((int)Integer.parseInt((String)this.currentString)));
                            break block16;
                        }
                        this.value.setValue(this.value.getValue());
                    }
                    catch (NumberFormatException e) {
                        this.value.setValue(this.value.getValue());
                    }
                } else if (this.value.getType() == 3) {
                    try {
                        if (!(Float.parseFloat((String)this.currentString) > this.value.getMaximum().floatValue()) && !(Float.parseFloat((String)this.currentString) < this.value.getMinimum().floatValue())) {
                            this.value.setValue((Number)Float.valueOf((float)Float.parseFloat((String)this.currentString)));
                            break block16;
                        }
                        this.value.setValue(this.value.getValue());
                    }
                    catch (NumberFormatException e) {
                        this.value.setValue(this.value.getValue());
                    }
                } else if (this.value.getType() == 2) {
                    try {
                        if (!(Double.parseDouble((String)this.currentString) > this.value.getMaximum().doubleValue()) && !(Double.parseDouble((String)this.currentString) < this.value.getMinimum().doubleValue())) {
                            this.value.setValue((Number)Double.valueOf((double)Double.parseDouble((String)this.currentString)));
                        } else {
                            this.value.setValue(this.value.getValue());
                        }
                    }
                    catch (NumberFormatException e) {
                        this.value.setValue(this.value.getValue());
                    }
                }
            }
        }
        this.currentString = "";
    }

    private boolean isValidChatCharacter(char c) {
        return c >= ' ' && c != '\u007f';
    }

    private String removeLastCharacter(String input) {
        if (input.length() > 0) {
            return input.substring(0, input.length() - 1);
        }
        return input;
    }

    public ValueNumber getValue() {
        return this.value;
    }
}
