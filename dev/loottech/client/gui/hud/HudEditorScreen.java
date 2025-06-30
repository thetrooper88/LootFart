package dev.loottech.client.gui.hud;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.element.Element;
import dev.loottech.api.utilities.Util;
import dev.loottech.api.utilities.render.RenderUtils;
import dev.loottech.client.gui.click.manage.Frame;
import dev.loottech.client.gui.hud.ElementFrame;
import java.awt.Color;
import java.util.ArrayList;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_437;

public class HudEditorScreen
extends class_437 {
    private final ArrayList<ElementFrame> elementFrames = new ArrayList();
    private final Frame frame = new Frame(20, 20);

    public HudEditorScreen() {
        super((class_2561)class_2561.method_43470((String)""));
        for (Element element : Managers.ELEMENT.getElements()) {
            this.addElement(element);
            element.setFrame(this.getFrame(element));
        }
    }

    public void addElement(Element element) {
        this.elementFrames.add((Object)new ElementFrame(element, 10.0f, 10.0f, 80.0f, 15.0f, this));
    }

    public void method_25394(class_332 context, int mouseX, int mouseY, float partialTicks) {
        super.method_25394(context, mouseX, mouseY, partialTicks);
        this.frame.render(context, mouseX, mouseY, partialTicks);
        float half = (float)Util.mc.method_22683().method_4486() / 2.0f;
        float height = Util.mc.method_22683().method_4502();
        RenderUtils.rect(context.method_51448(), half - 1.0f, height, half + 1.0f, 0.0f, Color.WHITE.getRGB());
        for (ElementFrame frame : this.elementFrames) {
            frame.render(context, mouseX, mouseY, partialTicks);
        }
    }

    public boolean method_25402(double mouseX, double mouseY, int button) {
        this.frame.mouseClicked((int)mouseX, (int)mouseY, button);
        for (ElementFrame frame : this.elementFrames) {
            frame.mouseClicked((int)mouseX, (int)mouseY, button);
        }
        return super.method_25402(mouseX, mouseY, button);
    }

    public boolean method_25406(double mouseX, double mouseY, int state) {
        this.frame.mouseReleased((int)mouseX, (int)mouseY, state);
        for (ElementFrame frame : this.elementFrames) {
            frame.mouseReleased((int)mouseX, (int)mouseY, state);
        }
        return super.method_25406(mouseX, mouseY, state);
    }

    public Frame getFrame() {
        return this.frame;
    }

    public ElementFrame getFrame(Element element) {
        for (ElementFrame frame : this.elementFrames) {
            if (!frame.getElement().equals(element)) continue;
            return frame;
        }
        return null;
    }
}
