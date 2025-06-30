package dev.loottech.api.utilities;

import dev.loottech.api.utilities.Util;
import org.lwjgl.glfw.GLFW;

public class InputUtils {
    public static boolean isKeyPressed(int keyCode) {
        if (keyCode == 0) {
            return false;
        }
        if (keyCode >= 0 && keyCode <= 7) {
            return GLFW.glfwGetMouseButton((long)Util.mc.method_22683().method_4490(), (int)keyCode) == 1;
        }
        return GLFW.glfwGetKey((long)Util.mc.method_22683().method_4490(), (int)keyCode) == 1;
    }
}
