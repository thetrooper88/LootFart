package dev.loottech.client.modules.movement;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.api.utilities.PositionUtil;
import dev.loottech.api.utilities.Timer;
import dev.loottech.asm.mixins.accessor.AccessorKeyBinding;
import dev.loottech.client.events.MovementPacketsEvent;
import dev.loottech.client.values.impl.ValueBoolean;
import dev.loottech.client.values.impl.ValueEnum;
import dev.loottech.client.values.impl.ValueNumber;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_2560;
import net.minecraft.class_2596;
import net.minecraft.class_2680;
import net.minecraft.class_2846;
import net.minecraft.class_2868;
import net.minecraft.class_2886;
import net.minecraft.class_304;
import net.minecraft.class_3532;
import net.minecraft.class_3675;

@RegisterModule(name="NoSlow", description="Remove the slow down of certain things.", category=Module.Category.MOVEMENT)
public class NoSlow
extends Module {
    private Timer timer = new Timer();
    double velocity;
    boolean addVelocity = false;
    public ValueBoolean inventoryMove = new ValueBoolean("Inventory Move", "Inventory Move", "Move while in inventory.", true);
    public ValueEnum items = new ValueEnum("Items", "Items", "", (Enum)ItemModes.Off);
    public final ValueEnum webs = new ValueEnum("Webs", "Webs", "", (Enum)WebModes.Off);
    public final ValueNumber speed = new ValueNumber("WebSpeed", "", (Number)Double.valueOf((double)3.0), (Number)Double.valueOf((double)0.1), (Number)Double.valueOf((double)10.0));

    @Override
    public String getHudInfo() {
        return this.items.getValue().name();
    }

    @Override
    public void onMovementPackets(MovementPacketsEvent event) {
        if (NoSlow.nullCheck()) {
            return;
        }
        if (NoSlow.mc.field_1724.method_6058() == class_1268.field_5810 && this.items.getValue() == ItemModes.Grim) {
            Managers.NETWORK.sendPacket((class_2596<?>)new class_2868(NoSlow.mc.field_1724.method_31548().field_7545 % 8 + 1));
            Managers.NETWORK.sendPacket((class_2596<?>)new class_2868(NoSlow.mc.field_1724.method_31548().field_7545 % 7 + 2));
            Managers.NETWORK.sendPacket((class_2596<?>)new class_2868(NoSlow.mc.field_1724.method_31548().field_7545));
        } else {
            Managers.NETWORK.sendSequencedPacket(id -> new class_2886(class_1268.field_5810, id, NoSlow.mc.field_1724.method_36454(), NoSlow.mc.field_1724.method_36455()));
        }
        this.velocity = NoSlow.mc.field_1724.method_18798().field_1352 > NoSlow.mc.field_1724.method_18798().field_1350 ? NoSlow.mc.field_1724.method_18798().field_1352 : NoSlow.mc.field_1724.method_18798().field_1350;
        if (this.items.getValue().equals((Object)ItemModes.GrimV3) && NoSlow.mc.field_1724.method_6115() && Math.abs((double)this.velocity) < 0.05 && this.timer.passedDms(8.0)) {
            NoSlow.mc.field_1724.method_18799(NoSlow.mc.field_1724.method_18798().method_18805(3.0, 1.0, 3.0));
            this.timer.reset();
        }
        if (this.items.getValue().equals((Object)ItemModes._2b2t) && NoSlow.mc.field_1724.method_6115() && Math.abs((double)this.velocity) < 0.05) {
            this.addVelocity = true;
        }
    }

    @Override
    public void onTick() {
        if (NoSlow.nullCheck()) {
            return;
        }
        if (this.inventoryMove.getValue()) {
            class_304[] keys;
            long handle = mc.method_22683().method_4490();
            for (class_304 binding : keys = new class_304[]{NoSlow.mc.field_1690.field_1903, NoSlow.mc.field_1690.field_1894, NoSlow.mc.field_1690.field_1881, NoSlow.mc.field_1690.field_1849, NoSlow.mc.field_1690.field_1913}) {
                binding.method_23481(class_3675.method_15987((long)handle, (int)((AccessorKeyBinding)binding).getBoundKey().method_1444()));
            }
            float yaw = NoSlow.mc.field_1724.method_36454();
            float pitch = NoSlow.mc.field_1724.method_36455();
            if (class_3675.method_15987((long)handle, (int)265)) {
                pitch -= 3.0f;
            } else if (class_3675.method_15987((long)handle, (int)264)) {
                pitch += 3.0f;
            } else if (class_3675.method_15987((long)handle, (int)263)) {
                yaw -= 3.0f;
            } else if (class_3675.method_15987((long)handle, (int)262)) {
                yaw += 3.0f;
            }
            NoSlow.mc.field_1724.method_36456(yaw);
            NoSlow.mc.field_1724.method_36457(class_3532.method_15363((float)pitch, (float)-90.0f, (float)90.0f));
        }
        if (!this.webs.getValue().equals((Object)WebModes.Off)) {
            if (NoSlow.isInWeb((class_1297)NoSlow.mc.field_1724)) {
                NoSlow.mc.field_1724.method_18800(NoSlow.mc.field_1724.method_18798().field_1352, NoSlow.mc.field_1724.method_18798().field_1351 - this.speed.getValue().doubleValue(), NoSlow.mc.field_1724.method_18798().field_1350);
            }
            if (this.webs.getValue().equals((Object)WebModes.Grim)) {
                class_238 bb = NoSlow.mc.field_1724.method_5829();
                for (class_2338 pos : this.getIntersectingWebs(bb)) {
                    Managers.NETWORK.sendPacket((class_2596<?>)new class_2846(class_2846.class_2847.field_12973, pos, class_2350.field_11033));
                    Managers.NETWORK.sendPacket((class_2596<?>)new class_2846(class_2846.class_2847.field_12971, pos, class_2350.field_11033));
                }
            }
        }
    }

    public static boolean isInWeb(class_1297 entity) {
        for (float x : new float[]{0.0f, 0.3f, -0.3f}) {
            for (float z : new float[]{0.0f, 0.3f, -0.3f}) {
                for (int y : new int[]{-1, 0, 1, 2}) {
                    class_2338 pos = class_2338.method_49637((double)(entity.method_23317() + (double)x), (double)entity.method_23318(), (double)(entity.method_23321() + (double)z)).method_10086(y);
                    if (!new class_238(pos).method_994(entity.method_5829()) || NoSlow.mc.field_1687.method_8320(pos).method_26204() != class_2246.field_10343) continue;
                    return true;
                }
            }
        }
        return false;
    }

    private List<class_2338> getIntersectingWebs(class_238 bb) {
        ArrayList blocks = new ArrayList();
        for (class_2338 pos : PositionUtil.getAllInBox(bb)) {
            class_2680 state = NoSlow.mc.field_1724.method_37908().method_8320(pos);
            if (!(state.method_26204() instanceof class_2560)) continue;
            blocks.add((Object)pos);
        }
        return blocks;
    }

    public static enum ItemModes {
        Off,
        Normal,
        Grim,
        GrimV3,
        _2b2t;

    }

    public static enum WebModes {
        Off,
        Normal,
        Grim;

    }
}
