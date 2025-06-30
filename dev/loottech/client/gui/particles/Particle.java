package dev.loottech.client.gui.particles;

import dev.loottech.api.utilities.IMinecraft;
import dev.loottech.api.utilities.MathUtils;
import dev.loottech.api.utilities.Vector2f;
import dev.loottech.client.modules.client.ModuleParticles;
import java.util.Random;

public class Particle
implements IMinecraft {
    private static final Random random = new Random();
    private Vector2f velocity;
    private Vector2f pos;
    private float size;
    private float alpha;

    public Particle(Vector2f velocity, float x, float y, float size) {
        this.velocity = velocity;
        this.pos = new Vector2f(x, y);
        this.size = size;
    }

    public static Particle generateParticle() {
        Vector2f velocity = new Vector2f((float)(Math.random() * 2.0 - 1.0), (float)(Math.random() * 2.0 - 1.0));
        float x = random.nextInt(mc.method_22683().method_4480());
        float y = random.nextInt(mc.method_22683().method_4507());
        return new Particle(velocity, x, y, ModuleParticles.INSTANCE.size.getValue().floatValue());
    }

    public float getAlpha() {
        return this.alpha;
    }

    public Vector2f getVelocity() {
        return this.velocity;
    }

    public void setVelocity(Vector2f velocity) {
        this.velocity = velocity;
    }

    public float getX() {
        return this.pos.getX();
    }

    public void setX(float x) {
        this.pos.setX(x);
    }

    public float getY() {
        return this.pos.getY();
    }

    public void setY(float y) {
        this.pos.setY(y);
    }

    public float getSize() {
        return this.size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public void tick(int delta, float speed) {
        if (mc.method_22683() == null) {
            return;
        }
        this.pos.x += this.velocity.getX() * (float)delta * speed;
        this.pos.y += this.velocity.getY() * (float)delta * speed;
        if (this.alpha < 255.0f) {
            this.alpha += 0.05f * (float)delta;
        }
        if (this.pos.getX() > (float)mc.method_22683().method_4480()) {
            this.pos.setX(0.0f);
        }
        if (this.pos.getX() < 0.0f) {
            this.pos.setX(mc.method_22683().method_4480());
        }
        if (this.pos.getY() > (float)mc.method_22683().method_4507()) {
            this.pos.setY(0.0f);
        }
        if (this.pos.getY() < 0.0f) {
            this.pos.setY(mc.method_22683().method_4507());
        }
    }

    public float getDistanceTo(Particle particle1) {
        return this.getDistanceTo(particle1.getX(), particle1.getY());
    }

    public float getDistanceTo(float x, float y) {
        return (float)MathUtils.distance(this.getX(), this.getY(), x, y);
    }
}
