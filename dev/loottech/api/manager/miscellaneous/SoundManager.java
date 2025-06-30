package dev.loottech.api.manager.miscellaneous;

import dev.loottech.api.utilities.Util;
import net.minecraft.class_2378;
import net.minecraft.class_2960;
import net.minecraft.class_3414;
import net.minecraft.class_7923;

public class SoundManager
implements Util {
    public static final class_3414 KILL = SoundManager.registerSoundEvent("kill_sound");
    public static final class_3414 MODULE = SoundManager.registerSoundEvent("module_sound");

    public void playSound(class_3414 sound) {
        this.playSound(sound, 1.0f, 1.0f);
    }

    public void playSound(class_3414 sound, float volume, float pitch) {
        if (SoundManager.mc.field_1724 != null) {
            mc.method_40000(() -> SoundManager.mc.field_1724.method_5783(sound, volume, pitch));
        }
    }

    private static class_3414 registerSoundEvent(String name) {
        class_2960 id = class_2960.method_60655((String)"loottech", (String)name);
        return (class_3414)class_2378.method_10230((class_2378)class_7923.field_41172, (class_2960)id, (Object)class_3414.method_47908((class_2960)id));
    }
}
