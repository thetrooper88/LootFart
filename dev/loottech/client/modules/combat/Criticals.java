package dev.loottech.client.modules.combat;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.client.InteractType;
import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.api.utilities.EntityUtil;
import dev.loottech.api.utilities.FindItemResult;
import dev.loottech.api.utilities.InventoryUtils;
import dev.loottech.api.utilities.PositionUtil;
import dev.loottech.asm.ducks.IPlayerInteractEntityC2SPacket;
import dev.loottech.client.events.PacketSendEvent;
import dev.loottech.client.values.impl.ValueBoolean;
import dev.loottech.client.values.impl.ValueEnum;
import net.minecraft.class_1268;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1802;
import net.minecraft.class_1829;
import net.minecraft.class_2338;
import net.minecraft.class_2596;
import net.minecraft.class_2680;
import net.minecraft.class_2824;
import net.minecraft.class_2848;
import net.minecraft.class_2879;

@RegisterModule(name="Criticals", description="Makes your attacks critical attacks.", category=Module.Category.COMBAT)
public class Criticals
extends Module {
    public final ValueEnum mode = new ValueEnum("Mode", "Mode of criticals to use.", CriticalModes.Packet);
    ValueBoolean phaseOnlyConfig = new ValueBoolean("PhasedOnly", "GrimV3 mode only", false);
    ValueBoolean wallsOnlyConfig = new ValueBoolean("WallsOnly", "GrimV3 mode only", false);
    boolean moveFixConfig = true;
    private boolean postUpdateGround;
    private boolean postUpdateSprint;

    @Override
    public String getHudInfo() {
        return this.mode.getValue().name();
    }

    @Override
    public void onDisable() {
        this.postUpdateGround = false;
        this.postUpdateSprint = false;
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        IPlayerInteractEntityC2SPacket packet;
        if (Criticals.mc.field_1724 == null || Criticals.mc.field_1687 == null) {
            return;
        }
        class_2596<?> class_25962 = event.getPacket();
        if (class_25962 instanceof IPlayerInteractEntityC2SPacket && (packet = (IPlayerInteractEntityC2SPacket)class_25962).getType() == InteractType.ATTACK) {
            if (Criticals.mc.field_1724.method_3144() || Criticals.mc.field_1724.method_6128() || Criticals.mc.field_1724.method_5799() || Criticals.mc.field_1724.method_5771() || Criticals.mc.field_1724.method_21754() || Criticals.mc.field_1724.method_6059(class_1294.field_5919) || InventoryUtils.isHolding32k()) {
                return;
            }
            class_1297 e = packet.getEntity();
            if (e == null || !e.method_5805() || !(e instanceof class_1309)) {
                return;
            }
            if (EntityUtil.isVehicle(e)) {
                if (this.mode.getValue().name().equals((Object)"Packet")) {
                    for (int i = 0; i < 5; ++i) {
                        Managers.NETWORK.sendQuietPacket((class_2596<?>)class_2824.method_34206((class_1297)e, (boolean)Managers.POSITION.isSneaking()));
                        Managers.NETWORK.sendPacket((class_2596<?>)new class_2879(class_1268.field_5808));
                    }
                }
                return;
            }
            this.postUpdateSprint = Criticals.mc.field_1724.method_5624();
            if (this.postUpdateSprint) {
                Managers.NETWORK.sendPacket((class_2596<?>)new class_2848((class_1297)Criticals.mc.field_1724, class_2848.class_2849.field_12985));
            }
            if (this.mode.getValue().name().equals((Object)"Mace")) {
                if (!(Criticals.mc.field_1724.method_6047().method_7909() instanceof class_1829) || Criticals.mc.field_1724.field_6017 < 5.0f) {
                    return;
                }
                FindItemResult maceResult = InventoryUtils.find(class_1802.field_49814);
                FindItemResult swordResult = InventoryUtils.find(class_1802.field_22022);
                if (!swordResult.found()) {
                    swordResult = InventoryUtils.find(class_1802.field_8802);
                }
                if (maceResult.found() && swordResult.found()) {
                    event.cancel();
                    Managers.INVENTORY.setSlot(maceResult.slot());
                    Managers.NETWORK.sendPacket((class_2596<?>)new class_2879(class_1268.field_5808));
                    Managers.NETWORK.sendQuietPacket(event.getPacket());
                    Managers.INVENTORY.setSlot(swordResult.slot());
                    Managers.INVENTORY.syncToClient();
                }
                return;
            }
            this.attackSpoofJump(e);
        }
    }

    /*
     * Exception decompiling
     */
    public void attackSpoofJump(class_1297 e) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter$TooOptimisticMatchException
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public boolean isDoublePhased() {
        for (class_2338 pos : PositionUtil.getAllInBox(Criticals.mc.field_1724.method_5829(), Criticals.mc.field_1724.method_24515())) {
            class_2680 state = Criticals.mc.field_1687.method_8320(pos);
            class_2680 state2 = Criticals.mc.field_1687.method_8320(pos.method_10084());
            if (!state.method_51366() || !state2.method_51366()) continue;
            return true;
        }
        return false;
    }

    public boolean isPhased() {
        for (class_2338 pos : PositionUtil.getAllInBox(Criticals.mc.field_1724.method_5829())) {
            if (!Criticals.mc.field_1687.method_8320(pos).method_51366()) continue;
            return true;
        }
        return false;
    }

    public static enum CriticalModes {
        Packet,
        GrimV3,
        Mace;

    }
}
