package dev.loottech;

import dev.loottech.api.manager.Managers;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LootTech
implements ModInitializer {
    public static final String NAME = "LootTech Beta";
    public static final String VERSION = "0.73";
    public static final Logger LOGGER = LoggerFactory.getLogger((String)"LootTech");

    public void onInitialize() {
        long startTime = System.currentTimeMillis();
        LOGGER.info("Initializing loot tech...");
        Managers.init();
        long endTime = System.currentTimeMillis();
        LOGGER.info("LootTech initialization finished in {} ms!", (Object)(endTime - startTime));
    }
}
