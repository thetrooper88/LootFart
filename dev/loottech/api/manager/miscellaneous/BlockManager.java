package dev.loottech.api.manager.miscellaneous;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.event.EventListener;
import dev.loottech.api.utilities.Util;
import dev.loottech.client.events.PacketReceiveEvent;
import dev.loottech.client.events.TickEvent;
import dev.loottech.client.modules.combat.AutoMine;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import net.minecraft.class_1922;
import net.minecraft.class_2338;
import net.minecraft.class_2596;
import net.minecraft.class_2620;

public class BlockManager
implements Util,
EventListener {
    private final List<BreakEntry> breakPositions = new CopyOnWriteArrayList();

    @Override
    public void onTick(TickEvent event) {
        if (BlockManager.mc.field_1724 == null || BlockManager.mc.field_1687 == null) {
            this.breakPositions.clear();
            return;
        }
        for (BreakEntry blockEntry : this.breakPositions) {
            blockEntry.updateDamage();
        }
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (BlockManager.mc.field_1724 == null || BlockManager.mc.field_1687 == null) {
            return;
        }
        class_2596<?> class_25962 = event.getPacket();
        if (class_25962 instanceof class_2620) {
            class_2620 packet = (class_2620)class_25962;
            if (this.countBreaks(packet.method_11280()) >= 2L) {
                this.breakPositions.stream().filter(d -> d.getEntityId() == packet.method_11280()).min(Comparator.comparingLong(BreakEntry::getStartTime)).ifPresent(arg_0 -> this.breakPositions.remove(arg_0));
            }
            BreakEntry data = new BreakEntry(packet.method_11280(), packet.method_11277());
            data.startMining();
            this.breakPositions.add((Object)data);
        }
    }

    public long countBreaks(int entityId) {
        return this.breakPositions.stream().filter(d -> d.getEntityId() == entityId).count();
    }

    public boolean isInstantMine(class_2338 pos) {
        return ((BreakEntry)this.breakPositions.getFirst()).getPos().equals((Object)pos);
    }

    public boolean isBreaking(class_2338 pos) {
        return this.breakPositions.stream().anyMatch(d -> d.getPos().equals((Object)pos));
    }

    public boolean isPassed(class_2338 pos, float blockDamage) {
        return this.breakPositions.stream().anyMatch(d -> d.getPos().equals((Object)pos) && d.getBlockDamage() >= blockDamage);
    }

    public Set<class_2338> getMines(float blockDamage) {
        return (Set)this.breakPositions.stream().filter(d -> this.isPassed(d.getPos(), blockDamage)).map(BreakEntry::getPos).collect(Collectors.toSet());
    }

    public static class BreakEntry {
        private final int entityId;
        private final class_2338 pos;
        private long startTime;
        private float blockDamage;
        private boolean started;

        public BreakEntry(int entityId, class_2338 pos) {
            this.entityId = entityId;
            this.pos = pos;
        }

        public void updateDamage() {
            if (this.started) {
                this.blockDamage += Managers.MODULE.getInstance(AutoMine.class).calcBlockBreakingDelta(Util.mc.field_1687.method_8320(this.pos), (class_1922)Util.mc.field_1687, this.pos);
            }
        }

        public void startMining() {
            this.started = true;
            this.startTime = System.currentTimeMillis();
        }

        public class_2338 getPos() {
            return this.pos;
        }

        public float getBlockDamage() {
            return Math.min((float)this.blockDamage, (float)1.0f);
        }

        public int getEntityId() {
            return this.entityId;
        }

        public long getStartTime() {
            return this.startTime;
        }
    }
}
