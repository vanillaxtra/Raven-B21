package keystrokesmod.module.impl.world;

import keystrokesmod.module.Module;
import keystrokesmod.module.setting.impl.SliderSetting;

public class Time extends Module {
    public SliderSetting time;

    public Time() {
        super("Time", category.world);
        this.registerSetting(time = new SliderSetting("Time", 6000, 0, 24000, 100));
    }

    @Override
    public String getInfo() {
        return String.valueOf((int) time.getInput());
    }

    public long getOverrideTime() {
        if (!isEnabled()) {
            return -1L;
        }
        return (long) time.getInput();
    }
}
