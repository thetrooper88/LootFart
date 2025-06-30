package dev.loottech.client.modules.combat;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.utilities.InventoryUtils;
import dev.loottech.api.utilities.Util;
import dev.loottech.client.modules.combat.AutoCrystal;
import dev.loottech.client.modules.combat.AutoMine;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2680;
import org.jetbrains.annotations.NotNull;

public static class AutoMine.MineData
implements Comparable<AutoMine.MineData> {
    private final class_2338 pos;
    private final class_2350 direction;
    private final AutoMine.MiningGoal goal;
    private int ticksMining;
    private float blockDamage;
    private float lastDamage;

    public AutoMine.MineData(class_2338 pos, class_2350 direction) {
        this.pos = pos;
        this.direction = direction;
        this.goal = AutoMine.MiningGoal.MINING_ENEMY;
    }

    public AutoMine.MineData(class_2338 pos, class_2350 direction, AutoMine.MiningGoal goal) {
        this.pos = pos;
        this.direction = direction;
        this.goal = goal;
    }

    public static AutoMine.MineData empty() {
        return new AutoMine.MineData(class_2338.field_10980, class_2350.field_11036);
    }

    private double getPriority() {
        double dist = Util.mc.field_1724.method_33571().method_1025(this.pos.method_10074().method_46558());
        if (dist <= (double)Managers.MODULE.getInstance(AutoCrystal.class).getPlaceRange()) {
            return 10.0;
        }
        return 0.0;
    }

    public int compareTo(@NotNull AutoMine.MineData o) {
        return Double.compare((double)this.getPriority(), (double)o.getPriority());
    }

    public boolean equals(Object obj) {
        AutoMine.MineData d;
        return obj instanceof AutoMine.MineData && (d = (AutoMine.MineData)obj).getPos().equals((Object)this.pos);
    }

    public void resetMiningTicks() {
        this.ticksMining = 0;
    }

    public void markAttemptedMine() {
        ++this.ticksMining;
    }

    public void addBlockDamage(float blockDamage) {
        this.lastDamage = this.blockDamage;
        this.blockDamage += blockDamage;
    }

    public void setTotalBlockDamage(float blockDamage, float lastDamage) {
        this.blockDamage = blockDamage;
        this.lastDamage = lastDamage;
    }

    public class_2338 getPos() {
        return this.pos;
    }

    public class_2350 getDirection() {
        return this.direction;
    }

    public AutoMine.MiningGoal getGoal() {
        return this.goal;
    }

    public int getTicksMining() {
        return this.ticksMining;
    }

    public float getBlockDamage() {
        return this.blockDamage;
    }

    public float getLastDamage() {
        return this.lastDamage;
    }

    public AutoMine.MineData copy() {
        AutoMine.MineData data = new AutoMine.MineData(this.pos, this.direction, this.goal);
        data.setTotalBlockDamage(this.blockDamage, this.lastDamage);
        return data;
    }

    public class_2680 getState() {
        return Util.mc.field_1687.method_8320(this.pos);
    }

    public int getBestSlot() {
        return InventoryUtils.getBestToolNoFallback(this.getState());
    }

    public int compareTo(@NotNull Object object) {
        return this.compareTo((AutoMine.MineData)object);
    }
}
