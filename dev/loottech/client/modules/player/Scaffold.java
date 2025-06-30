package dev.loottech.client.modules.player;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.api.utilities.PositionUtil;
import dev.loottech.api.utilities.Timer;
import dev.loottech.api.utilities.render.RenderBlock;
import dev.loottech.api.utilities.render.RenderBuffers;
import dev.loottech.api.utilities.render.RenderUtils;
import dev.loottech.client.events.Render3DEvent;
import dev.loottech.client.modules.client.ModuleColor;
import dev.loottech.client.values.impl.ValueBoolean;
import dev.loottech.client.values.impl.ValueColor;
import dev.loottech.client.values.impl.ValueNumber;
import java.awt.Color;
import java.util.ArrayList;
import net.minecraft.class_1661;
import net.minecraft.class_1747;
import net.minecraft.class_2338;
import net.minecraft.class_2350;

@RegisterModule(name="Scaffold", description="Automatically places blocks under your feet.", category=Module.Category.PLAYER)
public class Scaffold
extends Module {
    ValueNumber delay = new ValueNumber("Delay", "Delay between placing blocks.", (Number)Double.valueOf((double)0.0), (Number)Double.valueOf((double)0.0), (Number)Double.valueOf((double)200.0));
    ValueNumber expand = new ValueNumber("Expand", "Expand in each direction for this many blocks.", (Number)Integer.valueOf((int)0), (Number)Integer.valueOf((int)0), (Number)Integer.valueOf((int)8));
    ValueBoolean strictDirection = new ValueBoolean("StrictDirection", "Only place on visible block faces.", false);
    ValueBoolean airPlace = new ValueBoolean("AirPlace", "Place blocks in air.", false);
    ValueBoolean multiTask = new ValueBoolean("MultiTask", "Don't place when you are using an item.", false);
    ValueBoolean rotate = new ValueBoolean("Rotate", "Rotate to block placing.", false);
    ValueBoolean render = new ValueBoolean("Render", "Render placed blocks.", false);
    ValueColor fillColor = new ValueColor("Fill", "Fill", "", ModuleColor.getColor(30), false, true);
    ValueColor lineColor = new ValueColor("Line", "Line", "", ModuleColor.getColor(), false, true);
    ValueNumber fadeTime = new ValueNumber("FadeTime", "FadeTime", "Render fade time (ms)", (Number)Integer.valueOf((int)300), (Number)Integer.valueOf((int)0), (Number)Integer.valueOf((int)1000));
    private final ArrayList<RenderBlock> renderBlocks = new ArrayList();
    private final ArrayList<class_2338> placeBlocks = new ArrayList();
    private final Timer placeTimer = new Timer();

    @Override
    public void onTick() {
        if (Scaffold.nullCheck() || this.multiTask.getValue() && Scaffold.mc.field_1724.method_6115()) {
            return;
        }
        class_2338 belowPos = PositionUtil.getRoundedBlockPos(Scaffold.mc.field_1724.method_19538().method_1031(0.0, -1.0, 0.0));
        this.placeBlocks.add((Object)belowPos);
        int x = this.expand.getValue().intValue();
        for (int i = 0; i < x; ++i) {
            class_2350 facingDirection = Scaffold.mc.field_1724.method_5735();
            this.placeBlocks.add((Object)PositionUtil.getRoundedBlockPos(Scaffold.mc.field_1724.method_19538().method_1031(0.0, -1.0, 0.0)).method_10079(facingDirection, i));
        }
        this.placeCalculatedBlocks();
    }

    private void placeCalculatedBlocks() {
        for (class_2338 pos : this.placeBlocks) {
            if (!this.placeTimer.passedMs(this.delay.getValue().longValue())) continue;
            this.placeBlock(pos);
        }
        this.placeBlocks.clear();
    }

    private void placeBlock(class_2338 pos) {
        if (Scaffold.mc.field_1687.method_8320(pos).method_26215()) {
            int slot = this.getTargetBlockSlot();
            if (slot == -1) {
                return;
            }
            Managers.INTERACTION.placeBlock(pos, slot, this.strictDirection.getValue(), true, (state, angles) -> {
                if (state && this.rotate.getValue()) {
                    Managers.ROTATION.setRotationSilent(angles[0], angles[1]);
                }
            }, this.airPlace.getValue());
            this.renderBlocks.add((Object)new RenderBlock(pos));
            this.placeTimer.reset();
            if (this.rotate.getValue()) {
                Managers.ROTATION.setRotationSilentSync();
            }
        }
    }

    private int getTargetBlockSlot() {
        class_1661 inv = Scaffold.mc.field_1724.method_31548();
        if (inv.method_7391().method_7909() instanceof class_1747) {
            return inv.field_7545;
        }
        for (int i = 0; i < 9; ++i) {
            if (i == inv.field_7545 || !(inv.method_5438(i).method_7909() instanceof class_1747)) continue;
            return i;
        }
        return -1;
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (Scaffold.nullCheck() || !this.render.getValue()) {
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
            RenderUtils.drawBox(event.getMatrices(), block2.pos, fadedLine, 1.0);
            RenderUtils.drawBoxFilled(event.getMatrices(), block2.pos, fadedFill);
            RenderBuffers.postRender();
        }
    }
}
