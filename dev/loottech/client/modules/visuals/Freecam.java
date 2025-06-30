package dev.loottech.client.modules.visuals;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.api.manager.rotation.Rotation;
import dev.loottech.api.utilities.RayCastUtil;
import dev.loottech.api.utilities.RotationUtils;
import dev.loottech.client.events.CameraPositionEvent;
import dev.loottech.client.events.CameraRotationEvent;
import dev.loottech.client.events.DeathEvent;
import dev.loottech.client.events.EntityCameraPositionEvent;
import dev.loottech.client.events.EntityRotationVectorEvent;
import dev.loottech.client.events.LogoutEvent;
import dev.loottech.client.events.MouseUpdateEvent;
import dev.loottech.client.events.PerspectiveEvent;
import dev.loottech.client.events.PreTickEvent;
import dev.loottech.client.events.RenderArmEvent;
import dev.loottech.client.events.ViewBobEvent;
import dev.loottech.client.values.impl.ValueBoolean;
import dev.loottech.client.values.impl.ValueEnum;
import dev.loottech.client.values.impl.ValueNumber;
import net.minecraft.class_239;
import net.minecraft.class_241;
import net.minecraft.class_243;
import net.minecraft.class_315;
import net.minecraft.class_3532;
import net.minecraft.class_743;

