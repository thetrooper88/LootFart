package dev.loottech.api.manager.event;

import dev.loottech.client.events.AddEntityEvent;
import dev.loottech.client.events.AmbientColorEvent;
import dev.loottech.client.events.AttackBlockEvent;
import dev.loottech.client.events.CameraPositionEvent;
import dev.loottech.client.events.CameraRotationEvent;
import dev.loottech.client.events.ChatSendEvent;
import dev.loottech.client.events.ChunkDataEvent;
import dev.loottech.client.events.ClientEvent;
import dev.loottech.client.events.DeathEvent;
import dev.loottech.client.events.EntityCameraPositionEvent;
import dev.loottech.client.events.EntityRotationVectorEvent;
import dev.loottech.client.events.FinishUsingEvent;
import dev.loottech.client.events.GameJoinEvent;
import dev.loottech.client.events.InteractItemEvent;
import dev.loottech.client.events.ItemDesyncEvent;
import dev.loottech.client.events.JumpRotationEvent;
import dev.loottech.client.events.KeyEvent;
import dev.loottech.client.events.KeyboardTickEvent;
import dev.loottech.client.events.LightmapInitEvent;
import dev.loottech.client.events.LightmapTickEvent;
import dev.loottech.client.events.LightmapUpdateEvent;
import dev.loottech.client.events.LoginEvent;
import dev.loottech.client.events.LogoutEvent;
import dev.loottech.client.events.MouseDragEvent;
import dev.loottech.client.events.MouseUpdateEvent;
import dev.loottech.client.events.MovementPacketsEvent;
import dev.loottech.client.events.OpenScreenEvent;
import dev.loottech.client.events.PacketReceiveEvent;
import dev.loottech.client.events.PacketSendEvent;
import dev.loottech.client.events.PacketSneakingEvent;
import dev.loottech.client.events.PerspectiveEvent;
import dev.loottech.client.events.PerspectiveUpdateEvent;
import dev.loottech.client.events.PlaySoundEvent;
import dev.loottech.client.events.PlayerJoinEvent;
import dev.loottech.client.events.PlayerLeaveEvent;
import dev.loottech.client.events.PlayerTickEvent;
import dev.loottech.client.events.PostMotionEvent;
import dev.loottech.client.events.PostPlayerJumpEvent;
import dev.loottech.client.events.PostPlayerUpdateEvent;
import dev.loottech.client.events.PrePlayerJumpEvent;
import dev.loottech.client.events.PrePlayerUpdateEvent;
import dev.loottech.client.events.PreTickEvent;
import dev.loottech.client.events.PushEvent;
import dev.loottech.client.events.RemoveEntityEvent;
import dev.loottech.client.events.Render2DEvent;
import dev.loottech.client.events.Render3DEvent;
import dev.loottech.client.events.RenderArmEvent;
import dev.loottech.client.events.RenderCrystalEvent;
import dev.loottech.client.events.RenderEntityEvent;
import dev.loottech.client.events.RenderGameTextEvent;
import dev.loottech.client.events.RenderPlayerEvent;
import dev.loottech.client.events.RenderShulkerStackEvent;
import dev.loottech.client.events.RenderSwingAnimationEvent;
import dev.loottech.client.events.RenderThroughWallsEvent;
import dev.loottech.client.events.RenderTooltipEvent;
import dev.loottech.client.events.RenderWorldHandEvent;
import dev.loottech.client.events.RunTickEvent;
import dev.loottech.client.events.SlowMovementEvent;
import dev.loottech.client.events.SprintCancelEvent;
import dev.loottech.client.events.SprintResetEvent;
import dev.loottech.client.events.SwingSpeedEvent;
import dev.loottech.client.events.TickCounterEvent;
import dev.loottech.client.events.TickEvent;
import dev.loottech.client.events.TotemPopEvent;
import dev.loottech.client.events.UpdateVelocityEvent;
import dev.loottech.client.events.ViewBobEvent;

public interface EventListener {
    default public void onChatSend(ChatSendEvent event) {
    }

    default public void onClient(ClientEvent event) {
    }

    default public void onKey(KeyEvent event) {
    }

    default public void onLogin(LoginEvent event) {
    }

    default public void onLogout(LogoutEvent event) {
    }

    default public void onMovementPackets(MovementPacketsEvent event) {
    }

