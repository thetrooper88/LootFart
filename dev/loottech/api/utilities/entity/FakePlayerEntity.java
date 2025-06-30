package dev.loottech.api.utilities.entity;

import com.mojang.authlib.GameProfile;
import dev.loottech.api.utilities.Util;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_310;
import net.minecraft.class_745;

public class FakePlayerEntity
extends class_745
implements Util {
    public static final AtomicInteger CURRENT_ID = new AtomicInteger(1000000);
    private final class_1657 player;

    public FakePlayerEntity(class_1657 player, String name) {
        super(class_310.method_1551().field_1687, new GameProfile(UUID.fromString((String)"2c83d964-298e-4559-b1bf-314f9ad63f7b"), name));
        this.player = player;
        this.method_5719((class_1297)player);
        this.field_5982 = this.method_36454();
        this.field_6004 = this.method_36455();
        this.field_6259 = this.field_6241 = player.field_6241;
        this.field_6220 = this.field_6283 = player.field_6283;
        Byte playerModel = (Byte)player.method_5841().method_12789(class_1657.field_7518);
        this.field_6011.method_12778(class_1657.field_7518, (Object)playerModel);
        this.method_6127().method_26846(player.method_6127());
        this.method_18380(player.method_18376());
        this.method_6033(player.method_6032());
        this.method_6073(player.method_6067());
        this.method_31548().method_7377(player.method_31548());
        this.method_5838(CURRENT_ID.incrementAndGet());
        this.field_6012 = 100;
    }

    public FakePlayerEntity(class_1657 player, GameProfile profile) {
        super(class_310.method_1551().field_1687, profile);
        this.player = player;
        this.method_5719((class_1297)player);
        this.field_5982 = this.method_36454();
        this.field_6004 = this.method_36455();
        this.field_6259 = this.field_6241 = player.field_6241;
        this.field_6220 = this.field_6283 = player.field_6283;
        Byte playerModel = (Byte)player.method_5841().method_12789(class_1657.field_7518);
        this.field_6011.method_12778(class_1657.field_7518, (Object)playerModel);
        this.method_6127().method_26846(player.method_6127());
        this.method_18380(player.method_18376());
        this.method_6033(player.method_6032());
        this.method_6073(player.method_6067());
        this.method_31548().method_7377(player.method_31548());
        this.method_5838(CURRENT_ID.incrementAndGet());
        this.field_6012 = 100;
    }

    public FakePlayerEntity(class_1657 player) {
        this(player, player.method_5477().getString());
    }

    public void spawnPlayer() {
        if (FakePlayerEntity.mc.field_1687 != null) {
            this.method_31482();
            FakePlayerEntity.mc.field_1687.method_53875((class_1297)this);
        }
    }

    public void despawnPlayer() {
        if (FakePlayerEntity.mc.field_1687 != null) {
            FakePlayerEntity.mc.field_1687.method_2945(this.method_5628(), class_1297.class_5529.field_26999);
            this.method_31745(class_1297.class_5529.field_26999);
        }
    }

    public boolean method_29504() {
        return false;
    }

    public class_1657 getPlayer() {
        return this.player;
    }
}
