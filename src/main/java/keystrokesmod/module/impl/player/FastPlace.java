package keystrokesmod.module.impl.player;

import keystrokesmod.module.Module;
import keystrokesmod.module.setting.impl.ButtonSetting;
import keystrokesmod.module.setting.impl.SliderSetting;
import keystrokesmod.utility.Mc;

public class FastPlace extends Module {
    private SliderSetting delay;
    public ButtonSetting blocksOnly;

    public FastPlace() {
        super("FastPlace", category.player, 0);
        this.registerSetting(delay = new SliderSetting("Delay", 0, 0, 4, 1));
        this.registerSetting(blocksOnly = new ButtonSetting("Blocks only", true));
        this.closetModule = true;
    }

    public int getPlaceDelay() {
        return isEnabled() ? (int) delay.getInput() : 4;
    }
}
