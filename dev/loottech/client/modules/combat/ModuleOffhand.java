package dev.loottech.client.modules.combat;

import com.google.common.collect.Lists;
import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.api.utilities.CacheTimer;
import dev.loottech.api.utilities.ExplosionUtil;
import dev.loottech.api.utilities.InventoryUtils;
import dev.loottech.api.utilities.PlayerUtils;
import dev.loottech.client.events.RunTickEvent;
import dev.loottech.client.modules.miscellaneous.ChorusEscape;
import dev.loottech.client.values.impl.ValueBoolean;
import dev.loottech.client.values.impl.ValueCategory;
import dev.loottech.client.values.impl.ValueEnum;
import dev.loottech.client.values.impl.ValueNumber;
import java.util.ArrayList;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1511;
import net.minecraft.class_1792;
import net.minecraft.class_1802;

@RegisterModule(name="Offhand", description="Automatically switch items to your offhand.", category=Module.Category.COMBAT)
public class ModuleOffhand
extends Module {
    ValueEnum mode = new ValueEnum("Mode", "Mode", "Mode for offhand.", (Enum)Modes.Totem);
    public final ValueNumber hp = new ValueNumber("SafeHealth", "SafeHealth", "Health to override to totem.", (Number)Float.valueOf((float)12.0f), (Number)Float.valueOf((float)1.0f), (Number)Float.valueOf((float)20.0f));
    ValueBoolean swordGap = new ValueBoolean("OffhandGap", "OffhandGap", "Automatically switch to gap when sword.", false);
    ValueCategory mainhandCategory = new ValueCategory("Mainhand Totem", "Automatically switch to totem in mainhand when health is low.");
    ValueBoolean mainhandTotem = new ValueBoolean("Enabled", "Enabled", "Allow mainhand toteming to help prevent totem failing.", this.mainhandCategory, false);
    ValueNumber mainhandSlot = new ValueNumber("Slot", "Slot", "Slot to use for mainhanding totems.", this.mainhandCategory, (Number)Integer.valueOf((int)1), (Number)Integer.valueOf((int)1), (Number)Integer.valueOf((int)9));
    CacheTimer mainhandSwapTimer = new CacheTimer();
    private int lastHotbarSlot;
    private int lastTotemCount;
    private class_1792 lastHotbarItem;
    private class_1792 offhandItem;
    private boolean replacing;
    private long replaceTime;
    private boolean totemInMainhand;

    /*
     * Exception decompiling
     */
    @Override
    public void onRunTick(RunTickEvent event) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter$TooOptimisticMatchException
         */
        throw new IllegalStateException("Decompilation failed");
    }

    private class_1792 getGoldenAppleType() {
        if (InventoryUtils.isFound(class_1802.field_8463) && (ModuleOffhand.mc.field_1724.method_6059(class_1294.field_5898) || !InventoryUtils.isFound(class_1802.field_8367))) {
            return class_1802.field_8463;
        }
        return class_1802.field_8367;
    }

    private boolean checkMainhandTotem() {
        if (ModuleOffhand.mc.field_1724.method_6047().method_7909() == class_1802.field_8288 || Managers.MODULE.getInstance(ChorusEscape.class).isUsingChorus()) {
            return false;
        }
        return this.checkLethalCrystal(PlayerUtils.getLocalPlayerHealth());
    }

    private boolean checkLethalCrystal(float health) {
        ArrayList entities = Lists.newArrayList((Iterable)ModuleOffhand.mc.field_1687.method_18112());
        for (class_1297 e : entities) {
            double potential;
            if (e == null || !e.method_5805() || !(e instanceof class_1511)) continue;
            class_1511 crystal = (class_1511)e;
            if (ModuleOffhand.mc.field_1724.method_5858(e) > 144.0 || (double)health + 0.5 > (potential = ExplosionUtil.getDamageTo((class_1297)ModuleOffhand.mc.field_1724, crystal.method_19538(), false))) continue;
            return true;
        }
        return false;
    }

    private boolean checkLethal() {
        float health = PlayerUtils.getLocalPlayerHealth();
        return health <= this.hp.getValue().floatValue() || this.checkLethalCrystal(health) || (float)PlayerUtils.computeFallDamage(ModuleOffhand.mc.field_1724.field_6017, 1.0f) + 0.5f > ModuleOffhand.mc.field_1724.method_6032();
    }

    @Override
    public String getHudInfo() {
        return String.valueOf((int)ModuleOffhand.mc.field_1724.method_31548().method_18861(class_1802.field_8288));
    }

    public static enum Modes {
        Totem,
        Crystal,
        Gapple;

    }
}
