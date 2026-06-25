package keystrokesmod.utility;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.multiplayer.ClientLevel;

public final class Mc {
    private Mc() {}

    public static Minecraft mc() {
        return Minecraft.getInstance();
    }

    public static LocalPlayer player() {
        return mc().player;
    }

    public static ClientLevel level() {
        return mc().level;
    }

    public static ClientLevel world() {
        return level();
    }

    public static boolean nullCheck() {
        return mc().player != null && mc().level != null;
    }
}
