package dev.loottech.client.modules.combat;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_1297;
import net.minecraft.class_2338;

private static class AutoCrystal.DamageData<T> {
    private final List<String> tags = new ArrayList();
    private T damageData;
    private class_1297 attackTarget;
    private class_2338 blockPos;
    private double damage;
    private double selfDamage;
    private boolean antiSurround;

    public AutoCrystal.DamageData() {
    }

    public AutoCrystal.DamageData(class_2338 damageData, class_1297 attackTarget, double damage, double selfDamage, boolean antiSurround) {
        this.damageData = damageData;
        this.attackTarget = attackTarget;
        this.damage = damage;
        this.selfDamage = selfDamage;
        this.blockPos = damageData;
        this.antiSurround = antiSurround;
    }

    public AutoCrystal.DamageData(T damageData, class_1297 attackTarget, double damage, double selfDamage, class_2338 blockPos, boolean antiSurround) {
        this.damageData = damageData;
        this.attackTarget = attackTarget;
        this.damage = damage;
        this.selfDamage = selfDamage;
        this.blockPos = blockPos;
        this.antiSurround = antiSurround;
    }

    public void setDamageData(T damageData, class_1297 attackTarget, double damage, double selfDamage) {
        this.damageData = damageData;
        this.attackTarget = attackTarget;
        this.damage = damage;
        this.selfDamage = selfDamage;
    }

    public T getDamageData() {
        return this.damageData;
    }

    public class_1297 getAttackTarget() {
        return this.attackTarget;
    }

    public double getDamage() {
        return this.damage;
    }

    public double getSelfDamage() {
        return this.selfDamage;
    }

    public class_2338 getBlockPos() {
        return this.blockPos;
    }

    public boolean isAntiSurround() {
        return this.antiSurround;
    }
}
