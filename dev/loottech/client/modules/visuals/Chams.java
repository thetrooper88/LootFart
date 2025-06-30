package dev.loottech.client.modules.visuals;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.api.utilities.Animation;
import dev.loottech.api.utilities.ColorUtil;
import dev.loottech.api.utilities.EntityUtil;
import dev.loottech.api.utilities.entity.FakePlayerEntity;
import dev.loottech.api.utilities.render.ChamsModelRenderer;
import dev.loottech.api.utilities.render.RenderBuffers;
import dev.loottech.api.utilities.render.RenderUtils;
import dev.loottech.api.utilities.render.StaticBipedEntityModel;
import dev.loottech.client.events.PacketReceiveEvent;
import dev.loottech.client.events.Render3DEvent;
import dev.loottech.client.events.RenderArmEvent;
import dev.loottech.client.events.RenderCrystalEvent;
import dev.loottech.client.events.RenderEntityEvent;
import dev.loottech.client.events.RenderThroughWallsEvent;
import dev.loottech.client.events.RenderWorldHandEvent;
import dev.loottech.client.modules.client.ModuleColor;
import dev.loottech.client.modules.visuals.Freecam;
import dev.loottech.client.values.impl.ValueBoolean;
import dev.loottech.client.values.impl.ValueColor;
import dev.loottech.client.values.impl.ValueEnum;
import dev.loottech.client.values.impl.ValueNumber;
import java.awt.Color;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1477;
import net.minecraft.class_1493;
import net.minecraft.class_1511;
import net.minecraft.class_1657;
import net.minecraft.class_1937;
import net.minecraft.class_2350;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2663;
import net.minecraft.class_3532;
import net.minecraft.class_3887;
import net.minecraft.class_4050;
import net.minecraft.class_4587;
import net.minecraft.class_742;
import net.minecraft.class_922;