    default public void onPacketSend(PacketSendEvent event) {
    }

    default public void onPacketReceive(PacketReceiveEvent event) {
    }

    default public void onPush(PushEvent event) {
    }

    default public void onRender2D(Render2DEvent event) {
    }

    default public void onRender3D(Render3DEvent event) {
    }

    default public void onTick(TickEvent event) {
    }

    default public void onDeath(DeathEvent event) {
    }

    default public void onAttackBlock(AttackBlockEvent event) {
    }

    default public void onTotemPop(TotemPopEvent event) {
    }

    default public void onPostMotion(PostMotionEvent event) {
    }

    default public void onUpdateVelocity(UpdateVelocityEvent event) {
    }

    default public void onPrePlayerJump(PrePlayerJumpEvent event) {
    }

    default public void onPostPlayerJump(PostPlayerJumpEvent event) {
    }

    default public void onRenderPlayer(RenderPlayerEvent event) {
    }

    default public void onAmbientColor(AmbientColorEvent event) {
    }

    default public void onLightmapInit(LightmapInitEvent event) {
    }

    default public void onLightmapTick(LightmapTickEvent event) {
    }

    default public void onLightmapUpdate(LightmapUpdateEvent event) {
    }

    default public void onRenderArm(RenderArmEvent event) {
    }

    default public void onResetSprint(SprintResetEvent event) {
    }

    default public void onItemDesync(ItemDesyncEvent event) {
    }

    default public void onPacketSneak(PacketSneakingEvent event) {
    }

    default public void onKeyboardTick(KeyboardTickEvent event) {
    }

    default public void onPostPlayerUpdate(PostPlayerUpdateEvent event) {
    }

    default public void onPrePlayerUpdate(PrePlayerUpdateEvent event) {
    }

    default public void onPreTick(PreTickEvent event) {
    }

    default public void onJumpRotation(JumpRotationEvent event) {
    }

    default public void onSprintCancel(SprintCancelEvent event) {
    }

    default public void onRenderGameText(RenderGameTextEvent event) {
    }

    default public void onPlayerUpdate(PlayerTickEvent event) {
    }

    default public void onRunTick(RunTickEvent event) {
    }

    default public void onAddEntity(AddEntityEvent event) {
    }

    default public void onRemoveEntity(RemoveEntityEvent event) {
    }

    default public void onGameJoin(GameJoinEvent event) {
    }

    default public void onFinishUsing(FinishUsingEvent event) {
    }

    default public void onPlaySound(PlaySoundEvent event) {
    }

    default public void onRenderSwing(RenderSwingAnimationEvent event) {
    }

    default public void onPlayerJoin(PlayerJoinEvent event) {
    }

    default public void onPlayerLeave(PlayerLeaveEvent event) {
    }

    default public void onSwingSpeed(SwingSpeedEvent event) {
    }

    default public void onMouseDrag(MouseDragEvent event) {
    }

    default public void onRenderTooltip(RenderTooltipEvent event) {
    }

    default public void onScreenOpen(OpenScreenEvent event) {
    }

    default public void onCameraPosition(CameraPositionEvent event) {
    }

    default public void onCameraRotation(CameraRotationEvent event) {
    }

    default public void onMouseUpdate(MouseUpdateEvent event) {
    }

    default public void onEntityCameraPosition(EntityCameraPositionEvent event) {
    }

    default public void onEntityRotation(EntityRotationVectorEvent event) {
    }

    default public void onPerspective(PerspectiveEvent event) {
    }

    default public void onPerspectiveUpdate(PerspectiveUpdateEvent event) {
    }

    default public void onBob(ViewBobEvent event) {
    }

    default public void onSlowMovement(SlowMovementEvent event) {
    }

    default public void onTickCounter(TickCounterEvent event) {
    }

    default public void onChunkData(ChunkDataEvent event) {
    }

    default public void onInteractItem(InteractItemEvent event) {
    }

    default public void onRenderEntity(RenderEntityEvent event) {
    }

    default public void onRenderCrystal(RenderCrystalEvent event) {
    }

    default public void onRenderWorldHand(RenderWorldHandEvent event) {
    }

    default public void onRenderThroughWalls(RenderThroughWallsEvent event) {
    }

    default public void onRenderShulkerStack(RenderShulkerStackEvent event) {
    }
}
