package dev.loottech.client.modules.client;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.api.utilities.BlastResistantBlocks;
import dev.loottech.api.utilities.render.RenderBuffers;
import dev.loottech.api.utilities.render.RenderManager;
import dev.loottech.asm.mixins.accessor.MinecraftClientAccessor;
import dev.loottech.client.events.PacketSendEvent;
import dev.loottech.client.events.PreTickEvent;
import dev.loottech.client.events.Render3DEvent;
import dev.loottech.client.modules.client.ModuleColor;
import dev.loottech.client.values.impl.ValueBoolean;
import dev.loottech.client.values.impl.ValueCategory;
import dev.loottech.client.values.impl.ValueNumber;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1747;
import net.minecraft.class_1799;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2374;
import net.minecraft.class_238;
import net.minecraft.class_239;
import net.minecraft.class_2596;
import net.minecraft.class_2846;
import net.minecraft.class_2879;
import net.minecraft.class_3965;

@RegisterModule(name="AntiCheat", description="Manages client rotations for specific anti-cheats.", tag="AntiCheat", category=Module.Category.CLIENT, persistent=true, drawn=false)
public class AntiCheat
extends Module {
    public final ValueBoolean grim = new ValueBoolean("Grim", "Grim", "Use GrimAC based bypasses.", false);
    public final ValueBoolean miningFix = new ValueBoolean("MiningFix", "Fix mining on 2b2t", false);
    public final ValueBoolean grimCCFix = new ValueBoolean("GrimCC Mining Fix", "Don't mine above y 100 on grim.crystalpvp.cc.", false);
    private final ValueCategory airPlaceCategory = new ValueCategory("AirPlace", "Air-Place related settings");
    public final ValueBoolean manualAirPlace = new ValueBoolean("ManualAirPlace", "Air place manually.", this.airPlaceCategory, true);
    public final ValueNumber manualDistance = new ValueNumber("Manual Distance", "Manual air-place distacnce.", this.airPlaceCategory, (Number)Float.valueOf((float)4.0f), (Number)Float.valueOf((float)1.0f), (Number)Float.valueOf((float)6.0f));
    public final ValueBoolean grimAirPlace = new ValueBoolean("GrimAirPlace", "GrimAirPlace", "Air place on 2b2t.", this.airPlaceCategory, false);
    private int airPlaceTicks;

    @Override
    public void onPreTick(PreTickEvent event) {
        class_3965 result;
        if (this.airPlaceTicks > 0) {
            --this.airPlaceTicks;
        }
        if (AntiCheat.mc.field_1724 == null || AntiCheat.mc.field_1761 == null || !this.manualAirPlace.getValue()) {
            return;
        }
        class_239 class_2392 = AntiCheat.mc.field_1765;
        if (class_2392 instanceof class_3965 && !AntiCheat.mc.field_1687.method_22347((result = (class_3965)class_2392).method_17777())) {
            return;
        }
        class_1799 stack = AntiCheat.mc.field_1724.method_6047();
        if (stack.method_7960() || !(stack.method_7909() instanceof class_1747) || !AntiCheat.mc.field_1690.field_1904.method_1434()) {
            return;
        }
        class_239 result2 = AntiCheat.mc.field_1724.method_5745((double)this.manualDistance.getValue().floatValue(), 1.0f, false);
        if (((MinecraftClientAccessor)mc).hookGetItemUseCooldown() == 0 && this.airPlaceTicks == 0 && !AntiCheat.mc.field_1724.method_6115() && result2 instanceof class_3965) {
            class_3965 blockHitResult = (class_3965)result2;
            class_2338 blockPos = class_2338.method_49638((class_2374)blockHitResult.method_17784());
            if (!AntiCheat.mc.field_1687.method_22347(blockPos) || this.isEntityInBlockPos(blockPos)) {
                return;
            }
            ((MinecraftClientAccessor)mc).hookSetItemUseCooldown(4);
            this.airPlaceTicks = 4;
            if (this.grimAirPlace.getValue()) {
                Managers.NETWORK.sendPacket((class_2596<?>)new class_2846(class_2846.class_2847.field_12969, class_2338.field_10980, class_2350.field_11033));
                AntiCheat.mc.field_1761.method_2896(AntiCheat.mc.field_1724, class_1268.field_5810, blockHitResult);
                AntiCheat.mc.field_1724.method_23667(class_1268.field_5808, false);
                Managers.NETWORK.sendPacket((class_2596<?>)new class_2879(class_1268.field_5810));
                Managers.NETWORK.sendPacket((class_2596<?>)new class_2846(class_2846.class_2847.field_12969, class_2338.field_10980, class_2350.field_11033));
            } else {
                AntiCheat.mc.field_1761.method_2896(AntiCheat.mc.field_1724, class_1268.field_5808, blockHitResult);
                AntiCheat.mc.field_1724.method_6104(class_1268.field_5808);
            }
        }
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        class_3965 result;
        if (AntiCheat.mc.field_1724 == null || !this.manualAirPlace.getValue()) {
            return;
        }
        class_239 class_2392 = AntiCheat.mc.field_1765;
        if (class_2392 instanceof class_3965 && !AntiCheat.mc.field_1687.method_22347((result = (class_3965)class_2392).method_17777())) {
            return;
        }
        class_1799 stack = AntiCheat.mc.field_1724.method_6047();
        if (stack.method_7960() || !(stack.method_7909() instanceof class_1747)) {
            return;
        }
        class_239 result2 = AntiCheat.mc.field_1724.method_5745((double)this.manualDistance.getValue().floatValue(), 1.0f, false);
        if (!(result2 instanceof class_3965)) {
            return;
        }
        class_3965 blockHitResult = (class_3965)result2;
        class_2338 blockPos = class_2338.method_49638((class_2374)blockHitResult.method_17784());
        if (!AntiCheat.mc.field_1687.method_22347(blockPos) || this.isEntityInBlockPos(blockPos)) {
            return;
        }
        RenderBuffers.preRender();
        RenderManager.renderBoundingBox(event.getMatrices(), blockPos, 1.5f, Managers.MODULE.getInstance(ModuleColor.class).color.getValue().getRGB());
        RenderBuffers.postRender();
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        class_2846 packet;
        class_2596<?> class_25962;
        if (this.miningFix.getValue() && (class_25962 = event.getPacket()) instanceof class_2846 && ((packet = (class_2846)class_25962).method_12363() == class_2846.class_2847.field_12968 || packet.method_12363() == class_2846.class_2847.field_12973 || packet.method_12363() == class_2846.class_2847.field_12971)) {
            if (BlastResistantBlocks.isUnbreakable(packet.method_12362())) {
                event.cancel();
                return;
            }
            if (packet.method_12363() == class_2846.class_2847.field_12968) {
                Managers.NETWORK.sendQuietPacket((class_2596<?>)new class_2846(class_2846.class_2847.field_12973, packet.method_12362(), class_2350.field_11036));
                Managers.NETWORK.sendQuietPacket((class_2596<?>)new class_2846(class_2846.class_2847.field_12968, packet.method_12362(), class_2350.field_11036));
                Managers.NETWORK.sendQuietPacket((class_2596<?>)new class_2846(class_2846.class_2847.field_12971, packet.method_12362(), class_2350.field_11036));
                event.cancel();
            }
        }
    }

    private boolean isEntityInBlockPos(class_2338 blockPos) {
        for (class_1297 entity : AntiCheat.mc.field_1687.method_18112()) {
            if (!entity.method_5829().method_994(new class_238(blockPos))) continue;
            return true;
        }
        return false;
    }
}
