package dev.loottech.api.manager.module;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.event.EventListener;
import dev.loottech.api.manager.module.Module;
import dev.loottech.api.utilities.IMinecraft;
import dev.loottech.client.events.LoginEvent;
import dev.loottech.client.events.LogoutEvent;
import dev.loottech.client.events.MovementPacketsEvent;
import dev.loottech.client.events.Render2DEvent;
import dev.loottech.client.events.Render3DEvent;
import dev.loottech.client.events.TickEvent;
import dev.loottech.client.modules.client.AntiCheat;
import dev.loottech.client.modules.client.FastLatency;
import dev.loottech.client.modules.client.Font;
import dev.loottech.client.modules.client.ModuleColor;
import dev.loottech.client.modules.client.ModuleCommands;
import dev.loottech.client.modules.client.ModuleGUI;
import dev.loottech.client.modules.client.ModuleHUDEditor;
import dev.loottech.client.modules.client.ModuleParticles;
import dev.loottech.client.modules.client.Notifications;
import dev.loottech.client.modules.client.Rotations;
import dev.loottech.client.modules.client.VanillaFont;
import dev.loottech.client.modules.combat.AimBot;
import dev.loottech.client.modules.combat.AutoCrystal;
import dev.loottech.client.modules.combat.AutoMine;
import dev.loottech.client.modules.combat.AutoXP;
import dev.loottech.client.modules.combat.CrawlTrap;
import dev.loottech.client.modules.combat.Criticals;
import dev.loottech.client.modules.combat.KeepSprint;
import dev.loottech.client.modules.combat.KillAura;
import dev.loottech.client.modules.combat.ModuleOffhand;
import dev.loottech.client.modules.combat.SelfTrap;
import dev.loottech.client.modules.combat.Surround;
import dev.loottech.client.modules.miscellaneous.AutoBuilder;
import dev.loottech.client.modules.miscellaneous.AutoSpawner;
import dev.loottech.client.modules.miscellaneous.AutoTrialVault;
import dev.loottech.client.modules.miscellaneous.ChorusEscape;
import dev.loottech.client.modules.miscellaneous.FakePlayer;
import dev.loottech.client.modules.miscellaneous.GodMode;
import dev.loottech.client.modules.miscellaneous.InventoryTweaks;
import dev.loottech.client.modules.miscellaneous.KillSound;
import dev.loottech.client.modules.miscellaneous.ModuleMiddleClick;
import dev.loottech.client.modules.movement.ElytraFlight;
import dev.loottech.client.modules.movement.ModuleSprint;
import dev.loottech.client.modules.movement.ModuleVelocity;
import dev.loottech.client.modules.movement.NoFall;
import dev.loottech.client.modules.movement.NoJumpDelay;
import dev.loottech.client.modules.movement.NoSlow;
import dev.loottech.client.modules.movement.Speed;
import dev.loottech.client.modules.player.ChestSwap;
import dev.loottech.client.modules.player.FastUse;
import dev.loottech.client.modules.player.ModuleMultiTask;
import dev.loottech.client.modules.player.NoClimb;
import dev.loottech.client.modules.player.NoRotate;
import dev.loottech.client.modules.player.PearlPhase;
import dev.loottech.client.modules.player.Replenish;
import dev.loottech.client.modules.player.Scaffold;
import dev.loottech.client.modules.player.ScientifySexDupe;
import dev.loottech.client.modules.player.Swing;
import dev.loottech.client.modules.visuals.ActivatedSpawnerESP;
import dev.loottech.client.modules.visuals.BreakIndicators;
import dev.loottech.client.modules.visuals.Chams;
import dev.loottech.client.modules.visuals.CrystalModifier;
import dev.loottech.client.modules.visuals.DeathEffects;
import dev.loottech.client.modules.visuals.ESP;
import dev.loottech.client.modules.visuals.FreeLook;
import dev.loottech.client.modules.visuals.Freecam;
import dev.loottech.client.modules.visuals.Fullbright;
import dev.loottech.client.modules.visuals.LogoutSpots;
import dev.loottech.client.modules.visuals.ModuleCrosshair;
import dev.loottech.client.modules.visuals.Nametags;
import dev.loottech.client.modules.visuals.NoRender;
import dev.loottech.client.modules.visuals.PhaseESP;
import dev.loottech.client.modules.visuals.Shaders;
import dev.loottech.client.modules.visuals.ShulkerTooltips;
import dev.loottech.client.modules.visuals.SkyBox;
import dev.loottech.client.modules.visuals.ViewModel;
import dev.loottech.client.modules.visuals.VisualRange;
import dev.loottech.client.modules.visuals.Waypoints;
import dev.loottech.client.modules.visuals.Zoom;
import dev.loottech.client.values.Value;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;
import org.lwjgl.opengl.GL11;

