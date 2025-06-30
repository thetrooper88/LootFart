package dev.loottech.client.modules.movement;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.api.utilities.PlayerUtils;
import dev.loottech.client.events.JumpRotationEvent;
import dev.loottech.client.events.PreTickEvent;
import dev.loottech.client.events.SprintCancelEvent;
import dev.loottech.client.values.impl.ValueBoolean;
import dev.loottech.client.values.impl.ValueEnum;
import net.minecraft.class_1294;
import net.minecraft.class_3532;

@RegisterModule(name="Sprint", tag="Sprint", description="Always be sprinting.", category=Module.Category.MOVEMENT)
public class ModuleSprint
extends Module {
    ValueEnum modeConfig = new ValueEnum("Mode", "Mode", "Sprinting mode. Rage allows for multi-directional sprinting.", (Enum)SprintMode.LEGIT);
    ValueBoolean jumpFixConfig = new ValueBoolean("JumpFix", "Fixes jumping slowdown in Rage sprint", true);

    /*
     * Exception decompiling
     */
    @Override
    public void onPreTick(PreTickEvent event) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter$TooOptimisticMatchException
         */
        throw new IllegalStateException("Decompilation failed");
    }

    @Override
    public void onSprintCancel(SprintCancelEvent event) {
        if (this.canSprint() && (this.modeConfig.getValue() == SprintMode.RAGE || this.modeConfig.getValue() == SprintMode.RAGE_STRICT)) {
            float sprintYaw = this.getSprintYaw(ModuleSprint.mc.field_1724.method_36454());
            if (this.checkSprintAngle(sprintYaw)) {
                return;
            }
            event.cancel();
        }
    }

    @Override
    public void onUpdate() {
        if (this.canSprint() && this.modeConfig.getValue() == SprintMode.RAGE_STRICT) {
            Managers.ROTATION.setRotation(this.getSprintYaw(ModuleSprint.mc.field_1724.method_36454()), ModuleSprint.mc.field_1724.method_36455());
        }
    }

    @Override
    public void onJumpRotation(JumpRotationEvent event) {
        if (this.jumpFixConfig.getValue() && (this.modeConfig.getValue() == SprintMode.RAGE || this.modeConfig.getValue() == SprintMode.RAGE_STRICT)) {
            float yaw = event.getYaw();
            float forward = Math.signum((float)ModuleSprint.mc.field_1724.field_3913.field_3905);
            float strafe = 90.0f * Math.signum((float)ModuleSprint.mc.field_1724.field_3913.field_3907);
            if (forward != 0.0f) {
                strafe *= forward * 0.5f;
            }
            yaw -= strafe;
            if (forward < 0.0f) {
                yaw -= 180.0f;
            }
            event.cancel();
            event.setYaw(yaw);
        }
    }

    public static boolean isInputtingMovement() {
        return ModuleSprint.mc.field_1690.field_1894.method_1434() || ModuleSprint.mc.field_1690.field_1881.method_1434() || ModuleSprint.mc.field_1690.field_1913.method_1434() || ModuleSprint.mc.field_1690.field_1849.method_1434();
    }

    private boolean canSprint() {
        if (PlayerUtils.inWeb(1.0)) {
            return false;
        }
        return ModuleSprint.isInputtingMovement() && !ModuleSprint.mc.field_1724.method_5715() && !ModuleSprint.mc.field_1724.method_3144() && !ModuleSprint.mc.field_1724.method_6128() && !ModuleSprint.mc.field_1724.method_5799() && !ModuleSprint.mc.field_1724.method_5771() && !ModuleSprint.mc.field_1724.method_21754() && !ModuleSprint.mc.field_1724.method_6059(class_1294.field_5919) && (float)ModuleSprint.mc.field_1724.method_7344().method_7586() > 6.0f;
    }

    private boolean checkSprintAngle(float sprintYaw) {
        if (this.modeConfig.getValue() == SprintMode.RAGE_STRICT) {
            return class_3532.method_15356((float)sprintYaw, (float)Managers.ROTATION.getServerYaw()) > 0.0f;
        }
        if (this.modeConfig.getValue() == SprintMode.GRIM) {
            return class_3532.method_15356((float)ModuleSprint.mc.field_1724.method_36454(), (float)Managers.ROTATION.getServerYaw()) > 0.0f;
        }
        return false;
    }

    public float getSprintYaw(float yaw) {
        boolean forward = ModuleSprint.mc.field_1690.field_1894.method_1434();
        boolean backward = ModuleSprint.mc.field_1690.field_1881.method_1434();
        boolean left = ModuleSprint.mc.field_1690.field_1913.method_1434();
        boolean right = ModuleSprint.mc.field_1690.field_1849.method_1434();
        if (forward && !backward) {
            if (left && !right) {
                yaw -= 45.0f;
            } else if (right && !left) {
                yaw += 45.0f;
            }
        } else if (backward && !forward) {
            yaw += 180.0f;
            if (left && !right) {
                yaw += 45.0f;
            } else if (right && !left) {
                yaw -= 45.0f;
            }
        } else if (left && !right) {
            yaw -= 90.0f;
        } else if (right && !left) {
            yaw += 90.0f;
        }
        return class_3532.method_15393((float)yaw);
    }

    public static enum SprintMode {
        LEGIT,
        RAGE,
        RAGE_STRICT,
        GRIM;

    }
}
