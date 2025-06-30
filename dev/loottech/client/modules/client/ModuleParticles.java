package dev.loottech.client.modules.client;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.client.events.ClientEvent;
import dev.loottech.client.events.Render2DEvent;
import dev.loottech.client.gui.particles.ParticleSystem;
import dev.loottech.client.values.Value;
import dev.loottech.client.values.impl.ValueColor;
import dev.loottech.client.values.impl.ValueNumber;
import java.awt.Color;

@RegisterModule(name="Particles", description="Render particles in gui screens.", category=Module.Category.CLIENT, drawn=false)
public class ModuleParticles
extends Module {
    public static ModuleParticles INSTANCE;
    public ValueColor color = new ValueColor("ParticleColor", "Color", "", new Color(255, 255, 255));
    public ValueNumber size = new ValueNumber("Size", "Size", "", (Number)Float.valueOf((float)1.0f), (Number)Float.valueOf((float)0.5f), (Number)Float.valueOf((float)5.0f));
    public ValueNumber lineWidth = new ValueNumber("LineWidth", "Line Width", "", (Number)Double.valueOf((double)2.0), (Number)Double.valueOf((double)1.0), (Number)Double.valueOf((double)3.0));
    public ValueNumber amount = new ValueNumber("Population", "Population", "", (Number)Integer.valueOf((int)100), (Number)Integer.valueOf((int)50), (Number)Integer.valueOf((int)400));
    public ValueNumber radius = new ValueNumber("Radius", "Radius", "", (Number)Integer.valueOf((int)100), (Number)Integer.valueOf((int)50), (Number)Integer.valueOf((int)300));
    public ValueNumber speed = new ValueNumber("Speed", "Speed", "", (Number)Float.valueOf((float)0.1f), (Number)Float.valueOf((float)0.1f), (Number)Float.valueOf((float)10.0f));
    public ValueNumber delta = new ValueNumber("Delta", "Delta", "", (Number)Integer.valueOf((int)1), (Number)Integer.valueOf((int)1), (Number)Integer.valueOf((int)10));
    boolean updateParticles = false;
    private ParticleSystem ps;

    public ModuleParticles() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        this.ps = new ParticleSystem(this.amount.getValue().intValue(), this.radius.getValue().intValue());
        this.ps.tick(this.delta.getValue().intValue());
        this.ps.dist = this.radius.getValue().intValue();
        ParticleSystem.SPEED = (float)this.speed.getValue().doubleValue();
    }

    @Override
    public void onClient(ClientEvent event) {
        if (this.nullCheck()) {
            return;
        }
        Value value = event.getValue();
        if (value != null && value == this.amount) {
            this.updateParticles = true;
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.nullCheck()) {
            return;
        }
        if (this.updateParticles) {
            this.ps.changeParticles(this.amount.getValue().intValue());
            this.updateParticles = false;
        }
        this.ps.tick(this.delta.getValue().intValue());
        this.ps.dist = this.radius.getValue().intValue();
        ParticleSystem.SPEED = (float)this.speed.getValue().doubleValue();
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        if (ModuleParticles.mc.field_1755 == Managers.CLICK_GUI) {
            this.ps.render();
        }
    }
}
