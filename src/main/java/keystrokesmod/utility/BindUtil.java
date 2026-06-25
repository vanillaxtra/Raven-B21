package keystrokesmod.utility;

import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

public final class BindUtil {
    private BindUtil() {}

    public static boolean isBindDown(int keycode) {
        if (keycode == 0) {
            return false;
        }
        Minecraft mc = Mc.mc();
        if (mc.getWindow() == null) {
            return false;
        }
        long handle = mc.getWindow().handle();
        if (keycode == 1069 || keycode == 1070) {
            return false;
        }
        if (keycode >= 1000) {
            return GLFW.glfwGetMouseButton(handle, keycode - 1000) == GLFW.GLFW_PRESS;
        }
        return GLFW.glfwGetKey(handle, keycode) == GLFW.GLFW_PRESS;
    }
}
