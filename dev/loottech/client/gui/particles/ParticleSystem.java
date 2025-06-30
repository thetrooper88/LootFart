package dev.loottech.client.gui.particles;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.loottech.api.utilities.IMinecraft;
import dev.loottech.api.utilities.render.RenderBuffers;
import dev.loottech.api.utilities.render.RenderUtils;
import dev.loottech.client.gui.particles.Particle;
import dev.loottech.client.modules.client.ModuleParticles;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_4587;
import net.minecraft.class_9801;
import org.joml.Matrix4f;

public class ParticleSystem {
    public static float SPEED = 0.1f;
    public int dist;
    private final List<Particle> particleList = new ArrayList();

    public ParticleSystem(int initAmount, int dist) {
        this.addParticles(initAmount);
        this.dist = dist;
    }

    public void addParticles(int amount) {
        if (IMinecraft.mc.method_22683() == null) {
            return;
        }
        for (int i = 0; i < amount; ++i) {
            this.particleList.add((Object)Particle.generateParticle());
        }
    }

    public void changeParticles(int amount) {
        if (IMinecraft.mc.method_22683() == null) {
            return;
        }
        this.particleList.clear();
        for (int i = 0; i < amount; ++i) {
            this.particleList.add((Object)Particle.generateParticle());
        }
    }

    public void tick(int delta) {
        for (Particle particle : this.particleList) {
            particle.tick(delta, SPEED);
        }
    }

    public void render() {
        for (Particle particle : this.particleList) {
            for (Particle particle1 : this.particleList) {
                float distance = particle.getDistanceTo(particle1);
                if (!(particle.getDistanceTo(particle1) < (float)this.dist)) continue;
                float alpha = Math.min((float)1.0f, (float)Math.min((float)1.0f, (float)(1.0f - distance / (float)this.dist)));
                RenderBuffers.preRender();
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                RenderSystem.disableCull();
                RenderSystem.lineWidth((float)ModuleParticles.INSTANCE.lineWidth.getValue().floatValue());
                class_289 tessellator = class_289.method_1348();
                class_287 bufferBuilder = tessellator.method_60827(class_293.class_5596.field_29344, class_290.field_1576);
                Matrix4f matrix = new class_4587().method_23760().method_23761();
                float red = (float)ModuleParticles.INSTANCE.color.getValue().getRed() / 255.0f;
                float green = (float)ModuleParticles.INSTANCE.color.getValue().getGreen() / 255.0f;
                float blue = (float)ModuleParticles.INSTANCE.color.getValue().getBlue() / 255.0f;
                bufferBuilder.method_22918(matrix, particle.getX(), particle.getY(), 0.0f).method_22915(red, green, blue, alpha);
                bufferBuilder.method_22918(matrix, particle1.getX(), particle1.getY(), 0.0f).method_22915(red, green, blue, alpha);
                class_286.method_43433((class_9801)bufferBuilder.method_60800());
                RenderSystem.enableCull();
                RenderSystem.disableBlend();
                RenderBuffers.postRender();
            }
            RenderUtils.drawCircle(particle.getX(), particle.getY(), ModuleParticles.INSTANCE.size.getValue().floatValue(), ModuleParticles.INSTANCE.color.getValue());
        }
    }
}
