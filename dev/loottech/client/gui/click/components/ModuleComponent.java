package dev.loottech.client.gui.click.components;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.miscellaneous.SoundManager;
import dev.loottech.api.manager.module.Module;
import dev.loottech.api.utilities.ColorUtils;
import dev.loottech.api.utilities.render.RenderUtils;
import dev.loottech.client.gui.click.components.BindComponent;
import dev.loottech.client.gui.click.components.BooleanComponent;
import dev.loottech.client.gui.click.components.CategoryComponent;
import dev.loottech.client.gui.click.components.ColorComponentTest;
import dev.loottech.client.gui.click.components.EnumComponent;
import dev.loottech.client.gui.click.components.NumberComponent;
import dev.loottech.client.gui.click.components.StringComponent;
import dev.loottech.client.gui.click.manage.AnimatedComponent;
import dev.loottech.client.gui.click.manage.Component;
import dev.loottech.client.gui.click.manage.Frame;
import dev.loottech.client.modules.client.ModuleColor;
import dev.loottech.client.modules.client.ModuleGUI;
import dev.loottech.client.values.Value;
import dev.loottech.client.values.impl.ValueBind;
import dev.loottech.client.values.impl.ValueBoolean;
import dev.loottech.client.values.impl.ValueCategory;
import dev.loottech.client.values.impl.ValueColor;
import dev.loottech.client.values.impl.ValueEnum;
import dev.loottech.client.values.impl.ValueNumber;
import dev.loottech.client.values.impl.ValueString;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.class_124;
import net.minecraft.class_332;
import net.minecraft.class_3532;