public class ModuleManager
implements IMinecraft,
EventListener {
    private final ArrayList<Module> modules;
    private final Map<Class<? extends Module>, Module> moduleInstances = new Reference2ReferenceOpenHashMap();

    public ModuleManager() {
        Managers.EVENT.register(this);
        this.modules = new ArrayList();
        this.register(new AntiCheat());
        this.register(new ModuleColor());
        this.register(new ModuleCommands());
        this.register(new ModuleGUI());
        this.register(new ModuleHUDEditor());
        this.register(new ModuleParticles());
        this.register(new FastLatency());
        this.register(new Font());
        this.register(new Notifications());
        this.register(new Rotations());
        this.register(new VanillaFont());
        this.register(new ModuleOffhand());
        this.register(new Surround());
        this.register(new AutoCrystal());
        this.register(new AimBot());
        this.register(new AutoMine());
        this.register(new SelfTrap());
        this.register(new KillAura());
        this.register(new AutoXP());
        this.register(new KeepSprint());
        this.register(new CrawlTrap());
        this.register(new Criticals());
        this.register(new FakePlayer());
        this.register(new GodMode());
        this.register(new ModuleMiddleClick());
        this.register(new AutoSpawner());
        this.register(new ChorusEscape());
        this.register(new InventoryTweaks());
        this.register(new KillSound());
        this.register(new AutoTrialVault());
        this.register(new ModuleSprint());
        this.register(new ModuleVelocity());
        this.register(new NoSlow());
        this.register(new NoJumpDelay());
        this.register(new Speed());
        this.register(new NoFall());
        this.register(new AutoBuilder());
        this.register(new ElytraFlight());
        this.register(new ModuleMultiTask());
        this.register(new ChestSwap());
        this.register(new ScientifySexDupe());
        this.register(new PearlPhase());
        this.register(new Swing());
        this.register(new NoRotate());
        this.register(new NoClimb());
        this.register(new FastUse());
        this.register(new Replenish());
        this.register(new Scaffold());
        this.register(new ModuleCrosshair());
        this.register(new NoRender());
        this.register(new Fullbright());
        this.register(new DeathEffects());
        this.register(new PhaseESP());
        this.register(new VisualRange());
        this.register(new Nametags());
        this.register(new ViewModel());
        this.register(new SkyBox());
        this.register(new Shaders());
        this.register(new CrystalModifier());
        this.register(new Waypoints());
        this.register(new LogoutSpots());
        this.register(new ShulkerTooltips());
        this.register(new ESP());
        this.register(new ActivatedSpawnerESP());
        this.register(new Freecam());
        this.register(new FreeLook());
        this.register(new Chams());
        this.register(new Zoom());
        this.register(new BreakIndicators());
        this.modules.sort(Comparator.comparing(Module::getName));
    }

    public void register(Module module) {
        try {
            for (Field field : module.getClass().getDeclaredFields()) {
                if (!Value.class.isAssignableFrom(field.getType())) continue;
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                module.getValues().add((Object)((Value)field.get((Object)module)));
            }
            module.getValues().add((Object)module.tag);
            module.getValues().add((Object)module.chatNotify);
            module.getValues().add((Object)module.drawn);
            module.getValues().add((Object)module.holdBind);
            module.getValues().add((Object)module.bind);
            this.modules.add((Object)module);
            this.moduleInstances.put((Object)module.getClass(), (Object)module);
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Module> getModules() {
        return this.modules;
    }

    public ArrayList<Module> getModules(Module.Category category) {
        return (ArrayList)this.modules.stream().filter(mm -> mm.getCategory().equals((Object)category)).collect(Collectors.toList());
    }

    public boolean isModuleEnabled(String name) {
        Module module = (Module)this.modules.stream().filter(mm -> mm.getName().equals((Object)name)).findFirst().orElse(null);
        if (module != null) {
            return module.isEnabled();
        }
        return false;
    }

    public <T extends Module> boolean isModuleEnabled(Class<T> klass) {
        return ((Module)Managers.MODULE.getInstance(klass)).isEnabled();
    }

    public <T extends Module> T getInstance(Class<T> klass) {
        return (T)((Module)this.moduleInstances.get(klass));
    }

    public Module getInstance(String name) {
        for (Module module : this.moduleInstances.values()) {
            if (!module.name.equalsIgnoreCase(name)) continue;
            return module;
        }
        return null;
    }

    @Override
    public void onTick(TickEvent event) {
        if (ModuleManager.mc.field_1724 != null && ModuleManager.mc.field_1687 != null) {
            this.modules.stream().filter(Module::isEnabled).forEach(Module::onTick);
            this.modules.stream().filter(Module::isEnabled).forEach(Module::onTickTwo);
        }
    }

    @Override
    public void onMovementPackets(MovementPacketsEvent event) {
        if (ModuleManager.mc.field_1724 != null && ModuleManager.mc.field_1687 != null) {
            this.modules.stream().filter(Module::isEnabled).forEach(Module::onUpdate);
        }
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        this.modules.stream().filter(Module::isEnabled).forEach(m -> m.onRender2D(event));
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        GL11.glBlendFunc((int)770, (int)771);
        RenderSystem.defaultBlendFunc();
        RenderSystem.lineWidth((float)1.0f);
        RenderSystem.depthMask((boolean)false);
        RenderSystem.enableBlend();
        RenderSystem.disableDepthTest();
        RenderSystem.disableCull();
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        this.modules.stream().filter(Module::isEnabled).forEach(mm -> mm.onRender3D(event));
        RenderSystem.enableCull();
        RenderSystem.depthMask((boolean)true);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    @Override
    public void onLogin(LoginEvent event) {
        this.modules.stream().filter(Module::isEnabled).forEach(Module::onLogin);
    }

    @Override
    public void onLogout(LogoutEvent event) {
        this.modules.stream().filter(Module::isEnabled).forEach(Module::onLogout);
    }
}
