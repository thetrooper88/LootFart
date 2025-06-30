package dev.loottech.client.modules.visuals;

import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.client.modules.client.ModuleColor;
import dev.loottech.client.values.impl.ValueBoolean;
import dev.loottech.client.values.impl.ValueCategory;
import dev.loottech.client.values.impl.ValueColor;
import dev.loottech.client.values.impl.ValueNumber;

@RegisterModule(name="NoRender", tag="NoRender", description="dont render some things.", category=Module.Category.VISUALS)
public class NoRender
extends Module {
    public ValueBoolean arrows = new ValueBoolean("Arrows", "Arrows", "Dosen't render arrows.", true);
    public ValueBoolean weather = new ValueBoolean("Weather", "Weather", "Dosen't render weather.", true);
    public ValueBoolean explosions = new ValueBoolean("explosions", "Explosions", "Dosen't render explosions.", true);
    public ValueBoolean blockOverlay = new ValueBoolean("blockOverlay", "BlockOverlay", "Dosen't render block Overlay.", true);
    public ValueBoolean totemOverlay = new ValueBoolean("TotemOverlay", "TotemOverlay", "Dosen't render totem Overlay.", true);
    public ValueBoolean liquidOverlay = new ValueBoolean("LiquidOverlay", "LiquidOverlay", "Dosen't render liquid Overlays.", true);
    public ValueBoolean fireOverlay = new ValueBoolean("FireOverlay", "FireOverlay", "Dosen't render Fire Overlays.", true);
    public ValueBoolean bossBar = new ValueBoolean("BossBar", "BossBor", "Dosen't render boss bar.", true);
    public ValueBoolean statusOverlay = new ValueBoolean("StatusOverlay", "StatusOverlay", "Dosen't render status effects on HUD (potions on top right).", true);
    public ValueBoolean mainhandTotem = new ValueBoolean("MainHand Totem", "Sets your mainhand totem to a crystal *client-side*", false);
    public ValueCategory fogCategory = new ValueCategory("Fog", "Fog rendering options");
    public ValueBoolean noFog = new ValueBoolean("NoFog", "NoFog", "Dosen't render any fog effects.", this.fogCategory, true);
    public ValueNumber fogStart = new ValueNumber("FogStart", "FogStart", "", this.fogCategory, (Number)Double.valueOf((double)1.0), (Number)Double.valueOf((double)0.1), (Number)Double.valueOf((double)5.0));
    public ValueNumber fogEnd = new ValueNumber("FogEnd", "FogEnd", "", this.fogCategory, (Number)Double.valueOf((double)1.25), (Number)Double.valueOf((double)0.2), (Number)Double.valueOf((double)20.0));
    public ValueColor fogColor = new ValueColor("FogColor", "FogColor", "", this.fogCategory, ModuleColor.getColor(), false, true);
}
