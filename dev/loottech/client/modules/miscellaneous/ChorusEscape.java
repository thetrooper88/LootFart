package dev.loottech.client.modules.miscellaneous;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.api.utilities.CacheTimer;
import dev.loottech.api.utilities.InventoryUtils;
import dev.loottech.api.utilities.PlayerUtils;
import dev.loottech.client.events.FinishUsingEvent;
import dev.loottech.client.events.GameJoinEvent;
import dev.loottech.client.events.TickEvent;
import dev.loottech.client.modules.combat.AutoCrystal;
import dev.loottech.client.modules.combat.AutoMine;
import dev.loottech.client.modules.combat.AutoXP;
import dev.loottech.client.modules.combat.SelfTrap;
import dev.loottech.client.modules.combat.Surround;
import dev.loottech.client.values.impl.ValueBoolean;
import dev.loottech.client.values.impl.ValueNumber;
import net.minecraft.class_1799;
import net.minecraft.class_1802;

@RegisterModule(name="ChorusEscape", tag="ChorusEscape", description="Once logged in, automatically eat a chorus to escape logout traps.", category=Module.Category.MISCELLANEOUS)
public class ChorusEscape
extends Module {
    CacheTimer invincibilityTimer = new CacheTimer();
    ValueBoolean healthCheckConfig = new ValueBoolean("CheckHealth", "Checks health when joining game", false);
    ValueNumber healthConfig = new ValueNumber("Health", "The health to use chorus", (Number)Float.valueOf((float)1.0f), (Number)Float.valueOf((float)20.0f), (Number)Float.valueOf((float)20.0f));
    ValueBoolean totemsCheckConfig = new ValueBoolean("CheckTotems", "Checks totems when joining game", false);
    ValueNumber totemsConfig = new ValueNumber("Totems", "The totems to use chorus", (Number)Integer.valueOf((int)0), (Number)Integer.valueOf((int)0), (Number)Integer.valueOf((int)5));
    private boolean useChorus;

    @Override
    public void onDisable() {
        this.useChorus = false;
        ChorusEscape.mc.field_1690.field_1904.method_23481(false);
    }

    @Override
    public void onGameJoin(GameJoinEvent event) {
        if (this.checkChorus()) {
            this.useChorus = true;
            this.invincibilityTimer.reset();
            Managers.MODULE.getInstance(AutoCrystal.class).disable(true);
            Managers.MODULE.getInstance(AutoXP.class).disable(true);
            Managers.MODULE.getInstance(SelfTrap.class).disable(true);
            Managers.MODULE.getInstance(Surround.class).disable(true);
            Managers.MODULE.getInstance(AutoMine.class).disable(true);
        }
    }

    @Override
    public void onTick(TickEvent event) {
        if (!this.useChorus || this.invincibilityTimer.passed((Number)Integer.valueOf((int)5000))) {
            return;
        }
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            class_1799 stack1 = ChorusEscape.mc.field_1724.method_31548().method_5438(i);
            if (stack1.method_7909() != class_1802.field_8233) continue;
            slot = i;
        }
        if (slot == -1) {
            return;
        }
        Managers.INVENTORY.setClientSlot(slot);
        ChorusEscape.mc.field_1690.field_1904.method_23481(true);
    }

    @Override
    public void onFinishUsing(FinishUsingEvent event) {
        if (this.useChorus && event.getStack().method_7909() == class_1802.field_8233) {
            this.disable(false);
        }
    }

    private boolean checkChorus() {
        if (this.healthCheckConfig.getValue() && PlayerUtils.getLocalPlayerHealth() > this.healthConfig.getValue().floatValue()) {
            return false;
        }
        return !this.totemsCheckConfig.getValue() || InventoryUtils.count(class_1802.field_8288) <= this.totemsConfig.getValue().intValue();
    }

    public boolean isUsingChorus() {
        return this.useChorus;
    }
}
