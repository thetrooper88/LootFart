package dev.loottech.api.manager.client;

import dev.loottech.api.manager.event.EventListener;
import dev.loottech.api.utilities.PerSecondCounter;
import dev.loottech.api.utilities.Util;
import dev.loottech.asm.ducks.IClientPlayNetworkHandler;
import dev.loottech.asm.mixins.accessor.AccessorClientWorld;
import dev.loottech.client.events.LoginEvent;
import dev.loottech.client.events.LogoutEvent;
import dev.loottech.client.events.PacketReceiveEvent;
import dev.loottech.client.events.PacketSendEvent;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.class_2596;
import net.minecraft.class_2896;
import net.minecraft.class_635;
import net.minecraft.class_639;
import net.minecraft.class_640;
import net.minecraft.class_642;
import net.minecraft.class_7202;
import net.minecraft.class_7204;

public class NetworkManager
implements Util,
EventListener {
    private static final Set<class_2596<?>> PACKET_CACHE = new HashSet();
    private final PerSecondCounter outgoingCounter = new PerSecondCounter();
    private final PerSecondCounter incomingCounter = new PerSecondCounter();
    private class_639 address;
    private class_642 info;

    @Override
    public void onLogin(LoginEvent event) {
        this.address = event.getAddress();
        this.info = event.getInfo();
    }

    @Override
    public void onLogout(LogoutEvent event) {
        PACKET_CACHE.clear();
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        this.outgoingCounter.updateCounter();
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        this.incomingCounter.updateCounter();
    }

    public void connect(class_639 address, class_642 info) {
        if (mc.method_1562() == null) {
            return;
        }
        mc.method_1562().method_48296().method_52902(address.method_2952(), address.method_2954(), (class_2896)new class_635(mc.method_1562().method_48296(), mc, info, null, false, null, null, null));
    }

    public void sendPacket(class_2596<?> p) {
        if (mc.method_1562() != null) {
            PACKET_CACHE.add(p);
            mc.method_1562().method_52787(p);
        }
    }

    public void sendQuietPacket(class_2596<?> p) {
        if (mc.method_1562() != null) {
            PACKET_CACHE.add(p);
            ((IClientPlayNetworkHandler)mc.method_1562()).loottech$sendQuietPacket(p);
        }
    }

    public void sendSequencedPacket(class_7204 p) {
        if (NetworkManager.mc.field_1687 != null) {
            class_7202 updater = ((AccessorClientWorld)NetworkManager.mc.field_1687).hookGetPendingUpdateManager().method_41937();
            try {
                int i = updater.method_41942();
                class_2596 packet = p.predict(i);
                this.sendPacket(packet);
            }
            catch (Throwable e) {
                e.printStackTrace();
                if (updater != null) {
                    try {
                        updater.close();
                    }
                    catch (Throwable e1) {
                        e1.printStackTrace();
                        e.addSuppressed(e1);
                    }
                }
                throw e;
            }
            if (updater != null) {
                updater.close();
            }
        }
    }

    public int getClientLatency() {
        class_640 playerEntry;
        if (mc.method_1562() != null && (playerEntry = mc.method_1562().method_2871(NetworkManager.mc.field_1724.method_7334().getId())) != null) {
            return playerEntry.method_2959();
        }
        return 0;
    }

    public class_639 getAddress() {
        return this.address;
    }

    public void setAddress(class_639 address) {
        this.address = address;
    }

    public class_642 getInfo() {
        return this.info;
    }

    public void setInfo(class_642 info) {
        this.info = info;
    }

    public boolean isCrystalPvpCC() {
        return this.getServerIp().contains((CharSequence)"crystalpvp.cc");
    }

    public boolean is2b2t() {
        return this.getServerIp().contains((CharSequence)"2b2t.org");
    }

    public int getOutgoingPPS() {
        return this.outgoingCounter.getPerSecond();
    }

    public int getIncomingPPS() {
        return this.incomingCounter.getPerSecond();
    }

    public String getServerIp() {
        if (this.info != null) {
            return this.info.field_3761;
        }
        return "Singleplayer";
    }

    public boolean isCached(class_2596<?> p) {
        return PACKET_CACHE.contains(p);
    }
}
