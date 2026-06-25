package keystrokesmod.module.impl.player;

import keystrokesmod.module.Module;
import keystrokesmod.module.setting.impl.SliderSetting;
import keystrokesmod.utility.Mc;

public class FastMine extends Module {
    public SliderSetting speed;

    public FastMine() {
        super("FastMine", category.player, 0);
        this.registerSetting(speed = new SliderSetting("Speed", 1.2, 1.0, 2.0, 0.05));
        this.closetModule = true;
    }

    public float getMultiplier() {
        return isEnabled() ? (float) speed.getInput() : 1.0f;
    }
}
