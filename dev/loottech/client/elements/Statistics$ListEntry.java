package dev.loottech.client.elements;

import java.awt.Color;

private static class Statistics.ListEntry {
    String label;
    String value;
    Color valueColor;

    public Statistics.ListEntry(String label, String value, Color valueColor) {
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
