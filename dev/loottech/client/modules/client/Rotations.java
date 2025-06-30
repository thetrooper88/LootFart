package dev.loottech.client.modules.client;

import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.client.values.impl.ValueBoolean;
import dev.loottech.client.values.impl.ValueNumber;

@RegisterModule(name="Rotations", description="Manages client rotations for specific anti-cheats.", tag="Rotations", category=Module.Category.CLIENT, persistent=true, drawn=false)
public class Rotations
extends Module {
    public ValueNumber preserveTicksConfig = new ValueNumber("PreserveTicks", "PreserveTicks", "Time to preserve rotations after reaching the target rotations", (Number)Float.valueOf((float)0.0f), (Number)Float.valueOf((float)0.0f), (Number)Float.valueOf((float)20.0f));
    public ValueBoolean movementFixConfig = new ValueBoolean("MovementFix", "Fixes movement on Grim when rotating", false);
    public ValueBoolean mouseSensFixConfig = new ValueBoolean("MouseSensFix", "Fixes movement on Grim when applying mouse sensitivity", false);
}
