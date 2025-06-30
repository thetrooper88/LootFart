package dev.loottech.api.manager.miscellaneous;

import dev.loottech.api.manager.Managers;

public static class ConfigManager.SaveThread
extends Thread {
    public void run() {
        Managers.CONFIG.saveFullConfig("autoSave");
    }
}
