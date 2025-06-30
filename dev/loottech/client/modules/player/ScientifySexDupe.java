package dev.loottech.client.modules.player;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.client.events.PacketSendEvent;
import dev.loottech.client.values.impl.ValueBoolean;
import dev.loottech.client.values.impl.ValueNumber;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_124;
import net.minecraft.class_1268;
import net.minecraft.class_1657;
import net.minecraft.class_1713;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1935;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2561;
import net.minecraft.class_2596;
import net.minecraft.class_2813;
import net.minecraft.class_2815;
import net.minecraft.class_2828;
import net.minecraft.class_2846;
import net.minecraft.class_2848;
import net.minecraft.class_2886;
import net.minecraft.class_3545;
import net.minecraft.class_5250;

@RegisterModule(name="ScientifySexDupe", description="trident funny dupe", category=Module.Category.PLAYER)
public class ScientifySexDupe
extends Module {
    public ValueNumber delay = new ValueNumber("Delay", "Delay", "", (Number)Integer.valueOf((int)5), (Number)Integer.valueOf((int)0), (Number)Integer.valueOf((int)10));
    public ValueBoolean dropTridents = new ValueBoolean("Drop Tridents", "Drop Tridents", "", false);
    public ValueBoolean durabilityManagement = new ValueBoolean("DuraManagement", "DuraManagement", "", true);
    private final Queue<class_2596<?>> delayedPackets = new LinkedList();
    private boolean cancel = true;
    private final List<class_3545<Long, Runnable>> scheduledTasks = new ArrayList();
    private final List<class_3545<Long, Runnable>> scheduledTasks2 = new ArrayList();

    @EventHandler
    public void onSendPacket(PacketSendEvent event) {
        if (event.getPacket() instanceof class_2848 || event.getPacket() instanceof class_2828 || event.getPacket() instanceof class_2815) {
            return;
        }
        if (!(event.getPacket() instanceof class_2813) && !(event.getPacket() instanceof class_2846)) {
            return;
        }
        if (!this.cancel) {
            return;
        }
        class_5250 packetStr = class_2561.method_43470((String)event.getPacket().toString()).method_27692(class_124.field_1068);
        event.cancel();
    }

    @Override
    public void onEnable() {
        if (ScientifySexDupe.mc.field_1724 == null) {
            return;
        }
        for (int i = 0; i < 9; ++i) {
            if (ScientifySexDupe.mc.field_1724.method_31548().method_5438(i).method_7909() != class_1802.field_8547) continue;
            Integer n = ScientifySexDupe.mc.field_1724.method_31548().method_5438(i).method_7919();
        }
        class_2886 pckt = new class_2886(class_1268.field_5808, 10, -57.0f, 66.29f);
        Int2ObjectOpenHashMap modifiedStacks = new Int2ObjectOpenHashMap();
        modifiedStacks.put(3, (Object)ScientifySexDupe.mc.field_1724.method_31548().method_5438(ScientifySexDupe.mc.field_1724.method_31548().field_7545));
        modifiedStacks.put(36, (Object)ScientifySexDupe.mc.field_1724.method_31548().method_5438(ScientifySexDupe.mc.field_1724.method_31548().field_7545));
        class_2813 packet = new class_2813(0, 15, 0, 0, class_1713.field_7791, new class_1799((class_1935)class_1802.field_8162), (Int2ObjectMap)modifiedStacks);
        this.scheduledTasks.clear();
        this.dupe();
    }

    private void dupe() {
        int delayInt = this.delay.getValue().intValue() * 100;
        System.out.println(delayInt);
        int lowestHotbarSlot = 0;
        int lowestHotbarDamage = 1000;
        for (int i = 0; i < 9; ++i) {
            Integer currentHotbarDamage;
            if (ScientifySexDupe.mc.field_1724.method_31548().method_5438(i).method_7909() != class_1802.field_8547 || lowestHotbarDamage <= (currentHotbarDamage = Integer.valueOf((int)ScientifySexDupe.mc.field_1724.method_31548().method_5438(i).method_7919()))) continue;
            lowestHotbarSlot = i;
            lowestHotbarDamage = currentHotbarDamage;
        }
        ScientifySexDupe.mc.field_1761.method_2919((class_1657)ScientifySexDupe.mc.field_1724, class_1268.field_5808);
        this.cancel = true;
        int finalLowestHotbarSlot = lowestHotbarSlot;
        this.scheduleTask(() -> {
            this.cancel = false;
            if (this.durabilityManagement.getValue() && finalLowestHotbarSlot != 0) {
                ScientifySexDupe.mc.field_1761.method_2906(ScientifySexDupe.mc.field_1724.field_7512.field_7763, 44, 0, class_1713.field_7791, (class_1657)ScientifySexDupe.mc.field_1724);
                if (this.dropTridents.getValue()) {
                    ScientifySexDupe.mc.field_1761.method_2906(ScientifySexDupe.mc.field_1724.field_7512.field_7763, 44, 0, class_1713.field_7795, (class_1657)ScientifySexDupe.mc.field_1724);
                }
                ScientifySexDupe.mc.field_1761.method_2906(ScientifySexDupe.mc.field_1724.field_7512.field_7763, 36 + finalLowestHotbarSlot, 0, class_1713.field_7791, (class_1657)ScientifySexDupe.mc.field_1724);
            }
            ScientifySexDupe.mc.field_1761.method_2906(ScientifySexDupe.mc.field_1724.field_7512.field_7763, 3, 0, class_1713.field_7791, (class_1657)ScientifySexDupe.mc.field_1724);
            class_2846 packet2 = new class_2846(class_2846.class_2847.field_12974, class_2338.field_10980, class_2350.field_11033, 0);
            Managers.NETWORK.sendPacket((class_2596<?>)packet2);
            if (this.dropTridents.getValue()) {
                ScientifySexDupe.mc.field_1761.method_2906(ScientifySexDupe.mc.field_1724.field_7512.field_7763, 44, 0, class_1713.field_7795, (class_1657)ScientifySexDupe.mc.field_1724);
            }
            this.cancel = true;
            this.scheduleTask2(this::dupe, delayInt);
        }, delayInt);
    }

    public void scheduleTask(Runnable task, long delayMillis) {
        long executeTime = System.currentTimeMillis() + delayMillis;
        this.scheduledTasks.add((Object)new class_3545((Object)executeTime, (Object)task));
    }

    public void scheduleTask2(Runnable task, long delayMillis) {
        long executeTime = System.currentTimeMillis() + delayMillis;
        this.scheduledTasks2.add((Object)new class_3545((Object)executeTime, (Object)task));
    }

    @Override
    public void onTick() {
        class_3545 entry;
        long currentTime = System.currentTimeMillis();
        Iterator iterator = this.scheduledTasks.iterator();
        while (iterator.hasNext()) {
            entry = (class_3545)iterator.next();
            if ((Long)entry.method_15442() > currentTime) continue;
            ((Runnable)entry.method_15441()).run();
            iterator.remove();
        }
        iterator = this.scheduledTasks2.iterator();
        while (iterator.hasNext()) {
            entry = (class_3545)iterator.next();
            if ((Long)entry.method_15442() > currentTime) continue;
            ((Runnable)entry.method_15441()).run();
            iterator.remove();
        }
    }
}
