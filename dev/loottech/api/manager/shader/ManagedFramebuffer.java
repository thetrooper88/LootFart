package dev.loottech.api.manager.shader;

import net.minecraft.class_276;

public interface ManagedFramebuffer {
    public class_276 getFramebuffer();

    public void beginWrite(boolean var1);

    public void draw();

    public void draw(int var1, int var2, boolean var3);

    public void clear();

    public void clear(boolean var1);
}
