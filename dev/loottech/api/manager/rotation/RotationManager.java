package dev.loottech.api.manager.rotation;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.event.EventListener;
import dev.loottech.api.manager.rotation.Rotation;
import dev.loottech.api.utilities.PlayerUtils;
import dev.loottech.api.utilities.Util;
import dev.loottech.api.utilities.render.Interpolation;
import dev.loottech.asm.ducks.IClientPlayerEntity;
import dev.loottech.client.events.KeyboardTickEvent;
import dev.loottech.client.events.MovementPacketsEvent;
import dev.loottech.client.events.PacketSendEvent;
import dev.loottech.client.events.PostMotionEvent;
import dev.loottech.client.events.PostPlayerJumpEvent;
import dev.loottech.client.events.PrePlayerJumpEvent;
import dev.loottech.client.events.RenderPlayerEvent;
import dev.loottech.client.events.UpdateVelocityEvent;
import dev.loottech.client.modules.client.Rotations;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2828;
import net.minecraft.class_3532;

public class RotationManager
implements Util,
EventListener {
    private final List<Rotation> requests = new CopyOnWriteArrayList();
    boolean rotate;
    private float serverYaw;
    private float serverPitch;
    private float lastServerYaw;
    private float lastServerPitch;
    private float prevJumpYaw;
    private float prevYaw;
    private float prevPitch;
    private Rotation rotation;
    private int rotateTicks;
    private boolean webJumpFix;
    private boolean preJumpFix;

    @Override
    public void onPacketSend(PacketSendEvent event) {
        class_2828 packet;
        if (RotationManager.mc.field_1724 == null || RotationManager.mc.field_1687 == null) {
            return;
        }
        class_2596<?> class_25962 = event.getPacket();
        if (class_25962 instanceof class_2828 && (packet = (class_2828)class_25962).method_36172()) {
            float packetYaw = packet.method_12271(0.0f);
            float packetPitch = packet.method_12270(0.0f);
            this.serverYaw = packetYaw;
            this.serverPitch = packetPitch;
        }
    }

    @Override
    public void onMovementPackets(MovementPacketsEvent event) {
        if (this.rotation != null) {
            if (this.rotate) {
                this.removeRotation(this.rotation);
                event.cancel();
                event.setYaw(this.rotation.getYaw());
                event.setPitch(this.rotation.getPitch());
                this.rotate = false;
            }
            if (this.rotation.isSnap()) {
                this.rotation = null;
            }
        }
        this.webJumpFix = PlayerUtils.inWeb(1.0);
        if (this.requests.isEmpty()) {
            this.rotation = null;
            return;
        }
        Rotation request = this.getRotationRequest();
        if (request == null) {
            if (this.isDoneRotating()) {
                this.rotation = null;
                return;
            }
        } else {
            this.rotation = request;
        }
        if (this.rotation == null) {
            return;
        }
        this.rotateTicks = 0;
        this.rotate = true;
    }

    @Override
    public void onPostMotion(PostMotionEvent event) {
        this.lastServerYaw = ((IClientPlayerEntity)RotationManager.mc.field_1724).loottech$getLastSpoofedYaw();
        this.lastServerPitch = ((IClientPlayerEntity)RotationManager.mc.field_1724).loottech$getLastSpoofedPitch();
    }

    @Override
    public void onKeyboardTick(KeyboardTickEvent event) {
        if (this.rotation != null && RotationManager.mc.field_1724 != null && Managers.MODULE.getInstance(Rotations.class).movementFixConfig.getValue()) {
            float forward = RotationManager.mc.field_1724.field_3913.field_3905;
            float sideways = RotationManager.mc.field_1724.field_3913.field_3907;
            float delta = (RotationManager.mc.field_1724.method_36454() - this.rotation.getYaw()) * ((float)Math.PI / 180);
            float cos = class_3532.method_15362((float)delta);
            float sin = class_3532.method_15374((float)delta);
            RotationManager.mc.field_1724.field_3913.field_3907 = Math.round((float)(sideways * cos - forward * sin));
            RotationManager.mc.field_1724.field_3913.field_3905 = Math.round((float)(forward * cos + sideways * sin));
        }
    }

    @Override
    public void onUpdateVelocity(UpdateVelocityEvent event) {
        if (this.rotation != null && Managers.MODULE.getInstance(Rotations.class).movementFixConfig.getValue()) {
            event.cancel();
            event.setVelocity(this.movementInputToVelocity(this.rotation.getYaw(), event.getMovementInput(), event.getSpeed()));
        }
    }

    @Override
    public void onPrePlayerJump(PrePlayerJumpEvent event) {
        if (this.rotation != null && Managers.MODULE.getInstance(Rotations.class).movementFixConfig.getValue()) {
            this.prevJumpYaw = RotationManager.mc.field_1724.method_36454();
            RotationManager.mc.field_1724.method_36456(this.rotation.getYaw());
            if (this.webJumpFix) {
                this.preJumpFix = RotationManager.mc.field_1724.method_5624();
                RotationManager.mc.field_1724.method_5728(false);
            }
        }
    }

    @Override
    public void onPostPlayerJump(PostPlayerJumpEvent event) {
        if (this.rotation != null && Managers.MODULE.getInstance(Rotations.class).movementFixConfig.getValue()) {
            RotationManager.mc.field_1724.method_36456(this.prevJumpYaw);
            if (this.webJumpFix) {
                RotationManager.mc.field_1724.method_5728(this.preJumpFix);
            }
        }
    }

    @Override
    public void onRenderPlayer(RenderPlayerEvent event) {
        if (event.getEntity() == RotationManager.mc.field_1724 && this.rotation != null) {
            event.setYaw(Interpolation.interpolateFloat(this.prevYaw, this.getServerYaw(), mc.method_60646().method_60637(true)));
            event.setPitch(Interpolation.interpolateFloat(this.prevPitch, this.getServerPitch(), mc.method_60646().method_60637(true)));
            this.prevYaw = event.getYaw();
            this.prevPitch = event.getPitch();
            event.cancel();
        }
    }

    public void setRotation(float yaw, float pitch) {
        this.setRotation(new Rotation(100, yaw, pitch));
    }

    public void setRotation(Rotation rotation) {
        Rotation request;
        if (Managers.MODULE.getInstance(Rotations.class).mouseSensFixConfig.getValue()) {
            double fix = Math.pow((double)((Double)RotationManager.mc.field_1690.method_42495().method_41753() * 0.6 + 0.2), (double)3.0) * 1.2;
            rotation.setYaw((float)((double)rotation.getYaw() - (double)(rotation.getYaw() - this.serverYaw) % fix));
            rotation.setPitch((float)((double)rotation.getPitch() - (double)(rotation.getPitch() - this.serverPitch) % fix));
        }
        if (rotation.getPriority() == Integer.MAX_VALUE) {
            this.rotation = rotation;
        }
        if ((request = (Rotation)this.requests.stream().filter(r -> rotation.getPriority() == r.getPriority()).findFirst().orElse(null)) == null) {
            this.requests.add((Object)rotation);
        } else {
            request.setYaw(rotation.getYaw());
            request.setPitch(rotation.getPitch());
        }
    }

    public void setRotationClient(float yaw, float pitch) {
        if (RotationManager.mc.field_1724 == null) {
            return;
        }
        RotationManager.mc.field_1724.method_36456(yaw);
        RotationManager.mc.field_1724.method_36457(class_3532.method_15363((float)pitch, (float)-90.0f, (float)90.0f));
    }

    public void setRotationSilent(float yaw, float pitch) {
        this.setRotation(new Rotation(Integer.MAX_VALUE, yaw, pitch, true));
        Managers.NETWORK.sendPacket((class_2596<?>)new class_2828.class_2830(RotationManager.mc.field_1724.method_23317(), RotationManager.mc.field_1724.method_23318(), RotationManager.mc.field_1724.method_23321(), yaw, pitch, RotationManager.mc.field_1724.method_24828()));
    }

    public void setRotationSilentSync() {
        float yaw = RotationManager.mc.field_1724.method_36454();
        float pitch = RotationManager.mc.field_1724.method_36455();
        this.setRotation(new Rotation(Integer.MAX_VALUE, yaw, pitch, true));
        Managers.NETWORK.sendPacket((class_2596<?>)new class_2828.class_2830(RotationManager.mc.field_1724.method_23317(), RotationManager.mc.field_1724.method_23318(), RotationManager.mc.field_1724.method_23321(), yaw, pitch, RotationManager.mc.field_1724.method_24828()));
    }

    public boolean removeRotation(Rotation request) {
        return this.requests.remove((Object)request);
    }

    public boolean isRotationBlocked(int priority) {
        return this.rotation != null && priority < this.rotation.getPriority();
    }

    public boolean isDoneRotating() {
        return (float)this.rotateTicks > Managers.MODULE.getInstance(Rotations.class).preserveTicksConfig.getValue().floatValue();
    }

    public boolean isRotating() {
        return this.rotation != null;
    }

    public float getRotationYaw() {
        return this.rotation.getYaw();
    }

    public float getRotationPitch() {
        return this.rotation.getPitch();
    }

    public float getServerYaw() {
        return this.serverYaw;
    }

    public float getWrappedYaw() {
        return class_3532.method_15393((float)this.serverYaw);
    }

    public float getServerPitch() {
        return this.serverPitch;
    }

    private class_243 movementInputToVelocity(float yaw, class_243 movementInput, float speed) {
        double d = movementInput.method_1027();
        if (d < 1.0E-7) {
            return class_243.field_1353;
        }
        class_243 vec3d = (d > 1.0 ? movementInput.method_1029() : movementInput).method_1021((double)speed);
        float f = class_3532.method_15374((float)(yaw * ((float)Math.PI / 180)));
        float g = class_3532.method_15362((float)(yaw * ((float)Math.PI / 180)));
        return new class_243(vec3d.field_1352 * (double)g - vec3d.field_1350 * (double)f, vec3d.field_1351, vec3d.field_1350 * (double)g + vec3d.field_1352 * (double)f);
    }

    private Rotation getRotationRequest() {
        Rotation rotationRequest = null;
        int priority = 0;
        for (Rotation request : this.requests) {
            if (request.getPriority() <= priority) continue;
            rotationRequest = request;
            priority = request.getPriority();
        }
        return rotationRequest;
    }
}
