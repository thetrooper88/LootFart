package dev.loottech.client.modules.miscellaneous;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.miscellaneous.SoundManager;
import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.client.events.DeathEvent;
import dev.loottech.client.values.impl.ValueBoolean;
import dev.loottech.client.values.impl.ValueNumber;
import java.util.Objects;
import net.minecraft.class_1657;

@RegisterModule(name="KillSound", tag="KillSound", description="Play a sound when a player dies.", category=Module.Category.MISCELLANEOUS)
public class KillSound
extends Module {
    public final ValueNumber volume = new ValueNumber("Volume", "Volume", "How loud the sound is", (Number)Float.valueOf((float)1.0f), (Number)Float.valueOf((float)0.1f), (Number)Float.valueOf((float)2.0f));
    private final ValueBoolean self = new ValueBoolean("Self", "Play a sound when you die.", false);
    private final ValueBoolean friends = new ValueBoolean("Friends", "Play a sound when friends die.", false);

    @Override
    public void onDeath(DeathEvent event) {
        if (event.getEntity() instanceof class_1657) {
            if (Objects.equals((Object)event.getEntity().method_5477().method_54160(), (Object)KillSound.mc.field_1724.method_5477().method_54160()) && this.self.getValue()) {
                Managers.SOUND.playSound(SoundManager.KILL, this.volume.getValue().floatValue() * 2.0f, 1.0f);
            }
            if (!this.friends.getValue() || !Managers.FRIEND.isFriend((class_1657)event.getEntity())) {
                Managers.SOUND.playSound(SoundManager.KILL, this.volume.getValue().floatValue() * 2.0f, 1.0f);
            }
        }
    }
}
