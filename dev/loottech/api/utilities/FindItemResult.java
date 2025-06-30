package dev.loottech.api.utilities;

import net.minecraft.class_1268;
import net.minecraft.class_310;

public record FindItemResult(int slot, int count) {
    static class_310 mc = class_310.method_1551();

    public boolean found() {
        return this.slot != -1;
    }

    public class_1268 getHand() {
        if (this.slot == 45) {
            return class_1268.field_5810;
        }
        if (this.slot == FindItemResult.mc.field_1724.method_31548().field_7545) {
            return class_1268.field_5808;
        }
        return null;
    }

    public boolean isMainHand() {
        return this.getHand() == class_1268.field_5808;
    }

    public boolean isOffhand() {
        return this.getHand() == class_1268.field_5810;
    }

    public boolean isHotbar() {
        return this.slot >= 0 && this.slot <= 8;
    }

    public boolean isMain() {
        return this.slot >= 9 && this.slot <= 35;
    }

    public boolean isArmor() {
        return this.slot >= 36 && this.slot <= 39;
    }
}
