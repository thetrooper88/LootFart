package dev.loottech.api.utilities.render;

import net.minecraft.class_1007;
import net.minecraft.class_1268;
import net.minecraft.class_1306;
import net.minecraft.class_1309;
import net.minecraft.class_5600;
import net.minecraft.class_5602;
import net.minecraft.class_5607;
import net.minecraft.class_591;
import net.minecraft.class_742;

public class StaticBipedEntityModel<T extends class_742>
extends class_591<T> {
    private final T player;
    private float limbSwing;
    private float limbSwingAmount;
    private float yaw;
    private float bodyYaw;
    private float yawHead;
    private float pitch;
    private double x;
    private double y;
    private double z;

    public StaticBipedEntityModel(T player, boolean thinArms, float tickDelta) {
        super(((class_5607)class_5600.method_32073().get((Object)(thinArms ? class_5602.field_27581 : class_5602.field_27577))).method_32109(), thinArms);
        this.player = player;
        this.limbSwing = ((class_742)player).field_42108.method_48572(tickDelta);
        this.limbSwingAmount = ((class_742)player).field_42108.method_48570(tickDelta);
        this.yaw = player.method_36454();
        this.bodyYaw = player.method_43078();
        this.yawHead = player.method_5791();
        this.pitch = player.method_36455();
        this.field_3400 = player.method_5715();
        this.field_3395 = class_1007.method_4210(player, (class_1268)(player.method_6068() == class_1306.field_6183 ? class_1268.field_5808 : class_1268.field_5810));
        this.field_3399 = class_1007.method_4210(player, (class_1268)(player.method_6068() == class_1306.field_6183 ? class_1268.field_5810 : class_1268.field_5808));
        this.field_3447 = ((class_742)player).field_6251;
        this.x = player.method_23317();
        this.y = player.method_23318();
        this.z = player.method_23321();
        this.method_17086((class_1309)player, this.limbSwing, this.limbSwingAmount, tickDelta);
        this.method_17087((class_1309)player, this.limbSwing, this.limbSwingAmount, (float)((class_742)player).field_6012 + tickDelta, this.yaw, this.pitch);
    }

    public class_742 getPlayer() {
        return this.player;
    }

    public float getLimbSwing() {
        return this.limbSwing;
    }

    public float getLimbSwingAmount() {
        return this.limbSwingAmount;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getBodyYaw() {
        return this.bodyYaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }
}
