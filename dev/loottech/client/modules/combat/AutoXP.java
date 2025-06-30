package dev.loottech.client.modules.combat;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.api.manager.rotation.Rotation;
import dev.loottech.api.utilities.Timer;
import dev.loottech.client.values.impl.ValueBoolean;
import dev.loottech.client.values.impl.ValueNumber;
import net.minecraft.class_1268;
import net.minecraft.class_1657;
import net.minecraft.class_1779;
import net.minecraft.class_1799;
import net.minecraft.class_2886;

@RegisterModule(name="AutoXP", tag="AutoXP", description="Throw XP silently.", category=Module.Category.COMBAT)
public class AutoXP
extends Module {
    ValueBoolean multiTask = new ValueBoolean("MultiTask", "MultiTask", "Throw xp while using items.", false);
    ValueBoolean durabilityCheck = new ValueBoolean("DurabilityCheck", "DurabilityCheck", "Checks if items are full durability.", false);
    ValueNumber delay = new ValueNumber("Delay", "Delay", "Delay between throwing XP bottles.", (Number)Float.valueOf((float)1.0f), (Number)Double.valueOf((double)1.0), (Number)Float.valueOf((float)10.0f));
    ValueNumber shiftTicks = new ValueNumber("ShiftTicks", "ShiftTicks", "How many bottles to throw per tick.", (Number)Integer.valueOf((int)1), (Number)Integer.valueOf((int)1), (Number)Integer.valueOf((int)10));
    ValueBoolean rotate = new ValueBoolean("Rotate", "Rotate", "Rotate to the floor when throwing XP.", false);
    ValueBoolean swing = new ValueBoolean("Swing", "Swing", "Swing between throwing XP bottles.", false);
    Timer delayTimer = new Timer();

    @Override
    public void onUpdate() {
        int i;
        if (AutoXP.mc.field_1724 == null || !this.delayTimer.passedTicks(this.delay.getValue().longValue())) {
            return;
        }
        if (AutoXP.mc.field_1724.method_6115() && !this.multiTask.getValue()) {
            return;
        }
        if (this.durabilityCheck.getValue() && this.areItemsFullDura((class_1657)AutoXP.mc.field_1724)) {
            this.disable(false);
        }
        int slot = -1;
        for (i = 0; i < 9; ++i) {
            class_1799 stack = AutoXP.mc.field_1724.method_31548().method_5438(i);
            if (!(stack.method_7909() instanceof class_1779)) continue;
            slot = i;
            break;
        }
        if (slot == -1) {
            this.disable(false);
            return;
        }
        Managers.INVENTORY.setSlot(slot);
        if (this.rotate.getValue()) {
            Managers.ROTATION.setRotation(new Rotation(100, AutoXP.mc.field_1724.method_36454(), 90.0f));
        }
        for (i = 0; i < this.shiftTicks.getValue().intValue(); ++i) {
            Managers.NETWORK.sendSequencedPacket(id -> new class_2886(class_1268.field_5808, id, AutoXP.mc.field_1724.method_36454(), AutoXP.mc.field_1724.method_36455()));
            if (!this.swing.getValue()) continue;
            AutoXP.mc.field_1724.method_6104(class_1268.field_5808);
        }
        Managers.INVENTORY.syncToClient();
        this.delayTimer.reset();
    }

    private boolean areItemsFullDura(class_1657 player) {
        if (!this.isItemFullDura(player.method_6047()) || !this.isItemFullDura(player.method_6079())) {
            return false;
        }
        for (class_1799 stack : player.method_5661()) {
            if (this.isItemFullDura(stack)) continue;
            return false;
        }
        return true;
    }

    private boolean isItemFullDura(class_1799 stack) {
        if (stack.method_7960()) {
            return true;
        }
        int maxDura = stack.method_7936();
        int currentDura = stack.method_7919();
        return currentDura == 0 || maxDura == 0;
    }
}
