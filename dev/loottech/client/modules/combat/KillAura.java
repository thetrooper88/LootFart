package dev.loottech.client.modules.combat;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.api.utilities.FindItemResult;
import dev.loottech.api.utilities.InventoryUtils;
import dev.loottech.api.utilities.RangeUtils;
import dev.loottech.api.utilities.RotationUtils;
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
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1308;
import net.minecraft.class_1621;
import net.minecraft.class_1657;
import net.minecraft.class_1802;

@RegisterModule(name="KillAura", tag="KillAura", description="Attack entities.", category=Module.Category.COMBAT)
public class KillAura
extends Module {
    private final ValueBoolean rotate = new ValueBoolean("Rotate", "Rotate", "Rotate when attacking.", true);
    private final ValueNumber delay = new ValueNumber("Delay", "Delay", "Hit delay in ms.", (Number)Double.valueOf((double)1.0), (Number)Double.valueOf((double)0.0), (Number)Double.valueOf((double)2.0));
    private final ValueNumber range = new ValueNumber("Range", "Range", "Hit range.", (Number)Double.valueOf((double)4.0), (Number)Double.valueOf((double)1.0), (Number)Double.valueOf((double)6.0));
    private final ValueEnum swap = new ValueEnum("Swap", "Swap", "Swap to weapon", (Enum)SwapModes.Require);
    private final ValueBoolean mobs = new ValueBoolean("Mobs", "Mobs", "Attack mobs.", true);
    private final ValueBoolean aggressive = new ValueBoolean("Aggressive", "Aggressive", "Attack mobs only when they are aggressive.", true);
    private final ValueColor fillColor = new ValueColor("Fill", "Fill", "", ModuleColor.getColor(30), false, true);
    private final ValueColor lineColor = new ValueColor("Line", "Line", "", ModuleColor.getColor(), false, true);
    ValueNumber fadeTime = new ValueNumber("FadeTime", "FadeTime", "Render fade time (ms)", (Number)Integer.valueOf((int)300), (Number)Integer.valueOf((int)0), (Number)Integer.valueOf((int)1000));
    private final ArrayList<RenderEntity> renderEntities = new ArrayList();
    private class_1297 player = null;

    @Override
    public void onRender3D(Render3DEvent event) {
        if (KillAura.nullCheck()) {
            return;
        }
        this.renderEntities.removeIf(block -> System.currentTimeMillis() - block.startTime > this.fadeTime.getValue().longValue());
        for (RenderEntity block2 : this.renderEntities) {
            RenderBuffers.preRender();
            float progress = (float)(System.currentTimeMillis() - block2.startTime) / this.fadeTime.getValue().floatValue();
            progress = Math.min((float)1.0f, (float)progress);
            float alpha = (float)Math.pow((double)(1.0f - progress), (double)2.0);
            Color fadedFill = new Color(this.fillColor.getValue().getRed(), this.fillColor.getValue().getGreen(), this.fillColor.getValue().getBlue(), (int)(alpha * (float)this.fillColor.getValue().getAlpha()));
            Color fadedLine = new Color(this.lineColor.getValue().getRed(), this.lineColor.getValue().getGreen(), this.lineColor.getValue().getBlue(), (int)(alpha * (float)this.lineColor.getValue().getAlpha()));
            RenderUtils.drawBox(event.getMatrices(), RenderUtils.getInterpolatedBoundingBox(block2.getEntity(), RenderUtils.getTickDelta()), fadedLine, 1.5);
            RenderUtils.drawBoxFilled(event.getMatrices(), RenderUtils.getInterpolatedBoundingBox(block2.getEntity(), RenderUtils.getTickDelta()), fadedFill);
            RenderBuffers.postRender();
        }
    }

    @Override
    public void onTick() {
        if (KillAura.nullCheck()) {
            return;
        }
        if (this.player != null && (!this.player.method_5805() || KillAura.mc.field_1724.method_19538().method_1022(this.player.method_19538()) >= (double)this.range.getValue().floatValue())) {
            this.player = null;
        }
        for (class_1297 target : KillAura.mc.field_1687.method_18112()) {
            float progress;
            if (!target.method_5732() || target == KillAura.mc.field_1724 || !target.method_5805() || !RangeUtils.isInRange(target, this.range.getValue().doubleValue())) continue;
            if (target instanceof class_1657) {
                if (target == this.player || !RangeUtils.isInRange(target, this.range.getValue().doubleValue()) || Managers.FRIEND.isFriend(target.method_5820())) continue;
                progress = KillAura.mc.field_1724.method_7261(0.0f);
                if (progress >= this.delay.getValue().floatValue()) {
                    this.attackPlayer(target);
                }
            }
            if (this.mobs.getValue() && target instanceof class_1621 && ((class_1621)target).method_7157()) {
                this.attackPlayer(target);
            }
            if (!(target instanceof class_1308) || !this.mobs.getValue() || target.method_16914() || this.aggressive.getValue() && ((class_1308)target).method_5968() != KillAura.mc.field_1724 || !((progress = KillAura.mc.field_1724.method_7261(0.0f)) >= this.delay.getValue().floatValue())) continue;
            this.attackPlayer(target);
        }
    }

    private void attackPlayer(class_1297 target) {
        if (this.swap.getValue().equals((Object)SwapModes.Normal) && (InventoryUtils.isFound(class_1802.field_22022) || InventoryUtils.isFound(class_1802.field_8802))) {
            this.swapToSword();
        } else if (this.swap.getValue().equals((Object)SwapModes.Require) && !KillAura.mc.field_1724.method_6047().method_7909().equals((Object)class_1802.field_22022) && !KillAura.mc.field_1724.method_6047().method_7909().equals((Object)class_1802.field_8802)) {
            return;
        }
        if (this.rotate.getValue()) {
            float[] r = RotationUtils.getRotationsEntity(target);
            Managers.ROTATION.setRotationSilent(r[0], r[1]);
        }
        KillAura.mc.field_1761.method_2918((class_1657)KillAura.mc.field_1724, target);
        KillAura.mc.field_1724.method_6104(class_1268.field_5808);
        KillAura.mc.field_1724.method_7350();
        this.renderEntities.add((Object)new RenderEntity(this, target));
        Managers.ROTATION.setRotationSilentSync();
    }

    private void swapToSword() {
        int slot = 99;
        FindItemResult nethResult = InventoryUtils.find(class_1802.field_22022);
        FindItemResult diaResult = InventoryUtils.find(class_1802.field_8802);
        if (nethResult.slot() >= 9) {
            slot = nethResult.slot();
        } else if (diaResult.slot() >= 9) {
            slot = diaResult.slot();
        }
        Managers.INVENTORY.setClientSlot(slot);
    }

    private static enum SwapModes {
        Require,
        Normal;

    }

    private class RenderEntity {
        public class_1297 entity;
        public long startTime;

        public class_1297 getEntity() {
            return this.entity;
        }

        public RenderEntity(KillAura killAura, class_1297 entity) {
            this.entity = entity;
            this.startTime = System.currentTimeMillis();
        }
    }
}
