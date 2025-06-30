package dev.loottech.client.modules.client;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.api.utilities.ColorUtils;
import dev.loottech.api.utilities.MathUtils;
import dev.loottech.api.utilities.TPSUtils;
import dev.loottech.api.utilities.TimerUtils;
import dev.loottech.client.events.Render2DEvent;
import dev.loottech.client.modules.client.ModuleColor;
import dev.loottech.client.values.impl.ValueBoolean;
import dev.loottech.client.values.impl.ValueEnum;
import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_124;
import net.minecraft.class_1291;
import net.minecraft.class_1738;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_640;
import net.minecraft.class_642;
import net.minecraft.util.profiling.jfr.event.PacketSentEvent;

@RegisterModule(name="HUD", tag="HUD", description="Render information on screen.", category=Module.Category.CLIENT)
public class ModuleHUD
extends Module {
    public static ModuleHUD INSTANCE;
    ValueBoolean watermark = new ValueBoolean("Watermark", "Watermark", "", true);
    ValueBoolean direction = new ValueBoolean("Direction", "Direction", "", true);
    ValueBoolean potionEffects = new ValueBoolean("PotionEffects", "Potion Effects", "", true);
    public ValueEnum effectHud = new ValueEnum("EffectHUD", "Effect HUD", "", (Enum)effectHuds.Hide);
    ValueBoolean serverBrand = new ValueBoolean("ServerBrand", "Server Brand", "", true);
    ValueBoolean tps = new ValueBoolean("TPS", "TPS", "", true);
    ValueBoolean fps = new ValueBoolean("FPS", "FPS", "", true);
    ValueBoolean speed = new ValueBoolean("Speed", "Speed", "", true);
    ValueBoolean ping = new ValueBoolean("Ping", "Ping", "", true);
    ValueBoolean packetPS = new ValueBoolean("PacketsPS", "Packets/s", "", false);
    ValueEnum ordering = new ValueEnum("Ordering", "Ordering", "", (Enum)orderings.Length);
    ValueBoolean coords = new ValueBoolean("Coords", "Coords", "", true);
    ValueBoolean netherCoords = new ValueBoolean("NetherCoords", "Nether Coords", "", true);
    ValueBoolean durability = new ValueBoolean("Durability", "Durability", "", true);
    ValueBoolean arrayList = new ValueBoolean("ArrayList", "Array List", "", true);
    ValueEnum modulesColor = new ValueEnum("ModulesColor", "Modules Color", "Color mode for array list.", (Enum)modulesColors.Normal);
    ValueEnum rendering = new ValueEnum("Rendering", "Rendering", "", (Enum)renderings.Up);
    DecimalFormat format = new DecimalFormat("#.#");
    ArrayList<Module> modules;
    private int packets;
    TimerUtils packetTimer = new TimerUtils();
    private int maxFPS = 0;
    int compAdd;
    ArrayList<String> comps;
    class_124 reset = class_124.field_1070;
    class_124 gray = class_124.field_1080;

    public ModuleHUD() {
        INSTANCE = this;
    }

    @EventHandler
    public void onPacketSend(PacketSentEvent event) {
        ++this.packets;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.packetTimer.hasTimeElapsed(1000L)) {
            this.packets = 0;
            this.packetTimer.reset();
        }
        if (mc.method_47599() > this.maxFPS) {
            this.maxFPS = mc.method_47599();
        }
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        class_332 context = event.getContext();
        if (this.nullCheck()) {
            return;
        }
        Color durabilityColor = Color.WHITE;
        class_1799 heldItem = ModuleHUD.mc.field_1724.method_6047();
        if (this.isItemTool(heldItem.method_7909())) {
            float green = ((float)heldItem.method_7936() - (float)heldItem.method_7919()) / (float)heldItem.method_7936();
            float red = 1.0f - green;
            durabilityColor = new Color(red, green, 0.0f);
        }
        this.modules = new ArrayList();
        this.compAdd = 0;
        this.comps = new ArrayList();
        float sWidth = mc.method_22683().method_4486();
        float sHeight = mc.method_22683().method_4502();
        if (this.watermark.getValue()) {
            String watermarkText = "LootTech Beta 0.73";
            Managers.FONT.drawWithShadow(context.method_51448(), watermarkText, 2.0f, 2.0f, ModuleColor.getColor().getRGB());
        }
        if (this.direction.getValue()) {
            String directionText = this.getDirectionName() + String.valueOf((Object)this.gray) + " [" + String.valueOf((Object)this.reset) + this.getFacing(ModuleHUD.mc.field_1724.method_5735().method_10151()) + String.valueOf((Object)this.gray) + "]";
            Managers.FONT.drawWithShadow(context.method_51448(), directionText, 2.0f, (float)((int)(sHeight - 12.0f - (float)(this.coords.getValue() ? 10 : 0))), -1);
        }
        if (this.coords.getValue()) {
            Iterator coordsText = String.valueOf((Object)this.gray) + "XYZ" + String.valueOf((Object)this.reset) + " " + this.format.format(ModuleHUD.mc.field_1724.method_23317()) + String.valueOf((Object)this.gray) + ", " + String.valueOf((Object)this.reset) + this.format.format(ModuleHUD.mc.field_1724.method_23318()) + String.valueOf((Object)this.gray) + ", " + String.valueOf((Object)this.reset) + this.format.format(ModuleHUD.mc.field_1724.method_23321()) + (String)(this.netherCoords.getValue() ? String.valueOf((Object)this.gray) + " [" + String.valueOf((Object)this.reset) + this.format.format(ModuleHUD.mc.field_1724.method_37908().method_27983().method_29177().equals((Object)"minecraft:the_nether") ? ModuleHUD.mc.field_1724.method_23317() * 8.0 : ModuleHUD.mc.field_1724.method_23317() / 8.0) + String.valueOf((Object)this.gray) + ", " + String.valueOf((Object)this.reset) + this.format.format(ModuleHUD.mc.field_1724.method_37908().method_27983().method_29177().equals((Object)"minecraft:the_nether") ? ModuleHUD.mc.field_1724.method_23321() * 8.0 : ModuleHUD.mc.field_1724.method_23321() / 8.0) + String.valueOf((Object)this.gray) + "]" : "");
            Managers.FONT.drawWithShadow(context.method_51448(), (String)coordsText, 2.0f, (float)((int)(sHeight - 12.0f)), -1);
        }
        if (this.arrayList.getValue()) {
            this.modules.clear();
            for (Module module : Managers.MODULE.getModules()) {
                if (!module.isEnabled() || !module.isDrawn()) continue;
                this.modules.add((Object)module);
            }
            if (!this.modules.isEmpty()) {
                int addY = 0;
                if (this.ordering.getValue().equals((Object)orderings.Length)) {
                    for (Module m : this.modules.stream().sorted(Comparator.comparing(s -> Float.valueOf((float)(Managers.FONT.getWidth(s.getTag() + (s.getHudInfo().isEmpty() ? "" : String.valueOf((Object)this.gray) + " [" + String.valueOf((Object)class_124.field_1068) + s.getHudInfo() + String.valueOf((Object)this.gray) + "]")) * -1.0f)))).toList()) {
                        string = m.getTag() + (m.getHudInfo().isEmpty() ? "" : String.valueOf((Object)this.gray) + " [" + String.valueOf((Object)class_124.field_1068) + m.getHudInfo() + String.valueOf((Object)this.gray) + "]");
                        x = sWidth - 2.0f - Managers.FONT.getWidth(string);
                        y = this.rendering.getValue().equals((Object)renderings.Up) ? (float)(2 + addY * 10 + (this.effectHud.getValue().equals((Object)effectHuds.Shift) && !ModuleHUD.mc.field_1724.method_6088().isEmpty() ? 25 : 0)) : sHeight - 12.0f - (float)(addY * 10);
                        Managers.FONT.drawWithShadow(context.method_51448(), string, (float)((int)x), (float)((int)y), this.modulesColor.getValue().equals((Object)modulesColors.Normal) ? ModuleColor.getColor().getRGB() : (this.modulesColor.getValue().equals((Object)modulesColors.Random) ? m.getRandomColor().getRGB() : ColorUtils.rainbow(addY).getRGB()));
                        ++addY;
                    }
                } else {
                    for (Module m : this.modules.stream().sorted(Comparator.comparing(Module::getName)).toList()) {
                        string = m.getTag() + (m.getHudInfo().isEmpty() ? "" : String.valueOf((Object)this.gray) + " [" + String.valueOf((Object)class_124.field_1068) + m.getHudInfo() + String.valueOf((Object)this.gray) + "]");
                        x = sWidth - 2.0f - Managers.FONT.getWidth(string);
                        y = this.rendering.getValue().equals((Object)renderings.Up) ? (float)(2 + addY * 10 + (this.effectHud.getValue().equals((Object)effectHuds.Shift) && !ModuleHUD.mc.field_1724.method_6088().isEmpty() ? 25 : 0)) : sHeight - 12.0f - (float)(addY * 10);
                        Managers.FONT.drawWithShadow(context.method_51448(), string, (float)((int)x), (float)((int)y), this.modulesColor.getValue().equals((Object)modulesColors.Normal) ? ModuleColor.getColor().getRGB() : (this.modulesColor.getValue().equals((Object)modulesColors.Random) ? m.getRandomColor().getRGB() : ColorUtils.rainbow(addY).getRGB()));
                        ++addY;
                    }
                }
            }
        }
        if (this.potionEffects.getValue()) {
            int[] potCount = new int[]{0};
            try {
                ModuleHUD.mc.field_1724.method_6088().forEach((effect, instance) -> {
                    String name = class_2561.method_43471((String)effect.method_40231().name()).getString();
                    int duration = instance.method_5584();
                    int amplifier = instance.method_5578() + 1;
                    int potionColor = ((class_1291)effect.comp_349()).method_5556();
                    double p1 = (double)duration % 60.0;
                    DecimalFormat format2 = new DecimalFormat("00");
                    String seconds = format2.format(p1);
                    String s = name + " " + amplifier + String.valueOf((Object)class_124.field_1068) + " " + duration / 60 + ":" + seconds;
                    Managers.FONT.drawWithShadow(context.method_51448(), s, (float)((int)(sWidth - 2.0f - Managers.FONT.getWidth(s))), (float)((int)(this.rendering.getValue().equals((Object)renderings.Down) ? (float)(2 + potCount[0] * 10 + (this.effectHud.getValue().equals((Object)effectHuds.Shift) && !ModuleHUD.mc.field_1724.method_6088().isEmpty() ? 25 : 0)) : sHeight - 12.0f - (float)(potCount[0] * 10))), new Color(potionColor).getRGB());
                    potCount[0] = potCount[0] + 1;
                    ++this.compAdd;
                });
            }
            catch (NullPointerException nullPointerException) {
                nullPointerException.printStackTrace();
            }
        }
        if (this.fps.getValue()) {
            this.comps.add((Object)(String.valueOf((Object)class_124.field_1080) + "FPS: " + String.valueOf((Object)class_124.field_1068) + mc.method_47599() + String.valueOf((Object)class_124.field_1080) + " [" + String.valueOf((Object)class_124.field_1068) + this.maxFPS + String.valueOf((Object)class_124.field_1080) + "]"));
        }
        if (this.tps.getValue()) {
            this.comps.add((Object)(String.valueOf((Object)class_124.field_1080) + "TPS: " + String.valueOf((Object)class_124.field_1068) + String.format((String)"%.2f", (Object[])new Object[]{Float.valueOf((float)TPSUtils.getTickRate())})));
        }
        if (this.ping.getValue()) {
            this.comps.add((Object)(String.valueOf((Object)class_124.field_1080) + "Ping: " + String.valueOf((Object)class_124.field_1068) + this.getPing()));
        }
        if (this.packetPS.getValue()) {
            this.comps.add((Object)(String.valueOf((Object)class_124.field_1080) + "Packets/s: " + String.valueOf((Object)class_124.field_1068) + this.packets));
        }
        if (this.speed.getValue()) {
            double posX = ModuleHUD.mc.field_1724.method_23317();
            double posZ = ModuleHUD.mc.field_1724.method_23321();
            double lastPosX = ModuleHUD.mc.field_1724.field_6014;
            double lastPosZ = ModuleHUD.mc.field_1724.field_5969;
            String speedText = String.valueOf((Object)class_124.field_1080) + "Speed: " + String.valueOf((Object)class_124.field_1068) + MathUtils.roundToPlaces(Math.sqrt((double)((posX - lastPosX) * (posX - lastPosX) + (posZ - lastPosZ) * (posZ - lastPosZ))) * 20.0, 2) + String.valueOf((Object)class_124.field_1080) + " b/s";
            this.comps.add((Object)speedText);
        }
        if (this.serverBrand.getValue()) {
            this.comps.add((Object)(String.valueOf((Object)class_124.field_1080) + "Server: " + String.valueOf((Object)class_124.field_1068) + this.getServerBrand()));
        }
        if (this.durability.getValue()) {
            this.comps.add((Object)(String.valueOf((Object)class_124.field_1080) + "Durability: " + String.valueOf((Object)class_124.field_1068) + (heldItem.method_7936() - heldItem.method_7919())));
        }
        if (!this.comps.isEmpty()) {
            for (String string : this.comps.stream().sorted(Comparator.comparing(s -> Float.valueOf((float)(Managers.FONT.getWidth((String)s) * -1.0f)))).toList()) {
                if (string.startsWith(String.valueOf((Object)this.gray) + "Durability") && !this.isItemTool(heldItem.method_7909())) continue;
                Managers.FONT.drawWithShadow(context.method_51448(), string, sWidth - Managers.FONT.getWidth(string), (float)((int)(this.rendering.getValue().equals((Object)renderings.Down) ? (float)(2 + this.compAdd * 10 + (this.effectHud.getValue().equals((Object)effectHuds.Shift) && !ModuleHUD.mc.field_1724.method_6088().isEmpty() ? 25 : 0)) : sHeight - 12.0f - (float)(this.compAdd * 10))), string.startsWith(String.valueOf((Object)this.gray) + "Durability") ? durabilityColor.getRGB() : -1);
                ++this.compAdd;
            }
        }
    }

    public boolean isItemTool(class_1792 item) {
        return item instanceof class_1738 || item == class_1802.field_22022 || item == class_1802.field_22024 || item == class_1802.field_22025 || item == class_1802.field_22023 || item == class_1802.field_22026 || item == class_1802.field_8802 || item == class_1802.field_8377 || item == class_1802.field_8556 || item == class_1802.field_8250 || item == class_1802.field_8527 || item == class_1802.field_8371 || item == class_1802.field_8403 || item == class_1802.field_8475 || item == class_1802.field_8699 || item == class_1802.field_8609 || item == class_1802.field_8845 || item == class_1802.field_8335 || item == class_1802.field_8825 || item == class_1802.field_8322 || item == class_1802.field_8303 || item == class_1802.field_8528 || item == class_1802.field_8387 || item == class_1802.field_8062 || item == class_1802.field_8776 || item == class_1802.field_8431 || item == class_1802.field_8091 || item == class_1802.field_8647 || item == class_1802.field_8406 || item == class_1802.field_8876 || item == class_1802.field_8167;
    }

    private String getDirectionName() {
        return ModuleHUD.mc.field_1724.method_5735().method_10151().substring(0, 1).toUpperCase() + ModuleHUD.mc.field_1724.method_5735().method_10151().substring(1).toLowerCase();
    }

    /*
     * Exception decompiling
     */
    private String getFacing(String input) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter$TooOptimisticMatchException
         */
        throw new IllegalStateException("Decompilation failed");
    }

    private int getPing() {
        class_640 playerInfo;
        int ping = ModuleHUD.mc.field_1724 == null || mc.method_1562() == null || mc.method_1562().method_2874(ModuleHUD.mc.field_1724.method_7334().getName()) == null ? -1 : ((playerInfo = mc.method_1562().method_2874(ModuleHUD.mc.field_1724.method_7334().getName())) != null ? playerInfo.method_2959() : -1);
        return ping;
    }

    private String getServerBrand() {
        String serverBrand;
        class_642 serverData = mc.method_1558();
        if (serverData == null) {
            serverBrand = "Vanilla";
        } else {
            serverBrand = ModuleHUD.mc.field_1724.field_3944.method_52790();
            if (serverBrand == null) {
                serverBrand = "Vanilla";
            }
        }
        return serverBrand;
    }

    public static enum effectHuds {
        Show,
        Hide,
        Shift;

    }

    public static enum orderings {
        Length,
        ABC;

    }

    public static enum modulesColors {
        Normal,
        Random,
        Rainbow;

    }

    public static enum renderings {
        Up,
        Down;

    }
}
