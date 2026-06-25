package keystrokesmod.utility;

import keystrokesmod.event.MouseEvent;
import keystrokesmod.event.RavenEventBus;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

public class Reflection {
    public static Map<String, KeyMapping> keybinds = new HashMap<>();
    public static boolean sendMessage = false;

    public static void getFields() {
    }

    public static void setKeyMappings() {
        keybinds.clear();
        for (KeyMapping keyBind : Mc.mc().options.keyMappings) {
            String keyName = keyBind.getName().replaceFirst("key\\.", "");
            keybinds.put(keyName, keyBind);
        }
    }

    public static void setButton(int button, boolean state) {
        MouseEvent event = new MouseEvent(button, state);
        RavenEventBus.post(event);
    }
}
