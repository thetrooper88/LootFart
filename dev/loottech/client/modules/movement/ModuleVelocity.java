package dev.loottech.client.modules.movement;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.api.utilities.PositionUtil;
import dev.loottech.asm.mixins.IEntityVelocityUpdateS2CPacket;
import dev.loottech.asm.mixins.IExplosionS2CPacket;
import dev.loottech.asm.mixins.accessor.AccessorBundlePacket;
import dev.loottech.asm.mixins.accessor.AccessorClientWorld;
import dev.loottech.asm.mixins.accessor.AccessorEntityVelocityUpdateS2CPacket;
import dev.loottech.asm.mixins.accessor.AccessorExplosionS2CPacket;
import dev.loottech.client.events.PacketReceiveEvent;
import dev.loottech.client.events.PushEvent;
import dev.loottech.client.modules.combat.AutoMine;
import dev.loottech.client.values.impl.ValueBoolean;
import dev.loottech.client.values.impl.ValueNumber;
import java.util.ArrayList;
import net.minecraft.class_124;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_2338;
import net.minecraft.class_2596;
import net.minecraft.class_2664;
import net.minecraft.class_2708;
import net.minecraft.class_2743;
import net.minecraft.class_3414;
import net.minecraft.class_3417;
import net.minecraft.class_3419;
import net.minecraft.class_8042;

