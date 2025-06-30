package dev.loottech.api.manager.miscellaneous;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.event.EventListener;
import dev.loottech.api.manager.module.Module;
import dev.loottech.api.utilities.Util;
import dev.loottech.client.events.PacketReceiveEvent;
import dev.loottech.client.events.TickEvent;
import dev.loottech.client.events.TotemPopEvent;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_1937;
import net.minecraft.class_2596;
import net.minecraft.class_2663;
import net.minecraft.class_3532;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TotemPopManager
implements Util,
EventListener {
    public HashMap<String, Integer> popList = new HashMap();

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        class_2663 packet;
        if (Module.nullCheck()) {
            return;
        }
        class_2596<?> class_25962 = event.getPacket();
        if (class_25962 instanceof class_2663 && (packet = (class_2663)class_25962).method_11470() == 35) {
            class_1297 ent = packet.method_11469((class_1937)TotemPopManager.mc.field_1687);
            if (!(ent instanceof class_1657)) {
                return;
            }
            String name = ent.method_5477().getString();
            int currentPops = (Integer)this.popList.getOrDefault((Object)name, (Object)0);
            this.popList.put((Object)name, (Object)(currentPops + 1));
            TotemPopEvent popEvent = new TotemPopEvent((class_1657)ent, currentPops + 1);
            Managers.EVENT.call(popEvent);
        }
    }

    @Override
    public void onTick(TickEvent event) {
        if (Module.nullCheck()) {
            return;
        }
        for (class_1657 player : TotemPopManager.mc.field_1687.method_18456()) {
            if (!(player.method_6032() <= 0.0f) || !this.popList.containsKey((Object)player.method_5477().getString())) continue;
            this.popList.remove((Object)player.method_5477().getString(), this.popList.get((Object)player.method_5477().getString()));
        }
    }

    public int getPops(@NotNull class_1657 entity) {
        if (this.popList.get((Object)entity.method_5477().getString()) == null) {
            return 0;
        }
        return (Integer)this.popList.get((Object)entity.method_5477().getString());
    }

    public List<class_1657> getTargets(float range) {
        return (List)TotemPopManager.mc.field_1687.method_18456().stream().filter(e -> !e.method_29504()).filter(entityPlayer -> !Managers.FRIEND.isFriend((class_1657)entityPlayer)).filter(entityPlayer -> entityPlayer != TotemPopManager.mc.field_1724).filter(entityPlayer -> TotemPopManager.mc.field_1724.method_5858((class_1297)entityPlayer) < (double)(range * range)).sorted(Comparator.comparing(e -> TotemPopManager.mc.field_1724.method_5858((class_1297)e))).collect(Collectors.toList());
    }

    @Nullable
    public class_1657 getTarget(float range, @NotNull TargetBy targetBy) {
        class_1657 target = null;
        switch (targetBy.ordinal()) {
            case 1: {
                target = this.getTargetByFOV(range);
                break;
            }
            case 2: {
                target = this.getTargetByHealth(range);
                break;
            }
            case 0: {
                target = this.getNearestTarget(range);
            }
        }
        return target;
    }

    @Nullable
    public class_1657 getNearestTarget(float range) {
        return (class_1657)this.getTargets(range).stream().min(Comparator.comparing(t -> Float.valueOf((float)TotemPopManager.mc.field_1724.method_5739((class_1297)t)))).orElse(null);
    }

    public class_1657 getTargetByHealth(float range) {
        return (class_1657)this.getTargets(range).stream().min(Comparator.comparing(t -> Float.valueOf((float)(t.method_6032() + t.method_6067())))).orElse(null);
    }

    public class_1657 getTargetByFOV(float range) {
        return (class_1657)this.getTargets(range).stream().min(Comparator.comparing(this::getFOVAngle)).orElse(null);
    }

    public class_1657 getTargetByFOV(float range, float fov) {
        return (class_1657)this.getTargets(range).stream().filter(entityPlayer -> this.getFOVAngle((class_1309)entityPlayer) < fov).min(Comparator.comparing(this::getFOVAngle)).orElse(null);
    }

    @Nullable
    public class_1657 getTarget(float range, @NotNull TargetBy targetBy, @NotNull Predicate<class_1657> predicate) {
        class_1657 target = null;
        switch (targetBy.ordinal()) {
            case 1: {
                target = this.getTargetByFOV(range, predicate);
                break;
            }
            case 2: {
                target = this.getTargetByHealth(range, predicate);
                break;
            }
            case 0: {
                target = this.getNearestTarget(range, predicate);
            }
        }
        return target;
    }

    @Nullable
    public class_1657 getNearestTarget(float range, Predicate<class_1657> predicate) {
        return (class_1657)this.getTargets(range).stream().filter(predicate).min(Comparator.comparing(t -> Float.valueOf((float)TotemPopManager.mc.field_1724.method_5739((class_1297)t)))).orElse(null);
    }

    public class_1657 getTargetByHealth(float range, Predicate<class_1657> predicate) {
        return (class_1657)this.getTargets(range).stream().filter(predicate).min(Comparator.comparing(t -> Float.valueOf((float)(t.method_6032() + t.method_6067())))).orElse(null);
    }

    public class_1657 getTargetByFOV(float range, Predicate<class_1657> predicate) {
        return (class_1657)this.getTargets(range).stream().filter(predicate).min(Comparator.comparing(this::getFOVAngle)).orElse(null);
    }

    private float getFOVAngle(@NotNull class_1309 e) {
        float yaw = (float)class_3532.method_15338((double)(Math.toDegrees((double)Math.atan2((double)(e.method_23321() - TotemPopManager.mc.field_1724.method_23321()), (double)(e.method_23317() - TotemPopManager.mc.field_1724.method_23317()))) - 90.0));
        return Math.abs((float)(yaw - class_3532.method_15393((float)TotemPopManager.mc.field_1724.method_36454())));
    }

    public static enum TargetBy {
        Distance,
        FOV,
        Health;

    }
}
