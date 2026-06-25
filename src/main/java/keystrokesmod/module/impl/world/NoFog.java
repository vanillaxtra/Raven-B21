package keystrokesmod.module.impl.world;

import keystrokesmod.module.Module;
import keystrokesmod.module.setting.impl.ButtonSetting;

public class NoFog extends Module {
    public ButtonSetting noFog;

    public NoFog() {
        super("NoFog", category.world);
        this.registerSetting(noFog = new ButtonSetting("No fog", true));
    }

    public boolean shouldRemoveFog() {
        return isEnabled() && noFog.isToggled();
    }
}
