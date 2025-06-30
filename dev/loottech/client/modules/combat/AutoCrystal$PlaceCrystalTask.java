package dev.loottech.client.modules.combat;

import dev.loottech.client.modules.combat.AutoCrystal;
import java.util.List;
import java.util.concurrent.Callable;
import net.minecraft.class_1297;
import net.minecraft.class_2338;

private class AutoCrystal.PlaceCrystalTask
implements Callable<AutoCrystal.DamageData<class_2338>> {
    private final List<class_2338> threadSafeBlocks;
    private final List<class_1297> threadSafeEntities;

    public AutoCrystal.PlaceCrystalTask(List<class_2338> threadSafeBlocks, List<class_1297> threadSafeEntities) {
        this.threadSafeBlocks = threadSafeBlocks;
        this.threadSafeEntities = threadSafeEntities;
    }

    public AutoCrystal.DamageData<class_2338> call() throws Exception {
        return AutoCrystal.this.calculatePlaceCrystal(this.threadSafeBlocks, this.threadSafeEntities);
    }

    public Object call() throws Exception {
        return this.call();
    }
}
