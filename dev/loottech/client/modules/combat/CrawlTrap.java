package dev.loottech.client.modules.combat;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.module.CombatModule;
import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.api.utilities.CacheTimer;
import dev.loottech.api.utilities.InventoryUtils;
import dev.loottech.api.utilities.render.RenderBlock;
import dev.loottech.api.utilities.render.RenderBuffers;
import dev.loottech.api.utilities.render.RenderUtils;
import dev.loottech.client.events.PrePlayerUpdateEvent;
import dev.loottech.client.events.Render3DEvent;
import dev.loottech.client.modules.client.ModuleColor;
import dev.loottech.client.values.impl.ValueBoolean;
import dev.loottech.client.values.impl.ValueColor;
import dev.loottech.client.values.impl.ValueNumber;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_1802;
import net.minecraft.class_2338;
import net.minecraft.class_243;
import net.minecraft.class_746;

@RegisterModule(name="CrawlTrap", tag="CrawlTrap", description="Attempt to place blocks above opponents' heads to prevent them from un-crawling.", category=Module.Category.COMBAT)
public class CrawlTrap
extends CombatModule {
    ValueBoolean strictDirection = new ValueBoolean("StrictDirection", "Place on visible sides.", true);
    ValueNumber targetRange = new ValueNumber("TargetRange", "Range to select targets from.", (Number)Double.valueOf((double)5.0), (Number)Double.valueOf((double)0.1), (Number)Double.valueOf((double)10.0));
    ValueNumber placeRange = new ValueNumber("PlaceRange", "Range to place blocks from.", (Number)Double.valueOf((double)3.5), (Number)Double.valueOf((double)0.1), (Number)Double.valueOf((double)6.0));
    ValueNumber delay = new ValueNumber("Delay", "Delay time between places in ms.", (Number)Double.valueOf((double)10.0), (Number)Double.valueOf((double)0.0), (Number)Double.valueOf((double)1000.0));
    ValueBoolean renderConfig = new ValueBoolean("Render", "Render a box where the module has placed.", true);
    ValueColor fillColor = new ValueColor("Fill", "Fill", "Render box Fill color", ModuleColor.getColor(30), false, true);
    ValueColor lineColor = new ValueColor("Line", "Line", "Render box Line color", ModuleColor.getColor(255), false, true);
    private final ValueNumber fadeTime = new ValueNumber("FadeTime", "FadeTime", "Render fade time (ms)", (Number)Integer.valueOf((int)300), (Number)Integer.valueOf((int)0), (Number)Integer.valueOf((int)1000));
    CacheTimer placeTimer = new CacheTimer();
    private final ArrayList<RenderBlock> renderBlocks = new ArrayList();

    public CrawlTrap() {
        super(200);
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (CrawlTrap.nullCheck() || !this.renderConfig.getValue()) {
            return;
        }
        this.renderBlocks.removeIf(block -> System.currentTimeMillis() - block.startTime > this.fadeTime.getValue().longValue());
        for (RenderBlock block2 : this.renderBlocks) {
            RenderBuffers.preRender();
            float progress = (float)(System.currentTimeMillis() - block2.startTime) / this.fadeTime.getValue().floatValue();
            progress = Math.min((float)1.0f, (float)progress);
            float alpha = (float)Math.pow((double)(1.0f - progress), (double)2.0);
            Color fadedFill = new Color(this.fillColor.getValue().getRed(), this.fillColor.getValue().getGreen(), this.fillColor.getValue().getBlue(), (int)(alpha * (float)this.fillColor.getValue().getAlpha()));
            Color fadedLine = new Color(this.lineColor.getValue().getRed(), this.lineColor.getValue().getGreen(), this.lineColor.getValue().getBlue(), (int)(alpha * (float)this.lineColor.getValue().getAlpha()));
            RenderUtils.drawBox(event.getMatrices(), block2.pos, fadedLine, 1.5);
            RenderUtils.drawBoxFilled(event.getMatrices(), block2.pos, fadedFill);
            RenderBuffers.postRender();
        }
    }

    @Override
    public void onPrePlayerUpdate(PrePlayerUpdateEvent event) {
        if (CrawlTrap.nullCheck()) {
            return;
        }
        if (!InventoryUtils.findInHotbar(class_1802.field_8281).found()) {
            return;
        }
        if (this.placeTimer.passed((Number)Float.valueOf((float)this.delay.getValue().floatValue()))) {
            class_1657 target = this.getCrawlingPlayers(this.targetRange.getValue().floatValue());
            if (target == null) {
                return;
            }
            if (this.calculatePlacePosition(target) != null) {
                class_2338 placePos = this.calculatePlacePosition(target);
                this.placeBlock(placePos);
            }
        }
    }

    private class_2338 calculatePlacePosition(class_1657 player) {
        class_243 abovePos = new class_243((double)player.method_24515().method_10263(), (double)(player.method_24515().method_10264() + 1), (double)player.method_24515().method_10260());
        if (CrawlTrap.mc.field_1687.method_8320(player.method_24515().method_10084()).method_26215() && CrawlTrap.mc.field_1724.method_19538().method_1022(abovePos) <= (double)this.placeRange.getValue().floatValue()) {
            return player.method_24515().method_10084();
        }
        return null;
    }

    private class_1657 getCrawlingPlayers(double range) {
        return (class_1657)CrawlTrap.mc.field_1687.method_18456().stream().filter(e -> !(e instanceof class_746) && !e.method_7325()).filter(e -> CrawlTrap.mc.field_1724.method_5858((class_1297)e) <= range * range).filter(e -> !Managers.FRIEND.isFriend((class_1657)e)).filter(class_1297::method_20448).min(Comparator.comparingDouble(e -> CrawlTrap.mc.field_1724.method_5858((class_1297)e))).orElse(null);
    }

    private void placeBlock(class_2338 pos) {
        int slot = InventoryUtils.findInHotbar(class_1802.field_8281).slot();
        Managers.INTERACTION.placeBlock(pos, slot, this.strictDirection.getValue(), true, (state, angles) -> {
            if (state) {
                Managers.ROTATION.setRotationSilent(angles[0], angles[1]);
            }
        });
        Managers.ROTATION.setRotationSilentSync();
        if (this.renderConfig.getValue()) {
            this.renderBlocks.add((Object)new RenderBlock(pos));
        }
    }
}
