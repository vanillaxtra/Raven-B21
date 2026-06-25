package keystrokesmod.module.impl.player;

import keystrokesmod.module.Module;
import keystrokesmod.module.setting.impl.ButtonSetting;
import keystrokesmod.utility.Mc;

public class SafeWalk extends Module {
    public ButtonSetting air;
    public static boolean safeWalk;

    public SafeWalk() {
        super("SafeWalk", category.player, 0);
        this.registerSetting(air = new ButtonSetting("Air", false));
    }

    public void onUpdate() {
        safeWalk = Mc.nullCheck() && isEnabled();
    }

    public void onDisable() {
        safeWalk = false;
    }
}