@RegisterModule(name="Freecam", tag="Freecam", description="Lets you go into a spectator player to observe your surroundings.", category=Module.Category.VISUALS)
public class Freecam
extends Module {
    private final ValueNumber motionSpeed = new ValueNumber("Speed", "Spectator speed", (Number)Float.valueOf((float)4.0f), (Number)Float.valueOf((float)0.1f), (Number)Float.valueOf((float)10.0f));
    public final ValueBoolean renderPlayer = new ValueBoolean("Render Self", "Render yourself when in freecam.", true);
    private final ValueBoolean rotate = new ValueBoolean("Rotate", "Rotate to the freecam rotation.", true);
    private final ValueEnum interact = new ValueEnum("Interaction", "Interact through camera or player.", Interact.CAMERA);
    public class_243 position;
    public class_243 lastPosition;
    public float yaw;
    public float pitch;
    public boolean control = false;

    @Override
    public void onEnable() {
        if (Freecam.mc.field_1724 == null) {
            return;
        }
        this.control = false;
        this.lastPosition = this.position = Freecam.mc.field_1773.method_19418().method_19326();
        this.yaw = Freecam.mc.field_1724.method_36454();
        this.pitch = Freecam.mc.field_1724.method_36455();
        Freecam.mc.field_1724.field_3913 = new FreecamKeyboardInput(Freecam.mc.field_1690);
    }

    @Override
    public void onDisable() {
        if (Freecam.mc.field_1724 == null) {
            return;
        }
        Freecam.mc.field_1724.field_3913 = new class_743(Freecam.mc.field_1690);
    }

    @Override
    public void onDeath(DeathEvent event) {
        if (event.getEntity() == Freecam.mc.field_1724) {
            this.disable(false);
        }
    }

    @Override
    public void onLogout(LogoutEvent event) {
        this.disable(false);
    }

    @Override
    public void onCameraPosition(CameraPositionEvent event) {
        event.setPosition(this.control ? this.position : this.lastPosition.method_35590(this.position, (double)event.getTickDelta()));
    }

    @Override
    public void onCameraRotation(CameraRotationEvent event) {
        event.setRotation(new class_241(this.yaw, this.pitch));
    }

    @Override
    public void onMouseUpdate(MouseUpdateEvent event) {
        if (!this.control) {
            event.cancel();
            this.changeLookDirection(event.getCursorDeltaX(), event.getCursorDeltaY());
        }
    }

    @Override
    public void onEntityCameraPosition(EntityCameraPositionEvent event) {
        if (event.getEntity() != Freecam.mc.field_1724) {
            return;
        }
        if (!this.control && this.interact.getValue() == Interact.CAMERA) {
            event.setPosition(this.position);
        }
    }

    @Override
    public void onEntityRotation(EntityRotationVectorEvent event) {
        if (event.getEntity() != Freecam.mc.field_1724) {
            return;
        }
        if (!this.control && this.interact.getValue() == Interact.CAMERA) {
            event.setPosition(RotationUtils.getRotationVector(this.pitch, this.yaw));
        }
    }

    @Override
    public void onPreTick(PreTickEvent event) {
        if (!this.control && this.rotate.getValue()) {
            float[] currentAngles = new float[]{this.yaw, this.pitch};
            class_243 eyePos = this.position;
            class_239 result = RayCastUtil.rayCast(Freecam.mc.field_1724.method_55754(), eyePos, currentAngles);
            if (result.method_17783() == class_239.class_240.field_1332) {
                float[] newAngles = RotationUtils.getRotations(result.method_17784());
                Managers.ROTATION.setRotation(new Rotation(1, newAngles[0], newAngles[1]));
            }
        }
    }

    @Override
    public void onPerspective(PerspectiveEvent event) {
        if (this.renderPlayer.getValue()) {
            event.cancel();
        }
    }

    @Override
    public void onRenderArm(RenderArmEvent event) {
        event.cancel();
    }

    @Override
    public void onBob(ViewBobEvent event) {
        if (this.control) {
            event.cancel();
        }
    }

    private float getMovementMultiplier(boolean positive, boolean negative) {
        if (positive == negative) {
            return 0.0f;
        }
        return positive ? 1.0f : -1.0f;
    }

    private class_241 handleVanillaMotion(float speed, float forward, float strafe) {
        if (forward == 0.0f && strafe == 0.0f) {
            return class_241.field_1340;
        }
        if (forward != 0.0f && strafe != 0.0f) {
            forward *= (float)Math.sin((double)0.7853981633974483);
            strafe *= (float)Math.cos((double)0.7853981633974483);
        }
        return new class_241((float)((double)(forward * speed) * -Math.sin((double)Math.toRadians((double)this.yaw)) + (double)(strafe * speed) * Math.cos((double)Math.toRadians((double)this.yaw))), (float)((double)(forward * speed) * Math.cos((double)Math.toRadians((double)this.yaw)) - (double)(strafe * speed) * -Math.sin((double)Math.toRadians((double)this.yaw))));
    }

    private void changeLookDirection(double cursorDeltaX, double cursorDeltaY) {
        float f = (float)cursorDeltaY * 0.15f;
        float g = (float)cursorDeltaX * 0.15f;
        this.pitch += f;
        this.yaw += g;
        this.pitch = class_3532.method_15363((float)this.pitch, (float)-90.0f, (float)90.0f);
    }

    public class_243 getCameraPosition() {
        return this.position;
    }

    public class_243 getLastCameraPosition() {
        return this.lastPosition;
    }

    public float[] getCameraRotations() {
        return new float[]{this.yaw, this.pitch};
    }

    public static enum Interact {
        PLAYER,
        CAMERA;

    }

    public class FreecamKeyboardInput
    extends class_743 {
        private final class_315 options;

        public FreecamKeyboardInput(class_315 options) {
            super(options);
            this.options = options;
        }

        public void method_3129(boolean slowDown, float slowDownFactor) {
            if (Freecam.this.control) {
                super.method_3129(slowDown, slowDownFactor);
            } else {
                this.unset();
                float speed = Freecam.this.motionSpeed.getValue().floatValue() / 10.0f;
                float fakeMovementForward = Freecam.this.getMovementMultiplier(this.options.field_1894.method_1434(), this.options.field_1881.method_1434());
                float fakeMovementSideways = Freecam.this.getMovementMultiplier(this.options.field_1913.method_1434(), this.options.field_1849.method_1434());
                class_241 dir = Freecam.this.handleVanillaMotion(speed, fakeMovementForward, fakeMovementSideways);
                float y = 0.0f;
                if (this.options.field_1903.method_1434()) {
                    y += speed;
                } else if (this.options.field_1832.method_1434()) {
                    y -= speed;
                }
                Freecam.this.lastPosition = Freecam.this.position;
                Freecam.this.position = Freecam.this.position.method_1031((double)dir.field_1343, (double)y, (double)dir.field_1342);
            }
        }

        private void unset() {
            this.field_3910 = false;
            this.field_3909 = false;
            this.field_3908 = false;
            this.field_3906 = false;
            this.field_3905 = 0.0f;
            this.field_3907 = 0.0f;
            this.field_3904 = false;
            this.field_3903 = false;
        }
    }
}
