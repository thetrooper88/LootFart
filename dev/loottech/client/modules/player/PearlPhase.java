package dev.loottech.client.modules.player;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.api.utilities.InventoryUtils;
import dev.loottech.client.values.impl.ValueBoolean;
import dev.loottech.client.values.impl.ValueNumber;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1802;
import net.minecraft.class_2246;
import net.minecraft.class_2886;

@RegisterModule(name="PearlPhase", tag="PearlPhase", description="Phase into a block using a ender pearl.", category=Module.Category.PLAYER)
public class PearlPhase
extends Module {
    private final ValueBoolean swing = new ValueBoolean("Swing", "Swing", "", true);
    private final ValueNumber pitch = new ValueNumber("Pitch", "Pitch", "", (Number)Integer.valueOf((int)86), (Number)Integer.valueOf((int)70), (Number)Integer.valueOf((int)90));

    @Override
    public void onEnable() {
        if (PearlPhase.nullCheck()) {
            return;
        }
        int pearlSlot = this.getEnderPearlSlot();
        if (PearlPhase.mc.field_1687.method_20812((class_1297)PearlPhase.mc.field_1724, PearlPhase.mc.field_1724.method_5829()).iterator().hasNext() && !PearlPhase.mc.field_1687.method_8320(PearlPhase.mc.field_1724.method_24515().method_10084()).method_26204().equals((Object)class_2246.field_10540)) {
            this.disable(false);
            return;
        }
        if (pearlSlot == -1) {
            return;
        }
        float playerYaw = PearlPhase.mc.field_1724.method_36454();
        if (PearlPhase.mc.field_1690.field_1881.method_1434()) {
            playerYaw -= 180.0f;
        }
        if (PearlPhase.mc.field_1690.field_1913.method_1434()) {
            playerYaw -= 90.0f;
        }
        if (PearlPhase.mc.field_1690.field_1849.method_1434()) {
            playerYaw += 90.0f;
        }
        float prevYaw = PearlPhase.mc.field_1724.method_36454();
        float prevPitch = PearlPhase.mc.field_1724.method_36455();
        Managers.INVENTORY.setSlot(pearlSlot);
        Managers.ROTATION.setRotationClient(playerYaw, this.pitch.getValue().floatValue());
        Managers.ROTATION.setRotationSilent(playerYaw, this.pitch.getValue().floatValue());
        float finalPlayerYaw = playerYaw;
        Managers.NETWORK.sendSequencedPacket(seq -> new class_2886(class_1268.field_5808, seq, finalPlayerYaw, this.pitch.getValue().floatValue()));
        if (this.swing.getValue()) {
            PearlPhase.mc.field_1724.method_6104(class_1268.field_5808);
        }
        Managers.ROTATION.setRotationSilentSync();
        Managers.ROTATION.setRotationClient(prevYaw, prevPitch);
        Managers.INVENTORY.syncToClient();
        this.disable(false);
    }

    private int getEnderPearlSlot() {
        return InventoryUtils.find(class_1802.field_8634).slot();
    }
}
