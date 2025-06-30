package dev.loottech.client.modules.miscellaneous;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.api.utilities.ChatUtils;
import dev.loottech.api.utilities.DirectionUtils;
import dev.loottech.api.utilities.FindItemResult;
import dev.loottech.api.utilities.InventoryUtils;
import dev.loottech.api.utilities.RotationUtils;
import dev.loottech.client.events.TickEvent;
import dev.loottech.client.values.impl.ValueEnum;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1304;
import net.minecraft.class_1531;
import net.minecraft.class_1542;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2382;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_2586;
import net.minecraft.class_2680;
import net.minecraft.class_2741;
import net.minecraft.class_2769;
import net.minecraft.class_3965;
import net.minecraft.class_9199;

@RegisterModule(name="AutoTrialVault", description="Automatically uses a key on vaults with specific items.", category=Module.Category.MISCELLANEOUS)
public class AutoTrialVault
extends Module {
    private final ValueEnum item = new ValueEnum("Item", "Item", "What item to look for in the vault.", (Enum)ItemsSelection.WIND_CHARGE);

    @Override
    public void onTick(TickEvent event) {
        class_243 vaultPos;
        if (AutoTrialVault.mc.field_1724 == null || AutoTrialVault.mc.field_1687 == null) {
            return;
        }
        if (!this.hasTrialKeyInHotbar()) {
            return;
        }
        class_239 hit = AutoTrialVault.mc.field_1765;
        if (hit == null || hit.method_17783() != class_239.class_240.field_1332) {
            return;
        }
        class_2338 pos = ((class_3965)hit).method_17777();
        if (!this.isVault(pos)) {
            return;
        }
        class_243 eyesPos = AutoTrialVault.mc.field_1724.method_33571();
        if (eyesPos.method_1022(vaultPos = class_243.method_24953((class_2382)pos)) > 4.0) {
            return;
        }
        if (this.isShowingSelectedItem(pos)) {
            this.swapToKey();
            float[] rotation = RotationUtils.getRotations(class_243.method_24953((class_2382)pos));
            Managers.ROTATION.setRotation(rotation[0], rotation[1]);
            AutoTrialVault.mc.field_1761.method_2896(AutoTrialVault.mc.field_1724, class_1268.field_5808, new class_3965(class_243.method_24953((class_2382)pos), DirectionUtils.getDirection(pos), pos, false));
            AutoTrialVault.mc.field_1724.method_6104(class_1268.field_5808);
            this.sendInfo("\u00a7aUsed key on vault containing " + this.item.getValue().name());
        }
    }

    private boolean isVault(class_2338 pos) {
        class_2680 state = AutoTrialVault.mc.field_1687.method_8320(pos);
        class_2248 b = state.method_26204();
        return b == class_2246.field_48851 && b.method_34725(state).method_28498((class_2769)class_2741.field_50193);
    }

    private boolean hasTrialKeyInHotbar() {
        return InventoryUtils.findInHotbar(class_1802.field_50139).found();
    }

    private boolean isShowingSelectedItem(class_2338 pos) {
        class_2586 blockEntity = AutoTrialVault.mc.field_1687.method_8321(pos);
        if (!(blockEntity instanceof class_9199)) {
            return false;
        }
        class_9199 vault = (class_9199)blockEntity;
        class_1799 displayedItem = vault.method_56735().method_56787();
        if (displayedItem.method_7960()) {
            return false;
        }
        switch (((ItemsSelection)this.item.getValue()).ordinal()) {
            case 5: {
                return displayedItem.method_7909() == class_1802.field_49813;
            }
            case 0: {
                return displayedItem.method_31574(class_1802.field_49098);
            }
            case 1: {
                return displayedItem.method_31574(class_1802.field_50140);
            }
            case 2: {
                return displayedItem.method_31574(class_1802.field_49818);
            }
            case 3: {
                return displayedItem.method_31574(class_1802.field_51630);
            }
            case 4: {
                return displayedItem.method_31574(class_1802.field_8477);
            }
        }
        return false;
    }

    private class_1799 getItemFromEntity(class_1297 entity) {
        if (entity instanceof class_1542) {
            class_1542 itemEntity = (class_1542)entity;
            return itemEntity.method_6983();
        }
        if (entity instanceof class_1531) {
            class_1531 armorStand = (class_1531)entity;
            return armorStand.method_6118(class_1304.field_6173);
        }
        return class_1799.field_8037;
    }

    private void swapToKey() {
        FindItemResult result = InventoryUtils.findInHotbar(class_1802.field_50139);
        if (result.found()) {
            Managers.INVENTORY.setClientSlot(result.slot());
        }
    }

    private void sendInfo(String message) {
        ChatUtils.sendMessage(message, "AutoTrialVault");
    }

    private static enum ItemsSelection {
        WIND_CHARGE,
        OMINOUS_BOTTLE,
        BOLT_TRIM,
        PRECIPICE_DISK,
        DIAMOND,
        HEAVY_CORE;

    }
}
