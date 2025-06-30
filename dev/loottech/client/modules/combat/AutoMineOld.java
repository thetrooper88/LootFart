package dev.loottech.client.modules.combat;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.api.utilities.BlockUtils;
import dev.loottech.api.utilities.DirectionUtils;
import dev.loottech.api.utilities.InventoryUtils;
import dev.loottech.api.utilities.RangeUtils;
import dev.loottech.api.utilities.TargetUtils;
import dev.loottech.api.utilities.Timer;
import dev.loottech.api.utilities.render.RenderUtils;
import dev.loottech.client.events.AttackBlockEvent;
import dev.loottech.client.events.Render3DEvent;
import dev.loottech.client.modules.client.ModuleColor;
import dev.loottech.client.values.impl.ValueBoolean;
import dev.loottech.client.values.impl.ValueColor;
import dev.loottech.client.values.impl.ValueEnum;
import dev.loottech.client.values.impl.ValueNumber;
import java.awt.Color;
import java.util.ArrayList;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_2596;
import net.minecraft.class_2846;
import net.minecraft.class_2879;
import net.minecraft.class_3532;

@RegisterModule(name="AutoMine", tag="AutoMine", description="Automatically mine blocks using packets", category=Module.Category.COMBAT)
public class AutoMineOld
extends Module {
    private final ValueBoolean multiTask = new ValueBoolean("MultiTask", "MultiTask", "Mine while eating.", false);
    public ValueNumber range = new ValueNumber("Range", "Range", "", (Number)Double.valueOf((double)4.5), (Number)Double.valueOf((double)1.0), (Number)Double.valueOf((double)10.0));
    private final ValueBoolean doubleMine = new ValueBoolean("DoubleMine", "DoubleMine", "Mine 2 blocks at once.", false);
    private final ValueBoolean grimSpeed = new ValueBoolean("GrimSpeed", "GrimSpeed", "use 0.7 mining speed for grim servers", false);
    private final ValueBoolean swing = new ValueBoolean("SwingHand", "SwingHand", "Visually swing hand when done mining block", false);
    private final ValueBoolean remine = new ValueBoolean("InstantRemine", "InstantRemine", "Remine blocks instantly", false);
    public ValueBoolean auto = new ValueBoolean("Auto", "Auto", "", false);
    public ValueEnum crawlEscape = new ValueEnum("CrawlEscape", "CrawlEscape", "Mine up or down to escape crawl trap.", (Enum)EscapeCrawlModes.OFF);
    public ValueNumber targetRange = new ValueNumber("TargetRange", "TargetRange", "", (Number)Double.valueOf((double)5.0), (Number)Double.valueOf((double)1.0), (Number)Double.valueOf((double)10.0));
    public ValueColor fillColor = new ValueColor("Fill", "Fill", "", new Color(ModuleColor.getColor().getRed(), ModuleColor.getColor().getGreen(), ModuleColor.getColor().getBlue(), 25), false, true);
    public ValueColor outlineColor = new ValueColor("Outline", "Outline", "", Color.WHITE);
    class_2338 singlePos;
    class_2338 doublePos = null;
    class_2338 reminePos = null;
    private final ArrayList<RenderBlock> renderBlocks = new ArrayList();
    double singleProgress;
    double doubleProgress;
    double singleLinearProgress;
    double doubleLinearProgress;
    double singleBreakingSpeed;
    double doubleBreakingSpeed;
    int slot;
    Timer singleTimer = new Timer();
    Timer doubleTimer = new Timer();
    boolean mining = false;
    class_2350 singleDir;
    class_2350 doubleDir;
    boolean swapBackS = false;
    boolean swapBackD = false;

    @Override
    public void onAttackBlock(AttackBlockEvent event) {
        if (AutoMineOld.mc.field_1687 == null || AutoMineOld.mc.field_1724 == null) {
            return;
        }
        if (BlockUtils.canBreak(event.getPos()) && !AutoMineOld.mc.field_1687.method_8320(event.getPos()).method_26204().equals((Object)class_2246.field_9987)) {
            this.addNext(event.getPos());
        }
    }

    @Override
    public void onDisable() {
        this.singlePos = null;
        this.doublePos = null;
        Managers.INVENTORY.syncToClient();
    }

    @Override
    public void onTick() {
        if (AutoMineOld.mc.field_1687 == null || AutoMineOld.mc.field_1724 == null) {
            return;
        }
        if (this.remine.getValue() && this.reminePos != null && !AutoMineOld.mc.field_1724.method_6115() && !AutoMineOld.mc.field_1687.method_8320(this.reminePos).method_26215() && RangeUtils.isInRange(this.reminePos, this.range.getValue().doubleValue())) {
            this.remineBlock(this.reminePos);
            if (this.swapBackS) {
                Managers.INVENTORY.syncToClient();
                this.swapBackS = false;
            }
        }
        if (this.reminePos != null && !RangeUtils.isInRange(this.reminePos, this.range.getValue().doubleValue())) {
            this.reminePos = null;
        }
        if (this.singlePos != null && this.doublePos == null && AutoMineOld.mc.field_1687.method_8320(this.singlePos).method_26215()) {
            this.singlePos = null;
        }
        if (this.doublePos != null && AutoMineOld.mc.field_1687.method_8320(this.doublePos).method_26215()) {
            this.doublePos = null;
        }
        if (this.doublePos == null && this.singlePos == null) {
            this.mining = false;
        }
        if (this.singlePos != null && !RangeUtils.isInRange(this.singlePos, this.range.getValue().doubleValue())) {
            if (this.swapBackS) {
                Managers.INVENTORY.syncToClient();
                this.swapBackS = false;
            }
            this.singlePos = null;
            this.singleLinearProgress = 0.0;
        }
        if (this.doublePos != null && !RangeUtils.isInRange(this.doublePos, this.range.getValue().doubleValue())) {
            if (this.swapBackD) {
                Managers.INVENTORY.syncToClient();
                this.swapBackD = false;
            }
            this.doublePos = null;
            this.doubleLinearProgress = 0.0;
        }
        if (!AutoMineOld.mc.field_1724.method_6115() && this.singleLinearProgress >= 1.0 && this.mining && this.singlePos != null) {
            if (Managers.INVENTORY.getServerSlot() != this.slot) {
                Managers.INVENTORY.setSlot(this.slot);
            }
            this.swapBackS = true;
            this.sendPacket((class_2596<?>)new class_2846(class_2846.class_2847.field_12973, this.singlePos, this.singleDir, AutoMineOld.mc.field_1687.method_41925().method_41937().method_41942()));
            if (this.swing.getValue()) {
                AutoMineOld.mc.field_1724.method_6104(class_1268.field_5808);
            }
            this.reminePos = this.singlePos;
            this.singlePos = null;
            this.singleLinearProgress = 0.0;
        }
        if (!AutoMineOld.mc.field_1724.method_6115() && this.doubleLinearProgress >= 1.0 && this.mining && this.doublePos != null) {
            if (Managers.INVENTORY.getServerSlot() != this.slot) {
                Managers.INVENTORY.setSlot(this.slot);
            }
            this.swapBackD = true;
            this.sendPacket((class_2596<?>)new class_2846(class_2846.class_2847.field_12973, this.doublePos, this.doubleDir, AutoMineOld.mc.field_1687.method_41925().method_41937().method_41942()));
            if (this.swing.getValue()) {
                AutoMineOld.mc.field_1724.method_6104(class_1268.field_5808);
            }
            this.doublePos = null;
            this.doubleLinearProgress = 0.0;
        }
        if (this.auto.getValue() && (this.singlePos == null || this.doublePos == null)) {
            if (AutoMineOld.mc.field_1724.method_20448()) {
                if (this.crawlEscape.getValue().equals((Object)EscapeCrawlModes.UP)) {
                    if (!AutoMineOld.mc.field_1687.method_8320(AutoMineOld.mc.field_1724.method_24515().method_10084()).method_26215() && BlockUtils.canBreak(AutoMineOld.mc.field_1724.method_24515().method_10084())) {
                        this.addNext(AutoMineOld.mc.field_1724.method_24515().method_10084());
                    }
                } else if (this.crawlEscape.getValue().equals((Object)EscapeCrawlModes.DOWN) && !AutoMineOld.mc.field_1687.method_8320(AutoMineOld.mc.field_1724.method_24515().method_10074()).method_26215() && BlockUtils.canBreak(AutoMineOld.mc.field_1724.method_24515().method_10084())) {
                    this.addNext(AutoMineOld.mc.field_1724.method_24515().method_10074());
                }
            }
            for (class_1657 player : AutoMineOld.mc.field_1687.method_18456()) {
                class_2338 second;
                class_2338 first;
                if (player == AutoMineOld.mc.field_1724 || Managers.FRIEND.isFriend(player) || !RangeUtils.isInRange((class_1297)player, this.targetRange.getValue().doubleValue()) || (first = TargetUtils.getMinePos(player, this.singlePos, this.doublePos)) == null || !RangeUtils.isInRange(first, this.range.getValue().doubleValue())) continue;
                this.addNext(first);
                if (!this.doubleMine.getValue() || this.doublePos != null || (second = TargetUtils.getMinePos(player, this.singlePos, this.singlePos, first)) == null || second.equals((Object)first) || !RangeUtils.isInRange(second, this.range.getValue().doubleValue())) continue;
                this.addNext(second);
            }
        }
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        class_238 box;
        double scale;
        if (AutoMineOld.mc.field_1687 == null || AutoMineOld.mc.field_1724 == null) {
            return;
        }
        if (this.remine.getValue() && this.reminePos != null) {
            class_238 box2 = new class_238(this.reminePos);
            RenderUtils.drawBox(event.getMatrices(), box2, this.outlineColor.getValue(), 1.0);
        }
        if (this.singlePos != null) {
            double tickProgress = (float)this.singleTimer.getPassedTicks() + event.getTickDelta();
            this.singleLinearProgress = class_3532.method_15350((double)(tickProgress / this.singleBreakingSpeed), (double)0.0, (double)1.0);
            scale = this.singleLinearProgress * 0.5;
            box = new class_238(this.singlePos).method_1011(0.5).method_1014(scale);
            RenderUtils.drawBox(event.getMatrices(), box, this.outlineColor.getValue(), 1.0);
            RenderUtils.drawBoxFilled(event.getMatrices(), box, this.fillColor.getValue());
        }
        if (this.doublePos != null) {
            double tickProgress = (float)this.doubleTimer.getPassedTicks() + event.getTickDelta();
            this.doubleLinearProgress = class_3532.method_15350((double)(tickProgress / this.doubleBreakingSpeed), (double)0.0, (double)1.0);
            scale = this.doubleLinearProgress * 0.5;
            box = new class_238(this.doublePos).method_1011(0.5).method_1014(scale);
            RenderUtils.drawBox(event.getMatrices(), box, this.outlineColor.getValue(), 1.0);
            RenderUtils.drawBoxFilled(event.getMatrices(), box, this.fillColor.getValue());
        }
    }

    private void singleMine(class_2338 pos) {
        class_2350 dir;
        this.slot = InventoryUtils.getBestTool(AutoMineOld.mc.field_1687.method_8320(pos));
        this.singleTimer.reset();
        this.singleBreakingSpeed = BlockUtils.getBlockBreakingSpeed(this.slot, AutoMineOld.mc.field_1687.method_8320(pos), AutoMineOld.mc.field_1724.method_24828());
        if (this.grimSpeed.getValue()) {
            this.singleBreakingSpeed *= 0.75;
        }
        this.singleProgress = this.singleBreakingSpeed <= 0.0 ? 1.0 : class_3532.method_15350((double)((double)this.singleTimer.getPassedTicks() / this.singleBreakingSpeed), (double)0.0, (double)1.0);
        this.singlePos = pos;
        this.singleDir = dir = DirectionUtils.getDirection(pos);
        this.mining = true;
        Managers.NETWORK.sendPacket((class_2596<?>)new class_2846(class_2846.class_2847.field_12973, pos, dir));
        Managers.NETWORK.sendPacket((class_2596<?>)new class_2846(class_2846.class_2847.field_12968, pos, dir));
        Managers.NETWORK.sendPacket((class_2596<?>)new class_2846(class_2846.class_2847.field_12973, pos, dir));
        Managers.NETWORK.sendPacket((class_2596<?>)new class_2846(class_2846.class_2847.field_12973, pos, dir));
        Managers.NETWORK.sendPacket((class_2596<?>)new class_2846(class_2846.class_2847.field_12968, pos, dir));
        Managers.NETWORK.sendPacket((class_2596<?>)new class_2846(class_2846.class_2847.field_12973, pos, dir));
        Managers.NETWORK.sendPacket((class_2596<?>)new class_2879(class_1268.field_5808));
    }

    private void doubleMine(class_2338 pos) {
        class_2350 dir;
        this.singleProgress = ((double)this.singleTimer.getPassedTicks() / this.singleBreakingSpeed + 15.0) / 100.0;
        this.slot = InventoryUtils.getBestTool(AutoMineOld.mc.field_1687.method_8320(pos));
        this.doubleTimer.reset();
        this.doubleBreakingSpeed = BlockUtils.getBlockBreakingSpeed(this.slot, AutoMineOld.mc.field_1687.method_8320(pos), AutoMineOld.mc.field_1724.method_24828());
        this.doubleProgress = this.doubleBreakingSpeed <= 0.0 ? 1.0 : class_3532.method_15350((double)((double)this.doubleTimer.getPassedTicks() / this.doubleBreakingSpeed), (double)0.0, (double)1.0);
        this.doublePos = pos;
        this.doubleDir = dir = DirectionUtils.getDirection(pos);
        this.mining = true;
        Managers.NETWORK.sendPacket((class_2596<?>)new class_2846(class_2846.class_2847.field_12973, pos, dir));
        Managers.NETWORK.sendPacket((class_2596<?>)new class_2846(class_2846.class_2847.field_12968, pos, dir));
        Managers.NETWORK.sendPacket((class_2596<?>)new class_2846(class_2846.class_2847.field_12973, pos, dir));
        Managers.NETWORK.sendPacket((class_2596<?>)new class_2846(class_2846.class_2847.field_12973, pos, dir));
        Managers.NETWORK.sendPacket((class_2596<?>)new class_2846(class_2846.class_2847.field_12968, pos, dir));
        Managers.NETWORK.sendPacket((class_2596<?>)new class_2846(class_2846.class_2847.field_12973, pos, dir));
        Managers.NETWORK.sendPacket((class_2596<?>)new class_2879(class_1268.field_5808));
    }

    private void remineBlock(class_2338 pos) {
        this.slot = InventoryUtils.getBestTool(AutoMineOld.mc.field_1687.method_8320(pos));
        class_2350 dir = DirectionUtils.getDirection(pos);
        Managers.INVENTORY.setSlot(this.slot);
        this.swapBackS = true;
        this.sendPacket((class_2596<?>)new class_2846(class_2846.class_2847.field_12973, pos, dir));
        this.renderBlocks.add((Object)new RenderBlock(pos));
    }

    public void addNext(class_2338 pos) {
        if (this.singlePos != null && this.singlePos.equals((Object)pos) || this.doublePos != null && this.doublePos.equals((Object)pos)) {
            return;
        }
        if (this.reminePos != null) {
            this.reminePos = null;
        }
        if (this.singlePos == null) {
            this.singleMine(pos);
        } else if (this.doubleMine.getValue() && this.doublePos == null) {
            this.doubleMine(pos);
        }
    }

    public class_2338 getSingleMine() {
        return this.singlePos;
    }

    public class_2338 getDoubleMine() {
        return this.doublePos;
    }

    private static enum EscapeCrawlModes {
        UP,
        DOWN,
        OFF;

    }

    private static class RenderBlock {
        public class_2338 pos;
        public long startTime;

        public RenderBlock(class_2338 pos) {
            this.pos = pos;
            this.startTime = System.currentTimeMillis();
        }
    }
}
