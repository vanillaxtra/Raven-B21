package keystrokesmod.module.impl.movement;

import keystrokesmod.clickgui.ClickGui;
import keystrokesmod.module.Module;
import keystrokesmod.module.setting.impl.ButtonSetting;
import keystrokesmod.module.setting.impl.SliderSetting;
import keystrokesmod.utility.Mc;
import keystrokesmod.utility.TimerHelper;
import keystrokesmod.utility.Utils;

public class Timer extends Module {
    private SliderSetting speed;
    private ButtonSetting strafeOnly;

    public Timer() {
        super("Timer", category.movement, 0);
        this.registerSetting(speed = new SliderSetting("Speed", 1.0D, 0.5D, 2.5D, 0.01D));
        this.registerSetting(strafeOnly = new ButtonSetting("Strafe only", false));
    }

    @Override
    public String getInfo() {
        return Utils.asWholeNum(speed.getInput());
    }

    public void onUpdate() {
        if (!Mc.nullCheck()) {
            return;
        }
        if (mc.screen instanceof ClickGui) {
            Utils.resetTimer();
            return;
        }
        if (strafeOnly.isToggled() && Utils.getMovementSideways() == 0) {
            Utils.resetTimer();
            return;
        }
        TimerHelper.speed = (float) speed.getInput();
    }

    public void onDisable() {
        Utils.resetTimer();
    }
}
