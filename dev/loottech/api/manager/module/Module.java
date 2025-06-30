package dev.loottech.api.manager.module;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.event.EventListener;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.api.utilities.ChatUtils;
import dev.loottech.api.utilities.InputUtils;
import dev.loottech.api.utilities.Util;
import dev.loottech.client.events.Render2DEvent;
import dev.loottech.client.events.Render3DEvent;
import dev.loottech.client.modules.client.Notifications;
import dev.loottech.client.values.Value;
import dev.loottech.client.values.impl.ValueBind;
import dev.loottech.client.values.impl.ValueBoolean;
import dev.loottech.client.values.impl.ValueString;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.class_124;
import net.minecraft.class_2596;

public abstract class Module
implements Util,
EventListener {
    private ArrayList<Value> values = null;
    public String name;
    public String description;
    public Category category;
    private boolean toggled;
    private boolean persistent = false;
    private Color randomColor;
    public ValueString tag = new ValueString("Tag", "Tag", "The module's display name.", this.getName());
    public ValueBoolean chatNotify = new ValueBoolean("ChatNotify", "Chat Notify", "Notifies you in chat when the module is toggled on or off.", true);
    public ValueBoolean drawn = new ValueBoolean("Drawn", "Drawn", "Makes the module appear on the array list.", true);
    public ValueBoolean holdBind = new ValueBoolean("Hold Bind", "Hold Bind", "Activate only if the bind is held.", false);
    public ValueBind bind = new ValueBind("Bind", "Bind", "The module's toggle bind.", 0);

    public Module() {
        RegisterModule annotation = (RegisterModule)this.getClass().getAnnotation(RegisterModule.class);
        if (annotation != null) {
            this.name = annotation.name();
            this.tag.setValue(annotation.tag().equals((Object)" ") ? annotation.name() : annotation.tag());
            this.description = annotation.description();
            this.category = annotation.category();
            this.persistent = annotation.persistent();
            this.bind.setValue(annotation.bind());
            this.drawn.setValue(annotation.drawn());
            this.values = new ArrayList();
            if (this.persistent) {
                this.setToggled(true);
                Managers.EVENT.register(this);
            }
            this.randomColor = this.generateRandomColor();
        }
    }

    public Color generateRandomColor() {
        Random random = new Random();
        int randomRed = random.nextInt(255);
        int randomGreen = random.nextInt(255);
        int randomBlue = random.nextInt(255);
        return new Color(randomRed, randomGreen, randomBlue);
    }

    public void onTick() {
    }

    public void onTickTwo() {
        boolean shouldBeActive;
        if (this.isHoldBind() && this.getBind() != 0 && (shouldBeActive = InputUtils.isKeyPressed(this.getBind())) != this.isEnabled()) {
            this.setToggled(shouldBeActive);
            if (shouldBeActive) {
                Managers.EVENT.register(this);
                this.onEnable();
            } else {
                Managers.EVENT.unregister(this);
                this.onDisable();
            }
        }
    }

    public void onUpdate() {
    }

    @Override
    public void onRender2D(Render2DEvent event) {
    }

    @Override
    public void onRender3D(Render3DEvent event) {
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void onLogin() {
    }

    public void onLogout() {
    }

    public void onDeath() {
    }

    public static boolean nullCheck() {
        return Module.mc.field_1724 == null || Module.mc.field_1687 == null;
    }

    public String getHudInfo() {
        return "";
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public Category getCategory() {
        return this.category;
    }

    public boolean isEnabled() {
        return this.toggled;
    }

    public void setToggled(boolean toggled) {
        this.toggled = toggled;
    }

    public boolean isPersistent() {
        return this.persistent;
    }

    public String getTag() {
        return this.tag.getValue();
    }

    public void setTag(String tag) {
        this.tag.setValue(tag);
    }

    public boolean isChatNotify() {
        return this.chatNotify.getValue();
    }

    public void setChatNotify(boolean chatNotify) {
        this.chatNotify.setValue(chatNotify);
    }

    public boolean isDrawn() {
        return this.drawn.getValue();
    }

    public void setDrawn(boolean drawn) {
        this.drawn.setValue(drawn);
    }

    public int getBind() {
        return this.bind.getValue();
    }

    public boolean isHoldBind() {
        return this.holdBind.getValue();
    }

    public void setBind(int bind) {
        this.bind.setValue(bind);
    }

    public Color getRandomColor() {
        return this.randomColor;
    }

    public void toggle(boolean message) {
        if (this.isEnabled()) {
            this.disable(message);
        } else {
            this.enable(message);
        }
    }

    public void enable(boolean message) {
        if (!this.persistent) {
            this.setToggled(true);
            Managers.EVENT.register(this);
            if (message) {
                this.doToggleMessage();
            }
            this.onEnable();
        }
    }

    public void disable(boolean message) {
        if (!this.persistent) {
            this.setToggled(false);
            Managers.EVENT.unregister(this);
            if (message) {
                this.doToggleMessage();
            }
            this.onDisable();
        }
    }

    public void doToggleMessage() {
        if (this.isChatNotify()) {
            int number = 0;
            for (char character : this.getTag().toCharArray()) {
                number += character;
                number *= 10;
            }
            String state = this.isEnabled() ? "ON" : "OFF";
            class_124 stateColor = this.isEnabled() ? class_124.field_1060 : class_124.field_1061;
            ChatUtils.sendRawMessage(String.valueOf((Object)class_124.field_1067) + this.getTag() + String.valueOf((Object)class_124.field_1070) + " Toggled " + String.valueOf((Object)class_124.field_1067) + String.valueOf((Object)stateColor) + state);
            if (Managers.MODULE.isModuleEnabled("Notifications") && Managers.MODULE.getInstance(Notifications.class).toggle.getValue()) {
                Managers.MODULE.getInstance(Notifications.class).handleToggle(this);
            }
        }
    }

    public void sendPacket(class_2596<?> packet) {
        if (mc.method_1562() == null) {
            return;
        }
        Managers.NETWORK.sendPacket(packet);
    }

    public ArrayList<Value> getValues() {
        return this.values;
    }

    public static enum Category {
        COMBAT("Combat"),
        PLAYER("Player"),
        MISCELLANEOUS("Miscellaneous"),
        MOVEMENT("Movement"),
        VISUALS("Visuals"),
        CLIENT("Client");

        private final String name;

        private Category(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    public static enum HudCategory {
        HUD("HUD");

        private final String name;

        private HudCategory(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}