@RegisterModule(name="Chams", tag="Chams", description="Nice rendering of entities", category=Module.Category.VISUALS)
public class Chams
extends Module {
    ValueNumber rangeConfig = new ValueNumber("Range", "The chams render range", (Number)Float.valueOf((float)50.0f), (Number)Float.valueOf((float)10.0f), (Number)Float.valueOf((float)200.0f));
    ValueEnum modeConfig = new ValueEnum("Mode", "The rendering mode for the chams", ChamsMode.FILL);
    ValueNumber widthConfig = new ValueNumber("Width", "The line width of the render", (Number)Float.valueOf((float)1.5f), (Number)Float.valueOf((float)1.0f), (Number)Float.valueOf((float)5.0f));
    public ValueBoolean wallsConfig = new ValueBoolean("ThroughWalls", "Renders chams through walls", true);
    ValueBoolean textureConfig = new ValueBoolean("Texture", "Renders the entity model texture", false);
    ValueBoolean playersConfig = new ValueBoolean("Players", "Render chams on other players", true);
    ValueBoolean selfConfig = new ValueBoolean("Self", "Render chams on the player", true);
    ValueBoolean handsConfig = new ValueBoolean("Hands", "Render chams on first-person hands", true);
    ValueBoolean monstersConfig = new ValueBoolean("Monsters", "Render chams on monsters", true);
    ValueBoolean animalsConfig = new ValueBoolean("Animals", "Render chams on animals", true);
    ValueBoolean crystalsConfig = new ValueBoolean("Crystals", "Render chams on crystals", true);
    ValueBoolean popsConfig = new ValueBoolean("Pops", "Render chams on totem pops", false);
    ValueColor popsColor = new ValueColor("Pop Color", "Pop Color", "Color of pop chams", ModuleColor.getColor(30), false, true);
    ValueNumber fadeTimeConfig = new ValueNumber("Fade-Time", "Timer for the fade", (Number)Integer.valueOf((int)1000), (Number)Integer.valueOf((int)0), (Number)Integer.valueOf((int)3000));
    ValueColor colorConfig = new ValueColor("Color", "Color", "The color of the chams", ModuleColor.getColor(30), false, true);
    private final Map<PopChamEntity, Animation> fadeList = new ConcurrentHashMap();

    @Override
    public void onDisable() {
        this.fadeList.clear();
    }

    @Override
    public void onRenderThroughWalls(RenderThroughWallsEvent event) {
        if (this.wallsConfig.getValue() && this.checkChams(event.getEntity())) {
            RenderSystem.disableDepthTest();
            event.cancel();
        }
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        RenderBuffers.preRender();
        if (!this.wallsConfig.getValue()) {
            RenderSystem.enableDepthTest();
        } else {
            RenderSystem.disableDepthTest();
        }
        for (Map.Entry set : new HashSet((Collection)this.fadeList.entrySet())) {
            ((Animation)set.getValue()).setState(false);
            Color color = this.popsColor.getValue();
            int boxAlpha = (int)((double)color.getAlpha() * ((Animation)set.getValue()).getFactor());
            int lineAlpha = (int)(145.0 * ((Animation)set.getValue()).getFactor());
            int boxColor = ColorUtil.withAlpha(color.getRGB(), boxAlpha);
            int lineColor = ColorUtil.withAlpha(color.getRGB(), lineAlpha);
            ChamsModelRenderer.renderStaticPlayerModel(event.getMatrices(), (class_742)set.getKey(), ((PopChamEntity)set.getKey()).getModel(), event.getTickDelta(), boxColor, lineColor, this.widthConfig.getValue().floatValue(), this.modeConfig.getValue() != ChamsMode.FILL, true, false);
        }
        this.fadeList.entrySet().removeIf(e -> ((Animation)e.getValue()).getFactor() == 0.0);
        RenderBuffers.postRender();
        if (this.modeConfig.getValue() != ChamsMode.NORMAL) {
            RenderBuffers.preRender();
            RenderSystem.depthMask((boolean)false);
            if (!this.wallsConfig.getValue()) {
                RenderSystem.enableDepthTest();
            } else {
                RenderSystem.disableDepthTest();
            }
            for (class_1297 entity : Chams.mc.field_1687.method_18112()) {
                class_1309 livingEntity;
                double x = Math.abs((double)(Chams.mc.field_1773.method_19418().method_19326().field_1352 - entity.method_23317()));
                double z = Math.abs((double)(Chams.mc.field_1773.method_19418().method_19326().field_1350 - entity.method_23321()));
                double d = ((Integer)Chams.mc.field_1690.method_42503().method_41753() + 1) * 16;
                class_243 start = Managers.MODULE.getInstance(Freecam.class).isEnabled() ? Managers.MODULE.getInstance(Freecam.class).getCameraPosition() : Chams.mc.field_1724.method_19538();
                if (start.method_1025(entity.method_19538()) > (double)(this.rangeConfig.getValue().floatValue() * this.rangeConfig.getValue().floatValue()) || x > d || z > d || !RenderUtils.isFrustumVisible(entity.method_5829()) || (!(entity instanceof class_1309) || !this.checkChams(livingEntity = (class_1309)entity)) && (!(entity instanceof class_1511) || !this.crystalsConfig.getValue())) continue;
                this.renderEntityChams(event.getMatrices(), entity, event.getTickDelta());
            }
            RenderBuffers.postRender();
            RenderSystem.depthMask((boolean)true);
        }
    }

    @Override
    public void onRenderWorldHand(RenderWorldHandEvent event) {
        if (this.modeConfig.getValue() == ChamsMode.NORMAL) {
            return;
        }
        RenderBuffers.preRender();
        if (this.handsConfig.getValue()) {
            int color1 = this.colorConfig.getValue().getRGB();
            int lineColor1 = ColorUtil.withAlpha(color1, 145);
            ChamsModelRenderer.renderHand(event.getMatrices(), event.getTickDelta(), lineColor1, color1, this.widthConfig.getValue().floatValue(), this.modeConfig.getValue() != ChamsMode.FILL, this.modeConfig.getValue() != ChamsMode.WIREFRAME, false);
        }
        RenderBuffers.postRender();
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        class_2663 packet;
        if (Chams.mc.field_1687 == null) {
            return;
        }
        class_2596<?> class_25962 = event.getPacket();
        if (class_25962 instanceof class_2663 && (packet = (class_2663)class_25962).method_11470() == 35 && this.popsConfig.getValue()) {
            class_1297 entity = packet.method_11469((class_1937)Chams.mc.field_1687);
            if (entity == Chams.mc.field_1724 || !(entity instanceof class_1657)) {
                return;
            }
            class_1657 player = (class_1657)entity;
            Animation animation = new Animation(true, this.fadeTimeConfig.getValue().intValue());
            this.fadeList.put((Object)new PopChamEntity(player, mc.method_60646().method_60637(true)), (Object)animation);
        }
    }

    @Override
    public void onRenderCrystal(RenderCrystalEvent event) {
        if (this.modeConfig.getValue() == ChamsMode.NORMAL) {
            return;
        }
        if (Chams.mc.field_1724 != null && (!this.textureConfig.getValue() || !this.wallsConfig.getValue()) && this.crystalsConfig.getValue() && Chams.mc.field_1724.method_5858((class_1297)event.endCrystalEntity) <= (double)(this.rangeConfig.getValue().floatValue() * this.rangeConfig.getValue().floatValue())) {
            event.cancel();
        }
    }

    @Override
    public void onRenderEntity(RenderEntityEvent event) {
        float n;
        class_2350 direction;
        class_1297 class_12972;
        if (this.modeConfig.getValue() == ChamsMode.NORMAL) {
            return;
        }
        if (Chams.mc.field_1724 == null || this.textureConfig.getValue() && this.wallsConfig.getValue() || !this.checkChams(event.entity) || Chams.mc.field_1724.method_5858((class_1297)event.entity) > (double)(this.rangeConfig.getValue().floatValue() * this.rangeConfig.getValue().floatValue())) {
            return;
        }
        event.cancel();
        event.matrixStack.method_22903();
        event.model.field_3447 = event.entity.method_6055(event.g);
        event.model.field_3449 = event.entity.method_5765();
        event.model.field_3448 = event.entity.method_6109();
        float h = class_3532.method_17821((float)event.g, (float)event.entity.field_6220, (float)event.entity.field_6283);
        float j = class_3532.method_17821((float)event.g, (float)event.entity.field_6259, (float)event.entity.field_6241);
        float k = j - h;
        if (event.entity.method_5765() && (class_12972 = event.entity.method_5854()) instanceof class_1309) {
            class_1309 livingEntity2 = (class_1309)class_12972;
            h = class_3532.method_17821((float)event.g, (float)livingEntity2.field_6220, (float)livingEntity2.field_6283);
            k = j - h;
            float l = class_3532.method_15393((float)k);
            if (l < -85.0f) {
                l = -85.0f;
            }
            if (l >= 85.0f) {
                l = 85.0f;
            }
            h = j - l;
            if (l * l > 2500.0f) {
                h += l * 0.2f;
            }
            k = j - h;
        }
        float m = class_3532.method_16439((float)event.g, (float)event.entity.field_6004, (float)event.entity.method_36455());
        if (class_922.method_38563((class_1309)event.entity)) {
            m *= -1.0f;
            k *= -1.0f;
        }
        if (event.entity.method_41328(class_4050.field_18078) && (direction = event.entity.method_18401()) != null) {
            n = event.entity.method_18381(class_4050.field_18076) - 0.1f;
            event.matrixStack.method_46416((float)(-direction.method_10148()) * n, 0.0f, (float)(-direction.method_10165()) * n);
        }
        float l = this.getAnimationProgress(event.entity, event.g);
        if (event.entity instanceof class_1657) {
            ChamsModelRenderer.setupPlayerTransforms((class_742)event.entity, event.matrixStack, l, h, event.g);
        } else {
            ChamsModelRenderer.setupTransforms(event.entity, event.matrixStack, l, h, event.g);
        }
        event.matrixStack.method_22905(-1.0f, -1.0f, 1.0f);
        event.matrixStack.method_22905(0.9375f, 0.9375f, 0.9375f);
        event.matrixStack.method_46416(0.0f, -1.501f, 0.0f);
        n = 0.0f;
        float o = 0.0f;
        if (!event.entity.method_5765() && event.entity.method_5805()) {
            n = event.entity.field_42108.method_48570(event.g);
            o = event.entity.field_42108.method_48572(event.g);
            if (event.entity.method_6109()) {
                o *= 3.0f;
            }
            if (n > 1.0f) {
                n = 1.0f;
            }
        }
        event.model.method_2816((class_1297)event.entity, o, n, event.g);
        event.model.method_2819((class_1297)event.entity, o, n, l, k, m);
        if (!event.entity.method_7325()) {
            for (Object featureRenderer : event.features) {
                ((class_3887)featureRenderer).method_4199(event.matrixStack, event.vertexConsumerProvider, event.i, (class_1297)event.entity, o, n, event.g, l, k, m);
            }
        }
        event.matrixStack.method_22909();
    }

    @Override
    public void onRenderArm(RenderArmEvent event) {
        if (this.modeConfig.getValue() == ChamsMode.NORMAL) {
            return;
        }
        if (this.handsConfig.getValue() && !this.textureConfig.getValue()) {
            event.cancel();
        }
    }

    public void renderEntityChams(class_4587 matrixStack, class_1297 entity, float tickDelta) {
        int color1 = this.colorConfig.getValue().getRGB();
        int lineColor1 = ColorUtil.withAlpha(color1, 145);
        this.renderEntityChams(matrixStack, entity, tickDelta, color1, lineColor1);
    }

    public void renderEntityChams(class_4587 matrixStack, class_1297 entity, float tickDelta, int color, int lineColor) {
        ChamsModelRenderer.render(matrixStack, entity, tickDelta, color, lineColor, this.widthConfig.getValue().floatValue(), this.modeConfig.getValue() != ChamsMode.FILL, this.modeConfig.getValue() != ChamsMode.WIREFRAME, false);
    }

    private float getAnimationProgress(class_1309 entity, float f) {
        float f2;
        if (entity instanceof class_1477) {
            return class_3532.method_16439((float)f, (float)((class_1477)entity).field_6900, (float)((class_1477)entity).field_6904);
        }
        if (entity instanceof class_1493) {
            class_1493 wolf = (class_1493)entity;
            f2 = wolf.method_6714();
        } else {
            f2 = (float)entity.field_6012 + f;
        }
        return f2;
    }

    public boolean checkChams(class_1309 entity) {
        if (entity instanceof class_1657) {
            if (entity == Chams.mc.field_1724) {
                return this.selfConfig.getValue() && (!Chams.mc.field_1690.method_31044().method_31034() || Managers.MODULE.getInstance(Freecam.class).isEnabled());
            }
            return this.playersConfig.getValue();
        }
        return EntityUtil.isMonster((class_1297)entity) && this.monstersConfig.getValue() || (EntityUtil.isNeutral((class_1297)entity) || EntityUtil.isPassive((class_1297)entity)) && this.animalsConfig.getValue();
    }

    public static enum ChamsMode {
        NORMAL,
        FILL,
        WIREFRAME,
        WIRE_FILL;

    }

    public static class PopChamEntity
    extends FakePlayerEntity {
        private final StaticBipedEntityModel<class_742> model;

        public PopChamEntity(class_1657 player, float tickDelta) {
            super(player);
            this.model = new StaticBipedEntityModel<class_742>((class_742)player, false, tickDelta);
            this.field_6243 = player.field_6243;
            this.field_6264 = player.field_6243;
            this.method_18380(player.method_18376());
        }

        public StaticBipedEntityModel<class_742> getModel() {
            return this.model;
        }
    }
}
