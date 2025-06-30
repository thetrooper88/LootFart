package dev.loottech.client.modules.miscellaneous;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.api.utilities.RayCastUtil;
import dev.loottech.client.events.KeyEvent;
import dev.loottech.client.values.impl.ValueBoolean;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_1713;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_239;
import net.minecraft.class_3966;

@RegisterModule(name="MiddleClick", tag="Middle Click", description="Add actions to middle click.", category=Module.Category.MISCELLANEOUS)
public class ModuleMiddleClick
extends Module {
    public static ModuleMiddleClick INSTANCE;
    ValueBoolean mcf = new ValueBoolean("Friend", "Friend", "", true);
    ValueBoolean pearl = new ValueBoolean("Pearl", "Pearl", "", true);
    ValueBoolean rocket = new ValueBoolean("Rocket", "Rocket", "", true);

    public ModuleMiddleClick() {
        INSTANCE = this;
    }

    @Override
    public void onKey(KeyEvent event) {
        if (ModuleMiddleClick.mc.field_1724 == null || ModuleMiddleClick.mc.field_1761 == null) {
            return;
        }
        if (event.getKeyCode() == 2 && event.getScanCode() == 1 && ModuleMiddleClick.mc.field_1755 == null) {
            class_1297 class_12972;
            double d = ModuleMiddleClick.mc.field_1724.method_55755();
            class_239 result = RayCastUtil.raycastEntity(d);
            if (result != null && result.method_17783() == class_239.class_240.field_1331 && this.mcf.getValue() && (class_12972 = ((class_3966)result).method_17782()) instanceof class_1657) {
                class_1657 target = (class_1657)class_12972;
                if (Managers.FRIEND.isFriend(target)) {
                    Managers.FRIEND.removeFriend(target.method_5477().method_54160());
                } else {
                    Managers.FRIEND.addFriend(target.method_5477().method_54160());
                }
            } else {
                class_1792 item = null;
                if (ModuleMiddleClick.mc.field_1724.method_6128() && this.rocket.getValue()) {
                    item = class_1802.field_8639;
                } else if (this.pearl.getValue()) {
                    item = class_1802.field_8634;
                }
                if (item == null) {
                    return;
                }
                int slot = -1;
                for (int i = 0; i < 45; ++i) {
                    class_1799 stack = ModuleMiddleClick.mc.field_1724.method_31548().method_5438(i);
                    if (stack.method_7909() != item) continue;
                    slot = i;
                    break;
                }
                if (slot == -1) {
                    return;
                }
                if (slot < 9) {
                    Managers.INVENTORY.setSlot(slot);
                    ModuleMiddleClick.mc.field_1761.method_2919((class_1657)ModuleMiddleClick.mc.field_1724, class_1268.field_5808);
                    Managers.INVENTORY.syncToClient();
                } else {
                    ModuleMiddleClick.mc.field_1761.method_2906(0, slot, 0, class_1713.field_7790, (class_1657)ModuleMiddleClick.mc.field_1724);
                    ModuleMiddleClick.mc.field_1761.method_2906(0, ModuleMiddleClick.mc.field_1724.method_31548().field_7545 + 36, 0, class_1713.field_7790, (class_1657)ModuleMiddleClick.mc.field_1724);
                    ModuleMiddleClick.mc.field_1761.method_2906(0, slot, 0, class_1713.field_7790, (class_1657)ModuleMiddleClick.mc.field_1724);
                    ModuleMiddleClick.mc.field_1761.method_2919((class_1657)ModuleMiddleClick.mc.field_1724, class_1268.field_5808);
                    ModuleMiddleClick.mc.field_1761.method_2906(0, slot, 0, class_1713.field_7790, (class_1657)ModuleMiddleClick.mc.field_1724);
                    ModuleMiddleClick.mc.field_1761.method_2906(0, ModuleMiddleClick.mc.field_1724.method_31548().field_7545 + 36, 0, class_1713.field_7790, (class_1657)ModuleMiddleClick.mc.field_1724);
                    ModuleMiddleClick.mc.field_1761.method_2906(0, slot, 0, class_1713.field_7790, (class_1657)ModuleMiddleClick.mc.field_1724);
                }
            }
        }
    }
}
