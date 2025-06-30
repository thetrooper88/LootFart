package dev.loottech.client.modules.visuals;

import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.client.values.impl.ValueBoolean;
import dev.loottech.client.values.impl.ValueNumber;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_1297;
import net.minecraft.class_1299;
import net.minecraft.class_1538;
import net.minecraft.class_1657;
import net.minecraft.class_1937;
import net.minecraft.class_3417;

@RegisterModule(name="DeathEffects", tag="DeathEffects", description="Summon lightning at player deaths.", category=Module.Category.VISUALS)
public class DeathEffects
extends Module {
    private final ValueBoolean self = new ValueBoolean("Self", "Self", "Summon lightning on self death", true);
    private final ValueBoolean sound = new ValueBoolean("Sound", "Sound", "Play sound for lightning", true);
    private final ValueNumber volume = new ValueNumber("Volume", "Volume", "Volume of lightning sound", (Number)Double.valueOf((double)1.0), (Number)Double.valueOf((double)0.1), (Number)Double.valueOf((double)5.0));
    List<class_1657> lightningList = new ArrayList();
    List<class_1657> playerList = new ArrayList();

    @Override
    public void onTick() {
        if (DeathEffects.mc.field_1724 == null || DeathEffects.mc.field_1687 == null) {
            return;
        }
        for (class_1657 player : this.lightningList) {
            if (this.playerList.contains((Object)player)) continue;
            this.lightningList.remove((Object)player);
        }
        for (class_1657 player : DeathEffects.mc.field_1687.method_18456()) {
            class_1538 lightning;
            this.playerList.add((Object)player);
            if (player == null || player.method_6032() > 0.0f || this.lightningList.contains((Object)player) || player.method_6032() != 0.0f) continue;
            if (player == DeathEffects.mc.field_1724 && this.self.getValue()) {
                lightning = new class_1538(class_1299.field_6112, (class_1937)DeathEffects.mc.field_1687);
                this.summonLightning(lightning);
                continue;
            }
            lightning = new class_1538(class_1299.field_6112, (class_1937)DeathEffects.mc.field_1687);
            this.lightningList.add((Object)player);
            this.summonLightning(lightning);
        }
    }

    public void summonLightning(class_1538 lightning) {
        lightning.method_30634(lightning.method_23317(), lightning.method_23318(), lightning.method_23321());
        DeathEffects.mc.field_1687.method_53875((class_1297)lightning);
        if (this.sound.getValue()) {
            DeathEffects.mc.field_1724.method_5783(class_3417.field_14865, this.volume.getValue().floatValue(), 1.0f);
        }
    }
}
