package dev.loottech.api.manager.module;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.rotation.Rotation;
import dev.loottech.client.values.impl.ValueBoolean;
import java.util.Comparator;
import java.util.function.Predicate;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_742;
import net.minecraft.class_746;

public class CombatModule
extends Module {
    protected ValueBoolean multitask = new ValueBoolean("MultiTask", "Allows actions while using items", false);
    private final int rotationPriority;

    public CombatModule(int rotationPriority) {
        this.rotationPriority = rotationPriority;
    }

    protected void setRotation(float yaw, float pitch) {
        Managers.ROTATION.setRotation(new Rotation(this.getRotationPriority(), yaw, pitch));
    }

    protected void setRotationSilent(float yaw, float pitch) {
        Managers.ROTATION.setRotationSilent(yaw, pitch);
    }

    protected void setRotationClient(float yaw, float pitch) {
        Managers.ROTATION.setRotationClient(yaw, pitch);
    }

    protected boolean isRotationBlocked() {
        return Managers.ROTATION.isRotationBlocked(this.getRotationPriority());
    }

    protected int getRotationPriority() {
        return this.rotationPriority;
    }

    public class_1657 getClosestPlayer(double range) {
        return (class_1657)CombatModule.mc.field_1687.method_18456().stream().filter(e -> !(e instanceof class_746) && !e.method_7325()).filter(e -> CombatModule.mc.field_1724.method_5858((class_1297)e) <= range * range).filter(e -> !Managers.FRIEND.isFriend((class_1657)e)).min(Comparator.comparingDouble(e -> CombatModule.mc.field_1724.method_5858((class_1297)e))).orElse(null);
    }

    public class_1657 getClosestPlayer(Predicate<class_742> entityPredicate, double range) {
        return (class_1657)CombatModule.mc.field_1687.method_18456().stream().filter(e -> !(e instanceof class_746) && !e.method_7325()).filter(entityPredicate).filter(e -> CombatModule.mc.field_1724.method_5858((class_1297)e) <= range * range).filter(e -> !Managers.FRIEND.isFriend((class_1657)e)).min(Comparator.comparingDouble(e -> CombatModule.mc.field_1724.method_5858((class_1297)e))).orElse(null);
    }

    public boolean checkMultitask() {
        return this.checkMultitask(false);
    }

    public boolean checkMultitask(boolean checkOffhand) {
        if (checkOffhand && CombatModule.mc.field_1724.method_6058() != class_1268.field_5808) {
            return false;
        }
        return CombatModule.mc.field_1724.method_6115();
    }
}
