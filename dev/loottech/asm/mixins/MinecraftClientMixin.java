package dev.loottech.asm.mixins;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.shader.event.WindowResizeCallback;
import dev.loottech.client.events.DeathEvent;
import dev.loottech.client.events.OpenScreenEvent;
import dev.loottech.client.events.PreTickEvent;
import dev.loottech.client.events.RunTickEvent;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_1041;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_310;
import net.minecraft.class_437;
import net.minecraft.class_636;
import net.minecraft.class_638;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_310.class})
public class MinecraftClientMixin {
    @Unique
    private final List<Integer> deadList = new ArrayList();
    @Shadow
    @Final
    private class_1041 field_1704;
    @Shadow
    public class_638 field_1687;
    @Shadow
    public class_746 field_1724;

    @Inject(method={"tick"}, at={@At(value="HEAD")})
    private void hookTickPre(CallbackInfo ci) {
        if (this.field_1724 != null && this.field_1687 != null) {
            PreTickEvent tickPreEvent = new PreTickEvent();
            Managers.EVENT.call(tickPreEvent);
        }
    }

    @Inject(method={"tick"}, at={@At(value="TAIL")})
    private void hookTickPost(CallbackInfo ci) {
        if (this.field_1724 != null && this.field_1687 != null) {
            for (class_1297 entity : this.field_1687.method_18112()) {
                if (!(entity instanceof class_1309)) continue;
                class_1309 e = (class_1309)entity;
                if (e.method_29504() && !this.deadList.contains((Object)e.method_5628())) {
                    DeathEvent entityDeathEvent = new DeathEvent(e);
                    Managers.EVENT.call(entityDeathEvent);
                    this.deadList.add((Object)e.method_5628());
                    continue;
                }
                if (e.method_29504()) continue;
                this.deadList.remove((Object)e.method_5628());
            }
        }
    }

    @Inject(method={"getWindowTitle"}, at={@At(value="HEAD")}, cancellable=true)
    public void getTitle(CallbackInfoReturnable<String> cir) {
        cir.setReturnValue((Object)"LootTech Beta0.73");
    }

    @Inject(method={"setScreen"}, at={@At(value="HEAD")}, cancellable=true)
    private void onSetScreen(class_437 screen, CallbackInfo info) {
        OpenScreenEvent event = new OpenScreenEvent(screen);
        Managers.EVENT.call(event);
        if (event.isCanceled()) {
            info.cancel();
        }
    }

    @Redirect(method={"handleBlockBreaking"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"))
    public boolean injectHandleBlockBreaking(class_746 clientPlayerEntity) {
        return !Managers.MODULE.isModuleEnabled("MultiTask") && clientPlayerEntity.method_6115();
    }

    @Redirect(method={"doItemUse"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayerInteractionManager;isBreakingBlock()Z"))
    public boolean injectDoItemUse(class_636 clientPlayerInteractionManager) {
        return !Managers.MODULE.isModuleEnabled("MultiTask") && clientPlayerInteractionManager.method_2923();
    }

    @Inject(method={"onResolutionChanged"}, at={@At(value="TAIL")})
    private void captureResize(CallbackInfo ci) {
        ((WindowResizeCallback)WindowResizeCallback.EVENT.invoker()).onResized((class_310)this, this.field_1704);
    }

    @Inject(method={"run"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/MinecraftClient;render(Z)V", shift=At.Shift.BEFORE)})
    private void hookRun(CallbackInfo ci) {
        RunTickEvent runTickEvent = new RunTickEvent();
        Managers.EVENT.call(runTickEvent);
    }
}
