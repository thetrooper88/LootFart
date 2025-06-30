package dev.loottech.client.modules.combat;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.api.utilities.Animation;
import dev.loottech.api.utilities.BlastResistantBlocks;
import dev.loottech.api.utilities.PositionUtil;
import dev.loottech.api.utilities.render.RenderBuffers;
import dev.loottech.api.utilities.render.RenderManager;
import dev.loottech.client.events.PacketReceiveEvent;
import dev.loottech.client.events.PreTickEvent;
import dev.loottech.client.events.Render3DEvent;
import dev.loottech.client.modules.client.AntiCheat;
import dev.loottech.client.modules.client.ModuleColor;
import dev.loottech.client.modules.combat.AutoMine;
import dev.loottech.client.modules.combat.SelfTrap;
import dev.loottech.client.values.impl.ValueBoolean;
import dev.loottech.client.values.impl.ValueColor;
import dev.loottech.client.values.impl.ValueEnum;
import dev.loottech.client.values.impl.ValueNumber;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1299;
import net.minecraft.class_1511;
import net.minecraft.class_1657;
import net.minecraft.class_1747;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_2382;
import net.minecraft.class_2596;
import net.minecraft.class_2604;
import net.minecraft.class_2626;
import net.minecraft.class_2664;
import net.minecraft.class_2680;
import net.minecraft.class_2824;
import net.minecraft.class_2879;
import net.minecraft.class_3726;
import net.minecraft.class_8042;

