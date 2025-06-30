package dev.loottech.client.modules.miscellaneous;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.api.utilities.ChatUtils;
import dev.loottech.api.utilities.InventoryUtils;
import dev.loottech.api.utilities.Timer;
import dev.loottech.api.utilities.render.RenderBlock;
import dev.loottech.api.utilities.render.RenderBuffers;
import dev.loottech.api.utilities.render.RenderUtils;
import dev.loottech.client.events.Render3DEvent;
import dev.loottech.client.modules.client.ModuleColor;
import dev.loottech.client.values.impl.ValueBoolean;
import dev.loottech.client.values.impl.ValueColor;
import dev.loottech.client.values.impl.ValueEnum;
import dev.loottech.client.values.impl.ValueNumber;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.class_1268;
import net.minecraft.class_1802;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2382;
import net.minecraft.class_243;
import net.minecraft.class_3965;

@RegisterModule(name="AutoBuilder", description="Automatically build structures.", category=Module.Category.MISCELLANEOUS)
public class AutoBuilder
extends Module {
    private final ValueEnum structure = new ValueEnum("Structure", "What Structure to build.", Structures.Nether_Portal);
    ValueNumber delay = new ValueNumber("Delay", "Delay between placing blocks.", (Number)Double.valueOf((double)0.0), (Number)Double.valueOf((double)0.0), (Number)Double.valueOf((double)200.0));
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
    private boolean lightPortal = false;

    @Override
    public void onTick() {
        int lighterSlot;
        if (AutoBuilder.nullCheck() || this.multiTask.getValue() && AutoBuilder.mc.field_1724.method_6115()) {
            return;
        }
        if (this.structure.getValue() == Structures.Nether_Portal && this.materialsFound()) {
            Iterator structureOrigin = AutoBuilder.mc.field_1724.method_24515().method_10079(AutoBuilder.mc.field_1724.method_5735(), 3);
            class_2350 dir = AutoBuilder.mc.field_1724.method_5735().method_35833(class_2350.class_2351.field_11052);
            this.placeBlocks.add((Object)structureOrigin);
            this.placeBlocks.add((Object)structureOrigin.method_10093(dir));
            if (!this.airPlace.getValue()) {
                this.placeBlocks.add((Object)structureOrigin.method_10079(dir, -1));
            }
            if (!this.airPlace.getValue()) {
                this.placeBlocks.add((Object)structureOrigin.method_10079(dir, 2));
            }
            this.placeBlocks.add((Object)structureOrigin.method_10084().method_10079(dir, -1));
            this.placeBlocks.add((Object)structureOrigin.method_10084().method_10079(dir, 2));
            this.placeBlocks.add((Object)structureOrigin.method_10084().method_10084().method_10079(dir, -1));
            this.placeBlocks.add((Object)structureOrigin.method_10084().method_10084().method_10079(dir, 2));
            this.placeBlocks.add((Object)structureOrigin.method_10084().method_10084().method_10084().method_10079(dir, -1));
            this.placeBlocks.add((Object)structureOrigin.method_10084().method_10084().method_10084().method_10079(dir, 2));
            if (!this.airPlace.getValue()) {
                this.placeBlocks.add((Object)structureOrigin.method_10084().method_10084().method_10084().method_10084().method_10079(dir, -1));
            }
            if (!this.airPlace.getValue()) {
                this.placeBlocks.add((Object)structureOrigin.method_10084().method_10084().method_10084().method_10084().method_10079(dir, 2));
            }
            this.placeBlocks.add((Object)structureOrigin.method_10084().method_10084().method_10084().method_10084());
            this.placeBlocks.add((Object)structureOrigin.method_10084().method_10084().method_10084().method_10084().method_10093(dir));
        }
        for (class_2338 pos : this.placeBlocks) {
            if (AutoBuilder.mc.field_1687.method_8320(pos).method_26204() == class_2246.field_10540) {
                this.lightPortal = true;
                continue;
            }
            this.lightPortal = false;
        }
        if (this.lightPortal && (lighterSlot = InventoryUtils.find(class_1802.field_8884).slot()) != -1) {
            class_2338 bp = AutoBuilder.mc.field_1724.method_24515().method_10079(AutoBuilder.mc.field_1724.method_5735(), 3);
            class_243 pos = class_243.method_24953((class_2382)bp);
            Managers.INVENTORY.setClientSlot(lighterSlot);
            AutoBuilder.mc.field_1761.method_2896(AutoBuilder.mc.field_1724, class_1268.field_5808, new class_3965(pos, Managers.INTERACTION.getInteractDirection(bp, this.strictDirection.getValue()), bp, false));
            this.disable(false);
        }
        this.placeCalculatedBlocks();
    }

    private boolean materialsFound() {
        if (InventoryUtils.find(class_1802.field_8281).count() > 14 || InventoryUtils.findInHotbar(class_1802.field_8281).count() > 10 && this.airPlace.getValue()) {
            return true;
        }
        ChatUtils.sendMessage("[AutoBuilder] You don't have the required materials in your hotbar!");
        this.disable(false);
        return false;
    }

    private void placeCalculatedBlocks() {
        for (class_2338 pos : this.placeBlocks) {
            if (!this.placeTimer.passedMs(this.delay.getValue().longValue())) continue;
            this.placeBlock(pos, InventoryUtils.find(class_1802.field_8281).slot());
        }
        this.placeTimer.reset();
        this.placeBlocks.clear();
    }

    private void placeBlock(class_2338 pos, int slot) {
        if (AutoBuilder.mc.field_1687.method_8320(pos).method_26215()) {
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

    @Override
    public void onRender3D(Render3DEvent event) {
        if (AutoBuilder.nullCheck() || !this.render.getValue()) {
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

    private static enum Structures {
        Nether_Portal;

    }
}
