package keystrokesmod.utility;

import keystrokesmod.event.MouseEvent;
import keystrokesmod.event.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

public class CPSCalculator {
    public static long LL;

    @SubscribeEvent
    public void onMouse(MouseEvent event) {
        if (!event.buttonState) {
            return;
        }
    }

    public static boolean isMouseDown(int button) {
        if (!Mc.nullCheck() || Mc.mc().getWindow() == null) {
            return false;
        }
        return GLFW.glfwGetMouseButton(Mc.mc().getWindow().handle(), button) == GLFW.GLFW_PRESS;
    }

    public static int f() {
        return 0;
    }

    public static int i() {
        return 0;
    }

    public static void aL() {
    }

    public static void aR() {
    }
}
