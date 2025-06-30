package dev.loottech.api.manager.miscellaneous;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.utilities.Util;
import dev.loottech.client.modules.combat.AutoMine;
import net.minecraft.class_1922;
import net.minecraft.class_2338;

public static class BlockManager.BreakEntry {
    private final int entityId;
    private final class_2338 pos;
    private long startTime;
    private float blockDamage;
    private boolean started;

    public BlockManager.BreakEntry(int entityId, class_2338 pos) {
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
