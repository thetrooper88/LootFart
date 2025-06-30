package dev.loottech.client.modules.miscellaneous;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.api.utilities.ChatUtils;
import dev.loottech.api.utilities.FindItemResult;
import dev.loottech.api.utilities.InventoryUtils;
import dev.loottech.api.utilities.PlayerUtils;
import dev.loottech.api.utilities.Timer;
import dev.loottech.client.values.impl.ValueEnum;
import dev.loottech.client.values.impl.ValueNumber;
import java.util.LinkedList;
import java.util.Queue;
import net.minecraft.class_1268;
import net.minecraft.class_1802;
import net.minecraft.class_2246;
import net.minecraft.class_2338;

@RegisterModule(name="AutoSpawner", tag="AutoSpawner", description="Build a wither, iron golem, or snow golem automatically.", category=Module.Category.MISCELLANEOUS)
public class AutoSpawner
extends Module {
    private final ValueEnum mode = new ValueEnum("Mode", "Mode", "What mob to summon.", (Enum)Mobs.Wither);
    private final ValueEnum swapMode = new ValueEnum("Swap", "Swap", "What methods to swap to building items.", (Enum)SwapModes.Normal);
    private final ValueNumber delay = new ValueNumber("Delay", "Delay", "Place delay.", (Number)Double.valueOf((double)10.0), (Number)Double.valueOf((double)0.0), (Number)Double.valueOf((double)100.0));
    private Timer placeTimer = new Timer();
    private final Queue<BlockPlacement> placementQueue = new LinkedList();
    private class_2338 lastPlacedPos = null;

    @Override
    public void onEnable() {
        if (AutoSpawner.nullCheck()) {
            return;
        }
        this.placementQueue.clear();
        this.lastPlacedPos = null;
        class_2338 startPos = AutoSpawner.mc.field_1724.method_24515().method_10076(2);
        switch (((Mobs)this.mode.getValue()).ordinal()) {
            case 0: {
                this.buildWither(startPos);
                break;
            }
            case 1: {
                this.buildIronGolem(startPos);
                break;
            }
            case 2: {
                this.buildSnowGolem(startPos);
            }
        }
        if (this.placementQueue.isEmpty()) {
            this.disable(false);
        }
    }

    @Override
    public void onTick() {
        if (AutoSpawner.nullCheck() || this.placementQueue.isEmpty()) {
            this.disable(false);
            return;
        }
        if (this.lastPlacedPos != null) {
            if (!this.isCorrectBlock(this.lastPlacedPos)) {
                this.placementQueue.add((Object)new BlockPlacement(this.lastPlacedPos, ((BlockPlacement)this.placementQueue.peek()).slot, ((BlockPlacement)this.placementQueue.peek()).itemType));
            }
            this.lastPlacedPos = null;
        }
        if (this.placeTimer.passedMs(this.delay.getValue().longValue())) {
            BlockPlacement placement = (BlockPlacement)this.placementQueue.poll();
            if (placement != null) {
                this.swap(placement.slot);
                PlayerUtils.placeBlock(placement.pos, PlayerUtils.getDirection(placement.pos, null, true), class_1268.field_5808, () -> AutoSpawner.mc.field_1724.method_6104(class_1268.field_5808), false, true);
                this.lastPlacedPos = placement.pos;
                this.placeTimer.reset();
            }
            if (this.placementQueue.isEmpty()) {
                this.disable(false);
            }
        }
        Managers.INVENTORY.syncToClient();
    }

    private boolean isCorrectBlock(class_2338 pos) {
        if (this.placementQueue.isEmpty()) {
            return true;
        }
        BlockPlacement placement = (BlockPlacement)this.placementQueue.peek();
        if (placement == null) {
            return true;
        }
        switch (placement.itemType.ordinal()) {
            case 0: {
                return AutoSpawner.mc.field_1687.method_8320(pos).method_26204() == class_2246.field_10114;
            }
            case 1: {
                return AutoSpawner.mc.field_1687.method_8320(pos).method_26204() == class_2246.field_10177;
            }
            case 2: {
                return AutoSpawner.mc.field_1687.method_8320(pos).method_26204() == class_2246.field_10085;
            }
            case 3: {
                return AutoSpawner.mc.field_1687.method_8320(pos).method_26204() == class_2246.field_10147;
            }
            case 4: {
                return AutoSpawner.mc.field_1687.method_8320(pos).method_26204() == class_2246.field_10491;
            }
        }
        return false;
    }

    private void buildWither(class_2338 startPos) {
        FindItemResult soulSandResult = InventoryUtils.find(class_1802.field_8067);
        FindItemResult skullResult = InventoryUtils.find(class_1802.field_8791);
        if (soulSandResult.found() && skullResult.found()) {
            class_2338 one = startPos;
            class_2338 two = one.method_10084();
            class_2338 three = two.method_10078();
            class_2338 four = two.method_10067();
            class_2338 five = two.method_10084();
            class_2338 six = three.method_10084();
            class_2338 seven = four.method_10084();
            this.addPlacement(one, soulSandResult.slot(), ItemType.SOUL_SAND);
            this.addPlacement(two, soulSandResult.slot(), ItemType.SOUL_SAND);
            this.addPlacement(three, soulSandResult.slot(), ItemType.SOUL_SAND);
            this.addPlacement(four, soulSandResult.slot(), ItemType.SOUL_SAND);
            this.addPlacement(five, skullResult.slot(), ItemType.WITHER_SKULL);
            this.addPlacement(six, skullResult.slot(), ItemType.WITHER_SKULL);
            this.addPlacement(seven, skullResult.slot(), ItemType.WITHER_SKULL);
        } else {
            ChatUtils.sendMessage("[AutoSpawner] You don't have the required materials in your hotbar!");
            this.disable(false);
        }
    }

    private void buildIronGolem(class_2338 startPos) {
        FindItemResult pumpkinResult = InventoryUtils.find(class_1802.field_17519);
        FindItemResult ironBlockResult = InventoryUtils.find(class_1802.field_8773);
        if (pumpkinResult.found() && ironBlockResult.found()) {
            class_2338 center = startPos.method_10084();
            class_2338 base = center.method_10074();
            class_2338 north = base.method_10095();
            class_2338 south = base.method_10072();
            class_2338 east = base.method_10078();
            class_2338 west = base.method_10067();
            this.addPlacement(base, ironBlockResult.slot(), ItemType.IRON_BLOCK);
            this.addPlacement(north, ironBlockResult.slot(), ItemType.IRON_BLOCK);
            this.addPlacement(south, ironBlockResult.slot(), ItemType.IRON_BLOCK);
            this.addPlacement(east, ironBlockResult.slot(), ItemType.IRON_BLOCK);
            this.addPlacement(west, ironBlockResult.slot(), ItemType.IRON_BLOCK);
            this.addPlacement(center, pumpkinResult.slot(), ItemType.PUMPKIN);
        } else {
            ChatUtils.sendMessage("[AutoSpawner] You don't have the required materials in your hotbar!");
            this.disable(false);
        }
    }

    private void buildSnowGolem(class_2338 startPos) {
        FindItemResult pumpkinResult = InventoryUtils.find(class_1802.field_17519);
        FindItemResult snowBlockResult = InventoryUtils.find(class_1802.field_8246);
        if (pumpkinResult.found() && snowBlockResult.found()) {
            class_2338 base = startPos.method_10084();
            class_2338 top = base.method_10084();
            this.addPlacement(base, snowBlockResult.slot(), ItemType.SNOW_BLOCK);
            this.addPlacement(top, snowBlockResult.slot(), ItemType.SNOW_BLOCK);
            this.addPlacement(top.method_10084(), pumpkinResult.slot(), ItemType.PUMPKIN);
        } else {
            ChatUtils.sendMessage("[AutoSpawner] You don't have the required materials in your hotbar!");
            this.disable(false);
        }
    }

    private void addPlacement(class_2338 pos, int slot, ItemType itemType) {
        if (AutoSpawner.mc.field_1687.method_8320(pos).method_26215() || AutoSpawner.mc.field_1687.method_8320(pos).method_26204() == class_2246.field_10382) {
            this.placementQueue.add((Object)new BlockPlacement(pos, slot, itemType));
        }
    }

    private void swap(int slot) {
        if (this.swapMode.getValue() == SwapModes.Normal) {
            Managers.INVENTORY.setClientSlot(slot);
        } else {
            Managers.INVENTORY.setSlot(slot);
        }
    }

    private static enum Mobs {
        Wither,
        Iron_Golem,
        Snow_Golem;

    }

    private static enum SwapModes {
        Normal,
        Silent;

    }

    private static class BlockPlacement {
        public final class_2338 pos;
        public final int slot;
        public final ItemType itemType;

        public BlockPlacement(class_2338 pos, int slot, ItemType itemType) {
            this.pos = pos;
            this.slot = slot;
            this.itemType = itemType;
        }
    }

    private static enum ItemType {
        SOUL_SAND,
        WITHER_SKULL,
        IRON_BLOCK,
        PUMPKIN,
        SNOW_BLOCK;

    }
}
