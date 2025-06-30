package dev.loottech.api.utilities.render;

import net.minecraft.class_4668;
import org.lwjgl.opengl.GL11;

protected static class RenderLayersClient.DepthTest
extends class_4668.class_4672 {
    public RenderLayersClient.DepthTest() {
        super("depth_test", 519);
    }

    public void method_23516() {
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDepthFunc((int)514);
    }

    public void method_23518() {
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glDepthFunc((int)515);
        GL11.glDepthFunc((int)519);
    }
}