@RegisterModule(name="Velocity", description="Remove the knockback of the player.", category=Module.Category.MOVEMENT)
public class ModuleVelocity
extends Module {
    public static ValueBoolean noPush = new ValueBoolean("NoPush", "NoPush", "", true);
    public static ValueNumber horizontal = new ValueNumber("Horizontal", "Horizontal", "", (Number)Float.valueOf((float)0.0f), (Number)Float.valueOf((float)0.0f), (Number)Float.valueOf((float)100.0f));
    public static ValueNumber vertical = new ValueNumber("Vertical", "Vertical", "", (Number)Float.valueOf((float)0.0f), (Number)Float.valueOf((float)0.0f), (Number)Float.valueOf((float)100.0f));
    public static ValueBoolean pauseLag = new ValueBoolean("PauseLag", "PauseLag", "Pause on lagbacks.", false);
    public static ValueBoolean wallsOnly = new ValueBoolean("WallsOnly", "WallsOnly", "", false);
    private boolean pause;

    @Override
    public void onDisable() {
        this.pause = false;
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        class_2743 packet;
        class_2596<?> class_25962;
        if (ModuleVelocity.mc.field_1724 == null || ModuleVelocity.mc.field_1687 == null || !ModuleVelocity.mc.field_1687.method_20812((class_1297)ModuleVelocity.mc.field_1724, ModuleVelocity.mc.field_1724.method_5829()).iterator().hasNext() && wallsOnly.getValue()) {
            return;
        }
        if (event.getPacket() instanceof class_2708 && pauseLag.getValue()) {
            this.pause = true;
        }
        if (wallsOnly.getValue() && (class_25962 = event.getPacket()) instanceof class_2743) {
            packet = (class_2743)class_25962;
            if (packet.method_11818() != ModuleVelocity.mc.field_1724.method_5628()) {
                return;
            }
            if (!this.isPhased()) {
                return;
            }
            if (this.pause && packet.method_11815() == 0.0 && packet.method_11819() == 0.0 && packet.method_11819() == 0.0) {
                this.pause = false;
                return;
            }
            if (horizontal.getValue().floatValue() == 0.0f && vertical.getValue().floatValue() == 0.0f) {
                event.cancel();
                return;
            }
            ((AccessorEntityVelocityUpdateS2CPacket)packet).setVelocityX((int)(packet.method_11815() * (double)(horizontal.getValue().floatValue() / 100.0f)));
            ((AccessorEntityVelocityUpdateS2CPacket)packet).setVelocityY((int)(packet.method_11816() * (double)(vertical.getValue().floatValue() / 100.0f)));
            ((AccessorEntityVelocityUpdateS2CPacket)packet).setVelocityZ((int)(packet.method_11819() * (double)(horizontal.getValue().floatValue() / 100.0f)));
        }
        if (wallsOnly.getValue() && (class_25962 = event.getPacket()) instanceof class_2664) {
            packet = (class_2664)class_25962;
            if (!this.isPhased()) {
                return;
            }
            if (horizontal.getValue().floatValue() == 0.0f && vertical.getValue().floatValue() == 0.0f) {
                event.cancel();
                return;
            }
            ((AccessorExplosionS2CPacket)packet).setPlayerVelocityX(packet.method_11472() * (horizontal.getValue().floatValue() / 100.0f));
            ((AccessorExplosionS2CPacket)packet).setPlayerVelocityY(packet.method_11473() * (vertical.getValue().floatValue() / 100.0f));
            ((AccessorExplosionS2CPacket)packet).setPlayerVelocityZ(packet.method_11474() * (horizontal.getValue().floatValue() / 100.0f));
        }
        if (wallsOnly.getValue() && (class_25962 = event.getPacket()) instanceof class_8042) {
            packet = (class_8042)class_25962;
            ArrayList allowedBundle = new ArrayList();
            for (class_2596 packet1 : packet.method_48324()) {
                if (packet1 instanceof class_2664) {
                    class_2664 packet2 = (class_2664)packet1;
                    mc.method_40000(() -> ((AccessorClientWorld)ModuleVelocity.mc.field_1687).hookPlaySound(packet2.method_11475(), packet2.method_11477(), packet2.method_11478(), (class_3414)class_3417.field_15152.comp_349(), class_3419.field_15245, 4.0f, (1.0f + (RANDOM.nextFloat() - RANDOM.nextFloat()) * 0.2f) * 0.7f, false, RANDOM.nextLong()));
                    if (!this.isPhased()) {
                        allowedBundle.add((Object)packet1);
                        continue;
                    }
                    if (horizontal.getValue().floatValue() == 0.0f && vertical.getValue().floatValue() == 0.0f) {
                        event.cancel();
                        return;
                    }
                    ((AccessorExplosionS2CPacket)packet2).setPlayerVelocityX(packet2.method_11472() * (horizontal.getValue().floatValue() / 100.0f));
                    ((AccessorExplosionS2CPacket)packet2).setPlayerVelocityY(packet2.method_11473() * (vertical.getValue().floatValue() / 100.0f));
                    ((AccessorExplosionS2CPacket)packet2).setPlayerVelocityZ(packet2.method_11474() * (horizontal.getValue().floatValue() / 100.0f));
                } else if (packet1 instanceof class_2743) {
                    class_2743 packet2 = (class_2743)packet1;
                    if (packet2.method_11818() != ModuleVelocity.mc.field_1724.method_5628()) {
                        allowedBundle.add((Object)packet1);
                        continue;
                    }
                    if (!this.isPhased()) {
                        allowedBundle.add((Object)packet1);
                        return;
                    }
                    if (horizontal.getValue().floatValue() == 0.0f && vertical.getValue().floatValue() == 0.0f) {
                        event.cancel();
                        return;
                    }
                    ((AccessorEntityVelocityUpdateS2CPacket)packet2).setVelocityX((int)(packet2.method_11815() * (double)(horizontal.getValue().floatValue() / 100.0f)));
                    ((AccessorEntityVelocityUpdateS2CPacket)packet2).setVelocityY((int)(packet2.method_11816() * (double)(vertical.getValue().floatValue() / 100.0f)));
                    ((AccessorEntityVelocityUpdateS2CPacket)packet2).setVelocityZ((int)(packet2.method_11819() * (double)(horizontal.getValue().floatValue() / 100.0f)));
                }
                allowedBundle.add((Object)packet1);
            }
            ((AccessorBundlePacket)packet).setIterable((Iterable<class_2596<?>>)allowedBundle);
        }
        if (!wallsOnly.getValue() && (class_25962 = event.getPacket()) instanceof class_2743) {
            packet = (class_2743)class_25962;
            class_2743 sPacketEntityVelocity = (class_2743)event.getPacket();
            if (sPacketEntityVelocity.method_11818() == ModuleVelocity.mc.field_1724.method_5628()) {
                if (this.pause && packet.method_11815() == 0.0 && packet.method_11819() == 0.0 && packet.method_11819() == 0.0) {
                    this.pause = false;
                    return;
                }
                if (horizontal.getValue().floatValue() == 0.0f && vertical.getValue().floatValue() == 0.0f) {
                    event.cancel();
                } else {
                    ((IEntityVelocityUpdateS2CPacket)sPacketEntityVelocity).setX((int)(sPacketEntityVelocity.method_11815() * (double)horizontal.getValue().floatValue() / 100.0));
                    ((IEntityVelocityUpdateS2CPacket)sPacketEntityVelocity).setY((int)(sPacketEntityVelocity.method_11816() * (double)vertical.getValue().floatValue() / 100.0));
                    ((IEntityVelocityUpdateS2CPacket)sPacketEntityVelocity).setZ((int)(sPacketEntityVelocity.method_11819() * (double)horizontal.getValue().floatValue() / 100.0));
                }
            }
        }
        if ((class_25962 = event.getPacket()) instanceof class_2664) {
            class_2664 sPacketExplosion = (class_2664)class_25962;
            ((IExplosionS2CPacket)sPacketExplosion).setX((int)(sPacketExplosion.method_11472() * horizontal.getValue().floatValue() / 100.0f));
            ((IExplosionS2CPacket)sPacketExplosion).setY((int)(sPacketExplosion.method_11473() * vertical.getValue().floatValue() / 100.0f));
            ((IExplosionS2CPacket)sPacketExplosion).setZ((int)(sPacketExplosion.method_11474() * horizontal.getValue().floatValue() / 100.0f));
        }
    }

    @Override
    public void onUpdate() {
        this.pause = false;
    }

    @Override
    public void onPush(PushEvent event) {
        if (noPush.getValue()) {
            event.cancel();
        }
    }

    private boolean isWallsTrapped() {
        class_2338 headPos = ModuleVelocity.mc.field_1724.method_24515().method_10086(ModuleVelocity.mc.field_1724.method_20448() ? 1 : 2);
        if (ModuleVelocity.mc.field_1687.method_8320(headPos).method_45474()) {
            return false;
        }
        return Managers.MODULE.getInstance(AutoMine.class).getSurroundNoDown((class_1657)ModuleVelocity.mc.field_1724).stream().noneMatch(blockPos -> ModuleVelocity.mc.field_1687.method_8320(ModuleVelocity.mc.field_1724.method_20448() ? blockPos : blockPos.method_10084()).method_45474());
    }

    private boolean isPhased() {
        return PositionUtil.getAllInBox(ModuleVelocity.mc.field_1724.method_5829()).stream().anyMatch(blockPos -> !ModuleVelocity.mc.field_1687.method_8320(blockPos).method_45474());
    }

    @Override
    public String getHudInfo() {
        return "H" + horizontal.getValue().floatValue() + "%" + String.valueOf((Object)class_124.field_1080) + "," + String.valueOf((Object)class_124.field_1068) + "V" + vertical.getValue().floatValue() + "%";
    }
}
