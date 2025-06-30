package dev.loottech.client.modules.player;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.api.utilities.FindItemResult;
import dev.loottech.api.utilities.InventoryUtils;
import dev.loottech.client.values.impl.ValueBoolean;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_1738;
import net.minecraft.class_1770;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2596;
import net.minecraft.class_2848;

@RegisterModule(name="ChestSwap", category=Module.Category.PLAYER, tag="ChestSwap", description="Swap chestplate with elytra.")
public class ChestSwap
extends Module {
    ValueBoolean autoLaunch = new ValueBoolean("AutoLaunch", "AutoLaunch", "Automatically jump and use a rocket (HOTBAR ROCKET ONLY)", false);
    private boolean isHotbar;
    private boolean isElytra;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool((int)1);
    private ScheduledFuture<?> currentTask = null;

    @Override
    public void onEnable() {
        if (ChestSwap.mc.field_1724 == null || ChestSwap.mc.field_1724.method_31548() == null || this.determineTargetItem() == null) {
            this.disable(false);
            return;
        }
        class_1792 targetItem = this.determineTargetItem();
        FindItemResult itemResult = InventoryUtils.find(targetItem);
        if (itemResult.isMain()) {
            this.swapItemFromInventory(itemResult.slot());
        } else if (itemResult.isHotbar()) {
            Managers.INVENTORY.setSlot(itemResult.slot());
            ChestSwap.mc.field_1761.method_2919((class_1657)ChestSwap.mc.field_1724, class_1268.field_5808);
            Managers.INVENTORY.syncToClient();
        }
        if (this.autoLaunch.getValue() && targetItem.equals((Object)class_1802.field_8833) && InventoryUtils.isFoundInHotbar(class_1802.field_8639) && ChestSwap.mc.field_1690.field_1903.method_1434()) {
            ChestSwap.mc.field_1724.method_6043();
            this.currentTask = this.scheduler.schedule(() -> {
                Managers.NETWORK.sendPacket((class_2596<?>)new class_2848((class_1297)ChestSwap.mc.field_1724, class_2848.class_2849.field_12982));
                ChestSwap.mc.field_1724.method_23669();
                Managers.INVENTORY.setSlot(InventoryUtils.find(class_1802.field_8639).slot());
                ChestSwap.mc.field_1761.method_2919((class_1657)ChestSwap.mc.field_1724, class_1268.field_5808);
                Managers.INVENTORY.syncToClient();
            }, 50L, TimeUnit.MILLISECONDS);
        }
        this.disable(false);
    }

    private class_1792 determineTargetItem() {
        class_1799 chestStack = ChestSwap.mc.field_1724.method_31548().method_5438(38);
        if (chestStack.method_7909() instanceof class_1770) {
            if (InventoryUtils.isFound(class_1802.field_22028)) {
                return class_1802.field_22028;
            }
            if (InventoryUtils.isFound(class_1802.field_8058)) {
                return class_1802.field_8058;
            }
        } else if (chestStack.method_7909() instanceof class_1738) {
            return class_1802.field_8833;
        }
        return null;
    }

    private void swapItemFromInventory(int slot) {
        class_1799 equippedItem = ChestSwap.mc.field_1724.method_31548().method_7372(2);
        InventoryUtils.pickupSlot(slot);
        boolean hasEquippedItem = !equippedItem.method_7960();
        InventoryUtils.pickupSlot(6);
        if (hasEquippedItem) {
            InventoryUtils.pickupSlot(slot);
        }
    }
}