public class ModuleComponent
extends AnimatedComponent {
    private final ArrayList<Component> components;
    private final Module module;
    private boolean open = false;
    public Map<Integer, Color> colorMap = new HashMap();
    private float toggleProgress = 0.0f;
    private long lastToggleTime = System.currentTimeMillis();

    public ModuleComponent(Module module, int offset, Frame parent) {
        super(offset, parent, (Supplier<Boolean>)((Supplier)() -> true));
        this.module = module;
        this.components = new ArrayList();
        int valueOffset = offset;
        if (!module.getValues().isEmpty()) {
            for (Value value : module.getValues()) {
                if (value instanceof ValueBoolean) {
                    this.components.add((Object)new BooleanComponent((ValueBoolean)value, valueOffset, parent, (Supplier<Boolean>)((Supplier)value::isVisible)));
                    valueOffset += 14;
                }
                if (value instanceof ValueNumber) {
                    this.components.add((Object)new NumberComponent((ValueNumber)value, valueOffset, parent, (Supplier<Boolean>)((Supplier)value::isVisible)));
                    valueOffset += 14;
                }
                if (value instanceof ValueEnum) {
                    this.components.add((Object)new EnumComponent((ValueEnum)value, valueOffset, parent, (Supplier<Boolean>)((Supplier)value::isVisible)));
                    valueOffset += 14;
                }
                if (value instanceof ValueString) {
                    this.components.add((Object)new StringComponent((ValueString)value, valueOffset, parent, (Supplier<Boolean>)((Supplier)value::isVisible)));
                    valueOffset += 14;
                }
                if (value instanceof ValueColor) {
                    this.components.add((Object)new ColorComponentTest((ValueColor)value, valueOffset, parent, (Supplier<Boolean>)((Supplier)value::isVisible)));
                    valueOffset += 14;
                }
                if (value instanceof ValueBind) {
                    this.components.add((Object)new BindComponent((ValueBind)value, valueOffset, parent, (Supplier<Boolean>)((Supplier)value::isVisible)));
                    valueOffset += 14;
                }
                if (!(value instanceof ValueCategory)) continue;
                this.components.add((Object)new CategoryComponent((ValueCategory)value, valueOffset, parent, (Supplier<Boolean>)((Supplier)value::isVisible)));
                valueOffset += 14;
            }
        }
    }

    @Override
    public void render(class_332 context, int mouseX, int mouseY, float delta) {
        int fullWidth;
        int animatedWidth;
        if (!this.isVisible()) {
            return;
        }
        super.render(context, mouseX, mouseY, delta);
        int height = mc.method_22683().method_4502();
        for (int i = 0; i <= height; ++i) {
            this.colorMap.put((Object)i, (Object)ColorUtils.wave(Color.WHITE, ModuleGUI.INSTANCE.fadeOffset.getValue().intValue(), i * 2 + 10));
        }
        this.updateToggleAnimation();
        if (ModuleGUI.INSTANCE.rectEnabled.getValue() && (animatedWidth = (int)((double)(fullWidth = this.getWidth() - 0) * (1.0 - Math.pow((double)(1.0f - this.toggleProgress), (double)3.0)))) > 0) {
            RenderUtils.drawRect(context.method_51448(), (float)this.getX() + 0.4f, (float)this.getY() - 0.8f, (float)this.getX() + 0.4f + (float)animatedWidth, (float)this.getY() + 14.1f, Managers.CLICK_GUI.getColor());
        }
        Managers.FONT.drawWithShadow(context.method_51448(), String.valueOf((Object)(!this.module.isEnabled() ? class_124.field_1080 : "")) + this.module.getTag(), (float)(this.getX() + 3), (float)(this.getY() + 3), ModuleGUI.INSTANCE.fadeText.getValue() ? ((Color)this.colorMap.get((Object)class_3532.method_15340((int)(this.getY() + 3), (int)0, (int)height))).getRGB() : -1);
        for (Component component : this.components) {
            component.update(mouseX, mouseY, delta);
        }
        if (this.open) {
            for (Component component : this.components) {
                if (!component.isVisible()) continue;
                component.render(context, mouseX, mouseY, delta);
                if (!component.isHovering(mouseX, mouseY)) continue;
                String description = null;
                if (component instanceof BooleanComponent) {
                    description = ((BooleanComponent)component).getValue().getDescription();
                } else if (component instanceof NumberComponent) {
                    description = ((NumberComponent)component).getValue().getDescription();
                } else if (component instanceof EnumComponent) {
                    description = ((EnumComponent)component).getValue().getDescription();
                } else if (component instanceof StringComponent) {
                    description = ((StringComponent)component).getValue().getDescription();
                }
                if (description == null || description.isEmpty()) continue;
                RenderUtils.drawRect(context.method_51448(), mouseX + 5, mouseY - 2, (float)mouseX + Managers.FONT.getWidth(description) + 7.0f, mouseY + 11, new Color(40, 40, 40));
                RenderUtils.drawOutline(context.method_51448(), mouseX + 5, mouseY - 2, (float)mouseX + Managers.FONT.getWidth(description) + 7.0f, mouseY + 11, 1.0f, ModuleColor.getColor());
                Managers.FONT.drawWithShadow(context.method_51448(), description, (float)(mouseX + 7), (float)mouseY, -1);
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!this.isVisible()) {
            return;
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.isHovering(mouseX, mouseY)) {
            if (mouseButton == 0) {
                this.lastToggleTime = System.currentTimeMillis();
                this.module.toggle(true);
                if (Managers.MODULE.getInstance(ModuleGUI.class).sounds.getValue()) {
                    Managers.SOUND.playSound(SoundManager.MODULE);
                }
            }
            if (mouseButton == 1) {
                this.setOpen(!this.open);
                this.getParent().refresh();
                if (Managers.MODULE.getInstance(ModuleGUI.class).sounds.getValue()) {
                    Managers.SOUND.playSound(SoundManager.MODULE);
                }
            }
        }
        if (this.isOpen()) {
            for (Component component : this.components) {
                if (!component.isVisible()) continue;
                component.mouseClicked(mouseX, mouseY, mouseButton);
                if (!Managers.MODULE.getInstance(ModuleGUI.class).sounds.getValue() || !component.isHovering(mouseX, mouseY)) continue;
                Managers.SOUND.playSound(SoundManager.MODULE);
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        for (Component component : this.components) {
            component.mouseReleased(mouseX, mouseY, state);
        }
    }

    @Override
    public void charTyped(char typedChar, int keyCode) {
        super.charTyped(typedChar, keyCode);
        if (this.isOpen()) {
            for (Component component : this.components) {
                if (!component.isVisible()) continue;
                component.charTyped(typedChar, keyCode);
            }
        }
    }

    @Override
    public void onClose() {
        super.onClose();
        if (this.isOpen()) {
            for (Component component : this.components) {
                component.onClose();
            }
        }
    }

    private void updateToggleAnimation() {
        long now = System.currentTimeMillis();
        float delta = (float)(now - this.lastToggleTime) / 1000.0f;
        this.lastToggleTime = now;
        float speed = 6.5f;
        this.toggleProgress = this.module.isEnabled() ? Math.min((float)1.0f, (float)(this.toggleProgress + delta * speed)) : Math.max((float)0.0f, (float)(this.toggleProgress - delta * speed));
    }

    public ArrayList<Component> getComponents() {
        return this.components;
    }

    public Module getModule() {
        return this.module;
    }

    public boolean isOpen() {
        return this.open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}
