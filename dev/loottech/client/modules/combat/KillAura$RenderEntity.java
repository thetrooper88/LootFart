package dev.loottech.client.modules.combat;

import dev.loottech.client.modules.combat.KillAura;
import net.minecraft.class_1297;

private class KillAura.RenderEntity {
    public class_1297 entity;
    public long startTime;

    public class_1297 getEntity() {
        return this.entity;
    }

    public KillAura.RenderEntity(KillAura killAura, class_1297 entity) {
        this.entity = entity;
        this.startTime = System.currentTimeMillis();
    }
}
