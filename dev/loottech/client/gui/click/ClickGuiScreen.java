package dev.loottech.client.gui.click;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.event.EventListener;
import dev.loottech.api.manager.module.Module;
import dev.loottech.api.utilities.render.RenderUtils;
import dev.loottech.client.gui.click.components.ModuleComponent;
import dev.loottech.client.gui.click.manage.Component;
import dev.loottech.client.gui.click.manage.Frame;
import dev.loottech.client.modules.client.ModuleColor;
import dev.loottech.client.modules.client.ModuleGUI;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_437;

public class ClickGuiScreen
extends class_437
implements EventListener {
    private final ArrayList<Frame> frames = new ArrayList();
    private float tooltipProgress = 0.0f;
    private long lastTooltipTime = System.currentTimeMillis();
    private static final float TOOLTIP_ANIM_SPEED = 10.0f;
    private final Map<Module, Float> toggleProgressMap = new ConcurrentHashMap();

    public ClickGuiScreen() {
        super((class_2561)class_2561.method_43470((String)"Click GUI"));
        Managers.EVENT.register(this);
        int offset = 30;
        for (Module.Category category : Module.Category.values()) {
            this.frames.add((Object)new Frame(category, offset, 20));
            offset += 110;
        }
    }

    public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
        super.method_25394(context, mouseX, mouseY, delta);
        if (Managers.MODULE.getInstance(ModuleGUI.class).logo.getValue()) {
            class_2960 id = class_2960.method_60655((String)"loottech", (String)"logo.png");
            int x = class_310.method_1551().method_22683().method_4486() - 300;
            int y = class_310.method_1551().method_22683().method_4502() - 192;
            context.method_25290(id, x, y, 0.0f, 0.0f, 300, 192, 300, 192);
        }
        for (Frame frame : this.frames) {
            frame.render(context, mouseX, mouseY, delta);
        }
        for (Frame frame : this.frames) {
            for (Component c : frame.getComponents()) {
                if (!(c instanceof ModuleComponent)) continue;
                ModuleComponent component = (ModuleComponent)c;
                float openProgress = component.getEasedProgress();
                if (!component.isVisible() || !(openProgress > 0.8f) || !component.isHovering(mouseX, mouseY) || !frame.isOpen() || component.getModule().getDescription().isEmpty()) continue;
                RenderUtils.drawRect(context.method_51448(), mouseX + 5, mouseY - 2, (float)mouseX + Managers.FONT.getWidth(component.getModule().getDescription()) + 7.0f, mouseY + 11, new Color(40, 40, 40));
                RenderUtils.drawOutline(context.method_51448(), mouseX + 5, mouseY - 2, (float)mouseX + Managers.FONT.getWidth(component.getModule().getDescription()) + 7.0f, mouseY + 11, 1.0f, ModuleColor.getColor());
                Managers.FONT.drawWithShadow(context.method_51448(), component.getModule().getDescription(), (float)(mouseX + 7), (float)mouseY, -1);
            }
        }
    }

    public boolean method_25402(double mouseX, double mouseY, int button) {
        for (Frame frame : this.frames) {
            frame.mouseClicked((int)mouseX, (int)mouseY, button);
        }
        return super.method_25402(mouseX, mouseY, button);
    }

    public boolean method_25406(double mouseX, double mouseY, int state) {
        for (Frame frame : this.frames) {
            frame.mouseReleased((int)mouseX, (int)mouseY, state);
        }
        return super.method_25406(mouseX, mouseY, state);
    }

    public boolean method_25400(char typedChar, int keyCode) {
        for (Frame frame : this.frames) {
            frame.charTyped(typedChar, keyCode);
        }
        return super.method_25400(typedChar, keyCode);
    }

    public Color getColor() {
        return new Color(ModuleColor.getColor().getRed(), ModuleColor.getColor().getGreen(), ModuleColor.getColor().getBlue(), 160);
    }
}
