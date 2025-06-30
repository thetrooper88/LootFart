package dev.loottech.client.elements;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.element.Element;
import dev.loottech.api.manager.element.RegisterElement;
import dev.loottech.api.utilities.MathUtils;
import dev.loottech.client.events.Render2DEvent;
import dev.loottech.client.modules.client.ModuleHUDEditor;
import dev.loottech.client.values.impl.ValueBoolean;
import dev.loottech.client.values.impl.ValueEnum;
import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.minecraft.class_124;
import net.minecraft.class_408;

@RegisterElement(name="Statistics", description="Show player statistics.")
public class Statistics
extends Element {
    private final ValueBoolean fps = new ValueBoolean("FPS", "Display player FPS", true);
    private final ValueBoolean tps = new ValueBoolean("TPS", "Display server TPS", true);
    private final ValueBoolean ping = new ValueBoolean("Ping", "Display player Ping", true);
    private final ValueEnum speed = new ValueEnum("Speed", "Speed", "Display player Speed", (Enum)SpeedModes.Off);
    private final ValueEnum potionEffects = new ValueEnum("Potion Effects", "Display player potion effects.", PotionModes.Off);
    private final DecimalFormat decimal2 = new DecimalFormat("0.0#");

    @Override
    public void onRender2D(Render2DEvent event) {
        if (Statistics.nullCheck() || this.compileList().isEmpty()) {
            return;
        }
        float increment = 0.0f;
        for (ListEntry entry : this.compileList()) {
            String label = entry.getLabel();
            String value = entry.getValue();
            String full = entry.getLabel() + entry.getValue();
            Managers.FONT.drawWithShadow(event.getContext().method_51448(), label + String.valueOf((Object)class_124.field_1068) + value, this.align(full), !this.isOnTop() && Statistics.mc.field_1755 instanceof class_408 ? this.frame.getY() + increment - 14.0f : this.frame.getY() + increment, Managers.MODULE.getInstance(ModuleHUDEditor.class).color.getValue().getRGB());
            increment += Managers.FONT.getStringHeight(full) + 1.0f;
        }
        this.frame.setWidth(Managers.FONT.getStringWidth("big ass string lmao"));
        this.frame.setHeight(increment);
    }

    private List<ListEntry> compileList() {
        ArrayList entries = new ArrayList();
        if (this.ping.getValue()) {
            entries.add((Object)new ListEntry("Ping: ", String.valueOf((int)Managers.PLAYER.getPing()), Color.WHITE));
        }
        if (this.tps.getValue()) {
            entries.add((Object)new ListEntry("TPS: ", this.getTPS(), Color.WHITE));
        }
        if (this.fps.getValue()) {
            entries.add((Object)new ListEntry("FPS: ", String.valueOf((int)mc.method_47599()), Color.WHITE));
        }
        if (!this.speed.getValue().equals((Object)SpeedModes.Off)) {
            entries.add((Object)new ListEntry("Speed: ", this.getSpeed(), Color.WHITE));
        }
        return entries.stream().sorted(Comparator.comparing(this::getComparator)).toList();
    }

    private String getTPS() {
        float curr = Managers.CONNECTION.getTpsCurrent();
        String text = String.format((String)"%s", (Object[])new Object[]{this.decimal2.format((double)curr)});
        return text;
    }

    private String getSpeed() {
        if (this.speed.getValue().equals((Object)SpeedModes.BPS)) {
            return String.valueOf((double)this.getSpeedBps()) + " bps";
        }
        return String.valueOf((double)this.getSpeedKmh()) + " kmh";
    }

    private float getComparator(ListEntry entry) {
        if (this.isOnTop()) {
            return Managers.FONT.getStringWidth(entry.getFullString()) * -1.0f;
        }
        return Managers.FONT.getStringWidth(entry.getFullString());
    }

    private double getSpeedBps() {
        double x = Statistics.mc.field_1724.method_23317() - Statistics.mc.field_1724.field_6014;
        double z = Statistics.mc.field_1724.method_23321() - Statistics.mc.field_1724.field_5969;
        double dist = Math.sqrt((double)((x *= 20.0) * x + (z *= 20.0) * z));
        return MathUtils.roundToPlaces(Math.abs((double)dist), 2);
    }

    private double getSpeedKmh() {
        double x = Statistics.mc.field_1724.method_23317() - Statistics.mc.field_1724.field_6014;
        double z = Statistics.mc.field_1724.method_23321() - Statistics.mc.field_1724.field_5969;
        double dist = Math.sqrt((double)(x * x + z * z)) / 1000.0;
        double div = 1.388888888888889E-5;
        return MathUtils.roundToPlaces(dist / div, 2);
    }

    private static enum SpeedModes {
        Off,
        KMH,
        BPS;

    }

    private static enum PotionModes {
        Off,
        Static,
        Colors;

    }

    private static class ListEntry {
        String label;
        String value;
        Color valueColor;

        public ListEntry(String label, String value, Color valueColor) {
            this.label = label;
            this.value = value;
            this.valueColor = valueColor;
        }

        public String getLabel() {
            return this.label;
        }

        public String getValue() {
            return this.value;
        }

        public String getFullString() {
            return this.label + this.value;
        }

        public Color getValueColor() {
            return this.valueColor;
        }
    }
}
