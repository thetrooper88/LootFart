package dev.loottech.api.manager.miscellaneous;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.loottech.api.manager.event.EventListener;
import dev.loottech.api.utilities.Util;
import dev.loottech.client.events.LightmapInitEvent;
import dev.loottech.client.events.LightmapTickEvent;
import dev.loottech.client.events.LightmapUpdateEvent;
import net.minecraft.class_1011;
import net.minecraft.class_1043;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1309;
import net.minecraft.class_2874;
import net.minecraft.class_2960;
import net.minecraft.class_3532;
import net.minecraft.class_638;
import net.minecraft.class_757;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class LightmapManager
implements AutoCloseable,
Util,
EventListener {
    private class_1043 texture;
    private class_1011 image;
    private class_2960 textureIdentifier;
    private boolean dirty;
    private float flickerIntensity;
    private class_757 renderer;

    private static void clamp(Vector3f vec) {
        vec.set(class_3532.method_15363((float)vec.x, (float)0.0f, (float)1.0f), class_3532.method_15363((float)vec.y, (float)0.0f, (float)1.0f), class_3532.method_15363((float)vec.z, (float)0.0f, (float)1.0f));
    }

    public static float getBrightness(class_2874 type, int lightLevel) {
        float f = (float)lightLevel / 15.0f;
        float g = f / (4.0f - 3.0f * f);
        return class_3532.method_16439((float)type.comp_656(), (float)g, (float)1.0f);
    }

    public static int pack(int block, int sky) {
        return block << 4 | sky << 20;
    }

    @Override
    public void onLightmapInit(LightmapInitEvent event) {
        this.renderer = LightmapManager.mc.field_1773;
        this.texture = new class_1043(16, 16, false);
        this.textureIdentifier = mc.method_1531().method_4617("loottech_light_map", this.texture);
        this.image = this.texture.method_4525();
        for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < 16; ++j) {
                this.image.method_4305(j, i, -1);
            }
        }
        this.texture.method_4524();
    }

    @Override
    public void onLightmapTick(LightmapTickEvent event) {
        this.flickerIntensity += (float)((Math.random() - Math.random()) * Math.random() * Math.random() * 0.1);
        this.flickerIntensity *= 0.9f;
        this.dirty = true;
    }

    @Override
    public void onLightmapUpdate(LightmapUpdateEvent event) {
        if (this.renderer == null || this.texture == null || this.image == null) {
            return;
        }
        if (this.dirty) {
            this.dirty = false;
            class_638 clientWorld = LightmapManager.mc.field_1687;
            if (clientWorld != null) {
                float f = clientWorld.method_23783(1.0f);
                float g = clientWorld.method_23789() > 0 ? 1.0f : f * 0.95f + 0.05f;
                float h = ((Double)LightmapManager.mc.field_1690.method_42472().method_41753()).floatValue();
                float i = this.getDarknessFactor(event.getTickDelta()) * h;
                float j = this.getDarkness((class_1309)LightmapManager.mc.field_1724, i, event.getTickDelta()) * h;
                float k = LightmapManager.mc.field_1724.method_3140();
                float l = LightmapManager.mc.field_1724.method_6059(class_1294.field_5925) ? class_757.method_3174((class_1309)LightmapManager.mc.field_1724, (float)event.getTickDelta()) : (k > 0.0f && LightmapManager.mc.field_1724.method_6059(class_1294.field_5927) ? k : 0.0f);
                Vector3f vector3f = new Vector3f(f, f, 1.0f).lerp((Vector3fc)new Vector3f(1.0f, 1.0f, 1.0f), 0.35f);
                float m = this.flickerIntensity + 1.5f;
                Vector3f vector3f2 = new Vector3f();
                for (int n = 0; n < 16; ++n) {
                    for (int o = 0; o < 16; ++o) {
                        float v;
                        Vector3f vector3f4;
                        float u;
                        float p = LightmapManager.getBrightness(clientWorld.method_8597(), n) * g;
                        float q = LightmapManager.getBrightness(clientWorld.method_8597(), o) * m;
                        float s = q * ((q * 0.6f + 0.4f) * 0.6f + 0.4f);
                        float t = q * (q * q * 0.6f + 0.4f);
                        vector3f2.set(q, s, t);
                        boolean bl = clientWorld.method_28103().method_28114();
                        if (bl) {
                            vector3f2.lerp((Vector3fc)new Vector3f(0.99f, 1.12f, 1.0f), 0.25f);
                            LightmapManager.clamp(vector3f2);
                        } else {
                            Vector3f vector3f3 = new Vector3f((Vector3fc)vector3f).mul(p);
                            vector3f2.add((Vector3fc)vector3f3);
                            vector3f2.lerp((Vector3fc)new Vector3f(0.75f, 0.75f, 0.75f), 0.04f);
                            if (this.renderer.method_3195(event.getTickDelta()) > 0.0f) {
                                u = this.renderer.method_3195(event.getTickDelta());
                                vector3f4 = new Vector3f((Vector3fc)vector3f2).mul(0.7f, 0.6f, 0.6f);
                                vector3f2.lerp((Vector3fc)vector3f4, u);
                            }
                        }
                        if (l > 0.0f && (v = Math.max((float)vector3f2.x(), (float)Math.max((float)vector3f2.y(), (float)vector3f2.z()))) < 1.0f) {
                            u = 1.0f / v;
                            vector3f4 = new Vector3f((Vector3fc)vector3f2).mul(u);
                            vector3f2.lerp((Vector3fc)vector3f4, l);
                        }
                        if (!bl) {
                            if (j > 0.0f) {
                                vector3f2.add(-j, -j, -j);
                            }
                            LightmapManager.clamp(vector3f2);
                        }
                        float v2 = ((Double)LightmapManager.mc.field_1690.method_42473().method_41753()).floatValue();
                        Vector3f vector3f5 = new Vector3f(this.easeOutQuart(vector3f2.x), this.easeOutQuart(vector3f2.y), this.easeOutQuart(vector3f2.z));
                        vector3f2.lerp((Vector3fc)vector3f5, Math.max((float)0.0f, (float)(v2 - i)));
                        vector3f2.lerp((Vector3fc)new Vector3f(0.75f, 0.75f, 0.75f), 0.04f);
                        LightmapManager.clamp(vector3f2);
                        vector3f2.mul(255.0f);
                        int x = (int)vector3f2.x();
                        int y = (int)vector3f2.y();
                        int z = (int)vector3f2.z();
                        this.image.method_4305(o, n, 0xFF000000 | z << 16 | y << 8 | x);
                    }
                }
                this.texture.method_4524();
            }
        }
    }

    public void close() {
        this.texture.close();
    }

    public void disable() {
        RenderSystem.setShaderTexture((int)2, (int)0);
    }

    public void enable() {
        if (this.textureIdentifier == null) {
            return;
        }
        RenderSystem.setShaderTexture((int)2, (class_2960)this.textureIdentifier);
        mc.method_1531().method_22813(this.textureIdentifier);
        RenderSystem.texParameter((int)3553, (int)10241, (int)9729);
        RenderSystem.texParameter((int)3553, (int)10240, (int)9729);
    }

    private float getDarknessFactor(float delta) {
        class_1293 statusEffectInstance = LightmapManager.mc.field_1724.method_6112(class_1294.field_38092);
        return statusEffectInstance != null ? statusEffectInstance.method_55653((class_1309)LightmapManager.mc.field_1724, delta) : 0.0f;
    }

    private float getDarkness(class_1309 entity, float factor, float delta) {
        float f = 0.45f * factor;
        return Math.max((float)0.0f, (float)(class_3532.method_15362((float)(((float)entity.field_6012 - delta) * (float)Math.PI * 0.025f)) * f));
    }

    private float easeOutQuart(float x) {
        float f = 1.0f - x;
        return 1.0f - f * f * f * f;
    }
}