@RegisterModule(name="Surround", description="Places blocks around your feet to protect you from crystals.", category=Module.Category.COMBAT)
public class Surround
extends Module {
    private final Map<class_2338, Long> packets = new HashMap();
    private final Map<class_2338, Animation> fadeList = new HashMap();
    ValueBoolean multitaskConfig = new ValueBoolean("MultiTask", "Place blocks while using items.", false);
    ValueBoolean rotateConfig = new ValueBoolean("Rotate", "Rotate to block placement.", false);
    ValueBoolean strictDirectionConfig = new ValueBoolean("StrictDirection", "Places on visible sides only.", false);
    ValueBoolean airPlace = new ValueBoolean("AirPlace", "Place surround blocks in air.", false);
    ValueEnum timingConfig = new ValueEnum("Timing", "Timing for replacing blocks", Timing.VANILLA);
    ValueNumber placeRangeConfig = new ValueNumber("PlaceRange", "The placement range for surround", (Number)Float.valueOf((float)4.0f), (Number)Float.valueOf((float)0.0f), (Number)Float.valueOf((float)6.0f));
    ValueBoolean attackConfig = new ValueBoolean("Attack Crystals", "Attacks crystals in the way of surround", true);
    ValueBoolean extendConfig = new ValueBoolean("Extend", "Extends surround if the player is not in the center of a block", true);
    ValueBoolean headConfig = new ValueBoolean("Head", "Place a block at your head", false);
    ValueBoolean mineExtendConfig = new ValueBoolean("Blocker", "Extends surround if the block is being mined", false);
    ValueBoolean supportConfig = new ValueBoolean("Below", "Creates a floor for the surround if there is none", false);
    ValueNumber shiftTicksConfig = new ValueNumber("BPT", "The number of blocks to place per tick", (Number)Integer.valueOf((int)2), (Number)Integer.valueOf((int)1), (Number)Integer.valueOf((int)10));
    ValueNumber shiftDelayConfig = new ValueNumber("Place Delay", "The delay between each block placement interval", (Number)Float.valueOf((float)1.0f), (Number)Float.valueOf((float)0.0f), (Number)Float.valueOf((float)5.0f));
    ValueBoolean jumpDisableConfig = new ValueBoolean("AutoDisable", "Disables after moving out of the hole", true);
    ValueBoolean renderConfig = new ValueBoolean("Render", "Renders a box on each block placement", false);
    ValueColor fillColor = new ValueColor("Fill", "Fill", "Color to fill the box with", ModuleColor.getColor(30), false, true);
    ValueColor lineColor = new ValueColor("Line", "Line", "Color to outline the box with", ModuleColor.getColor(255), false, true);
    int fadeTimeConfig = 250;
    private int blocksPlaced;
    private List<class_2338> surround = new ArrayList();
    private List<class_2338> placements = new ArrayList();
    private double prevY;
    protected static final class_2680 DEFAULT_OBSIDIAN_STATE = class_2246.field_10540.method_9564();
    private static final List<class_2248> RESISTANT_BLOCKS = new LinkedList<class_2248>(){
        {
            this.add(class_2246.field_10540);
            this.add(class_2246.field_22423);
            this.add(class_2246.field_10443);
        }
    };

    @Override
    public void onEnable() {
        if (Surround.mc.field_1724 == null) {
            return;
        }
        this.prevY = Surround.mc.field_1724.method_23318();
    }

    @Override
    public void onDisable() {
        this.surround.clear();
        this.placements.clear();
        this.packets.clear();
        this.fadeList.clear();
    }

    @Override
    public void onPreTick(PreTickEvent event) {
        this.blocksPlaced = 0;
        if (Managers.MODULE.getInstance(SelfTrap.class).isEnabled()) {
            return;
        }
        if (this.jumpDisableConfig.getValue() && (Surround.mc.field_1724.method_23318() - this.prevY > 0.5 || Surround.mc.field_1724.field_6017 > 1.5f)) {
            this.disable(false);
            return;
        }
        if (!this.multitaskConfig.getValue() && this.checkMultitask()) {
            this.surround.clear();
            this.placements.clear();
            return;
        }
        int slot = this.getResistantBlockItem();
        if (slot == -1) {
            this.surround.clear();
            this.placements.clear();
            return;
        }
        this.surround = this.getSurround((class_1657)Surround.mc.field_1724);
        if (this.surround.isEmpty()) {
            return;
        }
        if (this.attackConfig.getValue()) {
            this.attackBlockingCrystals(this.surround);
        }
        this.placements = this.getPlacementsFromSurround(this.surround);
        if (this.placements.isEmpty()) {
            return;
        }
        if (this.supportConfig.getValue()) {
            for (class_2338 block : new ArrayList(this.placements)) {
                class_2350 direction;
                if ((double)block.method_10264() > (double)Surround.mc.field_1724.method_31478() + 1.0 || (direction = Managers.INTERACTION.getInteractDirectionInternal(block, this.strictDirectionConfig.getValue())) != null) continue;
                this.placements.add((Object)block.method_10074());
            }
        }
        this.placements.sort(Comparator.comparingInt(class_2382::method_10264));
        while (this.blocksPlaced < this.shiftTicksConfig.getValue().intValue() && this.blocksPlaced < this.placements.size()) {
            class_2338 targetPos = (class_2338)this.placements.get(this.blocksPlaced);
            this.placeBlock(targetPos, slot);
        }
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (Surround.mc.field_1724 == null || Surround.mc.field_1687 == null) {
            return;
        }
        Iterator iterator = event.getPacket();
        if (iterator instanceof class_8042) {
            class_8042 packet = (class_8042)iterator;
            for (class_2596 packet1 : packet.method_48324()) {
                this.handlePackets(packet1);
            }
        } else {
            this.handlePackets(event.getPacket());
        }
    }

    private void handlePackets(class_2596<?> serverPacket) {
        class_2338 pos;
        int slot;
        class_2626 packet;
        if (this.timingConfig.getValue() != Timing.SEQUENTIAL) {
            return;
        }
        if (serverPacket instanceof class_2626) {
            packet = (class_2626)serverPacket;
            class_2680 blockState = packet.method_11308();
            class_2338 targetPos = packet.method_11309();
            if (this.surround.contains((Object)targetPos)) {
                if (blockState.method_45474() && Surround.mc.field_1687.method_8628(DEFAULT_OBSIDIAN_STATE, targetPos, class_3726.method_16194())) {
                    slot = this.getResistantBlockItem();
                    if (slot == -1) {
                        return;
                    }
                    this.placeBlock(targetPos, slot);
                } else if (BlastResistantBlocks.isBlastResistant(blockState)) {
                    this.packets.remove((Object)targetPos);
                }
            }
        }
        if (this.blocksPlaced > this.shiftTicksConfig.getValue().intValue() * 2) {
            return;
        }
        if (serverPacket instanceof class_2664 && this.surround.contains((Object)(pos = class_2338.method_49637((double)(packet = (class_2664)serverPacket).method_11475(), (double)packet.method_11477(), (double)packet.method_11478())))) {
            int slot2 = this.getResistantBlockItem();
            if (slot2 == -1) {
                return;
            }
            this.placeBlock(pos, slot2);
        }
        if (serverPacket instanceof class_2604 && ((packet = (class_2604)serverPacket).method_11169().equals((Object)class_1299.field_6110) || packet.method_11169().equals((Object)class_1299.field_6052))) {
            for (class_2338 pos2 : this.surround) {
                if (!pos2.equals((Object)class_2338.method_49637((double)packet.method_11175(), (double)packet.method_11174(), (double)packet.method_11176()))) continue;
                slot = this.getResistantBlockItem();
                if (slot == -1) {
                    return;
                }
                this.placeBlock(pos2, slot);
                break;
            }
        }
    }

    private void placeBlock(class_2338 pos, int slot) {
        Managers.INTERACTION.placeBlock(pos, slot, this.strictDirectionConfig.getValue(), true, (state, angles) -> {
            if (state && this.rotateConfig.getValue()) {
                Managers.ROTATION.setRotationSilent(angles[0], angles[1]);
            }
        }, this.airPlace.getValue());
        this.packets.put((Object)pos, (Object)System.currentTimeMillis());
        ++this.blocksPlaced;
        if (this.rotateConfig.getValue()) {
            Managers.ROTATION.setRotationSilentSync();
        }
    }

    public void attackBlockingCrystals(List<class_2338> posList) {
        for (class_2338 pos : posList) {
            class_1297 crystalEntity = (class_1297)Surround.mc.field_1687.method_8335(null, new class_238(pos)).stream().filter(e -> e instanceof class_1511).findFirst().orElse(null);
            if (crystalEntity == null) continue;
            Managers.NETWORK.sendPacket((class_2596<?>)class_2824.method_34206((class_1297)crystalEntity, (boolean)Surround.mc.field_1724.method_5715()));
            Managers.NETWORK.sendPacket((class_2596<?>)new class_2879(class_1268.field_5808));
            return;
        }
    }

    public List<class_2338> getPlacementsFromSurround(List<class_2338> surround) {
        ArrayList placements = new ArrayList();
        for (class_2338 surroundPos : surround) {
            double dist;
            Long placed = (Long)this.packets.get((Object)surroundPos);
            if (this.shiftDelayConfig.getValue().floatValue() > 0.0f && placed != null && (float)(System.currentTimeMillis() - placed) < this.shiftDelayConfig.getValue().floatValue() * 50.0f || !Surround.mc.field_1687.method_8320(surroundPos).method_45474() || (dist = Surround.mc.field_1724.method_5707(surroundPos.method_46558())) > (double)(this.placeRangeConfig.getValue().floatValue() * this.placeRangeConfig.getValue().floatValue()) || !Surround.mc.field_1687.method_8628(DEFAULT_OBSIDIAN_STATE, surroundPos, class_3726.method_16194())) continue;
            placements.add((Object)surroundPos);
        }
        return placements;
    }

    public List<class_2338> getSurround(class_1657 player) {
        class_2338 miningPos;
        HashSet surroundBlocks = new HashSet(this.getSurroundNoDown(player));
        HashSet playerBlocks = new HashSet(this.getPlayerBlocks(player));
        for (class_2338 playerPos : playerBlocks) {
            if (playerPos.equals((Object)player.method_24515())) continue;
            surroundBlocks.add((Object)playerPos.method_10074());
        }
        if (this.mineExtendConfig.getValue()) {
            for (class_2338 surroundPos : new HashSet((Collection)surroundBlocks)) {
                if (!Managers.BLOCK.isPassed(surroundPos, 0.7f)) continue;
                for (class_2350 direction : class_2350.values()) {
                    class_2338 blockerPos;
                    if (direction == class_2350.field_11033 || playerBlocks.contains((Object)(blockerPos = surroundPos.method_10093(direction))) || Managers.MODULE.getInstance(AutoMine.class).getMiningBlock() == blockerPos) continue;
                    surroundBlocks.add((Object)blockerPos);
                }
            }
        }
        if (Managers.MODULE.getInstance(AntiCheat.class).isEnabled() && Managers.MODULE.getInstance(AntiCheat.class).grimAirPlace.getValue() && this.headConfig.getValue()) {
            surroundBlocks.add((Object)Surround.mc.field_1724.method_24515().method_10086(2));
        }
        if ((miningPos = Managers.MODULE.getInstance(AutoMine.class).getMiningBlock()) == null || !miningPos.equals((Object)player.method_24515().method_10074())) {
            surroundBlocks.add((Object)Surround.mc.field_1724.method_24515().method_10074());
        }
        return new ArrayList((Collection)surroundBlocks);
    }

    public List<class_2338> getSurroundNoDown(class_1657 player) {
        return this.getSurroundNoDown(player, 0.0f);
    }

    public List<class_2338> getSurroundNoDown(class_1657 player, float range) {
        HashSet surroundBlocks = new HashSet();
        HashSet playerBlocks = new HashSet(this.getPlayerBlocks(player));
        for (class_2338 pos : playerBlocks) {
            if (range > 0.0f && Surround.mc.field_1724.method_33571().method_1025(pos.method_46558()) > (double)(range * range)) continue;
            for (class_2350 dir : class_2350.values()) {
                class_2338 pos1;
                if (!dir.method_10166().method_10179() || playerBlocks.contains((Object)(pos1 = pos.method_10093(dir)))) continue;
                surroundBlocks.add((Object)pos1);
            }
        }
        return new ArrayList((Collection)surroundBlocks);
    }

    public List<class_2338> getPlayerBlocks(class_1657 entity) {
        class_2338 playerPos = PositionUtil.getRoundedBlockPos(entity.method_23317(), entity.method_23318(), entity.method_23321());
        ArrayList playerBlocks = new ArrayList();
        if (this.extendConfig.getValue()) {
            playerBlocks.addAll(PositionUtil.getAllInBox(entity.method_5829(), playerPos));
        } else {
            playerBlocks.add((Object)playerPos);
        }
        return playerBlocks;
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (Managers.MODULE.getInstance(SelfTrap.class).isEnabled()) {
            return;
        }
        if (this.renderConfig.getValue()) {
            RenderBuffers.preRender();
            for (Map.Entry set : this.fadeList.entrySet()) {
                ((Animation)set.getValue()).setState(false);
                int boxAlpha = (int)((double)this.fillColor.getValue().getAlpha() * ((Animation)set.getValue()).getFactor());
                int lineAlpha = (int)((double)this.lineColor.getValue().getAlpha() * ((Animation)set.getValue()).getFactor());
                Color boxColor = this.fillColor.getValue(boxAlpha);
                Color outlineColor = this.lineColor.getValue(lineAlpha);
                RenderManager.renderBox(event.getMatrices(), (class_2338)set.getKey(), boxColor.getRGB());
                RenderManager.renderBoundingBox(event.getMatrices(), (class_2338)set.getKey(), 1.0f, outlineColor.getRGB());
            }
            RenderBuffers.postRender();
            if (this.placements.isEmpty()) {
                return;
            }
            for (class_2338 pos : this.placements) {
                Animation animation = new Animation(true, this.fadeTimeConfig);
                this.fadeList.put((Object)pos, (Object)animation);
            }
        }
        this.fadeList.entrySet().removeIf(e -> ((Animation)e.getValue()).getFactor() == 0.0);
    }

    public boolean isPlacing() {
        return !this.placements.isEmpty();
    }

    protected int getResistantBlockItem() {
        HashSet blockSlots = new HashSet();
        for (class_2248 type : RESISTANT_BLOCKS) {
            int slot = this.getBlockItemSlot(type);
            if (slot == -1) continue;
            blockSlots.add((Object)new BlockSlot(type, slot));
        }
        BlockSlot slot = (BlockSlot)((Object)blockSlots.stream().filter(b -> b.block() == class_2246.field_10540).findFirst().orElse(null));
        if (slot != null) {
            return slot.slot();
        }
        BlockSlot slot1 = (BlockSlot)((Object)blockSlots.stream().filter(b -> b.block() == class_2246.field_22423).findFirst().orElse(null));
        if (slot1 != null) {
            return slot1.slot();
        }
        BlockSlot slot2 = (BlockSlot)((Object)blockSlots.stream().filter(b -> b.block() == class_2246.field_10443).findFirst().orElse(null));
        if (slot2 != null) {
            return slot2.slot();
        }
        return -1;
    }

    protected int getBlockItemSlot(class_2248 block) {
        for (int i = 0; i < 9; ++i) {
            class_1747 blockItem;
            class_1799 stack = Surround.mc.field_1724.method_31548().method_5438(i);
            class_1792 class_17922 = stack.method_7909();
            if (!(class_17922 instanceof class_1747) || (blockItem = (class_1747)class_17922).method_7711() != block) continue;
            return i;
        }
        return -1;
    }

    public boolean checkMultitask() {
        return this.checkMultitask(false);
    }

    public boolean checkMultitask(boolean checkOffhand) {
        if (checkOffhand && Surround.mc.field_1724.method_6058() != class_1268.field_5808) {
            return false;
        }
        return Surround.mc.field_1724.method_6115();
    }

    public static enum Timing {
        VANILLA,
        SEQUENTIAL;

    }

    public record BlockSlot(class_2248 block, int slot) {
        public boolean equals(Object obj) {
            BlockSlot b;
            return obj instanceof BlockSlot && (b = (BlockSlot)((Object)obj)).block() == this.block;
        }
    }
}
