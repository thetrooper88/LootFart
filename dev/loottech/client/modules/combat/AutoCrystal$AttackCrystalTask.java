package dev.loottech.client.modules.combat;

import dev.loottech.client.modules.combat.AutoCrystal;
import java.util.List;
import java.util.concurrent.Callable;
import net.minecraft.class_1297;
import net.minecraft.class_1511;

private class AutoCrystal.AttackCrystalTask
implements Callable<AutoCrystal.DamageData<class_1511>> {
    private final List<class_1297> threadSafeEntities;

    public AutoCrystal.AttackCrystalTask(List<class_1297> threadSafeEntities) {
        this.threadSafeEntities = threadSafeEntities;
    }

    public AutoCrystal.DamageData<class_1511> call() throws Exception {
        return AutoCrystal.this.calculateAttackCrystal(this.threadSafeEntities);
    }

    public Object call() throws Exception {
        return this.call();
    }
}
